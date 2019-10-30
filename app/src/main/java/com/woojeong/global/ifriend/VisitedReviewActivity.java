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
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

import static com.woojeong.global.ifriend.GlobalApplication.setDarkMode;

public class VisitedReviewActivity extends AppCompatActivity {
    private static String TAG = "VisitedReviewActivity";
    Context context;
    InputMethodManager ipmm;
    AQuery aQuery = null;
    OneBtnDialog oneBtnDialog;

    FrameLayout back_con, ok_con;
    ImageView back, customer_photo, review_img, sitter_photo;
    ScrollView scrollView;
    TextView customer_name, review_date, review_content, comment_content, comment_delete, comment_date, ok;
    MaterialRatingBar rating_bar;
    LinearLayout comment_con, input_con;
    EditText input;

    String getReserve;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visited_review);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#ffffff"));
            window.setBackgroundDrawable(null);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                setDarkMode(VisitedReviewActivity.this, true);
            }
        }

        getReserve = getIntent().getStringExtra("reserve");

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

        customer_photo = findViewById(R.id.customer_photo);
        review_img = findViewById(R.id.review_img);
        sitter_photo = findViewById(R.id.sitter_photo);

        scrollView = findViewById(R.id.scrollView);

        customer_name = findViewById(R.id.customer_name);
        review_date = findViewById(R.id.review_date);
        review_content = findViewById(R.id.review_content);
        comment_content = findViewById(R.id.comment_content);
        comment_delete = findViewById(R.id.comment_delete);
        comment_date = findViewById(R.id.comment_date);
        ok = findViewById(R.id.ok);

        rating_bar = findViewById(R.id.rating_bar);

        comment_con = findViewById(R.id.comment_con);
        input_con = findViewById(R.id.input_con);

        input = findViewById(R.id.input);
        ipmm.hideSoftInputFromWindow(input.getWindowToken(), 0);


        comment_con.setVisibility(View.GONE);
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
                String getToken = get_token.getString("Token", "");
                String url = ServerUrl.getBaseUrl() + "/review/reply";
                final Map<String, Object> params = new HashMap<String, Object>();
                params.put("reserve", getReserve);
                params.put("content", input.getText().toString());
                Log.i(TAG, " params " + params);
                aQuery.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
                    @Override
                    public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
                        Log.i(TAG, " jsonObject " + jsonObject );
                        try {
                            if (jsonObject.getBoolean("return")) {    //return이 true 면?
                                ipmm.hideSoftInputFromWindow(input.getWindowToken(), 0);
                                input.setText("");
                                comment_con.setVisibility(View.VISIBLE);
                                input_con.setVisibility(View.GONE);
                                viewSettings();
                            } else if (!jsonObject.getBoolean("return")) {
                                if("login".equals(jsonObject.getString("type"))){
                                    Toast.makeText(VisitedReviewActivity.this, "로그인 해주세요 ! ", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(context, LoginActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    oneBtnDialog = new OneBtnDialog(VisitedReviewActivity.this, "댓글 작성 실패", "확인");
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

        comment_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences get_token = getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
                String getToken = get_token.getString("Token", "");
                String url = ServerUrl.getBaseUrl() + "/review/delete";
                final Map<String, Object> params = new HashMap<String, Object>();
                params.put("reserve", getReserve);
                Log.i(TAG, " params " + params);
                aQuery.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
                    @Override
                    public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
                        Log.i(TAG, " jsonObject " + jsonObject );
                        try {
                            if (jsonObject.getBoolean("return")) {    //return이 true 면?
                                ipmm.hideSoftInputFromWindow(input.getWindowToken(), 0);
                                input.setText("");
                                comment_con.setVisibility(View.GONE);
                                input_con.setVisibility(View.VISIBLE);
                            } else if (!jsonObject.getBoolean("return")) {
                                if("login".equals(jsonObject.getString("type"))){
                                    Toast.makeText(VisitedReviewActivity.this, "로그인 해주세요 ! ", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(context, LoginActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    oneBtnDialog = new OneBtnDialog(VisitedReviewActivity.this, "댓글 삭제 실패", "확인");
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

    @Override
    protected void onResume() {
        super.onResume();
        viewSettings();
    }

    void viewSettings() {
        SharedPreferences get_token = getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
        final String getToken = get_token.getString("Token", "");
        final String url = ServerUrl.getBaseUrl() + "/review/detail";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("reserve", getReserve);
        Log.i(TAG, " params " + params);
        aQuery.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
                Log.i(TAG, " jsonObject " + jsonObject);
                try {
                    if (jsonObject.getBoolean("return")) {    //return이 true 면?
                        JSONObject jsonData = jsonObject.getJSONObject("data");
                        circleImage(customer_photo, ServerUrl.getBaseUrl() + "/uploads/images/origin/" + jsonData.getString("mom_image"));
                        customer_name.setText(jsonData.getString("mom_name"));
                        review_date.setText(jsonData.getString("content_date"));
                        review_content.setText(jsonData.getString("content"));
                        if(!"".equals(jsonData.getString("reply"))){
                            comment_con.setVisibility(View.VISIBLE);
                            circleImage(sitter_photo, ServerUrl.getBaseUrl() + "/uploads/images/origin/" + jsonData.getString("friend_image"));
                            comment_content.setText(jsonData.getString("reply"));
                            comment_date.setText(jsonData.getString("reply_date"));
                            input_con.setVisibility(View.GONE);
                        }else {
                            comment_con.setVisibility(View.GONE);
                            input_con.setVisibility(View.VISIBLE);
                        }
                    } else if (!jsonObject.getBoolean("return")) {
                        if("login".equals(jsonObject.getString("type"))){
                            Toast.makeText(VisitedReviewActivity.this, "로그인 해주세요 ! ", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(context, LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            oneBtnDialog = new OneBtnDialog(VisitedReviewActivity.this, "정보를 불러올 수 없습니다 !", "확인");
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

    private void circleImage(ImageView imageView, String getImg){
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
