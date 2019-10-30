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
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.bumptech.glide.Glide;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.woojeong.global.ifriend.DTO.Career;
import com.woojeong.global.ifriend.DTO.Two;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static android.media.MediaRecorder.VideoSource.CAMERA;
import static com.smarteist.autoimageslider.IndicatorView.utils.DensityUtils.dpToPx;
import static com.woojeong.global.ifriend.GlobalApplication.setDarkMode;

public class RegisterChild1Activity extends AppCompatActivity {
    private static String TAG = "RegisterChild1Activity";

    Context context;
    InputMethodManager ipmm;
    AQuery aQuery = null;
    OneBtnDialog oneBtnDialog;
    TwoBtnDialog twoBtnDialog;

    ScrollView scrollView;
    FrameLayout back_con, choice_picture_con, picture_con, ranking_spinner_con, born_spinner_con, next_con;
    ImageView back, choice_picture_icon, child_picture, delete, ranking_spinner_down, born_spinner_down;
    TextView title, choice_picture, ranking_txt, boy, girl, born_txt, age_text, yes, no, next;
    EditText name, education_place, interest;
    SpinnerReselect ranking_spinner, born_spinner;

    int CAMERA = 700, GALLERY = 800, totalImageCount = 0;
    ArrayList<String> imagePath = new ArrayList<String>();
    ArrayList<Bitmap> originalBitmap = new ArrayList<Bitmap>();
    ArrayList<Bitmap> resizeBitmap = new ArrayList<Bitmap>();
    ArrayList<File> file = new ArrayList<File>();
    ArrayList<String> getImageList = new ArrayList<String>();
    String filePath, getImage;
    Uri fileUri;
    boolean isPicture = false;

    String[] ranking = new String[]{"첫째 아이", "둘째 아이", "셋째 아이", "넷째 아이"};
    String getRanking = "", getGender = "", getInsurance = "";
    String[] bornYear = new String[]{"출생년도", "2012", "2013", "2014", "2015", "2016", "2017", "2018", "2019"};
    int age;

    Handler handler;
    Intent getIntent;
    String whatPage, getChild, getAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_child1);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#ffffff"));
            window.setBackgroundDrawable(null);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                setDarkMode(RegisterChild1Activity.this, true);
            }
        }

        getIntent = getIntent();
        whatPage = getIntent.getStringExtra("what");    //register, edit, add
        getChild = getIntent.getStringExtra("child");


        Log.i(TAG, " PAGE " + whatPage);

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

        scrollView = findViewById(R.id.scrollView);
        choice_picture_con = findViewById(R.id.choice_picture_con);
        picture_con = findViewById(R.id.picture_con);
        ranking_spinner_con = findViewById(R.id.ranking_spinner_con);
        born_spinner_con = findViewById(R.id.born_spinner_con);
        next_con = findViewById(R.id.next_con);
        choice_picture_icon = findViewById(R.id.choice_picture_icon);
        child_picture = findViewById(R.id.child_picture);
        delete = findViewById(R.id.delete);
        ranking_spinner_down = findViewById(R.id.ranking_spinner_down);
        born_spinner_down = findViewById(R.id.born_spinner_down);
        choice_picture = findViewById(R.id.choice_picture);

        title = findViewById(R.id.title);


        if ("edit".equals(whatPage)) {
            title.setText("우리 아이 프로필 수정");
            handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    viewSettings();
                    isPicture = true;
                }
            }, 350);
        } else {
            title.setText("우리 아이 프로필 등록");
        }
        ranking_txt = findViewById(R.id.ranking_txt);
        boy = findViewById(R.id.boy);
        girl = findViewById(R.id.girl);
        born_txt = findViewById(R.id.born_txt);
        age_text = findViewById(R.id.age_text);
        yes = findViewById(R.id.yes);
        no = findViewById(R.id.no);
        next = findViewById(R.id.next);
        name = findViewById(R.id.name);
        education_place = findViewById(R.id.education_place);
        interest = findViewById(R.id.interest);
        ranking_spinner = findViewById(R.id.ranking_spinner);
        born_spinner = findViewById(R.id.born_spinner);

        choice_picture_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                twoBtnDialog = new TwoBtnDialog(RegisterChild1Activity.this);
                twoBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                twoBtnDialog.show();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choice_picture_con.setVisibility(View.VISIBLE);
                picture_con.setVisibility(View.GONE);
                child_picture.setImageBitmap(null);
                imagePath.clear();
                isPicture = false;
            }
        });

        ArrayAdapter<String> rankingAdapter = new ArrayAdapter<String>(context, R.layout.spinner_item, ranking);
        rankingAdapter.setDropDownViewResource(R.layout.spinner_item);
        ranking_spinner.setAdapter(rankingAdapter);
        ranking_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ranking_txt.setText(ranking_spinner.getSelectedItem().toString());
                getRanking = (ranking_spinner.getSelectedItemPosition() + 1) + "";
                if ("-1".equals(getRanking)) {
                    getRanking = "";
                }
                Log.i(TAG, getRanking);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        boy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boy.setBackground(getResources().getDrawable(R.drawable.border_radius_yellow));
                boy.setTextColor(getResources().getColor(R.color.mainColor));
                boy.setSelected(true);
                girl.setBackground(getResources().getDrawable(R.drawable.border_radius1));
                girl.setTextColor(getResources().getColor(R.color.editHintGray));
                girl.setSelected(false);
                getGender = "1";
            }
        });
        girl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                girl.setBackground(getResources().getDrawable(R.drawable.border_radius_yellow));
                girl.setTextColor(getResources().getColor(R.color.mainColor));
                girl.setSelected(true);
                boy.setBackground(getResources().getDrawable(R.drawable.border_radius1));
                boy.setTextColor(getResources().getColor(R.color.editHintGray));
                boy.setSelected(false);
                getGender = "2";
            }
        });

        ArrayAdapter<String> yearAdapter = new ArrayAdapter<String>(context, R.layout.spinner_item, bornYear);
        yearAdapter.setDropDownViewResource(R.layout.spinner_item);
        born_spinner.setAdapter(yearAdapter);
        born_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                born_txt.setText(born_spinner.getSelectedItem().toString());

                if (!"출생년도".equals(born_txt.getText().toString())) {
                    age = 2019 - Integer.parseInt(born_txt.getText().toString());
                    age_text.setText("만 " + age + "세");
                } else {
                    age_text.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yes.setBackground(getResources().getDrawable(R.drawable.border_radius_yellow));
                yes.setTextColor(getResources().getColor(R.color.mainColor));
                yes.setSelected(true);
                no.setBackground(getResources().getDrawable(R.drawable.border_radius1));
                no.setTextColor(getResources().getColor(R.color.editHintGray));
                no.setSelected(false);
                getInsurance = "1";
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                no.setBackground(getResources().getDrawable(R.drawable.border_radius_yellow));
                no.setTextColor(getResources().getColor(R.color.mainColor));
                no.setSelected(true);
                yes.setBackground(getResources().getDrawable(R.drawable.border_radius1));
                yes.setTextColor(getResources().getColor(R.color.editHintGray));
                yes.setSelected(false);
                getInsurance = "0";
            }
        });


        next_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                next.callOnClick();
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("".equals(name.getText().toString())) {
                    oneBtnDialog = new OneBtnDialog(RegisterChild1Activity.this, "이름을 입력해주세요 !", "확인");
                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    oneBtnDialog.setCancelable(false);
                    oneBtnDialog.show();
                    return;
                } else if ("".equals(ranking_txt.getText().toString())) {
                    oneBtnDialog = new OneBtnDialog(RegisterChild1Activity.this, "출생 순위를 선택해주세요 !", "확인");
                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    oneBtnDialog.setCancelable(false);
                    oneBtnDialog.show();
                    return;
                } else if ("".equals(getGender)) {
                    oneBtnDialog = new OneBtnDialog(RegisterChild1Activity.this, "아이 성별을 선택해주세요 !", "확인");
                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    oneBtnDialog.setCancelable(false);
                    oneBtnDialog.show();
                    return;
                } else if ("".equals(born_txt.getText().toString())) {
                    oneBtnDialog = new OneBtnDialog(RegisterChild1Activity.this, "출생 년도를 선택해주세요 !", "확인");
                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    oneBtnDialog.setCancelable(false);
                    oneBtnDialog.show();
                    return;
                } else if ("출생년도".equals(born_txt.getText().toString())) {
                    oneBtnDialog = new OneBtnDialog(RegisterChild1Activity.this, "출생 년도를 선택해주세요 !", "확인");
                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    oneBtnDialog.setCancelable(false);
                    oneBtnDialog.show();
                    return;
                } else if ("".equals(education_place.getText().toString())) {
                    oneBtnDialog = new OneBtnDialog(RegisterChild1Activity.this, "교육 기관을 입력해주세요 !", "확인");
                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    oneBtnDialog.setCancelable(false);
                    oneBtnDialog.show();
                    return;
                } else if ("".equals(getInsurance)) {
                    oneBtnDialog = new OneBtnDialog(RegisterChild1Activity.this, "보험 유무를 선택해주세요 !", "확인");
                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    oneBtnDialog.setCancelable(false);
                    oneBtnDialog.show();
                    return;
                } else if ("".equals(interest.getText().toString())) {
                    oneBtnDialog = new OneBtnDialog(RegisterChild1Activity.this, "아이의 관심 대상을\n입력해주세요 !", "확인");
                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    oneBtnDialog.setCancelable(false);
                    oneBtnDialog.show();
                    return;
                }

                file.clear();

                Intent intent = new Intent(RegisterChild1Activity.this, RegisterChild2Activity.class);
                if (isPicture) {
                    if("register".equals(whatPage)){
                        if (resizeBitmap.size() == 1) {
                            fileUri = getImageUri(RegisterChild1Activity.this, resizeBitmap.get(0));
                            filePath = getImageRealPathFromURI(RegisterChild1Activity.this.getContentResolver(), fileUri);
                            File makeFile = new File(filePath);
                            file.add(makeFile);
                            intent.putExtra("image", filePath);
                        }
                    } else {    //아이 프로필 수정
                        if (resizeBitmap.size() == 1) {
                            Log.i(TAG, " 1111");
                            fileUri = getImageUri(RegisterChild1Activity.this, resizeBitmap.get(0));
                            filePath = getImageRealPathFromURI(RegisterChild1Activity.this.getContentResolver(), fileUri);
                            File makeFile = new File(filePath);
                            file.add(makeFile);
                            intent.putExtra("image", filePath);
                        } else {
                            Log.i(TAG, " 2222");
                            intent.putExtra("image", getImage);
                        }

                    }

                } else {
                    oneBtnDialog = new OneBtnDialog(RegisterChild1Activity.this, "아이 사진을\n첨부해주세요 !", "확인");
                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    oneBtnDialog.setCancelable(false);
                    oneBtnDialog.show();
                    return;
                }
                intent.putExtra("what", whatPage);
                if("edit".equals(whatPage)){
                    intent.putExtra("child",getChild);
                }
                intent.putExtra("name", name.getText().toString());
                intent.putExtra("no", getRanking);
                intent.putExtra("gender", getGender);
                intent.putExtra("birth", born_txt.getText().toString());
                intent.putExtra("edu", education_place.getText().toString());
                intent.putExtra("ins", getInsurance);
                intent.putExtra("like", interest.getText().toString());

                startActivity(intent);
            }
        });
    }

    void viewSettings() {
        Log.i(TAG, " viewSettings ");
        SharedPreferences get_token = getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
        final String getToken = get_token.getString("Token", "");
        final String url = ServerUrl.getBaseUrl() + "/child/detail";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("child", getChild);
        Log.i(TAG, " params " + params);
        aQuery.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
                Log.i(TAG, " jsonObject " + jsonObject);
                try {
                    if (jsonObject.getBoolean("return")) {    //return이 true 면?
                        JSONObject jsonData = jsonObject.getJSONObject("data");

                        if (!"".equals(jsonData.getString("image"))) {
                            isPicture = true;
                            getImage = jsonData.getString("image");
                            choice_picture_con.setVisibility(View.GONE);
                            picture_con.setVisibility(View.VISIBLE);
                            Glide.with(RegisterChild1Activity.this).load(ServerUrl.getBaseUrl() + "/uploads/images/origin/" + getImage).clone().into(child_picture);
                        } else {
                            isPicture = false;
                            choice_picture_con.setVisibility(View.VISIBLE);
                            picture_con.setVisibility(View.GONE);
                        }

                        name.setText(jsonData.getString("name"));

                        if ("1".equals(jsonData.getString("no"))) {
                            ranking_txt.setText("첫째 아이");
                            getRanking = "1";
                        } else if ("2".equals(jsonData.getString("no"))) {
                            ranking_txt.setText("둘째 아이");
                            getRanking = "2";
                        } else if ("3".equals(jsonData.getString("no"))) {
                            ranking_txt.setText("셋째 아이");
                            getRanking = "3";
                        } else if ("4".equals(jsonData.getString("no"))) {
                            ranking_txt.setText("넷째 아이");
                            getRanking = "4";
                        }

                        if ("1".equals(jsonData.getString("gender"))) {
                            boy.setBackground(getResources().getDrawable(R.drawable.border_radius_yellow));
                            boy.setTextColor(getResources().getColor(R.color.mainColor));
                            boy.setSelected(true);
                            girl.setBackground(getResources().getDrawable(R.drawable.border_radius1));
                            girl.setTextColor(getResources().getColor(R.color.editHintGray));
                            girl.setSelected(false);
                            getGender = "1";
                        } else if ("2".equals(jsonData.getString("gender"))) {
                            girl.setBackground(getResources().getDrawable(R.drawable.border_radius_yellow));
                            girl.setTextColor(getResources().getColor(R.color.mainColor));
                            girl.setSelected(true);
                            boy.setBackground(getResources().getDrawable(R.drawable.border_radius1));
                            boy.setTextColor(getResources().getColor(R.color.editHintGray));
                            boy.setSelected(false);
                            getGender = "2";
                        }
                        born_txt.setText(jsonData.getString("birth") + "년생");
                        age = 2019 - Integer.parseInt(born_txt.getText().toString().replace("년생", ""));
                        age_text.setText("만 " + age + "세");
                        education_place.setText(jsonData.getString("edu"));

                        if ("0".equals(jsonData.getString("ins"))) {
                            no.setBackground(getResources().getDrawable(R.drawable.border_radius_yellow));
                            no.setTextColor(getResources().getColor(R.color.mainColor));
                            no.setSelected(true);
                            yes.setBackground(getResources().getDrawable(R.drawable.border_radius1));
                            yes.setTextColor(getResources().getColor(R.color.editHintGray));
                            yes.setSelected(false);
                            getInsurance = "0";
                        } else if ("1".equals(jsonData.getString("ins"))) {
                            yes.setBackground(getResources().getDrawable(R.drawable.border_radius_yellow));
                            yes.setTextColor(getResources().getColor(R.color.mainColor));
                            yes.setSelected(true);
                            no.setBackground(getResources().getDrawable(R.drawable.border_radius1));
                            no.setTextColor(getResources().getColor(R.color.editHintGray));
                            no.setSelected(false);
                            getInsurance = "1";
                        }

                        interest.setText(jsonData.getString("like"));

                    } else if (!jsonObject.getBoolean("return")) {
                        if("login".equals(jsonObject.getString("type"))){
                            Toast.makeText(RegisterChild1Activity.this, "로그인 해주세요 ! ", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegisterChild1Activity.this, LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            oneBtnDialog = new OneBtnDialog(RegisterChild1Activity.this, "정보를 불러올 수 없습니다 !", "확인");
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

    Uri getImageUri(Context context, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
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
                            imagePath.clear();
                            imagePath.add(getImageRealPathFromURI(RegisterChild1Activity.this.getContentResolver(), imgUri));
                        }
                        ExifInterface exif = new ExifInterface(imagePath.get(totalImageCount));
                        int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                        int exifDegree = exifOrientationToDegrees(exifOrientation);
                        originalBitmap.clear();
                        resizeBitmap.clear();
                        originalBitmap.add(rotate(BitmapFactory.decodeFile(imagePath.get(totalImageCount)), exifDegree));
                        resizeBitmap.add(rotate(resizeBitmap(imagePath.get(totalImageCount), 1080, 1920), exifDegree));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    picture_con.setVisibility(View.VISIBLE);
                    choice_picture_con.setVisibility(View.GONE);
                    isPicture = true;
                    scrollView.smoothScrollTo(0, 0);
                    Glide.with(RegisterChild1Activity.this).load(imagePath.get(totalImageCount)).transition(withCrossFade()).into(child_picture);
                }
                break;
            case 800:
                if (resultCode == RESULT_CANCELED) {
                    return;
                } else {
                    try {
                        Uri imgUri = data.getData();
                        imagePath.clear();
                        imagePath.add(getImageRealPathFromURI(RegisterChild1Activity.this.getContentResolver(), imgUri));
                        ExifInterface exif = new ExifInterface(imagePath.get(totalImageCount));
                        int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                        int exifDegree = exifOrientationToDegrees(exifOrientation);
                        originalBitmap.clear();
                        resizeBitmap.clear();
                        originalBitmap.add(rotate(BitmapFactory.decodeFile(imagePath.get(totalImageCount)), exifDegree));
                        resizeBitmap.add(rotate(resizeBitmap(imagePath.get(totalImageCount), 1080, 1920), exifDegree));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    picture_con.setVisibility(View.VISIBLE);
                    choice_picture_con.setVisibility(View.GONE);
                    isPicture = true;
                    scrollView.smoothScrollTo(0, 0);
                    Glide.with(RegisterChild1Activity.this).load(imagePath.get(totalImageCount)).transition(withCrossFade()).into(child_picture);
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
            btn1.setTextColor(Color.parseColor("#ffcf28"));
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
