package com.chengtech.chengtechmt.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.TextView;

import com.chengtech.chengtechmt.R;
import com.chengtech.chengtechmt.util.ViewHolder;

import java.util.List;
import java.util.Map;

/**
 * 作者: LiuFuYingWang on 2016/7/8 9:30.
 */
public class MyExpandableAdapter3 extends BaseExpandableListAdapter {

    public String[] groupName;
    public Map<String, List<List<String>>> childData;
    public int group_layout;
    public int child_layout;
    public LayoutInflater inflater;
    public OnItemClickListener listener;


    public interface OnItemClickListener {
        public void onClick(View view, int groupPosition, int childPosition);
    }

    public void setOnItemClickListenr(OnItemClickListener listener) {
        this.listener = listener;
    }

    public MyExpandableAdapter3(Context context, String[] groupName, Map<String, List<List<String>>> childData, int group_layout, int child_layout) {
        this.groupName = groupName;
        this.childData = childData;
        this.group_layout = group_layout;
        this.child_layout = child_layout;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getGroupCount() {
        return groupName == null ? 0 : groupName.length;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return childData.get(groupName[groupPosition]) == null ? 0 : childData.get(groupName[groupPosition]).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupName[groupPosition];
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childData.get(groupName[groupPosition]).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(group_layout, parent, false);
        }
        TextView tv = ViewHolder.get(convertView, R.id.group_title);
        tv.setText(groupName[groupPosition]);
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(child_layout, parent, false);
        }

//            TextView tv1 = ViewHolder.get(convertView,R.id.tv1);
//            TextView tv2 = ViewHolder.get(convertView,R.id.tv2);
//            TextView tv3 = ViewHolder.get(convertView,R.id.tv3);
//            TextView tv4 = ViewHolder.get(convertView,R.id.tv4);
//            TextView tv5 = ViewHolder.get(convertView,R.id.tv5);
//
//        tv1.setText(childData.get(groupName[groupPosition]).get(childPosition).get(0));
//        tv2.setText(childData.get(groupName[groupPosition]).get(childPosition).get(1));
//        tv3.setText(childData.get(groupName[groupPosition]).get(childPosition).get(2));
//        tv4.setText(childData.get(groupName[groupPosition]).get(childPosition).get(3));
//        tv5.setText(childData.get(groupName[groupPosition]).get(childPosition).get(4));

        TextView tv1 = ViewHolder.get(convertView, R.id.tv1);
        EditText et1 = ViewHolder.get(convertView, R.id.et1);
        Button button = ViewHolder.get(convertView, R.id.detail_bt);
        tv1.setVisibility(View.GONE);
        button.setVisibility(View.VISIBLE);
        et1.setText(childData.get(groupName[groupPosition]).get(childPosition).get(0));

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(v, groupPosition, childPosition);
                }
            }
        });
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void onGroupExpanded(int groupPosition) {

    }

    @Override
    public void onGroupCollapsed(int groupPosition) {

    }

    @Override
    public long getCombinedChildId(long groupId, long childId) {
        return 0;
    }

    @Override
    public long getCombinedGroupId(long groupId) {
        return 0;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }
}
