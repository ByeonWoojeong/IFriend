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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;

import static com.woojeong.global.ifriend.GlobalApplication.setDarkMode;

public class ChoiceFilterActivity extends AppCompatActivity {
    private static String TAG = "ChoiceFilterActivity";
    Context context;
    InputMethodManager ipmm;
    OneBtnDialog oneBtnDialog;
    AQuery aQuery = null;
    FrameLayout back_con, consignment_con, education_con, theme_con, long_care_con, pick_up_con, spinner_born1_con, spinner_born2_con,
            boy_con, girl_con, doesnt_matter_con, near_con, popular_con, recommend_con;
    ImageView back, reset_icon, born_down1, born_down2;
    TextView ok, reset_txt, consignment, education, theme, long_care, pick_up, born_txt1, born_txt2, boy, girl, doesnt_matter,
            near, popular, recommend;
    LinearLayout reset_con;
    SpinnerReselect spinner_born1, spinner_born2;

    boolean isConsignment, isLongCare, isPickUp, isBoy, isGirl, isDoesntMatter, isNear, isPopular, isRecommend;

    String[] spinner = new String[]{"선택하세요.", "1950년생", "1951년생", "1952년생", "1953년생", "1954년생", "1955년생", "1956년생", "1957년생", "1958년생", "1959년생",
            "1960년생", "1961년생", "1962년생", "1963년생", "1964년생", "1965년생", "1966년생", "1967년생", "1968년생", "1969년생",
            "1970년생", "1971년생", "1972년생", "1973년생", "1974년생", "1975년생", "1976년생", "1977년생", "1978년생", "1979년생",
            "1980년생", "1981년생", "1982년생", "1983년생", "1984년생", "1985년생", "1986년생", "1987년생", "1988년생", "1989년생",
            "1990년생", "1991년생", "1992년생", "1993년생", "1994년생", "1995년생", "1996년생", "1997년생", "1998년생", "1999년생",
            "2000년생", "2001년생", "2002년생", "2003년생", "2004년생", "2005년생", "2006년생", "2007년생", "2008년생", "2009년생",
            "2010년생", "2011년생", "2012년생", "2013년생", "2014년생", "2015년생", "2016년생", "2017년생", "2018년생", "2019년생"};
    String getBorn1 = "", getBorn2 = "";

    Handler handler;
    SharedPreferences filteringData;
    SharedPreferences.Editor filterEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_filter);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#ffffff"));
            window.setBackgroundDrawable(null);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                setDarkMode(ChoiceFilterActivity.this, true);
            }
        }

        filteringData = getSharedPreferences("filtering", Activity.MODE_PRIVATE);
        filterEditor = filteringData.edit();

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                viewSettings();
            }
        }, 200);

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
        ok = findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterEditor.commit();
                finish();
            }
        });
        reset_con = findViewById(R.id.reset_con);
        reset_txt = findViewById(R.id.reset_txt);
        reset_icon = findViewById(R.id.reset_icon);
        reset_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset_icon.callOnClick();
            }
        });
        reset_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset_icon.callOnClick();
            }
        });
        consignment_con = findViewById(R.id.consignment_con);
        consignment = findViewById(R.id.consignment);
        consignment_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                consignment.callOnClick();
            }
        });
        education_con = findViewById(R.id.education_con);
        education = findViewById(R.id.education);
        education_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                education.callOnClick();
            }
        });
        theme_con = findViewById(R.id.theme_con);
        theme = findViewById(R.id.theme);
        theme_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                theme.callOnClick();
            }
        });
        long_care_con = findViewById(R.id.long_care_con);
        long_care = findViewById(R.id.long_care);
        long_care_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long_care.callOnClick();
            }
        });
        pick_up_con = findViewById(R.id.pick_up_con);
        pick_up = findViewById(R.id.pick_up);
        pick_up_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pick_up.callOnClick();
            }
        });
        spinner_born1_con = findViewById(R.id.spinner_born1_con);
        spinner_born1 = findViewById(R.id.spinner_born1);
        born_down1 = findViewById(R.id.born_down1);
        spinner_born1_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                born_down1.callOnClick();
            }
        });
        spinner_born2_con = findViewById(R.id.spinner_born2_con);
        spinner_born2 = findViewById(R.id.spinner_born2);
        born_down2 = findViewById(R.id.born_down2);
        spinner_born2_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                born_down2.callOnClick();
            }
        });
        boy_con = findViewById(R.id.boy_con);
        boy = findViewById(R.id.boy);
        boy_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boy.callOnClick();
            }
        });
        girl_con = findViewById(R.id.girl_con);
        girl = findViewById(R.id.girl);
        girl_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                girl.callOnClick();
            }
        });
        doesnt_matter_con = findViewById(R.id.doesnt_matter_con);
        doesnt_matter = findViewById(R.id.doesnt_matter);
        doesnt_matter_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doesnt_matter.callOnClick();
            }
        });
        near_con = findViewById(R.id.near_con);
        near = findViewById(R.id.near);
        near_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                near.callOnClick();
            }
        });
        popular_con = findViewById(R.id.popular_con);
        popular = findViewById(R.id.popular);
        popular_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popular.callOnClick();
            }
        });
        recommend_con = findViewById(R.id.recommend_con);
        recommend = findViewById(R.id.recommend);
        recommend_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recommend.callOnClick();
            }
        });
        born_txt1 = findViewById(R.id.born_txt1);
        born_txt2 = findViewById(R.id.born_txt2);


        reset_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                consignment.setTextColor(getResources().getColor(R.color.editHintGray));
                consignment.setBackground(getResources().getDrawable(R.drawable.border_radius1));
                isConsignment = false;
                long_care.setTextColor(getResources().getColor(R.color.editHintGray));
                long_care.setBackground(getResources().getDrawable(R.drawable.border_radius1));
                isLongCare = false;
                pick_up.setTextColor(getResources().getColor(R.color.editHintGray));
                pick_up.setBackground(getResources().getDrawable(R.drawable.border_radius1));
                isPickUp = false;
                boy.setTextColor(getResources().getColor(R.color.editHintGray));
                boy.setBackground(getResources().getDrawable(R.drawable.border_radius1));
                isBoy = false;
                girl.setTextColor(getResources().getColor(R.color.editHintGray));
                girl.setBackground(getResources().getDrawable(R.drawable.border_radius1));
                isGirl = false;
                doesnt_matter.setTextColor(getResources().getColor(R.color.editHintGray));
                doesnt_matter.setBackground(getResources().getDrawable(R.drawable.border_radius1));
                isDoesntMatter = false;
                near.setTextColor(getResources().getColor(R.color.editHintGray));
                near.setBackground(getResources().getDrawable(R.drawable.border_radius1));
                isNear = false;
                popular.setTextColor(getResources().getColor(R.color.editHintGray));
                popular.setBackground(getResources().getDrawable(R.drawable.border_radius1));
                isPopular = false;
                recommend.setTextColor(getResources().getColor(R.color.editHintGray));
                recommend.setBackground(getResources().getDrawable(R.drawable.border_radius1));
                isRecommend = false;
                born_txt1.setText(spinner[0]);
                born_txt2.setText(spinner[0]);

                filterEditor.remove("period");
                filterEditor.remove("pickup");
                filterEditor.remove("start");
                filterEditor.remove("end");
                filterEditor.remove("care");
                filterEditor.remove("order");
            }
        });

        consignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isConsignment) {
                    consignment.setTextColor(getResources().getColor(R.color.mainColor));
                    consignment.setBackground(getResources().getDrawable(R.drawable.border_radius_yellow));
                    isConsignment = true;
                } else {
                    consignment.setTextColor(getResources().getColor(R.color.editHintGray));
                    consignment.setBackground(getResources().getDrawable(R.drawable.border_radius1));
                    isConsignment = false;
                }
            }
        });

        long_care.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isLongCare) {
                    long_care.setTextColor(getResources().getColor(R.color.mainColor));
                    long_care.setBackground(getResources().getDrawable(R.drawable.border_radius_yellow));
                    isLongCare = true;
                    filterEditor.putBoolean("period", true);
                } else {
                    long_care.setTextColor(getResources().getColor(R.color.editHintGray));
                    long_care.setBackground(getResources().getDrawable(R.drawable.border_radius1));
                    isLongCare = false;
                    filterEditor.putBoolean("period", false);
                }
            }
        });

        pick_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isPickUp) {
                    pick_up.setTextColor(getResources().getColor(R.color.mainColor));
                    pick_up.setBackground(getResources().getDrawable(R.drawable.border_radius_yellow));
                    isPickUp = true;
                    filterEditor.putBoolean("pickup", true);
                } else {
                    pick_up.setTextColor(getResources().getColor(R.color.editHintGray));
                    pick_up.setBackground(getResources().getDrawable(R.drawable.border_radius1));
                    isPickUp = false;
                    filterEditor.putBoolean("pickup", false);
                }
            }
        });

        ArrayAdapter<String> spinner1Adapter = new ArrayAdapter<String>(context, R.layout.spinner_item, spinner);
        spinner1Adapter.setDropDownViewResource(R.layout.spinner_item);
        spinner_born1.setAdapter(spinner1Adapter);
        spinner_born1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                born_txt1.setText(spinner_born1.getSelectedItem().toString());

                if(!"선택하세요.".equals(born_txt1.getText().toString())){
                    filterEditor.putString("start", born_txt1.getText().toString().replaceAll("년생", ""));
                } else {
                    filterEditor.putString("start", "");
                }

                getBorn1 = (spinner_born1.getSelectedItemPosition()) + "";
                if ("0".equals(getBorn1)) {
                    getBorn1 = "";
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter<String> spinner2Adapter = new ArrayAdapter<String>(context, R.layout.spinner_item, spinner);
        spinner2Adapter.setDropDownViewResource(R.layout.spinner_item);
        spinner_born2.setAdapter(spinner2Adapter);
        spinner_born2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                born_txt2.setText(spinner_born2.getSelectedItem().toString());

                if(!"선택하세요.".equals(born_txt2.getText().toString())){
                    filterEditor.putString("end", born_txt2.getText().toString().replaceAll("년생", ""));
                } else {
                    filterEditor.putString("end", "");
                }


                getBorn2 = (spinner_born2.getSelectedItemPosition()) + "";
                if ("0".equals(getBorn2)) {
                    getBorn2 = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        boy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isBoy) {
                    boy.setTextColor(getResources().getColor(R.color.mainColor));
                    boy.setBackground(getResources().getDrawable(R.drawable.border_radius_yellow));
                    isBoy = true;
                    filterEditor.putString("care", "1");
                    if (isDoesntMatter) {
                        doesnt_matter.setTextColor(getResources().getColor(R.color.editHintGray));
                        doesnt_matter.setBackground(getResources().getDrawable(R.drawable.border_radius1));
                        isDoesntMatter = false;
                    }
                    if (isGirl) {
                        girl.setTextColor(getResources().getColor(R.color.editHintGray));
                        girl.setBackground(getResources().getDrawable(R.drawable.border_radius1));
                        isGirl = false;
                    }
                } else {
                    boy.setTextColor(getResources().getColor(R.color.editHintGray));
                    boy.setBackground(getResources().getDrawable(R.drawable.border_radius1));
                    isBoy = false;
                    filterEditor.remove("care");
                }
            }
        });
        girl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isGirl) {
                    girl.setTextColor(getResources().getColor(R.color.mainColor));
                    girl.setBackground(getResources().getDrawable(R.drawable.border_radius_yellow));
                    isGirl = true;
                    filterEditor.putString("care", "2");
                    if (isDoesntMatter) {
                        doesnt_matter.setTextColor(getResources().getColor(R.color.editHintGray));
                        doesnt_matter.setBackground(getResources().getDrawable(R.drawable.border_radius1));
                        isDoesntMatter = false;
                    }
                    if (isBoy) {
                        boy.setTextColor(getResources().getColor(R.color.editHintGray));
                        boy.setBackground(getResources().getDrawable(R.drawable.border_radius1));
                        isBoy = false;
                    }
                } else {
                    girl.setTextColor(getResources().getColor(R.color.editHintGray));
                    girl.setBackground(getResources().getDrawable(R.drawable.border_radius1));
                    isGirl = false;
                    filterEditor.remove("care");
                }
            }
        });

        doesnt_matter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isDoesntMatter) {
                    doesnt_matter.setTextColor(getResources().getColor(R.color.mainColor));
                    doesnt_matter.setBackground(getResources().getDrawable(R.drawable.border_radius_yellow));
                    isDoesntMatter = true;
                    filterEditor.putString("care", "3");
                    girl.setTextColor(getResources().getColor(R.color.editHintGray));
                    girl.setBackground(getResources().getDrawable(R.drawable.border_radius1));
                    isGirl = false;
                    boy.setTextColor(getResources().getColor(R.color.editHintGray));
                    boy.setBackground(getResources().getDrawable(R.drawable.border_radius1));
                    isBoy = false;
                } else {
                    doesnt_matter.setTextColor(getResources().getColor(R.color.editHintGray));
                    doesnt_matter.setBackground(getResources().getDrawable(R.drawable.border_radius1));
                    isDoesntMatter = false;
                    filterEditor.remove("care");
                }
            }
        });

        near.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isNear) {
                    near.setTextColor(getResources().getColor(R.color.mainColor));
                    near.setBackground(getResources().getDrawable(R.drawable.border_radius_yellow));
                    isNear = true;
                    filterEditor.putString("order", "1");
                    if (isPopular) {
                        popular.setTextColor(getResources().getColor(R.color.editHintGray));
                        popular.setBackground(getResources().getDrawable(R.drawable.border_radius1));
                        isPopular = false;
                    }
                    if (isRecommend) {
                        recommend.setTextColor(getResources().getColor(R.color.editHintGray));
                        recommend.setBackground(getResources().getDrawable(R.drawable.border_radius1));
                        isRecommend = false;
                    }
                } else {
                    near.setTextColor(getResources().getColor(R.color.editHintGray));
                    near.setBackground(getResources().getDrawable(R.drawable.border_radius1));
                    isNear = false;
                    filterEditor.remove("order");
                }
            }
        });

        popular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isPopular) {
                    popular.setTextColor(getResources().getColor(R.color.mainColor));
                    popular.setBackground(getResources().getDrawable(R.drawable.border_radius_yellow));
                    isPopular = true;
                    filterEditor.putString("order", "2");
                    if (isNear) {
                        near.setTextColor(getResources().getColor(R.color.editHintGray));
                        near.setBackground(getResources().getDrawable(R.drawable.border_radius1));
                        isNear = false;
                    }
                    if (isRecommend) {
                        recommend.setTextColor(getResources().getColor(R.color.editHintGray));
                        recommend.setBackground(getResources().getDrawable(R.drawable.border_radius1));
                        isRecommend = false;
                    }
                } else {
                    popular.setTextColor(getResources().getColor(R.color.editHintGray));
                    popular.setBackground(getResources().getDrawable(R.drawable.border_radius1));
                    isPopular = false;
                    filterEditor.remove("order");
                }
            }
        });

        recommend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isRecommend) {
                    recommend.setTextColor(getResources().getColor(R.color.mainColor));
                    recommend.setBackground(getResources().getDrawable(R.drawable.border_radius_yellow));
                    isRecommend = true;
                    filterEditor.putString("order", "3");
                    if (isNear) {
                        near.setTextColor(getResources().getColor(R.color.editHintGray));
                        near.setBackground(getResources().getDrawable(R.drawable.border_radius1));
                        isNear = false;
                    }
                    if (isPopular) {
                        popular.setTextColor(getResources().getColor(R.color.editHintGray));
                        popular.setBackground(getResources().getDrawable(R.drawable.border_radius1));
                        isPopular = false;
                    }
                } else {
                    recommend.setTextColor(getResources().getColor(R.color.editHintGray));
                    recommend.setBackground(getResources().getDrawable(R.drawable.border_radius1));
                    isRecommend = false;
                    filterEditor.remove("order");
                }
            }
        });
    }

    void viewSettings() {

        // 장기돌봄
        if (filteringData.getBoolean("period", false)) {
            Log.i(TAG, " true");
            long_care.setTextColor(getResources().getColor(R.color.mainColor));
            long_care.setBackground(getResources().getDrawable(R.drawable.border_radius_yellow));
            isLongCare = true;
        } else {
            Log.i(TAG, " false");
            long_care.setTextColor(getResources().getColor(R.color.editHintGray));
            long_care.setBackground(getResources().getDrawable(R.drawable.border_radius1));
            isLongCare = false;
        }

        // 픽업서비스
        if (filteringData.getBoolean("pickup", false)) {
            Log.i(TAG, " true");
            pick_up.setTextColor(getResources().getColor(R.color.mainColor));
            pick_up.setBackground(getResources().getDrawable(R.drawable.border_radius_yellow));
            isPickUp = true;

        } else {
            Log.i(TAG, " false");
            pick_up.setTextColor(getResources().getColor(R.color.editHintGray));
            pick_up.setBackground(getResources().getDrawable(R.drawable.border_radius1));
            isPickUp = false;
        }

        // 연령 start, end
        filterEditor.putString("start", filteringData.getString("start", ""));
        filterEditor.putString("end", filteringData.getString("end", ""));

        if (!"".equals(filteringData.getString("start", ""))) {
            Log.i(TAG, " start " + filteringData.getString("start", ""));
            born_txt1.setText(filteringData.getString("start", "") + "년생");
        } else {
            born_txt1.setText("선택하세요.");
        }
        if (!"".equals(filteringData.getString("end", ""))) {
            Log.i(TAG, " end " + filteringData.getString("end", ""));
            born_txt2.setText(filteringData.getString("end", "") + "년생");
        } else {
            born_txt2.setText("선택하세요.");
        }

        // 성별
        if (!"".equals(filteringData.getString("care", ""))) {
            if ("1".equals(filteringData.getString("care", ""))) {
                boy.setTextColor(getResources().getColor(R.color.mainColor));
                boy.setBackground(getResources().getDrawable(R.drawable.border_radius_yellow));
                isBoy = true;
                if (isDoesntMatter) {
                    doesnt_matter.setTextColor(getResources().getColor(R.color.editHintGray));
                    doesnt_matter.setBackground(getResources().getDrawable(R.drawable.border_radius1));
                    isDoesntMatter = false;
                }
                if (isGirl) {
                    girl.setTextColor(getResources().getColor(R.color.editHintGray));
                    girl.setBackground(getResources().getDrawable(R.drawable.border_radius1));
                    isGirl = false;
                }
            } else if ("2".equals(filteringData.getString("care", ""))) {
                girl.setTextColor(getResources().getColor(R.color.mainColor));
                girl.setBackground(getResources().getDrawable(R.drawable.border_radius_yellow));
                isGirl = true;
                if (isDoesntMatter) {
                    doesnt_matter.setTextColor(getResources().getColor(R.color.editHintGray));
                    doesnt_matter.setBackground(getResources().getDrawable(R.drawable.border_radius1));
                    isDoesntMatter = false;
                }
                if (isBoy) {
                    boy.setTextColor(getResources().getColor(R.color.editHintGray));
                    boy.setBackground(getResources().getDrawable(R.drawable.border_radius1));
                    isBoy = false;
                }
            } else if ("3".equals(filteringData.getString("care", ""))) {
                doesnt_matter.setTextColor(getResources().getColor(R.color.mainColor));
                doesnt_matter.setBackground(getResources().getDrawable(R.drawable.border_radius_yellow));
                isDoesntMatter = true;
                if (isGirl) {
                    girl.setTextColor(getResources().getColor(R.color.editHintGray));
                    girl.setBackground(getResources().getDrawable(R.drawable.border_radius1));
                    isGirl = false;
                }
                if (isBoy) {
                    boy.setTextColor(getResources().getColor(R.color.editHintGray));
                    boy.setBackground(getResources().getDrawable(R.drawable.border_radius1));
                    isBoy = false;
                }
            }
        }

        // order
        if (!"".equals(filteringData.getString("order", ""))) {
            if ("1".equals(filteringData.getString("order", ""))) {
                near.setTextColor(getResources().getColor(R.color.mainColor));
                near.setBackground(getResources().getDrawable(R.drawable.border_radius_yellow));
                isNear = true;
            } else if ("2".equals(filteringData.getString("order", ""))) {
                popular.setTextColor(getResources().getColor(R.color.mainColor));
                popular.setBackground(getResources().getDrawable(R.drawable.border_radius_yellow));
                isPopular = true;
            }
            if ("3".equals(filteringData.getString("order", ""))) {
                recommend.setTextColor(getResources().getColor(R.color.mainColor));
                recommend.setBackground(getResources().getDrawable(R.drawable.border_radius_yellow));
                isRecommend = true;
            }
        }
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
