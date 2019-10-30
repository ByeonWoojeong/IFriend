package com.woojeong.global.ifriend;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.woojeong.global.ifriend.DTO.Reservation;
import com.woojeong.global.ifriend.DTO.Two;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class ReservationAdapter extends BaseAdapter {
    Context context;
    int layout;
    ArrayList<Reservation> data;
    ListView listView;
    AQuery aQuery = null;
    ReservationAdapter reservationAdapter = this;

    public ReservationAdapter(Context context, int layout, ArrayList<Reservation> data, ListView listView) {
        this.context = context;
        this.layout = layout;
        this.data = data;
        this.listView = listView;
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
            vh.sitter_photo = convertView.findViewById(R.id.sitter_photo);
            vh.status = convertView.findViewById(R.id.status);
            vh.name = convertView.findViewById(R.id.name);
            vh.term = convertView.findViewById(R.id.term);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        final Reservation item = data.get(position);
        vh.name.setText(item.getName());
        vh.term.setText(item.getPeriod());

        if ("예약취소".equals(item.getStatus())) {
            vh.status.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.cancel_reservation));
        } else if ("방문완료".equals(item.getStatus())) {
            vh.status.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.visited));
        } else if ("방문중".equals(item.getStatus())) {
            vh.status.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.visiting));
        } else if ("승인대기".equals(item.getStatus())) {
            vh.status.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.waiting_approval));
        } else if ("승인완료".equals(item.getStatus())) {
            vh.status.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.approved));
        } else if ("결제완료".equals(item.getStatus())) {
            vh.status.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.payment_complete));
        } else if ("환불신청".equals(item.getStatus())) {
            vh.status.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.request_refund));
        } else if ("환불완료".equals(item.getStatus())) {
            vh.status.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.refunded));
        }

        circleImage(vh.sitter_photo, ServerUrl.getBaseUrl() + "/uploads/images/origin/" + item.getProfile());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CheckReservationActivity.class);
                intent.putExtra("reserve", item.getReserve());
                intent.putExtra("status", item.getStatus());
                intent.putExtra("member", item.getFriend());
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    class ViewHolder {
        ImageView sitter_photo, status;
        TextView name, term;
    }

    private void circleImage(ImageView imageView, String getImg) {
        Glide.with(context)
                .load(getImg)
                .apply(RequestOptions.bitmapTransform(new CropCircleTransformation()))
                .into(imageView);
    }
}
