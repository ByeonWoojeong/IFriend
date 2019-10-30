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
import android.support.annotation.Nullable;
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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.woojeong.global.ifriend.GlobalApplication.setDarkMode;

public class RegisterChild2Activity extends AppCompatActivity {
    private static String TAG = "RegisterChild2Activity";

    Context context;
    InputMethodManager ipmm;
    AQuery aQuery = null;
    OneBtnDialog oneBtnDialog;

    FrameLayout back_con, next_con;
    ImageView back;
    RadioGroup q1_radio_group, q2_radio_group;
    RadioButton q1_1_radio, q1_2_radio, q2_1_radio, q2_2_radio, q2_3_radio, q2_4_radio;
    LinearLayout q3_1_con, q3_2_con, q3_3_con, q3_4_con, q3_5_con, q3_6_con, q3_7_con, q3_8_con, q4_1_con, q4_2_con, q4_3_con, q4_4_con;
    CheckBox q3_1_check, q3_2_check, q3_3_check, q3_4_check, q3_5_check, q3_6_check, q3_7_check, q3_8_check, q4_1_check, q4_2_check, q4_3_check, q4_4_check;
    TextView title, q3_1_txt, q3_2_txt, q3_3_txt, q3_4_txt, q3_5_txt, q3_6_txt, q3_7_txt, q3_8_txt, q4_1_txt, q4_2_txt, q4_3_txt, q4_4_txt, next;

    Handler handler;
    Intent register;
    String whatPage = "", getChild = "", getImage = "", getName = "", getNo = "", getGender = "", getBirth = "", getEdu = "", getIns = "", getLike = "", getQuest1 = "", getQuest2 = "";
    ArrayList<String> getQuest3, getQuest4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_child2);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#ffffff"));
            window.setBackgroundDrawable(null);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                setDarkMode(RegisterChild2Activity.this, true);
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

        next_con = findViewById(R.id.next_con);
        next = findViewById(R.id.next);

        q1_radio_group = findViewById(R.id.q1_radio_group);
        q2_radio_group = findViewById(R.id.q2_radio_group);
        q1_1_radio = findViewById(R.id.q1_1_radio);
        q1_2_radio = findViewById(R.id.q1_2_radio);
        q2_1_radio = findViewById(R.id.q2_1_radio);
        q2_2_radio = findViewById(R.id.q2_2_radio);
        q2_3_radio = findViewById(R.id.q2_3_radio);
        q2_4_radio = findViewById(R.id.q2_4_radio);

        q3_1_con = findViewById(R.id.q3_1_con);
        q3_2_con = findViewById(R.id.q3_2_con);
        q3_3_con = findViewById(R.id.q3_3_con);
        q3_4_con = findViewById(R.id.q3_4_con);
        q3_5_con = findViewById(R.id.q3_5_con);
        q3_6_con = findViewById(R.id.q3_6_con);
        q3_7_con = findViewById(R.id.q3_7_con);
        q3_8_con = findViewById(R.id.q3_8_con);
        q4_1_con = findViewById(R.id.q4_1_con);
        q4_2_con = findViewById(R.id.q4_2_con);
        q4_3_con = findViewById(R.id.q4_3_con);
        q4_4_con = findViewById(R.id.q4_4_con);

        q3_1_check = findViewById(R.id.q3_1_check);
        q3_2_check = findViewById(R.id.q3_2_check);
        q3_3_check = findViewById(R.id.q3_3_check);
        q3_4_check = findViewById(R.id.q3_4_check);
        q3_5_check = findViewById(R.id.q3_5_check);
        q3_6_check = findViewById(R.id.q3_6_check);
        q3_7_check = findViewById(R.id.q3_7_check);
        q3_8_check = findViewById(R.id.q3_8_check);
        q4_1_check = findViewById(R.id.q4_1_check);
        q4_2_check = findViewById(R.id.q4_2_check);
        q4_3_check = findViewById(R.id.q4_3_check);
        q4_4_check = findViewById(R.id.q4_4_check);

        title = findViewById(R.id.title);
        q3_1_txt = findViewById(R.id.q3_1_txt);
        q3_2_txt = findViewById(R.id.q3_2_txt);
        q3_3_txt = findViewById(R.id.q3_3_txt);
        q3_4_txt = findViewById(R.id.q3_4_txt);
        q3_5_txt = findViewById(R.id.q3_5_txt);
        q3_6_txt = findViewById(R.id.q3_6_txt);
        q3_7_txt = findViewById(R.id.q3_7_txt);
        q3_8_txt = findViewById(R.id.q3_8_txt);
        q4_1_txt = findViewById(R.id.q4_1_txt);
        q4_2_txt = findViewById(R.id.q4_2_txt);
        q4_3_txt = findViewById(R.id.q4_3_txt);
        q4_4_txt = findViewById(R.id.q4_4_txt);

        if ("register".equals(whatPage)) {
            title.setText("우리 아이 프로필 등록");
        } else if ("edit".equals(whatPage)) {
            title.setText("우리 아이 프로필 수정");
            handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    viewSettings();
                }
            }, 200);
        }

        q1_radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.q1_1_radio) {
                    getQuest1 = "1";
                } else if (checkedId == R.id.q1_2_radio) {
                    getQuest1 = "2";
                }
            }
        });

        q2_radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.q2_1_radio) {
                    getQuest2 = "1";
                } else if (checkedId == R.id.q2_2_radio) {
                    getQuest2 = "2";
                } else if (checkedId == R.id.q2_3_radio) {
                    getQuest2 = "3";
                } else if (checkedId == R.id.q2_4_radio) {
                    getQuest2 = "4";
                }
            }
        });

        getQuest3 = new ArrayList<>();
        getQuest4 = new ArrayList<>();

        q3_1_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    getQuest3.add("1");
                } else {
                    getQuest3.remove("1");
                }
            }
        });
        q3_2_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    getQuest3.add("2");
                } else {
                    getQuest3.remove("2");
                }
            }
        });
        q3_3_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    getQuest3.add("3");
                } else {
                    getQuest3.remove("3");
                }
            }
        });
        q3_4_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    getQuest3.add("4");
                } else {
                    getQuest3.remove("4");
                }
            }
        });
        q3_5_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    getQuest3.add("5");
                } else {
                    getQuest3.remove("5");
                }
            }
        });
        q3_6_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    getQuest3.add("6");
                } else {
                    getQuest3.remove("6");
                }
            }
        });
        q3_7_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    getQuest3.add("7");
                } else {
                    getQuest3.remove("7");
                }
            }
        });
        q3_8_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    getQuest3.add("8");
                } else {
                    getQuest3.remove("8");
                }
            }
        });

        q4_1_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    getQuest4.add("1");
                } else {
                    getQuest4.remove("1");
                }
            }
        });
        q4_2_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    getQuest4.add("2");
                } else {
                    getQuest4.remove("2");
                }
            }
        });
        q4_3_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    getQuest4.add("3");
                } else {
                    getQuest4.remove("3");
                }
            }
        });
        q4_4_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    getQuest4.add("4");
                } else {
                    getQuest4.remove("4");
                }
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
                if ("".equals(getQuest1)) {
                    oneBtnDialog = new OneBtnDialog(RegisterChild2Activity.this, "Q1을 선택해주세요 !", "확인");
                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    oneBtnDialog.setCancelable(false);
                    oneBtnDialog.show();
                    return;
                } else if ("".equals(getQuest2)) {
                    oneBtnDialog = new OneBtnDialog(RegisterChild2Activity.this, "Q2를 선택해주세요 !", "확인");
                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    oneBtnDialog.setCancelable(false);
                    oneBtnDialog.show();
                    return;
                } else if ("".equals(getQuest3)) {
                    oneBtnDialog = new OneBtnDialog(RegisterChild2Activity.this, "Q3을 선택해주세요 !", "확인");
                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    oneBtnDialog.setCancelable(false);
                    oneBtnDialog.show();
                    return;
                } else if ("".equals(getQuest4)) {
                    oneBtnDialog = new OneBtnDialog(RegisterChild2Activity.this, "Q4를 선택해주세요 !", "확인");
                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    oneBtnDialog.setCancelable(false);
                    oneBtnDialog.show();
                    return;
                }

                Intent intent = new Intent(RegisterChild2Activity.this, RegisterChild3Activity.class);
                intent.putExtra("what", whatPage);
                if ("edit".equals(whatPage)) {
                    intent.putExtra("child", getChild);
                }
                intent.putExtra("image", getImage);
                intent.putExtra("name", getName);
                intent.putExtra("no", getNo);
                intent.putExtra("gender", getGender);
                intent.putExtra("birth", getBirth);
                intent.putExtra("edu", getEdu);
                intent.putExtra("ins", getIns);
                intent.putExtra("like", getLike);

                intent.putExtra("quest1", getQuest1);
                intent.putExtra("quest2", getQuest2);
                intent.putExtra("quest3", getQuest3);
                intent.putExtra("quest4", getQuest4);

                startActivity(intent);
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

                        if ("1".equals(jsonData.getString("quest1"))) {
                            getQuest1 = "1";
                            q1_1_radio.setChecked(true);
                            q1_2_radio.setChecked(false);
                        } else {
                            getQuest1 = "2";
                            q1_1_radio.setChecked(false);
                            q1_2_radio.setChecked(true);
                        }

                        if ("1".equals(jsonData.getString("quest2"))) {
                            getQuest2 = "1";
                            q2_1_radio.setChecked(true);
                            q2_2_radio.setChecked(false);
                            q2_3_radio.setChecked(false);
                            q2_4_radio.setChecked(false);
                        } else if ("2".equals(jsonData.getString("quest2"))) {
                            getQuest2 = "2";
                            q2_1_radio.setChecked(false);
                            q2_2_radio.setChecked(true);
                            q2_3_radio.setChecked(false);
                            q2_4_radio.setChecked(false);
                        } else if ("3".equals(jsonData.getString("quest2"))) {
                            getQuest2 = "3";
                            q2_1_radio.setChecked(false);
                            q2_2_radio.setChecked(false);
                            q2_3_radio.setChecked(true);
                            q2_4_radio.setChecked(false);
                        } else if ("4".equals(jsonData.getString("quest2"))) {
                            getQuest2 = "4";
                            q2_1_radio.setChecked(false);
                            q2_2_radio.setChecked(false);
                            q2_3_radio.setChecked(false);
                            q2_4_radio.setChecked(true);
                        }

                        String[] q3 = jsonData.getString("quest3").split(",");

                        if (q3.length == 1) {
                            getQuest3.add(q3[0]);
                            if ("1".equals(q3[0])) {
                                q3_1_check.setChecked(true);
                            } else if ("2".equals(q3[0])) {
                                q3_2_check.setChecked(true);
                            } else if ("3".equals(q3[0])) {
                                q3_3_check.setChecked(true);
                            } else if ("4".equals(q3[0])) {
                                q3_4_check.setChecked(true);
                            } else if ("5".equals(q3[0])) {
                                q3_5_check.setChecked(true);
                            } else if ("6".equals(q3[0])) {
                                q3_6_check.setChecked(true);
                            } else if ("7".equals(q3[0])) {
                                q3_7_check.setChecked(true);
                            } else if ("8".equals(q3[0])) {
                                q3_8_check.setChecked(true);
                            }
                        }
                        if (q3.length == 2) {
                            getQuest3.add(q3[0]);
                            getQuest3.add(q3[1]);
                            if ("1".equals(q3[0])) {
                                q3_1_check.setChecked(true);
                            } else if ("2".equals(q3[0])) {
                                q3_2_check.setChecked(true);
                            } else if ("3".equals(q3[0])) {
                                q3_3_check.setChecked(true);
                            } else if ("4".equals(q3[0])) {
                                q3_4_check.setChecked(true);
                            } else if ("5".equals(q3[0])) {
                                q3_5_check.setChecked(true);
                            } else if ("6".equals(q3[0])) {
                                q3_6_check.setChecked(true);
                            } else if ("7".equals(q3[0])) {
                                q3_7_check.setChecked(true);
                            } else if ("8".equals(q3[0])) {
                                q3_8_check.setChecked(true);
                            }
                            if ("1".equals(q3[1])) {
                                q3_1_check.setChecked(true);
                            } else if ("2".equals(q3[1])) {
                                q3_2_check.setChecked(true);
                            } else if ("3".equals(q3[1])) {
                                q3_3_check.setChecked(true);
                            } else if ("4".equals(q3[1])) {
                                q3_4_check.setChecked(true);
                            } else if ("5".equals(q3[1])) {
                                q3_5_check.setChecked(true);
                            } else if ("6".equals(q3[1])) {
                                q3_6_check.setChecked(true);
                            } else if ("7".equals(q3[1])) {
                                q3_7_check.setChecked(true);
                            } else if ("8".equals(q3[1])) {
                                q3_8_check.setChecked(true);
                            }
                        }
                        if (q3.length == 3) {
                            getQuest3.add(q3[0]);
                            getQuest3.add(q3[1]);
                            getQuest3.add(q3[2]);
                            if ("1".equals(q3[0])) {
                                q3_1_check.setChecked(true);
                            } else if ("2".equals(q3[0])) {
                                q3_2_check.setChecked(true);
                            } else if ("3".equals(q3[0])) {
                                q3_3_check.setChecked(true);
                            } else if ("4".equals(q3[0])) {
                                q3_4_check.setChecked(true);
                            } else if ("5".equals(q3[0])) {
                                q3_5_check.setChecked(true);
                            } else if ("6".equals(q3[0])) {
                                q3_6_check.setChecked(true);
                            } else if ("7".equals(q3[0])) {
                                q3_7_check.setChecked(true);
                            } else if ("8".equals(q3[0])) {
                                q3_8_check.setChecked(true);
                            }
                            if ("1".equals(q3[1])) {
                                q3_1_check.setChecked(true);
                            } else if ("2".equals(q3[1])) {
                                q3_2_check.setChecked(true);
                            } else if ("3".equals(q3[1])) {
                                q3_3_check.setChecked(true);
                            } else if ("4".equals(q3[1])) {
                                q3_4_check.setChecked(true);
                            } else if ("5".equals(q3[1])) {
                                q3_5_check.setChecked(true);
                            } else if ("6".equals(q3[1])) {
                                q3_6_check.setChecked(true);
                            } else if ("7".equals(q3[1])) {
                                q3_7_check.setChecked(true);
                            } else if ("8".equals(q3[1])) {
                                q3_8_check.setChecked(true);
                            }
                            if ("1".equals(q3[2])) {
                                q3_1_check.setChecked(true);
                            } else if ("2".equals(q3[2])) {
                                q3_2_check.setChecked(true);
                            } else if ("3".equals(q3[2])) {
                                q3_3_check.setChecked(true);
                            } else if ("4".equals(q3[2])) {
                                q3_4_check.setChecked(true);
                            } else if ("5".equals(q3[2])) {
                                q3_5_check.setChecked(true);
                            } else if ("6".equals(q3[2])) {
                                q3_6_check.setChecked(true);
                            } else if ("7".equals(q3[2])) {
                                q3_7_check.setChecked(true);
                            } else if ("8".equals(q3[2])) {
                                q3_8_check.setChecked(true);
                            }
                        }
                        if (q3.length == 4) {
                            getQuest3.add(q3[0]);
                            getQuest3.add(q3[1]);
                            getQuest3.add(q3[2]);
                            getQuest3.add(q3[3]);
                            if ("1".equals(q3[0])) {
                                q3_1_check.setChecked(true);
                            } else if ("2".equals(q3[0])) {
                                q3_2_check.setChecked(true);
                            } else if ("3".equals(q3[0])) {
                                q3_3_check.setChecked(true);
                            } else if ("4".equals(q3[0])) {
                                q3_4_check.setChecked(true);
                            } else if ("5".equals(q3[0])) {
                                q3_5_check.setChecked(true);
                            } else if ("6".equals(q3[0])) {
                                q3_6_check.setChecked(true);
                            } else if ("7".equals(q3[0])) {
                                q3_7_check.setChecked(true);
                            } else if ("8".equals(q3[0])) {
                                q3_8_check.setChecked(true);
                            }
                            if ("1".equals(q3[1])) {
                                q3_1_check.setChecked(true);
                            } else if ("2".equals(q3[1])) {
                                q3_2_check.setChecked(true);
                            } else if ("3".equals(q3[1])) {
                                q3_3_check.setChecked(true);
                            } else if ("4".equals(q3[1])) {
                                q3_4_check.setChecked(true);
                            } else if ("5".equals(q3[1])) {
                                q3_5_check.setChecked(true);
                            } else if ("6".equals(q3[1])) {
                                q3_6_check.setChecked(true);
                            } else if ("7".equals(q3[1])) {
                                q3_7_check.setChecked(true);
                            } else if ("8".equals(q3[1])) {
                                q3_8_check.setChecked(true);
                            }
                            if ("1".equals(q3[2])) {
                                q3_1_check.setChecked(true);
                            } else if ("2".equals(q3[2])) {
                                q3_2_check.setChecked(true);
                            } else if ("3".equals(q3[2])) {
                                q3_3_check.setChecked(true);
                            } else if ("4".equals(q3[2])) {
                                q3_4_check.setChecked(true);
                            } else if ("5".equals(q3[2])) {
                                q3_5_check.setChecked(true);
                            } else if ("6".equals(q3[2])) {
                                q3_6_check.setChecked(true);
                            } else if ("7".equals(q3[2])) {
                                q3_7_check.setChecked(true);
                            } else if ("8".equals(q3[2])) {
                                q3_8_check.setChecked(true);
                            }
                            if ("1".equals(q3[3])) {
                                q3_1_check.setChecked(true);
                            } else if ("2".equals(q3[3])) {
                                q3_2_check.setChecked(true);
                            } else if ("3".equals(q3[3])) {
                                q3_3_check.setChecked(true);
                            } else if ("4".equals(q3[3])) {
                                q3_4_check.setChecked(true);
                            } else if ("5".equals(q3[3])) {
                                q3_5_check.setChecked(true);
                            } else if ("6".equals(q3[3])) {
                                q3_6_check.setChecked(true);
                            } else if ("7".equals(q3[3])) {
                                q3_7_check.setChecked(true);
                            } else if ("8".equals(q3[3])) {
                                q3_8_check.setChecked(true);
                            }
                        }
                        if (q3.length == 5) {
                            getQuest3.add(q3[0]);
                            getQuest3.add(q3[1]);
                            getQuest3.add(q3[2]);
                            getQuest3.add(q3[3]);
                            getQuest3.add(q3[4]);
                            if ("1".equals(q3[0])) {
                                q3_1_check.setChecked(true);
                            } else if ("2".equals(q3[0])) {
                                q3_2_check.setChecked(true);
                            } else if ("3".equals(q3[0])) {
                                q3_3_check.setChecked(true);
                            } else if ("4".equals(q3[0])) {
                                q3_4_check.setChecked(true);
                            } else if ("5".equals(q3[0])) {
                                q3_5_check.setChecked(true);
                            } else if ("6".equals(q3[0])) {
                                q3_6_check.setChecked(true);
                            } else if ("7".equals(q3[0])) {
                                q3_7_check.setChecked(true);
                            } else if ("8".equals(q3[0])) {
                                q3_8_check.setChecked(true);
                            }
                            if ("1".equals(q3[1])) {
                                q3_1_check.setChecked(true);
                            } else if ("2".equals(q3[1])) {
                                q3_2_check.setChecked(true);
                            } else if ("3".equals(q3[1])) {
                                q3_3_check.setChecked(true);
                            } else if ("4".equals(q3[1])) {
                                q3_4_check.setChecked(true);
                            } else if ("5".equals(q3[1])) {
                                q3_5_check.setChecked(true);
                            } else if ("6".equals(q3[1])) {
                                q3_6_check.setChecked(true);
                            } else if ("7".equals(q3[1])) {
                                q3_7_check.setChecked(true);
                            } else if ("8".equals(q3[1])) {
                                q3_8_check.setChecked(true);
                            }
                            if ("1".equals(q3[2])) {
                                q3_1_check.setChecked(true);
                            } else if ("2".equals(q3[2])) {
                                q3_2_check.setChecked(true);
                            } else if ("3".equals(q3[2])) {
                                q3_3_check.setChecked(true);
                            } else if ("4".equals(q3[2])) {
                                q3_4_check.setChecked(true);
                            } else if ("5".equals(q3[2])) {
                                q3_5_check.setChecked(true);
                            } else if ("6".equals(q3[2])) {
                                q3_6_check.setChecked(true);
                            } else if ("7".equals(q3[2])) {
                                q3_7_check.setChecked(true);
                            } else if ("8".equals(q3[2])) {
                                q3_8_check.setChecked(true);
                            }
                            if ("1".equals(q3[3])) {
                                q3_1_check.setChecked(true);
                            } else if ("2".equals(q3[3])) {
                                q3_2_check.setChecked(true);
                            } else if ("3".equals(q3[3])) {
                                q3_3_check.setChecked(true);
                            } else if ("4".equals(q3[3])) {
                                q3_4_check.setChecked(true);
                            } else if ("5".equals(q3[3])) {
                                q3_5_check.setChecked(true);
                            } else if ("6".equals(q3[3])) {
                                q3_6_check.setChecked(true);
                            } else if ("7".equals(q3[3])) {
                                q3_7_check.setChecked(true);
                            } else if ("8".equals(q3[3])) {
                                q3_8_check.setChecked(true);
                            }
                            if ("1".equals(q3[4])) {
                                q3_1_check.setChecked(true);
                            } else if ("2".equals(q3[4])) {
                                q3_2_check.setChecked(true);
                            } else if ("3".equals(q3[4])) {
                                q3_3_check.setChecked(true);
                            } else if ("4".equals(q3[4])) {
                                q3_4_check.setChecked(true);
                            } else if ("5".equals(q3[4])) {
                                q3_5_check.setChecked(true);
                            } else if ("6".equals(q3[4])) {
                                q3_6_check.setChecked(true);
                            } else if ("7".equals(q3[4])) {
                                q3_7_check.setChecked(true);
                            } else if ("8".equals(q3[4])) {
                                q3_8_check.setChecked(true);
                            }
                        }
                        if (q3.length == 6) {
                            getQuest3.add(q3[0]);
                            getQuest3.add(q3[1]);
                            getQuest3.add(q3[2]);
                            getQuest3.add(q3[3]);
                            getQuest3.add(q3[4]);
                            getQuest3.add(q3[5]);
                            if ("1".equals(q3[0])) {
                                q3_1_check.setChecked(true);
                            } else if ("2".equals(q3[0])) {
                                q3_2_check.setChecked(true);
                            } else if ("3".equals(q3[0])) {
                                q3_3_check.setChecked(true);
                            } else if ("4".equals(q3[0])) {
                                q3_4_check.setChecked(true);
                            } else if ("5".equals(q3[0])) {
                                q3_5_check.setChecked(true);
                            } else if ("6".equals(q3[0])) {
                                q3_6_check.setChecked(true);
                            } else if ("7".equals(q3[0])) {
                                q3_7_check.setChecked(true);
                            } else if ("8".equals(q3[0])) {
                                q3_8_check.setChecked(true);
                            }
                            if ("1".equals(q3[1])) {
                                q3_1_check.setChecked(true);
                            } else if ("2".equals(q3[1])) {
                                q3_2_check.setChecked(true);
                            } else if ("3".equals(q3[1])) {
                                q3_3_check.setChecked(true);
                            } else if ("4".equals(q3[1])) {
                                q3_4_check.setChecked(true);
                            } else if ("5".equals(q3[1])) {
                                q3_5_check.setChecked(true);
                            } else if ("6".equals(q3[1])) {
                                q3_6_check.setChecked(true);
                            } else if ("7".equals(q3[1])) {
                                q3_7_check.setChecked(true);
                            } else if ("8".equals(q3[1])) {
                                q3_8_check.setChecked(true);
                            }
                            if ("1".equals(q3[2])) {
                                q3_1_check.setChecked(true);
                            } else if ("2".equals(q3[2])) {
                                q3_2_check.setChecked(true);
                            } else if ("3".equals(q3[2])) {
                                q3_3_check.setChecked(true);
                            } else if ("4".equals(q3[2])) {
                                q3_4_check.setChecked(true);
                            } else if ("5".equals(q3[2])) {
                                q3_5_check.setChecked(true);
                            } else if ("6".equals(q3[2])) {
                                q3_6_check.setChecked(true);
                            } else if ("7".equals(q3[2])) {
                                q3_7_check.setChecked(true);
                            } else if ("8".equals(q3[2])) {
                                q3_8_check.setChecked(true);
                            }
                            if ("1".equals(q3[3])) {
                                q3_1_check.setChecked(true);
                            } else if ("2".equals(q3[3])) {
                                q3_2_check.setChecked(true);
                            } else if ("3".equals(q3[3])) {
                                q3_3_check.setChecked(true);
                            } else if ("4".equals(q3[3])) {
                                q3_4_check.setChecked(true);
                            } else if ("5".equals(q3[3])) {
                                q3_5_check.setChecked(true);
                            } else if ("6".equals(q3[3])) {
                                q3_6_check.setChecked(true);
                            } else if ("7".equals(q3[3])) {
                                q3_7_check.setChecked(true);
                            } else if ("8".equals(q3[3])) {
                                q3_8_check.setChecked(true);
                            }
                            if ("1".equals(q3[4])) {
                                q3_1_check.setChecked(true);
                            } else if ("2".equals(q3[4])) {
                                q3_2_check.setChecked(true);
                            } else if ("3".equals(q3[4])) {
                                q3_3_check.setChecked(true);
                            } else if ("4".equals(q3[4])) {
                                q3_4_check.setChecked(true);
                            } else if ("5".equals(q3[4])) {
                                q3_5_check.setChecked(true);
                            } else if ("6".equals(q3[4])) {
                                q3_6_check.setChecked(true);
                            } else if ("7".equals(q3[4])) {
                                q3_7_check.setChecked(true);
                            } else if ("8".equals(q3[4])) {
                                q3_8_check.setChecked(true);
                            }
                            if ("1".equals(q3[5])) {
                                q3_1_check.setChecked(true);
                            } else if ("2".equals(q3[5])) {
                                q3_2_check.setChecked(true);
                            } else if ("3".equals(q3[5])) {
                                q3_3_check.setChecked(true);
                            } else if ("4".equals(q3[5])) {
                                q3_4_check.setChecked(true);
                            } else if ("5".equals(q3[5])) {
                                q3_5_check.setChecked(true);
                            } else if ("6".equals(q3[5])) {
                                q3_6_check.setChecked(true);
                            } else if ("7".equals(q3[5])) {
                                q3_7_check.setChecked(true);
                            } else if ("8".equals(q3[5])) {
                                q3_8_check.setChecked(true);
                            }
                        }
                        if (q3.length == 7) {
                            getQuest3.add(q3[0]);
                            getQuest3.add(q3[1]);
                            getQuest3.add(q3[2]);
                            getQuest3.add(q3[3]);
                            getQuest3.add(q3[4]);
                            getQuest3.add(q3[5]);
                            getQuest3.add(q3[6]);
                            if ("1".equals(q3[0])) {
                                q3_1_check.setChecked(true);
                            } else if ("2".equals(q3[0])) {
                                q3_2_check.setChecked(true);
                            } else if ("3".equals(q3[0])) {
                                q3_3_check.setChecked(true);
                            } else if ("4".equals(q3[0])) {
                                q3_4_check.setChecked(true);
                            } else if ("5".equals(q3[0])) {
                                q3_5_check.setChecked(true);
                            } else if ("6".equals(q3[0])) {
                                q3_6_check.setChecked(true);
                            } else if ("7".equals(q3[0])) {
                                q3_7_check.setChecked(true);
                            } else if ("8".equals(q3[0])) {
                                q3_8_check.setChecked(true);
                            }
                            if ("1".equals(q3[1])) {
                                q3_1_check.setChecked(true);
                            } else if ("2".equals(q3[1])) {
                                q3_2_check.setChecked(true);
                            } else if ("3".equals(q3[1])) {
                                q3_3_check.setChecked(true);
                            } else if ("4".equals(q3[1])) {
                                q3_4_check.setChecked(true);
                            } else if ("5".equals(q3[1])) {
                                q3_5_check.setChecked(true);
                            } else if ("6".equals(q3[1])) {
                                q3_6_check.setChecked(true);
                            } else if ("7".equals(q3[1])) {
                                q3_7_check.setChecked(true);
                            } else if ("8".equals(q3[1])) {
                                q3_8_check.setChecked(true);
                            }
                            if ("1".equals(q3[2])) {
                                q3_1_check.setChecked(true);
                            } else if ("2".equals(q3[2])) {
                                q3_2_check.setChecked(true);
                            } else if ("3".equals(q3[2])) {
                                q3_3_check.setChecked(true);
                            } else if ("4".equals(q3[2])) {
                                q3_4_check.setChecked(true);
                            } else if ("5".equals(q3[2])) {
                                q3_5_check.setChecked(true);
                            } else if ("6".equals(q3[2])) {
                                q3_6_check.setChecked(true);
                            } else if ("7".equals(q3[2])) {
                                q3_7_check.setChecked(true);
                            } else if ("8".equals(q3[2])) {
                                q3_8_check.setChecked(true);
                            }
                            if ("1".equals(q3[3])) {
                                q3_1_check.setChecked(true);
                            } else if ("2".equals(q3[3])) {
                                q3_2_check.setChecked(true);
                            } else if ("3".equals(q3[3])) {
                                q3_3_check.setChecked(true);
                            } else if ("4".equals(q3[3])) {
                                q3_4_check.setChecked(true);
                            } else if ("5".equals(q3[3])) {
                                q3_5_check.setChecked(true);
                            } else if ("6".equals(q3[3])) {
                                q3_6_check.setChecked(true);
                            } else if ("7".equals(q3[3])) {
                                q3_7_check.setChecked(true);
                            } else if ("8".equals(q3[3])) {
                                q3_8_check.setChecked(true);
                            }
                            if ("1".equals(q3[4])) {
                                q3_1_check.setChecked(true);
                            } else if ("2".equals(q3[4])) {
                                q3_2_check.setChecked(true);
                            } else if ("3".equals(q3[4])) {
                                q3_3_check.setChecked(true);
                            } else if ("4".equals(q3[4])) {
                                q3_4_check.setChecked(true);
                            } else if ("5".equals(q3[4])) {
                                q3_5_check.setChecked(true);
                            } else if ("6".equals(q3[4])) {
                                q3_6_check.setChecked(true);
                            } else if ("7".equals(q3[4])) {
                                q3_7_check.setChecked(true);
                            } else if ("8".equals(q3[4])) {
                                q3_8_check.setChecked(true);
                            }
                            if ("1".equals(q3[5])) {
                                q3_1_check.setChecked(true);
                            } else if ("2".equals(q3[5])) {
                                q3_2_check.setChecked(true);
                            } else if ("3".equals(q3[5])) {
                                q3_3_check.setChecked(true);
                            } else if ("4".equals(q3[5])) {
                                q3_4_check.setChecked(true);
                            } else if ("5".equals(q3[5])) {
                                q3_5_check.setChecked(true);
                            } else if ("6".equals(q3[5])) {
                                q3_6_check.setChecked(true);
                            } else if ("7".equals(q3[5])) {
                                q3_7_check.setChecked(true);
                            } else if ("8".equals(q3[5])) {
                                q3_8_check.setChecked(true);
                            }
                            if ("1".equals(q3[6])) {
                                q3_1_check.setChecked(true);
                            } else if ("2".equals(q3[6])) {
                                q3_2_check.setChecked(true);
                            } else if ("3".equals(q3[6])) {
                                q3_3_check.setChecked(true);
                            } else if ("4".equals(q3[6])) {
                                q3_4_check.setChecked(true);
                            } else if ("5".equals(q3[6])) {
                                q3_5_check.setChecked(true);
                            } else if ("6".equals(q3[6])) {
                                q3_6_check.setChecked(true);
                            } else if ("7".equals(q3[6])) {
                                q3_7_check.setChecked(true);
                            } else if ("8".equals(q3[6])) {
                                q3_8_check.setChecked(true);
                            }
                        }
                        if (q3.length == 8) {
                            getQuest3.add(q3[0]);
                            getQuest3.add(q3[1]);
                            getQuest3.add(q3[2]);
                            getQuest3.add(q3[3]);
                            getQuest3.add(q3[4]);
                            getQuest3.add(q3[5]);
                            getQuest3.add(q3[6]);
                            getQuest3.add(q3[7]);
                            if ("1".equals(q3[0])) {
                                q3_1_check.setChecked(true);
                            } else if ("2".equals(q3[0])) {
                                q3_2_check.setChecked(true);
                            } else if ("3".equals(q3[0])) {
                                q3_3_check.setChecked(true);
                            } else if ("4".equals(q3[0])) {
                                q3_4_check.setChecked(true);
                            } else if ("5".equals(q3[0])) {
                                q3_5_check.setChecked(true);
                            } else if ("6".equals(q3[0])) {
                                q3_6_check.setChecked(true);
                            } else if ("7".equals(q3[0])) {
                                q3_7_check.setChecked(true);
                            } else if ("8".equals(q3[0])) {
                                q3_8_check.setChecked(true);
                            }
                            if ("1".equals(q3[1])) {
                                q3_1_check.setChecked(true);
                            } else if ("2".equals(q3[1])) {
                                q3_2_check.setChecked(true);
                            } else if ("3".equals(q3[1])) {
                                q3_3_check.setChecked(true);
                            } else if ("4".equals(q3[1])) {
                                q3_4_check.setChecked(true);
                            } else if ("5".equals(q3[1])) {
                                q3_5_check.setChecked(true);
                            } else if ("6".equals(q3[1])) {
                                q3_6_check.setChecked(true);
                            } else if ("7".equals(q3[1])) {
                                q3_7_check.setChecked(true);
                            } else if ("8".equals(q3[1])) {
                                q3_8_check.setChecked(true);
                            }
                            if ("1".equals(q3[2])) {
                                q3_1_check.setChecked(true);
                            } else if ("2".equals(q3[2])) {
                                q3_2_check.setChecked(true);
                            } else if ("3".equals(q3[2])) {
                                q3_3_check.setChecked(true);
                            } else if ("4".equals(q3[2])) {
                                q3_4_check.setChecked(true);
                            } else if ("5".equals(q3[2])) {
                                q3_5_check.setChecked(true);
                            } else if ("6".equals(q3[2])) {
                                q3_6_check.setChecked(true);
                            } else if ("7".equals(q3[2])) {
                                q3_7_check.setChecked(true);
                            } else if ("8".equals(q3[2])) {
                                q3_8_check.setChecked(true);
                            }
                            if ("1".equals(q3[3])) {
                                q3_1_check.setChecked(true);
                            } else if ("2".equals(q3[3])) {
                                q3_2_check.setChecked(true);
                            } else if ("3".equals(q3[3])) {
                                q3_3_check.setChecked(true);
                            } else if ("4".equals(q3[3])) {
                                q3_4_check.setChecked(true);
                            } else if ("5".equals(q3[3])) {
                                q3_5_check.setChecked(true);
                            } else if ("6".equals(q3[3])) {
                                q3_6_check.setChecked(true);
                            } else if ("7".equals(q3[3])) {
                                q3_7_check.setChecked(true);
                            } else if ("8".equals(q3[3])) {
                                q3_8_check.setChecked(true);
                            }
                            if ("1".equals(q3[4])) {
                                q3_1_check.setChecked(true);
                            } else if ("2".equals(q3[4])) {
                                q3_2_check.setChecked(true);
                            } else if ("3".equals(q3[4])) {
                                q3_3_check.setChecked(true);
                            } else if ("4".equals(q3[4])) {
                                q3_4_check.setChecked(true);
                            } else if ("5".equals(q3[4])) {
                                q3_5_check.setChecked(true);
                            } else if ("6".equals(q3[4])) {
                                q3_6_check.setChecked(true);
                            } else if ("7".equals(q3[4])) {
                                q3_7_check.setChecked(true);
                            } else if ("8".equals(q3[4])) {
                                q3_8_check.setChecked(true);
                            }
                            if ("1".equals(q3[5])) {
                                q3_1_check.setChecked(true);
                            } else if ("2".equals(q3[5])) {
                                q3_2_check.setChecked(true);
                            } else if ("3".equals(q3[5])) {
                                q3_3_check.setChecked(true);
                            } else if ("4".equals(q3[5])) {
                                q3_4_check.setChecked(true);
                            } else if ("5".equals(q3[5])) {
                                q3_5_check.setChecked(true);
                            } else if ("6".equals(q3[5])) {
                                q3_6_check.setChecked(true);
                            } else if ("7".equals(q3[5])) {
                                q3_7_check.setChecked(true);
                            } else if ("8".equals(q3[5])) {
                                q3_8_check.setChecked(true);
                            }
                            if ("1".equals(q3[6])) {
                                q3_1_check.setChecked(true);
                            } else if ("2".equals(q3[6])) {
                                q3_2_check.setChecked(true);
                            } else if ("3".equals(q3[6])) {
                                q3_3_check.setChecked(true);
                            } else if ("4".equals(q3[6])) {
                                q3_4_check.setChecked(true);
                            } else if ("5".equals(q3[6])) {
                                q3_5_check.setChecked(true);
                            } else if ("6".equals(q3[6])) {
                                q3_6_check.setChecked(true);
                            } else if ("7".equals(q3[6])) {
                                q3_7_check.setChecked(true);
                            } else if ("8".equals(q3[6])) {
                                q3_8_check.setChecked(true);
                            }
                            if ("1".equals(q3[7])) {
                                q3_1_check.setChecked(true);
                            } else if ("2".equals(q3[7])) {
                                q3_2_check.setChecked(true);
                            } else if ("3".equals(q3[7])) {
                                q3_3_check.setChecked(true);
                            } else if ("4".equals(q3[7])) {
                                q3_4_check.setChecked(true);
                            } else if ("5".equals(q3[7])) {
                                q3_5_check.setChecked(true);
                            } else if ("6".equals(q3[7])) {
                                q3_6_check.setChecked(true);
                            } else if ("7".equals(q3[7])) {
                                q3_7_check.setChecked(true);
                            } else if ("8".equals(q3[7])) {
                                q3_8_check.setChecked(true);
                            }
                        }

                        String[] q4 = jsonData.getString("quest4").split(",");
                        if (q4.length == 1) {
                            getQuest4.add(q4[0]);
                            if ("1".equals(q4[0])) {
                                q4_1_check.setChecked(true);
                            } else if ("2".equals(q4[0])) {
                                q4_2_check.setChecked(true);
                            } else if ("3".equals(q4[0])) {
                                q4_3_check.setChecked(true);
                            } else if ("4".equals(q4[0])) {
                                q4_4_check.setChecked(true);
                            }
                        }
                        if (q4.length == 2) {
                            getQuest4.add(q4[0]);
                            getQuest4.add(q4[1]);
                            if ("1".equals(q4[0])) {
                                q4_1_check.setChecked(true);
                            } else if ("2".equals(q4[0])) {
                                q4_2_check.setChecked(true);
                            } else if ("3".equals(q4[0])) {
                                q4_3_check.setChecked(true);
                            } else if ("4".equals(q4[0])) {
                                q4_4_check.setChecked(true);
                            }
                            if ("1".equals(q4[1])) {
                                q4_1_check.setChecked(true);
                            } else if ("2".equals(q4[1])) {
                                q4_2_check.setChecked(true);
                            } else if ("3".equals(q4[1])) {
                                q4_3_check.setChecked(true);
                            } else if ("4".equals(q4[1])) {
                                q4_4_check.setChecked(true);
                            }
                        }
                        if (q4.length == 3) {
                            getQuest4.add(q4[0]);
                            getQuest4.add(q4[1]);
                            getQuest4.add(q4[2]);
                            if ("1".equals(q4[0])) {
                                q4_1_check.setChecked(true);
                            } else if ("2".equals(q4[0])) {
                                q4_2_check.setChecked(true);
                            } else if ("3".equals(q4[0])) {
                                q4_3_check.setChecked(true);
                            } else if ("4".equals(q4[0])) {
                                q4_4_check.setChecked(true);
                            }
                            if ("1".equals(q4[1])) {
                                q4_1_check.setChecked(true);
                            } else if ("2".equals(q4[1])) {
                                q4_2_check.setChecked(true);
                            } else if ("3".equals(q4[1])) {
                                q4_3_check.setChecked(true);
                            } else if ("4".equals(q4[1])) {
                                q4_4_check.setChecked(true);
                            }
                            if ("1".equals(q4[2])) {
                                q4_1_check.setChecked(true);
                            } else if ("2".equals(q4[2])) {
                                q4_2_check.setChecked(true);
                            } else if ("3".equals(q4[2])) {
                                q4_3_check.setChecked(true);
                            } else if ("4".equals(q4[2])) {
                                q4_4_check.setChecked(true);
                            }
                        }
                        if (q4.length == 4) {
                            getQuest4.add(q4[0]);
                            getQuest4.add(q4[1]);
                            getQuest4.add(q4[2]);
                            getQuest4.add(q4[3]);
                            if ("1".equals(q4[0])) {
                                q4_1_check.setChecked(true);
                            } else if ("2".equals(q4[0])) {
                                q4_2_check.setChecked(true);
                            } else if ("3".equals(q4[0])) {
                                q4_3_check.setChecked(true);
                            } else if ("4".equals(q4[0])) {
                                q4_4_check.setChecked(true);
                            }
                            if ("1".equals(q4[1])) {
                                q4_1_check.setChecked(true);
                            } else if ("2".equals(q4[1])) {
                                q4_2_check.setChecked(true);
                            } else if ("3".equals(q4[1])) {
                                q4_3_check.setChecked(true);
                            } else if ("4".equals(q4[1])) {
                                q4_4_check.setChecked(true);
                            }
                            if ("1".equals(q4[2])) {
                                q4_1_check.setChecked(true);
                            } else if ("2".equals(q4[2])) {
                                q4_2_check.setChecked(true);
                            } else if ("3".equals(q4[2])) {
                                q4_3_check.setChecked(true);
                            } else if ("4".equals(q4[2])) {
                                q4_4_check.setChecked(true);
                            }
                            if ("1".equals(q4[3])) {
                                q4_1_check.setChecked(true);
                            } else if ("2".equals(q4[3])) {
                                q4_2_check.setChecked(true);
                            } else if ("3".equals(q4[3])) {
                                q4_3_check.setChecked(true);
                            } else if ("4".equals(q4[3])) {
                                q4_4_check.setChecked(true);
                            }
                        }

                    } else if (!jsonObject.getBoolean("return")) {

                        if ("login".equals(jsonObject.getString("type"))) {
                            Toast.makeText(RegisterChild2Activity.this, "로그인 해주세요 ! ", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegisterChild2Activity.this, LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            oneBtnDialog = new OneBtnDialog(RegisterChild2Activity.this, "정보를 불러올 수 없습니다 !", "확인");
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
