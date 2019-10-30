package com.woojeong.global.ifriend;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.woojeong.global.ifriend.DTO.FaQ;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.woojeong.global.ifriend.GlobalApplication.setDarkMode;

public class FaQActivity extends AppCompatActivity {
    private static String TAG = "FaQActivity";

    private static Context context;

    AQuery aQuery = null;
    InputMethodManager ipm;
    String token;
    EditText search_text;
    ImageView search_btn;
    ExpandableListView listView;
    ArrayList<FaQ> data = null;
    ArrayList<ArrayList<FaQ>> data2;
    ExpandableListAdapter adapter = null;
    JSONObject jsonObjectList;
    OneBtnDialog oneBtnDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fa_q);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#ffffff"));
            window.setBackgroundDrawable(null);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                setDarkMode(FaQActivity.this, true);
            }
        }

        aQuery = new AQuery(this);
        ipm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        SharedPreferences prefToken = getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
        token = prefToken.getString("Token", "");
        listView = (ExpandableListView) findViewById(R.id.memberListView);
        findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        final String url = ServerUrl.getBaseUrl() + "";
        Map<String, Object> params = new HashMap<String, Object>();
        aQuery.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
                data = new ArrayList<FaQ>();
                data2 = new ArrayList<ArrayList<FaQ>>();
                try {
                    if (jsonObject.getBoolean("return")) {
                        final JSONArray jsonArray = new JSONArray(jsonObject.getString("list"));
                        for (int i = 0; i < jsonArray.length(); i++) {
                            jsonObjectList = jsonArray.getJSONObject(i);
                            data.add(new FaQ(jsonObjectList.getString("title"), jsonObjectList.getString("content")));
                            data2.add(new ArrayList<FaQ>());
                            data2.get(i).add(new FaQ(jsonObjectList.getString("title"), jsonObjectList.getString("content")));
                        }
                        adapter = new HelperAdapter(FaQActivity.this, listView, R.layout.list_helper_title_item, R.layout.list_helper_content_item, data, data2);
                        listView.setAdapter(adapter);
                    } else {
                        data.clear();
                        data2.clear();
                        adapter = new HelperAdapter(FaQActivity.this, listView, R.layout.list_helper_title_item, R.layout.list_helper_content_item, data, data2);
                        listView.setAdapter(adapter);
                        oneBtnDialog = new OneBtnDialog(FaQActivity.this, "검색 결과가 없습니다 !", "확인");
                        oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        oneBtnDialog.setCancelable(false);
                        oneBtnDialog.show();
                    }
                    search_text.setText("");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.header("GHsoft-Agent", "" + token).header("User-Agent", "android"));
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
