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
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;
import static com.woojeong.global.ifriend.GlobalApplication.setDarkMode;

public class WriteReviewActivity extends AppCompatActivity {
    private static String TAG = "WriteReviewActivity";
    Context context;
    InputMethodManager ipmm;
    AQuery aQuery = null;
    OneBtnDialog oneBtnDialog;
    FinishDialog finishDialog;
    TwoBtnDialog twoBtnDialog;

    FrameLayout back_con, update_con, imageview_con1;
    ImageView back, photo, pictures, image, imageview1, closeImageview1;
    ScrollView scrollView;
    TextView name, title, change_guide, update;

    MaterialRatingBar rating_bar;
    float star = 0;

    EditText write;
    LinearLayout pictures_con;

    Intent getIntent;
    String getMode, getReserve, getWrite, getIdx, getImage, getName, getReviewImage;

    int CAMERA = 700, GALLERY = 800, totalImageCount = 0, addImageCount = 0, getImageCount = 0;
    ArrayList<Bitmap> originalBitmap = new ArrayList<Bitmap>();
    ArrayList<Bitmap> resizeBitmap = new ArrayList<Bitmap>();
    ArrayList<String> imagePath = new ArrayList<String>();
    ArrayList<File> file = new ArrayList<File>();
    ArrayList<String> getImageList = new ArrayList<String>();
    String filePath;
    Uri fileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_review);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#ffffff"));
            window.setBackgroundDrawable(null);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                setDarkMode(WriteReviewActivity.this, true);
            }
        }

        getIntent = getIntent();
        getMode = getIntent.getStringExtra("mode");
        getReserve = getIntent.getStringExtra("reserve");
        getIdx = getIntent.getStringExtra("idx");
        getImage = getIntent.getStringExtra("image");
        getName = getIntent.getStringExtra("name");
        getWrite = getIntent.getStringExtra("write");

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

        pictures = findViewById(R.id.pictures);
        photo = findViewById(R.id.photo);
        image = findViewById(R.id.image);
        scrollView = findViewById(R.id.scrollView);
        name = findViewById(R.id.name);
        rating_bar = findViewById(R.id.rating_bar);
        write = findViewById(R.id.write);

        title = findViewById(R.id.title);
        change_guide = findViewById(R.id.change_guide);

        pictures_con = findViewById(R.id.pictures_con);
        update_con = findViewById(R.id.update_con);
        update = findViewById(R.id.update);

        imageview_con1 = findViewById(R.id.imageview_con1);
        imageview1 = findViewById(R.id.imageview1);
        closeImageview1 = findViewById(R.id.closeImageview1);

        closeImageview1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                totalImageCount = 0;
                addImageCount = 0;
                getImageCount--;
                imageview1.setImageBitmap(null);
                imageview_con1.setVisibility(View.GONE);
                imagePath.clear();
                originalBitmap.clear();
                resizeBitmap.clear();
            }
        });


        name.setText(getName);
        circleImage(photo,ServerUrl.getBaseUrl() + "/uploads/images/origin/" + getImage);
        if("sitter".equals(getMode)){
            title.setText("방문 후기 작성");
            change_guide.setText("님의 아이 방문 후기를\n남겨주세요.");
            pictures_con.setVisibility(View.GONE);
        } else {
            title.setText("후기 작성");
            change_guide.setText("님에 대한 후기를 남겨주세요.");
            pictures_con.setVisibility(View.VISIBLE);
        }

        if("edit".equals(getWrite)){
            Log.i(TAG, "  edit");
            viewSettings();
        }else {

        }

        rating_bar.setOnRatingChangeListener(new MaterialRatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChanged(MaterialRatingBar ratingBar, float rating) {
                star = rating * 2;
            }
        });

        pictures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ipmm.hideSoftInputFromWindow(pictures.getWindowToken(), 0);
                if (totalImageCount >= 1) {
                    oneBtnDialog = new OneBtnDialog(WriteReviewActivity.this, "이미지는 최대 1장까지\n첨부 가능합니다 !", "확인");
                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    oneBtnDialog.setCancelable(false);
                    oneBtnDialog.show();
                    return;
                }
                twoBtnDialog = new TwoBtnDialog(WriteReviewActivity.this);
                twoBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                twoBtnDialog.show();
            }
        });

        update_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update.callOnClick();
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences get_token = getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
                final String getToken = get_token.getString("Token", "");
                String url = ServerUrl.getBaseUrl() + "/review/update";
                final Map<String, Object> params = new HashMap<String, Object>();
                params.put("star", star);
                params.put("content", write.getText().toString());
                params.put("reserve", getReserve);
                if(!"sitter".equals(getMode)){
                    file.clear();
                    if(imagePath.size() > 0){
                        Uri fileUri = getImageUri(WriteReviewActivity.this, resizeBitmap.get(0));
                        String filePath = getImageRealPathFromURI(WriteReviewActivity.this.getContentResolver(), fileUri);
                        File makeFile = new File(filePath);
                        file.add(makeFile);
                        params.put("image", makeFile);
                    } else {
                    }
                }
                Log.i(TAG, " " + params);
                aQuery.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
                    @Override
                    public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
                        Log.i(TAG, " " + jsonObject );
                        try {
                            if (jsonObject.getBoolean("return")) {    //return이 true 면?
                                finishDialog = new FinishDialog(WriteReviewActivity.this, "후기 작성을 성공하였습니다.", "확인");
                                finishDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                finishDialog.setCancelable(false);
                                finishDialog.show();
                            } else if (!jsonObject.getBoolean("return")) {
                                if("login".equals(jsonObject.getString("type"))){
                                    Toast.makeText(WriteReviewActivity.this, "로그인 해주세요 ! ", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(WriteReviewActivity.this, LoginActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    oneBtnDialog = new OneBtnDialog(WriteReviewActivity.this, "후기 작성을 실패하였습니다.", "확인");
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
    }

    void viewSettings() {
        SharedPreferences get_token = getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
        final String getToken = get_token.getString("Token", "");
        final String url = ServerUrl.getBaseUrl() + "/review/detail";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("reserve", getReserve);
        aQuery.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
                Log.i(TAG, " " + jsonObject);
                try {
                    if (jsonObject.getBoolean("return")) {    //return이 true 면?
                        JSONObject jsonData = jsonObject.getJSONObject("data");

                        if("sitter".equals(getMode)){
                            write.setText(jsonData.getString("friend"));
                            if (jsonData.getString("friend_star").equals("null")) {
                                star = 0.0f;
                            } else {
                                star = Float.parseFloat(jsonData.getString("friend_star"));
                            }
                            rating_bar.setProgress(Math.round(star));
                        } else {
                            write.setText(jsonData.getString("content"));
                            if (jsonData.getString("content_star").equals("null")) {
                                star = 0.0f;
                            } else {
                                star = Float.parseFloat(jsonData.getString("content_star"));
                            }
                            rating_bar.setProgress(Math.round(star));

                            if (!"null".equals(jsonData.getString("content_image"))) {
                                imageview_con1.setVisibility(View.VISIBLE);
                                imageview1.setImageBitmap(null);
                                getReviewImage = jsonData.getString("content_image");
                                Glide.with(WriteReviewActivity.this).load(ServerUrl.getBaseUrl() + "/uploads/images/origin/" + jsonData.getString("content_image")).transition(withCrossFade()).into(imageview1);
                            } else {
                                imageview_con1.setVisibility(View.GONE);
                            }

                        }
                    } else if (!jsonObject.getBoolean("return")) {
                        if("login".equals(jsonObject.getString("type"))){
                            Toast.makeText(WriteReviewActivity.this, "로그인 해주세요 ! ", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(context, LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            oneBtnDialog = new OneBtnDialog(WriteReviewActivity.this, "정보를 불러올 수 없습니다 !", "확인");
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

    private void circleImage(ImageView imageView, String getImg) {
        Log.i(TAG, " circleImage : " + getImg);
        Glide.with(context)
                .load(getImg)
                .apply(RequestOptions.bitmapTransform(new CropCircleTransformation()))
                .into(imageView);
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
                            imagePath.add(getImageRealPathFromURI(WriteReviewActivity.this.getContentResolver(), imgUri));
                        }
                        ExifInterface exif = new ExifInterface(imagePath.get(totalImageCount));
                        int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                        int exifDegree = exifOrientationToDegrees(exifOrientation);
                        originalBitmap.add(rotate(BitmapFactory.decodeFile(imagePath.get(totalImageCount)), exifDegree));
                        resizeBitmap.add(rotate(resizeBitmap(imagePath.get(totalImageCount), 1080, 1920), exifDegree));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    pictures_con.setVisibility(View.VISIBLE);
                    scrollView.smoothScrollTo(0, 0);
                    if (totalImageCount < 2) {
                        imageview_con1.setVisibility(View.VISIBLE);

                    }
                    Glide.with(WriteReviewActivity.this).load(imagePath.get(totalImageCount)).transition(withCrossFade()).into(imageview1);
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
                        imagePath.add(getImageRealPathFromURI(WriteReviewActivity.this.getContentResolver(), imgUri));
                        ExifInterface exif = new ExifInterface(imagePath.get(totalImageCount));
                        int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                        int exifDegree = exifOrientationToDegrees(exifOrientation);
                        originalBitmap.add(rotate(BitmapFactory.decodeFile(imagePath.get(totalImageCount)), exifDegree));
                        resizeBitmap.add(rotate(resizeBitmap(imagePath.get(totalImageCount), 1080, 1920), exifDegree));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    pictures_con.setVisibility(View.VISIBLE);
                    if (totalImageCount < 1) {
                        imageview_con1.setVisibility(View.VISIBLE);
                    }
                    Glide.with(WriteReviewActivity.this).load(imagePath.get(totalImageCount)).transition(withCrossFade()).into(imageview1);
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
