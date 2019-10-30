package com.woojeong.global.ifriend;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.woojeong.global.ifriend.DTO.Career;
import com.woojeong.global.ifriend.DTO.ChatList;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class ChatListAdapter extends BaseAdapter {
    Context context;
    int layout;
    ArrayList<ChatList> data;
    ListView listView;
    AQuery aQuery = null;
    ChatListAdapter adapter = this;
    TwoBtnDialog twoBtnDialog;

    public ChatListAdapter(Context context, int layout, ArrayList<ChatList> data, ListView listView) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        aQuery = new AQuery(context);

        if (convertView == null) {
            convertView = View.inflate(context, layout, null);
            vh = new ViewHolder();
            vh.name = convertView.findViewById(R.id.name);
            vh.text = convertView.findViewById(R.id.text);
            vh.time = convertView.findViewById(R.id.time);
            vh.photo_con = convertView.findViewById(R.id.photo_con);
            vh.photo = convertView.findViewById(R.id.photo);
            vh.badge = convertView.findViewById(R.id.badge);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        final ChatList item = data.get(position);

       final ViewHolder finalVh = vh;

        circleImage(vh.photo, ServerUrl.getBaseUrl() + "/uploads/images/origin/" + item.getProfile());

        vh.name.setText(item.getName());
        vh.text.setText(item.getContent());
        vh.time.setText(item.getDate());

        if ("1".equals(item.getBadge())) {
            vh.badge.setVisibility(View.VISIBLE);
        } else if ("0".equals(item.getBadge()) || null == item.getBadge()) {
            vh.badge.setVisibility(View.INVISIBLE);
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatDetailsActivity.class);
                intent.putExtra("member", item.getIdx());
                intent.putExtra("name", item.getName());
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    class ViewHolder{
        TextView name, text, time;
        FrameLayout photo_con;
        ImageView photo, badge;
    }

    private void circleImage(ImageView imageView, String getImg) {
        Glide.with(context)
                .load(getImg)
                .apply(RequestOptions.bitmapTransform(new CropCircleTransformation()))
                .into(imageView);
    }

    public class TwoBtnDialog extends Dialog {
        TwoBtnDialog twoBtnDialog = this;
        Context context;
        public TwoBtnDialog(final Context context, final String text, final Career item) {
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
            title1.setText(text);
            btn1.setText("취소");
            btn2.setText("삭제");
            btn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    twoBtnDialog.dismiss();
                }
            });
            btn2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    data.remove(item);
                    adapter.notifyDataSetChanged();
                    twoBtnDialog.dismiss();

                }
            });
        }
    }
}
