package com.chengtech.chengtechmt.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.chengtech.chengtechmt.R;
import com.chengtech.chengtechmt.entity.attachment.AttachmentInfo;
import com.chengtech.chengtechmt.util.ViewHolder;

import java.util.List;
import java.util.Map;

import io.realm.RealmResults;

/**
 * 作者: LiuFuYingWang on 2016/7/8 9:30.
 */
public class AttachementInfoAdapter extends BaseExpandableListAdapter {

    public String[] groupName;
    public Map<String,RealmResults<AttachmentInfo>> data;
    public LayoutInflater inflater;


    public AttachementInfoAdapter(Context context, String[] groupName, Map<String,RealmResults<AttachmentInfo>> data) {
        this.data = data;
        this.groupName = groupName;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getGroupCount() {
        return groupName == null ? 0 : groupName.length;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return data.get(groupName[groupPosition]) == null ? 0 : data.get(groupName[groupPosition]).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupName[groupPosition];
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return data.get(groupName[groupPosition]).get(childPosition);
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
            convertView = inflater.inflate(R.layout.listview_group2, parent, false);
        }
        TextView tv = ViewHolder.get(convertView, R.id.group_tv);
        tv.setText(groupName[groupPosition]);
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_attachement_info, parent, false);
        }
        ImageView img = ViewHolder.get(convertView,R.id.img);
        TextView fileName = ViewHolder.get(convertView, R.id.fileName);
        TextView size = ViewHolder.get(convertView, R.id.size);
        TextView time = ViewHolder.get(convertView, R.id.time);
        RealmResults<AttachmentInfo> results = data.get(groupName[groupPosition]);
        AttachmentInfo attachmentInfo = results.get(childPosition);
        img.setBackgroundResource(R.mipmap.attachment);
        fileName.setText(attachmentInfo.getFileName());
        size.setText(attachmentInfo.getSize());
        time.setText(attachmentInfo.getTime());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
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
