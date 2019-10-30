package com.woojeong.global.ifriend;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.smarteist.autoimageslider.SliderLayout;

import static com.woojeong.global.ifriend.GlobalApplication.setDarkMode;

public class PaymentActivity extends AppCompatActivity {
    private static String TAG = "PaymentActivity";
    Context context;
    InputMethodManager ipmm;
    AQuery aQuery = null;
    OneBtnDialog oneBtnDialog;

    FrameLayout back_con, pay_con;
    ImageView back, sitter_photo, more, service_more_icon, refund_more_icon;
    ScrollView scrollView;
    SliderLayout home_slider;
    LinearLayout more_con, service_con, service_more_con, refund_con, refund_more_con;
    TextView sitter_name, sitter_location, start_date, finish_date, start_time, finish_time, total_time, total_children, expected_price, sale_price, vat, total_amount, service_more, refund_more, agree, pay;
    ListView child_list;
    CheckBox check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#ffffff"));
            window.setBackgroundDrawable(null);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                setDarkMode(PaymentActivity.this, true);
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

        pay_con = findViewById(R.id.pay_con);
        sitter_photo = findViewById(R.id.sitter_photo);
        more = findViewById(R.id.more);
        service_more_icon = findViewById(R.id.service_more_icon);
        refund_more_icon = findViewById(R.id.refund_more_icon);

        scrollView = findViewById(R.id.scrollView);
        scrollView.postDelayed(new Runnable() {
            @Override
            public void run() {
                scrollView.scrollTo(0, 0);
            }
        }, 700);

        home_slider = findViewById(R.id.home_slider);

        more_con = findViewById(R.id.more_con);
        service_con = findViewById(R.id.service_con);
        service_more_con = findViewById(R.id.service_more_con);
        refund_con = findViewById(R.id.refund_con);
        refund_more_con = findViewById(R.id.refund_more_con);

        sitter_name = findViewById(R.id.sitter_name);
        sitter_location = findViewById(R.id.sitter_location);
        start_date = findViewById(R.id.start_date);
        finish_date = findViewById(R.id.finish_date);
        start_time = findViewById(R.id.start_time);
        finish_time = findViewById(R.id.finish_time);
        total_time = findViewById(R.id.total_time);
        total_children = findViewById(R.id.total_children);
        expected_price = findViewById(R.id.expected_price);
        sale_price = findViewById(R.id.sale_price);
        vat = findViewById(R.id.vat);
        total_amount = findViewById(R.id.total_amount);
        service_more = findViewById(R.id.service_more);
        refund_more = findViewById(R.id.refund_more);
        agree = findViewById(R.id.agree);
        pay = findViewById(R.id.pay);

        child_list = findViewById(R.id.child_list);

        check = findViewById(R.id.check);

        pay_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pay.callOnClick();
            }
        });
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!check.isChecked()) {
                    oneBtnDialog = new OneBtnDialog(PaymentActivity.this, "동의 사항을 확인해주세요 !", "확인");
                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    oneBtnDialog.setCancelable(false);
                    oneBtnDialog.show();
                    return;
                }


            }
        });
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
