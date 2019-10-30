package com.woojeong.global.ifriend;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.util.AsyncListUtil;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.woojeong.global.ifriend.DTO.Child;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static com.woojeong.global.ifriend.GlobalApplication.setDarkMode;

public class ReservationActivity extends AppCompatActivity {
    private static String TAG = "ReservationActivity";
    Context context;
    InputMethodManager ipmm;
    AQuery aQuery = null;
    OneBtnDialog oneBtnDialog;
    OkDialog okDialog;

    DatePickerDialog datePickerDialog;
    DatePicker datePicker1, datePicker2;
    ArrayList<String> reserveDate;
    ArrayList<String> noDate2;
    ArrayList<String> dateList;

    FrameLayout back_con, sitter_photo_con, start_date_con, finish_date_con, start_time_con, finish_time_con, go_reservation_con;
    ImageView back, sitter_photo, start_date_down, finish_date_down, start_time_down, finish_time_down, add_child_icon;
    LinearLayout date_choice_con, time_choice_con, total_time_con, child_con, add_child_con, calculation_con, total_amount_con;
    TextView sitter_name, sitter_age, sitter_location, start_date, finish_date, start_time, finish_time, total_time_interval, add_child, total_time, total_amount, go_reservation;
    SpinnerReselect spinner_start_time, spinner_finish_time;
    EditText request, total_children;

    ListView child_list;
    ChildAdapter childAdapter;
    ArrayList<Child> data;

    String getFriend, getName, getAge, getNoDate, getWeekdayPrice, getWeekendPrice;
    String[] noDate = new String[]{"", ""};
    int childCnt;
    int getChildCnt;
    Intent getIntent;
    String getTime1 = "0", getTime2 = "1";
    String[] time = new String[]{"00:00 AM", "01:00 AM", "02:00 AM", "03:00 AM", "04:00 AM", "05:00 AM", "06:00 AM", "07:00 AM", "08:00 AM", "09:00 AM", "10:00 AM", "11:00 AM", "12:00 PM",
            "01:00 PM", "02:00 PM", "03:00 PM", "04:00 PM", "05:00 PM", "06:00 PM", "07:00 PM", "08:00 PM", "09:00 PM", "10:00 PM", "11:00 PM"};
    String[] timeValue = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23"};
    int getTotalTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#ffffff"));
            window.setBackgroundDrawable(null);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                setDarkMode(ReservationActivity.this, true);
            }
        }

        getIntent = getIntent();
        getFriend = getIntent.getStringExtra("friend");
        getName = getIntent.getStringExtra("name");
        getAge = getIntent.getStringExtra("age");
        getChildCnt = getIntent.getIntExtra("child_count", 0);
        getWeekdayPrice = getIntent.getStringExtra("weekday_price");
        getWeekendPrice = getIntent.getStringExtra("weekend_price");



        if(!"".equals(getIntent.getStringExtra("no_date")) || null != getIntent.getStringExtra("no_date")){

            getNoDate = getIntent.getStringExtra("no_date");

            Log.i(TAG, " getNoDate " + getNoDate);

            if(getNoDate!= null){
                noDate = getNoDate.split(",");
            }

        }


        for (int i = 0; i < noDate.length; i++) {
            Log.i(TAG, " NO DATE " + noDate[i]);
        }
        reserveDate = new ArrayList<String>();

        context = this;
        aQuery = new AQuery(this);
        ipmm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        back_con = findViewById(R.id.back_con);
        back = findViewById(R.id.back);
        back_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back.callOnClick();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        sitter_photo_con = findViewById(R.id.sitter_photo_con);
        start_date_con = findViewById(R.id.start_date_con);
        finish_date_con = findViewById(R.id.finish_date_con);
        start_time_con = findViewById(R.id.start_time_con);
        finish_time_con = findViewById(R.id.finish_time_con);
        go_reservation_con = findViewById(R.id.go_reservation_con);

        sitter_photo = findViewById(R.id.sitter_photo);
        start_date_down = findViewById(R.id.start_date_down);
        finish_date_down = findViewById(R.id.finish_date_down);
        start_time_down = findViewById(R.id.start_time_down);
        finish_time_down = findViewById(R.id.finish_time_down);
        add_child_icon = findViewById(R.id.add_child_icon);

        date_choice_con = findViewById(R.id.date_choice_con);
        time_choice_con = findViewById(R.id.time_choice_con);
        total_time_con = findViewById(R.id.total_time_con);
        child_con = findViewById(R.id.child_con);
        add_child_con = findViewById(R.id.add_child_con);
        calculation_con = findViewById(R.id.calculation_con);
        total_amount_con = findViewById(R.id.total_amount_con);

        sitter_name = findViewById(R.id.sitter_name);
        sitter_age = findViewById(R.id.sitter_age);
        sitter_location = findViewById(R.id.sitter_location);
        start_date = findViewById(R.id.start_date);
        finish_date = findViewById(R.id.finish_date);
        start_time = findViewById(R.id.start_time);
        finish_time = findViewById(R.id.finish_time);
        total_time_interval = findViewById(R.id.total_time_interval);
        add_child = findViewById(R.id.add_child);
        total_time = findViewById(R.id.total_time);
        total_amount = findViewById(R.id.total_amount);
        total_children = findViewById(R.id.total_children);
        go_reservation = findViewById(R.id.go_reservation);

        spinner_start_time = findViewById(R.id.spinner_start_time);
        spinner_finish_time = findViewById(R.id.spinner_finish_time);
        request = findViewById(R.id.request);

        start_date_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ipmm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);

                datePickerDialog = new DatePickerDialog(ReservationActivity.this, "start");
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.setCancelable(false);
                datePickerDialog.show();
            }
        });

        finish_date_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!"".equals(start_date.getText().toString())) {
                    ipmm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);

                    datePickerDialog = new DatePickerDialog(ReservationActivity.this, "finish");
                    datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    datePickerDialog.setCancelable(false);
                    datePickerDialog.show();
                } else {
                    oneBtnDialog = new OneBtnDialog(ReservationActivity.this, "시작일을 먼저\n 선택해 주세요 !", "확인");
                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    oneBtnDialog.setCancelable(false);
                    oneBtnDialog.show();
                    return;
                }

            }
        });

        ArrayAdapter<String> timeAdapter = new ArrayAdapter<String>(context, R.layout.time_item, time);
        timeAdapter.setDropDownViewResource(R.layout.time_item);
        spinner_start_time.setAdapter(timeAdapter);
        spinner_start_time.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                start_time.setText(spinner_start_time.getSelectedItem().toString());
                getTime1 = (spinner_start_time.getSelectedItemPosition()) + "";
                if ("-1".equals(getTime1)) {
                    getTime1 = "";
                }
                getTotalTime = spinner_finish_time.getSelectedItemPosition() - spinner_start_time.getSelectedItemPosition();
//                if (getTotalTime < 1 ) {
//                    oneBtnDialog = new OneBtnDialog(ReservationActivity.this, "시작 시간과 종료 시간을\n다시 확인해 주세요 !", "확인");
//                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                    oneBtnDialog.setCancelable(false);
//                    oneBtnDialog.show();
//                    return;
//                }
                total_time_interval.setText(getTotalTime + "");
                calculator();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner_finish_time.setAdapter(timeAdapter);
        spinner_finish_time.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                finish_time.setText(spinner_finish_time.getSelectedItem().toString());
                getTime2 = (spinner_finish_time.getSelectedItemPosition()) + "";
                if ("-1".equals(getTime2)) {
                    getTime2 = "";
                }
                getTotalTime = spinner_finish_time.getSelectedItemPosition() - spinner_start_time.getSelectedItemPosition();
//                if (getTotalTime < 1) {
//                    oneBtnDialog = new OneBtnDialog(ReservationActivity.this, "시작 시간과 종료 시간을\n다시 확인해 주세요 !", "확인");
//                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                    oneBtnDialog.setCancelable(false);
//                    oneBtnDialog.show();
//                    return;
//                }
                total_time_interval.setText(getTotalTime + "");
                calculator();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        child_list = findViewById(R.id.child_list);
        data = new ArrayList<Child>();
        childAdapter = new ChildAdapter(context, R.layout.list_child_item, data, child_list, "check");

        add_child.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_child_icon.callOnClick();
            }
        });
        add_child_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i(TAG, " childCnt " + childCnt);

                if (childCnt >= 4) {
                    oneBtnDialog = new OneBtnDialog(ReservationActivity.this, "아이 등록은\n최대 4명입니다 !", "확인");
                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    oneBtnDialog.setCancelable(false);
                    oneBtnDialog.show();
                    return;
                }

                Intent intent = new Intent(ReservationActivity.this, RegisterChild1Activity.class);
                intent.putExtra("what", "add");
                startActivity(intent);
            }
        });

        total_children.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.i(TAG, " total_children " + s);
                calculator();
            }
        });

        go_reservation_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                go_reservation.callOnClick();
            }
        });
        go_reservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences childCheckList = getSharedPreferences("childCheckList", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = childCheckList.edit();
                Map<String, ?> keys = childCheckList.getAll();

                Log.i(TAG, "keys " + keys.size());

                ArrayList<String> kids = new ArrayList<String>();
                if (0 != keys.size()) {
                    String getKidsIdx = "";
                    int i = 0;
                    for (Map.Entry<String, ?> entry : keys.entrySet()) {
                        kids.add(i, entry.getValue().toString());
                        if (i != 0) {
                            getKidsIdx += " ";
                        }
                        getKidsIdx += entry.getValue().toString();
                        i++;
                    }
                }
                dateInterval();

                // array to arraylist

                if(noDate != null){
                    noDate2 = new ArrayList<>(Arrays.asList(noDate));
                    intersection(noDate2, reserveDate);
                }


                if (dateList.size() > 0) {
                    oneBtnDialog = new OneBtnDialog(ReservationActivity.this, "예약 불가능 일이\n포함되어 있습니다 !", "확인");
                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    oneBtnDialog.setCancelable(false);
                    oneBtnDialog.show();
                    return;
                }

//                if (noDate2.size() > reserveDate.size()) {
//                    for (int i = 0; i < reserveDate.size(); i++) {
//                        if (noDate[i].equals(reserveDate.get(i))) {
//                            oneBtnDialog = new OneBtnDialog(ReservationActivity.this, "예약 불가능 일이\n포함되어 있습니다 !", "확인");
//                            oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                            oneBtnDialog.setCancelable(false);
//                            oneBtnDialog.show();
//                            return;
//                        }
//                    }
//                } else {
//                    for (int i = 0; i < noDate2.size(); i++) {
//                        if (noDate[i].equals(reserveDate.get(i))) {
//                            oneBtnDialog = new OneBtnDialog(ReservationActivity.this, "예약 불가능 일이\n포함되어 있습니다 !", "확인");
//                            oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                            oneBtnDialog.setCancelable(false);
//                            oneBtnDialog.show();
//                            return;
//                        }
//                    }
//                }

                if (0 == childAdapter.getCountCheck()) {
                    oneBtnDialog = new OneBtnDialog(ReservationActivity.this, "아이를 한명이라도\n 선택해 주세요!", "확인");
                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    oneBtnDialog.setCancelable(false);
                    oneBtnDialog.show();
                    return;
                } else if (getTotalTime < 2) {
                    oneBtnDialog = new OneBtnDialog(ReservationActivity.this, "돌봄 총 시간을 확인해주세요 !\n(기본 2시간)", "확인");
                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    oneBtnDialog.setCancelable(false);
                    oneBtnDialog.show();
                    return;
                } else if ("".equals(start_date.getText().toString()) || "".equals(finish_date.getText().toString())) {
                    oneBtnDialog = new OneBtnDialog(ReservationActivity.this, "날짜를 지정해주세요 !", "확인");
                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    oneBtnDialog.setCancelable(false);
                    oneBtnDialog.show();
                    return;
                } else if ("0".equals(total_time.getText().toString().replace("시간", ""))) {
                    oneBtnDialog = new OneBtnDialog(ReservationActivity.this, "날짜와 시간을 다시 확인해주세요 !", "확인");
                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    oneBtnDialog.setCancelable(false);
                    oneBtnDialog.show();
                    return;
                } else if (4 < (getChildCnt + childAdapter.getCountCheck())) {
                    oneBtnDialog = new OneBtnDialog(ReservationActivity.this, "이웃 친구 자녀와 돌봄 아이 자녀가\n4명 이하일 때만 예약 가능합니다.", "확인");
                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    oneBtnDialog.setCancelable(false);
                    oneBtnDialog.show();
                    return;
                }

                for (int i = 0; i < reserveDate.size(); i++) {

                }

                SharedPreferences get_token = getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
                final String getToken = get_token.getString("Token", "");
                final String url = ServerUrl.getBaseUrl() + "/reserve/insert";
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("startday", start_date.getText().toString());
                params.put("endday", finish_date.getText().toString());
                params.put("starttime", timeValue[spinner_start_time.getSelectedItemPosition()]);
                params.put("endtime", timeValue[spinner_finish_time.getSelectedItemPosition()]);
                for (int i = 0; i < kids.size(); i++) {
                    params.put("kids[" + i + "]", kids.get(i));
                }
                params.put("etc", request.getText().toString());
                params.put("friend", getFriend);
                params.put("cost", total_amount.getText().toString().replaceAll(",", ""));
                params.put("time", total_time.getText().toString().replace("시간", ""));
                params.put("cnt", childAdapter.getCountCheck());
                Log.i(TAG, " params " + params);
                aQuery.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
                    @Override
                    public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
                        Log.i(TAG, " jsonObject " + jsonObject);
                        try {
                            if (jsonObject.getBoolean("return")) {    //return이 true 면?
                                okDialog = new OkDialog(ReservationActivity.this, "예약 요청 완료\n이웃친구가 고객님의 예약을\n수락하기 전에는\n결제되지 않습니다.", "확인");
                                okDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                okDialog.setCancelable(false);
                                okDialog.show();
                                return;
                            } else if (!jsonObject.getBoolean("return")) {
                                if ("login".equals(jsonObject.getString("type"))) {
                                    Toast.makeText(ReservationActivity.this, "로그인 해주세요 ! ", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(context, LoginActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    oneBtnDialog = new OneBtnDialog(ReservationActivity.this, "예약 요청 실패\n 다시 시도해주세요 !", "확인");
                                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                    oneBtnDialog.setCancelable(false);
                                    oneBtnDialog.show();
                                    return;
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }.header("EPOCH-AGENT", "" + getToken).header("User-Agent", "android"));
            }
        });
        viewSettings();

    }

    @Override
    protected void onResume() {
        super.onResume();
        childList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences childCheckList = getSharedPreferences("childCheckList", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = childCheckList.edit();
        editor.clear();
        editor.commit();
    }

    @Override
    public void finish() {
        super.finish();
        SharedPreferences childCheckList = getSharedPreferences("childCheckList", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = childCheckList.edit();
        editor.clear();
        editor.commit();
    }

    void intersection(ArrayList<String> list1, ArrayList<String> list2) { // 교집합 메소드
        dateList = new ArrayList<String>();
        for (String t : list1) {
            if (list2.contains(t)) {
                dateList.add(t);
            }
        }
    }

    void viewSettings() {
        SharedPreferences get_token = getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
        final String getToken = get_token.getString("Token", "");
        final String url = ServerUrl.getBaseUrl() + "/friend/detail";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("friend", getFriend);
        aQuery.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
                Log.i(TAG, " " + jsonObject);
                try {
                    if (jsonObject.getBoolean("return")) {    //return이 true 면?
                        JSONObject jsonData = jsonObject.getJSONObject("data");

                        circleImage(sitter_photo, ServerUrl.getBaseUrl() + "/uploads/images/origin/" + jsonData.getString("profile"));

                        sitter_name.setText(jsonData.getString("name"));
                        sitter_age.setText(jsonData.getString("age") + "세");
                    } else if (!jsonObject.getBoolean("return")) {
                        if ("login".equals(jsonObject.getString("type"))) {
                            Toast.makeText(ReservationActivity.this, "로그인 해주세요 ! ", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(context, LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            oneBtnDialog = new OneBtnDialog(ReservationActivity.this, "정보를 불러올 수 없습니다 !", "확인");
                            oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            oneBtnDialog.setCancelable(false);
                            oneBtnDialog.show();
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.header("EPOCH-AGENT", "" + getToken).header("User-Agent", "android"));
    }

    void childList() {
        SharedPreferences get_token = getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
        final String getToken = get_token.getString("Token", "");
        final String url = ServerUrl.getBaseUrl() + "/child/list";
        Map<String, Object> params = new HashMap<String, Object>();
        Log.i(TAG, " childList params " + params);
        aQuery.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
                Log.i(TAG, " " + jsonObject);
                try {
                    data.clear();
                    if (jsonObject.getBoolean("return")) {    //return이 true 면?
                        JSONArray jsonArray = new JSONArray(jsonObject.getString("list"));
                        if (jsonArray.length() == 0) {
                            childCnt = 0;
                            child_list.setVisibility(View.GONE);
                            Toast.makeText(ReservationActivity.this, "아이 목록이 없습니다.", Toast.LENGTH_SHORT).show();
                        } else {
                            child_list.setVisibility(View.VISIBLE);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject getJsonObject = jsonArray.getJSONObject(i);
                                Log.i(TAG, " " + getJsonObject);
                                data.add(new Child(getJsonObject.getString("child"), getJsonObject.getString("name"), getJsonObject.getString("age"), getJsonObject.getString("gender"), getJsonObject.getString("image")));
                            }
                            child_list.setAdapter(childAdapter);
                            child_list.setDivider(null);
                            setListViewHeightBasedOnChildren(child_list);
                            childAdapter.notifyDataSetChanged();
                            childCnt = data.size();
                        }
                    } else if (!jsonObject.getBoolean("return")) {
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.header("EPOCH-AGENT", "" + getToken).header("User-Agent", "android"));
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);

        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    public void setListViewHeight(BaseAdapter adpater, ListView listView) {
        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);

        for (int size = 0; size < listView.getCount(); size++) {
            View listItem = adpater.getView(size, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listView.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    private void circleImage(ImageView imageView, String getImg) {
        Log.i(TAG, " circleImage : " + getImg);
        Glide.with(context)
                .load(getImg)
                .apply(RequestOptions.bitmapTransform(new CropCircleTransformation()))
                .into(imageView);
    }

    void dateInterval() {
        try {
            final String DATE_PATTERN = "yyyy-MM-dd";
            String inputStartDate = start_date.getText().toString();
            String inputEndDate = finish_date.getText().toString();
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);
            Date startDate = sdf.parse(inputStartDate);
            Date endDate = sdf.parse(inputEndDate);
            ArrayList<String> dates = new ArrayList<String>();
            Date currentDate = startDate;
            while (currentDate.compareTo(endDate) <= 0) {
                dates.add(sdf.format(currentDate));
                Calendar c = Calendar.getInstance();
                c.setTime(currentDate);
                c.add(Calendar.DAY_OF_MONTH, 1);
                currentDate = c.getTime();
            }
            reserveDate.clear();
            for (String date : dates) {
                Log.i(TAG, " date " + date);
                reserveDate.add(date);
            }
        } catch (ParseException e) {
            Log.i(TAG, " " + e);
        }

        for (int i = 0; i < reserveDate.size(); i++) {
            Log.i(TAG, " reserveDate " + reserveDate.get(i));
        }
    }

    void calculator() {
        SharedPreferences get_token = getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
        final String getToken = get_token.getString("Token", "");
        final String url = ServerUrl.getBaseUrl() + "/reserve/cost";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("start", start_date.getText().toString());
        params.put("end", finish_date.getText().toString());
        params.put("time1", getTime1);
        params.put("time2", getTime2);
        params.put("friend", getFriend);
        params.put("child", childAdapter.getCountCheck());
        Log.i(TAG, " calculator():: params " + params);
        aQuery.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
                Log.i(TAG, " calculator():: jsonObject " + jsonObject);
                try {
                    if (jsonObject.getBoolean("return")) {    //return이 true 면?
                        JSONObject dataObject = jsonObject.getJSONObject("data");
                        String money = dataObject.getString("cost");
                        long value = Long.parseLong(money);
                        DecimalFormat format = new DecimalFormat("###,###");//콤마
                        String totalCost = format.format(value);

                        total_amount.setText(totalCost + "원");
                        total_time.setText(dataObject.getString("time") + "시간");
                    } else if (!jsonObject.getBoolean("return")) {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.header("EPOCH-AGENT", "" + getToken).header("User-Agent", "android"));
    }

    public class OneBtnDialog extends Dialog {
        OneBtnDialog oneBtnDialog = this;
        Context context;

        public OneBtnDialog(final Context context, final String text, final String btnText) {
            super(context);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialog_one_btn);
            getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            this.context = context;
            TextView title1 = (TextView) findViewById(R.id.title1);
            TextView title2 = (TextView) findViewById(R.id.title2);
            TextView btn1 = (TextView) findViewById(R.id.btn1);
            title2.setVisibility(View.GONE);
            title1.setText(text);
            btn1.setText(btnText);
            btn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    oneBtnDialog.dismiss();
                }
            });
        }
    }

    public class OkDialog extends Dialog {
        OkDialog okDialog = this;
        Context context;

        public OkDialog(final Context context, final String text, final String btnText) {
            super(context);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialog_one_btn);
            getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            this.context = context;
            TextView title1 = (TextView) findViewById(R.id.title1);
            TextView title2 = (TextView) findViewById(R.id.title2);
            TextView btn1 = (TextView) findViewById(R.id.btn1);
            title2.setVisibility(View.GONE);
            title1.setText(text);
            btn1.setText(btnText);
            btn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    okDialog.dismiss();
                    finish();
                }
            });
        }
    }

    public class DatePickerDialog extends Dialog {
        DatePickerDialog datePickerDialog = this;
        Context context;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
        Date startDate, endDate;
        long interval;

        public DatePickerDialog(final Context context, final String what) {
            super(context);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialog_datepicker);
            getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            this.context = context;
            Calendar calendar = Calendar.getInstance();
            datePicker1 = (DatePicker) findViewById(R.id.datePicker);
            datePicker2 = (DatePicker) findViewById(R.id.datePicker);

            if ("start".equals(what)) {
                datePicker1.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            } else if ("finish".equals(what)) {
                datePicker2.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            }

            TextView btn1 = (TextView) findViewById(R.id.btn1);
            TextView btn2 = (TextView) findViewById(R.id.btn2);
            btn1.setText("취소");
            btn2.setText("선택");
            btn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    datePickerDialog.dismiss();
                }
            });
            btn2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    datePickerDialog.dismiss();
                    if ("start".equals(what)) {
                        start_date.setText(datePicker1.getYear() + "-" + (datePicker1.getMonth() + 1) + "-" + datePicker1.getDayOfMonth());
                        try {
                            endDate = dateFormat.parse(finish_date.getText().toString());
                            startDate = dateFormat.parse(start_date.getText().toString());
                            Log.i(TAG, "****startDate****" + startDate + "*****endDate******" + endDate);
                            interval = startDate.compareTo(endDate);
                            Log.i(TAG, "****interval****" + interval);
                            if (interval > 0) {
                                finish_date.setText("");
                                oneBtnDialog = new OneBtnDialog(ReservationActivity.this, "시작일과 종료일을\n다시 확인해주세요 !", "확인");
                                oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                oneBtnDialog.setCancelable(false);
                                oneBtnDialog.show();
                            }
                        } catch (ParseException e) {
                            Log.i(TAG, " DatePickerDialog " + e);
                        }
                    } else if ("finish".equals(what)) {
                        finish_date.setText(datePicker2.getYear() + "-" + (datePicker2.getMonth() + 1) + "-" + datePicker2.getDayOfMonth());
                        try {
                            endDate = dateFormat.parse(finish_date.getText().toString());
                            startDate = dateFormat.parse(start_date.getText().toString());
                            Log.i(TAG, "****startDate****" + startDate + "*****endDate******" + endDate);
                            interval = startDate.compareTo(endDate);
                            Log.i(TAG, "****interval****" + interval);
                            if (interval > 0) {
                                finish_date.setText("");
                                oneBtnDialog = new OneBtnDialog(ReservationActivity.this, "시작일과 종료일을\n다시 확인해주세요 !", "확인");
                                oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                oneBtnDialog.setCancelable(false);
                                oneBtnDialog.show();
                            }
                        } catch (ParseException e) {
                            Log.i(TAG, " DatePickerDialog " + e);
                        }
                    }
                    calculator();
                }
            });
        }
    }
}
