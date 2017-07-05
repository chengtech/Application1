package com.chengtech.chengtechmt.picasso;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.TextView;

import com.chengtech.chengtechmt.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

/**
 * 作者: LiuFuYingWang on 2016/11/9 17:17.
 */

public class TextViewTarget implements Target {
    public View view;
    public Context mContext;

    public TextViewTarget(View view, Context context) {
        this.view = view;
        this.mContext = context;
    }

    @Override
    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
        if (view instanceof TextView) {
            Drawable drawable = new BitmapDrawable(mContext.getResources(), bitmap);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            ((TextView)view).setCompoundDrawables(null, null, null, drawable);
        }

    }

    @Override
    public void onBitmapFailed(Drawable errorDrawable) {

    }

    @Override
    public void onPrepareLoad(Drawable placeHolderDrawable) {
        Drawable drawable = mContext.getResources().getDrawable(R.mipmap.placeholder);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        if (view instanceof TextView)
            ((TextView)view).setCompoundDrawables(null, null, null, drawable);
    }
}
