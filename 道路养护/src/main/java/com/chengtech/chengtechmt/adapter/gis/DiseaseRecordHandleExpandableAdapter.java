package com.chengtech.chengtechmt.adapter.gis;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chengtech.chengtechmt.R;
import com.chengtech.chengtechmt.entity.gis.DiseaseRecordHandle;
import com.chengtech.chengtechmt.entity.gis.DiseaseRecordHandleItem;
import com.chengtech.chengtechmt.entity.gis.ProjectAndMsg;
import com.chengtech.chengtechmt.entity.gis.ProjectAndMsgItem;
import com.chengtech.chengtechmt.util.ViewHolder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者: LiuFuYingWang on 2018/1/10 9:30.
 * 病害处理情况adapeter
 */
public class DiseaseRecordHandleExpandableAdapter extends BaseExpandableListAdapter {

    public LayoutInflater inflater;
    public List<DiseaseRecordHandle> data;
    public Map<String, String> dict;

    public DiseaseRecordHandleExpandableAdapter(Context context, List<DiseaseRecordHandle> data) {
        this.data = data;
        inflater = LayoutInflater.from(context);
        dict = new HashMap<>();
        dict.put("0", "未处理");
        dict.put("1", "处理中");
        dict.put("2", "已处理");
    }

    @Override
    public int getGroupCount() {
        return data.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return data.get(groupPosition).diseaseRecordHandleItem == null ? 0 : data.get(groupPosition).diseaseRecordHandleItem.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return data.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return data.get(groupPosition).diseaseRecordHandleItem.get(childPosition);
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
        DiseaseRecordHandle diseaseRecordHandle = data.get(groupPosition);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_diseasehandle_group, parent, false);
        }
        TextView tv1 = ViewHolder.get(convertView, R.id.tv1);
        TextView tv2 = ViewHolder.get(convertView, R.id.tv2);
        TextView tv3 = ViewHolder.get(convertView, R.id.tv3);
        tv1.setText(dict.get(diseaseRecordHandle.dealSituation));
        tv2.setText(diseaseRecordHandle.diseaseNum + "");
        if (!TextUtils.isEmpty(diseaseRecordHandle.proportion)) {
            try {
                float v = Float.parseFloat(diseaseRecordHandle.proportion);
                int b = (int) (v * 100);
                tv3.setText(String.valueOf(b));
            } catch (Exception e) {
                tv3.setText("-");
            }
        }else {
            tv3.setText("-");
        }
        ;
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (data.get(groupPosition).diseaseRecordHandleItem == null)
            return null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_diseasehandle_child, parent, false);
        }

        DiseaseRecordHandleItem handleItem = data.get(groupPosition).diseaseRecordHandleItem.get(childPosition);
        LinearLayout layout = ViewHolder.get(convertView, R.id.title);
        if (childPosition == 0) {
            layout.setVisibility(View.VISIBLE);
        } else {
            layout.setVisibility(View.GONE);
        }
        TextView tv1 = ViewHolder.get(convertView, R.id.tv1);
        TextView tv2 = ViewHolder.get(convertView, R.id.tv2);
        TextView tv3 = ViewHolder.get(convertView, R.id.tv3);
        TextView tv4 = ViewHolder.get(convertView, R.id.tv4);
//        TextView tv5 = ViewHolder.get(convertView, R.id.tv5);
//        TextView tv6 = ViewHolder.get(convertView, R.id.tv6);
//        TextView tv7 = ViewHolder.get(convertView, R.id.tv7);
//        TextView tv8 = ViewHolder.get(convertView, R.id.tv8);

        tv1.setText(handleItem.routeCode);
        tv2.setText(handleItem.diseaseName);
        tv3.setText(handleItem.diseaseType);
        tv4.setText(handleItem.diseaseSeverity);
//        tv5.setText("建设单位：" + projectAndMsgItem.buildUnit);
//        tv6.setText("投资金额（千万）：" + String.valueOf(projectAndMsgItem.totalInvestment));
//        tv7.setText("支出金额（千万）：" + String.valueOf(projectAndMsgItem.totalPaidFund));
//        tv8.setText("建设规模（公里）：" + String.valueOf(projectAndMsgItem.subtotal));

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
