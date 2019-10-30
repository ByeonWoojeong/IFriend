package com.woojeong.global.ifriend;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import me.relex.circleindicator.CircleIndicator;

import static com.woojeong.global.ifriend.GlobalApplication.setDarkMode;

public class TutorialActivity extends AppCompatActivity {

    TutorialPagerAdapter tutorialPagerAdapter;
    ViewPager viewpager;
    CircleIndicator circle;
    ImageView tutorial_close;
    Animation tutorial_open, fade_out;
    boolean animChecked = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#ffffff"));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                setDarkMode(TutorialActivity.this, true);
                }
        }

        tutorialPagerAdapter = new TutorialPagerAdapter(TutorialActivity.this, 3) {
            @Override public int getItemPosition(Object object) {
                return POSITION_NONE;
            }
        };
        viewpager = findViewById(R.id.tutorial_viewpager);
        circle = findViewById(R.id.circle);
        viewpager.setAdapter(tutorialPagerAdapter);
        circle.setViewPager(viewpager);
        tutorialPagerAdapter.registerDataSetObserver(circle.getDataSetObserver());
        tutorial_close = (ImageView) findViewById(R.id.tutorial_close);
        tutorial_open = AnimationUtils.loadAnimation(this, R.anim.tutorial_open);
        fade_out = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        viewpager.addOnPageChangeListener (new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int arg0) {
            }

            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            public void onPageSelected(int currentPage) {
                if (currentPage == 0) {
                    if (!animChecked) {
                        tutorial_close.startAnimation(fade_out);
                        tutorial_close.setClickable(false);
                        animChecked = true;
                    }
                } else if (currentPage == 1) {
                    if (!animChecked) {
                        tutorial_close.startAnimation(fade_out);
                        tutorial_close.setClickable(false);
                        animChecked = true;
                    }
                } else if (currentPage == 2) {
                    if (animChecked) {
                        tutorial_close.startAnimation(tutorial_open);
                        tutorial_close.setClickable(true);
                        animChecked = false;
                    }
                }
            }
        });
        tutorial_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences isFirst = TutorialActivity.this.getSharedPreferences("isFirst", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = isFirst.edit();
                editor.clear();
                editor.putBoolean("isFirst", false);
                editor.commit();

                Intent intent = new Intent(TutorialActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(TutorialActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}
