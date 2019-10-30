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
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.woojeong.global.ifriend.GlobalApplication.setDarkMode;

public class CancelAndRefundActivity extends AppCompatActivity {
    private static String TAG = "CancelAndRefundActivity";
    Context context;
    InputMethodManager ipmm;
    AQuery aQuery = null;
    OneBtnDialog oneBtnDialog;

    FrameLayout back_con;
    ImageView back;
    TextView title, cancel_guide, ok;
    ScrollView scrollView;
    LinearLayout refund_rule_con, ok_con;
    RadioGroup radio_group;
    RadioButton late_radio, unkind_radio, change_radio, personal_radio, etc_radio;
    EditText etc_edit;

    Intent getIntent;
    String getWhat, cancelNrefund, reserveNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel_and_refund);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#ffffff"));
            window.setBackgroundDrawable(null);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                setDarkMode(CancelAndRefundActivity.this, true);
            }
        }


        getIntent = getIntent();
        reserveNumber  = getIntent.getStringExtra("reserve");
        getWhat = getIntent.getStringExtra("what");

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

        title = findViewById(R.id.title);
        cancel_guide = findViewById(R.id.cancel_guide);
        ok = findViewById(R.id.ok);
        scrollView = findViewById(R.id.scrollView);
        scrollView.postDelayed(new Runnable() {
            @Override
            public void run() {
                scrollView.scrollTo(0, 0);
            }
        }, 700);

        refund_rule_con = findViewById(R.id.refund_rule_con);
        ok_con = findViewById(R.id.ok_con);

        radio_group = findViewById(R.id.radio_group);

        late_radio = findViewById(R.id.late_radio);
        unkind_radio = findViewById(R.id.unkind_radio);
        change_radio = findViewById(R.id.change_radio);
        personal_radio = findViewById(R.id.personal_radio);
        etc_radio = findViewById(R.id.etc_radio);

        etc_edit = findViewById(R.id.etc_edit);

        if("cancel".equals(getWhat)){
            title.setText("예약 취소 사유");
            cancel_guide.setVisibility(View.VISIBLE);
            refund_rule_con.setVisibility(View.GONE);
            ok.setText("예약 취소");
        } else if("refund".equals(getWhat)){
            title.setText("환불 사유");
            cancel_guide.setVisibility(View.GONE);
            refund_rule_con.setVisibility(View.VISIBLE);
            ok.setText("환불 신청");
        }

        radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.late_radio) {
                    etc_edit.setEnabled(false);
                    cancelNrefund = "1";
                } else if (checkedId == R.id.unkind_radio) {
                    etc_edit.setEnabled(false);
                    cancelNrefund = "2";
                } else if (checkedId == R.id.change_radio) {
                    etc_edit.setEnabled(false);
                    cancelNrefund = "3";
                } else if (checkedId == R.id.personal_radio) {
                    etc_edit.setEnabled(false);
                    cancelNrefund = "4";
                } else if (checkedId == R.id.etc_radio) {
                    etc_edit.setEnabled(true);
                    cancelNrefund = "5";
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
                if ("예약 취소".equals(ok.getText().toString())) {

                    SharedPreferences get_token = getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
                    final String getToken = get_token.getString("Token", "");
                    final String url = ServerUrl.getBaseUrl() + "/reserve/cancel";
                    Map<String, Object> params = new HashMap<String, Object>();
                    params.put("cancel", cancelNrefund);
                    params.put("detail", etc_edit.getText().toString());
                    params.put("reserve", reserveNumber);
                    aQuery.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
                        @Override
                        public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
                            Log.i(TAG, " " + jsonObject);
                            try {
                                if (jsonObject.getBoolean("return")) {    //return이 true 면?

                                    oneBtnDialog = new OneBtnDialog(CancelAndRefundActivity.this, "예약이 취소되었습니다 !", "확인");
                                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                    oneBtnDialog.setCancelable(false);
                                    oneBtnDialog.show();
                                    return;

                                } else if (!jsonObject.getBoolean("return")) {
                                    if("login".equals(jsonObject.getString("type"))){
                                        Toast.makeText(CancelAndRefundActivity.this, "로그인 해주세요 ! ", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(context, LoginActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        oneBtnDialog = new OneBtnDialog(CancelAndRefundActivity.this, "예약 취소를 실패하였습니다 !", "확인");
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

                } else if ("환불 신청".equals(ok.getText().toString())) {

                    SharedPreferences get_token = getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
                    final String getToken = get_token.getString("Token", "");
                    final String url = ServerUrl.getBaseUrl() + "/reserve/refund";
                    Map<String, Object> params = new HashMap<String, Object>();
                    params.put("type", cancelNrefund);
                    params.put("detail", etc_edit.getText().toString());
                    params.put("reserve", reserveNumber);
                    aQuery.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
                        @Override
                        public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
                            Log.i(TAG, " " + jsonObject);
                            try {
                                if (jsonObject.getBoolean("return")) {    //return이 true 면?
                                    oneBtnDialog = new OneBtnDialog(CancelAndRefundActivity.this, "환불이 신청되었었습니다 !", "확인");
                                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                    oneBtnDialog.setCancelable(false);
                                    oneBtnDialog.show();
                                    return;
                                } else if (!jsonObject.getBoolean("return")) {
                                    if("login".equals(jsonObject.getString("type"))){
                                        Toast.makeText(CancelAndRefundActivity.this, "로그인 해주세요 ! ", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(context, LoginActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        oneBtnDialog = new OneBtnDialog(CancelAndRefundActivity.this, "환불 신청을 실패하였습니다 !", "확인");
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
