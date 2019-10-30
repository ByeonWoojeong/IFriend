package com.woojeong.global.ifriend;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class TutorialPagerAdapter extends PagerAdapter {

    Context context;
    int mSize;
    float tutorial_width, tutorial_height;

    public TutorialPagerAdapter(Context context, int count) {
        this.context = context;
        this.mSize = count;
        this.tutorial_width = context.getResources().getDimension(R.dimen.tutorial_width);
        this.tutorial_height = context.getResources().getDimension(R.dimen.tutorial_height);
    }

    @Override public int getCount() {
        return mSize;
    }

    @Override public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override public void destroyItem(ViewGroup view, int position, Object object) {
        view.removeView((View) object);
    }

    @Override public Object instantiateItem(ViewGroup view, int position) {
        FrameLayout frameLayout = new FrameLayout(view.getContext());
        ImageView imageView = new ImageView(view.getContext());
        final int imageResource = context.getResources().getIdentifier("@drawable/tutorial_img" + position, null, "com.woojeong.global.ifriend");
        imageView.setBackgroundResource(imageResource);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams((int) tutorial_width, (int) tutorial_height);
        params.gravity = Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL;
        imageView.setLayoutParams(params);
        frameLayout.addView(imageView);
        view.addView(frameLayout, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        return frameLayout;
    }
}