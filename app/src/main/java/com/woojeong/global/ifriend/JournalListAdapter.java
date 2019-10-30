package com.woojeong.global.ifriend;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.woojeong.global.ifriend.DTO.JournalList;
import com.woojeong.global.ifriend.DTO.Two;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class JournalListAdapter extends BaseAdapter {
    Context context;
    int layout;
    ArrayList<JournalList> data;
    ListView listView;
    AQuery aQuery = null;
    JournalListAdapter journalListAdapter = this;
    String who;

    public JournalListAdapter(Context context, int layout, ArrayList<JournalList> data, ListView listView, String who) {
        this.context = context;
        this.layout = layout;
        this.data = data;
        this.listView = listView;
        this.who = who;
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
            vh.container = convertView.findViewById(R.id.container);
            vh.sitter_photo_con = convertView.findViewById(R.id.sitter_photo_con);
            vh.write_review_con = convertView.findViewById(R.id.write_review_con);
            vh.sitter_photo = convertView.findViewById(R.id.sitter_photo);
            vh.status = convertView.findViewById(R.id.status);
            vh.name = convertView.findViewById(R.id.name);
            vh.period = convertView.findViewById(R.id.period);
            vh.write_review = convertView.findViewById(R.id.write_review);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        final JournalList item = data.get(position);
        vh.name.setText(item.getFriend_name());
        vh.period.setText(item.getPeriod());
        vh.write_review_con.setVisibility(View.GONE);

        if("sitter".equals(who)){
            circleImage(vh.sitter_photo, ServerUrl.getBaseUrl() + "/uploads/images/origin/" + item.getProfile());
            if("방문중".equals(item.getStatus())){
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, JournalListActivity.class);
                        intent.putExtra("write", "yes");
                        intent.putExtra("reserve", item.getReserve());
                        context.startActivity(intent);

                    }
                });
            }
        } else if("mom".equals(who)){
            circleImage(vh.sitter_photo, ServerUrl.getBaseUrl() + "/uploads/images/origin/" + item.getProfile());
            if("방문완료".equals(item.getStatus())){
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, JournalListActivity.class);
                        intent.putExtra("write", "no");
                        intent.putExtra("reserve", item.getReserve());
                        context.startActivity(intent);

                    }
                });
            } else if("방문중".equals(item.getStatus())){
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, JournalListActivity.class);
                        intent.putExtra("write", "no");
                        intent.putExtra("reserve", item.getReserve());
                        context.startActivity(intent);
                    }
                });
            }
        }

        if("방문중".equals(item.getStatus())){
            vh.status.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.visiting));
        } else if("승인완료".equals(item.getStatus())){
            vh.status.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.approved));
        } else if("방문완료".equals(item.getStatus())){
            vh.status.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.visited));
            if("sitter".equals(who)){
                vh.write_review_con.setVisibility(View.VISIBLE);
                vh.container.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, JournalListActivity.class);
                        intent.putExtra("write", "no");
                        intent.putExtra("reserve", item.getReserve());
                        context.startActivity(intent);
                    }
                });
            }
        } else if("결제완료".equals(item.getStatus())){
            vh.status.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.payment_complete));
        }

        if("1".equals(item.getReview())){
            vh.write_review.setText("후기 수정");
        } else {
            vh.write_review.setText("후기 작성");
        }

        vh.write_review_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, WriteReviewActivity.class);
                intent.putExtra("mode", who);
                intent.putExtra("reserve", item.getReserve());
                if("sitter".equals(who)){

                    if("1".equals(item.getReview())){
                        intent.putExtra("write", "edit");
                    } else {
                        intent.putExtra("write", "write");
                    }
                    intent.putExtra("idx", item.getMother_idx());
                    intent.putExtra("image", item.getProfile());
                    intent.putExtra("name", item.getMother_name());
                } else if("mom".equals(who)){
                    intent.putExtra("idx", item.getFriend_idx());
                    intent.putExtra("image", item.getProfile());
                    intent.putExtra("name", item.getFriend_name());
                }
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    class ViewHolder{

        LinearLayout container;
        FrameLayout sitter_photo_con, write_review_con;
        ImageView sitter_photo, status;

        TextView name, period, write_review;
    }

    private void circleImage(ImageView imageView, String getImg){
        Glide.with(context)
                .load(getImg)
                .apply(RequestOptions.bitmapTransform(new CropCircleTransformation()))
                .into(imageView);
    }
}
