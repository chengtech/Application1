package com.chengtech.chengtechmt.adapter.business;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chengtech.chengtechmt.R;
import com.chengtech.chengtechmt.activity.business.TaskAcceptanceActivity;
import com.chengtech.chengtechmt.activity.business.TaskRegistActivity;
import com.chengtech.chengtechmt.entity.MaintainTaskItem;
import com.chengtech.chengtechmt.impl.OnItemClickListener;
import com.chengtech.chengtechmt.util.FormatUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者: LiuFuYingWang on 2016/12/27 9:20.
 * 小修作业，保养作业中详细信息展示adapter
 */

public class MaintenanceTaskItemAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<MaintainTaskItem> data;
    private Map<String, String> typeMap;
    private boolean isShowFootView = false;

    public MaintenanceTaskItemAdapter(List<MaintainTaskItem> data) {
        typeMap = new HashMap<>();
        typeMap.put("4046e26f5692acd4015692c1e779004f", "路面");
        typeMap.put("4046e26f5692acd4015692c43829006c", "绿化");
        typeMap.put("4081a8e558c270740158c3d9241109e4", "桥梁");
        typeMap.put("4081a8e558c270740158c3baa72008e8", "路缘带及人行道");
        typeMap.put("4081a8e558c270740158c3cd1c73092f", "标志和标线等");
        typeMap.put("4081a8e558c270740158c3e088320ac5", "隧道");
        typeMap.put("4081a8e558c270740158c36145ee067e", "排水设施	");
        typeMap.put("4081a8e558c270740158c372b209080d", "路肩及边坡");
        typeMap.put("4081a8e558c270740158c37869e50893", "挡土墙及防撞设施");
        typeMap.put("4081a8e558c270740158c3de59050a85", "涵洞");
        typeMap.put("4028835f58d333760158d3487f08010e", "其他");
        this.data = data;
    }

    public MaintenanceTaskItemAdapter(List<MaintainTaskItem> data, boolean isShowFootView) {
        typeMap = new HashMap<>();
        typeMap.put("4046e26f5692acd4015692c1e779004f", "路面");
        typeMap.put("4046e26f5692acd4015692c43829006c", "绿化");
        typeMap.put("4081a8e558c270740158c3d9241109e4", "桥梁");
        typeMap.put("4081a8e558c270740158c3baa72008e8", "路缘带及人行道");
        typeMap.put("4081a8e558c270740158c3cd1c73092f", "标志和标线等");
        typeMap.put("4081a8e558c270740158c3e088320ac5", "隧道");
        typeMap.put("4081a8e558c270740158c36145ee067e", "排水设施	");
        typeMap.put("4081a8e558c270740158c372b209080d", "路肩及边坡");
        typeMap.put("4081a8e558c270740158c37869e50893", "挡土墙及防撞设施");
        typeMap.put("4081a8e558c270740158c3de59050a85", "涵洞");
        typeMap.put("4028835f58d333760158d3487f08010e", "其他");
        this.data = data;
        this.isShowFootView = isShowFootView;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null)
            mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.main_task_item_adapter, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final MaintainTaskItem maintainTaskItem = data.get(position);
        MyViewHolder viewHolder = (MyViewHolder) holder;
        if (maintainTaskItem.registOrAcceptanceStatus == null) {
            viewHolder.registOrAcceptanceStatus.setText("");
        } else {
            switch (maintainTaskItem.registOrAcceptanceStatus) {
                case "0":
                    viewHolder.registOrAcceptanceStatus.setText("未实施");
                    viewHolder.registOrAcceptanceStatus.setTextColor(Color.BLACK);
                    break;
                case "1":
                    viewHolder.registOrAcceptanceStatus.setText("已核定");
                    viewHolder.registOrAcceptanceStatus.setTextColor(Color.RED);
                    break;
                case "2":
                    viewHolder.registOrAcceptanceStatus.setText("已完成");
                    viewHolder.registOrAcceptanceStatus.setTextColor(Color.GREEN);
                    break;
                case "3":
                    viewHolder.registOrAcceptanceStatus.setText("未实施");
                    break;
            }
        }
        viewHolder.workTeam.setText(maintainTaskItem.workTeam == null ? "" : maintainTaskItem.workTeam);
        viewHolder.mobileRouteNames.setText(maintainTaskItem.mobileRouteNames == null ? "" : maintainTaskItem.mobileRouteNames);
        viewHolder.routePeg.setText(maintainTaskItem.routePeg == null ? "" : maintainTaskItem.routePeg);
        viewHolder.taskType.setText(typeMap.get(maintainTaskItem.taskType) == null ? "" : typeMap.get(maintainTaskItem.taskType));
        viewHolder.workContent.setText(maintainTaskItem.workContent == null ? "" : maintainTaskItem.workContent);
        viewHolder.plannedTaskQuantity.setText(maintainTaskItem.plannedTaskQuantity == null ? "" : FormatUtil.double2String(maintainTaskItem.plannedTaskQuantity));
        viewHolder.plannedUnit.setText(maintainTaskItem.plannedUnit == null ? "" : maintainTaskItem.plannedUnit);
        viewHolder.plannedTaskDay.setText(maintainTaskItem.plannedTaskDay == null ? "" : FormatUtil.double2String(maintainTaskItem.plannedTaskDay));
        viewHolder.actualTaskQuantity.setText(maintainTaskItem.actualTaskQuantity == null ? "" : FormatUtil.double2String(maintainTaskItem.actualTaskQuantity));
        viewHolder.actualUnit.setText(maintainTaskItem.actualUnit == null ? "" : maintainTaskItem.actualUnit);
        viewHolder.actualTaskDay.setText(maintainTaskItem.actualTaskDay == null ? "" : FormatUtil.double2String(maintainTaskItem.actualTaskDay));
        viewHolder.memo.setText(maintainTaskItem.memo == null ? "" : maintainTaskItem.memo);
        if (isShowFootView) {
            viewHolder.footView.setVisibility(View.VISIBLE);
            viewHolder.taskRecord.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TaskRegistActivity.startAction(mContext, maintainTaskItem.id, maintainTaskItem.inOutPlanType);
                }
            });
            viewHolder.taskAcceptance.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TaskAcceptanceActivity.startAction(mContext, maintainTaskItem.id, maintainTaskItem.inOutPlanType);
                }
            });
        } else {
            viewHolder.footView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }


    private class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView registOrAcceptanceStatus, workTeam, mobileRouteNames, routePeg,
                taskType, workContent, plannedTaskQuantity, plannedUnit, plannedTaskDay,
                actualTaskQuantity, actualUnit, actualTaskDay, memo;
        private LinearLayout taskRecord, taskAcceptance, footView;

        public MyViewHolder(View itemView) {
            super(itemView);
            registOrAcceptanceStatus = (TextView) itemView.findViewById(R.id.tv1);
            workTeam = (TextView) itemView.findViewById(R.id.tv2);
            mobileRouteNames = (TextView) itemView.findViewById(R.id.tv3);
            routePeg = (TextView) itemView.findViewById(R.id.tv4);
            taskType = (TextView) itemView.findViewById(R.id.tv5);
            workContent = (TextView) itemView.findViewById(R.id.tv6);
            plannedTaskQuantity = (TextView) itemView.findViewById(R.id.tv7);
            plannedUnit = (TextView) itemView.findViewById(R.id.tv8);
            plannedTaskDay = (TextView) itemView.findViewById(R.id.tv9);
            actualTaskQuantity = (TextView) itemView.findViewById(R.id.tv10);
            actualUnit = (TextView) itemView.findViewById(R.id.tv11);
            actualTaskDay = (TextView) itemView.findViewById(R.id.tv12);
            memo = (TextView) itemView.findViewById(R.id.tv13);
            taskRecord = (LinearLayout) itemView.findViewById(R.id.taskRegisrt);
            taskAcceptance = (LinearLayout) itemView.findViewById(R.id.taskAcceptance);
            footView = (LinearLayout) itemView.findViewById(R.id.footView);
        }
    }
}
