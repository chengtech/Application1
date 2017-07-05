package com.chengtech.bliblitext.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by yingwang on 2016/1/21.
 */
public class MyDrawLine extends View{

    private Paint mPaint;
    private Path mPath;
    private Canvas mcCanvas;
    private int default_x = 50;
    private int default_y = 50;
    private int access_x = 100;
    private int access_y = 100;
    public MyDrawLine(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyDrawLine(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mPaint = new Paint();
        mPaint.setColor(Color.BLUE);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(8);
        mPath = new Path();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
       // canvas.drawLine(50,50,200,100,mPaint);
//        mPath.moveTo(default_x, default_y);
        mPath.lineTo(access_x, access_y);
        canvas.drawPath(mPath, mPaint);
        default_x = access_x;
        default_y = access_y;
    }

    public void addPath(int x,int y) {
        access_x = x;
        access_y = y;
        invalidate();
    }
}
