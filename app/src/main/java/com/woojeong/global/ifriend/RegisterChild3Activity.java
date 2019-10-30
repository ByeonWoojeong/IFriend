package com.woojeong.global.ifriend;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.woojeong.global.ifriend.GlobalApplication.setDarkMode;

public class RegisterChild3Activity extends AppCompatActivity {
    private static String TAG = "RegisterChild3Activity";

    Context context;
    InputMethodManager ipmm;
    AQuery aQuery = null;
    OneBtnDialog oneBtnDialog;
    FinishDialog finishDialog;
    AddDialog addDialog;

    FrameLayout back_con, next_con;
    ImageView back;
    EditText conditions, etc;
    LinearLayout check_con;
    CheckBox check;
    TextView title, next;

    Handler handler;
    Intent register;
    String whatPage = "", getChild = "", getImage = "", getName = "", getNo = "", getGender = "", getBirth = "", getEdu = "", getIns = "", getLike = "", getQuest1 = "", getQuest2 = "";
    ArrayList<String> getQuest3, getQuest4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_child3);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#ffffff"));
            window.setBackgroundDrawable(null);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                setDarkMode(RegisterChild3Activity.this, true);
            }
        }

        register = getIntent();
        whatPage = register.getStringExtra("what");
        getChild = register.getStringExtra("child");
        getImage = register.getStringExtra("image");
        getName = register.getStringExtra("name");
        getNo = register.getStringExtra("no");
        getGender = register.getStringExtra("gender");
        getBirth = register.getStringExtra("birth");
        getEdu = register.getStringExtra("edu");
        getIns = register.getStringExtra("ins");
        getLike = register.getStringExtra("like");
        getQuest1 = register.getStringExtra("quest1");
        getQuest2 = register.getStringExtra("quest2");
        getQuest3 = (ArrayList<String>) register.getSerializableExtra("quest3");
        getQuest4 = (ArrayList<String>) register.getSerializableExtra("quest4");

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

        title = findViewById(R.id.title);

        if ("edit".equals(whatPage)) {
            title.setText("우리 아이 프로필 수정");
            handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    viewSettings();
                }
            }, 200);
        } else {
            title.setText("우리 아이 프로필 등록");
        }

        next_con = findViewById(R.id.next_con);
        next = findViewById(R.id.next);

        conditions = findViewById(R.id.conditions);
        etc = findViewById(R.id.etc);

        check_con = findViewById(R.id.check_con);
        check = findViewById(R.id.check);

        check_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check.callOnClick();
            }
        });

        next_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                next.callOnClick();
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!check.isChecked()) {
                    oneBtnDialog = new OneBtnDialog(RegisterChild3Activity.this, "동의 사항에 동의해주세요 !", "확인");
                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    oneBtnDialog.setCancelable(false);
                    oneBtnDialog.show();
                    return;
                } else {
                    ipmm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);

                    SharedPreferences get_token = getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
                    final String getToken = get_token.getString("Token", "");
                    Log.i(TAG, " getToken " + getToken);
                    String url = "";
                    Map<String, Object> params = new HashMap<String, Object>();

                    if ("edit".equals(whatPage)) {
                        url = ServerUrl.getBaseUrl() + "/child/update";
                        params.put("child", getChild);
                    } else {
                        url = ServerUrl.getBaseUrl() + "/child/insert";
                    }

                    Log.i(TAG, " " + url);

                    params.put("name", getName);
                    params.put("no", getNo);
                    params.put("gender", getGender);
                    params.put("birth", getBirth.replace("년생", ""));
                    params.put("edu", getEdu);
                    params.put("ins", getIns);
                    params.put("like", getLike);

                    if(getImage.startsWith("/")){
                        File makeFile = new File(getImage);
                        params.put("image", makeFile);
                    } else {
                        // 그냥 스트링
                        Log.i(TAG, " 222222");
                    }

                    params.put("quest1", getQuest1);
                    params.put("quest2", getQuest2);
                    for (int i = 0; i < getQuest3.size(); i++) {
                        params.put("quest3[" + i + "]", getQuest3.get(i));
                    }
                    for (int i = 0; i < getQuest4.size(); i++) {
                        params.put("quest4[" + i + "]", getQuest4.get(i));
                    }

                    if (!"".equals(conditions.getText().toString())) {
                        params.put("quest5", conditions.getText().toString());
                    }
                    if (!"".equals(etc.getText().toString())) {
                        params.put("quest6", etc.getText().toString());
                    }
                    Log.i(TAG, " params " + params);
                    aQuery.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
                        @Override
                        public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
                            Log.i(TAG, " jsonObject " + jsonObject);

                            try {
                                if (jsonObject.getBoolean("return")) {    //return이 true 면?

                                    if ("register".equals(whatPage)) {
                                        finishDialog = new FinishDialog(RegisterChild3Activity.this, "아이 프로필 등록을\n완료하였습니다 !", "확인");
                                        finishDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                        finishDialog.setCancelable(false);
                                        finishDialog.show();
                                        return;
                                    } else if ("edit".equals(whatPage)) {
                                        finishDialog = new FinishDialog(RegisterChild3Activity.this, "아이 프로필 수정을\n완료하였습니다 !", "확인");
                                        finishDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                        finishDialog.setCancelable(false);
                                        finishDialog.show();
                                        return;
                                    } else if ("add".equals(whatPage)) {
                                        addDialog = new AddDialog(RegisterChild3Activity.this, "아이 프로필 등록을\n완료하였습니다 !", "확인");
                                        addDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                        addDialog.setCancelable(false);
                                        addDialog.show();
                                        return;
                                    }

                                } else if (!jsonObject.getBoolean("return")) {
                                    if ("register".equals(whatPage)) {
                                        if("login".equals(jsonObject.getString("type"))){
                                            Toast.makeText(RegisterChild3Activity.this, "로그인 해주세요 ! ", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(context, LoginActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            oneBtnDialog = new OneBtnDialog(RegisterChild3Activity.this, "출생 순위가 중복되었습니다 !", "확인");
                                            oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                            oneBtnDialog.setCancelable(false);
                                            oneBtnDialog.show();
                                        }

                                    } else if ("edit".equals(whatPage)) {
                                        if("login".equals(jsonObject.getString("type"))){
                                            Toast.makeText(RegisterChild3Activity.this, "로그인 해주세요 ! ", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(context, LoginActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            Toast.makeText(RegisterChild3Activity.this, "아이 프로필 수정 실패", Toast.LENGTH_SHORT).show();
                                        }

                                    } else {
                                        if("login".equals(jsonObject.getString("type"))){
                                            Toast.makeText(RegisterChild3Activity.this, "로그인 해주세요 ! ", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(context, LoginActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            oneBtnDialog = new OneBtnDialog(RegisterChild3Activity.this, "출생 순위가 중복되었습니다 !", "확인");
                                            oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                            oneBtnDialog.setCancelable(false);
                                            oneBtnDialog.show();
                                        }
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }.header("EPOCH-AGENT", "" + getToken).header("User-Agent", "android"));
                }
            }
        });
    }

    void viewSettings() {
        SharedPreferences get_token = getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
        final String getToken = get_token.getString("Token", "");
        final String url = ServerUrl.getBaseUrl() + "/child/detail";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("child", getChild);
        Log.i(TAG, " params " + params);
        aQuery.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
                Log.i(TAG, " jsonObject " + jsonObject);
                try {
                    if (jsonObject.getBoolean("return")) {    //return이 true 면?
                        JSONObject jsonData = jsonObject.getJSONObject("data");
                        conditions.setText(jsonData.getString("quest5"));
                        etc.setText(jsonData.getString("quest6"));

                    } else if (!jsonObject.getBoolean("return")) {
                        if("login".equals(jsonObject.getString("type"))){
                            Toast.makeText(RegisterChild3Activity.this, "로그인 해주세요 ! ", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(context, LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            oneBtnDialog = new OneBtnDialog(RegisterChild3Activity.this, "정보를 불러올 수 없습니다 !", "확인");
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

    public class FinishDialog extends Dialog {
        FinishDialog finishDialog = this;
        Context context;

        public FinishDialog(final Context context, final String text, final String btnText) {
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
                    finishDialog.dismiss();
                    Intent intent = new Intent(RegisterChild3Activity.this, ChildProfileActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            });
        }
    }

    public class AddDialog extends Dialog {
        AddDialog addDialog = this;
        Context context;

        public AddDialog(final Context context, final String text, final String btnText) {
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
                    addDialog.dismiss();
                    Intent intent = new Intent(RegisterChild3Activity.this, ReservationActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                }
            });
        }
    }
}
