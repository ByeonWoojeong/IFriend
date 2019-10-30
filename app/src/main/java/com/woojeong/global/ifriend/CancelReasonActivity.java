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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.woojeong.global.ifriend.DTO.Career;
import com.woojeong.global.ifriend.DTO.Two;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static com.woojeong.global.ifriend.GlobalApplication.setDarkMode;

public class CancelReasonActivity extends AppCompatActivity {
    private static String TAG = "CancelReasonActivity";
    Context context;
    InputMethodManager ipmm;
    AQuery aQuery = null;
    OneBtnDialog oneBtnDialog;

    FrameLayout back_con, profile_con;
    ImageView back, photo, more;
    ScrollView scrollView;
    TextView name, date, time, total_time, total_people, total_amount, cancel_time, cancel_reason, etc_detail;
    LinearLayout etc_detail_con;

    String getReserve, getMember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel_reason);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#ffffff"));
            window.setBackgroundDrawable(null);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                setDarkMode(CancelReasonActivity.this, true);
            }
        }

        getReserve = getIntent().getStringExtra("reserve");
        Log.i(TAG, " getReserve " + getReserve);

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

        profile_con = findViewById(R.id.profile_con);

        photo = findViewById(R.id.photo);
        more = findViewById(R.id.more);

        scrollView = findViewById(R.id.scrollView);

        name = findViewById(R.id.name);
        date = findViewById(R.id.date);
        time = findViewById(R.id.time);
        total_time = findViewById(R.id.total_time);
        total_people = findViewById(R.id.total_people);
        total_amount = findViewById(R.id.total_amount);
        cancel_time = findViewById(R.id.cancel_time);
        cancel_reason = findViewById(R.id.cancel_reason);
        etc_detail = findViewById(R.id.etc_detail);

        etc_detail_con = findViewById(R.id.etc_detail_con);

        viewSettings();

        profile_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                more.callOnClick();
            }
        });
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CancelReasonActivity.this, CustomerProfileActivity.class);
                intent.putExtra("member", getMember);
                startActivity(intent);
            }
        });
    }

    void viewSettings() {
        SharedPreferences get_token = getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
        final String getToken = get_token.getString("Token", "");
        final String url = ServerUrl.getBaseUrl() + "/reserve/canceldetail";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("reserve", getReserve);
        Log.i(TAG, " params " + params);
        aQuery.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
                Log.i(TAG, " " + jsonObject);
                try {
                    if (jsonObject.getBoolean("return")) {    //return이 true 면?
                        JSONObject jsonData = jsonObject.getJSONObject("data");

                        circleImage(photo, ServerUrl.getBaseUrl() + "/uploads/images/origin/" + jsonData.getString("profile"));

                        getMember = jsonData.getString("member");

                        name.setText(jsonData.getString("name"));
                        date.setText(jsonData.getString("dates"));
                        time.setText(jsonData.getString("times"));

                        total_time.setText(jsonData.getString("time") + "시간");
                        total_people.setText(jsonData.getString("child"));

                        int cost = (int) Integer.parseInt((jsonData.getString("cost").replace("원", "")));
                        DecimalFormat decimalFormat = new DecimalFormat("###,###");
                        String costStr = decimalFormat.format(cost);

                        total_amount.setText(costStr + "원");
                        cancel_time.setText(jsonData.getString("cancel"));

                        if ("1".equals(jsonData.getString("type"))) {
                            cancel_reason.setText("늦은 예약 승인 및 메세지 응답");
                        } else if ("2".equals(jsonData.getString("type"))) {
                            cancel_reason.setText("이웃 친구의 불친절한 응대");
                        } else if ("3".equals(jsonData.getString("type"))) {
                            cancel_reason.setText("다른 날짜로 예약 변경");
                        } else if ("4".equals(jsonData.getString("type"))) {
                            cancel_reason.setText("개인적인 사정");
                        } else if ("5".equals(jsonData.getString("type"))) {
                            cancel_reason.setText("기타");
                        }

                        etc_detail.setText(jsonData.getString("detail"));

                    } else if (!jsonObject.getBoolean("return")) {
                        if("login".equals(jsonObject.getString("type"))){
                            Toast.makeText(CancelReasonActivity.this, "로그인 해주세요 ! ", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(context, LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            oneBtnDialog = new OneBtnDialog(CancelReasonActivity.this, "정보를 불러올 수 없습니다 !", "확인");
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

    private void circleImage(ImageView imageView, String getImg) {
        Log.i(TAG, " circleImage : " + getImg);
        Glide.with(context)
                .load(getImg)
                .apply(RequestOptions.bitmapTransform(new CropCircleTransformation()))
                .into(imageView);
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
