package com.woojeong.global.ifriend;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.woojeong.global.ifriend.DTO.FaQ;

import java.util.ArrayList;

public class HelperAdapter extends BaseExpandableListAdapter {

    Context context;
    int layout;
    int layout2;
    ArrayList<FaQ> data;
    ArrayList<ArrayList<FaQ>> data2;
    LayoutInflater inflater;
    ExpandableListView list;
    boolean[] mGroupClickState;
    String getImagePath;
    int lastExpandedPosition = -1;
    Animation child_list = null;

    public HelperAdapter(Context context, ExpandableListView list, int layout, int layout2, ArrayList<FaQ> data, ArrayList<ArrayList<FaQ>> data2) {
        this.context = context;
        this.layout = layout;
        this.layout2 = layout2;
        this.data = data;
        this.data2 = data2;
        this.list = list;
        mGroupClickState = new boolean[data.size()];
        for (int i=0; i<mGroupClickState.length; i++) {
            mGroupClickState[i] = false;
        }
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return data2.get(groupPosition).get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return data2.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return data.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return data.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    class ViewHolder{
        TextView faq_title;
        ImageView faq_arrow;
    }

    class ViewHolder2{
        TextView faq_content;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView==null) {
            convertView = View.inflate(context, layout, null);
            holder = new ViewHolder();
            holder.faq_title = (TextView) convertView.findViewById(R.id.faq_title);
            holder.faq_arrow = (ImageView) convertView.findViewById(R.id.faq_arrow);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final FaQ item = data.get(groupPosition);
        holder.faq_title.setText(item.getTitle());
        final ViewHolder finalHolder = holder;
        if (list.isGroupExpanded(groupPosition)) {
//            finalHolder.faq_arrow.setImageResource(R.drawable.qna_up);
        } else {
//            finalHolder.faq_arrow.setImageResource(R.drawable.qna_down);
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (list.isGroupExpanded(groupPosition)) {
                    list.collapseGroup(groupPosition);
                } else {
                    list.expandGroup(groupPosition);
                }
            }
        });
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolder2 holder = null;
        if (convertView == null) {
            convertView = View.inflate(context, layout2, null);
            holder = new ViewHolder2();
            holder.faq_content = (TextView) convertView.findViewById(R.id.faq_content);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder2) convertView.getTag();
        }
        final FaQ item2 = data2.get(groupPosition).get(childPosition);
        child_list = AnimationUtils.loadAnimation(context, R.anim.child_list);
        if (child_list != null) {
            convertView.startAnimation(child_list);
            child_list = null;
        }
        holder.faq_content.setText(item2.getContent());
        return convertView;
    }
    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        mGroupClickState[groupPosition] = !mGroupClickState[groupPosition];
        if (lastExpandedPosition != -1 && lastExpandedPosition != groupPosition) {
            list.collapseGroup(lastExpandedPosition);
            mGroupClickState[lastExpandedPosition] = false;
        }
        lastExpandedPosition = groupPosition;
        super.onGroupExpanded(groupPosition);
    }

}

