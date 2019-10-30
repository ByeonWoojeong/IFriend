package com.woojeong.global.ifriend;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
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
import com.smarteist.autoimageslider.DefaultSliderView;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderLayout;
import com.woojeong.global.ifriend.DTO.Child;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static com.woojeong.global.ifriend.GlobalApplication.setDarkMode;

public class CheckReservation2Activity extends AppCompatActivity {
    private static String TAG = "CheckReservation2Activity";
    Context context;
    InputMethodManager ipmm;
    AQuery aQuery = null;
    OneBtnDialog oneBtnDialog;
    TwoBtnDialog twoBtnDialog;
    FinishDialog finishDialog;

    FrameLayout back_con;
    ImageView back, photo, more;
    ScrollView scrollView;
    TextView name, start_date, finish_date, start_time, finish_time, total_time, total_children, total_amount, bring, ok;
    LinearLayout sitter_info_con, ok_con;

    ListView child_list;
    ArrayList<Child> child;
    ChildAdapter childAdapter;

    Intent getIntent;
    String getReserve, getStatus, getWhat, getMomNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_reservation2);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#ffffff"));
            window.setBackgroundDrawable(null);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                setDarkMode(CheckReservation2Activity.this, true);
            }
        }

        getIntent = getIntent();
        getReserve = getIntent.getStringExtra("reserve");

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
        start_date = findViewById(R.id.start_date);
        finish_date = findViewById(R.id.finish_date);
        start_time = findViewById(R.id.start_time);
        finish_time = findViewById(R.id.finish_time);
        total_time = findViewById(R.id.total_time);
        total_children = findViewById(R.id.total_children);
        total_amount = findViewById(R.id.total_amount);
        bring = findViewById(R.id.bring);

        child_list = findViewById(R.id.child_list);
        child = new ArrayList<Child>();
        childAdapter = new ChildAdapter(CheckReservation2Activity.this, R.layout.list_child_item, child, child_list, "profile");

        scrollView.scrollTo(0, 0);

        more = findViewById(R.id.more);
        sitter_info_con = findViewById(R.id.sitter_info_con);
        ok_con = findViewById(R.id.ok_con);
        ok = findViewById(R.id.ok);

        sitter_info_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                more.callOnClick();
            }
        });
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CheckReservation2Activity.this, CustomerProfileActivity.class);
                intent.putExtra("member", getMomNumber);
                startActivity(intent);
            }
        });

        ok_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ok.callOnClick();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences get_token = getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
                final String getToken = get_token.getString("Token", "");
                final String url = ServerUrl.getBaseUrl() + "/reserve/approve";
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("reserve", getReserve);
                aQuery.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
                    @Override
                    public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
                        Log.i(TAG, " " + jsonObject);
                        try {
                            if (jsonObject.getBoolean("return")) {    //return이 true 면?

                                finishDialog = new FinishDialog(CheckReservation2Activity.this, "승인 완료 !\n고객님께서 결제 후 예약이 완료됩니다.", "확인");
                                finishDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                finishDialog.setCancelable(false);
                                finishDialog.show();
                                return;

                            } else if (!jsonObject.getBoolean("return")) {
                                if ("login".equals(jsonObject.getString("type"))) {
                                    Toast.makeText(CheckReservation2Activity.this, "로그인 해주세요 ! ", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(CheckReservation2Activity.this, LoginActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    oneBtnDialog = new OneBtnDialog(CheckReservation2Activity.this, "예약 승인 실패", "확인");
                                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                    oneBtnDialog.setCancelable(false);
                                    oneBtnDialog.show();
                                    return;
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }.header("EPOCH-AGENT", "" + getToken).header("User-Agent", "android"));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewSettings();
    }

    void viewSettings() {
        SharedPreferences get_token = getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
        final String getToken = get_token.getString("Token", "");
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
                            oneBtnDialog = new OneBtnDialog(CheckReservation2Activity.this, "예약 정보가 없습니다 !", "확인");
                            oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            oneBtnDialog.setCancelable(false);
                            oneBtnDialog.show();
                            return;
                        } else {
                            JSONObject getJsonObject = jsonArray.getJSONObject(0);
                            name.setText(getJsonObject.getString("mother_name"));
                            getMomNumber = getJsonObject.getString("mother");
                            getStatus = getJsonObject.getString("status");

                            circleImage(photo, ServerUrl.getBaseUrl() + "/uploads/images/origin/" + getJsonObject.getString("mother_profile"));

                            // 0승인대기 1승인완료 2결제완료 3방문중 4방문완료 6환불신청 7환불완료 9예약취소
                            if ("1".equals(getJsonObject.getString("status"))) {
                                ok.setText("결제 대기 중 입니다.");
                                ok_con.setEnabled(false);
                                ok.setEnabled(false);
                                ok_con.setBackgroundColor(getResources().getColor(R.color.subTitleGray));
                            } else if ("2".equals(getJsonObject.getString("status"))) {
                                ok.setText("결제 완료되었습니다.");
                                ok_con.setEnabled(false);
                                ok.setEnabled(false);
                                ok_con.setBackgroundColor(getResources().getColor(R.color.subTitleGray));
                            } else if ("3".equals(getJsonObject.getString("status"))) {
                                ok.setText("방문 중 입니다.");
                                ok_con.setEnabled(false);
                                ok.setEnabled(false);
                                ok_con.setBackgroundColor(getResources().getColor(R.color.subTitleGray));
                            } else if ("4".equals(getJsonObject.getString("status"))) {
                                ok.setText("방문 완료");
                                ok_con.setEnabled(false);
                                ok.setEnabled(false);
                                ok_con.setBackgroundColor(getResources().getColor(R.color.subTitleGray));
                            }

                            start_date.setText(getJsonObject.getString("day_start"));
                            finish_date.setText(getJsonObject.getString("day_end"));
                            start_time.setText(getJsonObject.getString("time_start") + "시");
                            finish_time.setText(getJsonObject.getString("time_end") + "시");
                            total_time.setText(getJsonObject.getString("time") + "시간");
                            total_children.setText(getJsonObject.getString("cnt") + "명");
                            total_amount.setText(getJsonObject.getString("cost") + "원");
                            bring.setText(getJsonObject.getString("etc"));

                            int discount = Integer.parseInt(getJsonObject.getString("sale")) * 10;

                            JSONArray childArray = new JSONArray(getJsonObject.getString("child"));
                            child_list.setVisibility(View.VISIBLE);
                            child.clear();
                            if (childArray.length() == 0) {
                            } else {
                                Log.i(TAG, " is Child ");
                                for (int i = 0; i < childArray.length(); i++) {
                                    JSONObject getChildObject = childArray.getJSONObject(i);
                                    child.add(new Child(getChildObject.getString("child"), getChildObject.getString("name"), getChildObject.getString("age"), getChildObject.getString("gender"), getChildObject.getString("image")));
                                }
                                child_list.setAdapter(childAdapter);
                                child_list.setDivider(null);
                                setListViewHeightBasedOnChildren(child_list);
                                childAdapter.notifyDataSetChanged();
                            }
                        }
                    } else if (!jsonObject.getBoolean("return")) {
                        if ("login".equals(jsonObject.getString("type"))) {
                            Toast.makeText(CheckReservation2Activity.this, "로그인 해주세요 ! ", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(CheckReservation2Activity.this, LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            oneBtnDialog = new OneBtnDialog(CheckReservation2Activity.this, "예약 정보 불러오기를\n실패하였습니다.", "확인");
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

    public class FinishDialog extends Dialog {
        FinishDialog finishDialog = this;
        Context context;

        public FinishDialog(final Context context, final String text, final String btnText) {
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
                    finishDialog.dismiss();
                    Intent intent = new Intent(CheckReservation2Activity.this, Main2Activity.class);
                    startActivity(intent);
                    finish();
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
                    Intent intent = new Intent(CheckReservation2Activity.this, CancelAndRefundActivity.class);
                    intent.putExtra("what", what);
                    intent.putExtra("reserve", getReserve);
                    startActivity(intent);
                    finish();
                }
            });
        }
    }
}
