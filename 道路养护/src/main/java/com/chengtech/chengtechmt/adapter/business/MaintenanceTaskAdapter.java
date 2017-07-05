package com.chengtech.chengtechmt.adapter.business;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chengtech.chengtechmt.R;
import com.chengtech.chengtechmt.impl.OnItemClickListener;

import java.util.List;

/**
 * 作者: LiuFuYingWang on 2016/12/27 9:20.
 */

public class MaintenanceTaskAdapter extends RecyclerView.Adapter{
    private Context mContext;
    private List<String[]> data;
    private OnItemClickListener listener;

    public MaintenanceTaskAdapter(Context context, List<String[]> data) {
        mContext = context;
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.main_task_adapter, parent, false);
        RecyclerView.ViewHolder viewHolder = new RecyclerView.ViewHolder(view) {
            @Override
            public String toString() {
                return super.toString();
            }
        };
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        View itemView = holder.itemView;
        TextView title_tv = (TextView) itemView.findViewById(R.id.title);
        TextView content_tv = (TextView) itemView.findViewById(R.id.subtitle);
        TextView plan_tv = (TextView) itemView.findViewById(R.id.plan_tv);
        TextView add_tv = (TextView) itemView.findViewById(R.id.add_tv);
        LinearLayout plan_layout = (LinearLayout) itemView.findViewById(R.id.plan_layout);
        LinearLayout add_layout = (LinearLayout) itemView.findViewById(R.id.add_layout);
        if (data != null && data.get(position) != null) {
            String[] array = data.get(position);
            if (array != null && array.length > 0) {
                title_tv.setText(array[0]);
                content_tv.setText(Html.fromHtml(array[1]));
                plan_tv.setText(Html.fromHtml(array[2]));
                add_tv.setText(Html.fromHtml(array[3]));
                if (listener!=null) {
                    plan_layout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            listener.onItemClick(v,position);
                        }
                    });
                    add_layout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            listener.onItemClick(v,position);
                        }
                    });
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

}
