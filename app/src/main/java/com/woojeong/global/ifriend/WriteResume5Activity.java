package com.woojeong.global.ifriend;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.woojeong.global.ifriend.DTO.Career;
import com.woojeong.global.ifriend.DTO.One;

import java.util.ArrayList;

import static com.woojeong.global.ifriend.GlobalApplication.setDarkMode;

public class WriteResume5Activity extends AppCompatActivity {
    private static String TAG = "WriteResume5Activity";
    Context context;
    InputMethodManager ipmm;
    AQuery aQuery = null;
    OneBtnDialog oneBtnDialog;

    FrameLayout back_con, next_con;
    ImageView back;
    ScrollView scrollView;
    EditText motive_edit, pursue_edit, experience_edit, weekday_pay, weekend_pay, month_pay, etc_check_edit;
    RadioGroup radio_group;
    RadioButton radio1, radio2, radio3, radio4;
    TextView next;

    Intent resume;
    String nameK, nameE, gender, birth, phone, mail, work, work_detail, childCount, parenting, isactive, childdetail, home, familydetail, month, optiondetail, edu, know;
    String[] children1 = {"", ""};
    String[] children2 = {"", ""};
    String[] children3 = {"", ""};
    String[] children4 = {"", ""};
    ArrayList<String> special = null, service = null;
    ArrayList<One> licenseData = null;
    ArrayList<Career> careerData = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_resume5);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#ffffff"));
            window.setBackgroundDrawable(null);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                setDarkMode(WriteResume5Activity.this, true);
            }
        }

        licenseData = new ArrayList<One>();
        careerData = new ArrayList<Career>();
        special = new ArrayList<String>();
        service = new ArrayList<String>();

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

        scrollView = findViewById(R.id.scrollView);

        motive_edit = findViewById(R.id.motive_edit);
        pursue_edit = findViewById(R.id.pursue_edit);
        experience_edit = findViewById(R.id.experience_edit);
        weekday_pay = findViewById(R.id.weekday_pay);
        weekend_pay = findViewById(R.id.weekend_pay);
        month_pay = findViewById(R.id.month_pay);
        etc_check_edit = findViewById(R.id.etc_check_edit);

        radio_group = findViewById(R.id.radio_group);

        radio1 = findViewById(R.id.radio1);
        radio2 = findViewById(R.id.radio2);
        radio3 = findViewById(R.id.radio3);
        radio4 = findViewById(R.id.radio4);

        next = findViewById(R.id.next);

        radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radio1) {
                    know = "1";
                    etc_check_edit.setEnabled(false);
                } else if (checkedId == R.id.radio2) {
                    know = "2";
                    etc_check_edit.setEnabled(false);
                } else if (checkedId == R.id.radio3) {
                    know = "3";
                    etc_check_edit.setEnabled(false);
                } else if (checkedId == R.id.radio4) {
                    know = "4";
                    etc_check_edit.setEnabled(true);
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
                ipmm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);

                Intent intent = new Intent(WriteResume5Activity.this, AgreeActivity.class);
                intent.putExtra("nameK", nameK);
                intent.putExtra("nameE", nameE);
                intent.putExtra("gender", gender);
                intent.putExtra("birth", birth);
                intent.putExtra("phone", phone);
                intent.putExtra("mail", mail);
                intent.putExtra("work", work);
                intent.putExtra("workdetail", work_detail);
                intent.putExtra("cert[]", licenseData);
                intent.putExtra("office[]", careerData);
                intent.putExtra("isactive", isactive);
                intent.putExtra("childcnt", childCount);
                if(!"".equals(children1[0])){
                    intent.putExtra("child[1]", children1);
                }
                if(!"".equals(children2[0])){
                    intent.putExtra("child[2]", children2);
                }
                if(!"".equals(children3[0])){
                    intent.putExtra("child[3]", children3);
                }
                if(!"".equals(children4[0])){
                    intent.putExtra("child[2]", children4);
                }
                intent.putExtra("childdetail", childdetail);
                intent.putExtra("career", parenting);

                intent.putExtra("home", home);
                intent.putExtra("special[]", special);
                intent.putExtra("familydetail", familydetail);
                intent.putExtra("month", month);
                intent.putExtra("option[]", service);
                intent.putExtra("optiondetail", optiondetail);
                intent.putExtra("edu", edu);

                intent.putExtra("motive", motive_edit.getText().toString());
                intent.putExtra("edutype", pursue_edit.getText().toString());
                intent.putExtra("exp", experience_edit.getText().toString());
                intent.putExtra("pay1", weekday_pay.getText().toString());
                intent.putExtra("pay2", weekend_pay.getText().toString());
                intent.putExtra("pay3", month_pay.getText().toString());
                intent.putExtra("know", know);
                intent.putExtra("knowdetail", etc_check_edit.getText().toString());

                startActivity(intent);
                finish();
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
