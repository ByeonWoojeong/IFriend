package com.woojeong.global.ifriend;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import com.woojeong.global.ifriend.DTO.Career;
import com.woojeong.global.ifriend.DTO.One;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.woojeong.global.ifriend.GlobalApplication.setDarkMode;

public class ReadCareerActivity extends AppCompatActivity {
    private static String TAG = "ReadCareerActivity";
    Context context;
    InputMethodManager ipmm;
    AQuery aQuery = null;
    OneBtnDialog oneBtnDialog;

    FrameLayout back_con, child1, child2, child3, child4;
    ImageView back;
    ScrollView scrollView;

    ListView license_list, experience_list;
    ArrayList<One> certData;
    OneItemAdapter licenseAdapter;
    ArrayList<Career> officeData;
    CareerAdapter officeAdapter;

    LinearLayout child_con;
    TextView child_count, child1_age, child2_age, child3_age, child4_age, child1_gender, child2_gender, child3_gender, child4_gender, explain, career;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_career);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#ffffff"));
            window.setBackgroundDrawable(null);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                setDarkMode(ReadCareerActivity.this, true);
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

        child_con = findViewById(R.id.child_con);
        child1 = findViewById(R.id.child1);
        child2 = findViewById(R.id.child2);
        child3 = findViewById(R.id.child3);
        child4 = findViewById(R.id.child4);

        scrollView = findViewById(R.id.scrollView);

        license_list = findViewById(R.id.license_list);
        experience_list = findViewById(R.id.experience_list);

        child_count = findViewById(R.id.child_count);
        child1_age = findViewById(R.id.child1_age);
        child2_age = findViewById(R.id.child2_age);
        child3_age = findViewById(R.id.child3_age);
        child4_age = findViewById(R.id.child4_age);
        child1_gender = findViewById(R.id.child1_gender);
        child2_gender = findViewById(R.id.child2_gender);
        child3_gender = findViewById(R.id.child3_gender);
        child4_gender = findViewById(R.id.child4_gender);
        explain = findViewById(R.id.explain);
        career = findViewById(R.id.career);

        viewSettings();
        scrollView.postDelayed(new Runnable() {
            @Override
            public void run() {
                scrollView.scrollTo(0, 0);
            }
        }, 700);

        certData = new ArrayList<One>();
        licenseAdapter = new OneItemAdapter(ReadCareerActivity.this, R.layout.list_one_item, certData, license_list);

        officeData = new ArrayList<Career>();
        officeAdapter = new CareerAdapter(ReadCareerActivity.this, R.layout.list_career_item, officeData, experience_list, false);
    }

    void viewSettings() {
        SharedPreferences get_token = getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
        final String getToken = get_token.getString("Token", "");
        final String url = ServerUrl.getBaseUrl() + "/friend/data";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("cert[]", "");
        params.put("office[]", "");
        params.put("childcnt", "");
        params.put("child[]", "");
        params.put("childdetail", "");
        params.put("career", "");
        Log.i(TAG, " params " + params);
        aQuery.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
                Log.i(TAG, " " + jsonObject);
                try {
                    if (jsonObject.getBoolean("return")) {    //return이 true 면?
                        JSONObject jsonData = jsonObject.getJSONObject("data");

                        //child
                        JSONArray childArray = new JSONArray(jsonData.getString("child"));
                        if(childArray.length() == 0){
                            child_con.setVisibility(View.GONE);
                        } else {
                            child_con.setVisibility(View.VISIBLE);

                            if(childArray.length() == 1){
                                child1.setVisibility(View.VISIBLE);

                                child1_age.setText(childArray.getJSONObject(0).getString("year") + "년생");
                                if("1".equals(childArray.getJSONObject(0).getString("gender"))) {
                                    child1_gender.setText("남아");
                                }else {
                                    child1_gender.setText("여아");
                                }

                            } else if (childArray.length() == 2) {
                                child1.setVisibility(View.VISIBLE);
                                child2.setVisibility(View.VISIBLE);

                                child1_age.setText(childArray.getJSONObject(0).getString("year") + "년생");
                                if("1".equals(childArray.getJSONObject(0).getString("gender"))) {
                                    child1_gender.setText("남아");
                                }else {
                                    child1_gender.setText("여아");
                                }
                                child2_age.setText(childArray.getJSONObject(1).getString("year") + "년생");
                                if("1".equals(childArray.getJSONObject(1).getString("gender"))) {
                                    child2_gender.setText("남아");
                                }else {
                                    child2_gender.setText("여아");
                                }
                            } else if (childArray.length() == 3) {
                                child1.setVisibility(View.VISIBLE);
                                child2.setVisibility(View.VISIBLE);
                                child3.setVisibility(View.VISIBLE);

                                child1_age.setText(childArray.getJSONObject(0).getString("year") + "년생");
                                if("1".equals(childArray.getJSONObject(0).getString("gender"))) {
                                    child1_gender.setText("남아");
                                }else {
                                    child1_gender.setText("여아");
                                }
                                child2_age.setText(childArray.getJSONObject(1).getString("year") + "년생");
                                if("1".equals(childArray.getJSONObject(1).getString("gender"))) {
                                    child2_gender.setText("남아");
                                }else {
                                    child2_gender.setText("여아");
                                }
                                child3_age.setText(childArray.getJSONObject(2).getString("year") + "년생");
                                if("1".equals(childArray.getJSONObject(2).getString("gender"))) {
                                    child3_gender.setText("남아");
                                }else {
                                    child3_gender.setText("여아");
                                }
                            } else if (childArray.length() == 4) {
                                child1.setVisibility(View.VISIBLE);
                                child2.setVisibility(View.VISIBLE);
                                child3.setVisibility(View.VISIBLE);
                                child4.setVisibility(View.VISIBLE);

                                child1_age.setText(childArray.getJSONObject(0).getString("year") + "년생");
                                if("1".equals(childArray.getJSONObject(0).getString("gender"))) {
                                    child1_gender.setText("남아");
                                }else {
                                    child1_gender.setText("여아");
                                }
                                child2_age.setText(childArray.getJSONObject(1).getString("year") + "년생");
                                if("1".equals(childArray.getJSONObject(1).getString("gender"))) {
                                    child2_gender.setText("남아");
                                }else {
                                    child2_gender.setText("여아");
                                }
                                child3_age.setText(childArray.getJSONObject(2).getString("year") + "년생");
                                if("1".equals(childArray.getJSONObject(2).getString("gender"))) {
                                    child3_gender.setText("남아");
                                }else {
                                    child3_gender.setText("여아");
                                }
                                child4_age.setText(childArray.getJSONObject(3).getString("year") + "년생");
                                if("1".equals(childArray.getJSONObject(3).getString("gender"))) {
                                    child4_gender.setText("남아");
                                }else {
                                    child4_gender.setText("여아");
                                }
                            }
                        }

                        if ("0".equals(jsonData.getString("career"))) {
                            career.setText("없음");
                        } else if ("1".equals(jsonData.getString("career"))) {
                            career.setText("3년 미만");
                        } else if ("2".equals(jsonData.getString("career"))) {
                            career.setText("3년 이상 ~ 7년 미만");
                        } else if ("3".equals(jsonData.getString("career"))) {
                            career.setText("7년 이상");
                        }

                        explain.setText(jsonData.getString("childdetail"));

                        String[] cert = jsonData.getString("cert").split(",");
                        if (cert.length == 0) {
                            license_list.setVisibility(View.GONE);
                        } else {
                            certData.clear();
                            license_list.setVisibility(View.VISIBLE);
                            for (int i = 0; i < cert.length; i++) {
                                Log.i(TAG, " cert " + cert[i]);
                                certData.add(new One(cert[i]));
                            }
                            license_list.setAdapter(licenseAdapter);
                            license_list.setDivider(null);
                            setListViewHeightBasedOnChildren(license_list);
                            licenseAdapter.notifyDataSetChanged();
                        }

                        JSONArray officeArray = new JSONArray(jsonData.getString("office"));
                        if (officeArray.length() == 0) {
                            experience_list.setVisibility(View.GONE);
                        } else {
                            experience_list.setVisibility(View.VISIBLE);
                            for (int i = 0; i < officeArray.length(); i++) {
                                Log.i(TAG, " office " + officeArray.get(i));
                                JSONObject getOffice = officeArray.getJSONObject(i);
                                officeData.add(new Career(getOffice.getString("name"), getOffice.getString("start"), getOffice.getString("end")));
                            }
                            experience_list.setAdapter(officeAdapter);
                            experience_list.setDivider(null);
                            setListViewHeightBasedOnChildren(experience_list);
                            officeAdapter.notifyDataSetChanged();
                        }

                        child_count.setText(jsonData.getString("childcnt"));

                    } else if (!jsonObject.getBoolean("return")) {
                        if("login".equals(jsonObject.getString("type"))){
                            Toast.makeText(ReadCareerActivity.this, "로그인 해주세요 ! ", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ReadCareerActivity.this, LoginActivity.class);
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
