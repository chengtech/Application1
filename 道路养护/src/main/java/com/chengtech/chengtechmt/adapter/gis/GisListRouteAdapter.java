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
import com.chengtech.chengtechmt.entity.Route;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * 作者: LiuFuYingWang on 2016/12/6 15:19.
 */

public class GisListRouteAdapter extends RecyclerView.Adapter<GisListRouteAdapter.MyHolder> {
    public SparseBooleanArray mSelectedPositions = new SparseBooleanArray();
    public HashMap<String,Boolean> selectedPosMap ;
    private List<Route> data;
    private Context context;

    public GisListRouteAdapter(List<Route> data,HashMap<String,Boolean> selectedPosMap) {
        this.data = data;
        if (selectedPosMap==null) {
            this.selectedPosMap = new HashMap<>();
        }else {
            this.selectedPosMap = selectedPosMap;
        }

    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gis_list_route, parent, false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyHolder holder, final int position) {
        holder.number.setText(String.valueOf(position+1));
        holder.routeName.setText(data.get(position).name);
        holder.routeCode.setText(data.get(position).code);
        holder.routeGrade.setText(data.get(position).routeGrade);
        holder.checkBox.setChecked(isItemChecked(data.get(position).id));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.checkBox.setChecked(!holder.checkBox.isChecked());
            }
        });
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setItemChecked(data.get(position).id,isChecked);
            }
        });

        if (position %2==0) {
            holder.itemView.setBackground(context.getResources().getDrawable(R.drawable.default_white_click));
        }else {
            holder.itemView.setBackground(context.getResources().getDrawable(R.drawable.default2_white_click));
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private void setItemChecked(int position, boolean isChecked) {
        mSelectedPositions.put(position, isChecked);
    }
    //根据对象id来保存选中状态，这样就算顺序出错也可以根据唯一的id来确认。
    private void setItemChecked(String targetId, boolean isChecked) {
        selectedPosMap.put(targetId, isChecked);
    }

    private boolean isItemChecked(int position) {
        return mSelectedPositions.get(position);
    }

    //根据对象id来辨别是否选中
    private boolean isItemChecked(String targetId) {
        return selectedPosMap.get(targetId)==null?false:selectedPosMap.get(targetId);
    }

    public List<Route> getCheckedItems(){
        List<Route> result = new ArrayList<>();
        for (int i=0;i<data.size();i++) {
            if (isItemChecked(data.get(i).id)){
                result.add(data.get(i));
            }
        }
        return result;
    }



    public class MyHolder extends RecyclerView.ViewHolder {

        public TextView number, routeName, routeCode, routeGrade;
        public CheckBox checkBox;
        public MyHolder(View itemView) {
            super(itemView);
            number = (TextView) itemView.findViewById(R.id.number);
            routeName = (TextView) itemView.findViewById(R.id.routeName);
            routeCode = (TextView) itemView.findViewById(R.id.routeCode);
            routeGrade = (TextView) itemView.findViewById(R.id.routeGrade);
            checkBox = (CheckBox) itemView.findViewById(R.id.select_checkbox);
        }
    }


}
