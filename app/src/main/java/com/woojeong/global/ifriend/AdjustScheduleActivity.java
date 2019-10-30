package com.woojeong.global.ifriend;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnRangeSelectedListener;
import com.woojeong.global.ifriend.CalendarView.RangeDayDecorator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.woojeong.global.ifriend.GlobalApplication.setDarkMode;

public class AdjustScheduleActivity extends AppCompatActivity {
    private static String TAG = "AdjustScheduleActivity";
    Context context;
    InputMethodManager ipmm;
    AQuery aQuery = null;
    OneBtnDialog oneBtnDialog;

    MaterialCalendarView calendar_view;
    TextView ok;
    ImageView back;
    FrameLayout back_con;
    LinearLayout calendar_con;

    ArrayList<String> impossible;

    ArrayList<String> getDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adjust_schedule);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#ffffff"));
            window.setBackgroundDrawable(null);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                setDarkMode(AdjustScheduleActivity.this, true);
            }
        }

        getDate = (ArrayList<String>) getIntent().getSerializableExtra("dateSetting");
        Log.i(TAG, "getDate " + getDate);
        Log.i(TAG, "getDate.size() " + getDate.size());

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


        calendar_view = findViewById(R.id.calendar_view);
        calendar_con = findViewById(R.id.calendar_con);

        impossible = new ArrayList<String>();

        if (getDate.size() > 1) {

            for (int i = 0; i < getDate.size(); i++) {

                impossible.add(getDate.get(i));

                int year = Integer.parseInt(getDate.get(i).substring(0, 4));
                int month = Integer.parseInt(getDate.get(i).substring(5, 7));
                if ("0".equals(getDate.get(i).indexOf(5))) {
                    month = Integer.parseInt(getDate.get(i).substring(6, 7));
                }
                int day = Integer.parseInt(getDate.get(i).substring(8, 10));
                if ("0".equals(getDate.get(i).indexOf(8))) {
                    day = Integer.parseInt(getDate.get(i).substring(9, 10));
                }

                calendar_view.setSelectionColor(getResources().getColor(R.color.subTitleGray));
                CalendarDay calendarDay = CalendarDay.from(year, month, day);
                calendar_view.setDateSelected(calendarDay, true);
            }

        }

        calendar_view.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView materialCalendarView, @NonNull CalendarDay calendarDay, boolean b) {
                if (b) {
//                    Toast.makeText(AdjustScheduleActivity.this, " " + calendarDay.getDate() + " " + b, Toast.LENGTH_SHORT).show();
                    impossible.add(calendarDay.getDate() + "");
                } else {
//                    Toast.makeText(AdjustScheduleActivity.this, " " + calendarDay.getDate() + " " + b, Toast.LENGTH_SHORT).show();
                    impossible.remove(calendarDay.getDate() + "");
                }
            }
        });

        ok = findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AdjustScheduleActivity.this, "선택 완료", Toast.LENGTH_SHORT).show();

                SharedPreferences get_token = getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
                final String getToken = get_token.getString("Token", "");
                final String url = ServerUrl.getBaseUrl() + "/profile/setdate";
                Map<String, Object> params = new HashMap<String, Object>();
                for (int i = 0; i < impossible.size(); i++) {
                    params.put("dates[" + i + "]", impossible.get(i));
                }
                Log.i(TAG, " " + params);
                aQuery.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
                    @Override
                    public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
                        Log.i(TAG, " " + jsonObject);
                        try {
                            if (jsonObject.getBoolean("return")) {    //return이 true 면?

                                oneBtnDialog = new OneBtnDialog(AdjustScheduleActivity.this, "돌봄 불가능한 날짜를\n설정 완료하였습니다 !", "확인");
                                oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                oneBtnDialog.setCancelable(false);
                                oneBtnDialog.show();

                            } else if (!jsonObject.getBoolean("return")) {
                                if("login".equals(jsonObject.getString("type"))){
                                    Toast.makeText(AdjustScheduleActivity.this, "로그인 해주세요 ! ", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(AdjustScheduleActivity.this, LoginActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    oneBtnDialog = new OneBtnDialog(AdjustScheduleActivity.this, "돌봄 불가능한 날짜를\n설정 실패하였습니다.", "확인");
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
        });
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
                    finish();
                }
            });
        }
    }
}
