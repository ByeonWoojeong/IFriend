package com.woojeong.global.ifriend;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateLongClickListener;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;
import com.prolificinteractive.materialcalendarview.OnRangeSelectedListener;
import com.woojeong.global.ifriend.CalendarView.RangeDayDecorator;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import static com.woojeong.global.ifriend.GlobalApplication.setDarkMode;

public class ChoiceDateActivity extends AppCompatActivity implements OnDateSelectedListener, OnMonthChangedListener, OnDateLongClickListener, OnRangeSelectedListener {
    private static String TAG = "ChoiceDateActivity";
    Context context;
    InputMethodManager ipmm;
    AQuery aQuery = null;
    OneBtnDialog oneBtnDialog;

    MaterialCalendarView materialCalendarView;
    SpinnerReselect spinner_start, spinner_finish;
    TextView ok, start_txt, finish_txt;
    ImageView back, reset, start_down, finish_down;
    FrameLayout back_con, start_con, finish_con, ok_con;
    TimePickerDialog timePickerDialog;
    LinearLayout calendar_con;
    private RangeDayDecorator decorator;

    String[] time = new String[]{"시간선택", "00:00", "01:00", "02:00", "03:00", "04:00", "05:00", "06:00", "07:00", "08:00", "09:00", "10:00", "11:00", "12:00",
            "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00"};
    String[] timeValue = new String[]{"", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23"};
    int getTotalTime = 0;

    Handler handler;
    SharedPreferences filteringData;
    SharedPreferences.Editor filterEditor;
    ArrayList<String> date;
    ArrayList<String> getDate;
    JSONArray dateArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_date);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#ffffff"));
            window.setBackgroundDrawable(null);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                setDarkMode(ChoiceDateActivity.this, true);
            }
        }

        filteringData = getSharedPreferences("filtering", Activity.MODE_PRIVATE);
        filterEditor = filteringData.edit();
        date = new ArrayList<String>();
        dateArray = new JSONArray();
        getDate = new ArrayList<String>();

        context = this;
        aQuery = new AQuery(this);
        ipmm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
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
        ok_con = findViewById(R.id.ok_con);
        ok = findViewById(R.id.ok);
        ok_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ok.callOnClick();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!"시간선택".equals(start_txt.getText().toString()) || !"시간선택".equals(finish_txt.getText().toString())) {
                    if (getTotalTime < 2) {
                        oneBtnDialog = new OneBtnDialog(ChoiceDateActivity.this, "시작, 종료 시간을 확인해주세요 !\n(기본 2시간)", "확인");
                        oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        oneBtnDialog.setCancelable(false);
                        oneBtnDialog.show();
                        return;
                    }
                }

                dateArray = new JSONArray();
                if (date.size() > 0) {
                    for (int i = 0; i < date.size(); i++) {
                        dateArray.put(date.get(i));
                    }
                } else if (getDate.size() > 0) {
                    for (int i = 0; i < getDate.size(); i++) {
                        dateArray.put(getDate.get(i));
                    }
                }
                filterEditor.putString("date", dateArray.toString());
                filterEditor.commit();
                finish();

            }
        });

        calendar_con = findViewById(R.id.calendar_con);
        decorator = new RangeDayDecorator(this);
        materialCalendarView = findViewById(R.id.calendar_view);
        materialCalendarView.setOnRangeSelectedListener(this);
        materialCalendarView.addDecorator(decorator);
        materialCalendarView.state().edit().setMinimumDate(CalendarDay.today()).commit();
//        materialCalendarView.setDateSelected(CalendarDay.from(2019, 05, 28), true);

        reset = findViewById(R.id.reset);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterEditor.remove("date");
                filterEditor.remove("time[0]");
                filterEditor.remove("time[1]");
                filterEditor.commit();
                start_txt.setText("시간선택");
                finish_txt.setText("시간선택");
                date.clear();
                getDate.clear();
                materialCalendarView.clearSelection();
            }
        });


        spinner_start = findViewById(R.id.spinner_start);
        spinner_finish = findViewById(R.id.spinner_finish);
        start_txt = findViewById(R.id.start_txt);
        finish_txt = findViewById(R.id.finish_txt);
        start_down = findViewById(R.id.start_down);
        finish_down = findViewById(R.id.finish_down);
        start_con = findViewById(R.id.start_con);
        finish_con = findViewById(R.id.finish_con);


        ArrayAdapter<String> timeAdapter = new ArrayAdapter<String>(context, R.layout.time_item, time);
        timeAdapter.setDropDownViewResource(R.layout.time_item);
        spinner_start.setAdapter(timeAdapter);
        spinner_start.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                start_txt.setText(spinner_start.getSelectedItem().toString());
                getTotalTime = spinner_finish.getSelectedItemPosition() - spinner_start.getSelectedItemPosition();
                if (!"시간선택".equals(start_txt.getText().toString())) {
                    filterEditor.putString("time[0]", timeValue[spinner_start.getSelectedItemPosition()]);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner_finish.setAdapter(timeAdapter);
        spinner_finish.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                finish_txt.setText(spinner_finish.getSelectedItem().toString());
                getTotalTime = spinner_finish.getSelectedItemPosition() - spinner_start.getSelectedItemPosition();
                if (!"시간선택".equals(finish_txt.getText().toString())) {
                    filterEditor.putString("time[1]", timeValue[spinner_finish.getSelectedItemPosition()]);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                viewSettings();
            }
        }, 430);

    }

    @Override
    public void onDateLongClick(@NonNull MaterialCalendarView materialCalendarView, @NonNull CalendarDay calendarDay) {

    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView materialCalendarView, @NonNull CalendarDay calendarDay, boolean b) {
    }

    @Override
    public void onMonthChanged(MaterialCalendarView materialCalendarView, CalendarDay calendarDay) {

    }

    @Override
    public void onRangeSelected(@NonNull MaterialCalendarView materialCalendarView, @NonNull List<CalendarDay> list) {
        Log.i(TAG, " list " + list);
        date.clear();
        for (int i = 0; i < list.size(); i++) {
            //list.get(i).toString().substring(12, 21)
            String addDate = list.get(i).toString().substring(12).replace("}", "");

            Log.i(TAG, " addDate " + addDate);

            date.add(addDate);
        }
        if (list.size() > 0) {
            decorator.addFirstAndLast(list.get(0), list.get(list.size() - 1));
            materialCalendarView.invalidateDecorators();
        }

    }

    void viewSettings() {
        Log.i(TAG, " viewSettings ");

        String dateStr = filteringData.getString("date", "");
        if (filteringData.getString("date", "") != null) {

            try {
//                dateArray = new JSONArray(dateStr);
//                getDate.clear();
//                for (int i = 0; i < dateArray.length(); i++) {
//                    String date = dateArray.optString(i);
//                    getDate.add(date);
//                    String[] dateSplit = getDate.get(i).split("-");
//                    Log.i(TAG, " dateSplit ------------ " + dateSplit[0] + " : " + dateSplit[1] + " : " + dateSplit[2]);
//
//                    CalendarDay calendarDay = CalendarDay.from(Integer.parseInt(dateSplit[0]), Integer.parseInt(dateSplit[1]), Integer.parseInt(dateSplit[2]));
//                    materialCalendarView.setDateSelected(calendarDay, true);
//                }
                dateArray = new JSONArray(dateStr);
                getDate.clear();
                for (int i = 0; i < dateArray.length(); i++) {
                    String date = dateArray.optString(i);
                    getDate.add(date);
                    String[] dateSplit = getDate.get(i).split("-");
                    Log.i(TAG, " dateSplit ------------ " + dateSplit[0] + " : " + dateSplit[1] + " : " + dateSplit[2]);

                    CalendarDay calendarDay = CalendarDay.from(Integer.parseInt(dateSplit[0]), Integer.parseInt(dateSplit[1]), Integer.parseInt(dateSplit[2]));
                    materialCalendarView.setDateSelected(calendarDay, true);
                }
            } catch (JSONException e) {
                Log.i(TAG, " Exception " + e);
            }
        }

        Log.i(TAG, " time[0] " + filteringData.getString("time[0]", ""));
        Log.i(TAG, " time[1] " + filteringData.getString("time[1]", ""));

        String time1 = filteringData.getString("time[0]", "");
        String time2 = filteringData.getString("time[1]", "");

        if (!"".equals(filteringData.getString("time[0]", ""))) {
            start_txt.setText(time1 + ":00");
        }
        if (!"".equals(filteringData.getString("time[1]", ""))) {
            finish_txt.setText(time2 + ":00");
        }
    }

    public class TimePickerDialog extends Dialog {
        TimePickerDialog timePickerDialog = this;
        Context context;

        public TimePickerDialog(final Context context, final String what) {
            super(context);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.custom_time_picker_dialog);
            getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            this.context = context;
            Calendar calendar = Calendar.getInstance();
            final TimePicker timePicker = findViewById(R.id.time_picker);
            TextView btn1 = (TextView) findViewById(R.id.btn1);
            TextView btn2 = (TextView) findViewById(R.id.btn2);
            btn1.setText("취소");
            btn2.setText("선택");
            btn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    timePickerDialog.dismiss();
                }
            });
            btn2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    timePickerDialog.dismiss();
                    int hour, minute;
                    if ("start".equals(what)) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            hour = timePicker.getHour();
                            minute = timePicker.getMinute();
                        } else {
                            hour = timePicker.getCurrentHour();
                            minute = timePicker.getCurrentMinute();
                        }
                        String AM_PM;
                        if (hour < 12) {
                            AM_PM = "AM";
                        } else {
                            AM_PM = "PM";
                            hour = hour - 12;
                            if (hour == 0) hour = 12;
                        }
//                        start_txt.setText(hour + " : " + minute + " " + AM_PM);
                        start_txt.setText(hour + " : 00 " + AM_PM);

                    } else if ("finish".equals(what)) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            hour = timePicker.getHour();
                            minute = timePicker.getMinute();
                        } else {
                            hour = timePicker.getCurrentHour();
                            minute = timePicker.getCurrentMinute();
                        }
                        String AM_PM;
                        if (hour < 12) {
                            AM_PM = "AM";
                        } else {
                            AM_PM = "PM";
                            hour = hour - 12;
                            if (hour == 0) hour = 12;
                        }
                        finish_txt.setText(hour + " : 00 " + AM_PM);
//                        finish_txt.setText(hour + " : " + minute + " " + AM_PM);
                    }
                }
            });
        }
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
}
