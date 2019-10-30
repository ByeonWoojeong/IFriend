package com.woojeong.global.ifriend;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
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
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.woojeong.global.ifriend.DTO.JournalDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.woojeong.global.ifriend.GlobalApplication.setDarkMode;

public class UseRulesActivity extends AppCompatActivity {
    private static String TAG = "UseRulesActivity";
    Context context;
    InputMethodManager ipmm;
    AQuery aQuery = null;
    OneBtnDialog oneBtnDialog;

    FrameLayout back_con, weekday_start_con, weekday_finish_con, weekend_start_con, weekend_finish_con, ok_con;
    ImageView back, weekday_start_down, weekday_finish_down, weekend_start_down, weekend_finish_down;
    ScrollView scrollView;
    EditText bring_edit, impossible_edit;
    SpinnerReselect spinner_weekday_start, spinner_weekday_finish, spinner_weekend_start, spinner_weekend_finish;
    TextView weekday_start_txt, weekday_finish_txt, weekend_start_txt, weekend_finish_txt, ok;

    String weekDayStart, weekDayFinish, weekEndStart, weekEndFinish;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_use_rules);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#ffffff"));
            window.setBackgroundDrawable(null);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                setDarkMode(UseRulesActivity.this, true);
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


        weekday_start_con = findViewById(R.id.weekday_start_con);
        weekday_finish_con = findViewById(R.id.weekday_finish_con);
        weekend_start_con = findViewById(R.id.weekend_start_con);
        weekend_finish_con = findViewById(R.id.weekend_finish_con);
        ok_con = findViewById(R.id.ok_con);

        weekday_start_down = findViewById(R.id.weekday_start_down);
        weekday_finish_down = findViewById(R.id.weekday_finish_down);
        weekend_start_down = findViewById(R.id.weekend_start_down);
        weekend_finish_down = findViewById(R.id.weekend_finish_down);

        scrollView = findViewById(R.id.scrollView);

        bring_edit = findViewById(R.id.bring_edit);
        impossible_edit = findViewById(R.id.impossible_edit);

        String[] time = new String[] { "00:00", "01:00",  "02:00",  "03:00",  "04:00",  "05:00",  "06:00",  "07:00",  "08:00",  "09:00",  "10:00",  "11:00",  "12:00",
                "13:00",  "14:00",  "15:00",  "16:00",  "17:00",  "18:00",  "19:00",  "20:00",  "21:00",  "22:00",  "23:00" };

        spinner_weekday_start = findViewById(R.id.spinner_weekday_start);
        spinner_weekday_finish = findViewById(R.id.spinner_weekday_finish);
        spinner_weekend_start = findViewById(R.id.spinner_weekend_start);
        spinner_weekend_finish = findViewById(R.id.spinner_weekend_finish);

        weekday_start_txt = findViewById(R.id.weekday_start_txt);
        weekday_finish_txt = findViewById(R.id.weekday_finish_txt);
        weekend_start_txt = findViewById(R.id.weekend_start_txt);
        weekend_finish_txt = findViewById(R.id.weekend_finish_txt);
        ok = findViewById(R.id.ok);


        ArrayAdapter<String> timeAdapter = new ArrayAdapter<String>(context, R.layout.time_item, time);
        timeAdapter.setDropDownViewResource(R.layout.time_item);
        spinner_weekday_start.setAdapter(timeAdapter);
        spinner_weekday_start.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                weekday_start_txt.setText(spinner_weekday_start.getSelectedItem().toString());
                weekDayStart = (spinner_weekday_start.getSelectedItemPosition()) + "";
                if ("-1".equals(weekDayStart)) {
                    weekDayStart = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner_weekday_finish.setAdapter(timeAdapter);
        spinner_weekday_finish.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                weekday_finish_txt.setText(spinner_weekday_finish.getSelectedItem().toString());
                weekDayFinish = (spinner_weekday_finish.getSelectedItemPosition()) + "";
                if ("-1".equals(weekDayFinish)) {
                    weekDayFinish = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner_weekend_start.setAdapter(timeAdapter);
        spinner_weekend_start.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                weekend_start_txt.setText(spinner_weekend_start.getSelectedItem().toString());
                weekEndStart = (spinner_weekend_start.getSelectedItemPosition()) + "";
                if ("-1".equals(weekEndStart)) {
                    weekEndStart = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner_weekend_finish.setAdapter(timeAdapter);
        spinner_weekend_finish.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                weekend_finish_txt.setText(spinner_weekend_finish.getSelectedItem().toString());
                weekEndFinish = (spinner_weekend_finish.getSelectedItemPosition()) + "";
                if ("-1".equals(weekEndFinish)) {
                    weekEndFinish = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        ok_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ok.callOnClick();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences get_token = getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
                final String getToken = get_token.getString("Token", "");
                final String url = ServerUrl.getBaseUrl() + "/friend/update";
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("have", bring_edit.getText().toString());
                params.put("not", impossible_edit.getText().toString());
                params.put("time1", weekDayStart);
                params.put("time2", weekDayFinish);
                params.put("time3", weekEndStart);
                params.put("time4", weekEndFinish);
                Log.i(TAG, " params " + params);
                aQuery.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
                    @Override
                    public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
                        Log.i(TAG, " " + jsonObject);
                        try {
                            if (jsonObject.getBoolean("return")) {    //return이 true 면?
                                Toast.makeText(UseRulesActivity.this, "이용 규칙 업로드 성공", Toast.LENGTH_SHORT).show();
                                finish();
                            } else if (!jsonObject.getBoolean("return")) {
                                if("login".equals(jsonObject.getString("type"))){
                                    Toast.makeText(UseRulesActivity.this, "로그인 해주세요 ! ", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(context, LoginActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(UseRulesActivity.this, "이용 규칙 업로드 실패", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }.header("EPOCH-AGENT", "" + getToken).header("User-Agent", "android"));
            }
        });

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                viewSettings();
            }
        }, 200);
        scrollView.postDelayed(new Runnable() {
            @Override
            public void run() {
                scrollView.scrollTo(0, 0);
            }
        }, 700);
    }

    void viewSettings(){
        SharedPreferences get_token = getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
        final String getToken = get_token.getString("Token", "");
        final String url = ServerUrl.getBaseUrl() + "/friend/data";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("have", "");
        params.put("not", "");
        params.put("time1", "");
        params.put("time2", "");
        params.put("time3", "");
        params.put("time4", "");
        Log.i(TAG, " params " + params);
        aQuery.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
                Log.i(TAG, " " + jsonObject);
                try {
                    if (jsonObject.getBoolean("return")) {    //return이 true 면?
                        JSONObject jsonData = jsonObject.getJSONObject("data");

                        bring_edit.setText(jsonData.getString("have"));
                        impossible_edit.setText(jsonData.getString("not"));
                        weekday_start_txt.setText(jsonData.getString("time1")+":00");
                        weekDayStart = jsonData.getString("time1");
                        weekday_finish_txt.setText(jsonData.getString("time2")+":00");
                        weekDayFinish = jsonData.getString("time2");
                        weekend_start_txt.setText(jsonData.getString("time3")+":00");
                        weekEndStart = jsonData.getString("time3");
                        weekend_finish_txt.setText(jsonData.getString("time4")+":00");
                        weekEndFinish = jsonData.getString("time4");

                    } else if (!jsonObject.getBoolean("return")) {
                        if("login".equals(jsonObject.getString("type"))){
                            Toast.makeText(UseRulesActivity.this, "로그인 해주세요 ! ", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(context, LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
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
}
