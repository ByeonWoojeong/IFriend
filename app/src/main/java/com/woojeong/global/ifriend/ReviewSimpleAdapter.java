package com.woojeong.global.ifriend;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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

public class ReviewSimpleAdapter extends BaseAdapter {
    private static final String TAG = "ReviewDetailsAdapter";
    Context context;
    int layout;
    ArrayList<Review> data;
    ListView listView;
    AQuery aQuery = null;
    ReviewSimpleAdapter reviewDetailsAdapter = this;

    public ReviewSimpleAdapter(Context context, int layout, ArrayList<Review> data, ListView listView) {
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
            vh.name = convertView.findViewById(R.id.name);
            vh.content = convertView.findViewById(R.id.content);
            vh.rating_bar = convertView.findViewById(R.id.rating_bar);
            vh.date = convertView.findViewById(R.id.date);
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
        vh.rating_bar.setProgress(Math.round(star * 2));
        vh.date.setText(item.getDate());

        return convertView;
    }

    class ViewHolder{
        ImageView photo;
        TextView name, date, content;
        MaterialRatingBar rating_bar;
    }

    private void circleImage(ImageView imageView, String getImg){
        Log.i(TAG, " circleImage : " + getImg);
        Glide.with(context)
                .load(getImg)
                .apply(RequestOptions.bitmapTransform(new CropCircleTransformation()))
                .into(imageView);
    }
}
