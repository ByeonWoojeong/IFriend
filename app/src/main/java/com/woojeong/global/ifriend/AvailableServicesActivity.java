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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.woojeong.global.ifriend.DTO.Career;
import com.woojeong.global.ifriend.DTO.JournalDetails;
import com.woojeong.global.ifriend.DTO.Two;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.woojeong.global.ifriend.GlobalApplication.setDarkMode;

public class AvailableServicesActivity extends AppCompatActivity {
    private static String TAG = "AvailableServicesActivity";
    Context context;
    InputMethodManager ipmm;
    AQuery aQuery = null;
    OneBtnDialog oneBtnDialog;

    FrameLayout back_con, ok_con;
    TextView ok;
    ImageView back;
    ScrollView scrollView;
    LinearLayout pick_up_con, medicine_con, toddlers_con, long_care_con, shower_con, homework_con, study_con, outdoor_con, snack_con, meal_con, etc_con;
    CheckBox check_pick_up, check_medicine, check_toddlers, check_long_care, check_shower, check_homework, check_study, check_outdoor, check_snack, check_meal, check_etc;
    EditText etc, education_service;

    ArrayList<String> service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_services);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#ffffff"));
            window.setBackgroundDrawable(null);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                setDarkMode(AvailableServicesActivity.this, true);
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

        ok_con = findViewById(R.id.ok_con);
        ok = findViewById(R.id.ok);
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

        check_pick_up = findViewById(R.id.check_pick_up);
        check_medicine = findViewById(R.id.check_medicine);
        check_toddlers = findViewById(R.id.check_toddlers);
        check_long_care = findViewById(R.id.check_long_care);
        check_shower = findViewById(R.id.check_shower);
        check_homework = findViewById(R.id.check_homework);
        check_study = findViewById(R.id.check_study);
        check_outdoor = findViewById(R.id.check_outdoor);
        check_snack = findViewById(R.id.check_snack);
        check_meal = findViewById(R.id.check_meal);
        check_etc = findViewById(R.id.check_etc);

        etc = findViewById(R.id.etc);
        education_service = findViewById(R.id.education_service);

        service = new ArrayList<String>();

        check_pick_up.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    service.add("1");
                } else {
                    service.remove("1");
                }
            }
        });
        check_medicine.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    service.add("2");
                } else {
                    service.remove("2");
                }
            }
        });
        check_toddlers.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    service.add("3");
                } else {
                    service.remove("3");
                }
            }
        });
        check_long_care.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    service.add("4");
                } else {
                    service.remove("4");
                }
            }
        });
        check_shower.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    service.add("5");
                } else {
                    service.remove("5");
                }
            }
        });
        check_homework.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    service.add("6");
                } else {
                    service.remove("6");
                }
            }
        });
        check_study.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    service.add("7");
                } else {
                    service.remove("7");
                }
            }
        });
        check_outdoor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    service.add("8");
                } else {
                    service.remove("8");
                }
            }
        });
        check_snack.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    service.add("9");
                } else {
                    service.remove("9");
                }
            }
        });
        check_meal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    service.add("10");
                } else {
                    service.remove("10");
                }
            }
        });
        check_etc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    service.add("11");
                    etc.setEnabled(true);
                } else {
                    service.remove("11");
                    etc.setEnabled(false);
                }
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

                if("".equals(education_service.getText().toString())){
                    oneBtnDialog = new OneBtnDialog(AvailableServicesActivity.this, "가능한 교육 서비스를\n작성 해주세요 !", "확인");
                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    oneBtnDialog.setCancelable(false);
                    oneBtnDialog.show();
                    return;
                }

                SharedPreferences get_token = getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
                final String getToken = get_token.getString("Token", "");
                final String url = ServerUrl.getBaseUrl() + "/friend/update";
                Map<String, Object> params = new HashMap<String, Object>();
                for (int i = 0; i < service.size(); i++) {
                    params.put("option[" + i + "]", service.get(i));
                }
                params.put("optiondetail", etc.getText().toString());
                params.put("edu", education_service.getText().toString());
                Log.i(TAG, " params " + params);
                aQuery.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
                    @Override
                    public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
                        Log.i(TAG, " " + jsonObject);
                        try {
                            if (jsonObject.getBoolean("return")) {    //return이 true 면?
                                Toast.makeText(AvailableServicesActivity.this, "이용 가능 서비스 업로드 성공", Toast.LENGTH_SHORT).show();
                                setResult(123);
                                finish();
                            } else if (!jsonObject.getBoolean("return")) {
                                if("login".equals(jsonObject.getString("type"))){
                                    Toast.makeText(AvailableServicesActivity.this, "로그인 해주세요 ! ", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(AvailableServicesActivity.this, LoginActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(AvailableServicesActivity.this, "이용 가능 서비스 업로드 실패", Toast.LENGTH_SHORT).show();
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

    void viewSettings() {
        SharedPreferences get_token = getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
        final String getToken = get_token.getString("Token", "");
        final String url = ServerUrl.getBaseUrl() + "/friend/data";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("option", "");
        params.put("optiondetail","");
        params.put("edu", "");
        aQuery.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
                Log.i(TAG, " jsonObject " + jsonObject);
                try {
                    if (jsonObject.getBoolean("return")) {    //return이 true 면?
                        JSONObject jsonData = jsonObject.getJSONObject("data");

                        String[] option = jsonData.getString("option").split(",");

                        for (int i = 0; i < option.length; i++) {
                            Log.i(TAG, " option " + option[i]);

                            if ("1".equals(option[i])) {
                                check_pick_up.setChecked(true);
                            } else if ("2".equals(option[i])) {
                                check_medicine.setChecked(true);
                            } else if ("3".equals(option[i])) {
                                check_toddlers.setChecked(true);
                            } else if ("4".equals(option[i])) {
                                check_long_care.setChecked(true);
                            } else if ("5".equals(option[i])) {
                                check_shower.setChecked(true);
                            } else if ("6".equals(option[i])) {
                                check_homework.setChecked(true);
                            } else if ("7".equals(option[i])) {
                                check_study.setChecked(true);
                            } else if ("8".equals(option[i])) {
                                check_outdoor.setChecked(true);
                            } else if ("9".equals(option[i])) {
                                check_snack.setChecked(true);
                            } else if ("10".equals(option[i])) {
                                check_meal.setChecked(true);
                            } else if ("11".equals(option[i])) {
                                check_etc.setChecked(true);
                            }
                        }

                        etc.setText(jsonData.getString("optiondetail"));
                        education_service.setText(jsonData.getString("edu"));

                    } else if (!jsonObject.getBoolean("return")) {
                        if("login".equals(jsonObject.getString("type"))){
                            Toast.makeText(AvailableServicesActivity.this, "로그인 해주세요 ! ", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(AvailableServicesActivity.this, LoginActivity.class);
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
