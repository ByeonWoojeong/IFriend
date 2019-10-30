package com.woojeong.global.ifriend;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.woojeong.global.ifriend.DTO.One;

import java.util.ArrayList;

public class OneItemAdapter extends BaseAdapter {
    Context context;
    int layout;
    ArrayList<One> data;
    ListView listView;
    AQuery aQuery = null;
    OneItemAdapter oneItemAdapter = this;
    String type;

    public OneItemAdapter(Context context, int layout, ArrayList<One> data, ListView listView) {
        this.context = context;
        this.layout = layout;
        this.data = data;
        this.listView = listView;
    }

    public OneItemAdapter(Context context, int layout, ArrayList<One> data, ListView listView, String type) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        aQuery = new AQuery(context);

        if (convertView == null) {
            convertView = View.inflate(context, layout, null);
            vh = new ViewHolder();
            vh.no1 = convertView.findViewById(R.id.no1);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        final One item = data.get(position);
        vh.no1.setText(item.getText1());

        if("family".equals(type)){
            if("1".equals(item.getText1())){
                vh.no1.setText("흡연자가 있어요.");
            } else  if("2".equals(item.getText1())){
                vh.no1.setText("알레르기 보유자.");
            } else  if("3".equals(item.getText1())){
                vh.no1.setText("반려 동물이 있어요.");
            } else  if("4".equals(item.getText1())){
                vh.no1.setText("혼자 살고 있어요.");
            }
        }



        return convertView;
    }

    class ViewHolder{
        TextView no1;
    }

    public void setListViewHeight(BaseAdapter adpater, ListView listView) {
        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);

        for (int size = 0; size < listView.getCount(); size++) {
            View listItem = adpater.getView(size, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listView.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

}
