package com.woojeong.global.ifriend;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.kyleduo.switchbutton.SwitchButton;
import com.woojeong.global.ifriend.Chatting.ChatMessage;
import com.woojeong.global.ifriend.Chatting.ChatMessageAdapter;
import com.wunderlist.slidinglayer.SlidingLayer;
import com.yalantis.guillotine.animation.GuillotineAnimation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import static com.kakao.auth.StringSet.msg;
import static com.woojeong.global.ifriend.GlobalApplication.setDarkMode;

public class ChatDetailsActivity extends AppCompatActivity {
    private static String TAG = "ChatDetailsActivity";
    Context context;
    InputMethodManager ipmm;
    AQuery aQuery = null;

    OneBtnDialog oneBtnDialog;
    CameraDialog cameraDialog;
    AboutChatDialog aboutChatDialog;


    Toolbar toolbar;
    FrameLayout back_con, report_con, hidden_form, chat_form, btn_camera_con, btn_send_con;
    ImageView back, status, down, report, profile_icon, phone_icon, reservation_icon, pencil_icon, fold_icon;
    ImageButton btn_camera;
    EditText chat_edt;
    TextView name, profile, phone, reservation, pencil, hidden_message, btn_send;

    FrameLayout sliding_layer;
    LinearLayout title_con, profile_con, phone_con, reservation_con, pencil_con, fold_con;
    GuillotineAnimation guillotineAnimation;
    boolean guillotineAnimationIsOpen, reload = true, hidden;

    private final int CAMERA = 700;
    private final int GALLERY = 800;
    Bitmap originalBitmap, resizeBitmap;

    Intent getIntent;
    String mode, getToken, getMember, getStatus, getReserve, getRoom, getMode, getPhone, getName, getType, getFileName, getImage1, lastDate = "", imagePath;
    ContentResolver contentResolver;

    ListView ping_pong_list;
    ChatMessageAdapter chatMessageAdapter;
    ArrayList<ChatMessage> data;

    JSONObject jsonObject;
    Socket socket;
    SQLiteDatabase database;
    SwitchButton subSwitch;

    String getId = "";

    SharedPreferences prefLoginChecked, preferencesMember;
    SharedPreferences.Editor editor;

    public final Handler mHandler = new Handler() { // 핸들러 처리부분
        public void handleMessage(Message msg) { // 메시지를 받는부분
            finish();
        }

        ;
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferencesMember = getSharedPreferences("member", Activity.MODE_PRIVATE);
        String intMember = preferencesMember.getString("index", "");
        String mem = getIntent().getStringExtra("member");
        setContentView(R.layout.activity_chat_details);
        ipmm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        chat_edt = findViewById(R.id.chat_edt);
        editor = preferencesMember.edit();
        try {
            socket = IO.socket("http://chat.ichingu.com/");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        if ((mem).equals(intMember)) {
            finish();
            return;
        } else {
            editor.putString("index", mem);
            editor.commit();
            Log.i(TAG, " idx " + intMember + " " + Integer.parseInt(getIntent().getStringExtra("member")));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#ffffff"));
            window.setBackgroundDrawable(null);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                setDarkMode(ChatDetailsActivity.this, true);
            }
        }

        SharedPreferences prefToken = getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
        getToken = prefToken.getString("Token", "");
        contentResolver = getContentResolver();

        prefLoginChecked = getSharedPreferences("modeChecked", Activity.MODE_PRIVATE);
        mode = prefLoginChecked.getString("mode", "");


        getMember = getIntent().getStringExtra("member");
        getName = getIntent().getStringExtra("name");
        getStatus = getIntent().getStringExtra("status");
        getReserve = getIntent().getStringExtra("reserve");

        if(null != getIntent().getStringExtra("mode") || !"".equals(getIntent().getStringExtra("mode"))){
            getMode = getIntent().getStringExtra("mode");
            mode = getMode;
            SharedPreferences.Editor editor = prefLoginChecked.edit();
            editor.putString("mode", mode);
            editor.commit();
        }
        Log.i(TAG, " mode " + mode);

        context = this;
        aQuery = new AQuery(this);

        back_con = findViewById(R.id.back_con);
        back = findViewById(R.id.back);
        back_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back.callOnClick();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        toolbar = findViewById(R.id.toolbar);
        down = findViewById(R.id.down);

        report_con = findViewById(R.id.report_con);
        hidden_form = findViewById(R.id.hidden_form);
        chat_form = findViewById(R.id.chat_form);
        btn_camera_con = findViewById(R.id.btn_camera_con);
        btn_send_con = findViewById(R.id.btn_send_con);

        status = findViewById(R.id.status);
        report = findViewById(R.id.report);
        profile_icon = findViewById(R.id.profile_icon);
        phone_icon = findViewById(R.id.phone_icon);
        reservation_icon = findViewById(R.id.reservation_icon);
        pencil_icon = findViewById(R.id.pencil_icon);
        fold_icon = findViewById(R.id.fold_icon);

        btn_camera = findViewById(R.id.btn_camera);

        name = findViewById(R.id.name);
        name.setText(getName);
        profile = findViewById(R.id.profile);
        phone = findViewById(R.id.phone);
        reservation = findViewById(R.id.reservation);
        pencil = findViewById(R.id.pencil);
        hidden_message = findViewById(R.id.hidden_message);
        btn_send = findViewById(R.id.btn_send);

        ping_pong_list = findViewById(R.id.ping_pong_list);
        data = new ArrayList<ChatMessage>();
        chatMessageAdapter = new ChatMessageAdapter(ChatDetailsActivity.this, R.layout.chat_message, data, ping_pong_list);
        chatMessageAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                ping_pong_list.post(new Runnable() {
                    public void run() {
                        ping_pong_list.getLastVisiblePosition();
                        Log.i(TAG, " 1111 " + ping_pong_list.getLastVisiblePosition());
                    }
                });
                Log.i(TAG, " 2222 " + chatMessageAdapter.getCount());
                if (ping_pong_list.getLastVisiblePosition() > chatMessageAdapter.getCount() - 3) {
                }
            }
        });

        sliding_layer = findViewById(R.id.sliding_layer);
        sliding_layer.setVisibility(View.VISIBLE);

        title_con = findViewById(R.id.title_con);
        profile_con = findViewById(R.id.profile_con);
        phone_con = findViewById(R.id.phone_con);
        reservation_con = findViewById(R.id.reservation_con);
        pencil_con = findViewById(R.id.pencil_con);
        fold_con = findViewById(R.id.fold_con);

//        down.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                name.callOnClick();
//            }
//        });
        title_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name.callOnClick();
            }
        });

//        name.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                sliding_layer.setVisibility(View.VISIBLE);
//                sliding_layer.openLayer(false);
//            }
//        });
        name.performClick();
        report_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                report.callOnClick();
            }
        });
        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aboutChatDialog = new AboutChatDialog(ChatDetailsActivity.this, "who");
                aboutChatDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                aboutChatDialog.setCancelable(false);
                aboutChatDialog.show();
            }
        });

        profile_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profile.callOnClick();
            }
        });
        profile_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profile.callOnClick();
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatDetailsActivity.this, CustomerProfileActivity.class);
                intent.putExtra("member", getMember);
                startActivity(intent);
            }
        });

        phone_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phone.callOnClick();
            }
        });
        phone_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phone.callOnClick();
            }
        });
        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + getPhone));
                startActivity(intent);
            }
        });

        reservation_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reservation.callOnClick();
            }
        });
        reservation_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reservation.callOnClick();
            }
        });
        reservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatDetailsActivity.this, CheckReservation2Activity.class);
                intent.putExtra("reserve", getReserve);
                startActivity(intent);
            }
        });

        pencil_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pencil.callOnClick();
            }
        });
        pencil_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pencil.callOnClick();
            }
        });
        pencil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatDetailsActivity.this, JournalListActivity.class);
                intent.putExtra("reserve", getReserve);
                intent.putExtra("write", "yes");
                startActivity(intent);
            }
        });

//        fold_con.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                fold_icon.callOnClick();
//            }
//        });
//        fold_icon.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                sliding_layer.setVisibility(View.GONE);
////                sliding_layer.closeLayer(true);
//            }
//        });

        if ("sitter".equals(mode)) {
            if ("0".equals(getStatus)) {
                status.setBackground(getResources().getDrawable(R.drawable.waiting_approval));
                profile_con.setVisibility(View.VISIBLE);
                phone_con.setVisibility(View.GONE);
                reservation_con.setVisibility(View.VISIBLE);
                pencil_con.setVisibility(View.GONE);
            } else if ("1".equals(getStatus)) {
                status.setBackground(getResources().getDrawable(R.drawable.approved));
                profile_con.setVisibility(View.VISIBLE);
                phone_con.setVisibility(View.GONE);
                reservation_con.setVisibility(View.VISIBLE);
                pencil_con.setVisibility(View.VISIBLE);
            } else if ("2".equals(getStatus)) {
                status.setBackground(getResources().getDrawable(R.drawable.payment_complete));
                profile_con.setVisibility(View.VISIBLE);
                phone_con.setVisibility(View.GONE);
                reservation_con.setVisibility(View.VISIBLE);
                pencil_con.setVisibility(View.VISIBLE);
            } else if ("3".equals(getStatus)) {
                status.setBackground(getResources().getDrawable(R.drawable.visiting));
                profile_con.setVisibility(View.VISIBLE);
                phone_con.setVisibility(View.VISIBLE);
                reservation_con.setVisibility(View.VISIBLE);
                pencil_con.setVisibility(View.VISIBLE);
            } else if ("4".equals(getStatus)) {
                status.setBackground(getResources().getDrawable(R.drawable.request_refund));
                profile_con.setVisibility(View.VISIBLE);
                phone_con.setVisibility(View.GONE);
                reservation_con.setVisibility(View.VISIBLE);
                pencil_con.setVisibility(View.GONE);
            } else if ("5".equals(getStatus)) {
                status.setBackground(getResources().getDrawable(R.drawable.refunded));
                profile_con.setVisibility(View.VISIBLE);
                phone_con.setVisibility(View.GONE);
                reservation_con.setVisibility(View.VISIBLE);
                pencil_con.setVisibility(View.GONE);
            } else if ("8".equals(getStatus)) {
                status.setBackground(getResources().getDrawable(R.drawable.visited));
                profile_con.setVisibility(View.VISIBLE);
                phone_con.setVisibility(View.GONE);
                reservation_con.setVisibility(View.VISIBLE);
                pencil_con.setVisibility(View.VISIBLE);
            } else if ("9".equals(getStatus)) {
                status.setBackground(getResources().getDrawable(R.drawable.cancel_reservation));
                profile_con.setVisibility(View.VISIBLE);
                phone_con.setVisibility(View.GONE);
                reservation_con.setVisibility(View.VISIBLE);
                pencil_con.setVisibility(View.GONE);
            } else {
                status.setBackground(null);
                fold_con.setClickable(false);
                fold_icon.setClickable(false);
//                sliding_layer.setVisibility(View.GONE);
            }
        } else {
//            down.setVisibility(View.GONE);
//            name.setClickable(false);
//            title_con.setClickable(false);
//            down.setClickable(false);
//            status.setBackground(null);
//            fold_con.setClickable(false);
//            fold_icon.setClickable(false);
            sliding_layer.setVisibility(View.GONE);
        }


        socket.on(Socket.EVENT_CONNECT, onConnect);
        socket.on(Socket.EVENT_DISCONNECT, onDisconnect);
        socket.on("reconnect", reconnect); // 재접속
//        socket.on("view", view); // 상대방이 글을 봤을때
        socket.on("regist", regist);
        socket.on("chat", chat); // 상대방이 글을 봤을때
        //on 전화받기, emit 전화 걸기

        socket.connect();

        btn_camera_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_camera.callOnClick();
            }
        });
        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraDialog = new CameraDialog(ChatDetailsActivity.this);
                cameraDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                cameraDialog.show();
            }
        });

        btn_send_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_send.callOnClick();
            }
        });
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ipmm.hideSoftInputFromWindow(chat_edt.getWindowToken(), 0);
                if ("".equals(chat_edt.getText().toString().trim())) {
                    return;
                } else {
                    getType = "1";
                }

                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("type", getType);
                    if ("1".equals(getType)) {
                        jsonObject.put("content", chat_edt.getText().toString().trim());
                    } else {

                    }
                    socket.emit("chat", jsonObject);
                    chat_edt.setText("");

                } catch (JSONException e) {

                }

            }
        });
    }

    Emitter.Listener regist = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            try {
                JSONObject getData = (JSONObject) args[0];
                getId = getData.getString("id");
                Log.i(TAG, " regist " + getData.getString("id"));
            } catch (JSONException e) {

            }

            SharedPreferences get_token = context.getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
            final String getToken = get_token.getString("Token", "");
            final String url = ServerUrl.getBaseUrl() + "/chat/regist";
            Map<String, Object> params = new HashMap<String, Object>();
            if ("sitter".equals(mode)) {
                params.put("mom", getMember);
            } else {
                params.put("friend", getMember);
            }
            params.put("id", getId);
            Log.i(TAG, " params " + params);
            aQuery.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
                    Log.i(TAG, " jsonObject " + jsonObject);
                    try {
                        if (!jsonObject.getBoolean("return")) {
                            return;
                        } else {
                            getPhone = jsonObject.getString("phone");
                            getReserve = jsonObject.getString("reserve");
                            Log.i(TAG, "phone " + getPhone);
                            Log.i(TAG, "reserve " + getReserve);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    socket.emit("list");
                }
            }.header("EPOCH-AGENT", "" + getToken).header("User-Agent", "android"));
        }
    };
    Emitter.Listener chat = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!reload) {
                        reload = true;
                        return;
                    }

                    JSONArray getData = (JSONArray) args[0];
                    try {

                        if (getData.length() == 0) {
                            ping_pong_list.setVisibility(View.GONE);
                            Toast.makeText(ChatDetailsActivity.this, "대화 내역이 없습니다.", Toast.LENGTH_SHORT).show();
                        } else {
                            ping_pong_list.setVisibility(View.VISIBLE);
                            for (int i = 0; i < getData.length(); i++) {
                                JSONObject chatData = getData.getJSONObject(i);
                                Log.i(TAG, " target " + chatData.getString("target"));
                                Log.i(TAG, " type " + chatData.getString("type"));
                                Log.i(TAG, " content " + chatData.getString("content"));
                                Log.i(TAG, " date " + chatData.getString("date"));
                                data.add(new ChatMessage(chatData.getString("target"), chatData.getString("type"), chatData.getString("content"), chatData.getString("date")));
                            }
                            ping_pong_list.setAdapter(chatMessageAdapter);
                            ping_pong_list.setDivider(null);
                            chatMessageAdapter.notifyDataSetChanged();

                            socket.emit("view");
                        }
                    } catch (JSONException e) {
                    }
                }
            });

        }
    };
    Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.i(TAG, " onConnect");
        }
    };
    Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            reload = false;
            Log.i(TAG, " onDisconnect");
        }
    };
    Emitter.Listener reconnect = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            Log.i(TAG, " reconnect");

        }
    };
//    Emitter.Listener re = new Emitter.Listener() {
//        @Override
//        public void call(final Object... args) {
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    socket.emit("view", getToken, getRoom);
//                    JSONObject data = (JSONObject) args[0];
//                    try {
//                        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.KOREA);
//                        Date date = dateFormat.parse(data.getString("time"));
//                        Calendar nowCalendar = Calendar.getInstance();
//                        nowCalendar.setTime(date);
//                        if (!lastDate.equals(nowCalendar.get(Calendar.YEAR) + "년 " + (nowCalendar.get(Calendar.MONTH) + 1) + "월 " + nowCalendar.get(Calendar.DATE) + "일")) {
//                            chatArrayAdapter.add(new ChatMessage(false, nowCalendar.get(Calendar.YEAR) + "년 " + (nowCalendar.get(Calendar.MONTH) + 1) + "월 " + nowCalendar.get(Calendar.DATE) + "일", "", "", "date", "", "", "", ""));
//                            database.execSQL("INSERT INTO CHAT (MIDX, LEFT, CONTENT, TYPE, READ, DATE, MODE, NAME, IMAGE) VALUES('" + getIdx + "','" + "false" + "','" + nowCalendar.get(Calendar.YEAR) + "년 " + (nowCalendar.get(Calendar.MONTH) + 1) + "월 " + nowCalendar.get(Calendar.DATE) + "일" + "','date','0','0','0','0','0')");
//                            lastDate = nowCalendar.get(Calendar.YEAR) + "년 " + (nowCalendar.get(Calendar.MONTH) + 1) + "월 " + nowCalendar.get(Calendar.DATE) + "일";
//                        }
//                        if ("null".equals(data.getString("nick"))) {
//                            chatArrayAdapter.add(new ChatMessage(false, data.getString("data"), new SimpleDateFormat("a h:mm").format(date), "", data.getString("type"), getMode, getName, getImage1, "0"));
//                            if ("1".equals(getMode)) {
//                                database.execSQL("INSERT INTO CHAT (MIDX, LEFT, CONTENT, TYPE, READ, DATE, MODE, NAME, IMAGE) VALUES('" + getIdx + "','" + "false" + "','" + data.getString("data").replaceAll("'", "＇") + "','" + data.getString("type") + "','0','" + new SimpleDateFormat("a h:mm").format(date) + "','" + getMode + "','" + name + "','" + getImage1 + "')");
//                            } else if ("2".equals(getMode)) {
//                                database.execSQL("INSERT INTO CHAT (MIDX, LEFT, CONTENT, TYPE, READ, DATE, MODE, NAME, IMAGE) VALUES('" + getIdx + "','" + "false" + "','" + data.getString("data").replaceAll("'", "＇") + "','" + data.getString("type") + "','0','" + new SimpleDateFormat("a h:mm").format(date) + "','" + getMode + "','" + name + "','" + "0" + "')");
//                            }
//                        } else {
//                            chatArrayAdapter.add(new ChatMessage(true, data.getString("data"), new SimpleDateFormat("a hh:mm").format(date), data.getString("nick"), data.getString("type"), getMode, getName, getImage1, "0"));
//                            if ("1".equals(getMode)) {
//                                database.execSQL("INSERT INTO CHAT (MIDX, LEFT, CONTENT, TYPE, READ, DATE, MODE, NAME, IMAGE) VALUES('" + getIdx + "','" + "true" + "','" + data.getString("data").replaceAll("'", "＇") + "','" + data.getString("type") + "','0','" + new SimpleDateFormat("a h:mm").format(date) + "','" + getMode + "','" + name + "','" + getImage1 + "')");
//                            } else if ("2".equals(getMode)) {
//                                database.execSQL("INSERT INTO CHAT (MIDX, LEFT, CONTENT, TYPE, READ, DATE, MODE, NAME, IMAGE) VALUES('" + getIdx + "','" + "true" + "','" + data.getString("data").replaceAll("'", "＇") + "','" + data.getString("type") + "','0','" + new SimpleDateFormat("a h:mm").format(date) + "','" + getMode + "','" + name + "','" + "0" + "')");
//                            }
//                        }
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
//        }
//    };
//    Emitter.Listener view = new Emitter.Listener() {
//        @Override
//        public void call(final Object... args) {
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        JSONObject getData = (JSONObject) args[0];
//                        getId = getData.getString("id");
//                        Log.i(TAG, " regist " + getData.getString("id"));
//                    } catch (JSONException e) {
//
//                    }
//
//                    SharedPreferences get_token = context.getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
//                    final String getToken = get_token.getString("Token", "");
//                    final String url = ServerUrl.getBaseUrl() + "/chat/regist";
//                    Map<String, Object> params = new HashMap<String, Object>();
//                    if ("sitter".equals(mode)) {
//                        params.put("mom", getMember);
//                    } else {
//                        params.put("friend", getMember);
//                    }
//                    params.put("id", getId);
//                    Log.i(TAG, " params " + params);
//                    aQuery.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
//                        @Override
//                        public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
//                            Log.i(TAG, " jsonObject " + jsonObject);
//                            try {
//                                if (!jsonObject.getBoolean("return")) {
//                                    return;
//                                } else {
//                                    getPhone = jsonObject.getString("phone");
//                                    Log.i(TAG, "phone " + getPhone);
//                                }
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                            socket.emit("list");
//                        }
//                    }.header("EPOCH-AGENT", "" + getToken).header("User-Agent", "android"));
//                }
//            });
//        }
//    };
//    Emitter.Listener init = new Emitter.Listener() {
//        @Override
//        public void call(final Object... args) {
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    socket.emit("view", getToken, getRoom);
//                    JSONArray getData = (JSONArray) args[0];
//                    for (int i = 0; i < getData.length(); i++) {
//                        try {
//                            JSONObject data = new JSONObject(getData.get(i).toString());
//                            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.KOREA);
//                            Date date = dateFormat.parse(data.getString("time"));
//                            Calendar nowCalendar = Calendar.getInstance();
//                            nowCalendar.setTime(date);
//                            if (!lastDate.equals(nowCalendar.get(Calendar.YEAR) + "년 " + (nowCalendar.get(Calendar.MONTH) + 1) + "월 " + nowCalendar.get(Calendar.DATE) + "일")) {
//                                chatArrayAdapter.add(new ChatMessage(false, nowCalendar.get(Calendar.YEAR) + "년 " + (nowCalendar.get(Calendar.MONTH) + 1) + "월 " + nowCalendar.get(Calendar.DATE) + "일", "", "", "date", "", "", "", ""));
//                                database.execSQL("INSERT INTO CHAT (MIDX, LEFT, CONTENT, TYPE, READ, DATE, MODE, NAME, IMAGE) VALUES('" + getIdx + "','" + "false" + "','" + nowCalendar.get(Calendar.YEAR) + "년 " + (nowCalendar.get(Calendar.MONTH) + 1) + "월 " + nowCalendar.get(Calendar.DATE) + "일" + "','date','0','0','0','0','0')");
//                                lastDate = nowCalendar.get(Calendar.YEAR) + "년 " + (nowCalendar.get(Calendar.MONTH) + 1) + "월 " + nowCalendar.get(Calendar.DATE) + "일";
//                            }
//                            if ("null".equals(data.getString("nick"))) {
//                                chatArrayAdapter.add(new ChatMessage(false, data.getString("data"), new SimpleDateFormat("a h:mm").format(date), "", data.getString("type"), getMode, getName, getImage1, "1"));
//                                if ("1".equals(getMode)) {
//                                    database.execSQL("INSERT INTO CHAT (MIDX, LEFT, CONTENT, TYPE, READ, DATE, MODE, NAME, IMAGE) VALUES('" + getIdx + "','" + "false" + "','" + data.getString("data").replaceAll("'", "＇") + "','" + data.getString("type") + "','1','" + new SimpleDateFormat("a h:mm").format(date) + "','" + getMode + "','" + getName + "','" + getImage1 + "')");
//                                } else if ("2".equals(getMode)) {
//                                    database.execSQL("INSERT INTO CHAT (MIDX, LEFT, CONTENT, TYPE, READ, DATE, MODE, NAME, IMAGE) VALUES('" + getIdx + "','" + "false" + "','" + data.getString("data").replaceAll("'", "＇") + "','" + data.getString("type") + "','1','" + new SimpleDateFormat("a h:mm").format(date) + "','" + getMode + "','" + getName + "','" + "0" + "')");
//                                }
//                            } else {
//                                chatArrayAdapter.add(new ChatMessage(true, data.getString("data"), new SimpleDateFormat("a hh:mm").format(date), data.getString("nick"), data.getString("type"), getMode, getName, getImage1, "1"));
//                                if ("1".equals(getMode)) {
//                                    database.execSQL("INSERT INTO CHAT (MIDX, LEFT, CONTENT, TYPE, READ, DATE, MODE, NAME, IMAGE) VALUES('" + getIdx + "','" + "true" + "','" + data.getString("data").replaceAll("'", "＇") + "','" + data.getString("type") + "','1','" + new SimpleDateFormat("a h:mm").format(date) + "','" + getMode + "','" + getName + "','" + getImage1 + "')");
//                                } else if ("2".equals(getMode)) {
//                                    database.execSQL("INSERT INTO CHAT (MIDX, LEFT, CONTENT, TYPE, READ, DATE, MODE, NAME, IMAGE) VALUES('" + getIdx + "','" + "true" + "','" + data.getString("data").replaceAll("'", "＇") + "','" + data.getString("type") + "','1','" + new SimpleDateFormat("a h:mm").format(date) + "','" + getMode + "','" + getName + "','" + "0" + "')");
//                                }
//                            }
//                        } catch (ParseException e) {
//                            e.printStackTrace();
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            });
//        }
//    };
//
//    Emitter.Listener data = new Emitter.Listener() {
//        @Override
//        public void call(final Object... args) {
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//
//                }
//            });
//        }
//    };
//    Emitter.Listener exit = new Emitter.Listener() {
//        @Override
//        public void call(final Object... args) {
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    Toast.makeText(ChatDetailsActivity.this, "방이 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
//                    finish();
//                }
//            });
//        }
//    };


    @Override
    public void finish() {
        super.finish();
        ipmm.hideSoftInputFromWindow(chat_edt.getWindowToken(), 0);
        editor.clear();
        editor.commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 700:
                if (resultCode == RESULT_CANCELED) {
                    getType = "1";
                    return;
                } else {
                    getType = "2";
                    try {
                        if (Build.VERSION.SDK_INT <= 21) {
                            Uri imgUri = data.getData();
                            imagePath = getImageRealPathFromURI(ChatDetailsActivity.this.getContentResolver(), imgUri);
                        }
                        ExifInterface exif = new ExifInterface(imagePath);
                        int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                        int exifDegree = exifOrientationToDegrees(exifOrientation);
                        originalBitmap = rotate(BitmapFactory.decodeFile(imagePath), exifDegree);
                        resizeBitmap = rotate(resizeBitmap(imagePath, 1080, 1920), exifDegree);

                        String url = ServerUrl.getBaseUrl() + "/upload/images";
                        Map<String, Object> params = new HashMap<String, Object>();
                        Uri fileUri = getImageUri(ChatDetailsActivity.this, resizeBitmap);
                        String filePath = getImageRealPathFromURI(ChatDetailsActivity.this.getContentResolver(), fileUri);
                        final File makeFile = new File(filePath);
                        params.put("image", makeFile);
                        aQuery.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
                            @Override
                            public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
                                Log.i(TAG, " jsonObject " + jsonObject);
                                try {
                                    JSONArray jsonArray = new JSONArray(jsonObject.getString("files"));
                                    Log.i(TAG, " files " + jsonArray.get(0));
                                    getFileName = jsonArray.get(0).toString();

                                    JSONObject fileObject = new JSONObject();
                                    fileObject.put("type", getType);
                                    fileObject.put("content", getFileName);
                                    socket.emit("chat", fileObject);
                                    socket.off("list");
                                    socket.emit("list");


//                                    JSONArray jsonArray = new JSONArray(jsonObject.getString("url"));
//                                    contentResolver.delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, MediaStore.Images.ImageColumns.DATA + "=?", new String[]{
//                                            makeFile.toString()
//                                    });
//                                    socket.emit("call", getToken, getRoom, jsonObject.getString("path_mobile") + jsonArray.get(0).toString(), "image");
//                                    chat_edt.setText("");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }.header("EPOCH-AGENT", "" + getToken).header("User-Agent", "android"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case 800:
                if (resultCode == RESULT_CANCELED) {
                    getType = "1";
                    return;
                } else {
                    getType = "2";
                    try {
                        Uri imgUri = data.getData();
                        imagePath = getImageRealPathFromURI(ChatDetailsActivity.this.getContentResolver(), imgUri);
                        imagePath = getImageRealPathFromURI(ChatDetailsActivity.this.getContentResolver(), imgUri);
                        ExifInterface exif = new ExifInterface(imagePath);
                        int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                        int exifDegree = exifOrientationToDegrees(exifOrientation);
                        originalBitmap = rotate(BitmapFactory.decodeFile(imagePath), exifDegree);
                        resizeBitmap = rotate(resizeBitmap(imagePath, 1080, 1920), exifDegree);

                        String url = ServerUrl.getBaseUrl() + "/upload/images";
                        Map<String, Object> params = new HashMap<String, Object>();
                        Uri fileUri = getImageUri(ChatDetailsActivity.this, resizeBitmap);
                        String filePath = getImageRealPathFromURI(ChatDetailsActivity.this.getContentResolver(), fileUri);
                        final File makeFile = new File(filePath);
                        params.put("image", makeFile);
                        aQuery.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
                            @Override
                            public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
                                Log.i(TAG, " jsonObject " + jsonObject);
                                try {
                                    JSONArray jsonArray = new JSONArray(jsonObject.getString("files"));
                                    Log.i(TAG, " files " + jsonArray.get(0));
                                    getFileName = jsonArray.get(0).toString();

                                    JSONObject fileObject = new JSONObject();
                                    fileObject.put("type", getType);
                                    fileObject.put("content", getFileName);
                                    socket.emit("chat", fileObject);
                                    socket.off("list");
                                    socket.emit("list");

//                                    JSONArray jsonArray = new JSONArray(jsonObject.getString("url"));
//                                    contentResolver.delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, MediaStore.Images.ImageColumns.DATA + "=?", new String[]{
//                                            makeFile.toString()
//                                    });
//                                    socket.emit("call", getToken, getRoom, jsonObject.getString("path_mobile") + jsonArray.get(0).toString(), "image");
//                                    chat_edt.setText("");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }.header("EPOCH-AGENT", "" + getToken).header("User-Agent", "android"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case RESULT_CANCELED:
                break;
            case 999:
                setResult(999);
                finish();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: " + getIntent().getStringExtra("member"));
        editor.putString("index", getIntent().getStringExtra("member"));
        editor.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        socket.disconnect();
        socket.off(Socket.EVENT_CONNECT, onConnect);
        socket.off(Socket.EVENT_DISCONNECT, onDisconnect);
        socket.off("reconnect", reconnect);
//        socket.off("return", re);
//        socket.off("view", view);
//        socket.off("init", init);
//        socket.off("data", data);
//        socket.off("exit", exit);
    }

    Uri getFileUri() {
        File dir = new File(getFilesDir(), "Picture");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(dir, System.currentTimeMillis() + ".png");
        imagePath = file.getAbsolutePath();
        return FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".fileprovider", file);
    }

    String getImageRealPathFromURI(ContentResolver contentResolver, Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = contentResolver.query(contentUri, proj, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            int path = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String tmp = cursor.getString(path);
            cursor.close();
            return tmp;
        }
    }

    int exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    Bitmap rotate(Bitmap bitmap, int degrees) {
        if (degrees != 0 && bitmap != null) {
            Matrix matrix = new Matrix();
            matrix.setRotate(degrees, (float) bitmap.getWidth() / 2, (float) bitmap.getHeight() / 2);
            try {
                Bitmap converted = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                if (bitmap != converted) {
                    bitmap.recycle();
                    bitmap = converted;
                }
            } catch (OutOfMemoryError ex) {

            }
        }
        return bitmap;
    }

    Bitmap resizeBitmap(String file, int width, int height) {
        BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
        bmpFactoryOptions.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);

        int heightRatio = (int) Math.ceil(bmpFactoryOptions.outHeight / (float) height);
        int widthRatio = (int) Math.ceil(bmpFactoryOptions.outWidth / (float) width);

        if (heightRatio > 1 || widthRatio > 1) {
            if (heightRatio > widthRatio) {
                bmpFactoryOptions.inSampleSize = heightRatio;
            } else {
                bmpFactoryOptions.inSampleSize = widthRatio;
            }
        }

        bmpFactoryOptions.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);
        return bitmap;
    }

    Uri getImageUri(Context context, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    int pxToDp(int px) {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int dp = Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return dp;
    }

    int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();

        final Rect rect = new Rect(0, 0, dpToPx(pxToDp(bitmap.getWidth())), dpToPx(pxToDp(bitmap.getHeight())));
        final RectF rectF = new RectF(rect);
        final float roundPx = 10;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    public class OneBtnDialog extends Dialog {
        OneBtnDialog oneBtnDialog = this;
        Context context;

        public OneBtnDialog(final Context context, final String text, final String btnText) {
            super(context);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialog_one_btn);
            getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            this.context = context;
            TextView title1 = (TextView) findViewById(R.id.title1);
            TextView title2 = (TextView) findViewById(R.id.title2);
            TextView btn1 = (TextView) findViewById(R.id.btn1);
            title2.setVisibility(View.GONE);
            title1.setText(text);
            btn1.setText(btnText);
            btn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    oneBtnDialog.dismiss();
                }
            });
        }
    }

    public class AboutChatDialog extends Dialog {
        AboutChatDialog aboutChatDialog = this;
        Context context;

        public AboutChatDialog(final Context context, String who) {
            super(context);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialog_hidden_chat);
            getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            this.context = context;
            LinearLayout report_con = findViewById(R.id.report_con);
            final TextView report = findViewById(R.id.report);
            LinearLayout show_n_hide_con = findViewById(R.id.show_n_hide_con);
            final TextView show_n_hide = findViewById(R.id.show_n_hide);
            TextView cancel = findViewById(R.id.cancel);

            report_con.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    report.callOnClick();
                }
            });
            report.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ChatDetailsActivity.this, ReportActivity.class);
                    intent.putExtra("target", getMember);
                    startActivity(intent);
                    aboutChatDialog.dismiss();
                    finish();
                }
            });

            show_n_hide_con.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    show_n_hide.callOnClick();
                }
            });
            show_n_hide.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = ServerUrl.getBaseUrl() + "/chat/hidden";
                    Map<String, Object> params = new HashMap<String, Object>();
                    if ("sitter".equals(mode)) {
                        params.put("mom", getMember);
                    } else {
                        params.put("friend", getMember);
                    }
                    Log.i(TAG, " params " + params);
                    aQuery.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
                        @Override
                        public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
                            Log.i(TAG, " jsonObject " + jsonObject);
                            try {

                                if (jsonObject.getBoolean("return")) {

                                    Log.i(TAG, " hidden " + hidden);

                                    Toast.makeText(ChatDetailsActivity.this, "해당 회원을 이 리스트에서 이동합니다.", Toast.LENGTH_SHORT).show();
                                    aboutChatDialog.dismiss();
                                } else {


                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }.header("EPOCH-AGENT", "" + getToken).header("User-Agent", "android"));

                }
            });

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    aboutChatDialog.dismiss();
                }
            });
        }
    }

    public class CameraDialog extends Dialog {
        CameraDialog cameraDialog = this;
        Context context;

        public CameraDialog(final Context context) {
            super(context);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.camera_dialog);
            getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            getWindow().setGravity(Gravity.BOTTOM | Gravity.LEFT);
            this.context = context;
            FrameLayout btn1_con = (FrameLayout) findViewById(R.id.btn1_con);
            FrameLayout btn2_con = (FrameLayout) findViewById(R.id.btn2_con);
            final TextView btn1 = (TextView) findViewById(R.id.btn1);
            final TextView btn2 = (TextView) findViewById(R.id.btn2);
            btn1_con.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btn1.callOnClick();
                }
            });
            btn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cameraDialog.dismiss();
                    if (Build.VERSION.SDK_INT > 21) {
                        Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, getFileUri());
                        startActivityForResult(intent, CAMERA);
                    } else {
                        Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, CAMERA);
                    }
                }
            });
            btn2_con.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btn2.callOnClick();
                }
            });
            btn2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cameraDialog.dismiss();
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                    intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, GALLERY);
                }
            });
        }
    }
}
