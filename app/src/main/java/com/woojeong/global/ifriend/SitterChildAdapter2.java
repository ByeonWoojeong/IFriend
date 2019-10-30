package com.woojeong.global.ifriend;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.woojeong.global.ifriend.DTO.Three;
import com.woojeong.global.ifriend.DTO.Two;

import java.util.ArrayList;

public class SitterChildAdapter2 extends BaseAdapter {
    Context context;
    int layout;
    ArrayList<Two> data;
    ListView listView;
    AQuery aQuery = null;
    SitterChildAdapter2 sitterChildAdapter = this;
    String type;

    public SitterChildAdapter2(Context context, int layout, ArrayList<Two> data, ListView listView, String type) {
        this.context = context;
        this.layout = layout;
        this.data = data;
        this.listView = listView;
        this.type = type;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        aQuery = new AQuery(context);

        if (convertView == null) {
            convertView = View.inflate(context, layout, null);
            vh = new ViewHolder();
            vh.gender_icon = convertView.findViewById(R.id.gender_icon);
            vh.age = convertView.findViewById(R.id.age);
            vh.gender = convertView.findViewById(R.id.gender);
            vh.etc = convertView.findViewById(R.id.etc);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        final Two item = data.get(position);

        if("1".equals(type)){
            vh.age.setText(item.getText1() + "살");
        } else if("2".equals(type)){
            vh.age.setText(item.getText1() + "년생");
        }

        if("1".equals(item.getText2())){
            vh.gender_icon.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.boy));
            vh.gender.setText("남아");
        } else if ("2".equals(item.getText2())){
            vh.gender_icon.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.girl));
            vh.gender.setText("여아");
        }

        return convertView;
    }

    class ViewHolder{
        ImageView gender_icon;
        TextView age, gender, etc;
    }
}
