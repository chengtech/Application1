package com.chengtech.chengtechmt.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chengtech.chengtechmt.R;
import com.chengtech.chengtechmt.thirdlibrary.wxpictureselector.PictureSelectorActivity;
import com.chengtech.chengtechmt.util.ViewHolder;

import java.io.File;
import java.util.ArrayList;

/**
 * 作者: LiuFuYingWang on 2017/5/22 18:27.
 */

public class ImageAddAdapter extends RecyclerView.Adapter {
    public ArrayList<String> picturePaths;
    public Context mContext;

    public ImageAddAdapter(Context context,ArrayList<String> picturePaths) {
        this.mContext = context;
        this.picturePaths = picturePaths;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ImageView imageView = new ImageView(parent.getContext());
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(150,
                150);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(params);
        RecyclerView.ViewHolder viewHolder = new RecyclerView.ViewHolder(imageView){

        };
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ImageView imageView = (ImageView) holder.itemView;
        if (picturePaths.size()==0) {
            imageView.setImageURI(null);
            imageView.setBackgroundResource(R.drawable.ic_add_picture);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, PictureSelectorActivity.class);
                    Activity activity = (Activity) mContext;
                    activity.startActivityForResult(intent,0x00);
                }
            });
        }else if (position < getItemCount() - 1) {
            imageView.setImageURI(Uri.fromFile(new File(picturePaths.get(position))));
            imageView.setOnClickListener(null);
        } else {
            imageView.setImageURI(null);
            imageView.setBackgroundResource(R.drawable.ic_add_picture);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, PictureSelectorActivity.class);
                    Activity activity = (Activity) mContext;
                    activity.startActivityForResult(intent,0x00);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return 1+picturePaths.size();
    }
}
