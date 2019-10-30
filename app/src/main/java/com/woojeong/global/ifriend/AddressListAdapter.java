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
import com.woojeong.global.ifriend.DTO.SearchAddress;

import java.util.ArrayList;

public class AddressListAdapter extends BaseAdapter {

    Context context;
    int layout;
    ArrayList<SearchAddress> data;
    ListView listView;
    AQuery aQuery = null;
    AddressListAdapter addressListAdapter = this;
    SearchAddressActivity searchAddressActivity;

    public AddressListAdapter(Context context, int layout, ArrayList<SearchAddress> data, ListView listView, SearchAddressActivity searchAddressActivity) {
        this.context = context;
        this.layout = layout;
        this.data = data;
        this.listView = listView;
        this.searchAddressActivity = searchAddressActivity;
    }

    @Override
    public int getCount() {
        return  data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class ViewHolder{
        ImageView search;
        TextView title;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        aQuery = new AQuery(context);
        if (convertView == null) {
            convertView = View.inflate(context, layout, null);
            vh = new ViewHolder();
            vh.search = (ImageView) convertView.findViewById(R.id.search);
            vh.title = (TextView) convertView.findViewById(R.id.title);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        final SearchAddress item = data.get(position);
        vh.title.setText(item.getAddr());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        return convertView;
    }
}
