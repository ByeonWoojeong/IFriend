package com.woojeong.global.ifriend;

import android.content.Context;
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
import com.woojeong.global.ifriend.DTO.Review;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class ReviewDetailsAdapter extends BaseAdapter {
    private static final String TAG = "ReviewDetailsAdapter";
    Context context;
    int layout;
    ArrayList<Review> data;
    ListView listView;
    AQuery aQuery = null;
    ReviewDetailsAdapter reviewDetailsAdapter = this;


    public ReviewDetailsAdapter(Context context, int layout, ArrayList<Review> data, ListView listView) {
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
            vh.photo = convertView.findViewById(R.id.photo);
            vh.sitter_photo = convertView.findViewById(R.id.sitter_photo);
            vh.content_img = convertView.findViewById(R.id.content_img);
            vh.name = convertView.findViewById(R.id.name);
            vh.review_date = convertView.findViewById(R.id.review_date);
            vh.content_img_con = convertView.findViewById(R.id.content_img_con);
            vh.content = convertView.findViewById(R.id.content);
            vh.comment_con = convertView.findViewById(R.id.comment_con);
            vh.comment = convertView.findViewById(R.id.comment);
            vh.comment_date = convertView.findViewById(R.id.comment_date);
            vh.rating_bar = convertView.findViewById(R.id.rating_bar);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        final Review item = data.get(position);

        circleImage(vh.photo, ServerUrl.getBaseUrl() + "/uploads/images/origin/" + item.getProfile());

        vh.name.setText(item.getName());
        vh.content.setText(item.getContent());
        Float star;
        if(item.getStar().equals("null")){
            star = 0.0f;
        }else{
            star = Float.parseFloat(item.getStar());
        }
        vh.rating_bar.setProgress(Math.round(star / 2));
        vh.review_date.setText(item.getDate());

        if("".equals(item.getImage()) || null == item.getImage() || "null".equals(item.getImage())){
            vh.content_img_con.setVisibility(View.GONE);
        }else {
            vh.content_img_con.setVisibility(View.VISIBLE);
            Glide.with(context).load(ServerUrl.getBaseUrl() + "/uploads/images/origin/" + item.getImage()).clone().into(vh.content_img);
        }

        circleImage(vh.sitter_photo, ServerUrl.getBaseUrl() + "/uploads/images/origin/" + item.getFriendImage());

        if("".equals(item.getComment()) || null == item.getComment() || "null".equals(item.getComment())){
            vh.comment_con.setVisibility(View.GONE);
        }else {
            vh.comment_con.setVisibility(View.VISIBLE);
            vh.comment.setText(item.getComment());
            vh.comment_date.setText(item.getCommentDate());
        }

        return convertView;
    }

    class ViewHolder{
        ImageView photo, sitter_photo, content_img;
        TextView name, review_date, content, comment, comment_date;
        MaterialRatingBar rating_bar;
        LinearLayout comment_con;
        FrameLayout content_img_con;
    }

    private void circleImage(ImageView imageView, String getImg){
        Log.i(TAG, " circleImage : " + getImg);
        Glide.with(context)
                .load(getImg)
                .apply(RequestOptions.bitmapTransform(new CropCircleTransformation()))
                .into(imageView);
    }
}
