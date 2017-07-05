package com.chengtech.chengtechmt.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chengtech.chengtechmt.R;
import com.chengtech.chengtechmt.entity.BaseModel;

import java.util.List;

/**
 * 作者: LiuFuYingWang on 2016/7/1 11:40.
 */
public class SupSubAdapter<T extends BaseModel> extends RecyclerView.Adapter<SupSubAdapter.MyViewHolder> {
    public LayoutInflater inflater;
    public List<T> data;

    public SupSubAdapter(Context context, List<T> data) {
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_bridge_sup_sub, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(SupSubAdapter.MyViewHolder holder, int position) {
        holder.tv1.setText(data.get(position).getContent().get(0));
        holder.tv2.setText(data.get(position).getContent().get(1));
        holder.tv3.setText(data.get(position).getContent().get(2));
        holder.tv4.setText(data.get(position).getContent().get(3));
    }


    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView tv1, tv2, tv3, tv4;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv1 = (TextView) itemView.findViewById(R.id.tv1);
            tv2 = (TextView) itemView.findViewById(R.id.tv2);
            tv3 = (TextView) itemView.findViewById(R.id.tv3);
            tv4 = (TextView) itemView.findViewById(R.id.tv4);
        }
    }
}
