package com.chengtech.chengtechmt.adapter.gis;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chengtech.chengtechmt.R;
import com.chengtech.chengtechmt.entity.gis.ProjectAndMsg;
import com.chengtech.chengtechmt.entity.gis.ProjectAndMsgItem;
import com.chengtech.chengtechmt.util.ViewHolder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者: LiuFuYingWang on 2018/1/10 9:30.
 */
public class ProjectAndMSgExpandableAdapter extends BaseExpandableListAdapter {

    public LayoutInflater inflater;
    public List<ProjectAndMsg> data;
    public Map<String, String> dict;

    public ProjectAndMSgExpandableAdapter(Context context, List<ProjectAndMsg> data) {
        this.data = data;
        inflater = LayoutInflater.from(context);
        dict = new HashMap<>();
        dict.put("0", "实施中");
        dict.put("1", "已完成");
        dict.put("ProjectManagement", "大中修、改造（善）及省部补助项目");
        dict.put("MediumPlanprogressItem", "小额专项维修项目");
    }

    @Override
    public int getGroupCount() {
        return data.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return data.get(groupPosition).projectAndMsgItem==null?0:data.get(groupPosition).projectAndMsgItem.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return data.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return data.get(groupPosition).projectAndMsgItem.get(childPosition);
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
        ProjectAndMsg projectAndMsg = data.get(groupPosition);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_projectandmsg_group, parent, false);
        }
        RelativeLayout relativeLayout = ViewHolder.get(convertView, R.id.parent);
        if (projectAndMsg.implementSituation.equals("0")) {
            relativeLayout.setBackgroundColor(Color.parseColor("#F4E7D3"));
        }else {
            relativeLayout.setBackgroundColor(Color.parseColor("#E1E1E1"));
        }
        TextView tv1 = ViewHolder.get(convertView, R.id.tv1);
        TextView tv2 = ViewHolder.get(convertView, R.id.tv2);
        TextView tv3 = ViewHolder.get(convertView, R.id.tv3);
        TextView tv4 = ViewHolder.get(convertView, R.id.tv4);
        TextView tv5 = ViewHolder.get(convertView, R.id.tv5);
        TextView tv6 = ViewHolder.get(convertView, R.id.tv6);
        tv1.setText("实施情况:" + dict.get(projectAndMsg.implementSituation));
        tv2.setText("项目数量(个):" + projectAndMsg.projectNum);
        tv3.setText("项目类型:" + dict.get(projectAndMsg.projectType));
        tv4.setText("总投资情况(千万):" + projectAndMsg.totalInvestment);
        tv5.setText("支出情况(千万):" + projectAndMsg.totalPaidFund);
        tv6.setText("总建设规模(公里):" + projectAndMsg.subtotal);
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (data.get(groupPosition).projectAndMsgItem == null)
            return null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_projectandmsg_child, parent, false);
        }

        ProjectAndMsgItem projectAndMsgItem = data.get(groupPosition).projectAndMsgItem.get(childPosition);
        TextView tv1 = ViewHolder.get(convertView, R.id.tv1);
        TextView tv2 = ViewHolder.get(convertView, R.id.tv2);
        TextView tv3 = ViewHolder.get(convertView, R.id.tv3);
        TextView tv4 = ViewHolder.get(convertView, R.id.tv4);
        TextView tv5 = ViewHolder.get(convertView, R.id.tv5);
        TextView tv6 = ViewHolder.get(convertView, R.id.tv6);
        TextView tv7 = ViewHolder.get(convertView, R.id.tv7);
        TextView tv8 = ViewHolder.get(convertView, R.id.tv8);

        tv1.setText("路线编号：" + projectAndMsgItem.routeCode);
        tv2.setText("开始桩号：" + projectAndMsgItem.startPeg);
        tv3.setText("结束桩号：" + projectAndMsgItem.endPeg);
        tv4.setText("项目名称：" + projectAndMsgItem.projectName);
        tv5.setText("建设单位：" + projectAndMsgItem.buildUnit);
        tv6.setText("投资金额（千万）：" + String.valueOf(projectAndMsgItem.totalInvestment));
        tv7.setText("支出金额（千万）：" + String.valueOf(projectAndMsgItem.totalPaidFund));
        tv8.setText("建设规模（公里）：" + String.valueOf(projectAndMsgItem.subtotal));

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
