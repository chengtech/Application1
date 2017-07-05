package com.chengtech.chengtechmt.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.chengtech.chengtechmt.R;
import com.chengtech.chengtechmt.util.CommonUtils;
import com.chengtech.chengtechmt.util.DateUtils;
import com.chengtech.chengtechmt.util.MyConstants;
import com.chengtech.chengtechmt.util.ViewHolder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者: LiuFuYingWang on 2016/5/11 15:49.
 */
public class DetailAdapter extends BaseAdapter {
    public String[] subTitle;
    public List<String> content;
    public Context mContext;
    public int layoutRes;
    public OnListViewHightChangeListener listener;


    public DetailAdapter(Context context, int resource, String[] subTitle, List<String> content) {
        this.subTitle = subTitle;
        this.content = content;
        this.mContext = context;
        this.layoutRes = resource;
    }

    @Override
    public int getCount() {
        return subTitle.length;
    }

    @Override
    public Object getItem(int position) {
        return content.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(layoutRes, parent, false);

        }
        TextView subTitle_tv = ViewHolder.get(convertView, R.id.subtitle);
        final TextView content_tv = ViewHolder.get(convertView, R.id.content);

        subTitle_tv.setText(subTitle[position] + ":");
        String msg = content.get(position);
        if (msg.contains("AM") || msg.contains("PM")) {
            msg = DateUtils.convertDate2(msg);
        }
        content_tv.setText("\t\t" + msg);
        return convertView;
    }

    public void setOnHeightChangeListener(OnListViewHightChangeListener listener) {
        this.listener = listener;
    }

    public interface OnListViewHightChangeListener {
        public void onHeightChangeListener(int addHeight);
    }

}
