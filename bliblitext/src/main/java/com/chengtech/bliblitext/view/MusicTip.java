package com.chengtech.bliblitext.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by yingwang on 2016/1/20.
 */
public class MusicTip extends View {

    private int mViewWidth;
    private int mViewHeight;
    private int rectCount = 10; //音乐条的数目
    private int mRectWdith ;  //音乐条的宽度
    private int offset =5; //平移的距离
    private float mRectHeight  ;
    private Paint mPaint ;
    private LinearGradient mLinearGradient;

    public MusicTip(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);


    }

    public MusicTip(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (int i=0;i<rectCount;i++) {
            mRectHeight = (float)Math.random()*mViewHeight;
            canvas.drawRect(((i+1)*offset+i*mRectWdith),mRectHeight,((i+1)*offset+(i+1)*mRectWdith),
                    mViewHeight,mPaint);
        }

        postInvalidateDelayed(500);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mViewWidth = getMeasuredWidth();
        mViewHeight = getMeasuredHeight();
        mRectWdith = (mViewWidth-(rectCount+1)*offset) / rectCount ;
        mPaint = new Paint();
        mLinearGradient = new LinearGradient(0,0,mRectWdith,mViewHeight,Color.BLUE,Color.GREEN,
                Shader.TileMode.CLAMP);

        mPaint.setShader(mLinearGradient);
    }
}
