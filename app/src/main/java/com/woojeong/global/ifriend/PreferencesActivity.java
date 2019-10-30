package com.woojeong.global.ifriend;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import static com.woojeong.global.ifriend.GlobalApplication.setDarkMode;

public class PreferencesActivity extends AppCompatActivity {
    private static String TAG = "PreferencesActivity";
    Context context;
    InputMethodManager ipmm;
    AQuery aQuery = null;
    OneBtnDialog oneBtnDialog;
    LogoutDialog logoutDialog;
    SignOutDialog signOutDialog;

    FrameLayout back_con, push_con, down_con, logout_con, leave_con;
    ImageView back, down;
    LinearLayout into_push_con;
    SwitchCompat menu1_status_switch, menu2_message_switch, menu3_journal_btn, menu4_agree_btn;
    TextView logout, leave;
    boolean isShow = false;

    String getMode, getSupport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#ffffff"));
            window.setBackgroundDrawable(null);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                setDarkMode(PreferencesActivity.this, true);
            }
        }

        getMode = getIntent().getStringExtra("mode");
        getSupport = getIntent().getStringExtra("support");

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

        push_con = findViewById(R.id.push_con);
        down_con = findViewById(R.id.down_con);
        logout_con = findViewById(R.id.logout_con);
        leave_con = findViewById(R.id.leave_con);

        down = findViewById(R.id.down);
        push_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                down.callOnClick();
            }
        });
        down_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                down.callOnClick();
            }
        });
        down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isShow) {    //true-안보이면 보이게
                    down.setBackgroundDrawable(getResources().getDrawable(R.drawable.up));
                    into_push_con.setVisibility(View.VISIBLE);
                    isShow = true;
                } else {    //false-보이면 안보이게
                    down.setBackgroundDrawable(getResources().getDrawable(R.drawable.spinner_down));
                    into_push_con.setVisibility(View.GONE);
                    isShow = false;
                }
            }
        });

        into_push_con = findViewById(R.id.into_push_con);

        menu1_status_switch = findViewById(R.id.menu1_status_switch);
        menu2_message_switch = findViewById(R.id.menu2_message_switch);
        menu3_journal_btn = findViewById(R.id.menu3_journal_btn);
        menu4_agree_btn = findViewById(R.id.menu4_agree_btn);

        getPush();

        logout = findViewById(R.id.logout);
        leave = findViewById(R.id.leave);

        setPush();

        logout_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout.callOnClick();
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutDialog = new LogoutDialog(context);
                logoutDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                logoutDialog.setCancelable(false);
                logoutDialog.show();
            }
        });

        leave_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leave.callOnClick();
            }
        });
        leave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOutDialog = new SignOutDialog(context);
                signOutDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                signOutDialog.setCancelable(false);
                signOutDialog.show();
            }
        });
    }

    void getPush() {
        SharedPreferences get_token = getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
        final String getToken = get_token.getString("Token", "");
        String url = ServerUrl.getBaseUrl() + "/setting/getpush";
        Map<String, Object> params = new HashMap<String, Object>();
        aQuery.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
                try {

                    if (jsonObject.getBoolean("return")) {
                        JSONObject jsonData = jsonObject.getJSONObject("data");

                        if ("1".equals(jsonData.getString("push1"))) {
                            menu1_status_switch.setChecked(true);
                        } else {
                            menu1_status_switch.setChecked(false);
                        }

                        if ("1".equals(jsonData.getString("push2"))) {
                            menu2_message_switch.setChecked(true);
                        } else {
                            menu2_message_switch.setChecked(false);
                        }

                        if ("1".equals(jsonData.getString("push3"))) {
                            menu3_journal_btn.setChecked(true);
                        } else {
                            menu3_journal_btn.setChecked(false);
                        }

                        if ("1".equals(jsonData.getString("push4"))) {
                            menu4_agree_btn.setChecked(true);
                        } else {
                            menu4_agree_btn.setChecked(false);
                        }

                    } else if (!jsonObject.getBoolean("return")) {
                        if("login".equals(jsonObject.getString("type"))){
                            Toast.makeText(PreferencesActivity.this, "로그인 해주세요 ! ", Toast.LENGTH_SHORT).show();
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

    void setPush() {
        menu1_status_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences get_token = getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
                final String getToken = get_token.getString("Token", "");
                String url = ServerUrl.getBaseUrl() + "/setting/setpush";
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("type", 1);
                if (isChecked) {
                    params.put("value", 1);
                } else {
                    params.put("value", 0);
                }
                aQuery.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
                    @Override
                    public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
                        try {

                            if (jsonObject.getBoolean("return")) {

                            } else if (!jsonObject.getBoolean("return")) {
                                if("login".equals(jsonObject.getString("type"))){
                                    Toast.makeText(PreferencesActivity.this, "로그인 해주세요 ! ", Toast.LENGTH_SHORT).show();
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
        });
        menu2_message_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences get_token = getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
                final String getToken = get_token.getString("Token", "");
                String url = ServerUrl.getBaseUrl() + "/setting/setpush";
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("type", 2);
                if (isChecked) {
                    params.put("value", 1);
                } else {
                    params.put("value", 0);
                }
                aQuery.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
                    @Override
                    public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
                        try {

                            if (jsonObject.getBoolean("return")) {

                            } else if (!jsonObject.getBoolean("return")) {
                                if("login".equals(jsonObject.getString("type"))){
                                    Toast.makeText(PreferencesActivity.this, "로그인 해주세요 ! ", Toast.LENGTH_SHORT).show();
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
        });
        menu3_journal_btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences get_token = getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
                final String getToken = get_token.getString("Token", "");
                String url = ServerUrl.getBaseUrl() + "/setting/setpush";
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("type", 3);
                if (isChecked) {
                    params.put("value", 1);
                } else {
                    params.put("value", 0);
                }
                aQuery.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
                    @Override
                    public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
                        try {

                            if (jsonObject.getBoolean("return")) {

                            } else if (!jsonObject.getBoolean("return")) {
                                if("login".equals(jsonObject.getString("type"))){
                                    Toast.makeText(PreferencesActivity.this, "로그인 해주세요 ! ", Toast.LENGTH_SHORT).show();
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
        });
        menu4_agree_btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences get_token = getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
                final String getToken = get_token.getString("Token", "");
                String url = ServerUrl.getBaseUrl() + "/setting/setpush";
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("type", 4);
                if (isChecked) {
                    params.put("value", 1);
                } else {
                    params.put("value", 0);
                }
                aQuery.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
                    @Override
                    public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
                        try {

                            if (jsonObject.getBoolean("return")) {

                            } else if (!jsonObject.getBoolean("return")) {
                                if("login".equals(jsonObject.getString("type"))){
                                    Toast.makeText(PreferencesActivity.this, "로그인 해주세요 ! ", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(PreferencesActivity.this, LoginActivity.class);
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
                }
            });
        }
    }

    public class LogoutDialog extends Dialog {
        LogoutDialog logoutDialog = this;
        Context context;

        public LogoutDialog(final Context context) {
            super(context);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialog_two_btn);
            getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            this.context = context;
            TextView title1 = (TextView) findViewById(R.id.title1);
            TextView title2 = (TextView) findViewById(R.id.title2);
            TextView btn1 = (TextView) findViewById(R.id.btn1);
            TextView btn2 = (TextView) findViewById(R.id.btn2);
            title2.setVisibility(View.GONE);
            title1.setText("로그아웃 하시겠습니까?");
            btn1.setText("취소");
            btn2.setText("확인");
            btn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    logoutDialog.dismiss();
                }
            });
            btn2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences get_token = context.getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
                    final String getToken = get_token.getString("Token", "");
                    String url = ServerUrl.getBaseUrl() + "/logout";
                    Map<String, Object> params = new HashMap<String, Object>();
                    aQuery.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
                        @Override
                        public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
                            Log.i(TAG, "" + jsonObject);
                            try {
                                if (jsonObject.getBoolean("return")) {

                                    Toast.makeText(context, "아이 친구를 로그아웃 하였습니다.", Toast.LENGTH_SHORT).show();
                                    SharedPreferences prefLoginChecked = context.getSharedPreferences("modeChecked", Activity.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = prefLoginChecked.edit();
                                    editor.clear();
                                    editor.commit();

                                    SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
                                    SharedPreferences.Editor editor3 = pref.edit();
                                    editor3.clear();
                                    editor3.commit();

                                    SharedPreferences prefLoginChecked2 = getSharedPreferences("prefLoginChecked", Activity.MODE_PRIVATE);
                                    SharedPreferences.Editor editor4 = prefLoginChecked2.edit();
                                    editor4.clear();
                                    editor4.commit();

                                    SharedPreferences filteringData = getSharedPreferences("filtering", Activity.MODE_PRIVATE);
                                    SharedPreferences.Editor filterEditor = filteringData.edit();
                                    filterEditor.clear();
                                    filterEditor.commit();



                                    Intent intent = new Intent(PreferencesActivity.this, LoginActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();

                                } else if (!jsonObject.getBoolean("return")) {
                                    if("login".equals(jsonObject.getString("type"))){
                                        Toast.makeText(PreferencesActivity.this, "로그인 해주세요 ! ", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(PreferencesActivity.this, LoginActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                    Toast.makeText(context, "로그아웃에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                                }
                                logoutDialog.dismiss();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }.header("EPOCH-AGENT", "" + getToken).header("User-Agent", "android"));
                }
            });
        }
    }

    public class SignOutDialog extends Dialog {
        SignOutDialog signOutDialog = this;
        Context context;

        public SignOutDialog(final Context context) {
            super(context);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialog_two_btn);
            getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            this.context = context;
            TextView title1 = (TextView) findViewById(R.id.title1);
            TextView title2 = (TextView) findViewById(R.id.title2);
            TextView btn1 = (TextView) findViewById(R.id.btn1);
            TextView btn2 = (TextView) findViewById(R.id.btn2);
            title2.setVisibility(View.VISIBLE);

            if ("1".equals(getSupport) || "sitter".equals(getMode)) {
                title1.setText("탈퇴 시 관리자의 승인이 필요합니다.");
                title2.setText("탈퇴 신청 하시겠습니까?");
            } else {
                title1.setText("회원 탈퇴 시 아이친구 내모든 정보가\n삭제되며 이후 복구가 불가능합니다.");
                title2.setText("정말 탈퇴하시겠습니까?");
            }

            btn1.setText("취소");
            btn2.setText("확인");
            btn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    signOutDialog.dismiss();
                }
            });
            btn2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences get_token = context.getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
                    final String getToken = get_token.getString("Token", "");
                    String url = ServerUrl.getBaseUrl() + "/signout";
                    Map<String, Object> params = new HashMap<String, Object>();
                    aQuery.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
                        @Override
                        public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
                            Log.i(TAG, "" + jsonObject);
                            try {
                                if (jsonObject.getBoolean("return")) {
                                    if ("yes".equals(getSupport) || "sitter".equals(getMode)) {
                                        Toast.makeText(context, "회원 탈퇴가 신청 되었습니다.\n문의사항은 고객센터로 연락주세요.", Toast.LENGTH_SHORT).show();
                                        setResult(9999);
                                        finish();
                                        System.exit(0);
                                    } else {
                                        Toast.makeText(context, "아이친구를 탈퇴하였습니다.", Toast.LENGTH_SHORT).show();
                                        SharedPreferences prefLoginChecked = context.getSharedPreferences("prefLoginChecked", Activity.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = prefLoginChecked.edit();
                                        editor.clear();
                                        editor.commit();
                                        setResult(9999);
                                        finish();
                                        System.exit(0);
                                    }
                                } else if (!jsonObject.getBoolean("return")) {
                                    if("login".equals(jsonObject.getString("type"))){
                                        Toast.makeText(PreferencesActivity.this, "로그인 해주세요 ! ", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(PreferencesActivity.this, LoginActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        oneBtnDialog = new OneBtnDialog(PreferencesActivity.this, "탈퇴 처리 중 입니다 !", "확인");
                                        oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                        oneBtnDialog.setCancelable(false);
                                        oneBtnDialog.show();
                                        return;
                                    }

                                }
                                signOutDialog.dismiss();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }.header("EPOCH-AGENT", "" + getToken).header("User-Agent", "android"));
                }
            });
        }
    }

}
