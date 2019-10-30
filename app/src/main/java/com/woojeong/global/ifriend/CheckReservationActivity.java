package com.woojeong.global.ifriend;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.GeolocationPermissions;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
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
import com.google.gson.JsonObject;
import com.smarteist.autoimageslider.DefaultSliderView;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderLayout;
import com.smarteist.autoimageslider.SliderView;
import com.woojeong.global.ifriend.DTO.Child;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static com.woojeong.global.ifriend.GlobalApplication.setDarkMode;

public class CheckReservationActivity extends AppCompatActivity {
    private static String TAG = "CheckReservationActivity";
    Context context;
    InputMethodManager ipmm;
    AQuery aQuery = null;
    OneBtnDialog oneBtnDialog;
    TwoBtnDialog twoBtnDialog;

    FrameLayout back_con;
    ImageView back, sitter_photo, chat, refund_more_icon;
    ScrollView scrollView;
    SliderLayout home_slider;
    TextView reserve_status, sitter_name, start_date, finish_date, start_time, finish_time, total_time, total_children, expected_price, sale_price, total_amount, bring,
            weekday_start_time, weekday_end_time, weekend_start_time, weekend_end_time, refund_more, negative, positive;
    LinearLayout profile_con, refund_more_con, more_con, negative_con, positive_con;

    String getToken;

    ListView child_list;
    ArrayList<Child> data;
    ChildAdapter childAdapter;

    Intent getIntent;
    String getReserve, getStatus, getProfile, getSitterNumber, getName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_reservation);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#ffffff"));
            window.setBackgroundDrawable(null);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                setDarkMode(CheckReservationActivity.this, true);
            }
        }

        getIntent = getIntent();
        getReserve = getIntent.getStringExtra("reserve");
        getStatus = getIntent.getStringExtra("status");

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

        sitter_photo = findViewById(R.id.sitter_photo);
        chat = findViewById(R.id.chat);
        refund_more_icon = findViewById(R.id.refund_more_icon);

        scrollView = findViewById(R.id.scrollView);
        home_slider = findViewById(R.id.home_slider);
        reserve_status = findViewById(R.id.status);
        sitter_name = findViewById(R.id.sitter_name);
        start_date = findViewById(R.id.start_date);
        finish_date = findViewById(R.id.finish_date);
        start_time = findViewById(R.id.start_time);
        finish_time = findViewById(R.id.finish_time);
        total_time = findViewById(R.id.total_time);
        total_children = findViewById(R.id.total_children);
        expected_price = findViewById(R.id.expected_price);
        sale_price = findViewById(R.id.sale_price);
        total_amount = findViewById(R.id.total_amount);
        bring = findViewById(R.id.bring);
        weekday_start_time = findViewById(R.id.weekday_start_time);
        weekday_end_time = findViewById(R.id.weekday_end_time);
        weekend_start_time = findViewById(R.id.weekend_start_time);
        weekend_end_time = findViewById(R.id.weekend_end_time);
        refund_more = findViewById(R.id.refund_more);
        negative = findViewById(R.id.negative);
        positive = findViewById(R.id.positive);

        profile_con = findViewById(R.id.profile_con);
        refund_more_con = findViewById(R.id.refund_more_con);
        more_con = findViewById(R.id.more_con);
        negative_con = findViewById(R.id.negative_con);
        positive_con = findViewById(R.id.positive_con);

        child_list = findViewById(R.id.child_list);
        data = new ArrayList<Child>();
        childAdapter = new ChildAdapter(CheckReservationActivity.this, R.layout.list_child_item, data, child_list, "profile");

        scrollView.scrollTo(0, 0);

        profile_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CheckReservationActivity.this, SitterProfileActivity.class);
                intent.putExtra("member", getSitterNumber);
                startActivity(intent);
            }
        });


        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                oneBtnDialog = new OneBtnDialog(CheckReservationActivity.this, "서비스 준비 중 입니다 !", "확인");
//                oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                oneBtnDialog.setCancelable(false);
//                oneBtnDialog.show();

                Intent intent = new Intent(CheckReservationActivity.this, ChatDetailsActivity.class);
                intent.putExtra("member", getSitterNumber);
                intent.putExtra("name", getName);
                intent.putExtra("status", getStatus);
                startActivity(intent);
            }
        });

        refund_more_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refund_more_icon.callOnClick();
            }
        });
        refund_more_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CheckReservationActivity.this, RefundGuideActivity.class);
                startActivity(intent);
            }
        });

        negative_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                negative.callOnClick();
            }
        });
        negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("예약 취소".equals(negative.getText().toString())) {
                    twoBtnDialog = new TwoBtnDialog(context, "예약을 취소하시겠습니까?", "cancel");
                    twoBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    twoBtnDialog.setCancelable(false);
                    twoBtnDialog.show();
                } else if ("환불 신청".equals(negative.getText().toString())) {
                    twoBtnDialog = new TwoBtnDialog(context, "환불을 신청하시겠습니까?", "refund");
                    twoBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    twoBtnDialog.setCancelable(false);
                    twoBtnDialog.show();
                }
            }
        });

        positive_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                positive.callOnClick();
            }
        });
        positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("예약하기".equals(positive.getText().toString())) {
                    Toast.makeText(CheckReservationActivity.this, "예약하기", Toast.LENGTH_SHORT).show();
//                    oneBtnDialog = new OneBtnDialog(CheckReservationActivity.this, "예약 요청 완료.\n이웃친구가 고객님의 예약을\n수락하기 전에는\n결제되지 않습니다.", "확인");
//                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                    oneBtnDialog.setCancelable(false);
//                    oneBtnDialog.show();
//                    return;
                } else if ("결제하기".equals(positive.getText().toString())) {
                    Toast.makeText(CheckReservationActivity.this, "결제 페이지로 넘어감", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(CheckReservationActivity.this, WebViewActivity.class);
                    intent.putExtra("reserve", getReserve);
                    startActivity(intent);

                } else if ("후기 작성".equals(positive.getText().toString())) {
                    Intent intent = new Intent(CheckReservationActivity.this, WriteReviewActivity.class);
                    intent.putExtra("write", "write");
                    //idx-getSitterNumber, profile-getProfile, name-getName
                    intent.putExtra("idx", getSitterNumber);
                    intent.putExtra("image", getProfile);
                    intent.putExtra("name", getName);
                    intent.putExtra("reserve", getReserve);
                    startActivity(intent);
                } else if ("후기 수정".equals(positive.getText().toString())) {
                    Intent intent = new Intent(CheckReservationActivity.this, WriteReviewActivity.class);
                    intent.putExtra("write", "edit");
                    intent.putExtra("idx", getSitterNumber);
                    intent.putExtra("image", getProfile);
                    intent.putExtra("name", getName);
                    intent.putExtra("reserve", getReserve);
                    startActivity(intent);
                } else if ("다시 예약하기".equals(positive.getText().toString())) {
                    Toast.makeText(CheckReservationActivity.this, "다시 예약하기", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(CheckReservationActivity.this, AboutSitterActivity.class);
                    intent.putExtra("member", getSitterNumber);
                    finish();
                    startActivity(intent);
                }
            }
        });

        home_slider.setIndicatorAnimation(IndicatorAnimations.SWAP);
        home_slider.setSliderTransformAnimation(SliderAnimations.FADETRANSFORMATION);
        home_slider.setAutoScrolling(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewSettings();
    }

    void homePhoto(String images) {
        Log.i(TAG, " homePhoto ");
        String[] array = images.split(",");
        DefaultSliderView sliderView = new DefaultSliderView(context);
        for (int i = 0; i < array.length; i++) {
            sliderView.setImageUrl(ServerUrl.getBaseUrl() + "/uploads/images/origin/" + array[i]);
            sliderView.setImageScaleType(ImageView.ScaleType.FIT_CENTER);
            home_slider.addSliderView(sliderView);
        }
    }

    void viewSettings() {
        Log.i(TAG, " viewSettings " + getStatus);

        SharedPreferences get_token = getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
        getToken = get_token.getString("Token", "");
        final String url = ServerUrl.getBaseUrl() + "/reserve/detail";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("reserve", getReserve);
        aQuery.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
                Log.i(TAG, " jsonObject " + jsonObject);
                try {
                    if (jsonObject.getBoolean("return")) {    //return이 true 면?
                        JSONArray jsonArray = new JSONArray(jsonObject.getString("list"));
                        if (jsonArray.length() == 0) {
                            Toast.makeText(CheckReservationActivity.this, "예약 정보가 없습니다 !", Toast.LENGTH_SHORT).show();
                        } else {
                            JSONObject getJsonObject = jsonArray.getJSONObject(0);
                            sitter_name.setText(getJsonObject.getString("friend_name"));
                            getName = getJsonObject.getString("friend_name");
                            getSitterNumber = getJsonObject.getString("friend");
                            getStatus = getJsonObject.getString("status");

                            getProfile = getJsonObject.getString("friend_profile");
                            circleImage(sitter_photo, ServerUrl.getBaseUrl() + "/uploads/images/origin/" + getJsonObject.getString("friend_profile"));

                            JSONArray imageArray = new JSONArray(getJsonObject.getString("images"));
                            for (int i = 0; i < imageArray.length(); i++) {
                                DefaultSliderView sliderView = new DefaultSliderView(context);
                                sliderView.setImageUrl(ServerUrl.getBaseUrl() + "/uploads/images/origin/" + imageArray.get(i));
                                sliderView.setImageScaleType(ImageView.ScaleType.FIT_CENTER);
                                home_slider.addSliderView(sliderView);
                            }

                            if ("0".equals(getStatus)) {
                                reserve_status.setText("승인 대기");
                                reserve_status.setTextColor(getResources().getColor(R.color.starOnRed));
                                positive_con.setVisibility(View.GONE);
                                negative_con.setVisibility(View.VISIBLE);
                                negative.setText("예약 취소");
                            } else if ("1".equals(getStatus)) {
                                reserve_status.setText("승인 완료");
                                reserve_status.setTextColor(Color.GREEN);
                                positive_con.setVisibility(View.VISIBLE);
                                negative_con.setVisibility(View.VISIBLE);
                                negative.setText("예약 취소");
                                positive.setText("결제하기");
                            } else if ("2".equals(getStatus)) {
                                reserve_status.setText("결제 완료");
                                reserve_status.setTextColor(Color.MAGENTA);
                                positive_con.setVisibility(View.GONE);
                                negative_con.setVisibility(View.VISIBLE);
                                negative.setText("환불 신청");
                            } else if ("3".equals(getStatus)) {
                                reserve_status.setText("방문 중");
                                reserve_status.setTextColor(getResources().getColor(R.color.mainColor));
                                positive_con.setVisibility(View.VISIBLE);
                                negative_con.setVisibility(View.GONE);
                                positive_con.setBackgroundColor(getResources().getColor(R.color.subTitleGray));
                                positive.setText("후기 작성");
                                positive.setTextColor(getResources().getColor(R.color.infoTextGray));
                                positive_con.setClickable(false);
                                positive.setClickable(false);
                            } else if ("6".equals(getStatus)) {
                                reserve_status.setText("환불 신청");
                                reserve_status.setTextColor(Color.GREEN);
                                positive_con.setVisibility(View.GONE);
                                negative_con.setVisibility(View.GONE);
                            } else if ("7".equals(getStatus)) {
                                reserve_status.setText("환불 완료");
                                reserve_status.setTextColor(getResources().getColor(R.color.subTitleGray));
                                positive_con.setVisibility(View.VISIBLE);
                                negative_con.setVisibility(View.GONE);
                                positive.setText("다시 예약하기");
                                positive_con.setClickable(true);
                                positive.setClickable(true);
                            } else if ("4".equals(getStatus)) {
                                reserve_status.setText("방문 완료");
                                reserve_status.setTextColor(getResources().getColor(R.color.subTitleGray));
                                positive_con.setVisibility(View.VISIBLE);
                                negative_con.setVisibility(View.GONE);

                                if ("0".equals(getJsonObject.getString("reviewm"))) {
                                    positive.setText("후기 작성");
                                    positive_con.setClickable(true);
                                    positive.setClickable(true);
                                } else {
                                    positive.setText("후기 수정");
                                    positive_con.setClickable(true);
                                    positive.setClickable(true);
                                }

                            } else if ("9".equals(getStatus)) {
                                reserve_status.setText("예약 취소");
                                reserve_status.setTextColor(getResources().getColor(R.color.subTitleGray));
                                positive_con.setVisibility(View.VISIBLE);
                                negative_con.setVisibility(View.GONE);
                                positive.setText("다시 예약하기");
                                positive_con.setClickable(true);
                                positive.setClickable(true);
                            }

                            start_date.setText(getJsonObject.getString("day_start"));
                            finish_date.setText(getJsonObject.getString("day_end"));
                            start_time.setText(getJsonObject.getString("time_start") + "시");
                            finish_time.setText(getJsonObject.getString("time_end") + "시");
                            total_time.setText(getJsonObject.getString("time") + "시간");
                            total_children.setText(getJsonObject.getString("cnt") + "명");
                            expected_price.setText(getJsonObject.getString("cost") + "원");

                            int discount = Integer.parseInt(getJsonObject.getString("sale")) * 10;

                            int cost = Integer.parseInt(getJsonObject.getString("cost").replace("원", "").replace(",", ""));
                            DecimalFormat decimalFormat = new DecimalFormat("###,###");

                            if (discount == 0) {
                                sale_price.setText("없음");
                            } else {
                                sale_price.setText(discount + "%");
                                cost = (cost * (100 - discount) / 100);
                            }
                            String costStr = decimalFormat.format(cost*1.1);

                            total_amount.setText(costStr + "원");

                            bring.setText(getJsonObject.getString("have"));

                            weekday_start_time.setText(getJsonObject.getString("time1") + "시");
                            weekday_end_time.setText(getJsonObject.getString("time2") + "시");
                            weekend_start_time.setText(getJsonObject.getString("time3") + "시");
                            weekend_end_time.setText(getJsonObject.getString("time4") + "시");


                            JSONArray childArray = new JSONArray(getJsonObject.getString("child"));
                            data.clear();
                            if (childArray.length() == 0) {
                                child_list.setVisibility(View.GONE);
                            } else {
                                child_list.setVisibility(View.VISIBLE);
                                for (int i = 0; i < childArray.length(); i++) {
                                    JSONObject getChildObject = childArray.getJSONObject(i);
                                    data.add(new Child(getChildObject.getString("child"), getChildObject.getString("name"), getChildObject.getString("age"), getChildObject.getString("gender"), getChildObject.getString("image")));
                                }
                                child_list.setAdapter(childAdapter);
                                child_list.setDivider(null);
                                setListViewHeightBasedOnChildren(child_list);
                                childAdapter.notifyDataSetChanged();
                            }
                        }
                    } else if (!jsonObject.getBoolean("return")) {
                        if ("login".equals(jsonObject.getString("type"))) {
                            Toast.makeText(CheckReservationActivity.this, "로그인 해주세요 ! ", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(CheckReservationActivity.this, LoginActivity.class);
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

    private void circleImage(ImageView imageView, String getImg) {
        Log.i(TAG, " circleImage : " + getImg);
        Glide.with(context)
                .load(getImg)
                .apply(RequestOptions.bitmapTransform(new CropCircleTransformation()))
                .into(imageView);
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

    public class TwoBtnDialog extends Dialog {
        TwoBtnDialog twoBtnDialog = this;
        Context context;

        public TwoBtnDialog(final Context context, String text, final String what) {
            super(context);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialog_two_btn);
            getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            this.context = context;
            TextView title1 = (TextView) findViewById(R.id.title1);
            TextView title2 = (TextView) findViewById(R.id.title2);
            TextView btn1 = (TextView) findViewById(R.id.btn1);
            TextView btn2 = (TextView) findViewById(R.id.btn2);
            title2.setVisibility(View.GONE);
            title1.setText(text);
            btn1.setText("취소");
            btn2.setText("확인");
            btn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    twoBtnDialog.dismiss();
                }
            });
            btn2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    twoBtnDialog.dismiss();
                    Intent intent = new Intent(CheckReservationActivity.this, CancelAndRefundActivity.class);
                    intent.putExtra("what", what);
                    intent.putExtra("reserve", getReserve);
                    startActivity(intent);
                    finish();
                }
            });
        }
    }
}
