package com.woojeong.global.ifriend;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.Nullable;
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
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.woojeong.global.ifriend.DTO.Career;
import com.woojeong.global.ifriend.DTO.Two;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.woojeong.global.ifriend.GlobalApplication.setDarkMode;

public class PageManagementActivity extends AppCompatActivity {
    private static String TAG = "PageManagementActivity";
    Context context;
    InputMethodManager ipmm;
    AQuery aQuery = null;
    OneBtnDialog oneBtnDialog;

    FrameLayout back_con, page_setting_con, insert_photo_con, introduce_con, career_con, available_services_con, care_environment_con, cost_con, rules_con;
    ImageView back;
    TextView page_setting, insert_photo, introduce, career, available_services, care_environment, cost, rules;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_management);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#ffffff"));
            window.setBackgroundDrawable(null);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                setDarkMode(PageManagementActivity.this, true);
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

        page_setting_con = findViewById(R.id.page_setting_con);
        insert_photo_con = findViewById(R.id.insert_photo_con);
        introduce_con = findViewById(R.id.introduce_con);
        career_con = findViewById(R.id.career_con);
        available_services_con = findViewById(R.id.available_services_con);
        care_environment_con = findViewById(R.id.care_environment_con);
        cost_con = findViewById(R.id.cost_con);
        rules_con = findViewById(R.id.rules_con);

        page_setting = findViewById(R.id.page_setting);
        insert_photo = findViewById(R.id.insert_photo);
        introduce = findViewById(R.id.introduce);
        career = findViewById(R.id.career);
        available_services = findViewById(R.id.available_services);
        care_environment = findViewById(R.id.care_environment);
        cost = findViewById(R.id.cost);
        rules = findViewById(R.id.rules);

        page_setting_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PageManagementActivity.this, PageSettingActivity.class);
                startActivity(intent);
            }
        });

        insert_photo_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PageManagementActivity.this, RegisterPhotoActivity.class);
                startActivity(intent);
            }
        });

        introduce_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PageManagementActivity.this, AboutMeActivity.class);
                startActivity(intent);
            }
        });

        career_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PageManagementActivity.this, ReadCareerActivity.class);
                startActivity(intent);
            }
        });

        available_services_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PageManagementActivity.this, AvailableServicesActivity.class);
                startActivity(intent);
            }
        });

        care_environment_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PageManagementActivity.this, CareEnvironmentActivity.class);
                startActivity(intent);
            }
        });

        cost_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PageManagementActivity.this, UseCostActivity.class);
                startActivity(intent);
            }
        });

        rules_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PageManagementActivity.this, UseRulesActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewSettings();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (resultCode) {
            case 123:
                viewSettings();
                break;
            default:
                break;
        }
    }

    void viewSettings() {
        SharedPreferences get_token = getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
        final String getToken = get_token.getString("Token", "");
        final String url = ServerUrl.getBaseUrl() + "/setting/page";
        Map<String, Object> params = new HashMap<String, Object>();
        aQuery.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
                Log.i(TAG, " " + jsonObject);
                try {
                    if (jsonObject.getBoolean("return")) {    //return이 true 면?
                        JSONObject jsonData = jsonObject.getJSONObject("data");

                        getData(jsonData.getString("step1"), page_setting);
                        getData(jsonData.getString("step2"), insert_photo);
                        getData(jsonData.getString("step3"), introduce);
                        getData(jsonData.getString("step4"), career);
                        getData(jsonData.getString("step5"), available_services);
                        getData(jsonData.getString("step6"), care_environment);
                        getData(jsonData.getString("step7"), cost);
                        getData(jsonData.getString("step8"), rules);

                    } else if (!jsonObject.getBoolean("return")) {
                        if("login".equals(jsonObject.getString("type"))){
                            Toast.makeText(PageManagementActivity.this, "로그인 해주세요 ! ", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(PageManagementActivity.this, LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            oneBtnDialog = new OneBtnDialog(PageManagementActivity.this, "페이지 정보를 불러올 수 없습니다 !", "확인");
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

    void getData(String step, TextView textView) {
        if ("1".equals(step)) {
            textView.setText("등록");
            textView.setTextColor(getResources().getColor(R.color.mainTextGray));
        } else if ("0".equals(step)) {
            textView.setText("미등록");
            textView.setTextColor(getResources().getColor(R.color.starOnRed));
        }
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
