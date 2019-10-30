package com.woojeong.global.ifriend.Chatting;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.bumptech.glide.Glide;
import com.woojeong.global.ifriend.InputImagePathActivity;
import com.woojeong.global.ifriend.R;

import java.util.ArrayList;
import java.util.List;


// http://chat.ifriend.epochcorp.kr
public class ChatMessageAdapter extends BaseAdapter {

    AQuery aQuery = null;
    Context context;
    int layout;
    ArrayList<ChatMessage> data;
    ListView listView;
    ChatMessageAdapter chatArrayAdapter = this;
    String midx;

//	public void setReadChecked() {
//		for (int i = 0;i < chatArrayAdapter.getCount(); i++) {
//			chatMessageList.get(i).setRead("1");
//		}
//		notifyDataSetChanged();
//	}

//    @Override
//    public void add(ChatMessage object) {
//        chatMessageList.add(object);
//        notifyDataSetChanged();
//        super.add(object);
//    }
//
//    public ChatMessageAdapter(Context context, int textViewResourceId, String midx) {
//        this.context = context;
//        this.midx = midx;
//    }

    public ChatMessageAdapter( Context context, int layout, ArrayList<ChatMessage> data, ListView listView) {
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
    public ChatMessage getItem(int index) {
        return data.get(index);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    class ViewHolder {
        LinearLayout target_con, message_con;
        TextView content, date;
        ImageView content_img;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        ViewHolder vh = null;
        aQuery = new AQuery(context);
        if (view == null) {
            vh = new ViewHolder();
            view = View.inflate(context, layout, null);
            vh.target_con = view.findViewById(R.id.target_con);
            vh.message_con = view.findViewById(R.id.message_con);
            vh.content = view.findViewById(R.id.content);
            vh.date = view.findViewById(R.id.date);
            vh.content_img = view.findViewById(R.id.content_img);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }
        final ChatMessage item = data.get(position);

        //text
        if ("1".equals(item.getType())) {
            vh.content.setVisibility(View.VISIBLE);
            vh.content.setText(item.getContent());
            vh.content_img.setVisibility(View.GONE);
        } else if ("2".equals(item.getType())) {        //image
            vh.content.setVisibility(View.GONE);
            vh.content_img.setVisibility(View.VISIBLE);
            //glide
            Glide.with(context).load("https://api.ichingu.com/uploads/images/origin/" + item.getContent()).clone().into(vh.content_img);
            vh.content_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
						Intent intent = new Intent(context, InputImagePathActivity.class);
						intent.putExtra("path", "https://api.ichingu.com/uploads/images/origin/" + item.getContent());
						context.startActivity(intent);
                }
            });
        }

        if ("me".equals(item.getTarget())) {
            vh.target_con.setGravity(Gravity.END);
            vh.message_con.setBackground(context.getResources().getDrawable(R.drawable.my_bubble));
        } else if ("other".equals(item.getTarget())) {
            vh.target_con.setGravity(Gravity.START);
            vh.message_con.setBackground(context.getResources().getDrawable(R.drawable.your_bubble));
        }

        vh.date.setText(item.getDate());

        return view;
    }
}