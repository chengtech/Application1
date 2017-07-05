package com.chengtech.chengtechmt.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chengtech.chengtechmt.R;

import java.util.List;
import java.util.Map;

public class MyExpandableAdapter2 implements ExpandableListAdapter {
	private Context context;
	private List<String> groupName;
	Map<String,List<String>> groupList;
	
	public MyExpandableAdapter2() {
		super();
	}
	
	public MyExpandableAdapter2(Context context,List<String> groupName,Map<String,List<String>> groupList) {
		this.context = context;
		this.groupName = groupName;
		this.groupList = groupList;
	
	}

	@Override
	public boolean areAllItemsEnabled() {
		return true;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		if (groupList.get(groupName.get(groupPosition))!=null) {
			return groupList.get(groupName.get(groupPosition)).get(childPosition);
			
		}
		return null;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return 0;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		if (groupList.get(groupName.get(groupPosition))!=null) {
			ViewHolder viewHolder=null;
			if (convertView==null) {
				viewHolder = new ViewHolder();
				convertView = LayoutInflater.from(context).inflate(R.layout.listview_child, null);
				viewHolder.tv = (TextView) convertView.findViewById(R.id.child_tv); 
				convertView.setTag(viewHolder);
			}else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			//int key = groupList.get(groupName[groupPosition]).get(childPosition).indexOf("=");
			viewHolder.tv.setText(groupList.get(groupName.get(groupPosition)).get(childPosition));
			return convertView;
		}
		return null;
		
	}
	
	class ViewHolder {
		TextView tv;
		ImageView iv;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		if (groupList.get(groupName.get(groupPosition))!=null) {
			return groupList.get(groupName.get(groupPosition)).size();
		}
		return 0;
		
	}

	
	

	@Override
	public Object getGroup(int groupPosition) {
		return groupName.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return groupName.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		// TODO: 2016/1/25
		ViewHolder viewHolder=null;
		if (convertView==null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.listview_group2, null);
			viewHolder.tv = (TextView) convertView.findViewById(R.id.group_tv); 
			viewHolder.iv = (ImageView) convertView.findViewById(R.id.group_right_iv);
			convertView.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		String groupN = groupName.get(groupPosition);
		SpannableStringBuilder builder = new SpannableStringBuilder(groupN);
		builder.setSpan(new ForegroundColorSpan(Color.parseColor("#EE7600") ), 0, 7, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
		builder.setSpan(new AbsoluteSizeSpan(16,true), 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		
		viewHolder.tv.setText(builder);
		if (isExpanded) {
			viewHolder.iv.setImageResource(R.mipmap.chevron_default_down);
		}else {
			viewHolder.iv.setImageResource(R.mipmap.chevron_default);
		}
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public void onGroupCollapsed(int groupPosition) {

	}

	@Override
	public void onGroupExpanded(int groupPosition) {

	}

	@Override
	public void registerDataSetObserver(DataSetObserver observer) {

	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {

	}

	@Override
	public long getCombinedChildId(long groupId, long childId) {
		return 0;
	}

	@Override
	public long getCombinedGroupId(long groupId) {
		return 0;
	}
}
