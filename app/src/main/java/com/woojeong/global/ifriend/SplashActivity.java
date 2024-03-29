package com.woojeong.global.ifriend;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.google.firebase.iid.FirebaseInstanceId;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.LogManager;

import static com.woojeong.global.ifriend.GlobalApplication.getOtherLogin;

public class SplashActivity extends AppCompatActivity {
    private static String TAG = "SplashActivity";
    private Context context;
    InputMethodManager methodManager;
    String token, beforeLogout, mode;
    AQuery aQuery = null;
    SharedPreferences isFirst;
    SharedPreferences prefLoginChecked;
    boolean getIsFirst;


    Handler handler = new Handler() {
        public void handleMessage(Message msg){

        }
    };

    Runnable token_load = new Runnable() {
        public void run() {
            SharedPreferences prefToken = getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
            token = prefToken.getString("Token", "");
            handler.postDelayed(this, 500);
            if (!"".equals(token.toString())){
                finish();
            }
        }
    };


    @SuppressWarnings("handlerLeak")
    Handler toastHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Toast.makeText(getApplicationContext(), "최신 버전으로 업데이트 해주세요.", Toast.LENGTH_SHORT).show();
        }
    };

//    @SuppressWarnings("handlerLeak")
//    Handler handler = new Handler(){
//        public void handleMessage(Message msg){
//            SharedPreferences autoLogin = SplashActivity.this.getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
//            String getAutoLogin = autoLogin.getString("autoLogin", "0");
//            if ("1".equals(getAutoLogin+"")) {
//                SharedPreferences pref = SplashActivity.this.getSharedPreferences("pref", Activity.MODE_PRIVATE);
//                boolean getLoginChecked = pref.getBoolean("loginChecked", false);
//                if (getLoginChecked) {
//                    final String getMode = pref.getString("mode", "");
//                    SharedPreferences prefToken = getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
//                    String token = prefToken.getString("Token", "");
//                    String url = ServerUrl.getBaseUrl() + "/main/logincheck";
//                    Map<String, Object> params = new HashMap<String, Object>();
//                    aQuery.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
//                        @Override
//                        public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
//                            Log.i(TAG, " " + jsonObject);
//                            try {
//                                if ("success".equals(jsonObject.getString("return"))) {
//                                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
//                                    intent.putExtra("mode", getMode);
//                                    finish();
//                                    startActivityForResult(intent, 1);
//                                } else if ("fail".equals(jsonObject.getString("return"))) {
//                                    Toast.makeText(SplashActivity.this, "다른 기기에서 접속을 시도하여 로그아웃을 합니다.", Toast.LENGTH_SHORT).show();
//                                    SharedPreferences pref = SplashActivity.this.getSharedPreferences("pref", Activity.MODE_PRIVATE);
//                                    SharedPreferences.Editor editor = pref.edit();
//                                    editor.clear();
//                                    editor.commit();
//                                    SharedPreferences autoLogin = SplashActivity.this.getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
//                                    SharedPreferences.Editor editor2 = autoLogin.edit();
//                                    editor2.clear();
//                                    editor2.commit();
//
//                                    isFirst = SplashActivity.this.getSharedPreferences("isFirst", Activity.MODE_PRIVATE);
//                                    getIsFirst = isFirst.getBoolean("isFirst", true);
//                                    if (getIsFirst) {
//                                        Intent intent = new Intent(SplashActivity.this, TutorialActivity.class);
//                                        startActivity(intent);
//                                        finish();
//                                    } else {
//                                        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
//                                        startActivity(intent);
//                                        finish();
//                                    }
//
////                                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
////                                    startActivity(intent);
////                                    finish();
//                                }
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }.header("EPOCH-AGENT", "" + token).header("User-Agent", "android"));
//                }
//            } else {
//                SharedPreferences pref = SplashActivity.this.getSharedPreferences("pref", Activity.MODE_PRIVATE);
//                SharedPreferences.Editor editor = pref.edit();
//                editor.clear();
//                editor.commit();
//
//                SharedPreferences.Editor editor2 = autoLogin.edit();
//                editor2.clear();
//                editor2.commit();
//
//                isFirst = SplashActivity.this.getSharedPreferences("isFirst", Activity.MODE_PRIVATE);
//                getIsFirst = isFirst.getBoolean("isFirst", true);
//                if (getIsFirst) {
//                    Intent intent = new Intent(SplashActivity.this, TutorialActivity.class);
//                    startActivity(intent);
//                    finish();
//                } else {
//                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
//                    startActivity(intent);
//                    finish();
//                }
//            }
//        }
//    };

    @Override
    @SuppressWarnings("deprecation")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        methodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        methodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        setContentView(R.layout.activity_splash);
        Intent intent = getIntent();
        beforeLogout = intent.getStringExtra("beforeLogout");
        aQuery = new AQuery(this);
        context = this.getBaseContext();
        ConnectivityManager manager = (ConnectivityManager) SplashActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifi.isConnected() || mobile.isConnected()) {
            Log.i(TAG, "인터넷이 연결되어 있습니다.");
        } else {
            Toast.makeText(SplashActivity.this, "인터넷이 연결되어 있지 않아 앱을 종료합니다.", Toast.LENGTH_SHORT).show();
            moveTaskToBack(true);
            ActivityCompat.finishAffinity(this);
            token_load.run();
        }
        registerForContextMenu(findViewById(R.id.splash_img));   //지정된 뷰에 대해 표시할 컨텍스트 메뉴를 등록.

        final PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String store_version = MarketVersionChecker.getMarketVersionFast(getPackageName());
                        if (store_version != null){
                            try {
                                String device_version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
                                if (store_version.compareTo(device_version) > 0) {
                                    Intent marketLaunch = new Intent(Intent.ACTION_VIEW);
                                    marketLaunch.setData(Uri.parse("market://details?id=com.woojeong.global.ifriend"));
                                    startActivity(marketLaunch);
                                    Message msg = toastHandler.obtainMessage();
                                    toastHandler.sendMessage(msg);
                                    moveTaskToBack(true);
                                    ActivityCompat.finishAffinity(SplashActivity.this);
                                    finish();
                                } else {
//                                    handler.sendEmptyMessageDelayed(0,500);
                                    autoLogin();
                                }
                            } catch (PackageManager.NameNotFoundException e) {
                                e.printStackTrace();
                            }
                        } else {
//                            handler.sendEmptyMessageDelayed(0,500);

                            autoLogin();
                        }
                    }
                }).start();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {

            }
        };

        TedPermission.with(SplashActivity.this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("권한을 허용하지 않으면\n서비스를 이용할 수 없습니다.\n설정 > 권한")
                .setPermissions(android.Manifest.permission.READ_CONTACTS, android.Manifest.permission.READ_PHONE_STATE, android.Manifest.permission.CAMERA, android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.ACCESS_FINE_LOCATION)
                .check();

       SharedPreferences filteringData = getSharedPreferences("filtering", Activity.MODE_PRIVATE);
        SharedPreferences.Editor filterEditor = filteringData.edit();
        filterEditor.clear();
        filterEditor.commit();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getOtherLogin) {
            getOtherLogin = false;
            moveTaskToBack(true);
            ActivityCompat.finishAffinity(this);
            finish();
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    void autoLogin(){
        final SharedPreferences get_token = getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
        String getToken = get_token.getString("Token", "");
        final String url = ServerUrl.getBaseUrl() + "/main/logincheck";
        Map<String, Object> params = new HashMap<String, Object>();
        aQuery.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
                Log.i(TAG, " " + jsonObject);
                try {
                    if (jsonObject.getBoolean("return")) {    //return이 true 면?

                        prefLoginChecked = context.getSharedPreferences("modeChecked", Activity.MODE_PRIVATE);
                        mode = prefLoginChecked.getString("mode", "");
                        if("sitter".equals(mode)){
                            Intent intent = new Intent(SplashActivity.this, Main2Activity.class);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                            startActivity(intent);
                        }

                    } else {
                        isFirst = SplashActivity.this.getSharedPreferences("isFirst", Activity.MODE_PRIVATE);
                        getIsFirst = isFirst.getBoolean("isFirst", true);
                        if (getIsFirst) {
                            Intent intent = new Intent(SplashActivity.this, TutorialActivity.class);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }
                    }
                    token_load.run();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.header("EPOCH-AGENT", "" + getToken).header("User-Agent", "android"));
    }
}
