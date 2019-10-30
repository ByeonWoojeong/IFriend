package com.woojeong.global.ifriend;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.androidquery.AQuery;

import static com.woojeong.global.ifriend.GlobalApplication.setDarkMode;

public class ReadAvailableServicesActivity extends AppCompatActivity {
    private static String TAG = "ReadAvailableServicesActivity";
    Context context;
    InputMethodManager ipmm;
    AQuery aQuery = null;

    FrameLayout back_con;
    ImageView back;
    ScrollView scrollView;
    LinearLayout pick_up_con, medicine_con, toddlers_con, long_care_con, shower_con, homework_con, study_con, outdoor_con, snack_con, meal_con, etc_con;

    String[] option;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_available_services);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#ffffff"));
            window.setBackgroundDrawable(null);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                setDarkMode(ReadAvailableServicesActivity.this, true);
            }
        }

        option = getIntent().getStringExtra("option").split(",");

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

        scrollView = findViewById(R.id.scrollView);

        pick_up_con = findViewById(R.id.pick_up_con);
        medicine_con = findViewById(R.id.medicine_con);
        toddlers_con = findViewById(R.id.toddlers_con);
        long_care_con = findViewById(R.id.long_care_con);
        shower_con = findViewById(R.id.shower_con);
        homework_con = findViewById(R.id.homework_con);
        study_con = findViewById(R.id.study_con);
        outdoor_con = findViewById(R.id.outdoor_con);
        snack_con = findViewById(R.id.snack_con);
        meal_con = findViewById(R.id.meal_con);
        etc_con = findViewById(R.id.etc_con);

        pick_up_con.setVisibility(View.GONE);
        medicine_con.setVisibility(View.GONE);
        toddlers_con.setVisibility(View.GONE);
        long_care_con.setVisibility(View.GONE);
        shower_con.setVisibility(View.GONE);
        homework_con.setVisibility(View.GONE);
        study_con.setVisibility(View.GONE);
        outdoor_con.setVisibility(View.GONE);
        snack_con.setVisibility(View.GONE);
        meal_con.setVisibility(View.GONE);
        etc_con.setVisibility(View.GONE);

        viewSettings();
    }

    void viewSettings(){
        for(int i=0; i<option.length; i++){
            if("1".equals(option[i])){
                pick_up_con.setVisibility(View.VISIBLE);
            } else if("2".equals(option[i])){
                medicine_con.setVisibility(View.VISIBLE);
            } else if("3".equals(option[i])){
                toddlers_con.setVisibility(View.VISIBLE);
            } else if("4".equals(option[i])){
                long_care_con.setVisibility(View.VISIBLE);
            } else if("5".equals(option[i])){
                shower_con.setVisibility(View.VISIBLE);
            } else if("6".equals(option[i])){
                homework_con.setVisibility(View.VISIBLE);
            } else if("7".equals(option[i])){
                study_con.setVisibility(View.VISIBLE);
            } else if("8".equals(option[i])){
                outdoor_con.setVisibility(View.VISIBLE);
            } else if("9".equals(option[i])){
                snack_con.setVisibility(View.VISIBLE);
            } else if("10".equals(option[i])){
                meal_con.setVisibility(View.VISIBLE);
            } else if("11".equals(option[i])){
                etc_con.setVisibility(View.VISIBLE);
            }
        }
    }
}
