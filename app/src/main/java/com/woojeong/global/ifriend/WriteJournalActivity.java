package com.woojeong.global.ifriend;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;
import static com.woojeong.global.ifriend.GlobalApplication.setDarkMode;

public class WriteJournalActivity extends AppCompatActivity {
    private static String TAG = "WriteJournalActivity";
    Context context;
    InputMethodManager ipmm;
    AQuery aQuery = null;
    OneBtnDialog oneBtnDialog;
    FinishDialog finishDialog;
    TimePickerDialog timePickerDialog;
    TwoBtnDialog twoBtnDialog;

    FrameLayout back_con, ok_con, time_con, imageview_con0, imageview_con1;
    LinearLayout image_con;
    ImageView back, spinner_down, choice_img, imageView0, closeImageView0, imageView1, closeImageView1;
    ScrollView scrollView;
    TextView ok, time_txt;
    EditText content;

    int CAMERA = 700, GALLERY = 800, totalImageCount = 0, addImageCount = 0, getImageCount = 0;
    FrameLayout imageview_con[] = new FrameLayout[2];
    ImageView imageView[] = new ImageView[2];
    ImageView closeImageView[] = new ImageView[2];
    ArrayList<Bitmap> originalBitmap = new ArrayList<Bitmap>();
    ArrayList<Bitmap> resizeBitmap = new ArrayList<Bitmap>();
    ArrayList<String> imagePath = new ArrayList<String>();
    ArrayList<File> file = new ArrayList<File>();
    ArrayList<String> getImageList = new ArrayList<String>();

    Intent getIntent;
    String reserveNumber;

    TimePicker timePicker;
    String getHour, getMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_journal);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#ffffff"));
            window.setBackgroundDrawable(null);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                setDarkMode(WriteJournalActivity.this, true);
            }
        }

        getIntent = getIntent();
        reserveNumber = getIntent.getStringExtra("reserve");

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
//        time_con = findViewById(R.id.time_con);
        imageview_con1 = findViewById(R.id.imageview_con1);
        imageview_con0 = findViewById(R.id.imageview_con0);

        image_con = findViewById(R.id.image_con);

//        spinner_down = findViewById(R.id.spinner_down);
        choice_img = findViewById(R.id.choice_img);
        imageView1 = findViewById(R.id.imageview1);
        closeImageView1 = findViewById(R.id.close_imageview1);
        imageView0 = findViewById(R.id.imageview0);
        closeImageView0 = findViewById(R.id.close_imageview0);

        scrollView = findViewById(R.id.scrollView);
        scrollView.postDelayed(new Runnable() {
            @Override
            public void run() {
                scrollView.scrollTo(0, 0);
            }
        }, 700);

        ok = findViewById(R.id.ok);

        time_txt = findViewById(R.id.time_txt);
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        String nowString = dateFormat.format(date);
        time_txt.setText(nowString);

        content = findViewById(R.id.content);

//        time_con.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ipmm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
//
//                timePickerDialog = new TimePickerDialog(WriteJournalActivity.this);
//                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                timePickerDialog.setCancelable(false);
//                timePickerDialog.show();
//            }
//        });

        choice_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ipmm.hideSoftInputFromWindow(choice_img.getWindowToken(), 0);
                if (totalImageCount >= 2) {
                    oneBtnDialog = new OneBtnDialog(WriteJournalActivity.this, "이미지는 최대 2장까지\n첨부 가능합니다 !", "확인");
                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    oneBtnDialog.setCancelable(false);
                    oneBtnDialog.show();
                    return;
                }
                twoBtnDialog = new TwoBtnDialog(WriteJournalActivity.this);
                twoBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                twoBtnDialog.show();
            }
        });

        for (int i = 0; i < imageView.length; i++) {
            int res1 = getResources().getIdentifier("imageview_con" + i, "id", getPackageName());
            int res2 = getResources().getIdentifier("imageview" + i, "id", getPackageName());
            int res3 = getResources().getIdentifier("close_imageview" + i, "id", getPackageName());
            imageview_con[i] = (FrameLayout) findViewById(res1);
            imageView[i] = (ImageView) findViewById(res2);
            closeImageView[i] = (ImageView) findViewById(res3);
            final int finalI = i;
            imageView[finalI].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(WriteJournalActivity.this, InputImagePathActivity.class);
                    intent.putExtra("path", imagePath.get(finalI));
                    startActivityForResult(intent, 900);
                }
            });
            closeImageView[finalI].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    totalImageCount--;
                    if (totalImageCount <= 0) {
                        image_con.setVisibility(View.VISIBLE);
                    }
                    imageview_con[totalImageCount].setVisibility(View.GONE);
                    imageView[totalImageCount].setImageBitmap(null);
                    imagePath.remove(finalI);
                    if (getImageList.size() > 0 && finalI < getImageList.size()) {
                        getImageCount--;
                        getImageList.remove(finalI);
                    }
                    if (addImageCount > 0) {
                        if (finalI > getImageList.size()) {
                            addImageCount--;
                            originalBitmap.remove(finalI - getImageCount);
                            resizeBitmap.remove(finalI - getImageCount);
                        }
                    }
                    for (int i = finalI; i < totalImageCount; i++) {
                        imageView[i].setImageBitmap(null);
                        Glide.with(WriteJournalActivity.this).load(imagePath.get(totalImageCount)).transition(withCrossFade()).into(imageView[i]);
                    }
                }
            });
        }

        ok_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ok.callOnClick();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ("".equals(time_txt.getText().toString())) {
                    oneBtnDialog = new OneBtnDialog(WriteJournalActivity.this, "시간을 선택해주세요 !", "확인");
                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    oneBtnDialog.setCancelable(false);
                    oneBtnDialog.show();
                    return;
                } else if ("".equals(content.getText().toString())) {
                    oneBtnDialog = new OneBtnDialog(WriteJournalActivity.this, "내용을 작성해주세요 !", "확인");
                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    oneBtnDialog.setCancelable(false);
                    oneBtnDialog.show();
                    return;
                }

                SharedPreferences get_token = getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
                final String getToken = get_token.getString("Token", "");
                final String url = ServerUrl.getBaseUrl() + "/diary/insert";
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("reserve", reserveNumber);
                file.clear();
                for (int i = 0; i < addImageCount; i++) {
                    Uri fileUri = getImageUri(WriteJournalActivity.this, resizeBitmap.get(i));
                    String filePath = getImageRealPathFromURI(WriteJournalActivity.this.getContentResolver(), fileUri);
                    File makeFile = new File(filePath);
                    file.add(makeFile);
                    params.put("images[" + i + "]", makeFile);
                }

                params.put("content", content.getText().toString());

//                if (timePicker.getHour() < 10) {
//                    getHour = "0" + timePicker.getHour();
//                } else {
//                    getHour = timePicker.getHour()+"";
//                }
//
//                if (timePicker.getMinute() < 10) {
//                    getMinute = "0" + timePicker.getMinute();
//                } else {
//                    getMinute = timePicker.getMinute()+"";
//                }

                params.put("time", time_txt.getText().toString());

                Log.i(TAG, " params " + params);
                aQuery.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
                    @Override
                    public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
                        Log.i(TAG, " jsonObject " + jsonObject);
                        try {
                            if (jsonObject.getBoolean("return")) {    //return이 true 면?
                                finishDialog = new FinishDialog(WriteJournalActivity.this, "일지 작성을\n완료 하였습니다 !", "확인");
                                finishDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                finishDialog.setCancelable(false);
                                finishDialog.show();
                            } else if (!jsonObject.getBoolean("return")) {
                                if("login".equals(jsonObject.getString("type"))){
                                    Toast.makeText(WriteJournalActivity.this, "로그인 해주세요 ! ", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(context, LoginActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }.header("EPOCH-AGENT", "" + getToken).header("User-Agent", "android"));
            }
        });
    }

    Uri getFileUri() {
        File dir = new File(getFilesDir(), "Picture");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(dir, System.currentTimeMillis() + ".png");
        imagePath.add(file.getAbsolutePath() + "");
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

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 1:
                break;
            case 700:
                if (resultCode == RESULT_CANCELED) {
                    return;
                } else {
                    try {
                        if (Build.VERSION.SDK_INT < 21) {
                            Uri imgUri = data.getData();
                            imagePath.add(getImageRealPathFromURI(WriteJournalActivity.this.getContentResolver(), imgUri));
                        }
                        ExifInterface exif = new ExifInterface(imagePath.get(totalImageCount));
                        int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                        int exifDegree = exifOrientationToDegrees(exifOrientation);
                        originalBitmap.add(rotate(BitmapFactory.decodeFile(imagePath.get(totalImageCount)), exifDegree));
                        resizeBitmap.add(rotate(resizeBitmap(imagePath.get(totalImageCount), 1080, 1920), exifDegree));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    image_con.setVisibility(View.VISIBLE);
                    scrollView.smoothScrollTo(0, 0);
                    if (totalImageCount < 2) {
                        imageview_con[totalImageCount].setVisibility(View.VISIBLE);

                    }
                    Glide.with(WriteJournalActivity.this).load(imagePath.get(totalImageCount)).transition(withCrossFade()).into(imageView[totalImageCount]);
                    totalImageCount++;
                    addImageCount++;
                }
                break;
            case 800:
                if (resultCode == RESULT_CANCELED) {
                    return;
                } else {
                    try {
                        Uri imgUri = data.getData();
                        imagePath.add(getImageRealPathFromURI(WriteJournalActivity.this.getContentResolver(), imgUri));
                        ExifInterface exif = new ExifInterface(imagePath.get(totalImageCount));
                        int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                        int exifDegree = exifOrientationToDegrees(exifOrientation);
                        originalBitmap.add(rotate(BitmapFactory.decodeFile(imagePath.get(totalImageCount)), exifDegree));
                        resizeBitmap.add(rotate(resizeBitmap(imagePath.get(totalImageCount), 1080, 1920), exifDegree));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    image_con.setVisibility(View.VISIBLE);
                    if (totalImageCount < 2) {
                        imageview_con[totalImageCount].setVisibility(View.VISIBLE);
                    }
                    Glide.with(WriteJournalActivity.this).load(imagePath.get(totalImageCount)).transition(withCrossFade()).into(imageView[totalImageCount]);
                    totalImageCount++;
                    addImageCount++;
                }
                break;
            case 900:
                break;
            case RESULT_CANCELED:
                break;
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

    public class FinishDialog extends Dialog {
        FinishDialog finishDialog = this;
        Context context;

        public FinishDialog(final Context context, final String text, final String btnText) {
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
                    finishDialog.dismiss();
                    finish();
                }
            });
        }
    }

    public class TimePickerDialog extends Dialog {
        TimePickerDialog timePickerDialog = this;
        Context context;

        public TimePickerDialog(final Context context) {
            super(context);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.custom_time_picker_dialog);
            getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            this.context = context;
            Calendar calendar = Calendar.getInstance();
            timePicker = findViewById(R.id.time_picker);
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
                    time_txt.setText(hour + " : " + minute + " " + AM_PM);

                }
            });
        }
    }

    public class TwoBtnDialog extends Dialog {
        TwoBtnDialog twoBtnDialog = this;
        Context context;

        public TwoBtnDialog(final Context context) {
            super(context);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialog_two_btn);
            getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            this.context = context;
            TextView title1 = (TextView) findViewById(R.id.title1);
            TextView title2 = (TextView) findViewById(R.id.title2);
            TextView btn1 = (TextView) findViewById(R.id.btn1);
            TextView btn2 = (TextView) findViewById(R.id.btn2);
            title2.setVisibility(View.GONE);
            title1.setText("이미지 첨부방식을\n선택해 주세요 !");
            btn1.setText("카메라");
            btn2.setText("갤러리");
            btn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    twoBtnDialog.dismiss();
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
            btn2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    twoBtnDialog.dismiss();
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                    intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, GALLERY);
                }
            });
        }
    }
}
