package com.woojeong.global.ifriend;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import static com.woojeong.global.ifriend.GlobalApplication.setDarkMode;

public class PasswordFindActivity extends AppCompatActivity {
    private static String TAG = "PasswordFindActivity";

    Context context;
    AQuery aQuery = null;
    InputMethodManager imm;
    ImageView back;
    EditText name, phone;
    TextView certify1, next;
    FrameLayout next_con, back_con;
    OneBtnDialog oneBtnDialog;
    boolean isClick;
    int secCount;
    Handler handler = new Handler();
    Runnable count = new Runnable() {
        public void run() {
            secCount--;
            handler.postDelayed(this, 1000);
            if (secCount < 0) {
                handler.removeCallbacks(count);
                isClick = false;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_find);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#ffffff"));
            window.setBackgroundDrawable(null);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                setDarkMode(PasswordFindActivity.this, true);
            }
        }

        aQuery = new AQuery(this);
        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        back_con = findViewById(R.id.back_con);
        back_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back.callOnClick();
            }
        });
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        certify1 = findViewById(R.id.certify1);
        certify1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);

                if ("".equals(name.getText().toString())) {
                    oneBtnDialog = new OneBtnDialog(PasswordFindActivity.this, "이름(실명)을\n입력해주세요 !\n예) 홍길동", "확인");
                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    oneBtnDialog.setCancelable(false);
                    oneBtnDialog.show();
                    return;
                } else if ("".equals(phone.getText().toString())) {
                    oneBtnDialog = new OneBtnDialog(PasswordFindActivity.this, "핸드폰 번호를\n입력해주세요 !\n예) 01012345678", "확인");
                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    oneBtnDialog.setCancelable(false);
                    oneBtnDialog.show();
                    return;
                } else if (!checkPhoneNumber(phone.getText().toString().replaceAll("-", "").replaceAll("\\)", "").trim())) {
                    oneBtnDialog = new OneBtnDialog(PasswordFindActivity.this, "핸드폰 번호를\n다시 입력해주세요 !\n예) 01012345678", "확인");
                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    oneBtnDialog.setCancelable(false);
                    oneBtnDialog.show();
                    return;
                } else if (phone.length() != 11) {
                    oneBtnDialog = new OneBtnDialog(PasswordFindActivity.this, "핸드폰 번호를\n다시 입력해주세요 !\n예) 01012345678", "확인");
                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    oneBtnDialog.setCancelable(false);
                    oneBtnDialog.show();
                    return;
                }

                SharedPreferences get_token = getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
                final String getToken = get_token.getString("Token", "");
                final String url = ServerUrl.getBaseUrl() + "/login/resetpass";
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("phone", phone.getText().toString());
                params.put("name", name.getText().toString());
                aQuery.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
                    @Override
                    public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
                        Log.i(TAG, " " + jsonObject);
                        try {
                            if (jsonObject.getBoolean("return")) {    //return이 true 면?
                                oneBtnDialog = new OneBtnDialog(PasswordFindActivity.this, "임시 비밀 번호를\n휴대폰번호로 전송했습니다.", "확인");
                                oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                oneBtnDialog.setCancelable(false);
                                oneBtnDialog.show();
                                return;
                            } else if (!jsonObject.getBoolean("return")) {

                                if ("sms".equals(jsonObject.getString("type"))) {
                                    //휴대폰 번호 변경 완료
                                    oneBtnDialog = new OneBtnDialog(PasswordFindActivity.this, "SMS 전송을 실패하였습니다.\n고객센터로 문의해주세요 !", "확인");
                                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                    oneBtnDialog.setCancelable(false);
                                    oneBtnDialog.show();
                                    return;
                                } else if ("pass".equals(jsonObject.getString("type"))) {
                                    oneBtnDialog = new OneBtnDialog(PasswordFindActivity.this, "입력한 정보와 일치하는\n회원이 없습니다.", "확인");
                                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                    oneBtnDialog.setCancelable(false);
                                    oneBtnDialog.show();
                                    return;
                                } else{
                                    oneBtnDialog = new OneBtnDialog(PasswordFindActivity.this, "다시 시도해주세요 !", "확인");
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
        next_con = findViewById(R.id.next_con);
        next = findViewById(R.id.next);
        next_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                next.callOnClick();
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);

                Intent intent = new Intent(PasswordFindActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    boolean checkPhoneNumber(String number) {    //핸드폰 번호 형식 체크
        boolean checkPhoneNumber = Pattern.matches("(01[016789])(\\d{3,4})(\\d{4})", number);
        return checkPhoneNumber;
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
