package com.chengtech.chengtechmt.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.chengtech.chengtechmt.R;
import com.chengtech.chengtechmt.util.ViewHolder;

import java.util.List;
import java.util.Map;

/**
 * 作者: LiuFuYingWang on 2017/1/5 15:01.
 */

public class AboutMeExpandAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private String[] groupName;
    private Map<Integer, List<String>> titleData;
    private Map<Integer, List<String>> contentData;

    public AboutMeExpandAdapter(Context context, String[] groupName, Map<Integer, List<String>> titleData,
                                Map<Integer, List<String>> contentData) {
        this.mContext = context;
        this.groupName = groupName;
        this.titleData = titleData;
        this.contentData = contentData;
    }

    @Override
    public int getGroupCount() {
        return groupName == null ? 0 : groupName.length;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return titleData.get(groupPosition) == null ? 0 : titleData.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupName[groupPosition];
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return contentData.get(groupPosition) == null ? "" : contentData.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupName[groupPosition].hashCode();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return contentData.get(groupPosition) == null ? 0 : contentData.get(groupPosition).get(childPosition).hashCode();
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(mContext).inflate(R.layout.about_me_group, null, false);

        TextView group_tv = ViewHolder.get(convertView, R.id.group_title);
        if (groupName[groupPosition] != null) {
            group_tv.setText(groupName[groupPosition]);
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(mContext).inflate(R.layout.about_me_child, parent, false);

        TextView title = ViewHolder.get(convertView, R.id.subtitle);
        TextView content = ViewHolder.get(convertView, R.id.content);
        if (titleData.get(groupPosition) != null) {
            title.setText(titleData.get(groupPosition).get(childPosition));
        }
        if (contentData.get(groupPosition) != null) {
            content.setText(contentData.get(groupPosition).get(childPosition));
        }
        return convertView;
    }


    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
