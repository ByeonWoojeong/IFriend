package com.woojeong.global.ifriend;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.woojeong.global.ifriend.GlobalApplication.setDarkMode;


public class Main2Activity extends AppCompatActivity {
    private static String TAG = "Main2Activity";

    public static Context context;
    String token, mode; //sitter, i
    BackPressCloseHandler backPressCloseHandler;
    InputMethodManager ipmm;
    Parcelable state;
    TabLayout tabLayout;
    String tabChoice = "1";
    BadgeView badge;
    Typeface typeface;
    View viewReservation, viewSchedule, viewChatting, viewJournal, viewSetting, tab_badge_con;
    ViewPager viewPager;
    FragmentManager fragmentManager;
    Main2PagerAdapter adapter;
    AQuery aQuery = null;
    AlphaAnimation fade_in, fade_out;
    WebView webView;

    OneBtnDialog oneBtnDialog;
    TwoBtnDialog twoBtnDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#ffffff"));
            window.setBackgroundDrawable(null);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                setDarkMode(Main2Activity.this, true);
            }
        }

        context = this;
        SharedPreferences prefToken = getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
        token = prefToken.getString("Token", "");
        aQuery = new AQuery(this);
        fade_in = new AlphaAnimation(0.0f, 1.0f);
        fade_out = new AlphaAnimation(1.0f, 0.0f);
        fade_in.setDuration(300);
        fade_out.setDuration(300);
        backPressCloseHandler = new BackPressCloseHandler(this);
        //뒤로가기(BackButton Listener) : backPressCloseHandler.onBackPressed();
        ipmm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        //키보드 숨기기 : ipmm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);

        // 1
        tabLayout = (TabLayout) findViewById(R.id.main_tabs2);
        viewReservation = getLayoutInflater().inflate(R.layout.custom_tab_main2, null);
        viewReservation.findViewById(R.id.icon2).setBackgroundResource(R.drawable.main2_reservation);
        tabLayout.addTab(tabLayout.newTab().setCustomView(viewReservation));

        // 2
        viewSchedule = getLayoutInflater().inflate(R.layout.custom_tab_main2, null);
        viewSchedule.findViewById(R.id.icon2).setBackgroundResource(R.drawable.main2_schedule);
        tabLayout.addTab(tabLayout.newTab().setCustomView(viewSchedule));

        // 3
        viewChatting = getLayoutInflater().inflate(R.layout.custom_tab_main2, null);
        viewChatting.findViewById(R.id.icon2).setBackgroundResource(R.drawable.main2_chatting);
        tab_badge_con = viewChatting.findViewById(R.id.tab_badge_con2);
        badge = new BadgeView(Main2Activity.this, tab_badge_con);
        badge.setTypeface(typeface);
        badge.setTextColor(Color.parseColor("#ffffff"));
        badge.setTextSize(8);
        badge.setPadding(10, 3, 10, 3);
//        badge.setBackgroundResource(R.drawable.red_circle);
        badge.setBadgeBackgroundColor(Color.parseColor("#ff0000"));
        badge.setBadgePosition(BadgeView.POSITION_CENTER, 0);
        tabLayout.addTab(tabLayout.newTab().setCustomView(viewChatting));

        // 4
        viewJournal = getLayoutInflater().inflate(R.layout.custom_tab_main2, null);
        viewJournal.findViewById(R.id.icon2).setBackgroundResource(R.drawable.main2_journal);
        tabLayout.addTab(tabLayout.newTab().setCustomView(viewJournal));

        // 5
        viewSetting = getLayoutInflater().inflate(R.layout.custom_tab_main2, null);
        viewSetting.findViewById(R.id.icon2).setBackgroundResource(R.drawable.main2_setting);
        tabLayout.addTab(tabLayout.newTab().setCustomView(viewSetting));

        tabLayout.getTabAt(0).setIcon(R.drawable.main2_reservation_on);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = (ViewPager) findViewById(R.id.main_viewpager);
        fragmentManager = getSupportFragmentManager();
        adapter = new Main2PagerAdapter(this, fragmentManager, tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.setOffscreenPageLimit(4);
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                ipmm.hideSoftInputFromWindow(viewPager.getWindowToken(), 0);
                if (tab.getPosition() == 0) {
                    viewReservation.setSelected(true);
                    viewSchedule.setSelected(false);
                    viewChatting.setSelected(false);
                    viewJournal.setSelected(false);
                    viewSetting.setSelected(false);
                    tabChoice = "1";
                } else if (tab.getPosition() == 1) {
                    viewReservation.setSelected(false);
                    viewSchedule.setSelected(true);
                    viewChatting.setSelected(false);
                    viewJournal.setSelected(false);
                    viewSetting.setSelected(false);
                    tabChoice = "2";
                    SharedPreferences pref = Main2Activity.this.getSharedPreferences("pref", Activity.MODE_PRIVATE);
                    boolean getLoginChecked = pref.getBoolean("loginChecked", false);
                    if (!getLoginChecked) {
//                        Intent getIntent = new Intent(MainActivity.this, LoginActivity.class);
//                        startActivityForResult(getIntent, 1);
                        tabChoice = "1";
//                        getViewPager().setCurrentItem(0);
                    }
                } else if (tab.getPosition() == 2) {
                    viewReservation.setSelected(false);
                    viewSchedule.setSelected(false);
                    viewChatting.setSelected(true);
                    viewJournal.setSelected(false);
                    viewSetting.setSelected(false);
                    Main2ThirdFragment fragment = (Main2ThirdFragment) adapter.getFragment(2);
                    if (fragment != null) {
                        fragment.reload();
                    }
                    tabChoice = "3";
                    SharedPreferences pref = Main2Activity.this.getSharedPreferences("pref", Activity.MODE_PRIVATE);
                    boolean getLoginChecked = pref.getBoolean("loginChecked", false);
                    if (!getLoginChecked) {
//                        Intent getIntent = new Intent(MainActivity.this, LoginActivity.class);
//                        startActivityForResult(getIntent, 1);
                        tabChoice = "1";
//                        getViewPager().setCurrentItem(0);
                    } else {
                        if ("sitter".equals(mode)) {
                            MainThirdFragment thirdFragment = (MainThirdFragment) fragmentManager.findFragmentByTag("third");
//                            thirdFragment.refresh_data();
                        } else if ("i".equals(mode)) {
                            MainThirdFragment thirdFragment2 = (MainThirdFragment) fragmentManager.findFragmentByTag("third2");
//                            thirdFragment2.refresh_data();
                        }
                    }
                } else if (tab.getPosition() == 3) {
                    viewReservation.setSelected(false);
                    viewSchedule.setSelected(false);
                    viewChatting.setSelected(false);
                    viewJournal.setSelected(true);
                    viewSetting.setSelected(false);
                    Main2FourthFragment fragment = (Main2FourthFragment) adapter.getFragment(3);
                    if (fragment != null) {
                        fragment.reload();
                    }
                    tabChoice = "4";
                    SharedPreferences pref = Main2Activity.this.getSharedPreferences("pref", Activity.MODE_PRIVATE);
                    boolean getLoginChecked = pref.getBoolean("loginChecked", false);
                    if (!getLoginChecked) {
//                        Intent getIntent = new Intent(MainActivity.this, LoginActivity.class);
//                        startActivityForResult(getIntent, 1);
                        tabChoice = "1";
//                        getViewPager().setCurrentItem(0);
                    } else {

                    }
                } else if (tab.getPosition() == 4) {
                    viewReservation.setSelected(false);
                    viewSchedule.setSelected(false);
                    viewChatting.setSelected(false);
                    viewJournal.setSelected(false);
                    viewSetting.setSelected(true);
                    SharedPreferences pref = Main2Activity.this.getSharedPreferences("pref", Activity.MODE_PRIVATE);
                    boolean getLoginChecked = pref.getBoolean("loginChecked", false);
                    if (!getLoginChecked) {
//                        Intent getIntent = new Intent(MainActivity.this, LoginActivity.class);
//                        startActivityForResult(getIntent, 1);
                        tabChoice = "1";
//                        getViewPager().setCurrentItem(0);
                    } else {

                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    tabChoice = "1";
                } else if (tab.getPosition() == 1) {
                    SharedPreferences pref = Main2Activity.this.getSharedPreferences("pref", Activity.MODE_PRIVATE);
                    boolean getLoginChecked = pref.getBoolean("loginChecked", false);
                    if (!getLoginChecked) {
//                        Intent getIntent = new Intent(MainActivity.this, LoginActivity.class);
//                        startActivityForResult(getIntent, 1);
                        tabChoice = "1";
//                        getViewPager().setCurrentItem(0);
                    } else {
                        tabChoice = "2";
                    }
                } else if (tab.getPosition() == 2) {
                    SharedPreferences pref = Main2Activity.this.getSharedPreferences("pref", Activity.MODE_PRIVATE);
                    boolean getLoginChecked = pref.getBoolean("loginChecked", false);
                    if (!getLoginChecked) {
//                        Intent getIntent = new Intent(MainActivity.this, LoginActivity.class);
//                        startActivityForResult(getIntent, 1);
                        tabChoice = "1";
//                        getViewPager().setCurrentItem(0);
                    } else {
                        tabChoice = "3";
                    }
                } else if (tab.getPosition() == 3) {
                    SharedPreferences pref = Main2Activity.this.getSharedPreferences("pref", Activity.MODE_PRIVATE);
                    boolean getLoginChecked = pref.getBoolean("loginChecked", false);
                    if (!getLoginChecked) {
//                        Intent getIntent = new Intent(MainActivity.this, LoginActivity.class);
//                        startActivityForResult(getIntent, 1);
                        tabChoice = "1";
//                        getViewPager().setCurrentItem(0);
                    } else {
                        tabChoice = "4";
                    }
                } else if (tab.getPosition() == 4) {
                    SharedPreferences pref = Main2Activity.this.getSharedPreferences("pref", Activity.MODE_PRIVATE);
                    boolean getLoginChecked = pref.getBoolean("loginChecked", false);
                    if (!getLoginChecked) {
//                        Intent getIntent = new Intent(MainActivity.this, LoginActivity.class);
//                        startActivityForResult(getIntent, 1);
                        tabChoice = "1";
//                        getViewPager().setCurrentItem(0);
                    } else {
                        tabChoice = "5";
                    }
                }
            }
        });

        tabLayout.setBackgroundColor(getResources().getColor(R.color.mainTextGray));

        //다이얼로그1
//        oneBtnDialog = new OneBtnDialog(MainActivity.this, "내용", "확인");
//        oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        oneBtnDialog.setCancelable(false);
//        oneBtnDialog.show();

    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
        registerReceiver(messageReceiver, new IntentFilter("ifriend_push"));
        setBadge();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(messageReceiver);
    }

    @Override
    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    public ViewPager getViewPager() {
        if (null == viewPager) {
            viewPager = findViewById(R.id.main_viewpager);
        }
        final MainPagerAdapter adapter = new MainPagerAdapter(this, getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        return viewPager;
    }

    BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            setBadge();
            Main2ThirdFragment thirdFragment = (Main2ThirdFragment) fragmentManager.findFragmentByTag("third");
            thirdFragment.pushOn();
        }
    };

    void setBadge() {
        Log.i(TAG, " setBadge() ");
        String url = ServerUrl.getBaseUrl() + "/chat/mlist";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("hidden", "0");
        Log.i(TAG, " params " + params);
        aQuery.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
                try {
                    Log.i(TAG, " jsonObject " + jsonObject);
                    JSONArray jsonArray = new JSONArray(jsonObject.getString("list"));

                    if (jsonArray.length() != 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject getJsonObject = jsonArray.getJSONObject(i);
                            if ("1".equals(getJsonObject.getString("new"))) {
                                Log.i(TAG, " new " + getJsonObject.getString("new"));
                                badge.setText("N");
                                badge.show();
                                badge.setVisibility(View.VISIBLE);
//                                viewChatting.setBackgroundColor(Color.parseColor("#ff0000"));
                            } else {
                                Log.i(TAG, " new " + getJsonObject.getString("new"));
                                badge.hide();
                                badge.setVisibility(View.GONE);
//                                viewChatting.setBackgroundColor(Color.TRANSPARENT);
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.header("EPOCH-AGENT", "" + token).header("User-Agent", "android"));

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

        public TwoBtnDialog(final Context context, final String address, final String la, final String lo) {
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
            title1.setText(address);
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
//                    Intent intent = new Intent(MainActivity.this, WriteActivity.class);
//                    intent.putExtra("addr", address);
//                    intent.putExtra("la", la);
//                    intent.putExtra("lo", lo);
//                    startActivityForResult(intent, 1);
                }
            });
        }
    }
}
