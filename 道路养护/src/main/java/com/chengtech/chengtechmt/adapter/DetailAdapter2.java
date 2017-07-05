package com.chengtech.chengtechmt.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chengtech.chengtechmt.R;
import com.chengtech.chengtechmt.picasso.CompressTransFormation;
import com.chengtech.chengtechmt.picasso.TextViewTarget;
import com.chengtech.chengtechmt.util.DateUtils;
import com.chengtech.chengtechmt.util.MyConstants;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者: LiuFuYingWang on 2016/5/11 15:49.
 */
public class DetailAdapter2 extends RecyclerView.Adapter<DetailAdapter2.MyViewHolder> {
    public ArrayList<String> subTitle;
    public List<String> content;
    public Context mContext;
    public int targetWidth;
    public List<TextViewTarget> targets = new ArrayList<>();
    public OnItemLongClickListener longClickListener;
    public Integer resId=null;


    public DetailAdapter2(Context context, ArrayList<String> subTitle, List<String> content) {
        this.subTitle = subTitle;
        this.content = content;
        this.mContext = context;
        this.targetWidth  = mContext.getResources().getDisplayMetrics().widthPixels-100;
    }

    public DetailAdapter2(Context context,int resId, ArrayList<String> subTitle, List<String> content) {
        this.subTitle = subTitle;
        this.content = content;
        this.mContext = context;
        this.resId = resId;
        this.targetWidth  = mContext.getResources().getDisplayMetrics().widthPixels-100;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view =null;
        if (resId==null) {
//            view = LayoutInflater.from(mContext).inflate(R.layout.item_detail_display, parent, false);
            view = LayoutInflater.from(mContext).inflate(R.layout.item_recycle4, parent, false);
        }else {
            view = LayoutInflater.from(mContext).inflate(resId, parent, false);
        }

        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.subTitle_tv.setText(subTitle.get(position) + ":");
        String msg = content.get(position);
        if (msg!=null) {
            if (msg.contains("AM") || msg.contains("PM")) {
                msg = DateUtils.convertDate2(msg);
            }
            if (msg.contains(".jpg") || msg.contains(".png") || msg.contains(".jpeg") ||
                    msg.contains(".JPG")|| msg.contains(".PNG") || msg.contains(".JPEG")) {
                //picasso的target必须要用实体类，不能使用匿名类，不然会被回收，当初在这里陷入了大坑！！！！
                TextViewTarget target = new TextViewTarget(holder.content_tv, mContext);
                targets.add(target);
                Picasso.with(mContext)
                        .load(MyConstants.PRE_URL + msg)
                        .transform(new CompressTransFormation(targetWidth))
                        .into(target);
            } else {
                holder.content_tv.setText(msg);
                if (longClickListener != null)
                    holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            longClickListener.onLongClick(holder.content_tv, position);
                            return true;
                        }
                    });
            }
        }

    }

    @Override
    public int getItemCount() {
        return subTitle==null?0:subTitle.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView subTitle_tv;
        public TextView content_tv;

        public MyViewHolder(View itemView) {
            super(itemView);
            subTitle_tv = (TextView) itemView.findViewById(R.id.subtitle);
            content_tv = (TextView) itemView.findViewById(R.id.content);
        }
    }

    public void setLongClickListener(OnItemLongClickListener longClickListener) {
        this.longClickListener = longClickListener;
    }

    public interface OnItemLongClickListener {
        public void onLongClick(View view, int position);
    }
}
