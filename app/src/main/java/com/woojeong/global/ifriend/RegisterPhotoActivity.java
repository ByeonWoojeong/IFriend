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
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;
import static com.woojeong.global.ifriend.GlobalApplication.setDarkMode;

public class RegisterPhotoActivity extends AppCompatActivity {
    private static String TAG = "RegisterPhotoActivity";
    Context context;
    InputMethodManager ipmm;
    AQuery aQuery = null;
    OneBtnDialog oneBtnDialog;
    WithChildrenDialog withChildrenDialog;
//    OnlyChildDialog onlyChildDialog;

    FrameLayout back_con, with_child_con, only_child_con, ok_con;
    ImageView back, choice_with_child, with_child_img, with_child_delete, choice_only_child, only_child_img, only_child_delete, home_img1, home_img2, outdoor_img1, outdoor_img2;
    TextView ok;

    int CAMERA1 = 701, GALLERY1 = 801, totalImageCount1 = 0, addImageCount1 = 0, getImageCount1 = 0;
    ArrayList<Bitmap> originalBitmap1 = new ArrayList<Bitmap>();
    ArrayList<Bitmap> resizeBitmap1 = new ArrayList<Bitmap>();
    ArrayList<String> imagePath1 = new ArrayList<String>();
    ArrayList<File> file1 = new ArrayList<File>();
    ArrayList<String> getImageList1 = new ArrayList<String>();
    String filePath1;
    Uri fileUri1;

    int CAMERA2 = 702, GALLERY2 = 802, totalImageCount2 = 0, addImageCount2 = 0, getImageCount2 = 0;
    ArrayList<Bitmap> originalBitmap2 = new ArrayList<Bitmap>();
    ArrayList<Bitmap> resizeBitmap2 = new ArrayList<Bitmap>();
    ArrayList<String> imagePath2 = new ArrayList<String>();
    ArrayList<File> file2 = new ArrayList<File>();
    ArrayList<String> getImageList2 = new ArrayList<String>();
    String filePath2;
    Uri fileUri2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_photo);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#ffffff"));
            window.setBackgroundDrawable(null);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                setDarkMode(RegisterPhotoActivity.this, true);
            }
        }
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

        with_child_con = findViewById(R.id.with_child_con);
//        only_child_con = findViewById(R.id.only_child_con);
        ok_con = findViewById(R.id.ok_con);

        choice_with_child = findViewById(R.id.choice_with_child);
        with_child_img = findViewById(R.id.with_child_img);
        with_child_delete = findViewById(R.id.with_child_delete);
//        choice_only_child = findViewById(R.id.choice_only_child);
//        only_child_img = findViewById(R.id.only_child_img);
//        only_child_delete = findViewById(R.id.only_child_delete);
        home_img1 = findViewById(R.id.home_img1);
        home_img2 = findViewById(R.id.home_img2);
        outdoor_img1 = findViewById(R.id.outdoor_img1);
        outdoor_img2 = findViewById(R.id.outdoor_img2);

        ok = findViewById(R.id.ok);

        choice_with_child.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                withChildrenDialog = new WithChildrenDialog(RegisterPhotoActivity.this);
                withChildrenDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                withChildrenDialog.show();
            }
        });

//        choice_only_child.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onlyChildDialog = new OnlyChildDialog(RegisterPhotoActivity.this);
//                onlyChildDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//                onlyChildDialog.show();
//            }
//        });

        with_child_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                totalImageCount1 = 0;
                addImageCount1 = 0;
                getImageCount1--;
                with_child_img.setImageBitmap(null);
                with_child_con.setVisibility(View.GONE);
                imagePath1.clear();
                originalBitmap1.clear();
                resizeBitmap1.clear();
            }
        });
//        only_child_delete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                totalImageCount2 = 0;
//                addImageCount2 = 0;
//                getImageCount2--;
//                only_child_con.setVisibility(View.GONE);
//                only_child_img.setImageBitmap(null);
//                imagePath2.clear();
//                originalBitmap2.clear();
//                resizeBitmap2.clear();
//            }
//        });

        ok_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ok.callOnClick();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences get_token = getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
                final String getToken = get_token.getString("Token", "");
                final String url = ServerUrl.getBaseUrl() + "/friend/update";
                Map<String, Object> params = new HashMap<String, Object>();

                if (with_child_con.getVisibility() == View.GONE ) {
                    // || only_child_con.getVisibility() == View.GONE
                    oneBtnDialog = new OneBtnDialog(RegisterPhotoActivity.this, "업로드할 사진을 선택해주세요 !", "확인");
                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    oneBtnDialog.setCancelable(false);
                    oneBtnDialog.show();
                }

                file1.clear();
                if(addImageCount1 == 1){
                    Uri fileUri = getImageUri(RegisterPhotoActivity.this, resizeBitmap1.get(0));
                    String filePath = getImageRealPathFromURI(RegisterPhotoActivity.this.getContentResolver(), fileUri);
                    File makeFile1 = new File(filePath);
                    file1.add(makeFile1);
                    params.put("image1", makeFile1);
                } else if (addImageCount1 == -1){
                    params.put("image1", "");
                }

//                file2.clear();
//                if(addImageCount2 == 1){
//                    Uri fileUri = getImageUri(RegisterPhotoActivity.this, resizeBitmap2.get(0));
//                    String filePath = getImageRealPathFromURI(RegisterPhotoActivity.this.getContentResolver(), fileUri);
//                    File makeFile2 = new File(filePath);
//                    file2.add(makeFile2);
//                    params.put("image2", makeFile2);
//                } else if (addImageCount2 == -1){
//                    params.put("image2", "");
//                }

                Log.i(TAG, " params " + params);
                aQuery.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
                    @Override
                    public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
                        Log.i(TAG, " jsonObject " + jsonObject);
                        try {
                            if (jsonObject.getBoolean("return")) {    //return이 true 면?
                                Toast.makeText(RegisterPhotoActivity.this, "사진 업로드 성공 !", Toast.LENGTH_SHORT).show();
                                setResult(123);
                                finish();

                            } else if (!jsonObject.getBoolean("return")) {
                                if("login".equals(jsonObject.getString("type"))){
                                    Toast.makeText(RegisterPhotoActivity.this, "로그인 해주세요 ! ", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(RegisterPhotoActivity.this, LoginActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    oneBtnDialog = new OneBtnDialog(RegisterPhotoActivity.this, "사진 업로드를 실패했습니다 !", "확인");
                                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                    oneBtnDialog.setCancelable(false);
                                    oneBtnDialog.show();
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }.header("EPOCH-AGENT", "" + getToken).header("User-Agent", "android"));
            }
        });

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                viewSettings();
            }
        }, 300);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    void viewSettings() {
        SharedPreferences get_token = getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
        final String getToken = get_token.getString("Token", "");
        final String url = ServerUrl.getBaseUrl() + "/friend/data";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("image1", "1");
        params.put("image2", "1");
        aQuery.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
                Log.i(TAG, " jsonObject " + jsonObject);
                try {
                    if (jsonObject.getBoolean("return")) {    //return이 true 면?
                        JSONObject jsonData = jsonObject.getJSONObject("data");
                        if (!"".equals(jsonData.getString("image1"))) {
                            with_child_con.setVisibility(View.VISIBLE);
                            with_child_img.setImageBitmap(null);
                            Glide.with(RegisterPhotoActivity.this).load(ServerUrl.getBaseUrl() + "/uploads/images/origin/" + jsonData.getString("image1")).transition(withCrossFade()).into(with_child_img);
                        } else {
                            with_child_con.setVisibility(View.GONE);
                        }

//                        if (!"".equals(jsonData.getString("image2"))) {
//                            only_child_con.setVisibility(View.VISIBLE);
//                            only_child_img.setImageBitmap(null);
//                            Glide.with(RegisterPhotoActivity.this).load(ServerUrl.getBaseUrl() + "/uploads/images/origin/" + jsonData.getString("image2")).transition(withCrossFade()).into(only_child_img);
//                        } else {
//                            only_child_con.setVisibility(View.GONE);
//                        }

                    } else if (!jsonObject.getBoolean("return")) {
                        if("login".equals(jsonObject.getString("type"))){
                            Toast.makeText(RegisterPhotoActivity.this, "로그인 해주세요 ! ", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegisterPhotoActivity.this, LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        } else {

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.header("EPOCH-AGENT", "" + getToken).header("User-Agent", "android"));

    }

    Uri getFileUri1() {
        File dir = new File(getFilesDir(), "Picture");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(dir, System.currentTimeMillis() + ".png");
        imagePath1.add(file.getAbsolutePath() + "");
        return FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".fileprovider", file);
    }

//    Uri getFileUri2() {
//        File dir = new File(getFilesDir(), "Picture");
//        if (!dir.exists()) {
//            dir.mkdirs();
//        }
//        File file = new File(dir, System.currentTimeMillis() + ".png");
//        imagePath2.add(file.getAbsolutePath() + "");
//        return FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".fileprovider", file);
//    }

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
            case 701:
                if (resultCode == RESULT_CANCELED) {
                    return;
                } else {
                    try {
                        if (Build.VERSION.SDK_INT < 21) {
                            Uri imgUri = data.getData();
                            imagePath1.add(getImageRealPathFromURI(RegisterPhotoActivity.this.getContentResolver(), imgUri));
                        }
                        ExifInterface exif = new ExifInterface(imagePath1.get(totalImageCount1));
                        int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                        int exifDegree = exifOrientationToDegrees(exifOrientation);
                        originalBitmap1.add(rotate(BitmapFactory.decodeFile(imagePath1.get(totalImageCount1)), exifDegree));
                        resizeBitmap1.add(rotate(resizeBitmap(imagePath1.get(totalImageCount1), 1080, 1920), exifDegree));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (totalImageCount1 < 1) {
                        with_child_con.setVisibility(View.VISIBLE);

                    }
                    Glide.with(RegisterPhotoActivity.this).load(imagePath1.get(totalImageCount1)).transition(withCrossFade()).into(with_child_img);
                    totalImageCount1++;
                    addImageCount1++;
                }
                break;
            case 801:
                if (resultCode == RESULT_CANCELED) {
                    return;
                } else {
                    try {
                        Uri imgUri = data.getData();
                        imagePath1.add(getImageRealPathFromURI(RegisterPhotoActivity.this.getContentResolver(), imgUri));
                        ExifInterface exif = new ExifInterface(imagePath1.get(totalImageCount1));
                        int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                        int exifDegree = exifOrientationToDegrees(exifOrientation);
                        originalBitmap1.add(rotate(BitmapFactory.decodeFile(imagePath1.get(totalImageCount1)), exifDegree));
                        resizeBitmap1.add(rotate(resizeBitmap(imagePath1.get(totalImageCount1), 1080, 1920), exifDegree));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (totalImageCount1 < 1) {
                        with_child_con.setVisibility(View.VISIBLE);
                    }
                    Glide.with(RegisterPhotoActivity.this).load(imagePath1.get(totalImageCount1)).transition(withCrossFade()).into(with_child_img);
                    totalImageCount1++;
                    addImageCount1++;
                }
                break;

//            case 702:
//                if (resultCode == RESULT_CANCELED) {
//                    return;
//                } else {
//                    try {
//                        if (Build.VERSION.SDK_INT < 21) {
//                            Uri imgUri = data.getData();
//                            imagePath2.add(getImageRealPathFromURI(RegisterPhotoActivity.this.getContentResolver(), imgUri));
//                        }
//                        ExifInterface exif = new ExifInterface(imagePath2.get(totalImageCount2));
//                        int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
//                        int exifDegree = exifOrientationToDegrees(exifOrientation);
//                        originalBitmap2.add(rotate(BitmapFactory.decodeFile(imagePath2.get(totalImageCount2)), exifDegree));
//                        resizeBitmap2.add(rotate(resizeBitmap(imagePath2.get(totalImageCount2), 1080, 1920), exifDegree));
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    if (totalImageCount2 < 1) {
//                        only_child_con.setVisibility(View.VISIBLE);
//
//                    }
//                    Glide.with(RegisterPhotoActivity.this).load(imagePath2.get(totalImageCount2)).transition(withCrossFade()).into(only_child_img);
//                    totalImageCount2++;
//                    addImageCount2++;
//                }
//                break;
//            case 802:
//                if (resultCode == RESULT_CANCELED) {
//                    return;
//                } else {
//                    try {
//                        Uri imgUri = data.getData();
//                        imagePath2.add(getImageRealPathFromURI(RegisterPhotoActivity.this.getContentResolver(), imgUri));
//                        ExifInterface exif = new ExifInterface(imagePath2.get(totalImageCount2));
//                        int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
//                        int exifDegree = exifOrientationToDegrees(exifOrientation);
//                        originalBitmap2.add(rotate(BitmapFactory.decodeFile(imagePath2.get(totalImageCount2)), exifDegree));
//                        resizeBitmap2.add(rotate(resizeBitmap(imagePath2.get(totalImageCount2), 1080, 1920), exifDegree));
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    if (totalImageCount2 < 1) {
//                        only_child_con.setVisibility(View.VISIBLE);
//                    }
//                    Glide.with(RegisterPhotoActivity.this).load(imagePath2.get(totalImageCount2)).transition(withCrossFade()).into(only_child_img);
//                    totalImageCount2++;
//                    addImageCount2++;
//                }
//                break;

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

    public class WithChildrenDialog extends Dialog {
        WithChildrenDialog withChildrenDialog = this;
        Context context;

        public WithChildrenDialog(final Context context) {
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
                    withChildrenDialog.dismiss();
                    if (Build.VERSION.SDK_INT > 21) {
                        Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, getFileUri1());
                        startActivityForResult(intent, CAMERA1);
                    } else {
                        Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, CAMERA1);
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
                    withChildrenDialog.dismiss();
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                    intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, GALLERY1);
                }
            });
        }
    }

//    public class OnlyChildDialog extends Dialog {
//        OnlyChildDialog onlyChildDialog = this;
//        Context context;
//
//        public OnlyChildDialog(final Context context) {
//            super(context);
//            requestWindowFeature(Window.FEATURE_NO_TITLE);
//            setContentView(R.layout.camera_dialog);
//            getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            getWindow().setGravity(Gravity.BOTTOM | Gravity.LEFT);
//            this.context = context;
//            FrameLayout btn1_con = (FrameLayout) findViewById(R.id.btn1_con);
//            FrameLayout btn2_con = (FrameLayout) findViewById(R.id.btn2_con);
//            final TextView btn1 = (TextView) findViewById(R.id.btn1);
//            final TextView btn2 = (TextView) findViewById(R.id.btn2);
//            btn1_con.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    btn1.callOnClick();
//                }
//            });
//            btn1.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    onlyChildDialog.dismiss();
//                    if (Build.VERSION.SDK_INT > 21) {
//                        Intent intent = new Intent(Intent.ACTION_PICK);
//                        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
//                        intent.putExtra(MediaStore.EXTRA_OUTPUT, getFileUri2());
//                        startActivityForResult(intent, CAMERA2);
//                    } else {
//                        Intent intent = new Intent(Intent.ACTION_PICK);
//                        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
//                        startActivityForResult(intent, CAMERA2);
//                    }
//                }
//            });
//            btn2_con.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    btn2.callOnClick();
//                }
//            });
//            btn2.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    onlyChildDialog.dismiss();
//                    Intent intent = new Intent(Intent.ACTION_PICK);
//                    intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
//                    intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                    startActivityForResult(intent, GALLERY2);
//                }
//            });
//        }
//    }
}
