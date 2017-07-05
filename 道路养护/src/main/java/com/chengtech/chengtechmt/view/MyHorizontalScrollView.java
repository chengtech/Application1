package com.chengtech.chengtechmt.view;


import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.View;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;
import android.widget.Toast;

import com.chengtech.chengtechmt.util.LogUtils;

public class MyHorizontalScrollView extends HorizontalScrollView {
    private Context mContext;
    private float downX;
    private float downY;
    private float moveX;
    private float moveY;
    private long currentMS;
    private boolean result = true;
    private boolean isOnClick;

    public interface OnMyScrollViewListener {
        public void onScrollChanged(int l, int t, int oldl, int oldt);

    }

    public interface OnMyClickListener {
        public void onMyScrollViewClick();

    }

    private OnMyScrollViewListener myScrollViewListener;
    private OnMyClickListener myClickListener;

    public void SetOnMyScrollViewListener(OnMyScrollViewListener listener) {
        myScrollViewListener = listener;
    }

    public void SetOnMyClickListener(OnMyClickListener listener) {
        this.myClickListener = listener;
    }

    public MyHorizontalScrollView(Context context, AttributeSet attrs,
                                  int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
    }

    public MyHorizontalScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyHorizontalScrollView(Context context) {
        this(context, null);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        return super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        if (myScrollViewListener!=null)
            myScrollViewListener.onScrollChanged(l, t, oldl, oldt);
        super.onScrollChanged(l, t, oldl, oldt);
    }

}
