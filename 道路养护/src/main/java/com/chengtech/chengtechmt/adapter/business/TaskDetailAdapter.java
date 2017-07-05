package com.chengtech.chengtechmt.adapter.business;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chengtech.chengtechmt.R;
import com.chengtech.chengtechmt.entity.TaskDetail;
import com.chengtech.chengtechmt.util.CommonUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者: LiuFuYingWang on 2016/10/27 14:01.
 */

public class TaskDetailAdapter extends RecyclerView.Adapter<TaskDetailAdapter.MyViewHolder> {

    private Context context;
    private List<TaskDetail> data;
    private String [][] data2;
    private List<String> content;

    public TaskDetailAdapter(Context context, List<TaskDetail> data,List<String> title) {
        this.context = context;
        this.data = data;
        content = new ArrayList<>();
        if (data != null && data.size() > 0) {
            for (TaskDetail item : data) {
                content.addAll(item.getContent());
            }
        }
        data2 = CommonUtils.translateToMartrix(content,title);

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_maintaintaskitem, parent, false);
        return new TaskDetailAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if (position % data2.length == 0) {
            holder.textView.setTextColor(context.getResources().getColor(android.R.color.holo_blue_bright));
            holder.textView.setTextSize(16);
        } else {
            holder.textView.setTextColor(context.getResources().getColor(android.R.color.black));
            holder.textView.setTextSize(12);
        }

        int x = position % data2.length;
        int y = position / data2.length;

        holder.textView.setText(data2[x][y]);
    }

    @Override
    public int getItemCount() {
        return data2.length * data2[0].length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textView;

        public MyViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.textview);
        }
    }
}
