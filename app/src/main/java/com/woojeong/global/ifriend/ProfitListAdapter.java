package com.woojeong.global.ifriend;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.woojeong.global.ifriend.DTO.Profit;
import com.woojeong.global.ifriend.DTO.SearchName;

import java.util.ArrayList;

public class ProfitListAdapter extends BaseAdapter {
    Context context;
    int layout;
    ArrayList<Profit> data;
    ListView listView;
    AQuery aQuery = null;
    ProfitListAdapter profitListAdapter = this;

    public ProfitListAdapter(Context context, int layout, ArrayList<Profit> data, ListView listView) {
        this.context = context;
        this.layout = layout;
        this.data = data;
        this.listView = listView;
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
            vh.price = convertView.findViewById(R.id.price);
            vh.date = convertView.findViewById(R.id.date);
            vh.status = convertView.findViewById(R.id.status);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        final Profit item = data.get(position);
        vh.price.setText(item.getCost());
        vh.date.setText(item.getDay2());

        if("1".equals(item.getStatus())){
            vh.status.setBackground(context.getResources().getDrawable(R.drawable.calculate_waiting));
        } else if("2".equals(item.getStatus())){
            vh.status.setBackground(context.getResources().getDrawable(R.drawable.non_calculate));
        } else if("3".equals(item.getStatus())){
            vh.status.setBackground(context.getResources().getDrawable(R.drawable.deposit_waiting));
        } else if("4".equals(item.getStatus())){
            vh.status.setBackground(context.getResources().getDrawable(R.drawable.deposit_completed));
        }

        return convertView;
    }

    class ViewHolder{
        TextView price, date;
        ImageView status;
    }
}
