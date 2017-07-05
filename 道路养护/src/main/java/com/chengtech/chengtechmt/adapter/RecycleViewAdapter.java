package com.chengtech.chengtechmt.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chengtech.chengtechmt.R;
import com.chengtech.chengtechmt.entity.Menu;
import com.chengtech.chengtechmt.util.MyConstants;

import java.util.List;

/**
 * 作者: LiuFuYingWang on 2016/6/22 14:50.
 */
public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.MyViewHolder> {
    private String[] subTitle;
    private int[] imageIds;
    private LayoutInflater inflater;
    public OnItemClickListener onItemClickListener;
    public List<Menu> menuList;
    public int item_res;
    public String[] flags; //该属性是区分哪些可以点击，哪些不能点击

    public static final int SIMPLE_ITEM_1 = R.layout.item_recycle1;
    public static final int SIMPLE_ITEM_2 = R.layout.item_recycle2;


    public RecycleViewAdapter(Context context, String[] subtitle, int[] imageIds, int item_res) {
        this.subTitle = subtitle;
        this.imageIds = imageIds;
        this.item_res = item_res;
        inflater = LayoutInflater.from(context);

    }

    public RecycleViewAdapter(Context context, String[] subtitle, int item_res) {
        this.subTitle = subtitle;
        this.item_res = item_res;
        inflater = LayoutInflater.from(context);

    }

    public RecycleViewAdapter(Context context, List<Menu> menuList, int[] imageIds, int item_res) {
        this.menuList = menuList;
        this.imageIds = imageIds;
        this.item_res = item_res;
        inflater = LayoutInflater.from(context);
    }

    public RecycleViewAdapter(Context context, List<Menu> menuList, int[] imageIds, int item_res, String[] flags) {
        this.menuList = menuList;
        this.imageIds = imageIds;
        this.item_res = item_res;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(item_res, parent, false);
//        TypedValue value = new TypedValue();
//        parent.getContext().getTheme().resolveAttribute(R.attr.selectableItemBackground,value,true);
//        view.setBackgroundResource(value.resourceId);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        if (menuList == null) {
            return subTitle.length;
        }
        return menuList.size();
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        if (menuList == null) {
            holder.tv.setText(subTitle[position]);
            if (MyConstants.imageDict.containsKey(subTitle[position]))
                holder.imageview.setBackgroundResource(MyConstants.imageDict.get(subTitle[position]));
        } else {
            String title = menuList.get(position).itemName;
            holder.tv.setText(title);
            if (MyConstants.imageDict.containsKey(title))
                holder.imageview.setBackgroundResource(MyConstants.imageDict.get(title));
        }

        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (MyConstants.userRights.get(holder.tv.getText().toString().trim())!=null
                            && MyConstants.userRights.get(holder.tv.getText().toString().trim())){
                        onItemClickListener.setOnItemClickListener(v, position);
                    }else {
                        Toast.makeText(inflater.getContext(), "没有权限", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onItemClickListener.setOnItemLongClickListener(v, position);
                    return true;
                }
            });


        }
    }



    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        public void setOnItemClickListener(View view, int position);

        public void setOnItemLongClickListener(View view, int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageview;
        TextView tv;
        View itemView;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            imageview = (ImageView) itemView.findViewById(R.id.recycle_image);
            tv = (TextView) itemView.findViewById(R.id.recycle_tv);
        }
    }

}



