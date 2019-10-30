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
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static com.woojeong.global.ifriend.GlobalApplication.setDarkMode;

public class IntoChildProfileActivity extends AppCompatActivity {
    private static String TAG = "IntoChildProfileActivity";
    Context context;
    InputMethodManager ipmm;
    AQuery aQuery = null;
    OneBtnDialog oneBtnDialog;

    FrameLayout back_con;
    ImageView back, photo;
    ScrollView scrollView;
    TextView name, ranking, gender, birth, agency, insurance, interest, answer1, answer2, answer3_1, answer3_2, answer3_3, answer3_4, answer3_5, answer3_6, answer3_7, answer3_8, answer4_1, answer4_2, answer4_3, answer4_4, careful, etc;
    LinearLayout answer3_con, answer3_1_con, answer3_2_con, answer3_3_con, answer3_4_con, answer3_5_con, answer3_6_con, answer3_7_con, answer3_8_con,
            answer4_con, answer4_1_con, answer4_2_con, answer4_3_con, answer4_4_con;

    String getChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_into_child_profile);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#ffffff"));
            window.setBackgroundDrawable(null);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                setDarkMode(IntoChildProfileActivity.this, true);
            }
        }

        getChild = getIntent().getStringExtra("child");

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
        scrollView = findViewById(R.id.scrollView);

        name = findViewById(R.id.name);
        ranking = findViewById(R.id.ranking);
        gender = findViewById(R.id.gender);
        birth = findViewById(R.id.birth);
        agency = findViewById(R.id.agency);
        insurance = findViewById(R.id.insurance);
        interest = findViewById(R.id.interest);
        answer1 = findViewById(R.id.answer1);
        answer2 = findViewById(R.id.answer2);
        answer3_1 = findViewById(R.id.answer3_1);
        answer3_2 = findViewById(R.id.answer3_2);
        answer3_3 = findViewById(R.id.answer3_3);
        answer3_4 = findViewById(R.id.answer3_4);
        answer3_5 = findViewById(R.id.answer3_5);
        answer3_6 = findViewById(R.id.answer3_6);
        answer3_7 = findViewById(R.id.answer3_7);
        answer3_8 = findViewById(R.id.answer3_8);

        answer4_1 = findViewById(R.id.answer4_1);
        answer4_2 = findViewById(R.id.answer4_2);
        answer4_3 = findViewById(R.id.answer4_3);
        answer4_4 = findViewById(R.id.answer4_4);

        answer3_con = findViewById(R.id.answer3_con);
        answer3_1_con = findViewById(R.id.answer3_1_con);
        answer3_2_con = findViewById(R.id.answer3_2_con);
        answer3_3_con = findViewById(R.id.answer3_3_con);
        answer3_4_con = findViewById(R.id.answer3_4_con);
        answer3_5_con = findViewById(R.id.answer3_5_con);
        answer3_6_con = findViewById(R.id.answer3_6_con);
        answer3_7_con = findViewById(R.id.answer3_7_con);
        answer3_8_con = findViewById(R.id.answer3_8_con);
        answer3_1 = findViewById(R.id.answer3_1);
        answer3_2 = findViewById(R.id.answer3_2);
        answer3_3 = findViewById(R.id.answer3_3);
        answer3_4 = findViewById(R.id.answer3_4);
        answer3_5 = findViewById(R.id.answer3_5);
        answer3_6 = findViewById(R.id.answer3_6);
        answer3_7 = findViewById(R.id.answer3_7);
        answer3_8 = findViewById(R.id.answer3_8);

        answer4_con = findViewById(R.id.answer4_con);
        answer4_1_con = findViewById(R.id.answer4_1_con);
        answer4_2_con = findViewById(R.id.answer4_2_con);
        answer4_3_con = findViewById(R.id.answer4_3_con);
        answer4_4_con = findViewById(R.id.answer4_4_con);

        careful = findViewById(R.id.careful);
        etc = findViewById(R.id.etc);

        viewSettings();
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

                        Glide.with(IntoChildProfileActivity.this)
                                .load(ServerUrl.getBaseUrl() + "/uploads/images/origin/" + jsonData.getString("image"))
                                .into(photo);

                        name.setText(jsonData.getString("name"));

                        if("1".equals(jsonData.getString("no"))){
                            ranking.setText("첫째");
                        } else if("2".equals(jsonData.getString("no"))){
                            ranking.setText("둘째");
                        } else if("3".equals(jsonData.getString("no"))){
                            ranking.setText("셋째");
                        } else if("4".equals(jsonData.getString("no"))){
                            ranking.setText("넷째");
                        }

                        if("1".equals(jsonData.getString("gender"))){
                            gender.setText("남아");
                        } else if("2".equals(jsonData.getString("gender"))){
                            gender.setText("여아");
                        }
                        birth.setText(jsonData.getString("birth"));
                        agency.setText(jsonData.getString("edu"));
                        if("0".equals(jsonData.getString("ins"))){
                            insurance.setText("무");
                        } else {
                            insurance.setText("유");
                        }

                        interest.setText(jsonData.getString("like"));

                        if ("1".equals(jsonData.getString("quest1"))) {
                            answer1.setText("혼자서도 잘 놀아요.");
                        } else {
                            answer1.setText("엄마, 아빠 곁을 잘 안떠나려고 해요.");
                        }

                        if ("1".equals(jsonData.getString("quest2"))) {
                            answer2.setText("또래 친구를 좋아하며 잘 어울려요.");
                        } else if ("2".equals(jsonData.getString("quest2"))) {
                            answer2.setText("처음에는 낯을 조금 가려요.");
                        } else if ("3".equals(jsonData.getString("quest2"))) {
                            answer2.setText("친해지려면 시간이 오래 걸려요.");
                        } else if ("4".equals(jsonData.getString("quest2"))) {
                            answer2.setText("다른 아이들과 잘 어울리지 못해요.");
                        }

                        String[] quest3 = jsonData.getString("quest3").split(",");
                        if (quest3.length == 0 || "null".equals(jsonData.getString("quest3"))) {
                            answer3_con.setVisibility(View.GONE);
                        } else {
                            answer3_con.setVisibility(View.VISIBLE);

                            if (quest3.length == 1) {
                                answer3_1_con.setVisibility(View.VISIBLE);

                                if ("1".equals(quest3[0])) {
                                    answer3_1.setText("에너지가 넘치는 편이고 활동량이 많아요.");
                                } else if ("2".equals(quest3[0])) {
                                    answer3_1.setText("차분하고 조용한 편이에요.");
                                } else if ("3".equals(quest3[0])) {
                                    answer3_1.setText("한 가지 활동에 집중을 잘해요.");
                                } else if ("4".equals(quest3[0])) {
                                    answer3_1.setText("외부 활동을 좋아해요.");
                                } else if ("5".equals(quest3[0])) {
                                    answer3_1.setText("실내 활동을 좋아해요.");
                                } else if ("6".equals(quest3[0])) {
                                    answer3_1.setText("승부욕이 강한 편이에요.");
                                } else if ("7".equals(quest3[0])) {
                                    answer3_1.setText("책 읽는 걸 좋아해요.");
                                } else if ("8".equals(quest3[0])) {
                                    answer3_1.setText("장난감 또는 인형놀이를 좋아해요.");
                                }
                            } else if (quest3.length == 2) {
                                answer3_1_con.setVisibility(View.VISIBLE);
                                answer3_2_con.setVisibility(View.VISIBLE);

                                if ("1".equals(quest3[0])) {
                                    answer3_1.setText("에너지가 넘치는 편이고 활동량이 많아요.");
                                } else if ("2".equals(quest3[0])) {
                                    answer3_1.setText("차분하고 조용한 편이에요.");
                                } else if ("3".equals(quest3[0])) {
                                    answer3_1.setText("한 가지 활동에 집중을 잘해요.");
                                } else if ("4".equals(quest3[0])) {
                                    answer3_1.setText("외부 활동을 좋아해요.");
                                } else if ("5".equals(quest3[0])) {
                                    answer3_1.setText("실내 활동을 좋아해요.");
                                } else if ("6".equals(quest3[0])) {
                                    answer3_1.setText("승부욕이 강한 편이에요.");
                                } else if ("7".equals(quest3[0])) {
                                    answer3_1.setText("책 읽는 걸 좋아해요.");
                                } else if ("8".equals(quest3[0])) {
                                    answer3_1.setText("장난감 또는 인형놀이를 좋아해요.");
                                }

                                if ("1".equals(quest3[1])) {
                                    answer3_2.setText("에너지가 넘치는 편이고 활동량이 많아요.");
                                } else if ("2".equals(quest3[1])) {
                                    answer3_2.setText("차분하고 조용한 편이에요.");
                                } else if ("3".equals(quest3[1])) {
                                    answer3_2.setText("한 가지 활동에 집중을 잘해요.");
                                } else if ("4".equals(quest3[1])) {
                                    answer3_2.setText("외부 활동을 좋아해요.");
                                } else if ("5".equals(quest3[1])) {
                                    answer3_2.setText("실내 활동을 좋아해요.");
                                } else if ("6".equals(quest3[1])) {
                                    answer3_2.setText("승부욕이 강한 편이에요.");
                                } else if ("7".equals(quest3[1])) {
                                    answer3_2.setText("책 읽는 걸 좋아해요.");
                                } else if ("8".equals(quest3[1])) {
                                    answer3_2.setText("장난감 또는 인형놀이를 좋아해요.");
                                }

                            } else if (quest3.length == 3) {
                                answer3_1_con.setVisibility(View.VISIBLE);
                                answer3_2_con.setVisibility(View.VISIBLE);
                                answer3_3_con.setVisibility(View.VISIBLE);

                                if ("1".equals(quest3[0])) {
                                    answer3_1.setText("에너지가 넘치는 편이고 활동량이 많아요.");
                                } else if ("2".equals(quest3[0])) {
                                    answer3_1.setText("차분하고 조용한 편이에요.");
                                } else if ("3".equals(quest3[0])) {
                                    answer3_1.setText("한 가지 활동에 집중을 잘해요.");
                                } else if ("4".equals(quest3[0])) {
                                    answer3_1.setText("외부 활동을 좋아해요.");
                                } else if ("5".equals(quest3[0])) {
                                    answer3_1.setText("실내 활동을 좋아해요.");
                                } else if ("6".equals(quest3[0])) {
                                    answer3_1.setText("승부욕이 강한 편이에요.");
                                } else if ("7".equals(quest3[0])) {
                                    answer3_1.setText("책 읽는 걸 좋아해요.");
                                } else if ("8".equals(quest3[0])) {
                                    answer3_1.setText("장난감 또는 인형놀이를 좋아해요.");
                                }

                                if ("1".equals(quest3[1])) {
                                    answer3_2.setText("에너지가 넘치는 편이고 활동량이 많아요.");
                                } else if ("2".equals(quest3[1])) {
                                    answer3_2.setText("차분하고 조용한 편이에요.");
                                } else if ("3".equals(quest3[1])) {
                                    answer3_2.setText("한 가지 활동에 집중을 잘해요.");
                                } else if ("4".equals(quest3[1])) {
                                    answer3_2.setText("외부 활동을 좋아해요.");
                                } else if ("5".equals(quest3[1])) {
                                    answer3_2.setText("실내 활동을 좋아해요.");
                                } else if ("6".equals(quest3[1])) {
                                    answer3_2.setText("승부욕이 강한 편이에요.");
                                } else if ("7".equals(quest3[1])) {
                                    answer3_2.setText("책 읽는 걸 좋아해요.");
                                } else if ("8".equals(quest3[1])) {
                                    answer3_2.setText("장난감 또는 인형놀이를 좋아해요.");
                                }


                                if ("1".equals(quest3[2])) {
                                    answer3_3.setText("에너지가 넘치는 편이고 활동량이 많아요.");
                                } else if ("2".equals(quest3[2])) {
                                    answer3_3.setText("차분하고 조용한 편이에요.");
                                } else if ("3".equals(quest3[2])) {
                                    answer3_3.setText("한 가지 활동에 집중을 잘해요.");
                                } else if ("4".equals(quest3[2])) {
                                    answer3_3.setText("외부 활동을 좋아해요.");
                                } else if ("5".equals(quest3[2])) {
                                    answer3_3.setText("실내 활동을 좋아해요.");
                                } else if ("6".equals(quest3[2])) {
                                    answer3_3.setText("승부욕이 강한 편이에요.");
                                } else if ("7".equals(quest3[2])) {
                                    answer3_3.setText("책 읽는 걸 좋아해요.");
                                } else if ("8".equals(quest3[2])) {
                                    answer3_3.setText("장난감 또는 인형놀이를 좋아해요.");
                                }
                            } else if (quest3.length == 4) {
                                answer3_1_con.setVisibility(View.VISIBLE);
                                answer3_2_con.setVisibility(View.VISIBLE);
                                answer3_3_con.setVisibility(View.VISIBLE);
                                answer3_4_con.setVisibility(View.VISIBLE);

                                if ("1".equals(quest3[0])) {
                                    answer3_1.setText("에너지가 넘치는 편이고 활동량이 많아요.");
                                } else if ("2".equals(quest3[0])) {
                                    answer3_1.setText("차분하고 조용한 편이에요.");
                                } else if ("3".equals(quest3[0])) {
                                    answer3_1.setText("한 가지 활동에 집중을 잘해요.");
                                } else if ("4".equals(quest3[0])) {
                                    answer3_1.setText("외부 활동을 좋아해요.");
                                } else if ("5".equals(quest3[0])) {
                                    answer3_1.setText("실내 활동을 좋아해요.");
                                } else if ("6".equals(quest3[0])) {
                                    answer3_1.setText("승부욕이 강한 편이에요.");
                                } else if ("7".equals(quest3[0])) {
                                    answer3_1.setText("책 읽는 걸 좋아해요.");
                                } else if ("8".equals(quest3[0])) {
                                    answer3_1.setText("장난감 또는 인형놀이를 좋아해요.");
                                }

                                if ("1".equals(quest3[1])) {
                                    answer3_2.setText("에너지가 넘치는 편이고 활동량이 많아요.");
                                } else if ("2".equals(quest3[1])) {
                                    answer3_2.setText("차분하고 조용한 편이에요.");
                                } else if ("3".equals(quest3[1])) {
                                    answer3_2.setText("한 가지 활동에 집중을 잘해요.");
                                } else if ("4".equals(quest3[1])) {
                                    answer3_2.setText("외부 활동을 좋아해요.");
                                } else if ("5".equals(quest3[1])) {
                                    answer3_2.setText("실내 활동을 좋아해요.");
                                } else if ("6".equals(quest3[1])) {
                                    answer3_2.setText("승부욕이 강한 편이에요.");
                                } else if ("7".equals(quest3[1])) {
                                    answer3_2.setText("책 읽는 걸 좋아해요.");
                                } else if ("8".equals(quest3[1])) {
                                    answer3_2.setText("장난감 또는 인형놀이를 좋아해요.");
                                }

                                if ("1".equals(quest3[2])) {
                                    answer3_3.setText("에너지가 넘치는 편이고 활동량이 많아요.");
                                } else if ("2".equals(quest3[2])) {
                                    answer3_3.setText("차분하고 조용한 편이에요.");
                                } else if ("3".equals(quest3[2])) {
                                    answer3_3.setText("한 가지 활동에 집중을 잘해요.");
                                } else if ("4".equals(quest3[2])) {
                                    answer3_3.setText("외부 활동을 좋아해요.");
                                } else if ("5".equals(quest3[2])) {
                                    answer3_3.setText("실내 활동을 좋아해요.");
                                } else if ("6".equals(quest3[2])) {
                                    answer3_3.setText("승부욕이 강한 편이에요.");
                                } else if ("7".equals(quest3[2])) {
                                    answer3_3.setText("책 읽는 걸 좋아해요.");
                                } else if ("8".equals(quest3[2])) {
                                    answer3_3.setText("장난감 또는 인형놀이를 좋아해요.");
                                }

                                if ("1".equals(quest3[3])) {
                                    answer3_4.setText("에너지가 넘치는 편이고 활동량이 많아요.");
                                } else if ("2".equals(quest3[3])) {
                                    answer3_4.setText("차분하고 조용한 편이에요.");
                                } else if ("3".equals(quest3[3])) {
                                    answer3_4.setText("한 가지 활동에 집중을 잘해요.");
                                } else if ("4".equals(quest3[3])) {
                                    answer3_4.setText("외부 활동을 좋아해요.");
                                } else if ("5".equals(quest3[3])) {
                                    answer3_4.setText("실내 활동을 좋아해요.");
                                } else if ("6".equals(quest3[3])) {
                                    answer3_4.setText("승부욕이 강한 편이에요.");
                                } else if ("7".equals(quest3[3])) {
                                    answer3_4.setText("책 읽는 걸 좋아해요.");
                                } else if ("8".equals(quest3[3])) {
                                    answer3_4.setText("장난감 또는 인형놀이를 좋아해요.");
                                }
                            } else if (quest3.length == 5) {
                                answer3_1_con.setVisibility(View.VISIBLE);
                                answer3_2_con.setVisibility(View.VISIBLE);
                                answer3_3_con.setVisibility(View.VISIBLE);
                                answer3_4_con.setVisibility(View.VISIBLE);
                                answer3_5_con.setVisibility(View.VISIBLE);

                                if ("1".equals(quest3[0])) {
                                    answer3_1.setText("에너지가 넘치는 편이고 활동량이 많아요.");
                                } else if ("2".equals(quest3[0])) {
                                    answer3_1.setText("차분하고 조용한 편이에요.");
                                } else if ("3".equals(quest3[0])) {
                                    answer3_1.setText("한 가지 활동에 집중을 잘해요.");
                                } else if ("4".equals(quest3[0])) {
                                    answer3_1.setText("외부 활동을 좋아해요.");
                                } else if ("5".equals(quest3[0])) {
                                    answer3_1.setText("실내 활동을 좋아해요.");
                                } else if ("6".equals(quest3[0])) {
                                    answer3_1.setText("승부욕이 강한 편이에요.");
                                } else if ("7".equals(quest3[0])) {
                                    answer3_1.setText("책 읽는 걸 좋아해요.");
                                } else if ("8".equals(quest3[0])) {
                                    answer3_1.setText("장난감 또는 인형놀이를 좋아해요.");
                                }

                                if ("1".equals(quest3[1])) {
                                    answer3_2.setText("에너지가 넘치는 편이고 활동량이 많아요.");
                                } else if ("2".equals(quest3[1])) {
                                    answer3_2.setText("차분하고 조용한 편이에요.");
                                } else if ("3".equals(quest3[1])) {
                                    answer3_2.setText("한 가지 활동에 집중을 잘해요.");
                                } else if ("4".equals(quest3[1])) {
                                    answer3_2.setText("외부 활동을 좋아해요.");
                                } else if ("5".equals(quest3[1])) {
                                    answer3_2.setText("실내 활동을 좋아해요.");
                                } else if ("6".equals(quest3[1])) {
                                    answer3_2.setText("승부욕이 강한 편이에요.");
                                } else if ("7".equals(quest3[1])) {
                                    answer3_2.setText("책 읽는 걸 좋아해요.");
                                } else if ("8".equals(quest3[1])) {
                                    answer3_2.setText("장난감 또는 인형놀이를 좋아해요.");
                                }

                                if ("1".equals(quest3[2])) {
                                    answer3_3.setText("에너지가 넘치는 편이고 활동량이 많아요.");
                                } else if ("2".equals(quest3[2])) {
                                    answer3_3.setText("차분하고 조용한 편이에요.");
                                } else if ("3".equals(quest3[2])) {
                                    answer3_3.setText("한 가지 활동에 집중을 잘해요.");
                                } else if ("4".equals(quest3[2])) {
                                    answer3_3.setText("외부 활동을 좋아해요.");
                                } else if ("5".equals(quest3[2])) {
                                    answer3_3.setText("실내 활동을 좋아해요.");
                                } else if ("6".equals(quest3[2])) {
                                    answer3_3.setText("승부욕이 강한 편이에요.");
                                } else if ("7".equals(quest3[2])) {
                                    answer3_3.setText("책 읽는 걸 좋아해요.");
                                } else if ("8".equals(quest3[2])) {
                                    answer3_3.setText("장난감 또는 인형놀이를 좋아해요.");
                                }

                                if ("1".equals(quest3[3])) {
                                    answer3_4.setText("에너지가 넘치는 편이고 활동량이 많아요.");
                                } else if ("2".equals(quest3[3])) {
                                    answer3_4.setText("차분하고 조용한 편이에요.");
                                } else if ("3".equals(quest3[3])) {
                                    answer3_4.setText("한 가지 활동에 집중을 잘해요.");
                                } else if ("4".equals(quest3[3])) {
                                    answer3_4.setText("외부 활동을 좋아해요.");
                                } else if ("5".equals(quest3[3])) {
                                    answer3_4.setText("실내 활동을 좋아해요.");
                                } else if ("6".equals(quest3[3])) {
                                    answer3_4.setText("승부욕이 강한 편이에요.");
                                } else if ("7".equals(quest3[3])) {
                                    answer3_4.setText("책 읽는 걸 좋아해요.");
                                } else if ("8".equals(quest3[3])) {
                                    answer3_4.setText("장난감 또는 인형놀이를 좋아해요.");
                                }

                                if ("1".equals(quest3[4])) {
                                    answer3_5.setText("에너지가 넘치는 편이고 활동량이 많아요.");
                                } else if ("2".equals(quest3[4])) {
                                    answer3_5.setText("차분하고 조용한 편이에요.");
                                } else if ("3".equals(quest3[4])) {
                                    answer3_5.setText("한 가지 활동에 집중을 잘해요.");
                                } else if ("4".equals(quest3[4])) {
                                    answer3_5.setText("외부 활동을 좋아해요.");
                                } else if ("5".equals(quest3[4])) {
                                    answer3_5.setText("실내 활동을 좋아해요.");
                                } else if ("6".equals(quest3[4])) {
                                    answer3_5.setText("승부욕이 강한 편이에요.");
                                } else if ("7".equals(quest3[4])) {
                                    answer3_5.setText("책 읽는 걸 좋아해요.");
                                } else if ("8".equals(quest3[4])) {
                                    answer3_5.setText("장난감 또는 인형놀이를 좋아해요.");
                                }
                            } else if (quest3.length == 6) {
                                answer3_1_con.setVisibility(View.VISIBLE);
                                answer3_2_con.setVisibility(View.VISIBLE);
                                answer3_3_con.setVisibility(View.VISIBLE);
                                answer3_4_con.setVisibility(View.VISIBLE);
                                answer3_5_con.setVisibility(View.VISIBLE);
                                answer3_6_con.setVisibility(View.VISIBLE);

                                if ("1".equals(quest3[0])) {
                                    answer3_1.setText("에너지가 넘치는 편이고 활동량이 많아요.");
                                } else if ("2".equals(quest3[0])) {
                                    answer3_1.setText("차분하고 조용한 편이에요.");
                                } else if ("3".equals(quest3[0])) {
                                    answer3_1.setText("한 가지 활동에 집중을 잘해요.");
                                } else if ("4".equals(quest3[0])) {
                                    answer3_1.setText("외부 활동을 좋아해요.");
                                } else if ("5".equals(quest3[0])) {
                                    answer3_1.setText("실내 활동을 좋아해요.");
                                } else if ("6".equals(quest3[0])) {
                                    answer3_1.setText("승부욕이 강한 편이에요.");
                                } else if ("7".equals(quest3[0])) {
                                    answer3_1.setText("책 읽는 걸 좋아해요.");
                                } else if ("8".equals(quest3[0])) {
                                    answer3_1.setText("장난감 또는 인형놀이를 좋아해요.");
                                }

                                if ("1".equals(quest3[1])) {
                                    answer3_2.setText("에너지가 넘치는 편이고 활동량이 많아요.");
                                } else if ("2".equals(quest3[1])) {
                                    answer3_2.setText("차분하고 조용한 편이에요.");
                                } else if ("3".equals(quest3[1])) {
                                    answer3_2.setText("한 가지 활동에 집중을 잘해요.");
                                } else if ("4".equals(quest3[1])) {
                                    answer3_2.setText("외부 활동을 좋아해요.");
                                } else if ("5".equals(quest3[1])) {
                                    answer3_2.setText("실내 활동을 좋아해요.");
                                } else if ("6".equals(quest3[1])) {
                                    answer3_2.setText("승부욕이 강한 편이에요.");
                                } else if ("7".equals(quest3[1])) {
                                    answer3_2.setText("책 읽는 걸 좋아해요.");
                                } else if ("8".equals(quest3[1])) {
                                    answer3_2.setText("장난감 또는 인형놀이를 좋아해요.");
                                }

                                if ("1".equals(quest3[2])) {
                                    answer3_3.setText("에너지가 넘치는 편이고 활동량이 많아요.");
                                } else if ("2".equals(quest3[2])) {
                                    answer3_3.setText("차분하고 조용한 편이에요.");
                                } else if ("3".equals(quest3[2])) {
                                    answer3_3.setText("한 가지 활동에 집중을 잘해요.");
                                } else if ("4".equals(quest3[2])) {
                                    answer3_3.setText("외부 활동을 좋아해요.");
                                } else if ("5".equals(quest3[2])) {
                                    answer3_3.setText("실내 활동을 좋아해요.");
                                } else if ("6".equals(quest3[2])) {
                                    answer3_3.setText("승부욕이 강한 편이에요.");
                                } else if ("7".equals(quest3[2])) {
                                    answer3_3.setText("책 읽는 걸 좋아해요.");
                                } else if ("8".equals(quest3[2])) {
                                    answer3_3.setText("장난감 또는 인형놀이를 좋아해요.");
                                }

                                if ("1".equals(quest3[3])) {
                                    answer3_4.setText("에너지가 넘치는 편이고 활동량이 많아요.");
                                } else if ("2".equals(quest3[3])) {
                                    answer3_4.setText("차분하고 조용한 편이에요.");
                                } else if ("3".equals(quest3[3])) {
                                    answer3_4.setText("한 가지 활동에 집중을 잘해요.");
                                } else if ("4".equals(quest3[3])) {
                                    answer3_4.setText("외부 활동을 좋아해요.");
                                } else if ("5".equals(quest3[3])) {
                                    answer3_4.setText("실내 활동을 좋아해요.");
                                } else if ("6".equals(quest3[3])) {
                                    answer3_4.setText("승부욕이 강한 편이에요.");
                                } else if ("7".equals(quest3[3])) {
                                    answer3_4.setText("책 읽는 걸 좋아해요.");
                                } else if ("8".equals(quest3[3])) {
                                    answer3_4.setText("장난감 또는 인형놀이를 좋아해요.");
                                }

                                if ("1".equals(quest3[4])) {
                                    answer3_5.setText("에너지가 넘치는 편이고 활동량이 많아요.");
                                } else if ("2".equals(quest3[4])) {
                                    answer3_5.setText("차분하고 조용한 편이에요.");
                                } else if ("3".equals(quest3[4])) {
                                    answer3_5.setText("한 가지 활동에 집중을 잘해요.");
                                } else if ("4".equals(quest3[4])) {
                                    answer3_5.setText("외부 활동을 좋아해요.");
                                } else if ("5".equals(quest3[4])) {
                                    answer3_5.setText("실내 활동을 좋아해요.");
                                } else if ("6".equals(quest3[4])) {
                                    answer3_5.setText("승부욕이 강한 편이에요.");
                                } else if ("7".equals(quest3[4])) {
                                    answer3_5.setText("책 읽는 걸 좋아해요.");
                                } else if ("8".equals(quest3[4])) {
                                    answer3_5.setText("장난감 또는 인형놀이를 좋아해요.");
                                }

                                if ("1".equals(quest3[5])) {
                                    answer3_6.setText("에너지가 넘치는 편이고 활동량이 많아요.");
                                } else if ("2".equals(quest3[5])) {
                                    answer3_6.setText("차분하고 조용한 편이에요.");
                                } else if ("3".equals(quest3[5])) {
                                    answer3_6.setText("한 가지 활동에 집중을 잘해요.");
                                } else if ("4".equals(quest3[5])) {
                                    answer3_6.setText("외부 활동을 좋아해요.");
                                } else if ("5".equals(quest3[5])) {
                                    answer3_6.setText("실내 활동을 좋아해요.");
                                } else if ("6".equals(quest3[5])) {
                                    answer3_6.setText("승부욕이 강한 편이에요.");
                                } else if ("7".equals(quest3[5])) {
                                    answer3_6.setText("책 읽는 걸 좋아해요.");
                                } else if ("8".equals(quest3[5])) {
                                    answer3_6.setText("장난감 또는 인형놀이를 좋아해요.");
                                }
                            } else if (quest3.length == 7) {
                                answer3_1_con.setVisibility(View.VISIBLE);
                                answer3_2_con.setVisibility(View.VISIBLE);
                                answer3_3_con.setVisibility(View.VISIBLE);
                                answer3_4_con.setVisibility(View.VISIBLE);
                                answer3_5_con.setVisibility(View.VISIBLE);
                                answer3_7_con.setVisibility(View.VISIBLE);

                                if ("1".equals(quest3[0])) {
                                    answer3_1.setText("에너지가 넘치는 편이고 활동량이 많아요.");
                                } else if ("2".equals(quest3[0])) {
                                    answer3_1.setText("차분하고 조용한 편이에요.");
                                } else if ("3".equals(quest3[0])) {
                                    answer3_1.setText("한 가지 활동에 집중을 잘해요.");
                                } else if ("4".equals(quest3[0])) {
                                    answer3_1.setText("외부 활동을 좋아해요.");
                                } else if ("5".equals(quest3[0])) {
                                    answer3_1.setText("실내 활동을 좋아해요.");
                                } else if ("6".equals(quest3[0])) {
                                    answer3_1.setText("승부욕이 강한 편이에요.");
                                } else if ("7".equals(quest3[0])) {
                                    answer3_1.setText("책 읽는 걸 좋아해요.");
                                } else if ("8".equals(quest3[0])) {
                                    answer3_1.setText("장난감 또는 인형놀이를 좋아해요.");
                                }

                                if ("1".equals(quest3[1])) {
                                    answer3_2.setText("에너지가 넘치는 편이고 활동량이 많아요.");
                                } else if ("2".equals(quest3[1])) {
                                    answer3_2.setText("차분하고 조용한 편이에요.");
                                } else if ("3".equals(quest3[1])) {
                                    answer3_2.setText("한 가지 활동에 집중을 잘해요.");
                                } else if ("4".equals(quest3[1])) {
                                    answer3_2.setText("외부 활동을 좋아해요.");
                                } else if ("5".equals(quest3[1])) {
                                    answer3_2.setText("실내 활동을 좋아해요.");
                                } else if ("6".equals(quest3[1])) {
                                    answer3_2.setText("승부욕이 강한 편이에요.");
                                } else if ("7".equals(quest3[1])) {
                                    answer3_2.setText("책 읽는 걸 좋아해요.");
                                } else if ("8".equals(quest3[1])) {
                                    answer3_2.setText("장난감 또는 인형놀이를 좋아해요.");
                                }

                                if ("1".equals(quest3[2])) {
                                    answer3_3.setText("에너지가 넘치는 편이고 활동량이 많아요.");
                                } else if ("2".equals(quest3[2])) {
                                    answer3_3.setText("차분하고 조용한 편이에요.");
                                } else if ("3".equals(quest3[2])) {
                                    answer3_3.setText("한 가지 활동에 집중을 잘해요.");
                                } else if ("4".equals(quest3[2])) {
                                    answer3_3.setText("외부 활동을 좋아해요.");
                                } else if ("5".equals(quest3[2])) {
                                    answer3_3.setText("실내 활동을 좋아해요.");
                                } else if ("6".equals(quest3[2])) {
                                    answer3_3.setText("승부욕이 강한 편이에요.");
                                } else if ("7".equals(quest3[2])) {
                                    answer3_3.setText("책 읽는 걸 좋아해요.");
                                } else if ("8".equals(quest3[2])) {
                                    answer3_3.setText("장난감 또는 인형놀이를 좋아해요.");
                                }

                                if ("1".equals(quest3[3])) {
                                    answer3_4.setText("에너지가 넘치는 편이고 활동량이 많아요.");
                                } else if ("2".equals(quest3[3])) {
                                    answer3_4.setText("차분하고 조용한 편이에요.");
                                } else if ("3".equals(quest3[3])) {
                                    answer3_4.setText("한 가지 활동에 집중을 잘해요.");
                                } else if ("4".equals(quest3[3])) {
                                    answer3_4.setText("외부 활동을 좋아해요.");
                                } else if ("5".equals(quest3[3])) {
                                    answer3_4.setText("실내 활동을 좋아해요.");
                                } else if ("6".equals(quest3[3])) {
                                    answer3_4.setText("승부욕이 강한 편이에요.");
                                } else if ("7".equals(quest3[3])) {
                                    answer3_4.setText("책 읽는 걸 좋아해요.");
                                } else if ("8".equals(quest3[3])) {
                                    answer3_4.setText("장난감 또는 인형놀이를 좋아해요.");
                                }

                                if ("1".equals(quest3[4])) {
                                    answer3_5.setText("에너지가 넘치는 편이고 활동량이 많아요.");
                                } else if ("2".equals(quest3[4])) {
                                    answer3_5.setText("차분하고 조용한 편이에요.");
                                } else if ("3".equals(quest3[4])) {
                                    answer3_5.setText("한 가지 활동에 집중을 잘해요.");
                                } else if ("4".equals(quest3[4])) {
                                    answer3_5.setText("외부 활동을 좋아해요.");
                                } else if ("5".equals(quest3[4])) {
                                    answer3_5.setText("실내 활동을 좋아해요.");
                                } else if ("6".equals(quest3[4])) {
                                    answer3_5.setText("승부욕이 강한 편이에요.");
                                } else if ("7".equals(quest3[4])) {
                                    answer3_5.setText("책 읽는 걸 좋아해요.");
                                } else if ("8".equals(quest3[4])) {
                                    answer3_5.setText("장난감 또는 인형놀이를 좋아해요.");
                                }

                                if ("1".equals(quest3[5])) {
                                    answer3_6.setText("에너지가 넘치는 편이고 활동량이 많아요.");
                                } else if ("2".equals(quest3[5])) {
                                    answer3_6.setText("차분하고 조용한 편이에요.");
                                } else if ("3".equals(quest3[5])) {
                                    answer3_6.setText("한 가지 활동에 집중을 잘해요.");
                                } else if ("4".equals(quest3[5])) {
                                    answer3_6.setText("외부 활동을 좋아해요.");
                                } else if ("5".equals(quest3[5])) {
                                    answer3_6.setText("실내 활동을 좋아해요.");
                                } else if ("6".equals(quest3[5])) {
                                    answer3_6.setText("승부욕이 강한 편이에요.");
                                } else if ("7".equals(quest3[5])) {
                                    answer3_6.setText("책 읽는 걸 좋아해요.");
                                } else if ("8".equals(quest3[5])) {
                                    answer3_6.setText("장난감 또는 인형놀이를 좋아해요.");
                                }

                                if ("1".equals(quest3[6])) {
                                    answer3_7.setText("에너지가 넘치는 편이고 활동량이 많아요.");
                                } else if ("2".equals(quest3[6])) {
                                    answer3_7.setText("차분하고 조용한 편이에요.");
                                } else if ("3".equals(quest3[6])) {
                                    answer3_7.setText("한 가지 활동에 집중을 잘해요.");
                                } else if ("4".equals(quest3[6])) {
                                    answer3_7.setText("외부 활동을 좋아해요.");
                                } else if ("5".equals(quest3[6])) {
                                    answer3_7.setText("실내 활동을 좋아해요.");
                                } else if ("6".equals(quest3[6])) {
                                    answer3_7.setText("승부욕이 강한 편이에요.");
                                } else if ("7".equals(quest3[6])) {
                                    answer3_7.setText("책 읽는 걸 좋아해요.");
                                } else if ("8".equals(quest3[6])) {
                                    answer3_7.setText("장난감 또는 인형놀이를 좋아해요.");
                                }
                            } else if (quest3.length == 8) {
                                answer3_1_con.setVisibility(View.VISIBLE);
                                answer3_2_con.setVisibility(View.VISIBLE);
                                answer3_3_con.setVisibility(View.VISIBLE);
                                answer3_4_con.setVisibility(View.VISIBLE);
                                answer3_5_con.setVisibility(View.VISIBLE);
                                answer3_7_con.setVisibility(View.VISIBLE);
                                answer3_8_con.setVisibility(View.VISIBLE);

                                if ("1".equals(quest3[0])) {
                                    answer3_1.setText("에너지가 넘치는 편이고 활동량이 많아요.");
                                } else if ("2".equals(quest3[0])) {
                                    answer3_1.setText("차분하고 조용한 편이에요.");
                                } else if ("3".equals(quest3[0])) {
                                    answer3_1.setText("한 가지 활동에 집중을 잘해요.");
                                } else if ("4".equals(quest3[0])) {
                                    answer3_1.setText("외부 활동을 좋아해요.");
                                } else if ("5".equals(quest3[0])) {
                                    answer3_1.setText("실내 활동을 좋아해요.");
                                } else if ("6".equals(quest3[0])) {
                                    answer3_1.setText("승부욕이 강한 편이에요.");
                                } else if ("7".equals(quest3[0])) {
                                    answer3_1.setText("책 읽는 걸 좋아해요.");
                                } else if ("8".equals(quest3[0])) {
                                    answer3_1.setText("장난감 또는 인형놀이를 좋아해요.");
                                }

                                if ("1".equals(quest3[1])) {
                                    answer3_2.setText("에너지가 넘치는 편이고 활동량이 많아요.");
                                } else if ("2".equals(quest3[1])) {
                                    answer3_2.setText("차분하고 조용한 편이에요.");
                                } else if ("3".equals(quest3[1])) {
                                    answer3_2.setText("한 가지 활동에 집중을 잘해요.");
                                } else if ("4".equals(quest3[1])) {
                                    answer3_2.setText("외부 활동을 좋아해요.");
                                } else if ("5".equals(quest3[1])) {
                                    answer3_2.setText("실내 활동을 좋아해요.");
                                } else if ("6".equals(quest3[1])) {
                                    answer3_2.setText("승부욕이 강한 편이에요.");
                                } else if ("7".equals(quest3[1])) {
                                    answer3_2.setText("책 읽는 걸 좋아해요.");
                                } else if ("8".equals(quest3[1])) {
                                    answer3_2.setText("장난감 또는 인형놀이를 좋아해요.");
                                }

                                if ("1".equals(quest3[2])) {
                                    answer3_3.setText("에너지가 넘치는 편이고 활동량이 많아요.");
                                } else if ("2".equals(quest3[2])) {
                                    answer3_3.setText("차분하고 조용한 편이에요.");
                                } else if ("3".equals(quest3[2])) {
                                    answer3_3.setText("한 가지 활동에 집중을 잘해요.");
                                } else if ("4".equals(quest3[2])) {
                                    answer3_3.setText("외부 활동을 좋아해요.");
                                } else if ("5".equals(quest3[2])) {
                                    answer3_3.setText("실내 활동을 좋아해요.");
                                } else if ("6".equals(quest3[2])) {
                                    answer3_3.setText("승부욕이 강한 편이에요.");
                                } else if ("7".equals(quest3[2])) {
                                    answer3_3.setText("책 읽는 걸 좋아해요.");
                                } else if ("8".equals(quest3[2])) {
                                    answer3_3.setText("장난감 또는 인형놀이를 좋아해요.");
                                }

                                if ("1".equals(quest3[3])) {
                                    answer3_4.setText("에너지가 넘치는 편이고 활동량이 많아요.");
                                } else if ("2".equals(quest3[3])) {
                                    answer3_4.setText("차분하고 조용한 편이에요.");
                                } else if ("3".equals(quest3[3])) {
                                    answer3_4.setText("한 가지 활동에 집중을 잘해요.");
                                } else if ("4".equals(quest3[3])) {
                                    answer3_4.setText("외부 활동을 좋아해요.");
                                } else if ("5".equals(quest3[3])) {
                                    answer3_4.setText("실내 활동을 좋아해요.");
                                } else if ("6".equals(quest3[3])) {
                                    answer3_4.setText("승부욕이 강한 편이에요.");
                                } else if ("7".equals(quest3[3])) {
                                    answer3_4.setText("책 읽는 걸 좋아해요.");
                                } else if ("8".equals(quest3[3])) {
                                    answer3_4.setText("장난감 또는 인형놀이를 좋아해요.");
                                }

                                if ("1".equals(quest3[4])) {
                                    answer3_5.setText("에너지가 넘치는 편이고 활동량이 많아요.");
                                } else if ("2".equals(quest3[4])) {
                                    answer3_5.setText("차분하고 조용한 편이에요.");
                                } else if ("3".equals(quest3[4])) {
                                    answer3_5.setText("한 가지 활동에 집중을 잘해요.");
                                } else if ("4".equals(quest3[4])) {
                                    answer3_5.setText("외부 활동을 좋아해요.");
                                } else if ("5".equals(quest3[4])) {
                                    answer3_5.setText("실내 활동을 좋아해요.");
                                } else if ("6".equals(quest3[4])) {
                                    answer3_5.setText("승부욕이 강한 편이에요.");
                                } else if ("7".equals(quest3[4])) {
                                    answer3_5.setText("책 읽는 걸 좋아해요.");
                                } else if ("8".equals(quest3[4])) {
                                    answer3_5.setText("장난감 또는 인형놀이를 좋아해요.");
                                }

                                if ("1".equals(quest3[5])) {
                                    answer3_6.setText("에너지가 넘치는 편이고 활동량이 많아요.");
                                } else if ("2".equals(quest3[5])) {
                                    answer3_6.setText("차분하고 조용한 편이에요.");
                                } else if ("3".equals(quest3[5])) {
                                    answer3_6.setText("한 가지 활동에 집중을 잘해요.");
                                } else if ("4".equals(quest3[5])) {
                                    answer3_6.setText("외부 활동을 좋아해요.");
                                } else if ("5".equals(quest3[5])) {
                                    answer3_6.setText("실내 활동을 좋아해요.");
                                } else if ("6".equals(quest3[5])) {
                                    answer3_6.setText("승부욕이 강한 편이에요.");
                                } else if ("7".equals(quest3[5])) {
                                    answer3_6.setText("책 읽는 걸 좋아해요.");
                                } else if ("8".equals(quest3[5])) {
                                    answer3_6.setText("장난감 또는 인형놀이를 좋아해요.");
                                }

                                if ("1".equals(quest3[6])) {
                                    answer3_7.setText("에너지가 넘치는 편이고 활동량이 많아요.");
                                } else if ("2".equals(quest3[6])) {
                                    answer3_7.setText("차분하고 조용한 편이에요.");
                                } else if ("3".equals(quest3[6])) {
                                    answer3_7.setText("한 가지 활동에 집중을 잘해요.");
                                } else if ("4".equals(quest3[6])) {
                                    answer3_7.setText("외부 활동을 좋아해요.");
                                } else if ("5".equals(quest3[6])) {
                                    answer3_7.setText("실내 활동을 좋아해요.");
                                } else if ("6".equals(quest3[6])) {
                                    answer3_7.setText("승부욕이 강한 편이에요.");
                                } else if ("7".equals(quest3[6])) {
                                    answer3_7.setText("책 읽는 걸 좋아해요.");
                                } else if ("8".equals(quest3[6])) {
                                    answer3_7.setText("장난감 또는 인형놀이를 좋아해요.");
                                }

                                if ("1".equals(quest3[7])) {
                                    answer3_8.setText("에너지가 넘치는 편이고 활동량이 많아요.");
                                } else if ("2".equals(quest3[7])) {
                                    answer3_8.setText("차분하고 조용한 편이에요.");
                                } else if ("3".equals(quest3[7])) {
                                    answer3_8.setText("한 가지 활동에 집중을 잘해요.");
                                } else if ("4".equals(quest3[7])) {
                                    answer3_8.setText("외부 활동을 좋아해요.");
                                } else if ("5".equals(quest3[7])) {
                                    answer3_8.setText("실내 활동을 좋아해요.");
                                } else if ("6".equals(quest3[7])) {
                                    answer3_8.setText("승부욕이 강한 편이에요.");
                                } else if ("7".equals(quest3[7])) {
                                    answer3_8.setText("책 읽는 걸 좋아해요.");
                                } else if ("8".equals(quest3[7])) {
                                    answer3_8.setText("장난감 또는 인형놀이를 좋아해요.");
                                }
                            }
                        }

                        String[] ques4 = jsonData.getString("quest4").split(",");
                        if (ques4.length == 0 || "null".equals(jsonData.getString("quest4"))) {
                            answer4_con.setVisibility(View.GONE);
                        } else {
                            answer4_con.setVisibility(View.VISIBLE);

                            if (ques4.length == 1) {
                                answer4_1_con.setVisibility(View.VISIBLE);
                                answer4_2_con.setVisibility(View.GONE);
                                answer4_3_con.setVisibility(View.GONE);
                                answer4_4_con.setVisibility(View.GONE);

                                if ("1".equals(ques4[0])) {
                                    answer4_1.setText("아빠를 자주 찾아요.");
                                } else if ("2".equals(ques4[0])) {
                                    answer4_1.setText("엄마를 자주 찾아요.");
                                } else if ("3".equals(ques4[0])) {
                                    answer4_1.setText("형제, 자매와 잘 놀아요.");
                                } else if ("4".equals(ques4[0])) {
                                    answer4_1.setText("형제, 자매와 종종 다투는 편이에요.");
                                }
                            } else if (ques4.length == 2) {
                                answer4_1_con.setVisibility(View.VISIBLE);
                                answer4_2_con.setVisibility(View.VISIBLE);
                                answer4_3_con.setVisibility(View.GONE);
                                answer4_4_con.setVisibility(View.GONE);

                                if ("1".equals(ques4[0])) {
                                    answer4_1.setText("아빠를 자주 찾아요.");
                                } else if ("2".equals(ques4[0])) {
                                    answer4_1.setText("엄마를 자주 찾아요.");
                                } else if ("3".equals(ques4[0])) {
                                    answer4_1.setText("형제, 자매와 잘 놀아요.");
                                } else if ("4".equals(ques4[0])) {
                                    answer4_1.setText("형제, 자매와 종종 다투는 편이에요.");
                                }

                                if ("1".equals(ques4[1])) {
                                    answer4_2.setText("아빠를 자주 찾아요.");
                                } else if ("2".equals(ques4[1])) {
                                    answer4_2.setText("엄마를 자주 찾아요.");
                                } else if ("3".equals(ques4[1])) {
                                    answer4_2.setText("형제, 자매와 잘 놀아요.");
                                } else if ("4".equals(ques4[1])) {
                                    answer4_2.setText("형제, 자매와 종종 다투는 편이에요.");
                                }

                            } else if (ques4.length == 3) {
                                answer4_1_con.setVisibility(View.VISIBLE);
                                answer4_2_con.setVisibility(View.VISIBLE);
                                answer4_3_con.setVisibility(View.VISIBLE);
                                answer4_4_con.setVisibility(View.GONE);
                                if ("1".equals(ques4[0])) {
                                    answer4_1.setText("아빠를 자주 찾아요.");
                                } else if ("2".equals(ques4[0])) {
                                    answer4_1.setText("엄마를 자주 찾아요.");
                                } else if ("3".equals(ques4[0])) {
                                    answer4_1.setText("형제, 자매와 잘 놀아요.");
                                } else if ("4".equals(ques4[0])) {
                                    answer4_1.setText("형제, 자매와 종종 다투는 편이에요.");
                                }

                                if ("1".equals(ques4[1])) {
                                    answer4_2.setText("아빠를 자주 찾아요.");
                                } else if ("2".equals(ques4[1])) {
                                    answer4_2.setText("엄마를 자주 찾아요.");
                                } else if ("3".equals(ques4[1])) {
                                    answer4_2.setText("형제, 자매와 잘 놀아요.");
                                } else if ("4".equals(ques4[1])) {
                                    answer4_2.setText("형제, 자매와 종종 다투는 편이에요.");
                                }


                                if ("1".equals(ques4[2])) {
                                    answer4_3.setText("아빠를 자주 찾아요.");
                                } else if ("2".equals(ques4[2])) {
                                    answer4_3.setText("엄마를 자주 찾아요.");
                                } else if ("3".equals(ques4[2])) {
                                    answer4_3.setText("형제, 자매와 잘 놀아요.");
                                } else if ("4".equals(ques4[2])) {
                                    answer4_3.setText("형제, 자매와 종종 다투는 편이에요.");
                                }
                            } else if (ques4.length == 4) {
                                answer4_1_con.setVisibility(View.VISIBLE);
                                answer4_2_con.setVisibility(View.VISIBLE);
                                answer4_3_con.setVisibility(View.VISIBLE);
                                answer4_4_con.setVisibility(View.VISIBLE);

                                if ("1".equals(ques4[0])) {
                                    answer4_1.setText("아빠를 자주 찾아요.");
                                } else if ("2".equals(ques4[0])) {
                                    answer4_1.setText("엄마를 자주 찾아요.");
                                } else if ("3".equals(ques4[0])) {
                                    answer4_1.setText("형제, 자매와 잘 놀아요.");
                                } else if ("4".equals(ques4[0])) {
                                    answer4_1.setText("형제, 자매와 종종 다투는 편이에요.");
                                }

                                if ("1".equals(ques4[1])) {
                                    answer4_2.setText("아빠를 자주 찾아요.");
                                } else if ("2".equals(ques4[1])) {
                                    answer4_2.setText("엄마를 자주 찾아요.");
                                } else if ("3".equals(ques4[1])) {
                                    answer4_2.setText("형제, 자매와 잘 놀아요.");
                                } else if ("4".equals(ques4[1])) {
                                    answer4_2.setText("형제, 자매와 종종 다투는 편이에요.");
                                }

                                if ("1".equals(ques4[2])) {
                                    answer4_3.setText("아빠를 자주 찾아요.");
                                } else if ("2".equals(ques4[2])) {
                                    answer4_3.setText("엄마를 자주 찾아요.");
                                } else if ("3".equals(ques4[2])) {
                                    answer4_3.setText("형제, 자매와 잘 놀아요.");
                                } else if ("4".equals(ques4[2])) {
                                    answer4_3.setText("형제, 자매와 종종 다투는 편이에요.");
                                }

                                if ("1".equals(ques4[3])) {
                                    answer4_4.setText("아빠를 자주 찾아요.");
                                } else if ("2".equals(ques4[3])) {
                                    answer4_4.setText("엄마를 자주 찾아요.");
                                } else if ("3".equals(ques4[3])) {
                                    answer4_4.setText("형제, 자매와 잘 놀아요.");
                                } else if ("4".equals(ques4[3])) {
                                    answer4_4.setText("형제, 자매와 종종 다투는 편이에요.");
                                }
                            }
                        }

                        careful.setText(jsonData.getString("quest5"));
                        etc.setText(jsonData.getString("quest6"));

                    } else if (!jsonObject.getBoolean("return")) {
                        if("login".equals(jsonObject.getString("type"))){
                            Toast.makeText(IntoChildProfileActivity.this, "로그인 해주세요 ! ", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(IntoChildProfileActivity.this, LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            oneBtnDialog = new OneBtnDialog(IntoChildProfileActivity.this, "정보를 불러올 수 없습니다 !", "확인");
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
}
