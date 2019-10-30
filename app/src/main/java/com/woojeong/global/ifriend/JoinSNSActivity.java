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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.woojeong.global.ifriend.DTO.SearchAddress;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import static com.woojeong.global.ifriend.GlobalApplication.setDarkMode;

public class JoinSNSActivity extends AppCompatActivity {
    private static String TAG = "JoinSNSActivity";

    Context context;
    AQuery aQuery = null;
    InputMethodManager ipm;
    ImageView back;
    OneBtnDialog oneBtnDialog;
    EditText name, birth, password, re_password, phone, number, address_detail;
    TextView man, woman, password_condition, certify1, certify2, phone_condition, certify_condition, address_btn, address1, service_info, privacy, next;
    CheckBox check1, check2;
    FrameLayout next_con, back_con;

    String getToken;
    String lat = "";
    String lng = "";
    String addr = "";
    String addr1 = "";
    WebView webView;
    Handler handler = new Handler();
    TwoBtnDialog twoBtnDialog;
    boolean isCertify1;
    boolean isClick;
    int secCount;
    Runnable count = new Runnable() {
        public void run() {
            secCount--;
            handler.postDelayed(this, 1000);
            if (secCount < 0) {
                handler.removeCallbacks(count);
                isClick = false;
            }
        }
    };

    int gender = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_sns);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#ffffff"));
            window.setBackgroundDrawable(null);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                setDarkMode(JoinSNSActivity.this, true);
            }
        }

        SharedPreferences get_token = getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
        getToken = get_token.getString("Token", "");

        ipm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        aQuery = new AQuery(this);
        back_con = findViewById(R.id.back_con);
        back = findViewById(R.id.back);
        name = findViewById(R.id.name);
        birth = findViewById(R.id.birth);
        password = findViewById(R.id.password);
        re_password = findViewById(R.id.re_password);
        man = findViewById(R.id.man);
        woman = findViewById(R.id.woman);
        password_condition = findViewById(R.id.password_condition);
        certify1 = findViewById(R.id.certify1);
        address1 = findViewById(R.id.address1);
        address_btn = findViewById(R.id.address_btn);
        address1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                address_btn.callOnClick();
            }
        });
        address_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.setVisibility(View.VISIBLE);
                webView.loadUrl(ServerUrl.getBaseUrl() + "/search");
            }
        });

        certify1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ipm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
                webView.setVisibility(View.VISIBLE);
                webView.loadUrl(ServerUrl.getBaseUrl() + "/join/nicecert");
            }
        });

        address_detail = findViewById(R.id.address_detail);
        check1 = findViewById(R.id.check1);
        check2 = findViewById(R.id.check2);
        service_info = findViewById(R.id.service_info);
        privacy = findViewById(R.id.privacy);
        next_con = findViewById(R.id.next_con);
        next = findViewById(R.id.next);

        back_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back.callOnClick();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backIntent = new Intent(JoinSNSActivity.this, LoginActivity.class);
                startActivity(backIntent);
                finish();
            }
        });
        man.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                man.setBackground(getResources().getDrawable(R.drawable.border_radius_yellow));
                man.setTextColor(getResources().getColor(R.color.mainColor));
                man.setSelected(true);
                woman.setBackground(getResources().getDrawable(R.drawable.border_radius1));
                woman.setTextColor(getResources().getColor(R.color.editHintGray));
                woman.setSelected(false);
                gender = 1;
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
                gender = 2;
            }
        });


        service_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(JoinSNSActivity.this, TermsDetailsActivity.class);
                intent.putExtra("what", "서비스이용약관");
                startActivity(intent);
            }
        });

        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(JoinSNSActivity.this, TermsDetailsActivity.class);
                intent.putExtra("what", "개인정보처리방침");
                startActivity(intent);
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
                ipm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);

                if ("".equals(name.getText().toString())) {
                    oneBtnDialog = new OneBtnDialog(JoinSNSActivity.this, "이름(실명)을\n입력해주세요 !\n예) 홍길동", "확인");
                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    oneBtnDialog.setCancelable(false);
                    oneBtnDialog.show();
                    return;
                } else if (!(birth.length() == 8)) {
                    oneBtnDialog = new OneBtnDialog(JoinSNSActivity.this, "생년월일을\n다시 입력해주세요 !\n예) 880509", "확인");
                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    oneBtnDialog.setCancelable(false);
                    oneBtnDialog.show();
                    return;
                } else if (!(man.isSelected() || woman.isSelected())) {
                    oneBtnDialog = new OneBtnDialog(JoinSNSActivity.this, "성별을\n선택해주세요 !", "확인");
                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    oneBtnDialog.setCancelable(false);
                    oneBtnDialog.show();
                    return;
                } else if ("주소 검색".equals(address1.getText().toString())) {
                    oneBtnDialog = new OneBtnDialog(JoinSNSActivity.this, "검색 버튼으로\n주소를 입력해주세요 !", "확인");
                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    oneBtnDialog.setCancelable(false);
                    oneBtnDialog.show();
                    return;
                } else if (!isCertify1) {
                    oneBtnDialog = new OneBtnDialog(JoinSNSActivity.this, "본인 인증을 완료해주세요 !", "확인");
                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    oneBtnDialog.setCancelable(false);
                    oneBtnDialog.show();
                    return;
                } else if ("".equals(address_detail.getText().toString())) {
                    oneBtnDialog = new OneBtnDialog(JoinSNSActivity.this, "상세 주소를\n입력해주세요 !\n예) 3동 101호", "확인");
                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    oneBtnDialog.setCancelable(false);
                    oneBtnDialog.show();
                    return;
                } else if (!check1.isChecked()) {
                    oneBtnDialog = new OneBtnDialog(JoinSNSActivity.this, "이용 약관에 동의해주세요 !", "확인");
                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    oneBtnDialog.setCancelable(false);
                    oneBtnDialog.show();
                    return;
                }  else if (!check2.isChecked()) {
                    oneBtnDialog = new OneBtnDialog(JoinSNSActivity.this, "개인 정보 처리 방침에\n동의해주세요 !", "확인");
                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    oneBtnDialog.setCancelable(false);
                    oneBtnDialog.show();
                    return;
                }

                String url = ServerUrl.getBaseUrl() + "/join/profile";
                final Map<String, Object> params = new HashMap<String, Object>();
                params.put("name", name.getText().toString());
                params.put("birth", birth.getText().toString());
                params.put("gender", gender);
                params.put("addr", addr);
                params.put("addr1", addr1);
                params.put("addr2", address_detail.getText().toString());
                params.put("lat", lat);
                params.put("lng", lng);
                Log.i(TAG, " " + params);
                aQuery.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
                    @Override
                    public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
                        Log.i(TAG, " " + jsonObject);
                        try {
                            if (jsonObject.getBoolean("return")) {    //return이 true 면?
                                SharedPreferences prefLoginChecked = getSharedPreferences("prefLoginChecked", Activity.MODE_PRIVATE);
                                SharedPreferences.Editor editor = prefLoginChecked.edit();
                                editor.putBoolean("loginChecked", true);
                                editor.putString("type", "kakao");
                                editor.commit();
                                Intent intent = new Intent(JoinSNSActivity.this, MainActivity.class);
                                setResult(999);
                                finish();
                                startActivityForResult(intent, 1);

                            } else if (!jsonObject.getBoolean("return")) {
                                oneBtnDialog = new OneBtnDialog(JoinSNSActivity.this, "가입에 실패하였습니다.", "확인");
                                oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                oneBtnDialog.setCancelable(false);
                                oneBtnDialog.show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }.header("EPOCH-AGENT", "" + getToken).header("User-Agent", "android"));
            }
        });

        webView = findViewById(R.id.webView);
        webView.addJavascriptInterface(new AndroidBridge(), "ichingu");
        String userAgent = new WebView(getBaseContext()).getSettings().getUserAgentString();
        webView.getSettings().setUserAgentString(userAgent + " epochcorp{" + getToken +"}" );
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setGeolocationEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(false);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setDatabaseEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);
        webView.getSettings().setBuiltInZoomControls(true);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
            webView.getSettings().setDisplayZoomControls(false);
        }
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            webView.getSettings().setTextZoom(100);
        }
        webView.setWebViewClient(new MyWebViewClient());
        webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                super.onGeolocationPermissionsShowPrompt(origin, callback);
                Log.i(TAG, "onGeolocationPermissionsShowPrompt");
                callback.invoke(origin, true, false);
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                Log.i(TAG, "onJsAlert");
                WebDialog webDialog = new WebDialog();
                Dialog dialog = webDialog.alertDialog(JoinSNSActivity.this, message, result);
                dialog.show();
                return true;
            }

            @Override
            public void onCloseWindow(WebView w) {
                super.onCloseWindow(w);
                finish();
            }

            @Override
            public boolean onCreateWindow(WebView view, boolean dialog, boolean userGesture, Message resultMsg) {
                final WebSettings settings = view.getSettings();
                settings.setDomStorageEnabled(true);
                settings.setJavaScriptEnabled(true);
                settings.setAllowFileAccess(true);
                settings.setAllowContentAccess(true);
                view.setWebChromeClient(this);
                WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
                transport.setWebView(view);
                resultMsg.sendToTarget();
                return false;
            }

            @Override
            public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
                Log.i(TAG, "onJsConfirm");
                WebDialog webDialog = new WebDialog();
                Dialog dialog = webDialog.confirmDialog(JoinSNSActivity.this, message, result);
                dialog.show();
                return true;
            }
        });
    }

    @Override
    public void finish() {
        if(webView.getVisibility() == View.VISIBLE){
            webView.setVisibility(View.GONE);
        } else {
            Intent intent = new Intent(JoinSNSActivity.this, LoginActivity.class);
            startActivity(intent);
            super.finish();
        }
    }

    boolean checkPhoneNumber(String number) {    //핸드폰 번호 형식 체크
        boolean checkPhoneNumber = Pattern.matches("(01[016789])(\\d{3,4})(\\d{4})", number);
        return checkPhoneNumber;
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

        public TwoBtnDialog(final Context context, final String addr, final String addr1, final String lng, final String lat) {
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
            title1.setText(addr1);
            btn1.setText("취소");
            btn2.setText("등록");
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
                    webView.setVisibility(View.GONE);
                    address1.setText(addr1);
                    address1.setTextColor(getResources().getColor(R.color.mainTextGray));
                }
            });
        }
    }

    class MyWebViewClient extends WebViewClient {
        public boolean doFallback(WebView view, Intent parsedIntent) {
            if (parsedIntent == null) {
                return false;
            }
            String fallbackUrl = parsedIntent.getStringExtra("browser_fallback_url");
            if (fallbackUrl != null) {
                view.loadUrl(fallbackUrl);
                return true;
            }

            final String packageName = parsedIntent.getPackage();
            if (packageName != null) {

                AlertDialog.Builder builder = new AlertDialog.Builder(JoinSNSActivity.this);
                builder.setMessage("설치 후 사용하시겠습니까?")
                        .setCancelable(false)
                        .setPositiveButton("네", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName)));
                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

                return true;
            }
            return false;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.startsWith("tel:")) {
                Intent dial = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(dial);
                return true;
            } else if (url.startsWith("sms:")) {
                Intent i = new Intent(Intent.ACTION_SENDTO, Uri.parse(url));
                startActivity(i);
                return true;
            } else if (url.startsWith("intent:")) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    Intent intent = null;
                    try {
                        intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                        if (webView.canGoBack()) {
                            webView.clearHistory();
                        }
                        return doFallback(view, intent);
                    }
                } else {
                    Intent intent = null;
                    try {
                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(intent);
                    } catch (ActivityNotFoundException e) {
                        e.printStackTrace();
                        if (webView.canGoBack()) {
                            webView.clearHistory();
                        }
                        return doFallback(view, intent);
                    }
                }
                return true;
            } else if (url.endsWith(".mp4") || url.endsWith(".swf")) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse(url), "video/*");
                view.getContext().startActivity(intent);
                return true;
            } else if (url.startsWith("http://") || url.startsWith("https://")) {
                return false;
            } else if (url.startsWith("ichingu:")){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    try {
                        Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(intent);
                    } catch (ActivityNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
            else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    try {
                        Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(intent);
                    } catch (ActivityNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
            return true;
        }
    }

    class AndroidBridge {
        @JavascriptInterface
        public void address(final String gps) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Log.i(TAG, gps);
                    try {
                        final JSONObject jsonObject = new JSONObject(gps);
                        lat = jsonObject.getString("lat");
                        lng = jsonObject.getString("lng");
                        addr = jsonObject.getString("addr");
                        addr1 = jsonObject.getString("addr1");

                        twoBtnDialog = new TwoBtnDialog(JoinSNSActivity.this, addr, addr1, lng, lat);
                        twoBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        twoBtnDialog.setCancelable(false);
                        twoBtnDialog.show();
                    } catch (JSONException e) {

                    }
                }
            });
        }

        @JavascriptInterface
        public void cert(final String cert) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        final JSONObject jsonObject = new JSONObject(cert);
                        String certreturn = jsonObject.getString("return");
                        if("true".equals(jsonObject.getString("return"))){
                            webView.setVisibility(View.GONE);
                            certify1.setText("본인 인증 완료");
                            certify1.setEnabled(false);
                            isCertify1 = true;
                        }
                    } catch (JSONException e) {

                    }
                }
            });
        }
    }
}


