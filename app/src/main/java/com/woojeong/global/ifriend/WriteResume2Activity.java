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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.woojeong.global.ifriend.DTO.Career;
import com.woojeong.global.ifriend.DTO.One;

import java.util.ArrayList;

import static com.woojeong.global.ifriend.GlobalApplication.setDarkMode;

public class WriteResume2Activity extends AppCompatActivity {
    private static String TAG = "WriteResume2Activity";
    Context context;
    InputMethodManager ipmm;
    AQuery aQuery = null;
    OneBtnDialog oneBtnDialog;

    FrameLayout back_con, next_con, child_count_con, spinner_child1_con, spinner_child2_con, spinner_child3_con, spinner_child4_con;
    SpinnerReselect spinner_child_count, spinner_child1, spinner_child2, spinner_child3, spinner_child4;
    ImageView back, spinner_down, child1_down, child2_down, child3_down, child4_down;
    ScrollView scrollView;
    TextView have, none, license_ok, agency_ok, yes, no, child_count_txt, child1_txt, child1_boy, child1_girl, child2_txt, child2_boy, child2_girl, child3_txt, child3_boy, child3_girl, child4_txt, child4_boy, child4_girl, next;
    EditText license_edit, start_work, end_work, agency_edit, child_explain_edit;
    ListView license_list, career_list;
    LinearLayout license_con, about_child_con, child1_con, child2_con, child3_con, child4_con, child_explain_con, career_con;
    RadioGroup career_radio_group;
    RadioButton career_1_radio, career_2_radio, career_3_radio, career_4_radio;


    LicenseAdapter licenseAdapter;
    ArrayList<One> licenseData = null;
    CareerAdapter careerAdapter;
    ArrayList<Career> careerData = null;
    String[] children = new String[]{"0", "1", "2", "3", "4"};
    String[] bornYear = new String[]{"출생년도", "2012", "2013", "2014", "2015", "2016", "2017", "2018", "2019"};

    Intent resume;
    String nameK = "", nameE = "", gender = "", birth = "", phone = "", mail = "", work = "", work_detail = "", childCount = "", parenting = "", isactive = "",
            child1BG = "", child2BG = "", child3BG = "", child4BG = "", child1year = "", child2year = "", child3year = "", child4year = "";
    String[] children1 = {"", ""};
    String[] children2 = {"", ""};
    String[] children3 = {"", ""};
    String[] children4 = {"", ""};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_resume2);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#ffffff"));
            window.setBackgroundDrawable(null);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                setDarkMode(WriteResume2Activity.this, true);
            }
        }

        resume = getIntent();
        nameK = resume.getStringExtra("nameK");
        nameE = resume.getStringExtra("nameE");
        gender = resume.getStringExtra("gender");
        birth = resume.getStringExtra("birth");
        phone = resume.getStringExtra("phone");
        mail = resume.getStringExtra("mail");
        work = resume.getStringExtra("work");
        work_detail = resume.getStringExtra("work_detail");

        context = this;
        aQuery = new AQuery(this);
        ipmm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        ipmm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
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
        license_con = findViewById(R.id.license_con);
        child_count_con = findViewById(R.id.child_count_con);
        spinner_child1_con = findViewById(R.id.spinner_child1_con);
        spinner_child2_con = findViewById(R.id.spinner_child2_con);
        spinner_child3_con = findViewById(R.id.spinner_child3_con);
        spinner_child4_con = findViewById(R.id.spinner_child4_con);

        spinner_child_count = findViewById(R.id.spinner_child_count);
        spinner_child1 = findViewById(R.id.spinner_child1);
        spinner_child2 = findViewById(R.id.spinner_child2);
        spinner_child3 = findViewById(R.id.spinner_child3);
        spinner_child4 = findViewById(R.id.spinner_child4);

        spinner_down = findViewById(R.id.spinner_down);
        child1_down = findViewById(R.id.child1_down);
        child2_down = findViewById(R.id.child2_down);
        child3_down = findViewById(R.id.child3_down);
        child4_down = findViewById(R.id.child4_down);

        scrollView = findViewById(R.id.scrollView);
        scrollView.postDelayed(new Runnable() {
            @Override
            public void run() {
                scrollView.scrollTo(0, 0);
            }
        }, 700);

        have = findViewById(R.id.have);
        none = findViewById(R.id.none);
        license_ok = findViewById(R.id.license_ok);
        agency_ok = findViewById(R.id.agency_ok);
        yes = findViewById(R.id.yes);
        no = findViewById(R.id.no);
        child_count_txt = findViewById(R.id.child_count_txt);
        child1_txt = findViewById(R.id.child1_txt);
        child1_boy = findViewById(R.id.child1_boy);
        child1_girl = findViewById(R.id.child1_girl);
        child2_txt = findViewById(R.id.child2_txt);
        child2_boy = findViewById(R.id.child2_boy);
        child2_girl = findViewById(R.id.child2_girl);
        child3_txt = findViewById(R.id.child3_txt);
        child3_boy = findViewById(R.id.child3_boy);
        child3_girl = findViewById(R.id.child3_girl);
        child4_txt = findViewById(R.id.child4_txt);
        child4_boy = findViewById(R.id.child4_boy);
        child4_girl = findViewById(R.id.child4_girl);
        next = findViewById(R.id.next);

        license_edit = findViewById(R.id.license_edit);
        start_work = findViewById(R.id.start_work);
        end_work = findViewById(R.id.end_work);
        agency_edit = findViewById(R.id.agency_edit);
        child_explain_edit = findViewById(R.id.child_explain_edit);

        license_list = findViewById(R.id.license_list);
        career_list = findViewById(R.id.experience_list);

        about_child_con = findViewById(R.id.about_child_con);
        child1_con = findViewById(R.id.child1_con);
        child2_con = findViewById(R.id.child2_con);
        child3_con = findViewById(R.id.child3_con);
        child4_con = findViewById(R.id.child4_con);
        child_explain_con = findViewById(R.id.child_explain_con);
        career_con = findViewById(R.id.career_con);

        career_radio_group = findViewById(R.id.career_radio_group);

        career_1_radio = findViewById(R.id.career_1_radio);
        career_2_radio = findViewById(R.id.career_2_radio);
        career_3_radio = findViewById(R.id.career_3_radio);
        career_4_radio = findViewById(R.id.career_4_radio);

        license_con.setVisibility(View.GONE);

        have.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                have.setBackground(getResources().getDrawable(R.drawable.border_radius_yellow));
                have.setTextColor(getResources().getColor(R.color.mainColor));
                have.setSelected(true);
                none.setBackground(getResources().getDrawable(R.drawable.border_radius1));
                none.setTextColor(getResources().getColor(R.color.editHintGray));
                none.setSelected(false);
                license_edit.setEnabled(true);
                license_con.setVisibility(View.VISIBLE);
            }
        });
        none.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                none.setBackground(getResources().getDrawable(R.drawable.border_radius_yellow));
                none.setTextColor(getResources().getColor(R.color.mainColor));
                none.setSelected(true);
                have.setBackground(getResources().getDrawable(R.drawable.border_radius1));
                have.setTextColor(getResources().getColor(R.color.editHintGray));
                have.setSelected(false);
                license_edit.setEnabled(false);
                license_con.setVisibility(View.GONE);
            }
        });

        licenseData = new ArrayList<One>();
        licenseAdapter = new LicenseAdapter(WriteResume2Activity.this, R.layout.list_license_item, licenseData, license_list, true);
        license_list.setDivider(null);
        license_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                licenseData.add(new One(license_edit.getText().toString()));
                license_list.setAdapter(licenseAdapter);
                licenseAdapter.notifyDataSetChanged();
                setListViewHeight(licenseAdapter, license_list);
                license_edit.setText("");
            }
        });

        careerData = new ArrayList<Career>();
        careerAdapter = new CareerAdapter(WriteResume2Activity.this, R.layout.list_career_item, careerData, career_list, true);
        career_list.setDivider(null);
        agency_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                careerData.add(new Career(agency_edit.getText().toString(), start_work.getText().toString(), end_work.getText().toString()));
                career_list.setAdapter(careerAdapter);
                careerAdapter.notifyDataSetChanged();
                setListViewHeight(careerAdapter, career_list);
                start_work.setText("");
                end_work.setText("");
                agency_edit.setText("");
            }
        });

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yes.setBackground(getResources().getDrawable(R.drawable.border_radius_yellow));
                yes.setTextColor(getResources().getColor(R.color.mainColor));
                yes.setSelected(true);
                no.setBackground(getResources().getDrawable(R.drawable.border_radius1));
                no.setTextColor(getResources().getColor(R.color.editHintGray));
                no.setSelected(false);
                isactive  = "1";
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                no.setBackground(getResources().getDrawable(R.drawable.border_radius_yellow));
                no.setTextColor(getResources().getColor(R.color.mainColor));
                no.setSelected(true);
                yes.setBackground(getResources().getDrawable(R.drawable.border_radius1));
                yes.setTextColor(getResources().getColor(R.color.editHintGray));
                yes.setSelected(false);
                isactive  = "0";
            }
        });

        ArrayAdapter<String> childrenAdapter = new ArrayAdapter<String>(context, R.layout.spinner_item, children);
        childrenAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner_child_count.setAdapter(childrenAdapter);
        spinner_child_count.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                child_count_txt.setText(spinner_child_count.getSelectedItem().toString());
                childCount = (spinner_child_count.getSelectedItemPosition()) + "";
                Log.i(TAG, childCount);
                if (spinner_child_count.getSelectedItemPosition() == 0) {
                    about_child_con.setVisibility(View.GONE);
                    child_explain_con.setVisibility(View.GONE);
                } else if (spinner_child_count.getSelectedItemPosition() == 1) {
                    about_child_con.setVisibility(View.VISIBLE);
                    child_explain_con.setVisibility(View.VISIBLE);
                    child1_con.setVisibility(View.VISIBLE);
                    child2_con.setVisibility(View.GONE);
                    child3_con.setVisibility(View.GONE);
                    child4_con.setVisibility(View.GONE);
                } else if (spinner_child_count.getSelectedItemPosition() == 2) {
                    about_child_con.setVisibility(View.VISIBLE);
                    child_explain_con.setVisibility(View.VISIBLE);
                    child1_con.setVisibility(View.VISIBLE);
                    child2_con.setVisibility(View.VISIBLE);
                    child3_con.setVisibility(View.GONE);
                    child4_con.setVisibility(View.GONE);
                } else if (spinner_child_count.getSelectedItemPosition() == 3) {
                    about_child_con.setVisibility(View.VISIBLE);
                    child_explain_con.setVisibility(View.VISIBLE);
                    child1_con.setVisibility(View.VISIBLE);
                    child2_con.setVisibility(View.VISIBLE);
                    child3_con.setVisibility(View.VISIBLE);
                    child4_con.setVisibility(View.GONE);
                } else if (spinner_child_count.getSelectedItemPosition() == 4) {
                    about_child_con.setVisibility(View.VISIBLE);
                    child_explain_con.setVisibility(View.VISIBLE);
                    child1_con.setVisibility(View.VISIBLE);
                    child2_con.setVisibility(View.VISIBLE);
                    child3_con.setVisibility(View.VISIBLE);
                    child4_con.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter<String> yearAdapter = new ArrayAdapter<String>(context, R.layout.spinner_item, bornYear);
        yearAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner_child1.setAdapter(yearAdapter);
        spinner_child1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                child1_txt.setText(spinner_child1.getSelectedItem().toString());
                children1[0] = spinner_child1.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });
        spinner_child2.setAdapter(yearAdapter);
        spinner_child2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                child2_txt.setText(spinner_child2.getSelectedItem().toString());
                children2[0] = spinner_child2.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });
        spinner_child3.setAdapter(yearAdapter);
        spinner_child3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                child3_txt.setText(spinner_child3.getSelectedItem().toString());
                children3[0] = spinner_child3.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });
        spinner_child4.setAdapter(yearAdapter);
        spinner_child4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                child4_txt.setText(spinner_child4.getSelectedItem().toString());
                children4[0] = spinner_child4.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });

        child1_boy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                child1_boy.setBackground(getResources().getDrawable(R.drawable.border_radius_yellow));
                child1_boy.setTextColor(getResources().getColor(R.color.mainColor));
                child1_boy.setSelected(true);
                child1_girl.setBackground(getResources().getDrawable(R.drawable.border_radius1));
                child1_girl.setTextColor(getResources().getColor(R.color.editHintGray));
                child1_girl.setSelected(false);
                if("".equals(child1BG) || "2".equals(child1BG)){
                    child1BG = "1";
                }
            }
        });
        child1_girl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                child1_girl.setBackground(getResources().getDrawable(R.drawable.border_radius_yellow));
                child1_girl.setTextColor(getResources().getColor(R.color.mainColor));
                child1_girl.setSelected(true);
                child1_boy.setBackground(getResources().getDrawable(R.drawable.border_radius1));
                child1_boy.setTextColor(getResources().getColor(R.color.editHintGray));
                child1_boy.setSelected(false);
                if("".equals(child1BG) || "1".equals(child1BG)){
                    child1BG = "2";
                }
            }
        });
        child2_boy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                child2_boy.setBackground(getResources().getDrawable(R.drawable.border_radius_yellow));
                child2_boy.setTextColor(getResources().getColor(R.color.mainColor));
                child2_boy.setSelected(true);
                child2_girl.setBackground(getResources().getDrawable(R.drawable.border_radius1));
                child2_girl.setTextColor(getResources().getColor(R.color.editHintGray));
                child2_girl.setSelected(false);
                if("".equals(child2BG) || "2".equals(child2BG)){
                    child2BG = "1";
                }
            }
        });
        child2_girl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                child2_girl.setBackground(getResources().getDrawable(R.drawable.border_radius_yellow));
                child2_girl.setTextColor(getResources().getColor(R.color.mainColor));
                child2_girl.setSelected(true);
                child2_boy.setBackground(getResources().getDrawable(R.drawable.border_radius1));
                child2_boy.setTextColor(getResources().getColor(R.color.editHintGray));
                child2_boy.setSelected(false);
                if("".equals(child2BG) || "1".equals(child2BG)){
                    child2BG = "2";
                }
            }
        });
        child3_boy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                child3_boy.setBackground(getResources().getDrawable(R.drawable.border_radius_yellow));
                child3_boy.setTextColor(getResources().getColor(R.color.mainColor));
                child3_boy.setSelected(true);
                child3_girl.setBackground(getResources().getDrawable(R.drawable.border_radius1));
                child3_girl.setTextColor(getResources().getColor(R.color.editHintGray));
                child3_girl.setSelected(false);
                if("".equals(child3BG) || "2".equals(child3BG)){
                    child3BG = "1";
                }
            }
        });
        child3_girl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                child3_girl.setBackground(getResources().getDrawable(R.drawable.border_radius_yellow));
                child3_girl.setTextColor(getResources().getColor(R.color.mainColor));
                child3_girl.setSelected(true);
                child3_boy.setBackground(getResources().getDrawable(R.drawable.border_radius1));
                child3_boy.setTextColor(getResources().getColor(R.color.editHintGray));
                child3_boy.setSelected(false);
                if("".equals(child3BG) || "1".equals(child3BG)){
                    child3BG = "2";
                }
            }
        });
        child4_boy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                child4_boy.setBackground(getResources().getDrawable(R.drawable.border_radius_yellow));
                child4_boy.setTextColor(getResources().getColor(R.color.mainColor));
                child4_boy.setSelected(true);
                child4_girl.setBackground(getResources().getDrawable(R.drawable.border_radius1));
                child4_girl.setTextColor(getResources().getColor(R.color.editHintGray));
                child4_girl.setSelected(false);
                if("".equals(child4BG) || "2".equals(child4BG)){
                    child4BG = "1";
                }
            }
        });
        child4_girl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                child4_girl.setBackground(getResources().getDrawable(R.drawable.border_radius_yellow));
                child4_girl.setTextColor(getResources().getColor(R.color.mainColor));
                child4_girl.setSelected(true);
                child4_boy.setBackground(getResources().getDrawable(R.drawable.border_radius1));
                child4_boy.setTextColor(getResources().getColor(R.color.editHintGray));
                child4_boy.setSelected(false);
                if("".equals(child4BG) || "1".equals(child4BG)){
                    child4BG = "2";
                }
            }
        });

        career_radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.career_1_radio) {
                    parenting = "0";
                } else if (checkedId == R.id.career_2_radio) {
                    parenting = "1";
                } else if (checkedId == R.id.career_3_radio) {
                    parenting = "2";
                } else if (checkedId == R.id.career_4_radio) {
                    parenting = "3";
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

                children1[1] = child1BG;
                children2[1] = child2BG;
                children3[1] = child3BG;
                children4[1] = child4BG;

                Intent intent = new Intent(WriteResume2Activity.this, WriteResume3Activity.class);
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

                intent.putExtra("childdetail", child_explain_edit.getText().toString());
                intent.putExtra("career", parenting);
                startActivity(intent);
                finish();
            }
        });
    }

    public void setListViewHeight(BaseAdapter adpater, ListView listView) {
        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);

        for (int size = 0; size < listView.getCount(); size++) {
            View listItem = adpater.getView(size, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listView.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
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
