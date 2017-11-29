package com.chengtech.chengtechmt.adapter.sidemonitor;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chengtech.chengtechmt.R;
import com.chengtech.chengtechmt.entity.monitoremergency.Lvdtdata;
import com.chengtech.chengtechmt.entity.monitoremergency.TempHumiData;
import com.chengtech.chengtechmt.util.DateUtils;

import java.util.List;

/**
 * 作者: LiuFuYingWang on 2017/11/28 9:55.
 * 温湿度adapter
 */

public class TempHumiDataAdapter extends RecyclerView.Adapter {
    private List<TempHumiData> data;

    public TempHumiDataAdapter(List<TempHumiData> data) {
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_temphumidata, parent, false);
        TempHumiDataAdapter.MyViewHolder viewHolder = new TempHumiDataAdapter.MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        TempHumiDataAdapter.MyViewHolder viewHolder = (TempHumiDataAdapter.MyViewHolder) holder;
        if (position == 0) {
            viewHolder.tv1.setText("模块号");
            viewHolder.tv2.setText("通道号");
            viewHolder.tv3.setText("温度值");
            viewHolder.tv4.setText("湿度值");
            viewHolder.tv5.setText("采集时间");
        } else {
            TempHumiData tempHumiData = data.get(position - 1);
            viewHolder.tv1.setText(tempHumiData.moduleNo);
            viewHolder.tv2.setText(tempHumiData.channelId);
            viewHolder.tv3.setText(String.valueOf(tempHumiData.temperatureValue));
            viewHolder.tv4.setText(String.valueOf(tempHumiData.humilityValue));
            viewHolder.tv5.setText(DateUtils.convertDate3(tempHumiData.acquisitionDatetime));
        }
    }

    @Override
    public int getItemCount() {
        return data.size() + 1;
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv1, tv2, tv3, tv4, tv5;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv1 = (TextView) itemView.findViewById(R.id.tv1);
            tv2 = (TextView) itemView.findViewById(R.id.tv2);
            tv3 = (TextView) itemView.findViewById(R.id.tv3);
            tv4 = (TextView) itemView.findViewById(R.id.tv4);
            tv5 = (TextView) itemView.findViewById(R.id.tv5);
        }
    }
}
