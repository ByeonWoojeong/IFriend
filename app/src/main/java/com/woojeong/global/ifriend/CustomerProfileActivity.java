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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.woojeong.global.ifriend.DTO.Child;
import com.woojeong.global.ifriend.DTO.Review;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

import static com.woojeong.global.ifriend.GlobalApplication.setDarkMode;

public class CustomerProfileActivity extends AppCompatActivity {
    private static String TAG = "CustomerProfileActivity";
    Context context;
    InputMethodManager ipmm;
    AQuery aQuery = null;
    OneBtnDialog oneBtnDialog;

    FrameLayout back_con;
    ImageView back, photo, review_more;
    ScrollView scrollView;
    TextView name, age, gender, location, name2, review_count, review_average;
    LinearLayout review_simple;
    MaterialRatingBar rating_bar;

    ListView child_list, review_list;
    ArrayList<Review> reviewData;
    ReviewSimpleAdapter reviewAdapter;
    ArrayList<Child> childData;
    ChildAdapter childAdapter;

    Intent getIntent;
    String getMember;
    int reviewCnt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_profile);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#ffffff"));
            window.setBackgroundDrawable(null);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                setDarkMode(CustomerProfileActivity.this, true);
            }
        }

        getIntent = getIntent();
        getMember = getIntent.getStringExtra("member");

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

        photo = findViewById(R.id.photo);
        review_more = findViewById(R.id.review_more);

        scrollView = findViewById(R.id.scrollView);

        name = findViewById(R.id.name);
        age = findViewById(R.id.age);
        gender = findViewById(R.id.gender);
        location = findViewById(R.id.location);
        name2 = findViewById(R.id.name2);
        review_count = findViewById(R.id.review_count);
        review_average = findViewById(R.id.review_average);

        child_list = findViewById(R.id.child_list);
        review_list = findViewById(R.id.review_list);

        review_simple = findViewById(R.id.review_simple);

        rating_bar = findViewById(R.id.rating_bar);

        childData = new ArrayList<Child>();
        childAdapter = new ChildAdapter(CustomerProfileActivity.this, R.layout.list_child_item, childData, child_list, "profile");

        reviewData = new ArrayList<Review>();
        reviewAdapter = new ReviewSimpleAdapter(CustomerProfileActivity.this, R.layout.list_review_simple_item, reviewData, review_list);

        review_simple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(reviewCnt == 0){
                    oneBtnDialog = new OneBtnDialog(CustomerProfileActivity.this, "이용 후기가 없습니다 !", "확인");
                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    oneBtnDialog.setCancelable(false);
                    oneBtnDialog.show();
                    return;
                }
                Intent intent = new Intent(CustomerProfileActivity.this, ReviewsActivity.class);
                intent.putExtra("mom", getMember);
                startActivity(intent);
            }
        });

        viewSettings();
    }

    void viewSettings(){
        SharedPreferences get_token = getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
        final String getToken = get_token.getString("Token", "");
        final String url = ServerUrl.getBaseUrl() + "/profile/member";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("member", getMember);
        aQuery.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
                Log.i(TAG, " " + jsonObject);
                try {
                    reviewData.clear();
                    childData.clear();
                    if (jsonObject.getBoolean("return")) {    //return이 true 면?
                        JSONObject jsonData = jsonObject.getJSONObject("data");
                        JSONArray jsonChildArray = new JSONArray(jsonObject.getString("child"));
                        JSONArray jsonReviewArray = new JSONArray(jsonObject.getString("review"));

                        circleImage(photo, ServerUrl.getBaseUrl() + "/uploads/images/origin/" + jsonData.getString("profile"));

                        name.setText(jsonData.getString("name"));
                        name2.setText(jsonData.getString("name"));
                        age.setText(jsonData.getString("age")+"세");

                        if("1".equals(jsonData.getString("gender"))){
                            gender.setText("남자");
                        }else if("2".equals(jsonData.getString("gender"))){
                            gender.setText("여자");
                        }
                        location.setText(jsonData.getString("addr"));
                        reviewCnt = Integer.parseInt(jsonData.getString("reviewcnt"));
                        review_count.setText("("+jsonData.getString("reviewcnt")+")");

                        if("0".equals(jsonData.getString("reviewcnt"))){
                            rating_bar.setProgress(0);
                        }
                        if("0.0000".equals(jsonData.getString("star"))){
                            review_average.setText("0.0");
                            rating_bar.setProgress(0);
                        } else {
                            review_average.setText(jsonData.getString("star"));
                            rating_bar.setProgress(Math.round(Float.parseFloat(jsonData.getString("star"))/ 2) );
                        }

                        if (jsonReviewArray.length() == 0) {
                            review_list.setVisibility(View.GONE);
                        } else {
                            review_list.setVisibility(View.VISIBLE);
                            for (int i = 0; i < jsonReviewArray.length(); i++) {
                                JSONObject getJsonObject = jsonReviewArray.getJSONObject(i);
                                reviewData.add(new Review(getJsonObject.getString("friend_profile"), getJsonObject.getString("name"), getJsonObject.getString("content"), getJsonObject.getString("star"), getJsonObject.getString("date")));
                            }
                            review_list.setAdapter(reviewAdapter);
                            review_list.setDivider(null);
                            setListViewHeightBasedOnChildren(review_list);
                            reviewAdapter.notifyDataSetChanged();
                        }

                        if (jsonChildArray.length() == 0) {
                            child_list.setVisibility(View.GONE);
                        } else {
                            child_list.setVisibility(View.VISIBLE);
                            for (int i = 0; i < jsonChildArray.length(); i++) {
                                JSONObject getJsonObject = jsonChildArray.getJSONObject(i);
                                childData.add(new Child(getJsonObject.getString("child"), getJsonObject.getString("name"), getJsonObject.getString("age"), getJsonObject.getString("gender"), getJsonObject.getString("image")));
                            }
                            child_list.setAdapter(childAdapter);
                            child_list.setDivider(null);
                            setListViewHeightBasedOnChildren(child_list);
                            childAdapter.notifyDataSetChanged();
                        }
                    } else if (!jsonObject.getBoolean("return")) {
                        if("login".equals(jsonObject.getString("type"))){
                            Toast.makeText(CustomerProfileActivity.this, "로그인 해주세요 ! ", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(context, LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(CustomerProfileActivity.this, "고객 프로필 불러오기 실패", Toast.LENGTH_SHORT).show();
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

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);

        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
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
