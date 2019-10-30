package com.woojeong.global.ifriend;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.woojeong.global.ifriend.DTO.JournalDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.woojeong.global.ifriend.GlobalApplication.setDarkMode;

public class UseCostActivity extends AppCompatActivity {
    private static String TAG = "UseCostActivity";
    Context context;
    InputMethodManager ipmm;
    AQuery aQuery = null;
    OneBtnDialog oneBtnDialog;

    FrameLayout back_con, sale_con, ok_con;
    ImageView back, spinner_down;
    ScrollView scrollView;
    TextView child_count, sale_txt, ok;
    EditText weekday_pay, weekend_pay;
    SpinnerReselect spinner_sale;

    String[] discount = {"없음", "10%", "20%", "30%", "40%", "50%"};
    String getDiscount = "";

    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_use_cost);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#ffffff"));
            window.setBackgroundDrawable(null);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                setDarkMode(UseCostActivity.this, true);
            }
        }
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

        sale_con = findViewById(R.id.sale_con);
        ok_con = findViewById(R.id.ok_con);

        spinner_down = findViewById(R.id.spinner_down);

        scrollView = findViewById(R.id.scrollView);

        child_count = findViewById(R.id.child_count);
        sale_txt = findViewById(R.id.sale_txt);
        ok = findViewById(R.id.ok);

        weekday_pay = findViewById(R.id.weekday_pay);
        weekend_pay = findViewById(R.id.weekend_pay);

        spinner_sale = findViewById(R.id.spinner_sale);

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                viewSettings();
            }
        }, 300);

        ArrayAdapter<String> discountAdapter = new ArrayAdapter<String>(context, R.layout.spinner_item, discount);
        discountAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner_sale.setAdapter(discountAdapter);
        spinner_sale.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sale_txt.setText(spinner_sale.getSelectedItem().toString());
                getDiscount = (spinner_sale.getSelectedItemPosition()) + "";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                getDiscount = "0";
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

                if("없음".equals(sale_txt.getText().toString())){
                    getDiscount = "0";
                } else if("10%".equals(sale_txt.getText().toString())){
                    getDiscount = "1";
                }else if("20%".equals(sale_txt.getText().toString())){
                    getDiscount = "2";
                }else if("30%".equals(sale_txt.getText().toString())){
                    getDiscount = "3";
                }else if("40%".equals(sale_txt.getText().toString())){
                    getDiscount = "4";
                }else if("50%".equals(sale_txt.getText().toString())){
                    getDiscount = "5";
                }

                SharedPreferences get_token = getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
                final String getToken = get_token.getString("Token", "");
                final String url = ServerUrl.getBaseUrl() + "/friend/update";
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("pay1", weekday_pay.getText().toString());
                params.put("pay2", weekend_pay.getText().toString());
                params.put("discount", getDiscount);
                Log.i(TAG, " ok params " + params);
                aQuery.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
                    @Override
                    public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
                        Log.i(TAG, " ok jsonObject " + jsonObject);
                        try {
                            if (jsonObject.getBoolean("return")) {    //return이 true 면?
                                Toast.makeText(UseCostActivity.this, "이용 비용 업로드 성공", Toast.LENGTH_SHORT).show();
                                setResult(123);
                                finish();
                            } else if (!jsonObject.getBoolean("return")) {
                                if("login".equals(jsonObject.getString("type"))){
                                    Toast.makeText(UseCostActivity.this, "로그인 해주세요 ! ", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(UseCostActivity.this, LoginActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(UseCostActivity.this, "이용 비용 업로드 실패", Toast.LENGTH_SHORT).show();
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

    void viewSettings(){
        SharedPreferences get_token = getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
        final String getToken = get_token.getString("Token", "");
        final String url = ServerUrl.getBaseUrl() + "/friend/data";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("carecnt", "");
        params.put("pay1", "");
        params.put("pay2", "");
        params.put("discount", "");
        Log.i(TAG, " viewSettings params " + params);
        aQuery.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
                Log.i(TAG, " viewSettings jsonObject " + jsonObject);
                try {
                    if (jsonObject.getBoolean("return")) {    //return이 true 면?
                        JSONObject jsonData = jsonObject.getJSONObject("data");

                        child_count.setText(jsonData.getString("carecnt"));
                        weekday_pay.setText(jsonData.getString("pay1"));
                        weekend_pay.setText(jsonData.getString("pay2"));

                        int discount = Integer.parseInt(jsonData.getString("discount")) * 10;

                        Log.i(TAG, " viewSettings discount " + discount);

                        if("0".equals(jsonData.getString("discount"))){
                            sale_txt.setText("없음");
                            getDiscount = "0";
                        } else {
                            sale_txt.setText(discount + "%");
                            getDiscount = jsonData.getString("discount");
                            Log.i(TAG, " viewSettings getDiscount " + getDiscount);
                        }

                    } else if (!jsonObject.getBoolean("return")) {
                        if("login".equals(jsonObject.getString("type"))){
                            Toast.makeText(UseCostActivity.this, "로그인 해주세요 ! ", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(UseCostActivity.this, LoginActivity.class);
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
