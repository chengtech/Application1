package com.chengtech.chengtechmt.thirdlibrary.wxpictureselector;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;


import com.chengtech.chengtechmt.R;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 2016/3/16.
 */
public class MyPictureAdapter extends BaseAdapter {
    private Set<String> checkedState = new HashSet<>();
    private List<String> imgPaths;
    private String dirPath;
    private LayoutInflater mInflater;

    public MyPictureAdapter(Context context,List<String> imgPath,String dirPath) {
        this.imgPaths = imgPath;
        this.dirPath = dirPath;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return imgPaths.size();
    }

    @Override
    public Object getItem(int position) {
        return imgPaths.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder ;
        if (convertView==null) {
            convertView = mInflater.inflate(R.layout.item_gridview_picture,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.id_imageView);
            viewHolder.imageButton = (ImageButton) convertView.findViewById(R.id.id_imageButton);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.imageView.setImageResource(R.mipmap.pictures_no);
        viewHolder.imageButton.setImageResource(R.mipmap.picture_unselected);
        viewHolder.imageView.setColorFilter(null);

        final String filePath = dirPath+"/"+imgPaths.get(position);
        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkedState.contains(filePath)) {
                    checkedState.remove(filePath);
                    viewHolder.imageView.setColorFilter(null);
                    viewHolder.imageButton.setImageResource(R.mipmap.picture_unselected);
                } else {
                    checkedState.add(filePath);
                    viewHolder.imageView.setColorFilter(Color.parseColor("#77000000"));
                    viewHolder.imageButton.setImageResource(R.mipmap.pictures_selected);
                }
            }
        });

        ImageLoader.getmInstance(3, ImageLoader.Type.LIFO).loadImage(filePath,viewHolder.imageView);

        if (checkedState.contains(filePath)){
            viewHolder.imageView.setColorFilter(Color.parseColor("#77000000"));
            viewHolder.imageButton.setImageResource(R.mipmap.pictures_selected);
        }
        return convertView;
    }

    public Set<String> getCheckedState() {
        return checkedState;
    }


    private class ViewHolder{
        ImageView imageView;
        ImageButton imageButton;
    }
}
