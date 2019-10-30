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
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.smarteist.autoimageslider.DefaultSliderView;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderLayout;
import com.woojeong.global.ifriend.DTO.Career;
import com.woojeong.global.ifriend.DTO.Child;
import com.woojeong.global.ifriend.DTO.One;
import com.woojeong.global.ifriend.DTO.Review;
import com.woojeong.global.ifriend.DTO.Three;
import com.woojeong.global.ifriend.DTO.Two;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

import static com.woojeong.global.ifriend.GlobalApplication.setDarkMode;

public class AboutSitterActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static String TAG = "AboutSitterActivity";
    Context context;
    InputMethodManager ipmm;
    AQuery aQuery = null;
    OneBtnDialog oneBtnDialog;

    FrameLayout back_con, service_more_con, review_more_con, refund_more_con;
    ImageView back, sitter_photo, identity_verification_icon, education_certification_icon, environmental_certification_icon, service_more, service1_icon, service2_icon, service3_icon, refund_more, review_more, chat_icon;
    CheckBox like;
    LinearLayout like_con, sitter_info_con, certify_total_con, identity_verification_con, education_certification_con, environmental_certification_con, available_services_con_con, available_services_con, available_total_con, service1_con, service2_con, service3_con, review_con, refund_con,
            chat_con, reservation_con, location_con;
    ScrollView scrollView;
    SliderLayout home_slider;
    TextView title, sitter_name, sitter_age, sitter_location, identity_verification_txt, education_certification_txt, environmental_certification_txt, weekday_price, weekend_price, care_gender, sitter_child_character,
            house_type, family_explanation, prohibition_explanation, bring_explanation, available_services, service1_txt, service2_txt, service3_txt, available_education_services, weekday_start_time, weekday_end_time, weekend_start_time, weekend_end_time,
            sitter_mind_explanation, review, review_count, review_average, location_sitter, refund, chat, reservation, impossible_date;
    MaterialCalendarView calendar_view;
    MaterialRatingBar rating_bar;

    ListView career_list, sitter_child_list, family_character_list, review_list;
    ArrayList<Three> childData;
    ArrayList<One> oneData;
    ArrayList<Career> careerData;
    ArrayList<Review> reviewData;
    SitterChildAdapter sitterChildAdapter;
    CareerAdapter careerAdapter;
    OneItemAdapter oneItemAdapter;
    ReviewSimpleAdapter reviewSimpleAdapter;

    GoogleMap googleMap;
    double getLat, getLng;

    int getChildCnt, reviewCnt = 0;

    String mode, getMember, getName, noDate, getOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_sitter);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#ffffff"));
            window.setBackgroundDrawable(null);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                setDarkMode(AboutSitterActivity.this, true);
            }
        }

        getMember = getIntent().getStringExtra("member");
        Log.i(TAG, " getMember " + getMember);

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

        service_more_con = findViewById(R.id.service_more_con);
        review_more_con = findViewById(R.id.review_more_con);
        refund_more_con = findViewById(R.id.refund_more_con);
        like = findViewById(R.id.like);
        sitter_photo = findViewById(R.id.sitter_photo);
        identity_verification_icon = findViewById(R.id.identity_verification_icon);
        education_certification_icon = findViewById(R.id.education_certification_icon);
        environmental_certification_icon = findViewById(R.id.environmental_certification_icon);
        service_more = findViewById(R.id.service_more);
        service1_icon = findViewById(R.id.service1_icon);
        service2_icon = findViewById(R.id.service2_icon);
        service3_icon = findViewById(R.id.service3_icon);
        refund_more = findViewById(R.id.refund_more);
        review_more = findViewById(R.id.review_more);
        chat_icon = findViewById(R.id.chat_icon);
        like_con = findViewById(R.id.like_con);
        sitter_info_con = findViewById(R.id.sitter_info_con);
        certify_total_con = findViewById(R.id.certify_total_con);
        identity_verification_con = findViewById(R.id.identity_verification_con);
        education_certification_con = findViewById(R.id.education_certification_con);
        environmental_certification_con = findViewById(R.id.environmental_certification_con);
        available_services_con_con = findViewById(R.id.available_services_con_con);
        available_services_con = findViewById(R.id.available_services_con);
        available_total_con = findViewById(R.id.available_total_con);
        service1_con = findViewById(R.id.service1_con);
        service2_con = findViewById(R.id.service2_con);
        service3_con = findViewById(R.id.service3_con);
        refund_con = findViewById(R.id.refund_con);
        chat_con = findViewById(R.id.chat_con);
        reservation_con = findViewById(R.id.reservation_con);
        location_con = findViewById(R.id.location_con);

        scrollView = findViewById(R.id.scrollView);

        home_slider = findViewById(R.id.home_slider);
        title = findViewById(R.id.title);
        sitter_name = findViewById(R.id.sitter_name);
        sitter_age = findViewById(R.id.sitter_age);
        sitter_location = findViewById(R.id.sitter_location);
        identity_verification_txt = findViewById(R.id.identity_verification_txt);
        education_certification_txt = findViewById(R.id.education_certification_txt);
        environmental_certification_txt = findViewById(R.id.environmental_certification_txt);
        weekday_price = findViewById(R.id.weekday_price);
        weekend_price = findViewById(R.id.weekend_price);
        care_gender = findViewById(R.id.care_gender);
        sitter_child_character = findViewById(R.id.sitter_child_character);
        house_type = findViewById(R.id.house_type);
        family_explanation = findViewById(R.id.family_explanation);
        prohibition_explanation = findViewById(R.id.prohibition_explanation);
        bring_explanation = findViewById(R.id.bring_explanation);
        available_services = findViewById(R.id.available_services);
        service1_txt = findViewById(R.id.service1_txt);
        service2_txt = findViewById(R.id.service2_txt);
        service3_txt = findViewById(R.id.service3_txt);
        available_education_services = findViewById(R.id.available_education_services);
        weekday_start_time = findViewById(R.id.weekday_start_time);
        weekday_end_time = findViewById(R.id.weekday_end_time);
        weekend_start_time = findViewById(R.id.weekend_start_time);
        weekend_end_time = findViewById(R.id.weekend_end_time);
        sitter_mind_explanation = findViewById(R.id.sitter_mind_explanation);
        review_con = findViewById(R.id.review_con);
        review = findViewById(R.id.review);
        review_count = findViewById(R.id.review_count);
        review_average = findViewById(R.id.review_average);
        location_sitter = findViewById(R.id.location_sitter);
        refund = findViewById(R.id.refund);
        chat = findViewById(R.id.chat);
        reservation = findViewById(R.id.reservation);
        impossible_date = findViewById(R.id.impossible_date);

        career_list = findViewById(R.id.career_list);
        sitter_child_list = findViewById(R.id.sitter_child_list);
        family_character_list = findViewById(R.id.family_character_list);
        review_list = findViewById(R.id.review_list);

        calendar_view = findViewById(R.id.calendar_view);
        rating_bar = findViewById(R.id.rating_bar);

        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.sitter_map);
        supportMapFragment.getMapAsync(this);

        oneData = new ArrayList<>();
        childData = new ArrayList<>();
        careerData = new ArrayList<>();
        reviewData = new ArrayList<Review>();
        oneItemAdapter = new OneItemAdapter(AboutSitterActivity.this, R.layout.list_one_item, oneData, family_character_list, "family");
        sitterChildAdapter = new SitterChildAdapter(AboutSitterActivity.this, R.layout.list_sitter_child_item, childData, sitter_child_list, "2");
        careerAdapter = new CareerAdapter(AboutSitterActivity.this, R.layout.list_career_item, careerData, career_list, false);
        reviewSimpleAdapter = new ReviewSimpleAdapter(AboutSitterActivity.this, R.layout.list_review_simple_item, reviewData, review_list);

        String str1 = "예약 ";
        String str2 = "<b>불가능</b>";
        String str3 = "일";
        impossible_date.setText(Html.fromHtml(str1 + str2 + str3));

        like_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                like.callOnClick();
            }
        });
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences get_token = getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
                final String getToken = get_token.getString("Token", "");
                String url = ServerUrl.getBaseUrl() + "/friend/like";
                final Map<String, Object> params = new HashMap<String, Object>();
                params.put("friend", getMember);
                Log.i(TAG, " " + params);

                if (like.isChecked()) {
                    Toast.makeText(AboutSitterActivity.this, "좋아요", Toast.LENGTH_SHORT).show();

                    aQuery.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
                        @Override
                        public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
                            Log.i(TAG, " " + jsonObject);
                            try {
                                if (jsonObject.getBoolean("return")) {    //return이 true 면?

                                    if ("1".equals(jsonObject.getString("like"))) {

                                    } else if ("0".equals(jsonObject.getString("like"))) {

                                    }

                                } else if (!jsonObject.getBoolean("return")) {
                                    if ("login".equals(jsonObject.getString("type"))) {
                                        Toast.makeText(AboutSitterActivity.this, "로그인 해주세요 ! ", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(context, SitterProfileActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }.header("EPOCH-AGENT", "" + getToken).header("User-Agent", "android"));

                } else {
                    Toast.makeText(AboutSitterActivity.this, "취소", Toast.LENGTH_SHORT).show();
                    aQuery.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
                        @Override
                        public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
                            Log.i(TAG, " " + jsonObject);
                            try {
                                if (jsonObject.getBoolean("return")) {    //return이 true 면?

                                    if ("1".equals(jsonObject.getString("like"))) {

                                    } else if ("0".equals(jsonObject.getString("like"))) {

                                    }

                                } else if (!jsonObject.getBoolean("return")) {
                                    if ("login".equals(jsonObject.getString("type"))) {
                                        Toast.makeText(AboutSitterActivity.this, "로그인 해주세요 ! ", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(context, LoginActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
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


        sitter_info_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sitter_photo.callOnClick();
            }
        });
        sitter_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AboutSitterActivity.this, SitterProfileActivity.class);
                intent.putExtra("member", getMember);
                startActivity(intent);
            }
        });

        service_more_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                service_more.callOnClick();
            }
        });
        available_services.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                service_more.callOnClick();
            }
        });
        review_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                review_more.callOnClick();
            }
        });
        review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                review_more.callOnClick();
            }
        });
        review_more_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                review_more.callOnClick();
            }
        });
        review_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (reviewCnt == 0) {
                    oneBtnDialog = new OneBtnDialog(AboutSitterActivity.this, "이용 후기가\n아직 없습니다 !", "확인");
                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    oneBtnDialog.setCancelable(false);
                    oneBtnDialog.show();
                    return;
                }

                Intent intent = new Intent(AboutSitterActivity.this, ReviewsActivity.class);
                intent.putExtra("sitter", getMember);
                startActivity(intent);
            }
        });
        location_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AboutSitterActivity.this, "지도보기", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AboutSitterActivity.this, MapViewActivity.class);
                intent.putExtra("lat", getLat);
                intent.putExtra("lng", getLng);
                startActivity(intent);
            }
        });
        refund_more_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refund_more.callOnClick();
            }
        });
        refund_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AboutSitterActivity.this, RefundGuideActivity.class);
                startActivity(intent);
            }
        });
        chat_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chat.callOnClick();
            }
        });
        chat_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chat.callOnClick();
            }
        });
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                oneBtnDialog = new OneBtnDialog(AboutSitterActivity.this, "대화하기\n서비스 준비 중 !", "확인");
//                oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                oneBtnDialog.setCancelable(false);
//                oneBtnDialog.show();

                Intent intent = new Intent(AboutSitterActivity.this, ChatDetailsActivity.class);
                intent.putExtra("member", getMember);
                intent.putExtra("name", getName);
                startActivity(intent);
            }
        });
        reservation_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reservation.callOnClick();
            }
        });
        reservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AboutSitterActivity.this, ReservationActivity.class);
                intent.putExtra("friend", getMember);
                intent.putExtra("name", sitter_name.getText().toString());
                intent.putExtra("age", sitter_age.getText().toString());
                intent.putExtra("child_count", getChildCnt);
                if(!"".equals(noDate) || null != noDate){
                    intent.putExtra("no_date", noDate);
                }
                Log.i(TAG, " noDate " + noDate);
                intent.putExtra("weekday_price", weekday_price.getText().toString().replaceAll(",", ""));
                intent.putExtra("weekend_price", weekend_price.getText().toString().replaceAll(",", ""));
                startActivity(intent);
            }
        });


        scrollView.postDelayed(new Runnable() {
            @Override
            public void run() {
                scrollView.scrollTo(0, 0);
            }
        }, 750);

    }

    @Override
    protected void onResume() {
        super.onResume();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                viewSettings();
            }
        }, 350);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        this.googleMap = googleMap;
        this.googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }

    void viewSettings() {

        final SharedPreferences get_token = getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
        final String getToken = get_token.getString("Token", "");
        final String url = ServerUrl.getBaseUrl() + "/friend/detail";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("friend", getMember);
        Log.i(TAG, " params " + params);
        aQuery.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
                Log.i(TAG, " jsonObject " + jsonObject);
                try {
                    if (jsonObject.getBoolean("return")) {    //return이 true 면?
                        final JSONObject jsonData = jsonObject.getJSONObject("data");
                        Log.d(TAG, "jsonData : " + jsonData.toString());

                        circleImage(sitter_photo, ServerUrl.getBaseUrl() + "/uploads/images/origin/" + jsonData.getString("profile"));

                        title.setText(jsonData.getString("title"));
                        sitter_name.setText(jsonData.getString("name"));
                        getName = jsonData.getString("name");
                        sitter_age.setText(jsonData.getString("age") + "세");
                        sitter_location.setText(jsonData.getString("addr"));
                        location_sitter.setText(jsonData.getString("addr"));
                        getLat = Double.parseDouble(jsonData.getString("lat"));
                        getLng = Double.parseDouble(jsonData.getString("lng"));
                        weekday_price.setText(jsonData.getString("pay1") + "원");
                        weekend_price.setText(jsonData.getString("pay2") + "원");
                        Log.d(TAG, "callback: " + getLat + " " + getLng);
                        if ("1".equals(jsonData.getString("care"))) {
                            care_gender.setText("남아만");
                        } else if ("2".equals(jsonData.getString("care"))) {
                            care_gender.setText("여아만");
                        } else {
                            care_gender.setText("상관 없음");
                        }

                        String[] images = jsonData.getString("images").split(",");

                        int img_length = images.length;
                        if(images.length>6){
                            img_length = 6;
                        }

                        home_slider.setIndicatorAnimation(IndicatorAnimations.SWAP);
                        home_slider.setSliderTransformAnimation(SliderAnimations.FADETRANSFORMATION);
                        home_slider.setAutoScrolling(false);

                        for (int i = 0; i < img_length; i++) {
                            DefaultSliderView sliderView = new DefaultSliderView(context);
                            sliderView.setImageUrl(ServerUrl.getBaseUrl() + "/uploads/images/origin/" + images[i]);
                            sliderView.setImageScaleType(ImageView.ScaleType.FIT_CENTER);
                            home_slider.addSliderView(sliderView);
                        }

                        LatLng position = new LatLng(getLat, getLng);
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 14));

                        // cert
                        String[] cert = jsonData.getString("office").split(",");
                        careerData.clear();
                        if (cert.length == 0) {
                            career_list.setVisibility(View.GONE);
                        } else {
                            career_list.setVisibility(View.VISIBLE);
                            for (int i = 0; i < cert.length; i++) {
                                if (cert[i].contains("|")) {
                                    String[] certDetail = cert[i].split("\\|");
                                    careerData.add(new Career(certDetail[0], certDetail[1], certDetail[2]));
                                } else {
                                    careerData.add(new Career(cert[i], "0000.00.00", "0000.00.00"));
                                }
                            }
                            career_list.setAdapter(careerAdapter);
                            career_list.setDivider(null);
                            setListViewHeightBasedOnChildren(career_list);
                            careerAdapter.notifyDataSetChanged();
                        }

                        // child
                        String jsonChild = jsonData.getString("child");
                        if (!"".equals(jsonChild)) {
                            String child[] = jsonChild.split(",");
                            childData.clear();
                            if (child.length == 0 || "null".equals(child[0]) || null == child[0]) {
                                sitter_child_list.setVisibility(View.GONE);
                            } else {
                                sitter_child_list.setVisibility(View.VISIBLE);
                                for (int i = 0; i < child.length; i++) {
                                    Log.i(TAG, "child[" + i + "] : " + child[i]);
                                    String bornYear = child[i].substring(0, 4);
                                    String childGender = child[i].substring(5, 6);
                                    String childEtc = "";

                                    if(child[i].length()>7){
                                        childEtc = child[i].substring(7);
                                    }

                                    Log.i(TAG, "bornYear : " + bornYear);
                                    Log.i(TAG, "childGender : " + childGender);
                                    Log.i(TAG, "childEtc : " + childEtc);

                                    childData.add(new Three(bornYear, childGender, childEtc));
                                }
                                sitter_child_list.setAdapter(sitterChildAdapter);
                                sitter_child_list.setDivider(null);
                                setListViewHeightBasedOnChildren(sitter_child_list);
                                sitterChildAdapter.notifyDataSetChanged();

                                getChildCnt = childData.size();
                            }
                        }

                        sitter_child_character.setText(jsonData.getString("childdetail"));

                        if ("1".equals(jsonData.getString("home"))) {
                            house_type.setText("단독 주택");
                        } else if ("2".equals(jsonData.getString("home"))) {
                            house_type.setText("아파트");
                        } else if ("3".equals(jsonData.getString("home"))) {
                            house_type.setText("빌라");
                        } else {
                            house_type.setText("기타");
                        }

                        if ("null".equals(jsonData.getString("special"))) {
                            family_character_list.setVisibility(View.GONE);
                        } else {
                            oneData.clear();
                            family_character_list.setVisibility(View.VISIBLE);
                            String[] specialArray = jsonData.getString("special").split(",");
                            for (int i = 0; i < specialArray.length; i++) {
                                oneData.add(new One(specialArray[i]));
                            }
                            family_character_list.setAdapter(oneItemAdapter);
                            family_character_list.setDivider(null);
                            setListViewHeightBasedOnChildren(family_character_list);
                            oneItemAdapter.notifyDataSetChanged();
                        }

                        family_explanation.setText(jsonData.getString("familydetail"));
                        prohibition_explanation.setText(jsonData.getString("not"));
                        bring_explanation.setText(jsonData.getString("have"));

                        getOption = jsonData.getString("option");

                        if ("null".equals(jsonData.getString("option")) || null == jsonData.getString("option")) {
                            available_services_con_con.setVisibility(View.GONE);
                        } else {
                            available_services_con_con.setVisibility(View.VISIBLE);
                            final String[] option = jsonData.getString("option").split(",");
                            if (option.length == 0) {
                                service1_con.setVisibility(View.GONE);
                                service2_con.setVisibility(View.GONE);
                                service3_con.setVisibility(View.GONE);
                                service_more.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        oneBtnDialog = new OneBtnDialog(AboutSitterActivity.this, "이용 가능 서비스\n더보기가 없습니다 !", "확인");
                                        oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                        oneBtnDialog.setCancelable(false);
                                        oneBtnDialog.show();
                                    }
                                });
                            } else if (option.length == 1) {
                                if ("1".equals(option[0])) {
                                    service1_icon.setBackground(getResources().getDrawable(R.drawable.pick_up_on));
                                    service1_txt.setText("픽업");
                                } else if ("2".equals(option[0])) {
                                    service1_icon.setBackground(getResources().getDrawable(R.drawable.medicine_on));
                                    service1_txt.setText("투약");
                                } else if ("3".equals(option[0])) {
                                    service1_icon.setBackground(getResources().getDrawable(R.drawable.toddlers_care_on));
                                    service1_txt.setText("영유아 돌봄");
                                } else if ("4".equals(option[0])) {
                                    service1_icon.setBackground(getResources().getDrawable(R.drawable.long_care_on));
                                    service1_txt.setText("장기 돌봄");
                                } else if ("5".equals(option[0])) {
                                    service1_icon.setBackground(getResources().getDrawable(R.drawable.shower_on));
                                    service1_txt.setText("목욕");
                                } else if ("6".equals(option[0])) {
                                    service1_icon.setBackground(getResources().getDrawable(R.drawable.home_work_on));
                                    service1_txt.setText("숙제 지도");
                                } else if ("7".equals(option[0])) {
                                    service1_icon.setBackground(getResources().getDrawable(R.drawable.study_lead_on));
                                    service1_txt.setText("학습지 지도");
                                } else if ("8".equals(option[0])) {
                                    service1_icon.setBackground(getResources().getDrawable(R.drawable.outdoor_on));
                                    service1_txt.setText("야외 활동");
                                } else if ("9".equals(option[0])) {
                                    service1_icon.setBackground(getResources().getDrawable(R.drawable.snack_on));
                                    service1_txt.setText("간식 제공");
                                } else if ("10".equals(option[0])) {
                                    service1_icon.setBackground(getResources().getDrawable(R.drawable.meal_on));
                                    service1_txt.setText("식사 제공");
                                } else if ("11".equals(option[0])) {
                                    service1_icon.setBackground(getResources().getDrawable(R.drawable.etc_on));
                                    service1_txt.setText("기타");
                                }
                                service1_con.setVisibility(View.VISIBLE);
                                service2_con.setVisibility(View.GONE);
                                service3_con.setVisibility(View.GONE);
                                service_more.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        oneBtnDialog = new OneBtnDialog(AboutSitterActivity.this, "이용 가능 서비스\n더보기가 없습니다 !", "확인");
                                        oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                        oneBtnDialog.setCancelable(false);
                                        oneBtnDialog.show();
                                    }
                                });
                            } else if (option.length == 2) {
                                if ("1".equals(option[0])) {
                                    service1_icon.setBackground(getResources().getDrawable(R.drawable.pick_up_on));
                                    service1_txt.setText("픽업");
                                } else if ("2".equals(option[0])) {
                                    service1_icon.setBackground(getResources().getDrawable(R.drawable.medicine_on));
                                    service1_txt.setText("투약");
                                } else if ("3".equals(option[0])) {
                                    service1_icon.setBackground(getResources().getDrawable(R.drawable.toddlers_care_on));
                                    service1_txt.setText("영유아 돌봄");
                                } else if ("4".equals(option[0])) {
                                    service1_icon.setBackground(getResources().getDrawable(R.drawable.long_care_on));
                                    service1_txt.setText("장기 돌봄");
                                } else if ("5".equals(option[0])) {
                                    service1_icon.setBackground(getResources().getDrawable(R.drawable.shower_on));
                                    service1_txt.setText("목욕");
                                } else if ("6".equals(option[0])) {
                                    service1_icon.setBackground(getResources().getDrawable(R.drawable.home_work_on));
                                    service1_txt.setText("숙제 지도");
                                } else if ("7".equals(option[0])) {
                                    service1_icon.setBackground(getResources().getDrawable(R.drawable.study_lead_on));
                                    service1_txt.setText("학습지 지도");
                                } else if ("8".equals(option[0])) {
                                    service1_icon.setBackground(getResources().getDrawable(R.drawable.outdoor_on));
                                    service1_txt.setText("야외 활동");
                                } else if ("9".equals(option[0])) {
                                    service1_icon.setBackground(getResources().getDrawable(R.drawable.snack_on));
                                    service1_txt.setText("간식 제공");
                                } else if ("10".equals(option[0])) {
                                    service1_icon.setBackground(getResources().getDrawable(R.drawable.meal_on));
                                    service1_txt.setText("식사 제공");
                                } else if ("11".equals(option[0])) {
                                    service1_icon.setBackground(getResources().getDrawable(R.drawable.etc_on));
                                    service1_txt.setText("기타");
                                }
                                if ("1".equals(option[1])) {
                                    service2_icon.setBackground(getResources().getDrawable(R.drawable.pick_up_on));
                                    service2_txt.setText("픽업");
                                } else if ("2".equals(option[1])) {
                                    service2_icon.setBackground(getResources().getDrawable(R.drawable.medicine_on));
                                    service2_txt.setText("투약");
                                } else if ("3".equals(option[1])) {
                                    service2_icon.setBackground(getResources().getDrawable(R.drawable.toddlers_care_on));
                                    service2_txt.setText("영유아 돌봄");
                                } else if ("4".equals(option[1])) {
                                    service2_icon.setBackground(getResources().getDrawable(R.drawable.long_care_on));
                                    service2_txt.setText("장기 돌봄");
                                } else if ("5".equals(option[1])) {
                                    service2_icon.setBackground(getResources().getDrawable(R.drawable.shower_on));
                                    service2_txt.setText("목욕");
                                } else if ("6".equals(option[1])) {
                                    service2_icon.setBackground(getResources().getDrawable(R.drawable.home_work_on));
                                    service2_txt.setText("숙제 지도");
                                } else if ("7".equals(option[1])) {
                                    service2_icon.setBackground(getResources().getDrawable(R.drawable.study_lead_on));
                                    service2_txt.setText("학습지 지도");
                                } else if ("8".equals(option[1])) {
                                    service2_icon.setBackground(getResources().getDrawable(R.drawable.outdoor_on));
                                    service2_txt.setText("야외 활동");
                                } else if ("9".equals(option[1])) {
                                    service2_icon.setBackground(getResources().getDrawable(R.drawable.snack_on));
                                    service2_txt.setText("간식 제공");
                                } else if ("10".equals(option[1])) {
                                    service2_icon.setBackground(getResources().getDrawable(R.drawable.meal_on));
                                    service2_txt.setText("식사 제공");
                                } else if ("11".equals(option[1])) {
                                    service2_icon.setBackground(getResources().getDrawable(R.drawable.etc_on));
                                    service2_txt.setText("기타");
                                }
                                service1_con.setVisibility(View.VISIBLE);
                                service2_con.setVisibility(View.VISIBLE);
                                service3_con.setVisibility(View.GONE);
                                service_more.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        oneBtnDialog = new OneBtnDialog(AboutSitterActivity.this, "이용 가능 서비스\n더보기가 없습니다 !", "확인");
                                        oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                        oneBtnDialog.setCancelable(false);
                                        oneBtnDialog.show();
                                    }
                                });
                            } else if (option.length == 3) {
                                if ("1".equals(option[0])) {
                                    service1_icon.setBackground(getResources().getDrawable(R.drawable.pick_up_on));
                                    service1_txt.setText("픽업");
                                } else if ("2".equals(option[0])) {
                                    service1_icon.setBackground(getResources().getDrawable(R.drawable.medicine_on));
                                    service1_txt.setText("투약");
                                } else if ("3".equals(option[0])) {
                                    service1_icon.setBackground(getResources().getDrawable(R.drawable.toddlers_care_on));
                                    service1_txt.setText("영유아 돌봄");
                                } else if ("4".equals(option[0])) {
                                    service1_icon.setBackground(getResources().getDrawable(R.drawable.long_care_on));
                                    service1_txt.setText("장기 돌봄");
                                } else if ("5".equals(option[0])) {
                                    service1_icon.setBackground(getResources().getDrawable(R.drawable.shower_on));
                                    service1_txt.setText("목욕");
                                } else if ("6".equals(option[0])) {
                                    service1_icon.setBackground(getResources().getDrawable(R.drawable.home_work_on));
                                    service1_txt.setText("숙제 지도");
                                } else if ("7".equals(option[0])) {
                                    service1_icon.setBackground(getResources().getDrawable(R.drawable.study_lead_on));
                                    service1_txt.setText("학습지 지도");
                                } else if ("8".equals(option[0])) {
                                    service1_icon.setBackground(getResources().getDrawable(R.drawable.outdoor_on));
                                    service1_txt.setText("야외 활동");
                                } else if ("9".equals(option[0])) {
                                    service1_icon.setBackground(getResources().getDrawable(R.drawable.snack_on));
                                    service1_txt.setText("간식 제공");
                                } else if ("10".equals(option[0])) {
                                    service1_icon.setBackground(getResources().getDrawable(R.drawable.meal_on));
                                    service1_txt.setText("식사 제공");
                                } else if ("11".equals(option[0])) {
                                    service1_icon.setBackground(getResources().getDrawable(R.drawable.etc_on));
                                    service1_txt.setText("기타");
                                }
                                if ("1".equals(option[1])) {
                                    service2_icon.setBackground(getResources().getDrawable(R.drawable.pick_up_on));
                                    service2_txt.setText("픽업");
                                } else if ("2".equals(option[1])) {
                                    service2_icon.setBackground(getResources().getDrawable(R.drawable.medicine_on));
                                    service2_txt.setText("투약");
                                } else if ("3".equals(option[1])) {
                                    service2_icon.setBackground(getResources().getDrawable(R.drawable.toddlers_care_on));
                                    service2_txt.setText("영유아 돌봄");
                                } else if ("4".equals(option[1])) {
                                    service2_icon.setBackground(getResources().getDrawable(R.drawable.long_care_on));
                                    service2_txt.setText("장기 돌봄");
                                } else if ("5".equals(option[1])) {
                                    service2_icon.setBackground(getResources().getDrawable(R.drawable.shower_on));
                                    service2_txt.setText("목욕");
                                } else if ("6".equals(option[1])) {
                                    service2_icon.setBackground(getResources().getDrawable(R.drawable.home_work_on));
                                    service2_txt.setText("숙제 지도");
                                } else if ("7".equals(option[1])) {
                                    service2_icon.setBackground(getResources().getDrawable(R.drawable.study_lead_on));
                                    service2_txt.setText("학습지 지도");
                                } else if ("8".equals(option[1])) {
                                    service2_icon.setBackground(getResources().getDrawable(R.drawable.outdoor_on));
                                    service2_txt.setText("야외 활동");
                                } else if ("9".equals(option[1])) {
                                    service2_icon.setBackground(getResources().getDrawable(R.drawable.snack_on));
                                    service2_txt.setText("간식 제공");
                                } else if ("10".equals(option[1])) {
                                    service2_icon.setBackground(getResources().getDrawable(R.drawable.meal_on));
                                    service2_txt.setText("식사 제공");
                                } else if ("11".equals(option[1])) {
                                    service2_icon.setBackground(getResources().getDrawable(R.drawable.etc_on));
                                    service2_txt.setText("기타");
                                }
                                if ("1".equals(option[2])) {
                                    service3_icon.setBackground(getResources().getDrawable(R.drawable.pick_up_on));
                                    service3_txt.setText("픽업");
                                } else if ("2".equals(option[2])) {
                                    service3_icon.setBackground(getResources().getDrawable(R.drawable.medicine_on));
                                    service3_txt.setText("투약");
                                } else if ("3".equals(option[2])) {
                                    service3_icon.setBackground(getResources().getDrawable(R.drawable.toddlers_care_on));
                                    service3_txt.setText("영유아 돌봄");
                                } else if ("4".equals(option[2])) {
                                    service3_icon.setBackground(getResources().getDrawable(R.drawable.long_care_on));
                                    service3_txt.setText("장기 돌봄");
                                } else if ("5".equals(option[2])) {
                                    service3_icon.setBackground(getResources().getDrawable(R.drawable.shower_on));
                                    service3_txt.setText("목욕");
                                } else if ("6".equals(option[2])) {
                                    service3_icon.setBackground(getResources().getDrawable(R.drawable.home_work_on));
                                    service3_txt.setText("숙제 지도");
                                } else if ("7".equals(option[2])) {
                                    service3_icon.setBackground(getResources().getDrawable(R.drawable.study_lead_on));
                                    service3_txt.setText("학습지 지도");
                                } else if ("8".equals(option[2])) {
                                    service3_icon.setBackground(getResources().getDrawable(R.drawable.outdoor_on));
                                    service3_txt.setText("야외 활동");
                                } else if ("9".equals(option[2])) {
                                    service3_icon.setBackground(getResources().getDrawable(R.drawable.snack_on));
                                    service3_txt.setText("간식 제공");
                                } else if ("10".equals(option[2])) {
                                    service3_icon.setBackground(getResources().getDrawable(R.drawable.meal_on));
                                    service3_txt.setText("식사 제공");
                                } else if ("11".equals(option[2])) {
                                    service3_icon.setBackground(getResources().getDrawable(R.drawable.etc_on));
                                    service3_txt.setText("기타");
                                }
                                service1_con.setVisibility(View.VISIBLE);
                                service2_con.setVisibility(View.VISIBLE);
                                service3_con.setVisibility(View.VISIBLE);
                                service_more.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        oneBtnDialog = new OneBtnDialog(AboutSitterActivity.this, "이용 가능 서비스\n더보기가 없습니다 !", "확인");
                                        oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                        oneBtnDialog.setCancelable(false);
                                        oneBtnDialog.show();
                                    }
                                });
                            } else {
                                if ("1".equals(option[0])) {
                                    service1_icon.setBackground(getResources().getDrawable(R.drawable.pick_up_on));
                                    service1_txt.setText("픽업");
                                } else if ("2".equals(option[0])) {
                                    service1_icon.setBackground(getResources().getDrawable(R.drawable.medicine_on));
                                    service1_txt.setText("투약");
                                } else if ("3".equals(option[0])) {
                                    service1_icon.setBackground(getResources().getDrawable(R.drawable.toddlers_care_on));
                                    service1_txt.setText("영유아 돌봄");
                                } else if ("4".equals(option[0])) {
                                    service1_icon.setBackground(getResources().getDrawable(R.drawable.long_care_on));
                                    service1_txt.setText("장기 돌봄");
                                } else if ("5".equals(option[0])) {
                                    service1_icon.setBackground(getResources().getDrawable(R.drawable.shower_on));
                                    service1_txt.setText("목욕");
                                } else if ("6".equals(option[0])) {
                                    service1_icon.setBackground(getResources().getDrawable(R.drawable.home_work_on));
                                    service1_txt.setText("숙제 지도");
                                } else if ("7".equals(option[0])) {
                                    service1_icon.setBackground(getResources().getDrawable(R.drawable.study_lead_on));
                                    service1_txt.setText("학습지 지도");
                                } else if ("8".equals(option[0])) {
                                    service1_icon.setBackground(getResources().getDrawable(R.drawable.outdoor_on));
                                    service1_txt.setText("야외 활동");
                                } else if ("9".equals(option[0])) {
                                    service1_icon.setBackground(getResources().getDrawable(R.drawable.snack_on));
                                    service1_txt.setText("간식 제공");
                                } else if ("10".equals(option[0])) {
                                    service1_icon.setBackground(getResources().getDrawable(R.drawable.meal_on));
                                    service1_txt.setText("식사 제공");
                                } else if ("11".equals(option[0])) {
                                    service1_icon.setBackground(getResources().getDrawable(R.drawable.etc_on));
                                    service1_txt.setText("기타");
                                }
                                if ("1".equals(option[1])) {
                                    service2_icon.setBackground(getResources().getDrawable(R.drawable.pick_up_on));
                                    service2_txt.setText("픽업");
                                } else if ("2".equals(option[1])) {
                                    service2_icon.setBackground(getResources().getDrawable(R.drawable.medicine_on));
                                    service2_txt.setText("투약");
                                } else if ("3".equals(option[1])) {
                                    service2_icon.setBackground(getResources().getDrawable(R.drawable.toddlers_care_on));
                                    service2_txt.setText("영유아 돌봄");
                                } else if ("4".equals(option[1])) {
                                    service2_icon.setBackground(getResources().getDrawable(R.drawable.long_care_on));
                                    service2_txt.setText("장기 돌봄");
                                } else if ("5".equals(option[1])) {
                                    service2_icon.setBackground(getResources().getDrawable(R.drawable.shower_on));
                                    service2_txt.setText("목욕");
                                } else if ("6".equals(option[1])) {
                                    service2_icon.setBackground(getResources().getDrawable(R.drawable.home_work_on));
                                    service2_txt.setText("숙제 지도");
                                } else if ("7".equals(option[1])) {
                                    service2_icon.setBackground(getResources().getDrawable(R.drawable.study_lead_on));
                                    service2_txt.setText("학습지 지도");
                                } else if ("8".equals(option[1])) {
                                    service2_icon.setBackground(getResources().getDrawable(R.drawable.outdoor_on));
                                    service2_txt.setText("야외 활동");
                                } else if ("9".equals(option[1])) {
                                    service2_icon.setBackground(getResources().getDrawable(R.drawable.snack_on));
                                    service2_txt.setText("간식 제공");
                                } else if ("10".equals(option[1])) {
                                    service2_icon.setBackground(getResources().getDrawable(R.drawable.meal_on));
                                    service2_txt.setText("식사 제공");
                                } else if ("11".equals(option[1])) {
                                    service2_icon.setBackground(getResources().getDrawable(R.drawable.etc_on));
                                    service2_txt.setText("기타");
                                }
                                if ("1".equals(option[2])) {
                                    service3_icon.setBackground(getResources().getDrawable(R.drawable.pick_up_on));
                                    service3_txt.setText("픽업");
                                } else if ("2".equals(option[2])) {
                                    service3_icon.setBackground(getResources().getDrawable(R.drawable.medicine_on));
                                    service3_txt.setText("투약");
                                } else if ("3".equals(option[2])) {
                                    service3_icon.setBackground(getResources().getDrawable(R.drawable.toddlers_care_on));
                                    service3_txt.setText("영유아 돌봄");
                                } else if ("4".equals(option[2])) {
                                    service3_icon.setBackground(getResources().getDrawable(R.drawable.long_care_on));
                                    service3_txt.setText("장기 돌봄");
                                } else if ("5".equals(option[2])) {
                                    service3_icon.setBackground(getResources().getDrawable(R.drawable.shower_on));
                                    service3_txt.setText("목욕");
                                } else if ("6".equals(option[2])) {
                                    service3_icon.setBackground(getResources().getDrawable(R.drawable.home_work_on));
                                    service3_txt.setText("숙제 지도");
                                } else if ("7".equals(option[2])) {
                                    service3_icon.setBackground(getResources().getDrawable(R.drawable.study_lead_on));
                                    service3_txt.setText("학습지 지도");
                                } else if ("8".equals(option[2])) {
                                    service3_icon.setBackground(getResources().getDrawable(R.drawable.outdoor_on));
                                    service3_txt.setText("야외 활동");
                                } else if ("9".equals(option[2])) {
                                    service3_icon.setBackground(getResources().getDrawable(R.drawable.snack_on));
                                    service3_txt.setText("간식 제공");
                                } else if ("10".equals(option[2])) {
                                    service3_icon.setBackground(getResources().getDrawable(R.drawable.meal_on));
                                    service3_txt.setText("식사 제공");
                                } else if ("11".equals(option[2])) {
                                    service3_icon.setBackground(getResources().getDrawable(R.drawable.etc_on));
                                    service3_txt.setText("기타");
                                }
                                service_more.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(AboutSitterActivity.this, ReadAvailableServicesActivity.class);
                                        intent.putExtra("option", getOption);

                                        startActivity(intent);
                                    }
                                });
                            }
                        }

                        available_education_services.setText(jsonData.getString("edu"));

                        if (!"".equals(jsonData.getString("date"))) {
                            noDate = jsonData.getString("date");
                            Log.i(TAG, " NODATE " + noDate);
                            String date[] = noDate.split(",");
                            calendar_view.setSelectionColor(getResources().getColor(R.color.impossibleDateGray));
                            if (date.length > 0) {
                                for (int i = 0; i < date.length; i++) {
                                    Log.i(TAG, "date[" + i + "] : " + date[i]);
                                    int year = Integer.parseInt(date[i].substring(0, 4));
                                    int month = Integer.parseInt(date[i].substring(5, 7));
                                    if ("0".equals(date[i].indexOf(5))) {
                                        month = Integer.parseInt(date[i].substring(6, 7));
                                    }
                                    int day = Integer.parseInt(date[i].substring(8, 10));
                                    if ("0".equals(date[i].indexOf(8))) {
                                        day = Integer.parseInt(date[i].substring(9, 10));
                                    }

                                    Log.i(TAG, "year : " + year);
                                    Log.i(TAG, "month : " + month);
                                    Log.i(TAG, "year : " + day);

                                    CalendarDay calendarDay = CalendarDay.from(year, month, day);
                                    calendar_view.setDateSelected(calendarDay, true);
                                }
                            }
                        }

                        weekday_start_time.setText(jsonData.getString("time1") + "시");
                        weekday_end_time.setText(jsonData.getString("time2") + "시");
                        weekend_start_time.setText(jsonData.getString("time3") + "시");
                        weekend_end_time.setText(jsonData.getString("time4") + "시");
                        sitter_mind_explanation.setText(jsonData.getString("intro"));

                        if (!"".equals(jsonData.getString("reviewcnt"))) {
                            reviewCnt = Integer.parseInt(jsonData.getString("reviewcnt"));
                            review_count.setText("(" + jsonData.getString("reviewcnt") + ")");
                        }
                        rating_bar.setProgress(Math.round(Float.parseFloat(jsonData.getString("star"))) * 2);

                        reviewData.clear();
                        JSONArray jsonArray = new JSONArray(jsonObject.getString("list"));
                        if (jsonArray.length() == 0) {
                            review_list.setVisibility(View.GONE);
                        } else {
                            review_list.setVisibility(View.VISIBLE);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject getJsonObject = jsonArray.getJSONObject(i);
                                reviewData.add(new Review(getJsonObject.getString("profile"), getJsonObject.getString("name"), getJsonObject.getString("content"), getJsonObject.getString("star"), getJsonObject.getString("date")));
                            }
                            review_list.setAdapter(reviewSimpleAdapter);
                            reviewSimpleAdapter.notifyDataSetChanged();
                        }

                        if ("1".equals(jsonData.getString("islike"))) {
                            Log.i(TAG, " like");
                            like.setChecked(true);
                        } else {
                            Log.i(TAG, " hate");
                            like.setChecked(false);
                        }

                        circleMarker();

                    } else if (!jsonObject.getBoolean("return")) {
                        if ("login".equals(jsonObject.getString("type"))) {
                            Toast.makeText(AboutSitterActivity.this, "로그인 해주세요 ! ", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(context, LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(AboutSitterActivity.this, "이웃 정보를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.header("EPOCH-AGENT", "" + getToken).header("User-Agent", "android"));


    }

    public void circleMarker() {
        Log.d(TAG, "circleMarker: " + getLat + " " + getLng);
        LatLng position = new LatLng(getLat, getLng);
//        MarkerOptions myMarker = new MarkerOptions()
//                .position(position);
        CircleOptions circle200M = new CircleOptions().center(position) //원점
                .radius(200)      //반지름 단위 : m
                .strokeWidth(0f)  //선너비 0f : 선없음
                .fillColor(Color.parseColor("#b2fcf5a4")); //배경색
//        this.googleMap.addMarker(myMarker);
        this.googleMap.addCircle(circle200M);
    }

    private void circleImage(ImageView imageView, String getImg) {
        Log.i(TAG, " circleImage : " + getImg);
        Glide.with(context)
                .load(getImg)
                .apply(RequestOptions.bitmapTransform(new CropCircleTransformation()))
                .into(imageView);
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);

        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
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
