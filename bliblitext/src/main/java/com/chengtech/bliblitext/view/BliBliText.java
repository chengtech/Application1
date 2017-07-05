package com.chengtech.bliblitext.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by yingwang on 2016/1/19.
 */
public class BliBliText extends TextView{
    private Context mcContext;
    private int mViewWidth ;
    private Paint mPaint;
    private LinearGradient mlLinearGradient;
    private Matrix matrix;
    private int mTranslate = 0;
    public BliBliText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BliBliText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mcContext = context;

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (mViewWidth==0) {
            mViewWidth = getMeasuredWidth();
            if (mViewWidth>0) {
                mPaint = getPaint();
                mlLinearGradient = new LinearGradient(0,0,mViewWidth,0,new int[]{Color.BLUE,Color.GREEN,
                Color.RED,Color.BLUE},null, Shader.TileMode.CLAMP);
                mPaint.setShader(mlLinearGradient);
                matrix = new Matrix();
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (matrix!=null) {
            mTranslate += mViewWidth/5;
            if (mTranslate>2*mViewWidth) {
                mTranslate = -mViewWidth;
            }

            matrix.setTranslate(mTranslate,0);
            mlLinearGradient.setLocalMatrix(matrix);

            postInvalidateDelayed(50);
        }
    }
}
