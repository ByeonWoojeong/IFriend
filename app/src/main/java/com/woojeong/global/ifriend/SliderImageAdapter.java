package com.woojeong.global.ifriend;

import android.util.Log;

import com.woojeong.global.ifriend.ServerUrl;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import ss.com.bannerslider.adapters.SliderAdapter;
import ss.com.bannerslider.viewholder.ImageSlideViewHolder;

public class SliderImageAdapter extends SliderAdapter {

    JSONArray imageUrl;

    public SliderImageAdapter(JSONArray imageUrl) {
        this.imageUrl = imageUrl;

    }

    public String getUrl(int position) {
        String link = null;
        try{
           link =  imageUrl.getJSONObject(position).getString("link");
        }catch (JSONException e){
        }
        return link;
    }

    @Override
    public int getItemCount() {
        return imageUrl.length();
    }

    @Override
    public void onBindImageSlide(int position, ImageSlideViewHolder viewHolder) {
        try{
            viewHolder.bindImageSlide(ServerUrl.getBaseUrl() + "/uploads/images/origin/" + imageUrl.getJSONObject(position).getString("image"));
        }catch (JSONException e){
        }
    }
}