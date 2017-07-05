package com.chengtech.chengtechmt.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chengtech.chengtechmt.R;
import com.chengtech.chengtechmt.entity.Attachment;
import com.chengtech.chengtechmt.impl.OnItemClickListener;
import com.chengtech.chengtechmt.util.CommonUtils;

import java.util.List;

/**
 * 作者: LiuFuYingWang on 2016/12/28 17:49.
 */

public class AttachmentAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<Attachment> data;
    private OnItemClickListener onItemClickListener;



    public AttachmentAdapter(Context context, List<Attachment> data){
        this.mContext = context;
        this.data = data;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_attachement_info,parent,false);
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
        ImageView img = (ImageView) itemView.findViewById(R.id.img);
        TextView fileName = (TextView) itemView.findViewById(R.id.fileName);
        TextView size = (TextView) itemView.findViewById(R.id.size);
        TextView time = (TextView) itemView.findViewById(R.id.time);
        Attachment results = data.get(position);
//        img.setBackgroundResource(R.mipmap.file);
        fileName.setText(results.fileName);
        size.setText(results.size);
        time.setVisibility(View.GONE);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener!=null){
                    onItemClickListener.onItemClick(v,position);
                }
            }
        });
//        img.getParent().requestDisallowInterceptTouchEvent(false);
//        time.setText(results.getTime());
    }

    @Override
    public int getItemCount() {
        return data==null?0:data.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
