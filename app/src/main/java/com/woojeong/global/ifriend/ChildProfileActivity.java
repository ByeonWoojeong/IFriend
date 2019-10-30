package com.woojeong.global.ifriend;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.Nullable;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.woojeong.global.ifriend.DTO.Child;
import com.woojeong.global.ifriend.DTO.Main;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.woojeong.global.ifriend.GlobalApplication.setDarkMode;

public class ChildProfileActivity extends AppCompatActivity {
    private static String TAG = "ChildProfileActivity";
    Context context;
    InputMethodManager ipmm;
    AQuery aQuery = null;
    OneBtnDialog oneBtnDialog;

    FrameLayout back_con;
    ImageView back, add_icon;
    LinearLayout add_con;
    TextView add;

    ListView child_list;
    ArrayList<Child> data;
    ChildAdapter childAdapter;

    int childCnt = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_profile);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#ffffff"));
            window.setBackgroundDrawable(null);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                setDarkMode(ChildProfileActivity.this, true);
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

        add_icon = findViewById(R.id.add_icon);
        child_list = findViewById(R.id.child_list);
        add_con = findViewById(R.id.add_con);
        add = findViewById(R.id.add);

        data = new ArrayList<Child>();
        childAdapter = new ChildAdapter(context, R.layout.list_child_item, data, child_list, "more");

        add_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add.callOnClick();
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (childCnt >= 4) {
                    oneBtnDialog = new OneBtnDialog(ChildProfileActivity.this, "아이 등록은\n최대 4명입니다 !", "확인");
                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    oneBtnDialog.setCancelable(false);
                    oneBtnDialog.show();
                    return;
                }
                    Intent intent = new Intent(ChildProfileActivity.this, RegisterChild1Activity.class);
                    intent.putExtra("what", "register");
                    startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        showList();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, " requestCode " + requestCode + " resultCode " + resultCode);
        switch (resultCode) {
            case 9999:
                showList();
                break;
            default:
                break;
        }
    }

    void showList() {
        SharedPreferences get_token = getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
        final String getToken = get_token.getString("Token", "");
        final String url = ServerUrl.getBaseUrl() + "/child/list";
        Map<String, Object> params = new HashMap<String, Object>();
        aQuery.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
                Log.i(TAG, " " + jsonObject);
                try {
                    data.clear();
                    if (jsonObject.getBoolean("return")) {    //return이 true 면?
                        JSONArray jsonArray = new JSONArray(jsonObject.getString("list"));
                        if (jsonArray.length() == 0) {
                            childCnt = 0;
                            child_list.setVisibility(View.GONE);
                            Toast.makeText(context, "아이 목록이 없습니다.", Toast.LENGTH_SHORT).show();
                        } else {
                            child_list.setVisibility(View.VISIBLE);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject getJsonObject = jsonArray.getJSONObject(i);
                                data.add(new Child(getJsonObject.getString("child"), getJsonObject.getString("name"), getJsonObject.getString("age"), getJsonObject.getString("gender"), getJsonObject.getString("image")));
                            }
                            child_list.setAdapter(childAdapter);
                            childAdapter.notifyDataSetChanged();
                            childCnt = data.size();
                        }
                    } else if (!jsonObject.getBoolean("return")) {
                        if ("login".equals(jsonObject.getString("type"))) {
                            Toast.makeText(ChildProfileActivity.this, "로그인 해주세요 ! ", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ChildProfileActivity.this, LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        } else {

                        }
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
