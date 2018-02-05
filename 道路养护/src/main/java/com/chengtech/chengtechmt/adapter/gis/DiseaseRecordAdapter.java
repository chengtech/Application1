package com.chengtech.chengtechmt.adapter.gis;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.chengtech.chengtechmt.R;
import com.chengtech.chengtechmt.activity.integratequery.gis.DiseaseRecord.DiseaseRecordActivity;
import com.chengtech.chengtechmt.entity.Route;
import com.chengtech.chengtechmt.entity.gis.DiseaseRecord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * 作者: LiuFuYingWang on 2016/12/6 15:19.
 * 病害记录adapter
 */

public class DiseaseRecordAdapter extends RecyclerView.Adapter<DiseaseRecordAdapter.MyHolder> {
    private List<DiseaseRecord> data;

    public DiseaseRecordAdapter(List<DiseaseRecord> data) {
        this.data = data;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_disease_record, parent, false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyHolder holder, final int position) {
        holder.routeCode.setText(data.get(position).routeCode);
        holder.diseaseNum.setText(data.get(position).diseaseNum + "");
        holder.diseaseType.setText(data.get(position).diseaseType);


    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public class MyHolder extends RecyclerView.ViewHolder {

        public TextView routeCode, diseaseNum, diseaseType;

        public MyHolder(View itemView) {
            super(itemView);
            routeCode = (TextView) itemView.findViewById(R.id.routeCode);
            diseaseNum = (TextView) itemView.findViewById(R.id.diseaseNum);
            diseaseType = (TextView) itemView.findViewById(R.id.diseaseType);
        }
    }


}
