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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.woojeong.global.ifriend.DTO.JournalDetails;
import com.woojeong.global.ifriend.DTO.One;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.woojeong.global.ifriend.GlobalApplication.getGlobalApplicationContext;
import static com.woojeong.global.ifriend.GlobalApplication.setDarkMode;

public class CareEnvironmentActivity extends AppCompatActivity {
    private static String TAG = "CareEnvironmentActivity";
    Context context;
    InputMethodManager ipmm;
    AQuery aQuery = null;
    OneBtnDialog oneBtnDialog;

    FrameLayout back_con, ok_con;
    ImageView back;
    ScrollView scrollView;
    TextView place_kinds, ok;

    EditText explain_edit;
    RadioGroup radio_group;
    RadioButton radio_boy, radio_girl, radio_both;

    ListView character_list;
    ArrayList<One> data;
    OneItemAdapter oneItemAdapter;

    String gender = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_care_environment);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#ffffff"));
            window.setBackgroundDrawable(null);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                setDarkMode(CareEnvironmentActivity.this, true);
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

        scrollView = findViewById(R.id.scrollView);

        place_kinds = findViewById(R.id.place_kinds);
        ok = findViewById(R.id.ok);

        character_list = findViewById(R.id.character_list);
        data = new ArrayList<One>();
        oneItemAdapter = new OneItemAdapter(CareEnvironmentActivity.this, R.layout.list_one_item, data, character_list, "family");

        explain_edit = findViewById(R.id.explain_edit);

        radio_group = findViewById(R.id.radio_group);

        radio_boy = findViewById(R.id.radio_boy);
        radio_girl = findViewById(R.id.radio_girl);
        radio_both = findViewById(R.id.radio_both);

        radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radio_boy) {
                    gender = "1";
                } else if (checkedId == R.id.radio_girl) {
                    gender = "2";
                } else if (checkedId == R.id.radio_both) {
                    gender = "0";
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

                if("".equals(gender)){
                    oneBtnDialog = new OneBtnDialog(CareEnvironmentActivity.this, "돌봄 가능 성별을\n선택해 주세요 !", "확인");
                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    oneBtnDialog.setCancelable(false);
                    oneBtnDialog.show();
                    return;
                }

                SharedPreferences get_token = getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
                final String getToken = get_token.getString("Token", "");
                final String url = ServerUrl.getBaseUrl() + "/friend/update";
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("familydetail", explain_edit.getText().toString());
                params.put("care", gender);
                Log.i(TAG, " params " + params);
                aQuery.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
                    @Override
                    public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
                        Log.i(TAG, " " + jsonObject);
                        try {
                            if (jsonObject.getBoolean("return")) {    //return이 true 면?
                                setResult(123);
                                finish();
                            } else if (!jsonObject.getBoolean("return")) {
                                if("login".equals(jsonObject.getString("type"))){
                                    Toast.makeText(CareEnvironmentActivity.this, "로그인 해주세요 ! ", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(CareEnvironmentActivity.this, LoginActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(CareEnvironmentActivity.this, "돌봄 환경 업로드 실패", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }.header("EPOCH-AGENT", "" + getToken).header("User-Agent", "android"));
            }
        });

        viewSettings();
    }

    void viewSettings() {
        SharedPreferences get_token = getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
        final String getToken = get_token.getString("Token", "");
        final String url = ServerUrl.getBaseUrl() + "/friend/data";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("home", "1");
        params.put("special[]", "1");
        params.put("familydetail", "1");
        params.put("care", "1");
        Log.i(TAG, " params " + params);
        aQuery.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
                Log.i(TAG, " " + jsonObject);
                try {
                    if (jsonObject.getBoolean("return")) {    //return이 true 면?
                        JSONObject jsonData = jsonObject.getJSONObject("data");

                        if("1".equals(jsonData.getString("home"))){
                            place_kinds.setText("단독 주택");
                        } else if("2".equals(jsonData.getString("home"))){
                            place_kinds.setText("아파트");
                        } else if("3".equals(jsonData.getString("home"))){
                            place_kinds.setText("빌라");
                        } else{
                            place_kinds.setText("기타");
                        }

                        String[] special = jsonData.getString("special").split(",");

                        if (special.length == 0) {
                            character_list.setVisibility(View.GONE);
                        } else {
                            data.clear();
                            character_list.setVisibility(View.VISIBLE);
                            String[] specialArray = jsonData.getString("special").split(",");
                            for(int i=0; i<specialArray.length; i++){
                                data.add(new One(specialArray[i]));
                            }
                            character_list.setAdapter(oneItemAdapter);
                            character_list.setDivider(null);
                            setListViewHeightBasedOnChildren(character_list);
                            oneItemAdapter.notifyDataSetChanged();
                        }

                        explain_edit.setText(jsonData.getString("familydetail"));

                        if("1".equals(jsonData.getString("care"))){
                            radio_boy.setChecked(true);
                        } else if("2".equals(jsonData.getString("care"))){
                            radio_girl.setChecked(true);
                        } else if("".equals(jsonData.getString("care"))){
                            radio_both.setChecked(true);
                        }

                    } else if (!jsonObject.getBoolean("return")) {
                        if("login".equals(jsonObject.getString("type"))){
                            Toast.makeText(CareEnvironmentActivity.this, "로그인 해주세요 ! ", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(CareEnvironmentActivity.this, LoginActivity.class);
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
