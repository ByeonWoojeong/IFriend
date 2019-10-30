package com.woojeong.global.ifriend;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.woojeong.global.ifriend.GlobalApplication.setDarkMode;

public class WriteResume1Activity extends AppCompatActivity {
    private static String TAG = "WriteResume1Activity";
    Context context;
    InputMethodManager ipmm;
    AQuery aQuery = null;
    OneBtnDialog oneBtnDialog;

    FrameLayout back_con, next_con;
    ImageView back;
    ScrollView scrollView;
    EditText name_ko, name_en, birthday, phone_number, email, etc_text;
    TextView man, woman, next;
    RadioGroup radio_group;
    RadioButton housewife_radio, student_radio, worker_radio, freelancer_radio, jobless_radio, etc_radio;

    String whatGender = "", whatJob = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_resume1);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#ffffff"));
            window.setBackgroundDrawable(null);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                setDarkMode(WriteResume1Activity.this, true);
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

        next_con = findViewById(R.id.next_con);

        scrollView = findViewById(R.id.scrollView);

        name_ko = findViewById(R.id.name_ko);
        name_en = findViewById(R.id.name_en);
        birthday = findViewById(R.id.birthday);
        phone_number = findViewById(R.id.phone_number);
        email = findViewById(R.id.email);
        etc_text = findViewById(R.id.etc_text);

        man = findViewById(R.id.man);
        woman = findViewById(R.id.woman);
        next = findViewById(R.id.next);

        radio_group = findViewById(R.id.radio_group);

        housewife_radio = findViewById(R.id.housewife_radio);
        student_radio = findViewById(R.id.student_radio);
        worker_radio = findViewById(R.id.worker_radio);
        freelancer_radio = findViewById(R.id.freelancer_radio);
        jobless_radio = findViewById(R.id.jobless_radio);
        etc_radio = findViewById(R.id.etc_radio);

        man.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                man.setBackground(getResources().getDrawable(R.drawable.border_radius_yellow));
                man.setTextColor(getResources().getColor(R.color.mainColor));
                man.setSelected(true);
                woman.setBackground(getResources().getDrawable(R.drawable.border_radius1));
                woman.setTextColor(getResources().getColor(R.color.editHintGray));
                woman.setSelected(false);
                whatGender = "1";
            }
        });
        woman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                woman.setBackground(getResources().getDrawable(R.drawable.border_radius_yellow));
                woman.setTextColor(getResources().getColor(R.color.mainColor));
                woman.setSelected(true);
                man.setBackground(getResources().getDrawable(R.drawable.border_radius1));
                man.setTextColor(getResources().getColor(R.color.editHintGray));
                man.setSelected(false);
                whatGender = "2";
            }
        });

        radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.housewife_radio) {
                    etc_text.setEnabled(false);
                    whatJob = "1";
                } else if (checkedId == R.id.student_radio) {
                    etc_text.setEnabled(false);
                    whatJob = "2";
                } else if (checkedId == R.id.worker_radio) {
                    etc_text.setEnabled(false);
                    whatJob = "3";
                } else if (checkedId == R.id.freelancer_radio) {
                    etc_text.setEnabled(false);
                    whatJob = "4";
                } else if (checkedId == R.id.jobless_radio) {
                    etc_text.setEnabled(false);
                    whatJob = "5";
                } else if (checkedId == R.id.etc_radio) {
                    etc_text.setEnabled(true);
                    whatJob = "6";
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

                if ("".equals(name_ko.getText().toString()) || "" == name_ko.getText().toString()) {
                    oneBtnDialog = new OneBtnDialog(WriteResume1Activity.this, "한글 성명을 입력해 주세요 !", "확인");
                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    oneBtnDialog.setCancelable(false);
                    oneBtnDialog.show();
                    return;
                }
                if ("".equals(name_en.getText().toString()) || "" == name_en.getText().toString()) {
                    oneBtnDialog = new OneBtnDialog(WriteResume1Activity.this, "영문 성명을 입력해 주세요 !", "확인");
                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    oneBtnDialog.setCancelable(false);
                    oneBtnDialog.show();
                    return;
                }
                if ("".equals(whatGender)) {
                    oneBtnDialog = new OneBtnDialog(WriteResume1Activity.this, "성별을 선택해 주세요 !", "확인");
                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    oneBtnDialog.setCancelable(false);
                    oneBtnDialog.show();
                    return;
                }
                if (8 > birthday.getText().toString().length() || 8 < birthday.getText().toString().length()) {
                    oneBtnDialog = new OneBtnDialog(WriteResume1Activity.this, "생년월일\n형식을 맞춰주세요 !", "확인");
                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    oneBtnDialog.setCancelable(false);
                    oneBtnDialog.show();
                    return;
                }
                if ("".equals(phone_number.getText().toString().trim())) {
                    oneBtnDialog = new OneBtnDialog(WriteResume1Activity.this, "핸드폰 번호를 입력해 주세요 !", "확인");
                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    oneBtnDialog.setCancelable(false);
                    oneBtnDialog.show();
                    return;
                }
                if (!"".equals(phone_number.getText().toString().trim())) {
                    if (!checkPhoneNumber(phone_number.getText().toString().replaceAll("-", "").replaceAll("\\)", "").trim())) {
                        oneBtnDialog = new OneBtnDialog(WriteResume1Activity.this, "핸드폰 번호\n형식을 맞춰주세요 !", "확인");
                        oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        oneBtnDialog.setCancelable(false);
                        oneBtnDialog.show();
                        return;
                    }
                }
                if ("".equals(email.getText().toString().trim())) {
                    oneBtnDialog = new OneBtnDialog(WriteResume1Activity.this, "이메일 주소를 입력해 주세요 !", "확인");
                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    oneBtnDialog.setCancelable(false);
                    oneBtnDialog.show();
                    return;
                }
                if (!"".equals(email.getText().toString().trim())) {
                    if (!checkEmail(email.getText().toString().trim())) {
                        oneBtnDialog = new OneBtnDialog(WriteResume1Activity.this, "이메일 주소\n형식을 맞춰주세요 !", "확인");
                        oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        oneBtnDialog.setCancelable(false);
                        oneBtnDialog.show();
                        return;
                    }
                }

                if ("".equals(whatJob)) {
                    oneBtnDialog = new OneBtnDialog(WriteResume1Activity.this, "직업을 선택해주세요 !", "확인");
                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    oneBtnDialog.setCancelable(false);
                    oneBtnDialog.show();
                    return;
                }

                if(etc_radio.isChecked()){
                    if ("".equals(etc_text.getText().toString())) {
                        oneBtnDialog = new OneBtnDialog(WriteResume1Activity.this, "직업을 입력해주세요 !", "확인");
                        oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        oneBtnDialog.setCancelable(false);
                        oneBtnDialog.show();
                        return;
                    }
                }

                Intent intent = new Intent(WriteResume1Activity.this, WriteResume2Activity.class);
                intent.putExtra("nameK", name_ko.getText().toString().trim());
                intent.putExtra("nameE", name_en.getText().toString().trim());
                intent.putExtra("gender", whatGender);
                intent.putExtra("birth", birthday.getText().toString().trim());
                intent.putExtra("phone", phone_number.getText().toString().trim());
                intent.putExtra("mail", email.getText().toString().trim());
                intent.putExtra("work", whatJob);
                intent.putExtra("workdetail ", etc_text.getText().toString().trim());
                startActivity(intent);
                finish();
            }
        });
    }

    boolean checkPhoneNumber(String number) {
        boolean checkPhoneNumber = Pattern.matches("(01[016789])(\\d{3,4})(\\d{4})", number);
        return checkPhoneNumber;
    }

    boolean checkNumber(String number) {
        boolean checkAreaNumber = Pattern.matches("(\\d{3,4})(\\d{4})", number);
        return checkAreaNumber;
    }

    boolean checkEmail(String email) {
        Pattern pattern = Pattern.compile("^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$");
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
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
