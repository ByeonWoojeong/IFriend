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
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.woojeong.global.ifriend.DTO.JournalDetails;
import com.woojeong.global.ifriend.DTO.JournalList;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.BitmapTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class JournalDetailsAdapter extends BaseAdapter {
    private static final String TAG = "JournalDetailsAdapter";
    Context context;
    int layout;
    ArrayList<JournalDetails> data;
    ListView listView;
    AQuery aQuery = null;
    JournalDetailsAdapter journalListAdapter = this;

    public JournalDetailsAdapter(Context context, int layout, ArrayList<JournalDetails> data, ListView listView) {
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
            vh.image_con = convertView.findViewById(R.id.image_con);
            vh.image1 = convertView.findViewById(R.id.image1);
            vh.image2 = convertView.findViewById(R.id.image2);
            vh.sitter_photo = convertView.findViewById(R.id.sitter_photo);
            vh.data_n_time = convertView.findViewById(R.id.data_n_time);
            vh.content = convertView.findViewById(R.id.content);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        final JournalDetails item = data.get(position);

        vh.image1.setVisibility(View.GONE);
        vh.image2.setVisibility(View.GONE);

        circleImage(vh.sitter_photo, ServerUrl.getBaseUrl() + "/uploads/images/origin/" + item.getFriend_profile());

        vh.data_n_time.setText(item.getDate());
        vh.content.setText(item.getContent());

        if (!"".equals(item.getImages()) || null != item.getImages()) {
            Log.i("JournalListActivity", " 이미지 있음");
            vh.image_con.setVisibility(View.VISIBLE);

            String getImages[] = item.getImages().split(",");

            for (int i = 0; i < getImages.length; i++) {
                Log.i("JournalListActivity", " getImages " + getImages[i]);
            }
            if ("null".equals(item.getImages()) || getImages.length == 0) {
                Log.i("JournalListActivity", " getImages Length 0 " + getImages.length);
                vh.image_con.setVisibility(View.GONE);
                vh.image1.setVisibility(View.GONE);
                vh.image2.setVisibility(View.GONE);
            } else if (getImages.length == 1) {
                Log.i("JournalListActivity", " getImages Length 1 " + getImages.length);
                vh.image1.setVisibility(View.VISIBLE);
                Glide.with(context).load(ServerUrl.getBaseUrl() + "/uploads/images/origin/" + getImages[0]).into(vh.image1);
            } else if (getImages.length == 2) {
                Log.i("JournalListActivity", " getImages Length 2 " + getImages.length);
                vh.image1.setVisibility(View.VISIBLE);
                vh.image2.setVisibility(View.VISIBLE);
                Glide.with(context).load(ServerUrl.getBaseUrl() + "/uploads/images/origin/" + getImages[0]).into(vh.image1);
                Glide.with(context).load(ServerUrl.getBaseUrl() + "/uploads/images/origin/" + getImages[1]).into(vh.image2);
            }

        } else {
            vh.image_con.setVisibility(View.GONE);
            vh.image1.setVisibility(View.GONE);
            vh.image2.setVisibility(View.GONE);
        }

        return convertView;
    }

    private void circleImage(ImageView imageView, String getImg) {
        Log.i(TAG, " circleImage : " + getImg);
        Glide.with(context)
                .load(getImg)
                .apply(RequestOptions.bitmapTransform(new CropCircleTransformation()))
                .into(imageView);
    }

    class ViewHolder {
        LinearLayout image_con;
        ImageView sitter_photo, image1, image2;
        TextView data_n_time, content;
    }
}
