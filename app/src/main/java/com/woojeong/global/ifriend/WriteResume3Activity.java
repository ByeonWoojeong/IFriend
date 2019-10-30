package com.woojeong.global.ifriend;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.woojeong.global.ifriend.DTO.Career;
import com.woojeong.global.ifriend.DTO.One;

import java.util.ArrayList;

import static com.woojeong.global.ifriend.GlobalApplication.setDarkMode;

public class WriteResume3Activity extends AppCompatActivity {
    private static String TAG = "WriteResume3Activity";
    Context context;
    InputMethodManager ipmm;
    AQuery aQuery = null;
    OneBtnDialog oneBtnDialog;

    FrameLayout back_con, next_con;
    ImageView back;
    ScrollView scrollView;
    RadioGroup place_radio_group;
    RadioButton place_radio1, place_radio2, place_radio3, place_radio4;
    LinearLayout family_character_con, family_character_check1_con, family_character_check2_con, family_character_check3_con, family_character_check4_con, family_character_check5_con;
    CheckBox family_character_check1, family_character_check2, family_character_check3, family_character_check4, family_character_check5;
    TextView family_character_txt1, family_character_txt2, family_character_txt3, family_character_txt4, family_character_txt5, next;
    EditText family_explain_edit;

    Intent resume;
    String nameK, nameE, gender, birth, phone, mail, work, work_detail, childCount, parenting, isactive, childdetail, home;
    String[] children1 = {"", ""};
    String[] children2 = {"", ""};
    String[] children3 = {"", ""};
    String[] children4 = {"", ""};
    ArrayList<String> special = null;
    ArrayList<One> licenseData = null;
    ArrayList<Career> careerData = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_resume3);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#ffffff"));
            window.setBackgroundDrawable(null);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                setDarkMode(WriteResume3Activity.this, true);
            }
        }

        licenseData = new ArrayList<One>();
        careerData = new ArrayList<Career>();

        resume = getIntent();
        nameK = resume.getStringExtra("nameK");
        nameE = resume.getStringExtra("nameE");
        gender = resume.getStringExtra("gender");
        birth = resume.getStringExtra("birth");
        phone = resume.getStringExtra("phone");
        mail = resume.getStringExtra("mail");
        work = resume.getStringExtra("work");
        work_detail = resume.getStringExtra("work_detail");
        licenseData =  (ArrayList<One>) resume.getSerializableExtra("cert[]");
        careerData =  (ArrayList<Career>) resume.getSerializableExtra("office[]");
        isactive = resume.getStringExtra("isactive");
        childCount = resume.getStringExtra("childcnt");


        if(resume.getStringArrayExtra("child[1]") != null){
            if(!"".equals(resume.getStringArrayExtra("child[1]")[0])){
                children1 = resume.getStringArrayExtra("child[1]");
            }
        }
        if(resume.getStringArrayExtra("child[2]") != null){
            if(!"".equals(resume.getStringArrayExtra("child[2]")[0])){
                children2 = resume.getStringArrayExtra("child[2]");
            }
        }
        if(resume.getStringArrayExtra("child[3]") != null){
            if(!"".equals(resume.getStringArrayExtra("child[3]")[0])){
                children3 = resume.getStringArrayExtra("child[3]");
            }
        }
        if(resume.getStringArrayExtra("child[4]") != null){
            if(!"".equals(resume.getStringArrayExtra("child[4]")[0])){
                children4 = resume.getStringArrayExtra("child[4]");
            }
        }

        childdetail = resume.getStringExtra("childdetail");
        parenting = resume.getStringExtra("career");

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
        place_radio_group = findViewById(R.id.place_radio_group);

        place_radio1 = findViewById(R.id.place_radio1);
        place_radio2 = findViewById(R.id.place_radio2);
        place_radio3 = findViewById(R.id.place_radio3);
        place_radio4 = findViewById(R.id.place_radio4);

        family_character_con = findViewById(R.id.family_character_con);
        family_character_check1_con = findViewById(R.id.family_character_check1_con);
        family_character_check2_con = findViewById(R.id.family_character_check2_con);
        family_character_check3_con = findViewById(R.id.family_character_check3_con);
        family_character_check4_con = findViewById(R.id.family_character_check4_con);
        family_character_check5_con = findViewById(R.id.family_character_check5_con);

        family_character_check1 = findViewById(R.id.family_character_check1);
        family_character_check2 = findViewById(R.id.family_character_check2);
        family_character_check3 = findViewById(R.id.family_character_check3);
        family_character_check4 = findViewById(R.id.family_character_check4);
        family_character_check5 = findViewById(R.id.family_character_check5);

        family_character_txt1 = findViewById(R.id.family_character_txt1);
        family_character_txt2 = findViewById(R.id.family_character_txt2);
        family_character_txt3 = findViewById(R.id.family_character_txt3);
        family_character_txt4 = findViewById(R.id.family_character_txt4);
        family_character_txt5 = findViewById(R.id.family_character_txt5);
        next = findViewById(R.id.next);

        family_explain_edit = findViewById(R.id.family_explain_edit);

        place_radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.place_radio1) {
                    home = "1";
                } else if (checkedId == R.id.place_radio2) {
                    home = "2";
                } else if (checkedId == R.id.place_radio3) {
                    home = "3";
                } else if (checkedId == R.id.place_radio4) {
                    home = "4";
                }
            }
        });

        family_character_check1_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                family_character_check1.callOnClick();
            }
        });
        family_character_check2_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                family_character_check2.callOnClick();
            }
        });
        family_character_check3_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                family_character_check3.callOnClick();
            }
        });
        family_character_check4_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                family_character_check4.callOnClick();
            }
        });
        family_character_check5_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                family_character_check5.callOnClick();
            }
        });
        special = new ArrayList<String>();
        family_character_check1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(family_character_check1.isChecked()){
                    special.add("1");
                }else{
                    special.remove("1");
                }
            }
        });
        family_character_check2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(family_character_check2.isChecked()){
                    special.add("2");
                }else{
                    special.remove("2");
                }
            }
        });
        family_character_check3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(family_character_check3.isChecked()){
                    special.add("3");
                }else{
                    special.remove("3");
                }
            }
        });
        family_character_check4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(family_character_check4.isChecked()){
                    special.add("4");
                }else{
                    special.remove("4");
                }
            }
        });
        family_character_check5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(family_character_check5.isChecked()){
                    special.clear();
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

                Intent intent = new Intent(WriteResume3Activity.this, WriteResume4Activity.class);
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
                intent.putExtra("familydetail", family_explain_edit.getText().toString());
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
