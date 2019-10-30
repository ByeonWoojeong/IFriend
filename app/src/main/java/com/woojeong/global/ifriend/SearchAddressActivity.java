package com.woojeong.global.ifriend;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.woojeong.global.ifriend.DTO.SearchAddress;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.android.gms.location.FusedLocationProviderClient;

import static com.woojeong.global.ifriend.GlobalApplication.setDarkMode;

public class SearchAddressActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private static String TAG = "SearchAddressActivity";
    Context context;
    AQuery aQuery = null;
    InputMethodManager ipm;

    LocationManager locManager;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    double startLatitude = 37.506276, startLongitude = 127.028923;

    private FusedLocationProviderClient mFusedLocationClient;
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private Location location;
    private Location location2;

    TextView this_location_search, home_location_search, search_text;
    ImageView back, search_btn;
    LinearLayout example_box;
    FrameLayout back_con;
    ListView address_listview;
    ArrayList<SearchAddress> data;
    AddressListAdapter addressListAdapter;
    OneBtnDialog oneBtnDialog;

    boolean isClick;
    int secCount;
    WebView webView;
    Handler handler = new Handler();
    TwoBtnDialog twoBtnDialog;

    String getToken = "";

    SharedPreferences filteringData;
    String lat = "";
    String lng = "";
    String addr = "";
    String addr1 = "";
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_address);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#ffffff"));
            window.setBackgroundDrawable(null);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                setDarkMode(SearchAddressActivity.this, true);
            }
        }

        mGoogleApiClient = new GoogleApiClient.Builder(SearchAddressActivity.this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(SearchAddressActivity.this);
        if (!locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            Toast.makeText(SearchAddressActivity.this, "GPS가 꺼져있습니다.'위치 서비스’에서 활성화 해주세요.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivityForResult(intent, 123);
        } else {
            onLocationChanged(location);
        }

        SharedPreferences get_token = getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
        getToken = get_token.getString("Token", "");

        aQuery = new AQuery(this);
        ipm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        back_con = findViewById(R.id.back_con);
        back_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back.callOnClick();
            }
        });
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        webView = findViewById(R.id.webView);
        webView.addJavascriptInterface(new AndroidBridge(), "ichingu");
        String userAgent = new WebView(getBaseContext()).getSettings().getUserAgentString();
        webView.getSettings().setUserAgentString(userAgent + "" + getToken);
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
        webView.setWebViewClient(new WebViewClient());
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
                Dialog dialog = webDialog.alertDialog(SearchAddressActivity.this, message, result);
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
                Dialog dialog = webDialog.confirmDialog(SearchAddressActivity.this, message, result);
                dialog.show();
                return true;
            }
        });

        search_text = findViewById(R.id.search_text);
        search_btn = findViewById(R.id.search_btn);

        search_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_btn.callOnClick();
            }
        });
        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.setVisibility(View.VISIBLE);
                webView.loadUrl(ServerUrl.getBaseUrl() + "/search");

            }
        });
        this_location_search = findViewById(R.id.this_location_search);
        this_location_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String url = ServerUrl.getBaseUrl() + "/search/gps";
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("lat", startLatitude);
                params.put("lng", startLongitude);
                Log.i(TAG, " params " + params );
                aQuery.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
                    @Override
                    public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
                        Log.i(TAG, " jsonObject " + jsonObject );
                        try {
                            if (jsonObject.getBoolean("return")) {    //return이 true 면?

                                JSONObject jsonData = jsonObject.getJSONObject("data");

                                search_text.setText(jsonData.getString("addr"));

                                filteringData = getSharedPreferences("filtering", Activity.MODE_PRIVATE);
                                SharedPreferences.Editor filterEditor = filteringData.edit();
                                filterEditor.putString("address", jsonData.getString("addr"));
                                filterEditor.commit();
                                finish();

                            } else if (!jsonObject.getBoolean("return")) {
                                if("login".equals(jsonObject.getString("type"))){
                                    Toast.makeText(SearchAddressActivity.this, "로그인 해주세요 ! ", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(SearchAddressActivity.this, LoginActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(SearchAddressActivity.this, "다시 검색해주세요 !", Toast.LENGTH_SHORT).show();
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }.header("EPOCH-AGENT", "" + getToken).header("User-Agent", "android"));
            }
        });

        home_location_search = findViewById(R.id.home_location_search);
        home_location_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String url = ServerUrl.getBaseUrl() + "/search/home";
                Map<String, Object> params = new HashMap<String, Object>();
                Log.i(TAG, " params " + params );
                aQuery.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
                    @Override
                    public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
                        Log.i(TAG, " jsonObject " + jsonObject );
                        try {
                            if (jsonObject.getBoolean("return")) {    //return이 true 면?

                                JSONObject jsonData = jsonObject.getJSONObject("data");

                                search_text.setText(jsonData.getString("addr"));

                                filteringData = getSharedPreferences("filtering", Activity.MODE_PRIVATE);
                                SharedPreferences.Editor filterEditor = filteringData.edit();
                                filterEditor.putString("address", jsonData.getString("addr"));
                                filterEditor.putString("lat", jsonData.getString("lat"));
                                filterEditor.putString("lng", jsonData.getString("lng"));
                                filterEditor.commit();
                                finish();

                            } else if (!jsonObject.getBoolean("return")) {
                                if("login".equals(jsonObject.getString("type"))){
                                    Toast.makeText(SearchAddressActivity.this, "로그인 해주세요 ! ", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(SearchAddressActivity.this, LoginActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(SearchAddressActivity.this, "다시 검색해주세요 !", Toast.LENGTH_SHORT).show();
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }.header("EPOCH-AGENT", "" + getToken).header("User-Agent", "android"));
            }
        });
        example_box = findViewById(R.id.example_box);
        address_listview = findViewById(R.id.address_listview);
        address_listview.setVisibility(View.GONE);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 123:
                if (!locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    Toast.makeText(context, "GPS가 꺼져있습니다.'위치 서비스’에서 활성화 해주세요.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivityForResult(intent, 123);
                } else {
                    onLocationChanged(location);
                }

            default:
                break;
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        List<String> providers = locManager.getProviders(true);
        Location bestLocation = null;
        locManager.removeUpdates(this);
        for (String provider : providers) {
            if (ContextCompat.checkSelfPermission(SearchAddressActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                location = locManager.getLastKnownLocation(provider);
                Log.i(TAG, " location " + location);
            }
            if (location == null) {

                Log.i(TAG, "location: " + location + " 위치가 동일할 수 있습니다.");
                continue;
            } else {
                startLatitude = location.getLatitude();
                startLongitude = location.getLongitude();
            }
            if (bestLocation == null || location.getAccuracy() < location.getAccuracy()) {
                bestLocation = location;
            } else {
                startLatitude = location.getLatitude();
                startLongitude = location.getLongitude();
            }
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(3000); //3000(위치 업데이트 반복 주기)
        mLocationRequest.setFastestInterval(3000);  //1500
        if (ActivityCompat.checkSelfPermission(SearchAddressActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(SearchAddressActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            CheckPermission();
        }
        Log.i("GPS", "onConnected -> startLocationUpdates START");
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);

            List<Location> locationList = locationResult.getLocations();

            if (locationList.size() > 0) {
                location2 = locationList.get(locationList.size() - 1);
                location = location2;

                String markerSnippet = "위도:" + String.valueOf(location.getLatitude())
                        + " 경도:" + String.valueOf(location.getLongitude());
            }
            onLocationChanged(location);
        }
    };

    private void CheckPermission() {
        Log.i("GPS", "CheckPermission START");
        int hasWriteContactsPermission = ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION);
        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(SearchAddressActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION)) {
                ActivityCompat.requestPermissions(SearchAddressActivity.this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, 2);
                return;
            }
            ActivityCompat.requestPermissions(SearchAddressActivity.this, new String[]{android.Manifest.permission.WRITE_CONTACTS}, 2);
            return;
        }
        hasWriteContactsPermission = ContextCompat.checkSelfPermission(SearchAddressActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION);
        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(SearchAddressActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(SearchAddressActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 2);
                return;
            }
            ActivityCompat.requestPermissions(SearchAddressActivity.this, new String[]{android.Manifest.permission.WRITE_CONTACTS}, 2);
            return;
        }
    }

    protected void startLocationUpdates() {
        Log.i("GPS", "startLocationUpdates START");
        if (ActivityCompat.checkSelfPermission(SearchAddressActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(SearchAddressActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        LocationSettingsRequest.Builder builder =
                new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);

        mFusedLocationClient.requestLocationUpdates(mLocationRequest, locationCallback, Looper.myLooper());

        if (!locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Log.i("GPS", " GPS 서비스 활성화 해야함.2");
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            startActivityForResult(intent, GPS_ENABLE_REQUEST_CODE);

        } else {
            Log.i("GPS", " GPS 서비스 활성화");
            onLocationChanged(location);
        }
    }

    void addressResult(){
        example_box.setVisibility(View.GONE);
        address_listview.setVisibility(View.VISIBLE);
        address_listview.setDivider(null);
        addressListAdapter = new AddressListAdapter(SearchAddressActivity.this, R.layout.list_address_item, data, address_listview, SearchAddressActivity.this);

        final String url = ServerUrl.getBaseUrl() + "/search";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("address", search_text.getText().toString());
        aQuery.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
                Log.i(TAG, " " + jsonObject );
                try {
                    data.clear();
                    if (jsonObject.getBoolean("return")) {    //return이 true 면?
                        JSONArray jsonArray = new JSONArray(jsonObject.getString("list"));
                        if (jsonArray.length() == 0) {
                            address_listview.setVisibility(View.GONE);
                            example_box.setVisibility(View.VISIBLE);
                            oneBtnDialog = new OneBtnDialog(SearchAddressActivity.this, "검색 결과가 없습니다 !", "확인");
                            oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            oneBtnDialog.setCancelable(false);
                            oneBtnDialog.show();
                        } else {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject getJsonObject = jsonArray.getJSONObject(i);
//                                data.add(new SearchAddress());
                            }
                            address_listview.setAdapter(addressListAdapter);
                            addressListAdapter.notifyDataSetChanged();
                        }
                    } else if (!jsonObject.getBoolean("return")) {

                        if("login".equals(jsonObject.getString("type"))){
                            Toast.makeText(SearchAddressActivity.this, "로그인 해주세요 ! ", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SearchAddressActivity.this, LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(SearchAddressActivity.this, "다시 검색해주세요 !", Toast.LENGTH_SHORT).show();
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.header("EPOCH-AGENT", "" + getToken).header("User-Agent", "android"));
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

        public TwoBtnDialog(final Context context, final String address, final String addr1, final String lng, final String lat) {
            super(context);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialog_two_btn);
            getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            this.context = context;
            TextView title1 = (TextView) findViewById(R.id.title1);
            TextView title2 = (TextView) findViewById(R.id.title2);
            final TextView btn1 = (TextView) findViewById(R.id.btn1);
            TextView btn2 = (TextView) findViewById(R.id.btn2);
            title2.setVisibility(View.GONE);
            title1.setText(address);
            btn1.setText("취소");
            btn2.setText("검색");
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
                    search_text.setText(addr1);

                    filteringData = getSharedPreferences("filtering", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor filterEditor = filteringData.edit();
                    filterEditor.putString("address", address);
                    filterEditor.commit();

                    finish();

                }
            });
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

                        twoBtnDialog = new TwoBtnDialog(SearchAddressActivity.this, addr, addr1, lng, lat);
                        twoBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        twoBtnDialog.setCancelable(false);
                        twoBtnDialog.show();
                    } catch (JSONException e) {

                    }
                }
            });
        }
    }
}
