package com.woojeong.global.ifriend;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.woojeong.global.ifriend.DTO.Child;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class ChildAdapter extends BaseAdapter {
    Context context;
    int layout;
    ArrayList<Child> data;
    ListView listView;
    String what;
    AQuery aQuery = null;
    ChildAdapter childAdapter = this;
    int checkAccumulator;

    public ChildAdapter(Context context, int layout, ArrayList<Child> data, ListView listView, String what) {
        this.context = context;
        this.layout = layout;
        this.data = data;
        this.listView = listView;
        this.what = what;
        checkAccumulator = 0;
    }

    public void countCheck(boolean isChecked) {
        checkAccumulator += isChecked ? 1 : -1;
        Log.i("ChildAdapter", "" + checkAccumulator);
    }

    public int getCountCheck() {
        return checkAccumulator;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        aQuery = new AQuery(context);

        if (convertView == null) {
            convertView = View.inflate(context, layout, null);
            vh = new ViewHolder();
            vh.child_photo = convertView.findViewById(R.id.child_photo);
            vh.name = convertView.findViewById(R.id.name);
            vh.age = convertView.findViewById(R.id.age);
            vh.gender = convertView.findViewById(R.id.gender);
            vh.check_con = convertView.findViewById(R.id.check_con);
            vh.more_con = convertView.findViewById(R.id.more_con);
            vh.check = convertView.findViewById(R.id.check);
            vh.more = convertView.findViewById(R.id.more);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        final Child item = data.get(position);

        circleImage(vh.child_photo, ServerUrl.getBaseUrl() + "/uploads/images/origin/" + item.getImage());

        vh.name.setText(item.getName());
        vh.age.setText(item.getAge() + "살");

        if (item.getGender() == "1") {
            vh.gender.setText("남아");
        } else if (item.getGender() == "2") {
            vh.gender.setText("여아");
        }

        if ("check".equals(what)) {
            vh.check_con.setVisibility(View.VISIBLE);
            vh.check.setVisibility(View.VISIBLE);
            vh.more_con.setVisibility(View.GONE);
            vh.more.setVisibility(View.GONE);
        } else if ("profile".equals(what)) {
            vh.check_con.setVisibility(View.GONE);
            vh.check.setVisibility(View.GONE);
            vh.more_con.setVisibility(View.GONE);
            vh.more.setVisibility(View.GONE);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, IntoChildProfileActivity.class);
                    intent.putExtra("child", item.getNumber());
                    context.startActivity(intent);
                }
            });
        } else {
            vh.check_con.setVisibility(View.GONE);
            vh.check.setVisibility(View.GONE);
            vh.more_con.setVisibility(View.VISIBLE);
            vh.more.setVisibility(View.VISIBLE);
        }

        final ViewHolder finalVh = vh;

        vh.check_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalVh.check.callOnClick();
            }
        });
        vh.check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                countCheck(isChecked);
                if (isChecked) {
                    SharedPreferences childCheckList = context.getSharedPreferences("childCheckList", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor = childCheckList.edit();
                    editor.putString("" + position, item.getNumber());
                    editor.commit();
                } else {
                    SharedPreferences childCheckList = context.getSharedPreferences("childCheckList", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor = childCheckList.edit();
                    editor.remove("" + position);
                    editor.commit();
                }
                ((ReservationActivity) context).total_children.setText(getCountCheck() + " 명");
            }
        });

        vh.more_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalVh.more.callOnClick();
            }
        });
        vh.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RegisterChild1Activity.class);
                intent.putExtra("what", "edit");
                intent.putExtra("child", item.getNumber());
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    private void circleImage(ImageView imageView, String getImg) {
        Glide.with(context)
                .load(getImg)
                .apply(RequestOptions.bitmapTransform(new CropCircleTransformation()))
                .into(imageView);
    }

    class ViewHolder {
        ImageView child_photo, more;
        TextView name, age, gender;
        LinearLayout check_con;
        FrameLayout more_con;
        CheckBox check;
    }
}
