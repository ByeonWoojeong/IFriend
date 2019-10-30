package com.woojeong.global.ifriend;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import com.woojeong.global.ifriend.DTO.SearchName;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class SearchNameListAdapter extends BaseAdapter {
    Context context;
    int layout;
    ArrayList<SearchName> data;
    ListView listView;
    AQuery aQuery = null;
    SearchNameListAdapter searchNameListAdapter = this;

    public SearchNameListAdapter(Context context, int layout, ArrayList<SearchName> data, ListView listView) {
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
            vh.name = convertView.findViewById(R.id.name);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        final SearchName item = data.get(position);
        vh.name.setText(item.getName());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AboutSitterActivity.class);
                intent.putExtra("member", item.getMember());
                context.startActivity(intent);
//                ((SearchNameActivity)context).finish();
            }
        });
        return convertView;
    }

    class ViewHolder{
        TextView name;
    }
}
