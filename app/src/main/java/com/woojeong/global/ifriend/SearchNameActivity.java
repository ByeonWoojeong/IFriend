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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.woojeong.global.ifriend.DTO.SearchName;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.woojeong.global.ifriend.GlobalApplication.setDarkMode;

public class SearchNameActivity extends AppCompatActivity {
    private static String TAG = "SearchNameActivity";
    Context context;
    InputMethodManager ipmm;
    AQuery aQuery = null;
    OneBtnDialog oneBtnDialog;
    FrameLayout back_con;
    ImageView back, search_btn;
    EditText search_text;
    ListView name_listview, history_listview;
    TextView delete_data;
    SearchNameListAdapter adapter;
    ArrayList<SearchName> data;

    SharedPreferences sharedPreferences;
    SharedPreferences get_token;
    SharedPreferences.Editor editor;
    JSONArray nameArray;

    String getMember, getName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_name);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#ffffff"));
            window.setBackgroundDrawable(null);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                setDarkMode(SearchNameActivity.this, true);
            }
        }
        aQuery = new AQuery(this);
        context = getApplicationContext();
        ipmm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        back_con = findViewById(R.id.back_con);
        back = findViewById(R.id.back);
        search_btn = findViewById(R.id.search_btn);
        search_text = findViewById(R.id.search_text);
        name_listview = findViewById(R.id.name_listview);
        name_listview.setDivider(null);
        history_listview = findViewById(R.id.history_listview);
        history_listview.setDivider(null);
        delete_data = findViewById(R.id.delete_data);

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

        data = new ArrayList<SearchName>();
        adapter = new SearchNameListAdapter(SearchNameActivity.this, R.layout.list_name_item, data, name_listview);

//        //저장된 값을 불러오기 위해 같은 네임파일을 찾음.
//        SharedPreferences sharedPreferences = getSharedPreferences("name", MODE_PRIVATE);
//        editor = sharedPreferences.edit();
//        //text라는 key에 저장된 값이 있는지 확인. 아무값도 들어있지 않으면 ""를 반환
//        String text = sharedPreferences.getString("nameKey", "");
//        if (text.length() > 0) {
//            name_listview.setVisibility(View.VISIBLE);
//            for (int i = 0; i < text.length(); i++) {
//                data.add(new SearchName(getMember, getName));
//            }
//            name_listview.setAdapter(adapter);
//            adapter.notifyDataSetChanged();
//        } else {
//            name_listview.setVisibility(View.GONE);
//        }

        nameArray = new JSONArray();
        get_token = getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
        editor = get_token.edit();

        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ipmm.hideSoftInputFromWindow(search_text.getWindowToken(), 0);

                if ("".equals(search_text.getText().toString()) || "" == search_text.getText().toString()) {    //문자열 비교
                    oneBtnDialog = new OneBtnDialog(SearchNameActivity.this, "이름을\n입력해 주세요 !", "확인");
                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    oneBtnDialog.setCancelable(false);
                    oneBtnDialog.show();
                    return;
                }

                nameArray.put(search_text.getText().toString());
                editor.putString("searchName", search_text.getText().toString()); // key, value를 이용하여 저장하는 형태
                editor.commit();

                Log.i(TAG, search_text.getText().toString());

                final String getToken = get_token.getString("Token", "");
                final String url = ServerUrl.getBaseUrl() + "/search/name";
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("name", search_text.getText().toString());
                aQuery.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
                    @Override
                    public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
                        try {
                            data.clear();
                            if (jsonObject.getBoolean("return")) {
                                JSONArray jsonArray = new JSONArray(jsonObject.getString("list"));
                                if (jsonArray.length() == 0) {
                                    name_listview.setVisibility(View.GONE);
                                    oneBtnDialog = new OneBtnDialog(SearchNameActivity.this, "검색 결과가 없습니다 !", "확인");
                                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                    oneBtnDialog.setCancelable(false);
                                    oneBtnDialog.show();
                                    return;
                                } else {
                                    name_listview.setVisibility(View.VISIBLE);
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject getJsonObject = jsonArray.getJSONObject(i);
                                        getMember = getJsonObject.getString("member");
                                        getName = getJsonObject.getString("name");
                                        data.add(new SearchName(getName, getMember));
                                    }
                                    name_listview.setAdapter(adapter);
                                    adapter.notifyDataSetChanged();
                                }
                            } else if (!jsonObject.getBoolean("return")) {
                                if("login".equals(jsonObject.getString("type"))){
                                    Toast.makeText(SearchNameActivity.this, "로그인 해주세요 ! ", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(SearchNameActivity.this, LoginActivity.class);
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
                search_text.setText("");
            }
        });

        delete_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ipmm.hideSoftInputFromWindow(search_text.getWindowToken(), 0);
                Toast.makeText(SearchNameActivity.this, "지난 검색 기록을 삭제하였습니다.", Toast.LENGTH_SHORT).show();
                editor.remove("searchName");
                editor.commit();
                data.clear();
                name_listview.setAdapter(adapter);
                adapter.notifyDataSetChanged();
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
}
