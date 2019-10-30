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
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.woojeong.global.ifriend.DTO.Profit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.woojeong.global.ifriend.GlobalApplication.setDarkMode;

public class InputAccountActivity extends AppCompatActivity {
    private static String TAG = "InputAccountActivity";
    Context context;
    InputMethodManager ipmm;
    AQuery aQuery = null;
    OneBtnDialog oneBtnDialog;
    OkDialog okDialog;

    FrameLayout back_con, ok_con;
    ImageView back;
    EditText name, bank, number;
    TextView ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_account);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#ffffff"));
            window.setBackgroundDrawable(null);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                setDarkMode(InputAccountActivity.this, true);
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
        name = findViewById(R.id.name);
        bank = findViewById(R.id.bank);
        number = findViewById(R.id.number);

        viewSettings();

        ok_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ok.callOnClick();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("".equals(name.getText().toString())) {
                    oneBtnDialog = new OneBtnDialog(InputAccountActivity.this, "예금주를 입력해 주세요 !", "확인");
                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    oneBtnDialog.setCancelable(false);
                    oneBtnDialog.show();
                    return;
                } else if ("".equals(bank.getText().toString())) {
                    oneBtnDialog = new OneBtnDialog(InputAccountActivity.this, "은행을 입력해 주세요 !", "확인");
                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    oneBtnDialog.setCancelable(false);
                    oneBtnDialog.show();
                    return;
                } else if ("".equals(number.getText().toString())) {
                    oneBtnDialog = new OneBtnDialog(InputAccountActivity.this, "계좌 번호를 입력해 주세요 !", "확인");
                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    oneBtnDialog.setCancelable(false);
                    oneBtnDialog.show();
                    return;
                } else {
                    SharedPreferences get_token = getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
                    final String getToken = get_token.getString("Token", "");
                    final String url = ServerUrl.getBaseUrl() + "/pay/update";
                    Map<String, Object> params = new HashMap<String, Object>();
                    params.put("name", name.getText().toString());
                    params.put("bank", bank.getText().toString());
                    params.put("account", number.getText().toString());
                    Log.i(TAG, " params " + params);
                    aQuery.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
                        @Override
                        public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
                            Log.i(TAG, " jsonObject " + jsonObject);
                            try {
                                if (jsonObject.getBoolean("return")) {    //return이 true 면?
                                    okDialog = new OkDialog(InputAccountActivity.this, "수령 계좌 정보를\n전달하였습니다 !", "확인");
                                    okDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                    okDialog.setCancelable(false);
                                    okDialog.show();
                                    return;
                                } else if (!jsonObject.getBoolean("return")) {
                                    if("login".equals(jsonObject.getString("type"))){
                                        Toast.makeText(InputAccountActivity.this, "로그인 해주세요 ! ", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(InputAccountActivity.this, LoginActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        oneBtnDialog = new OneBtnDialog(InputAccountActivity.this, "수령 계좌 정보 전달에\n실패하였습니다.", "확인");
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
            }
        });
    }

    void viewSettings() {
        SharedPreferences get_token = getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
        final String getToken = get_token.getString("Token", "");
        final String url = ServerUrl.getBaseUrl() + "/pay/mydata";
        Map<String, Object> params = new HashMap<String, Object>();
        aQuery.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
                Log.i(TAG, " " + jsonObject);
                try {
                    if (jsonObject.getBoolean("return")) {    //return이 true 면?
                        JSONObject jsonData = jsonObject.getJSONObject("data");

                        name.setText(jsonData.getString("name"));
                        bank.setText(jsonData.getString("bank"));
                        number.setText(jsonData.getString("account"));
                    } else if (!jsonObject.getBoolean("return")) {
                        if("login".equals(jsonObject.getString("type"))){
                            Toast.makeText(InputAccountActivity.this, "로그인 해주세요 ! ", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(context, LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            oneBtnDialog = new OneBtnDialog(InputAccountActivity.this, "정보를 불러올 수 없습니다 !", "확인");
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
}
