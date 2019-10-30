package com.woojeong.global.ifriend;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.woojeong.global.ifriend.DTO.Notice;
import com.woojeong.global.ifriend.DTO.One;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public class LicenseAdapter extends BaseAdapter {
    Context context;
    int layout;
    ArrayList<One> data;
    ListView listView;
    AQuery aQuery = null;
    LicenseAdapter licenseAdapter = this;
    TwoBtnDialog twoBtnDialog;
    boolean delete;

    public LicenseAdapter(Context context, int layout, ArrayList<One> data, ListView listView, boolean delete) {
        this.context = context;
        this.layout = layout;
        this.data = data;
        this.listView = listView;
        this.delete = delete;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        aQuery = new AQuery(context);

        if (convertView == null) {
            convertView = View.inflate(context, layout, null);
            vh = new ViewHolder();
            vh.career = convertView.findViewById(R.id.career);
            vh.delete_con = convertView.findViewById(R.id.delete_con);
            vh.delete = convertView.findViewById(R.id.delete);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        final One item = data.get(position);

       final ViewHolder finalVh = vh;

        if (delete) {
            vh.delete_con.setVisibility(View.VISIBLE);
            vh.delete.setVisibility(View.VISIBLE);
        } else {
            vh.delete_con.setVisibility(View.GONE);
            vh.delete.setVisibility(View.GONE);
        }

        vh.career.setText(item.getText1());
        vh.delete_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalVh.delete.callOnClick();
            }
        });
        finalVh.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                twoBtnDialog = new TwoBtnDialog(context, "해당 자격증을\n삭제 하시겠습니까?", item);
                twoBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                twoBtnDialog.setCancelable(false);
                twoBtnDialog.show();
            }
        });
        return convertView;
    }

    class ViewHolder{
        TextView career;
        FrameLayout delete_con;
        ImageView delete;
    }

    public void setListViewHeight(BaseAdapter adpater, ListView listView) {
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

    public class TwoBtnDialog extends Dialog {
        TwoBtnDialog twoBtnDialog = this;
        Context context;
        public TwoBtnDialog(final Context context, final String text, final One item) {
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
            btn2.setText("삭제");
            btn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    twoBtnDialog.dismiss();
                }
            });
            btn2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    data.remove(item);
                    licenseAdapter.notifyDataSetChanged();
                    setListViewHeight(licenseAdapter, listView);
                    twoBtnDialog.dismiss();

                }
            });
        }
    }
}
