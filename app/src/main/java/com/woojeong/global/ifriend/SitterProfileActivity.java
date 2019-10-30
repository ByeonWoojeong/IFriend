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
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.woojeong.global.ifriend.DTO.Career;
import com.woojeong.global.ifriend.DTO.Three;
import com.woojeong.global.ifriend.DTO.Two;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static com.woojeong.global.ifriend.GlobalApplication.setDarkMode;

public class SitterProfileActivity extends AppCompatActivity {
    private static String TAG = "SitterProfileActivity";
    Context context;
    InputMethodManager ipmm;
    AQuery aQuery = null;
    OneBtnDialog oneBtnDialog;

    FrameLayout back_con, sitter_photo_con;
    ImageView back, sitter_photo;
    ScrollView scrollView;
    TextView  sitter_name, sitter_age, sitter_location, introduce;

    ListView sitter_child_list, career_list;
    ArrayList<Two> childData;
    ArrayList<Career> careerData;
    SitterChildAdapter2 sitterChildAdapter;
    CareerAdapter careerAdapter;

    String getMember = "";
    Intent sitterIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sitter_profile);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#ffffff"));
            window.setBackgroundDrawable(null);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                setDarkMode(SitterProfileActivity.this, true);
            }
        }

        sitterIntent = getIntent();
        getMember = sitterIntent.getStringExtra("member");
        Log.i(TAG, " getMember " + getMember);

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

        sitter_photo_con = findViewById(R.id.sitter_photo_con);
        sitter_photo = findViewById(R.id.sitter_photo);
        scrollView = findViewById(R.id.scrollView);
        sitter_name = findViewById(R.id.sitter_name);
        sitter_age = findViewById(R.id.sitter_age);
        sitter_location = findViewById(R.id.sitter_location);
        introduce = findViewById(R.id.introduce);
        sitter_child_list = findViewById(R.id.sitter_child_list);
        career_list = findViewById(R.id.career_list);

        childData = new ArrayList<>();
        careerData = new ArrayList<>();
        sitterChildAdapter = new SitterChildAdapter2(SitterProfileActivity.this, R.layout.list_sitter_child_item, childData, sitter_child_list, "1");
        careerAdapter = new CareerAdapter(SitterProfileActivity.this, R.layout.list_career_item, careerData, career_list, false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewSettings();
        scrollView.postDelayed(new Runnable() {
            @Override
            public void run() {
                scrollView.scrollTo(0, 0);
            }
        }, 700);
    }

    void viewSettings(){
        SharedPreferences get_token = getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
        final String getToken = get_token.getString("Token", "");
        Log.i(TAG, "TOKEN" + getToken);
        final String url = ServerUrl.getBaseUrl() + "/friend/profile";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("member", getMember);
        Log.i(TAG, " params " + params);
        aQuery.ajax(url, params, String.class, new AjaxCallback<String>() {
            @Override
            public void callback(String url, String str, AjaxStatus status) {
                Log.i(TAG, " jsonObject " + str);
                try {
                    JSONObject jsonObject = new JSONObject(str);
                    if (jsonObject.getBoolean("return")) {    //return이 true 면?
                        JSONObject jsonData = jsonObject.getJSONObject("data");

                        circleImage(sitter_photo, ServerUrl.getBaseUrl() + "/uploads/images/origin/" + jsonData.getString("profile"));

                        sitter_name.setText(jsonData.getString("name"));
                        sitter_age.setText(jsonData.getString("age") + "세");
                        sitter_location.setText(jsonData.getString("addr"));
                        introduce.setText(jsonData.getString("intro"));

                        JSONArray jsonArrayChild = new JSONArray(jsonData.getString("child"));
                        JSONArray jsonArrayOffice = new JSONArray(jsonData.getString("office"));

                        if (jsonArrayChild.length() == 0) {
                            sitter_child_list.setVisibility(View.GONE);
                        } else {
                            sitter_child_list.setVisibility(View.VISIBLE);
                            for (int i = 0; i < jsonArrayChild.length(); i++) {
                                JSONObject getJsonObject = jsonArrayChild.getJSONObject(i);
                                childData.add(new Two(getJsonObject.getString("age"), getJsonObject.getString("gender")));
                            }
                            sitter_child_list.setAdapter(sitterChildAdapter);
                            sitterChildAdapter.notifyDataSetChanged();
                            sitter_child_list.setDivider(null);
                            setListViewHeight(sitterChildAdapter, sitter_child_list);
                        }

                        if (jsonArrayOffice.length() == 0) {
                            career_list.setVisibility(View.GONE);
                        } else {
                            career_list.setVisibility(View.VISIBLE);
                            for (int i = 0; i < jsonArrayOffice.length(); i++) {
                                JSONObject getJsonObject = jsonArrayOffice.getJSONObject(i);
                                careerData.add(new Career(getJsonObject.getString("name"), getJsonObject.getString("start"), getJsonObject.getString("end")));
                            }
                            career_list.setAdapter(careerAdapter);
                            careerAdapter.notifyDataSetChanged();
                            career_list.setDivider(null);
                            setListViewHeight(careerAdapter, career_list);
                        }

                    } else if (!jsonObject.getBoolean("return")) {
                        if("login".equals(jsonObject.getString("type"))){
                            Toast.makeText(SitterProfileActivity.this, "로그인 해주세요 ! ", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(context, LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            oneBtnDialog = new OneBtnDialog(SitterProfileActivity.this, "정보를 불러올 수 없습니다 !", "확인");
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

    public void setListViewHeight(BaseAdapter adpater, ListView listView) {
        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);

        for (int size = 0; size < listView.getCount(); size++) {
            View listItem = adpater.getView(size, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listView.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
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
