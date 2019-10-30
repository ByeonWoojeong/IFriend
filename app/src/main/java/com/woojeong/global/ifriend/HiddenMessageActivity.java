package com.woojeong.global.ifriend;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.woojeong.global.ifriend.DTO.ChatList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.woojeong.global.ifriend.GlobalApplication.setDarkMode;

public class HiddenMessageActivity extends AppCompatActivity {
    private static String TAG = "HiddenMessageActivity";
    Context context;
    InputMethodManager ipmm;
    AQuery aQuery = null;
    OneBtnDialog oneBtnDialog;

    FrameLayout back_con;
    ImageView back;

    ListView hidden_list;
    ArrayList<ChatList> data;
    ChatListAdapter chatListAdapter;

    SharedPreferences prefLoginChecked;
    String mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hidden_message);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#ffffff"));
            window.setBackgroundDrawable(null);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                setDarkMode(HiddenMessageActivity.this, true);
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

        hidden_list = findViewById(R.id.hidden_list);
        data = new ArrayList<ChatList>();
        chatListAdapter = new ChatListAdapter(context, R.layout.list_chat_item, data, hidden_list);

        prefLoginChecked = context.getSharedPreferences("modeChecked", Activity.MODE_PRIVATE);
        mode = prefLoginChecked.getString("mode", "");
    }

    @Override
    protected void onResume() {
        super.onResume();
        showList();
    }

    void showList() {
        SharedPreferences get_token = context.getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
        final String getToken = get_token.getString("Token", "");
        final String url;
        if ("sitter".equals(mode)) {
            url = ServerUrl.getBaseUrl() + "/chat/mlist";
        } else {
            url = ServerUrl.getBaseUrl() + "/chat/flist";
        }
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("hidden", "1");
        aQuery.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
                Log.i(TAG, " jsonObject " + jsonObject);
                try {
                    data.clear();
                    if (jsonObject.getBoolean("return")) {    //return이 true 면?
                        JSONArray jsonArray = new JSONArray(jsonObject.getString("list"));
                        if (jsonArray.length() == 0) {
                            hidden_list.setVisibility(View.GONE);
                            Toast.makeText(context, "대화 목록이 없습니다.", Toast.LENGTH_SHORT).show();
                        } else {
                            hidden_list.setVisibility(View.VISIBLE);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject getJsonObject = jsonArray.getJSONObject(i);
                                data.add(new ChatList(getJsonObject.getString("idx"), getJsonObject.getString("profile"), getJsonObject.getString("name"), getJsonObject.getString("content"), getJsonObject.getString("date"), getJsonObject.getString("new")));
                            }
                            hidden_list.setAdapter(chatListAdapter);
                            chatListAdapter.notifyDataSetChanged();
                        }
                    } else if (!jsonObject.getBoolean("return")) {
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
}
