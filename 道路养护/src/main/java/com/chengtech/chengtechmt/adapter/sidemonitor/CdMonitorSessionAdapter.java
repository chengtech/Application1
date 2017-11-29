package com.chengtech.chengtechmt.adapter.sidemonitor;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chengtech.chengtechmt.R;
import com.chengtech.chengtechmt.entity.monitoremergency.CdMonitorSession;
import com.chengtech.chengtechmt.entity.monitoremergency.Lvdtdata;
import com.chengtech.chengtechmt.util.DateUtils;

import java.util.List;

/**
 * 作者: LiuFuYingWang on 2017/11/27 16:32.
 */

public class CdMonitorSessionAdapter extends RecyclerView.Adapter {
    private List<CdMonitorSession> data;


    public CdMonitorSessionAdapter(List<CdMonitorSession> data) {
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cdmonitorsession, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder viewHolder = (MyViewHolder) holder;
        if (position == 0) {
            viewHolder.tv1.setText("监测点名称");
            viewHolder.tv2.setText("基准名称");
            viewHolder.tv3.setText("X轴平面投影坐标值");
            viewHolder.tv4.setText("Y轴平面投影坐标值");
            viewHolder.tv5.setText("高程值");
            viewHolder.tv6.setText("采集时间");
        } else {
            CdMonitorSession cdMonitorSession = data.get(position - 1);
            viewHolder.tv1.setText(cdMonitorSession.mname);
            viewHolder.tv2.setText(cdMonitorSession.bname);
            viewHolder.tv3.setText(String.valueOf(cdMonitorSession.xaxisvalue));
            viewHolder.tv4.setText(String.valueOf(cdMonitorSession.yaxisvalue));
            viewHolder.tv5.setText(String.valueOf(cdMonitorSession.highvalue));
            viewHolder.tv6.setText(DateUtils.convertDate3(cdMonitorSession.acquisitionDatetime));
        }

    }

    @Override
    public int getItemCount() {
        return data.size() + 1;
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv1, tv2, tv3, tv4, tv5,tv6;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv1 = (TextView) itemView.findViewById(R.id.tv1);
            tv2 = (TextView) itemView.findViewById(R.id.tv2);
            tv3 = (TextView) itemView.findViewById(R.id.tv3);
            tv4 = (TextView) itemView.findViewById(R.id.tv4);
            tv5 = (TextView) itemView.findViewById(R.id.tv5);
            tv6 = (TextView) itemView.findViewById(R.id.tv6);
        }
    }
}
