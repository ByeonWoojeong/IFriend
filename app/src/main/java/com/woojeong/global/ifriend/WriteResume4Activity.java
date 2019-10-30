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

public class WriteResume4Activity extends AppCompatActivity {
    private static String TAG = "WriteResume4Activity";
    Context context;
    InputMethodManager ipmm;
    AQuery aQuery = null;
    OneBtnDialog oneBtnDialog;

    FrameLayout back_con, next_con;
    ImageView back;
    ScrollView scrollView;
    RadioGroup radio_group;
    RadioButton radio1, radio2, radio3, radio4;
    LinearLayout pick_up_check_con, medicate_check_con, toddlers_check_con, long_care_check_con, shower_check_con, homework_check_con, solving_check_con, outdoor_check_con, snack_check_con, meal_check_con, etc_check_con;
    CheckBox pick_up_check, medicate_check, toddlers_check, long_care_check, shower_check, homework_check, solving_check, outdoor_check, snack_check, meal_check, etc_check;
    TextView pick_up_check_txt, medicate_check_txt, toddlers_check_txt, long_care_check_txt, shower_check_txt, homework_check_txt, solving_check_txt, outdoor_check_txt, snack_check_txt, meal_check_txt, etc_check_txt, next;
    EditText etc_check_edit, available_edit;

    Intent resume;
    String nameK, nameE, gender, birth, phone, mail, work, work_detail, childCount, parenting, isactive, childdetail, home, familydetail, month;
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
        setContentView(R.layout.activity_write_resume4);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#ffffff"));
            window.setBackgroundDrawable(null);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                setDarkMode(WriteResume4Activity.this, true);
            }
        }

//        licenseData = new ArrayList<One>();
//        careerData = new ArrayList<Career>();
//        special = new ArrayList<String>();
//        service = new ArrayList<String>();

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
        radio_group = findViewById(R.id.radio_group);

        radio1 = findViewById(R.id.radio1);
        radio2 = findViewById(R.id.radio2);
        radio3 = findViewById(R.id.radio3);
        radio4 = findViewById(R.id.radio4);

        pick_up_check_con = findViewById(R.id.pick_up_check_con);
        medicate_check_con = findViewById(R.id.medicate_check_con);
        toddlers_check_con = findViewById(R.id.toddlers_check_con);
        long_care_check_con = findViewById(R.id.long_care_check_con);
        shower_check_con = findViewById(R.id.shower_check_con);
        homework_check_con = findViewById(R.id.homework_check_con);
        solving_check_con = findViewById(R.id.solving_check_con);
        outdoor_check_con = findViewById(R.id.outdoor_check_con);
        snack_check_con = findViewById(R.id.snack_check_con);
        meal_check_con = findViewById(R.id.meal_check_con);
        etc_check_con = findViewById(R.id.etc_check_con);

        pick_up_check = findViewById(R.id.pick_up_check);
        medicate_check = findViewById(R.id.medicate_check);
        toddlers_check = findViewById(R.id.toddlers_check);
        long_care_check = findViewById(R.id.long_care_check);
        shower_check = findViewById(R.id.shower_check);
        homework_check = findViewById(R.id.homework_check);
        solving_check = findViewById(R.id.solving_check);
        outdoor_check = findViewById(R.id.outdoor_check);
        snack_check = findViewById(R.id.snack_check);
        meal_check = findViewById(R.id.meal_check);
        etc_check = findViewById(R.id.etc_check);

        pick_up_check_txt = findViewById(R.id.pick_up_check_txt);
        medicate_check_txt = findViewById(R.id.medicate_check_txt);
        toddlers_check_txt = findViewById(R.id.toddlers_check_txt);
        long_care_check_txt = findViewById(R.id.long_care_check_txt);
        shower_check_txt = findViewById(R.id.shower_check_txt);
        homework_check_txt = findViewById(R.id.homework_check_txt);
        solving_check_txt = findViewById(R.id.solving_check_txt);
        outdoor_check_txt = findViewById(R.id.etc_check);
        snack_check_txt = findViewById(R.id.snack_check_txt);
        meal_check_txt = findViewById(R.id.meal_check_txt);
        etc_check_txt = findViewById(R.id.etc_check_txt);
        next = findViewById(R.id.next);

        etc_check_edit = findViewById(R.id.etc_check_edit);
        available_edit = findViewById(R.id.available_edit);

        radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radio1) {
                    month = "1";
                } else if (checkedId == R.id.radio2) {
                    month = "2";
                } else if (checkedId == R.id.radio3) {
                    month = "3";
                } else if (checkedId == R.id.radio4) {
                    month = "4";
                }
            }
        });

        service = new ArrayList<String>();
        pick_up_check_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pick_up_check.callOnClick();
            }
        });
        pick_up_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    service.add("1");
                } else {
                    service.remove("1");
                }
            }
        });
        medicate_check_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                medicate_check.callOnClick();
            }
        });
        medicate_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    service.add("2");
                } else {
                    service.remove("2");
                }
            }
        });
        toddlers_check_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toddlers_check.callOnClick();
            }
        });
        toddlers_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    service.add("3");
                } else {
                    service.remove("3");
                }
            }
        });
        long_care_check_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long_care_check.callOnClick();
            }
        });
        long_care_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    service.add("4");
                } else {
                    service.remove("4");
                }
            }
        });
        shower_check_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shower_check.callOnClick();
            }
        });
        shower_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    service.add("5");
                } else {
                    service.remove("5");
                }
            }
        });
        homework_check_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homework_check.callOnClick();
            }
        });
        homework_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    service.add("6");
                } else {
                    service.remove("6");
                }
            }
        });
        solving_check_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                solving_check.callOnClick();
            }
        });
        solving_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    service.add("7");
                } else {
                    service.remove("7");
                }
            }
        });
        outdoor_check_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                outdoor_check.callOnClick();
            }
        });
        outdoor_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    service.add("8");
                } else {
                    service.remove("8");
                }
            }
        });
        snack_check_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snack_check.callOnClick();
            }
        });
        snack_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    service.add("9");
                } else {
                    service.remove("9");
                }
            }
        });
        meal_check_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                meal_check.callOnClick();
            }
        });
        meal_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    service.add("10");
                } else {
                    service.remove("10");
                }
            }
        });
        etc_check_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etc_check.callOnClick();
            }
        });
        etc_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    service.add("11");
                    etc_check_edit.setEnabled(true);
                } else {
                    service.remove("11");
                    etc_check_edit.setEnabled(false);
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

                Intent intent = new Intent(WriteResume4Activity.this, WriteResume5Activity.class);
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
                intent.putExtra("optiondetail", etc_check_edit.getText().toString());
                intent.putExtra("edu", available_edit.getText().toString());
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
