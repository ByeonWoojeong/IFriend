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
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.woojeong.global.ifriend.DTO.Profit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import ru.dimorinny.floatingtextbutton.FloatingTextButton;

import static com.woojeong.global.ifriend.GlobalApplication.setDarkMode;

public class ProfitCalcuationActivity extends AppCompatActivity {
    private static String TAG = "ProfitCalcuationActivity";
    Context context;
    InputMethodManager ipmm;
    AQuery aQuery = null;
    OneBtnDialog oneBtnDialog;

    DatePickerDialog datePickerDialog;
    DatePicker datePicker1, datePicker2;

    FrameLayout back_con, start_date_con, finish_date_con;
    ImageView back;
    TextView total_amount, spinner_text, start_date, finish_date;
    SpinnerReselect spinner;

    FloatingTextButton profit_guide;

    ListView profit_list;
    ArrayList<Profit> data;
    ProfitListAdapter profitListAdapter;

    String getSpinner = "", getDate1 = "", getDate2 = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profit_calcuation);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#ffffff"));
            window.setBackgroundDrawable(null);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                setDarkMode(ProfitCalcuationActivity.this, true);
            }
        }
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

        total_amount = findViewById(R.id.total_amount);
        spinner_text = findViewById(R.id.spinner_text);
        start_date = findViewById(R.id.start_date);
        finish_date = findViewById(R.id.finish_date);

        start_date_con = findViewById(R.id.start_date_con);
        finish_date_con = findViewById(R.id.finish_date_con);

        spinner = findViewById(R.id.spinner);

        showList(getSpinner, getDate1, getDate2);

        String[] spinnerText = new String[]{"모든정산", "정산대기", "정산불가", "입금대기", "입금완료"};
        ArrayAdapter<String> spinnerAdapter1 = new ArrayAdapter<String>(context, R.layout.spinner_item2, spinnerText);
        spinnerAdapter1.setDropDownViewResource(R.layout.spinner_item2);
        spinner.setAdapter(spinnerAdapter1);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                spinner_text.setText(spinner.getSelectedItem().toString());
                getSpinner = (spinner.getSelectedItemPosition()) + "";

                showList(getSpinner, getDate1, getDate2);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        start_date_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ipmm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);

                datePickerDialog = new DatePickerDialog(ProfitCalcuationActivity.this, "start");
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

                    datePickerDialog = new DatePickerDialog(ProfitCalcuationActivity.this, "end");
                    datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    datePickerDialog.setCancelable(false);
                    datePickerDialog.show();
                } else {
                    oneBtnDialog = new OneBtnDialog(ProfitCalcuationActivity.this, "시작일을 먼저\n 선택해 주세요 !", "확인");
                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    oneBtnDialog.setCancelable(false);
                    oneBtnDialog.show();
                    return;
                }

            }
        });

        profit_guide = findViewById(R.id.profit_guide);

        profit_list = findViewById(R.id.profit_list);
        data = new ArrayList<Profit>();
        profitListAdapter = new ProfitListAdapter(ProfitCalcuationActivity.this, R.layout.list_profit_item, data, profit_list);

        profit_guide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfitCalcuationActivity.this, ProfitGuideActivity.class);
                startActivity(intent);
            }
        });
    }

    void showList(String param1, String param2, String param3) {
        SharedPreferences get_token = getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
        final String getToken = get_token.getString("Token", "");
        final String url = ServerUrl.getBaseUrl() + "/pay/detail";
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("status", param1);
        params.put("start", param2);
        params.put("end", param3);
        Log.i(TAG, " params " + params);

        aQuery.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
                Log.i(TAG, " jsonObject " + jsonObject);
                try {
                    data.clear();
                    if (jsonObject.getBoolean("return")) {    //return이 true 면?
                        JSONArray jsonArray = new JSONArray(jsonObject.getString("list"));
                        JSONObject jsonData = jsonObject.getJSONObject("data");

                        total_amount.setText(jsonData.getString("total"));

                        if (jsonArray.length() == 0) {
                            profit_list.setVisibility(View.GONE);
                            Toast.makeText(context, "통계 내역이 없습니다.", Toast.LENGTH_SHORT).show();
                        } else {
                            profit_list.setVisibility(View.VISIBLE);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject getJsonObject = jsonArray.getJSONObject(i);
                                data.add(new Profit(getJsonObject.getString("cost"), getJsonObject.getString("day2"), getJsonObject.getString("status")));
                            }
                            profit_list.setAdapter(profitListAdapter);
                            profit_list.setDivider(null);
                            profitListAdapter.notifyDataSetChanged();
                        }
                    } else if (!jsonObject.getBoolean("return")) {
                        profit_list.setVisibility(View.GONE);
                        if("login".equals(jsonObject.getString("type"))){
                            Toast.makeText(ProfitCalcuationActivity.this, "로그인 해주세요 ! ", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(context, LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(ProfitCalcuationActivity.this, "통계 내역 불러오기 실패", Toast.LENGTH_SHORT).show();
                        }


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

    public class DatePickerDialog extends Dialog {
        DatePickerDialog datePickerDialog = this;
        Context context;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
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
                        getDate1 = datePicker1.getYear() + "-" + (datePicker1.getMonth() + 1) + "-" + datePicker1.getDayOfMonth();
                        try {
                            endDate = dateFormat.parse(finish_date.getText().toString());
                            startDate = dateFormat.parse(start_date.getText().toString());
                            interval = startDate.compareTo(endDate);
                            if (interval > 0) {
                                start_date.setText("");
                                oneBtnDialog = new OneBtnDialog(ProfitCalcuationActivity.this, "시작일과 종료일을\n다시 확인해주세요 !", "확인");
                                oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                oneBtnDialog.setCancelable(false);
                                oneBtnDialog.show();
                            }
                        } catch (ParseException e) {
                            Log.i(TAG, " DatePickerDialog " + e);
                        }
                        showList(getSpinner, getDate1, getDate2);
                    } else if ("end".equals(what)) {
                        finish_date.setText(datePicker2.getYear() + "-" + (datePicker2.getMonth() + 1) + "-" + datePicker2.getDayOfMonth());
                        getDate2 = datePicker2.getYear() + "-" + (datePicker2.getMonth() + 1) + "-" + datePicker2.getDayOfMonth();
                        try {
                            endDate = dateFormat.parse(finish_date.getText().toString());
                            startDate = dateFormat.parse(start_date.getText().toString());
                            interval = startDate.compareTo(endDate);
                            if (interval > 0) {
                                finish_date.setText("");
                                oneBtnDialog = new OneBtnDialog(ProfitCalcuationActivity.this, "시작일과 종료일을\n다시 확인해주세요 !", "확인");
                                oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                oneBtnDialog.setCancelable(false);
                                oneBtnDialog.show();
                            }
                        } catch (ParseException e) {
                            Log.i(TAG, " DatePickerDialog " + e);
                        }
                        showList(getSpinner, getDate1, getDate2);
                    }
                }
            });
        }
    }
}
