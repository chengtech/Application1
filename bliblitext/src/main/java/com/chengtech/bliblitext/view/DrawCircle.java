package com.chengtech.bliblitext.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by yingwang on 2016/1/21.
 */
public class DrawCircle extends View {
    private int mViewWidth;
    private int mViewHeight;
    public DrawCircle(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawCircle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);


    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mViewWidth = getMeasuredWidth();
        mViewHeight = getMeasuredHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint circlePaint = new Paint();
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setAntiAlias(true);
        circlePaint.setStrokeWidth(10);
        canvas.drawCircle(mViewWidth/2,mViewHeight/2,mViewWidth/2-5,circlePaint);

        //画刻度
        Paint paintDegree = new Paint();
        for (int i=0;i<24;i++) {
            if (i==0 || i==6 || i==12 || i==18) {
                paintDegree.setStrokeWidth(6);
                paintDegree.setTextSize(50);
                canvas.drawLine(mViewWidth / 2, mViewHeight / 2 - mViewWidth / 2, mViewWidth / 2,
                        mViewHeight / 2 - mViewWidth / 2 + 60, paintDegree);
                canvas.drawText(String.valueOf(i),mViewWidth/2-paintDegree.measureText(String.valueOf(i))/2,
                        mViewHeight / 2 - mViewWidth / 2 + 90,paintDegree);
            }else {
                paintDegree.setStrokeWidth(3);
                paintDegree.setTextSize(30);
                canvas.drawLine(mViewWidth / 2, mViewHeight / 2 - mViewWidth / 2, mViewWidth / 2,
                        mViewHeight / 2 - mViewWidth / 2 + 30, paintDegree);
                canvas.drawText(String.valueOf(i), mViewWidth / 2 - paintDegree.measureText(String.valueOf(i)) / 2,
                        mViewHeight / 2 - mViewWidth / 2 + 60, paintDegree);
            }

            canvas.rotate(15,mViewWidth/2,mViewHeight/2);
        }

        //画指针
        Paint hourPaint  = new Paint();
        hourPaint.setStrokeWidth(50);
        Paint minutePaint = new Paint();
        minutePaint.setStrokeWidth(30);

        canvas.translate(mViewWidth/2,mViewHeight/2);
        canvas.drawLine(0,0,100,100,hourPaint);
        canvas.drawLine(-180,-200,0,0,minutePaint);

    }
}
