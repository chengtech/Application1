package com.chengtech.chengtechmt.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 作者: LiuFuYingWang on 2017/6/5 16:30.
 */

public class MyViewPager extends ViewPager {

    public boolean isScrollable = true;
    public MyViewPager(Context context) {
        super(context);
    }

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!isScrollable)
            return false;
        return super.onInterceptTouchEvent(ev);
    }

    public void setScrollable(boolean isScrollable){
        this.isScrollable = isScrollable;
    }
}
