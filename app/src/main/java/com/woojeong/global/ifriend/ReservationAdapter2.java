package com.woojeong.global.ifriend;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.woojeong.global.ifriend.DTO.Reservation;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class ReservationAdapter2 extends BaseAdapter {
    Context context;
    int layout;
    ArrayList<Reservation> data;
    ListView listView;
    AQuery aQuery = null;
    ReservationAdapter2 reservationAdapter2 = this;

    String cancel = "";

    public ReservationAdapter2(Context context, int layout, ArrayList<Reservation> data, ListView listView) {
        this.context = context;
        this.layout = layout;
        this.data = data;
        this.listView = listView;
    }

    public ReservationAdapter2(Context context, int layout, ArrayList<Reservation> data, ListView listView, String cancel) {
        this.context = context;
        this.layout = layout;
        this.data = data;
        this.listView = listView;
        this.cancel = cancel;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        aQuery = new AQuery(context);

        if (convertView == null) {
            convertView = View.inflate(context, layout, null);
            vh = new ViewHolder();
            vh.photo = convertView.findViewById(R.id.photo);
            vh.status = convertView.findViewById(R.id.status);
            vh.jounal_icon = convertView.findViewById(R.id.jounal_icon);
            vh.go_chat = convertView.findViewById(R.id.go_chat);
            vh.name = convertView.findViewById(R.id.name);
            vh.period = convertView.findViewById(R.id.period);
            vh.status_con = convertView.findViewById(R.id.status_con);
            vh.jounal_con = convertView.findViewById(R.id.jounal_con);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        final Reservation item = data.get(position);
        final ViewHolder finalVh = vh;
        vh.name.setText(item.getName());
        vh.period.setText(item.getPeriod());

        if ("승인대기".equals(item.getStatus())) {
            vh.jounal_con.setVisibility(View.GONE);
            vh.status_con.setVisibility(View.GONE);
        } else if ("승인완료".equals(item.getStatus())) {
            vh.jounal_con.setVisibility(View.GONE);
            vh.status_con.setVisibility(View.GONE);
        } else if ("결제완료".equals(item.getStatus())) {
            vh.jounal_con.setVisibility(View.GONE);
            vh.status_con.setVisibility(View.GONE);
        } else if ("방문중".equals(item.getStatus())) {
            vh.jounal_con.setVisibility(View.VISIBLE);
            vh.status_con.setVisibility(View.GONE);
        } else if ("방문완료".equals(item.getStatus())) {
            vh.jounal_con.setVisibility(View.GONE);
            vh.status_con.setVisibility(View.VISIBLE);

            if ("0".equals(item.getIsreview())) {
                vh.status.setBackground(context.getResources().getDrawable(R.drawable.no_review));
            } else {
                vh.status.setBackground(context.getResources().getDrawable(R.drawable.reviewed));
                vh.status_con.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, VisitedReviewActivity.class);
                        intent.putExtra("reserve", item.getReserve());
                        intent.putExtra("status", item.getStatus());
                        context.startActivity(intent);
                    }
                });
            }


        } else if ("환불완료".equals(item.getStatus())) {
            vh.jounal_con.setVisibility(View.GONE);
            vh.status_con.setVisibility(View.GONE);
        } else if ("환불신청".equals(item.getStatus())) {
            vh.jounal_con.setVisibility(View.GONE);
            vh.status_con.setVisibility(View.GONE);
        } else if ("예약취소".equals(item.getStatus())) {
            vh.jounal_con.setVisibility(View.GONE);
            vh.status_con.setVisibility(View.GONE);
        } else if ("예약문의".equals(item.getStatus())) {
            vh.jounal_con.setVisibility(View.GONE);
            vh.status_con.setVisibility(View.GONE);
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ("cancel".equals(cancel)) {
                    Intent intent = new Intent(context, CancelReasonActivity.class);
                    intent.putExtra("reserve", item.getReserve());
                    context.startActivity(intent);
                } else {

                    Intent intent = new Intent(context, CheckReservation2Activity.class);
                    intent.putExtra("reserve", item.getReserve());
                    context.startActivity(intent);
                }

            }
        });

        vh.jounal_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalVh.jounal_icon.callOnClick();
            }
        });
        vh.jounal_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, WriteJournalActivity.class);
                intent.putExtra("reserve", item.getReserve());
                context.startActivity(intent);
            }
        });

        circleImage(vh.photo, ServerUrl.getBaseUrl() + "/uploads/images/origin/" + item.getProfile());

        return convertView;
    }

    class ViewHolder {
        ImageView photo, status, jounal_icon, go_chat;
        TextView name, period, jounal_txt;
        FrameLayout status_con, jounal_con;
    }

    private void circleImage(ImageView imageView, String getImg) {
        Glide.with(context)
                .load(getImg)
                .apply(RequestOptions.bitmapTransform(new CropCircleTransformation()))
                .into(imageView);
    }
}
