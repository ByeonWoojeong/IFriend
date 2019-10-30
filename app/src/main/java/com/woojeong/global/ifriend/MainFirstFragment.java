package com.woojeong.global.ifriend;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.woojeong.global.ifriend.DTO.Main;
import com.woojeong.global.ifriend.SliderImageView.PicassoImageLoadingService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ss.com.bannerslider.Slider;
import ss.com.bannerslider.event.OnSlideClickListener;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainFirstFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MainFirstFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFirstFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private static final String TAG = "MainFirstFragment";
    MainActivity mainActivity;
    AQuery aQuery = null;
    Context context;
    View view;
    String token;
    private OnFragmentInteractionListener mListener;

    LoginDialog loginDialog;
    OneBtnDialog oneBtnDialog;

    Slider adv_slider;

    ImageView search, filter;
    LinearLayout date_con, location_con;

    TextView myLocation, date_start, date_end;

    ListView main_list;
    MainListAdapter mainListAdapter;
    ArrayList<Main> data;

    LocationManager locManager;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    double startLatitude = 37.506276, startLongitude = 127.028923;

    private FusedLocationProviderClient mFusedLocationClient;
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private Location location;
    private Location location2;

    boolean isStop = false;

    String defaultAddr = null;
    String searchAddr, searchLat, searchLng;

    SharedPreferences filterData;
    String getAddress = "", getCare = "", getStart = "", getEnd = "", getOrder = "", getTime0 = "", getTime1 = "";
    JSONArray dateArray;
    ArrayList<String> getDate;
    boolean getPeriod, getPickUp;
    SharedPreferences prefToken;

    public MainFirstFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainFirstFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainFirstFragment newInstance(String param1, String param2) {
        MainFirstFragment fragment = new MainFirstFragment();
        Bundle args = new Bundle();
        args.putString("some_int", param1);
        args.putString("some_title", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_main_first, container, false);
        context = getContext();
        aQuery = new AQuery(context);
        prefToken = context.getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
        token = prefToken.getString("Token", "");
        search = view.findViewById(R.id.search);
        filter = view.findViewById(R.id.filter);
        date_con = view.findViewById(R.id.date_con);
        location_con = view.findViewById(R.id.location_con);
        myLocation = view.findViewById(R.id.location);
        date_start = view.findViewById(R.id.date_start);
        date_end = view.findViewById(R.id.date_end);

        adv_slider = view.findViewById(R.id.adv_slider);
        Point size = new Point();
        getActivity().getWindowManager().getDefaultDisplay().getSize(size);
        int width = size.x;
        int height = width / 2;
        adv_slider.setLayoutParams(new LinearLayout.LayoutParams(width, height));
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchNameActivity.class);
                startActivity(intent);
            }
        });
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChoiceFilterActivity.class);
                startActivity(intent);
            }
        });
        date_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChoiceDateActivity.class);
                startActivity(intent);
            }
        });
        location_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchAddressActivity.class);
                intent.putExtra("lat", startLatitude);
                intent.putExtra("lng", startLongitude);
                startActivity(intent);
            }
        });

        mGoogleApiClient = new GoogleApiClient.Builder(context).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        locManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);

        main_list = view.findViewById(R.id.main_list);
        data = new ArrayList<Main>();
        getDate = new ArrayList<String>();


        if (!locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            Toast.makeText(context, "GPS가 꺼져있습니다.'위치 서비스’에서 활성화 해주세요.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivityForResult(intent, 123);
        } else {
            onLocationChanged(location);
        }
        advImages();
        mainListAdapter = new MainListAdapter(context, R.layout.list_main_item, data, main_list, MainFirstFragment.this);
        main_list.setAdapter(mainListAdapter);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
        SharedPreferences.Editor filterEditor = filterData.edit();
        filterEditor.clear();
        filterEditor.commit();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, " onResume");
//        locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

       if (prefToken.getString("tab", "").equals("1")) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    reload();
                }
            }, 10);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
//        adv_slider.stopAutoCycle();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, " onActivityResult");
        Log.i(TAG, " " + requestCode + " " + resultCode);
        Log.i("result", " 1Fragment");
        switch (resultCode) {
            case 9999:
                Intent LogoutIntent = new Intent(getActivity(), LoginActivity.class);
                startActivity(LogoutIntent);
                getActivity().finish();
                break;
            default:
                break;
        }
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
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
//        mLocationRequest.setInterval(10000); //3000(위치 업데이트 반복 주기)
        mLocationRequest.setFastestInterval(3000);  //1500
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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

    @Override
    public void onLocationChanged(Location location) {
        List<String> providers = locManager.getProviders(true);
        Location bestLocation = null;
        locManager.removeUpdates(this);
        for (String provider : providers) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
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

        //getLatitude 널 처리할 것.!

//        startLatitude = location.getLatitude();
//        startLongitude = location.getLongitude();

        //홍대역
//        StartLatitude = 37.556719;
//        StartLongitude = 126.923673;

        Log.i("GPS", " lat: " + startLatitude + " lon: " + startLongitude);
        mGoogleApiClient.disconnect();
        SharedPreferences get_token = context.getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
        final String getToken = get_token.getString("Token", "");
        String url = ServerUrl.getBaseUrl() + "/search/gps";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("lat", startLatitude);
        params.put("lng", startLongitude);
        Log.i(TAG, " " + params);
        aQuery.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
                Log.i(TAG, "" + jsonObject);
                if (jsonObject != null) {

                    try {
                        if (jsonObject.getBoolean("return")) {    //return이 true 면?
                            defaultAddr = jsonObject.getJSONObject("data").getString("addr");
                            myLocation.setText(defaultAddr);
                            reload();
                        } else if (!jsonObject.getBoolean("return")) {
                            myLocation.setText("현재 위치 알 수 없음.");
                            if ("login".equals(jsonObject.getString("type"))) {
                                Toast.makeText(context, "로그인 해주세요 ! ", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.header("EPOCH-AGENT", "" + getToken).header("User-Agent", "android"));
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
            if (!ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION)) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, 2);
                return;
            }
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.WRITE_CONTACTS}, 2);
            return;
        }
        hasWriteContactsPermission = ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION);
        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 2);
                return;
            }
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.WRITE_CONTACTS}, 2);
            return;
        }
    }

    protected void startLocationUpdates() {
        Log.i("GPS", "startLocationUpdates START");
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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

    public void advImages() {
        Log.i(TAG, " --advImages-- ");
        Slider.init(new PicassoImageLoadingService(context));

        SharedPreferences get_token = context.getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
        final String getToken = get_token.getString("Token", "");
        final String url = ServerUrl.getBaseUrl() + "/home/adv";
        Map<String, Object> params = new HashMap<String, Object>();
        aQuery.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
                Log.i(TAG, " jsonObject " + jsonObject);
                try {
                    if (jsonObject.getBoolean("return")) {    //return이 true 면?
                        final JSONArray jsonArray = new JSONArray(jsonObject.getString("list"));
                        if (jsonArray.length() == 0) {

                        } else {
                            final SliderImageAdapter sliderImageAdapter = new SliderImageAdapter(jsonArray);
                            adv_slider.setAdapter(sliderImageAdapter);
                            adv_slider.setOnSlideClickListener(new OnSlideClickListener() {
                                @Override
                                public void onSlideClick(int position) {
                                    String url = sliderImageAdapter.getUrl(position);
                                    if (url != null && (url.startsWith("http://") || url.startsWith("https://"))) {
                                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                        startActivity(intent);
                                    }
                                }
                            });

                        }
                    } else if (!jsonObject.getBoolean("return")) {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.header("EPOCH-AGENT", "" + getToken).header("User-Agent", "android"));

    }

    public void reload() {
        if(isStop){
            return;
        }
        isStop = true;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                filterData = context.getSharedPreferences("filtering", Activity.MODE_PRIVATE);
                getPeriod = filterData.getBoolean("period", false);
                getPickUp = filterData.getBoolean("pickup", false);

                if (!"".equals(filterData.getString("address", "")) || null != filterData.getString("address", "")) {
                    getAddress = filterData.getString("address", "");
                    myLocation.setText(getAddress);
                    if ("".equals(getAddress)) {
                        myLocation.setText(defaultAddr);
                    }
                }
                if (!"".equals(filterData.getString("care", "")) || null != filterData.getString("care", "")) {
                    getCare = filterData.getString("care", "");
                }
                if (!"".equals(filterData.getString("start", "")) || null != filterData.getString("start", "")) {
                    getStart = filterData.getString("start", "");
                }
                if (!"".equals(filterData.getString("end", "")) || null != filterData.getString("end", "")) {
                    getEnd = filterData.getString("end", "");
                }
                if (!"".equals(filterData.getString("order", "")) || null != filterData.getString("order", "")) {
                    getOrder = filterData.getString("order", "");
                }

                if (!"".equals(filterData.getString("time[0]", "")) || null != filterData.getString("time[0]", "")) {
                    getTime0 = filterData.getString("time[0]", "");
                }

                if (!"".equals(filterData.getString("time[1]", "")) || null != filterData.getString("time[1]", "")) {
                    getTime1 = filterData.getString("time[1]", "");
                }

                date_start.setText("전체 날짜");
                date_end.setText("전체 날짜");

                String dateStr = null;
                if (!"".equals(filterData.getString("date", "")) || null != filterData.getString("date", "")) {
                    dateStr = filterData.getString("date", "");
                    if (dateStr != null) {
                        try {
                            dateArray = new JSONArray(dateStr);
                            getDate.clear();
                            for (int i = 0; i < dateArray.length(); i++) {
                                String date = dateArray.optString(i);
                                getDate.add(date);
                            }
                            if (getDate.size() == 0) {
                                date_start.setText("전체 날짜");
                                date_end.setText("전체 날짜");
                            } else {
                                date_start.setText(getDate.get(0));
                                date_end.setText(getDate.get(getDate.size() - 1));
                            }
                        } catch (JSONException e) {
                            Log.i(TAG, " Exception " + e);
                        }
                    } else {
                    }
                }
                if (prefToken.getString("tab", "").equals("1") ) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            if (!"".equals(getAddress) || getPeriod || getPickUp || !"".equals(getCare) || !"".equals(getStart) || !"".equals(getEnd) || !"".equals(getOrder) || !"".equals(getTime0) || !"".equals(getTime1) || getDate.size() > 0) {
                                filtering(getAddress, getPeriod, getPickUp, getCare, getStart, getEnd, getOrder, getDate, getTime0, getTime1);
                            } else
                            showList();
                        }
                    }, 10);
                }
            }
        }, 100);
    }

    void showList() {
        Log.i(TAG, " showList");

        final String url = ServerUrl.getBaseUrl() + "/home/list";
        Map<String, Object> params = new HashMap<String, Object>();
        Log.i(TAG, " showList params :: " + params);
        aQuery.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
                Log.i(TAG, " showList jsonObject :: " + jsonObject);
                try {
                    data.clear();
                    if (jsonObject.getBoolean("return")) {    //return이 true 면?
                        JSONArray jsonArray = new JSONArray(jsonObject.getString("list"));
                        int length = jsonArray.length();
                        if (length == 0) {
                            main_list.setVisibility(View.GONE);
                            Toast.makeText(context, "목록이 없습니다.", Toast.LENGTH_SHORT).show();
                        } else {
                            main_list.setVisibility(View.VISIBLE);
                            for (int i = 0; i < length; i++) {
                                JSONObject getJsonObject = jsonArray.getJSONObject(i);
                                Log.i(TAG, " showList getJsonObject :: " + getJsonObject);
                                data.add(new Main(getJsonObject.getString("member"), getJsonObject.getString("profile"), getJsonObject.getString("images").split(","), getJsonObject.getString("discount"), getJsonObject.getString("title"), getJsonObject.getString("name"), getJsonObject.getString("addr"), getJsonObject.getString("islike"), getJsonObject.getString("star"), getJsonObject.getString("reviewcnt"), getJsonObject.getString("pay1"), getJsonObject.getString("pay2")));
                            }
//                            main_list.setAdapter(mainListAdapter);
                            mainListAdapter.notifyDataSetChanged();
                            setListViewHeightBasedOnChildren(main_list);
                        }
                    } else if (!jsonObject.getBoolean("return")) {
                        main_list.setVisibility(View.GONE);

                        Toast.makeText(context, "해당 리스트가 없습니다.", Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                isStop = false;
            }
        }.header("EPOCH-AGENT", "" + token).header("User-Agent", "android"));


    }

    void filtering(String address, boolean period, boolean pickUp, String care, String start, String end, String order, ArrayList<String> date, String time0, String time1) {
        Log.i(TAG, " filtering");

        SharedPreferences get_token = context.getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
        final String getToken = get_token.getString("Token", "");
        final String url = ServerUrl.getBaseUrl() + "/home/list";
        Map<String, Object> params = new HashMap<String, Object>();

        if (!"".equals(address)) {
            params.put("addr", address);
        }
        if (period) {
            params.put("long", "1");
        }
        if (pickUp) {
            params.put("pickup", "1");
        }
        if (!"".equals(care)) {
            params.put("care", care);
        }
        if (!"".equals(start)) {
                params.put("start", start);
        }
        if (!"".equals(end)) {
                params.put("end", end);
        }
        if (!"".equals(order)) {
            params.put("order", order);
        }
        if (date.size() > 0) {
            for (int i = 0; i < date.size(); i++) {
                params.put("date[" + i + "]", date.get(i));
            }
        }
        if (!"".equals(time0)) {
            params.put("time[0]", time0);
        }
        if (!"".equals(time1)) {
            params.put("time[1]", time1);
        }
        Log.i(TAG, " params " + params);
        aQuery.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
                Log.i(TAG, " jsonObject " + jsonObject);
                try {
                    data.clear();
                    if (jsonObject.getBoolean("return")) {    //return이 true 면?
                        JSONArray jsonArray = new JSONArray(jsonObject.getString("list"));
                        if (jsonArray.length() == 0) {
                            main_list.setVisibility(View.GONE);
                            Toast.makeText(context, "목록이 없습니다.", Toast.LENGTH_SHORT).show();
                        } else {
                            main_list.setVisibility(View.VISIBLE);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject getJsonObject = jsonArray.getJSONObject(i);
                                data.add(new Main(getJsonObject.getString("member"), getJsonObject.getString("profile"), getJsonObject.getString("images").split(","), getJsonObject.getString("discount"), getJsonObject.getString("title"), getJsonObject.getString("name"), getJsonObject.getString("addr"), getJsonObject.getString("islike"), getJsonObject.getString("star"), getJsonObject.getString("reviewcnt"), getJsonObject.getString("pay1"), getJsonObject.getString("pay2")));
                            }
                            mainListAdapter.notifyDataSetChanged();

                            setListViewHeightBasedOnChildren(main_list);
                        }
                    } else if (!jsonObject.getBoolean("return")) {
                        main_list.setVisibility(View.GONE);

                        Toast.makeText(context, "해당 리스트가 없습니다.", Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                isStop = false;
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
            Log.i(TAG, " 123 :: " + i);
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    public void setListViewHeight(MainListAdapter adpater, ListView listView) {
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

    public class LoginDialog extends Dialog {
        LoginDialog loginDialog = this;
        Context context;

        public LoginDialog(final Context context, final String text, final String btnText) {
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
                    loginDialog.dismiss();
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }
            });
        }
    }
}
