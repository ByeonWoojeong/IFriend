package com.woojeong.global.ifriend;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

//앱 설치 경로 받아오기

public class InstallReferrerReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String referrer = intent.getStringExtra("referrer");
        if (referrer == null || referrer.length() == 0) {
            return;
        }
//        Toast.makeText(context, referrer+"", Toast.LENGTH_SHORT).show();
    }
}