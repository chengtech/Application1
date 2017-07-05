package com.chengtech.chengtechmt.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chengtech.chengtechmt.R;
import com.chengtech.chengtechmt.entity.bridge.Bridge;
import com.chengtech.chengtechmt.util.MyConstants;
import com.chengtech.chengtechmt.util.ViewHolder;

public class MyExpandableAdapter implements ExpandableListAdapter {
	private Context context;
	private String [] groupName;
	private Map<String,List<String>> childMap1;
	private Map<String,List<String>> childMap2;
	private int groupLayout = -1;
	private int childLayout = -1;
	private int flag;
	public OnChildItemClickListener listener;

	public MyExpandableAdapter(Context context,String [] groupName,Map<String,List<String>> childMap1) {
		this.context = context;
		this.groupName = groupName;
		this.childMap1 = childMap1;
		this.flag = 1;
	
	}

	public MyExpandableAdapter(Context context,String[] groupName,Map<String,List<String>> childMap1,
			Map<String,List<String>> childMap2,int groupLayout,int childLayout) {
		this.context = context;
		this.groupName = groupName;
		this.childMap1 = childMap1;
		this.childMap2 = childMap2;
		this.groupLayout = groupLayout;
		this.childLayout = childLayout;
		this.flag = 2;
	}
	@Override
	public boolean areAllItemsEnabled() {
		return true;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		if (childMap1.get(groupName[groupPosition])!=null) {
			return childMap1.get(groupName[groupPosition]).get(childPosition);
			
		}
		return null;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getChildView(final int groupPosition, final int childPosition,
							 boolean isLastChild, View convertView, ViewGroup parent) {
		if (childMap1.get(groupName[groupPosition])!=null && childMap1.get(groupName[groupPosition]).size()>0) {
			if (flag==1) {
				if (convertView==null) {
					convertView = LayoutInflater.from(context).inflate(R.layout.listview_child, null);
				}
				TextView tv = ViewHolder.get(convertView, R.id.child_tv);
				ImageView imageview = ViewHolder.get(convertView,R.id.image);
				String title = childMap1.get(groupName[groupPosition]).get(childPosition);
				tv.setText(title);
				if (MyConstants.imageDict.containsKey(title))
					imageview.setBackgroundResource(MyConstants.imageDict.get(title));
				return convertView;
			}else if (flag==2) {
				if (convertView==null) {
					convertView = LayoutInflater.from(context).inflate(childLayout, null,false);
				}
				
				TextView tv1 = ViewHolder.get(convertView, R.id.tv1);
				EditText et2 = ViewHolder.get(convertView, R.id.et1);
				Button button = ViewHolder.get(convertView,R.id.detail_bt);
				if (groupPosition==3) {
					tv1.setText("检查年月");
					et2.setVisibility(View.VISIBLE);
					button.setVisibility(View.VISIBLE);
					if (listener!=null) {
						button.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								listener.onChildItemClick(v,groupPosition,childPosition);
							}
						});
					}
				}
				else if (groupPosition==4) {
					tv1.setText(childMap1.get(groupName[groupPosition]).get(childPosition));
					et2.setVisibility(View.GONE);
					button.setVisibility(View.GONE);
				}
				else{
					String tvContent = childMap2.get(groupName[groupPosition]).get(childPosition);
					tv1.setText(tvContent==null?"":tvContent);
					et2.setVisibility(View.VISIBLE);
					button.setVisibility(View.GONE);
				}

				String etContent = childMap1.get(groupName[groupPosition]).get(childPosition);
				if (Bridge.FlAG.equals(etContent)) {
					et2.setText("查看详情");
					et2.setBackgroundResource(R.drawable.login_btn_selector);
					if (listener!=null) {
						et2.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								listener.onChildItemClick(v,groupPosition,childPosition);

							}
						});
					}
				}else {
					et2.setText(etContent==null?"":etContent);
					et2.setBackgroundResource(R.drawable.noticedetail_time_shape);
					et2.setOnClickListener(null);

				}
				return convertView;
			}
			
		}
		return null;
		
	}
	

	@Override
	public int getChildrenCount(int groupPosition) {
		if (childMap1.get(groupName[groupPosition])!=null) {
			return childMap1.get(groupName[groupPosition]).size();
		}
		return 0;
		
	}

	
	

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return groupName[groupPosition];
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return groupName.length;
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		if (groupName[groupPosition]!=null) {
			if (flag==1) {
				if (convertView==null) {
					convertView = LayoutInflater.from(context).inflate(R.layout.listview_group, null);
				}
				TextView tv = ViewHolder.get(convertView, R.id.group_tv);
				tv.setText(groupName[groupPosition]);
				ImageView imageview = ViewHolder.get(convertView,R.id.image);
				if (MyConstants.imageDict.containsKey(groupName[groupPosition]))
					imageview.setBackgroundResource(MyConstants.imageDict.get(groupName[groupPosition]));
				return convertView;
			}else if (flag==2) {
				if (convertView==null) {
					convertView = LayoutInflater.from(context).inflate(groupLayout, null,false);
				}
				TextView tv = ViewHolder.get(convertView, R.id.group_tv);
				tv.setText(groupName[groupPosition]);
				return convertView;
			}
			
		}
		return null;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onGroupCollapsed(int groupPosition) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGroupExpanded(int groupPosition) {
		// TODO Auto-generated method stub

	}

	@Override
	public void registerDataSetObserver(DataSetObserver observer) {
		// TODO Auto-generated method stub

	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
		// TODO Auto-generated method stub

	}

	@Override
	public long getCombinedChildId(long groupId, long childId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getCombinedGroupId(long groupId) {
		// TODO Auto-generated method stub
		return 0;
	}

	public void setOnChildItemClickListener(OnChildItemClickListener listener){
		this.listener = listener;
	}
	public interface OnChildItemClickListener {
		public void onChildItemClick(View view,int groupPosition,int childPosition);
	}
}
