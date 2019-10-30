package com.woojeong.global.ifriend;


import android.app.Activity;
import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.google.firebase.messaging.RemoteMessage;
import com.gun0912.tedpermission.util.ObjectUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import me.leolin.shortcutbadger.ShortcutBadger;

import static com.woojeong.global.ifriend.GlobalApplication.applicationLifecycleHandler;
import static com.woojeong.global.ifriend.GlobalApplication.getOtherLogin;


public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    private static String TAG = "FirebaseMessagingService";

    Context context = this;
    AQuery aQuery = null;
    SQLiteDatabase database;

    @SuppressWarnings("handlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if ("1".equals(msg.obj.toString().trim())) {
                Toast.makeText(FirebaseMessagingService.this, "다른 기기에서 로그인을 시도하여 앱을 종료합니다.", Toast.LENGTH_SHORT).show();
            } else if ("2".equals(msg.obj.toString().trim())) {
                Toast.makeText(FirebaseMessagingService.this, "다른 기기에서 로그인을 시도하여 앱을 종료합니다.", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.i(TAG, " getData: " + remoteMessage.getData());
        if ("text".equals(remoteMessage.getData().get("type").toString().trim())) {

            if("reserve".equals(remoteMessage.getData().get("case").toString().trim())){
                if (applicationLifecycleHandler.isInBackground()) {
                    sendNotificationReserve(remoteMessage.getData().get("message").toString().trim(), remoteMessage.getData().get("key").toString().trim());
                } else {
                    sendNotificationReserve(remoteMessage.getData().get("message").toString().trim(), remoteMessage.getData().get("key").toString().trim());
                }
            } else {
                if (applicationLifecycleHandler.isInBackground()) {
                    sendNotificationText(remoteMessage.getData().get("message").toString().trim(), remoteMessage.getData().get("link").toString().trim());
                } else {
                    sendNotificationText(remoteMessage.getData().get("message").toString().trim(), remoteMessage.getData().get("link").toString().trim());
                }
            }
        } else if ("image".equals(remoteMessage.getData().get("type").toString().trim())) {
            if (applicationLifecycleHandler.isInBackground()) {
                if (ObjectUtils.isEmpty(remoteMessage.getData().get("message"))) {
                    sendNotificationImage("", remoteMessage.getData().get("image").toString().trim(), remoteMessage.getData().get("link").toString().trim());
                } else {
                    sendNotificationImage(remoteMessage.getData().get("message").toString().trim(), remoteMessage.getData().get("image").toString().trim(), remoteMessage.getData().get("link").toString().trim());
                }
            } else {
                if (ObjectUtils.isEmpty(remoteMessage.getData().get("message"))) {
                    sendNotificationImage("", remoteMessage.getData().get("image").toString().trim(), remoteMessage.getData().get("link").toString().trim());
                } else {
                    sendNotificationImage(remoteMessage.getData().get("message").toString().trim(), remoteMessage.getData().get("image").toString().trim(), remoteMessage.getData().get("link").toString().trim());
                }
            }
        } else if ("notice".equals(remoteMessage.getData().get("type").toString().trim())) {
            if (applicationLifecycleHandler.isInBackground()) {
                sendNotificationNotice(remoteMessage.getData().get("ntitle").toString().trim(), remoteMessage.getData().get("link").toString().trim(), remoteMessage.getData().get("ncontent").toString().trim(), remoteMessage.getData().get("image").toString().trim(), remoteMessage.getData().get("date").toString().trim(), false);
            } else {
                sendNotificationNotice(remoteMessage.getData().get("ntitle").toString().trim(), remoteMessage.getData().get("link").toString().trim(), remoteMessage.getData().get("ncontent").toString().trim(), remoteMessage.getData().get("image").toString().trim(), remoteMessage.getData().get("date").toString().trim(), true);
            }
        }
        if ("chat".equals(remoteMessage.getData().get("type").toString().trim())) {
            Intent intent = new Intent("ifriend_push");
            sendBroadcast(intent);
            if (applicationLifecycleHandler.isInBackground()) {
                sendNotificationChat(remoteMessage.getData().get("message").toString().trim(), remoteMessage.getData().get("room").toString().trim(), remoteMessage.getData().get("mode").toString().trim(), remoteMessage.getData().get("name").toString().trim());
            } else {
                sendNotificationChat(remoteMessage.getData().get("message").toString().trim(), remoteMessage.getData().get("room").toString().trim(), remoteMessage.getData().get("mode").toString().trim(), remoteMessage.getData().get("name").toString().trim());
            }
        } else if ("logout".equals(remoteMessage.getData().get("type"))) {
            SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
            boolean loginChecked = pref.getBoolean("loginChecked", false);
            if (loginChecked) {
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.commit();
                SharedPreferences autoLogin = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor2 = autoLogin.edit();
                editor2.clear();
                editor2.commit();
                ShortcutBadger.removeCount(getApplicationContext());
                if (!applicationLifecycleHandler.isInBackground()) {
                    Message msg = handler.obtainMessage();
                    msg.obj = "1";
                    handler.sendMessage(msg);
                    try {
                        Thread.sleep(3000);
                        Intent killApp = new Intent(FirebaseMessagingService.this, MainActivity.class);
                        killApp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        killApp.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        killApp.putExtra("KILL_APP", true);
                        startActivity(killApp);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    getOtherLogin = true;
                    Message msg = handler.obtainMessage();
                    msg.obj = "2";
                    handler.sendMessage(msg);
                }
            } else {
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.commit();
                SharedPreferences autoLogin = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor2 = autoLogin.edit();
                editor2.clear();
                editor2.commit();
                ShortcutBadger.removeCount(getApplicationContext());
            }
        }
    }

    private void handleNow() {
        Log.d(TAG, "Short lived task is done.");
    }


    private void sendNotification(String messageBody) {

        Intent intent = new Intent(this, MainActivity.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,

                PendingIntent.FLAG_ONE_SHOT);


        String channelId = "default_notification_channel_id";

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder =

                new NotificationCompat.Builder(this, channelId)

                        .setSmallIcon(R.mipmap.ic_launcher)

                        .setContentTitle("FCM Message")

                        .setContentText(messageBody)

                        .setAutoCancel(true)

                        .setSound(defaultSoundUri)

                        .setContentIntent(pendingIntent);


        NotificationManager notificationManager =

                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            String channelName = "default_notification_channel_name";

            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);

            notificationManager.createNotificationChannel(channel);

        }

        notificationManager.notify(0, notificationBuilder.build());

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void sendNotificationText(String text, String link) {
        Log.i(TAG, "sendNotificationText");

        Intent intent = null;
        if ("".equals(link)) {
            intent = new Intent(this, SplashActivity.class);
        } else {
            intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(link));
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, (int) (System.currentTimeMillis() / 1000), intent, PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder notificationBuilder = null;
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel notificationChannel = new NotificationChannel("아이친구", "아이친구", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(notificationChannel);
            notificationBuilder = new NotificationCompat.Builder(this, notificationChannel.getId());
        } else {
            notificationBuilder = new NotificationCompat.Builder(this);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setSmallIcon(getResources().getIdentifier("icon_noti", "drawable", getPackageName()));
//            notificationBuilder.setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.icon));
        } else {
            notificationBuilder.setSmallIcon(getResources().getIdentifier("icon", "drawable", getPackageName()));
        }
        notificationBuilder.setContentTitle("아이친구")
                .setContentText(text)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setLights(Color.RED, 3000, 3000)
                .setContentIntent(pendingIntent);
        notificationManager.notify(0, notificationBuilder.build());
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void sendNotificationImage(String text, String image, String link) {
        Intent intent = null;
        if ("".equals(link)) {
            intent = new Intent(this, SplashActivity.class);
        } else {
            intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(link));
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, (int) (System.currentTimeMillis() / 1000), intent, PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder notificationBuilder = null;
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel notificationChannel = new NotificationChannel("아이친구", "아이친구", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(notificationChannel);
            notificationBuilder = new NotificationCompat.Builder(this, notificationChannel.getId());
        } else {
            notificationBuilder = new NotificationCompat.Builder(this);
        }
        try {
            URL url = new URL(image);
            Bitmap getBitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                notificationBuilder.setSmallIcon(getResources().getIdentifier("icon_noti", "drawable", getPackageName()));
//                notificationBuilder.setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.icon));
            } else {
                notificationBuilder.setSmallIcon(getResources().getIdentifier("icon", "drawable", getPackageName()));
            }
            notificationBuilder.setContentTitle("아이친구")
                    .setContentText("아래로 드래그하여 알림을 확인해주세요.")
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                    .setLights(Color.RED, 3000, 3000)
                    .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(getBitmap).setBigContentTitle("아이친구").setSummaryText(text))
                    .setContentIntent(pendingIntent);
            notificationManager.notify(0, notificationBuilder.build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void sendNotificationReserve(String text, String key) {
        Log.i(TAG, "sendNotificationReserve");
        Intent intent = null;
        if (!"".equals(key)) {
            intent = new Intent(this, CheckReservation2Activity.class);
            intent.putExtra("reserve", key);
        } else {
            intent = new Intent(this, SplashActivity.class);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, (int) (System.currentTimeMillis() / 1000), intent, PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder notificationBuilder = null;
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel notificationChannel = new NotificationChannel("아이친구", "아이친구", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(notificationChannel);
            notificationBuilder = new NotificationCompat.Builder(this, notificationChannel.getId());
        } else {
            notificationBuilder = new NotificationCompat.Builder(this);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setSmallIcon(getResources().getIdentifier("icon_noti", "drawable", getPackageName()));
//            notificationBuilder.setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.icon));
        } else {
            notificationBuilder.setSmallIcon(getResources().getIdentifier("icon", "drawable", getPackageName()));
        }
        notificationBuilder.setContentTitle("아이친구")
                .setContentText(text)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setLights(Color.RED, 3000, 3000)
                .setContentIntent(pendingIntent);
        notificationManager.notify(0, notificationBuilder.build());
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void sendNotificationNotice(String text, String link, String content, String image, String date, boolean isShown) {
        Intent intent = null;
        if (isShown) {
            intent = new Intent(this, NoticeActivity.class);
        } else {
            intent = new Intent(this, MainActivity.class);
        }
        intent.putExtra("isNotice", "ok");
        intent.putExtra("title", text);
        intent.putExtra("content", content);
        intent.putExtra("image", image);
        intent.putExtra("date", date);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, (int) (System.currentTimeMillis() / 1000), intent, PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel notificationChannel = new NotificationChannel("아이친구", "아이친구", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(notificationChannel);
            notificationBuilder = new NotificationCompat.Builder(this, notificationChannel.getId());
        } else {
            notificationBuilder = new NotificationCompat.Builder(this);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setSmallIcon(getResources().getIdentifier("icon_noti", "drawable", getPackageName()));
            notificationBuilder.setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.icon));
        } else {
            notificationBuilder.setSmallIcon(getResources().getIdentifier("icon", "drawable", getPackageName()));
        }
        notificationBuilder.setContentTitle("아이친구 공지사항")
                .setContentText(text)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setLights(Color.RED, 3000, 3000)
                .setContentIntent(pendingIntent);
        notificationManager.notify(2554, notificationBuilder.build());
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void sendNotificationChat(final String text, final String idx, final String mode, final String name) {
        Intent intent = new Intent(context, ChatDetailsActivity.class);
        intent.putExtra("mode", mode);
        intent.putExtra("member", idx);
        intent.putExtra("name", name);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, (int) (System.currentTimeMillis() / 1000), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        // FLAG_ONE_SHOT, (int) (System.currentTimeMillis() / 1000)
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel notificationChannel = new NotificationChannel("아이친구", "아이친구", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(notificationChannel);
            notificationBuilder = new NotificationCompat.Builder(this, notificationChannel.getId());
        } else {
            notificationBuilder = new NotificationCompat.Builder(this);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setSmallIcon(getResources().getIdentifier("icon_noti", "drawable", getPackageName()));
            notificationBuilder.setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.icon));
        } else {
            notificationBuilder.setSmallIcon(getResources().getIdentifier("icon", "drawable", getPackageName()));
        }
        notificationBuilder.setContentTitle(name + "님의 채팅 알림")
                .setContentText(text)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setLights(Color.RED, 3000, 3000)
                .setContentIntent(pendingIntent);

        notificationManager.notify(2554, notificationBuilder.build());
    }
}
