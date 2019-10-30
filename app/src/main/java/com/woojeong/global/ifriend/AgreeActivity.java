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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.woojeong.global.ifriend.DTO.Career;
import com.woojeong.global.ifriend.DTO.One;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.woojeong.global.ifriend.GlobalApplication.setDarkMode;

public class AgreeActivity extends AppCompatActivity {
    private static String TAG = "AgreeActivity";
    Context context;
    InputMethodManager ipmm;
    AQuery aQuery = null;
    OneBtnDialog oneBtnDialog;
    CompleteDialog completeDialog;

    FrameLayout back_con, submission_con;
    ImageView back;
    ScrollView scrollView;
    LinearLayout check_con;
    CheckBox check;
    TextView check_txt, submission;

    Intent resume;
    String nameK, nameE, gender, birth, phone, mail, work, work_detail, childCount, parenting, isactive, childdetail, home, familydetail, month, optiondetail, edu, know, motive, edutype, exp, pay1, pay2, pay3, knowdetail;
    String[] children1 = {"", ""};
    String[] children2 = {"", ""};
    String[] children3 = {"", ""};
    String[] children4 = {"", ""};
    ArrayList<String> special = null, service = null;
    String[]  specialArray, serviceArray;
    ArrayList<One> licenseData = null;
    ArrayList<Career> careerData = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agree);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#ffffff"));
            window.setBackgroundDrawable(null);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                setDarkMode(AgreeActivity.this, true);
            }
        }

        licenseData = new ArrayList<One>();
        careerData = new ArrayList<Career>();
        special = new ArrayList<String>();
        service = new ArrayList<String>();
        specialArray = special.toArray(new String[special.size()]);
        serviceArray = service.toArray(new String[service.size()]);

        resume = getIntent();
        nameK = resume.getStringExtra("nameK");
        nameE = resume.getStringExtra("nameE");
        gender = resume.getStringExtra("gender");
        birth = resume.getStringExtra("birth");
        phone = resume.getStringExtra("phone");
        mail = resume.getStringExtra("mail");
        work = resume.getStringExtra("work");
        work_detail = resume.getStringExtra("work_detail");
        licenseData = (ArrayList<One>) resume.getSerializableExtra("cert[]");
        careerData = (ArrayList<Career>) resume.getSerializableExtra("office[]");
        isactive = resume.getStringExtra("isactive");
        childCount = resume.getStringExtra("childcnt");
        if(null != resume.getStringArrayExtra("child[1]")){
            children1 = resume.getStringArrayExtra("child[1]");
        }
        if(null != resume.getStringArrayExtra("child[2]")){
            children2 = resume.getStringArrayExtra("child[2]");
        }
        if(null != resume.getStringArrayExtra("child[3]")){
            children3 = resume.getStringArrayExtra("child[3]");
        }
        if(null != resume.getStringArrayExtra("child[4]")){
            children4 = resume.getStringArrayExtra("child[4]");
        }
        childdetail = resume.getStringExtra("childdetail");
        parenting = resume.getStringExtra("career");
        home = resume.getStringExtra("home");
        special = (ArrayList<String>) resume.getSerializableExtra("special[]");
        familydetail = resume.getStringExtra("familydetail");
        month = resume.getStringExtra("month");
        service = (ArrayList<String>) resume.getSerializableExtra("option[]");
        optiondetail = resume.getStringExtra("optiondetail");
        edu = resume.getStringExtra("edu");
        motive = resume.getStringExtra("motive");
        edutype = resume.getStringExtra("edutype");
        exp = resume.getStringExtra("exp");
        pay1 = resume.getStringExtra("pay1");
        pay2 = resume.getStringExtra("pay2");
        pay3 = resume.getStringExtra("pay3");
        know = resume.getStringExtra("know");
        knowdetail = resume.getStringExtra("knowdetail");

        Log.i(TAG, " child1 " + children1.toString());

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

        submission_con = findViewById(R.id.submission_con);
        scrollView = findViewById(R.id.scrollView);
        scrollView.postDelayed(new Runnable() {
            @Override
            public void run() {
                scrollView.scrollTo(0, 0);
            }
        }, 700);
        check_con = findViewById(R.id.check_con);
        check = findViewById(R.id.check);
        check_txt = findViewById(R.id.check_txt);
        submission = findViewById(R.id.submission);

        check_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check.callOnClick();
            }
        });

        submission_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submission.callOnClick();
            }
        });
        submission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!check.isChecked()){
                    oneBtnDialog = new OneBtnDialog(AgreeActivity.this, "다음 내용에 관해\n동의해주세요 !", "확인");
                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    oneBtnDialog.setCancelable(false);
                    oneBtnDialog.show();
                    return;
                } else {
                    SharedPreferences get_token = getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
                    final String getToken = get_token.getString("Token", "");
                    String url = ServerUrl.getBaseUrl() + "/friend/join";
                    final Map<String, Object> params = new HashMap<String, Object>();
                    params.put("nameK", nameK);
                    params.put("nameE", nameE);
                    params.put("gender", gender);
                    params.put("birth", birth);
                    params.put("phone", phone);
                    params.put("mail", mail);
                    params.put("work", work);
                    params.put("workdetail", work_detail);
                    if(licenseData.size()>0 && licenseData!=null){
                        for(int i=0; i<licenseData.size(); i++){
                            params.put("cert["+i+"]", licenseData.get(i).getText1());
                        }
                    }
                    if(careerData.size()>0 && careerData!=null){
                        for(int i=0; i<careerData.size(); i++){
                            params.put("office["+i+"]", careerData.get(i).getTitle() +"|"+ careerData.get(i).getStartDate() +"|"+ careerData.get(i).getEndDate());
                        }
                    }
                    params.put("isactive", isactive);
                    params.put("childcnt", childCount);
                    if(null != resume.getStringArrayExtra("child[1]")){
                        params.put("child[1]", children1[0] +"|"+ children1[1]);
                    }
                    if(null != resume.getStringArrayExtra("child[2]")){
                        params.put("child[2]", children2[0]  +"|"+children2[1]);
                    }
                    if(null != resume.getStringArrayExtra("child[3]")){
                        params.put("child[3]", children3[0]  +"|"+ children3[1]);
                    }
                    if(null != resume.getStringArrayExtra("child[4]")){
                        params.put("child[4]", children4[0]  +"|"+ children4[1]);
                    }
                    params.put("childdetail", childdetail);
                    params.put("career", parenting);
                    params.put("home", home);
                    for(int i=0; i<special.size(); i++){
                        params.put("special["+i+"]", special.get(i));
                    }
                    params.put("month", month);
                    for(int i=0; i<service.size(); i++){
                        params.put("option["+i+"]", service.get(i));
                    }
                    params.put("optiondetail", optiondetail);
                    params.put("edu", edu);
                    params.put("motive", motive);
                    params.put("edutype", edutype);
                    params.put("exp", exp);
                    params.put("pay1", pay1);
                    params.put("pay2", pay2);
                    params.put("pay3", pay3);
                    params.put("know", know);
                    params.put("knowdetail", month);

                    Log.i(TAG, " " + params);

                    aQuery.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
                        @Override
                        public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
                            Log.i(TAG, " " + jsonObject );
                            try {
                                if (jsonObject.getBoolean("return")) {    //return이 true 면?

                                    completeDialog = new CompleteDialog(AgreeActivity.this, "제출 완료\n1차 서류 심사 합격 여부는\n합격자에 한해서\n등록하신 연락처로\n개별 연락 드립니다.", "확인");
                                    completeDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                    completeDialog.setCancelable(false);
                                    completeDialog.show();

                                } else if (!jsonObject.getBoolean("return")) {
                                    if("login".equals(jsonObject.getString("type"))){
                                        Toast.makeText(AgreeActivity.this, "로그인 해주세요 ! ", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(AgreeActivity.this, LoginActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        oneBtnDialog = new OneBtnDialog(AgreeActivity.this, "다시 시도해주세요 !", "확인");
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

    public class CompleteDialog extends Dialog {
        CompleteDialog completeDialog = this;
        Context context;

        public CompleteDialog(final Context context, final String text, final String btnText) {
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
                    completeDialog.dismiss();
                    finish();
                }
            });
        }
    }
}
