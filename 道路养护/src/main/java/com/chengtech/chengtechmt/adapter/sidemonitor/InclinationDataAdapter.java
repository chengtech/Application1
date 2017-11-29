package com.chengtech.chengtechmt.adapter.sidemonitor;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chengtech.chengtechmt.R;
import com.chengtech.chengtechmt.entity.monitoremergency.Inclinationdata;
import com.chengtech.chengtechmt.entity.monitoremergency.VibratingWireData;
import com.chengtech.chengtechmt.util.DateUtils;

import java.util.List;

/**
 * 作者: LiuFuYingWang on 2017/11/27 16:32.
 * 内部位移监测adapter
 */

public class InclinationDataAdapter extends RecyclerView.Adapter {
    private List<Inclinationdata> data;


    public InclinationDataAdapter(List<Inclinationdata> data) {
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_inclinationdata, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder viewHolder = (MyViewHolder) holder;
        if (position == 0) {
            viewHolder.tv1.setText("模块号");
            viewHolder.tv2.setText("通道号");
            viewHolder.tv3.setText("x方向角度值");
            viewHolder.tv4.setText("y方向角度值");
            viewHolder.tv5.setText("计算后x方向偏移量");
            viewHolder.tv6.setText("计算后y方向偏移量");
            viewHolder.tv7.setText("采集时间");
        } else {
            Inclinationdata inclinationdata = data.get(position - 1);
            viewHolder.tv1.setText(inclinationdata.moduleNo);
            viewHolder.tv2.setText(inclinationdata.channelId);
            viewHolder.tv3.setText(String.valueOf(inclinationdata.angleOriginalX));
            viewHolder.tv4.setText(String.valueOf(inclinationdata.angleOriginalY));
            viewHolder.tv5.setText(String.valueOf(inclinationdata.angleOffsetX));
            viewHolder.tv6.setText(String.valueOf(inclinationdata.angleOffsetY));
            viewHolder.tv7.setText(DateUtils.convertDate3(inclinationdata.acquisitionDatetime));
        }

    }

    @Override
    public int getItemCount() {
        return data.size() + 1;
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv1, tv2, tv3, tv4, tv5, tv6, tv7;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv1 = (TextView) itemView.findViewById(R.id.tv1);
            tv2 = (TextView) itemView.findViewById(R.id.tv2);
            tv3 = (TextView) itemView.findViewById(R.id.tv3);
            tv4 = (TextView) itemView.findViewById(R.id.tv4);
            tv5 = (TextView) itemView.findViewById(R.id.tv5);
            tv6 = (TextView) itemView.findViewById(R.id.tv6);
            tv7 = (TextView) itemView.findViewById(R.id.tv7);
        }
    }
}
