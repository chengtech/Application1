package com.chengtech.bliblitext.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import com.chengtech.bliblitext.R;

/**
 * Created by yingwang on 2016/1/21.
 */
public class MyBitmapGradient extends View {

    public MyBitmapGradient(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyBitmapGradient(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.GRAY);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.mine);
        BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        Paint mPaint = new Paint();
        mPaint.setShader(shader);
        canvas.drawCircle(500,250,300,mPaint);
    }
}
