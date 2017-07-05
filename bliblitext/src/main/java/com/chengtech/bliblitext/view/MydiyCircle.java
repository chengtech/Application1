package com.chengtech.bliblitext.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by yingwang on 2016/1/19.
 */
public class MydiyCircle extends View {

    private int length;
    private int circleXY;
    private float mRadius;
    private RectF mRectF;
    private Paint circlePaint;
    private Paint arcPaint;
    private Paint textPaint;

    public MydiyCircle(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MydiyCircle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        length = context.getResources().getDisplayMetrics().widthPixels;
        circleXY = length / 2;
        mRadius = (float)circleXY / 2 ;
        mRectF = new RectF((float)(length*0.1),(float)(length*0.1),(float)(length*0.9),(float)(length*0.9));
        circlePaint = new Paint();
        circlePaint.setColor(Color.RED);
        textPaint = new Paint();
        textPaint.setColor(Color.BLUE);
        textPaint.setTextSize(40);

        arcPaint = new Paint();
        arcPaint.setAntiAlias(true);
        arcPaint.setStrokeWidth((float) 6);              //线宽
        arcPaint.setStyle(Paint.Style.STROKE);
        arcPaint.setShader(new LinearGradient(0,0,length,length,Color.BLUE,Color.RED, Shader.TileMode.CLAMP));

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        canvas.drawCircle(circleXY, circleXY, mRadius, circlePaint);

        String mString = "android is very interesting!";
//        canvas.drawText(mString,circleXY,circleXY+mString.length(),textPaint);
//        canvas.drawText(mString,0,mString.length(),circleXY-(mString.length()*10),circleXY,textPaint);

//        canvas.drawArc(mRectF, 270, 270, false, arcPaint);
        Path path = new Path();
        path.moveTo(10, 10); //移动到 坐标10,10

        path.lineTo(50, 60);

        path.lineTo(200,80);

        canvas.drawPath(path,arcPaint);
    }
}
