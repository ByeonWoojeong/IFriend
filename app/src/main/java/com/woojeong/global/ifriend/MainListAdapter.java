package com.woojeong.global.ifriend;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.smarteist.autoimageslider.DefaultSliderView;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderLayout;
import com.smarteist.autoimageslider.SliderView;
import com.woojeong.global.ifriend.DTO.Main;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class MainListAdapter extends BaseAdapter {
    private static final String TAG = "MainListAdapter";
    Context context;
    int layout;
    ArrayList<Main> data;
    ListView listView;
    AQuery aQuery = null;
    //    Main item;
    MainListAdapter mainListAdapter = this;
    MainFirstFragment mainFirstFragment;
    String type;
    String getLike = "";

    public MainListAdapter(Context context, int layout, ArrayList<Main> data, ListView listView, MainFirstFragment mainFirstFragment) {
        this.context = context;
        this.layout = layout;
        this.data = data;
        this.listView = listView;
        this.mainFirstFragment = mainFirstFragment;
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

        convertView = View.inflate(context, layout, null);
        vh = new ViewHolder();
        vh.sliderLayout = convertView.findViewById(R.id.main_home_slider);
        vh.sale_con = convertView.findViewById(R.id.sale_con);
        vh.sale_txt = convertView.findViewById(R.id.sale_txt);
        vh.ifriend_info_con = convertView.findViewById(R.id.sitter_info_con);
        vh.sitter_photo_con = convertView.findViewById(R.id.sitter_photo_con);
        vh.sitter_introduce_con = convertView.findViewById(R.id.sitter_introduce_con);
        vh.ifriend_info_con2 = convertView.findViewById(R.id.sitter_info_con2);

        vh.title = convertView.findViewById(R.id.title);
        vh.weekday_price = convertView.findViewById(R.id.weekday_price);
        vh.weekend_price = convertView.findViewById(R.id.weekend_price);

        vh.sitter_name = convertView.findViewById(R.id.sitter_name);
        vh.sitter_location = convertView.findViewById(R.id.sitter_location);
        vh.review_count = convertView.findViewById(R.id.review_count);
        vh.sitter_photo = convertView.findViewById(R.id.sitter_photo);
        vh.like = convertView.findViewById(R.id.like);
        vh.like_con = convertView.findViewById(R.id.like_con);
        vh.rating_bar = convertView.findViewById(R.id.rating_bar);
        convertView.setTag(vh);

        final Main item = data.get(position);
        vh.title.setText(item.getTitle());
        vh.weekday_price.setText(item.getWeekdayPrice());
        vh.weekend_price.setText(item.getWeekendPrice());
        vh.sitter_name.setText(item.getName());
        vh.sitter_location.setText(item.getLocation());

        if ("1".equals(item.getIsLike())) {
            vh.like.setChecked(true);
        } else {
            vh.like.setChecked(false);
        }

        if ("null".equals(item.getDiscount()) || null == item.getDiscount()) {
            vh.sale_con.setVisibility(View.GONE);
        } else {
            vh.sale_con.setVisibility(View.VISIBLE);
            vh.sale_txt.setText((Integer.parseInt(item.getDiscount()) * 10) + "%");
        }

        vh.review_count.setText(item.getReviewCount());

        vh.sliderLayout.setIndicatorAnimation(IndicatorAnimations.SWAP);
        vh.sliderLayout.setSliderTransformAnimation(SliderAnimations.FADETRANSFORMATION);
        vh.sliderLayout.setAutoScrolling(false);


        final ViewHolder finalVh = vh;

        vh.sliderLayout.clearSliderViews();

        for (int i = 0; i < item.getHomeImgId().length; i++) {
            DefaultSliderView sliderView = new DefaultSliderView(context);
            sliderView.setImageScaleType(ImageView.ScaleType.FIT_CENTER);
            sliderView.setOnSliderClickListener(new SliderView.OnSliderClickListener() {
                @Override
                public void onSliderClick(SliderView sliderView) {
                    finalVh.sitter_introduce_con.callOnClick();
                }
            });
            Log.i(TAG, " image " + item.getHomeImgId()[i]);
            sliderView.setImageUrl(ServerUrl.getBaseUrl() + "/uploads/images/origin/" + item.getHomeImgId()[i]);
            vh.sliderLayout.addSliderView(sliderView);
        }

        Log.i(TAG, " position " + position);

        vh.sitter_introduce_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AboutSitterActivity.class);

                if (!"like".equals(type)) {
                    intent.putExtra("lat", mainFirstFragment.startLatitude);
                    intent.putExtra("lng", mainFirstFragment.startLongitude);
                }
                intent.putExtra("member", item.getMember());
                intent.putExtra("name", item.getName());
                intent.putExtra("addr", item.getLocation());
                intent.putExtra("title", item.getTitle());
                intent.putExtra("pay1", item.getWeekdayPrice());
                intent.putExtra("pay2", item.getWeekendPrice());
                intent.putExtra("discount", item.getDiscount());
                intent.putExtra("reviewcnt", item.getReviewCount());
                intent.putExtra("star", item.getStar());
                context.startActivity(intent);
            }
        });
        vh.ifriend_info_con2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalVh.sitter_introduce_con.callOnClick();
            }
        });
        vh.sitter_photo_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalVh.sitter_introduce_con.callOnClick();
            }
        });

        vh.like_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalVh.like.callOnClick();
            }
        });
        vh.like.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                SharedPreferences get_token = context.getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
                final String getToken = get_token.getString("Token", "");
                String url = ServerUrl.getBaseUrl() + "/friend/like";
                final Map<String, Object> params = new HashMap<String, Object>();
                params.put("friend", item.getMember());
                Log.i(TAG, " " + params);

                if (isChecked) {

                    aQuery.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
                        @Override
                        public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
                            Log.i(TAG, " " + jsonObject);
                            try {
                                if (jsonObject.getBoolean("return")) {    //return이 true 면?

                                    if ("1".equals(jsonObject.getString("like"))) {
                                    } else if ("0".equals(jsonObject.getString("like"))) {
                                    }

                                } else if (!jsonObject.getBoolean("return")) {
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }.header("EPOCH-AGENT", "" + getToken).header("User-Agent", "android"));

                } else {
                    aQuery.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
                        @Override
                        public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
                            Log.i(TAG, " " + jsonObject);
                            try {
                                if (jsonObject.getBoolean("return")) {    //return이 true 면?

                                    if ("1".equals(jsonObject.getString("like"))) {

                                    } else if ("0".equals(jsonObject.getString("like"))) {

                                    }

                                } else if (!jsonObject.getBoolean("return")) {
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }.header("EPOCH-AGENT", "" + getToken).header("User-Agent", "android"));
                }
            }
        });
        circleImage(vh.sitter_photo, item.getProfile());

        Float star;
        if (item.getStar().equals("null")) {
            star = 0.0f;
        } else {
            star = Float.parseFloat(item.getStar());
        }
        vh.rating_bar.setProgress(Math.round(star * 2));
        return convertView;
    }

    class ViewHolder {
        SliderLayout sliderLayout;
        FrameLayout like_con, sale_con, sitter_photo_con;
        LinearLayout ifriend_info_con, sitter_introduce_con, ifriend_info_con2;
        TextView sale_txt, title, sitter_name, sitter_location, review_count, weekday_price, weekend_price;
        ImageView sitter_photo;
        CheckBox like;
        MaterialRatingBar rating_bar;
    }

    private void circleImage(ImageView imageView, String url) {
        Glide.with(context)
                .load(ServerUrl.getBaseUrl() + "/uploads/images/origin/" + url)
                .apply(RequestOptions.bitmapTransform(new CropCircleTransformation()))
                .into(imageView);
    }

}
