package com.chengtech.guaguaka.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.chengtech.guaguaka.R;

/**
 * Created by yingwang on 2016/1/26.
 */
public class GuaGuaka  extends View{
    private Canvas mCanvas;
    private Paint topPaint;
    private Bitmap mBitmap;
    private int viewWidth;
    private int viewHeight;
    private Path mPath;
    private int lastX;
    private int lastY;
    private String mText;
    private Rect mRect;
    private Paint underPaint;
    private int textColor = 0x000000;
    private float textSize = 30;
    private volatile boolean isCompleted =false;
    public GuaGuaka(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,R.styleable.GuaGuaka,defStyleAttr,0);
        int count = a.getIndexCount();
        for (int i=0;i<count;i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.GuaGuaka_textColor:
                    textColor = a.getColor(attr,Color.BLACK);
                    break;
                case R.styleable.GuaGuaka_textSize :
                    textSize = a.getDimension(attr,TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,20,getResources().
                            getDisplayMetrics()));
                    break;
            }
        }
        a.recycle();
        init();
    }

    private void init() {
        //设置顶部的画笔
        topPaint = new Paint();
        topPaint.setColor(Color.GRAY);
        topPaint.setStyle(Paint.Style.FILL);
        topPaint.setAntiAlias(true);
        topPaint.setStrokeJoin(Paint.Join.ROUND);
        topPaint.setDither(true);
        topPaint.setStrokeCap(Paint.Cap.ROUND);
        topPaint.setStrokeWidth(50);

        //设置path
        mPath = new Path();

        mText = "谢谢惠顾";
        mRect = new Rect();
        underPaint = new Paint();
        underPaint.setTextSize(textSize);
        underPaint.setColor(textColor);
        underPaint.getTextBounds(mText,0,mText.length(),mRect);

    }



    public GuaGuaka(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        viewWidth = getMeasuredWidth();
        viewHeight = getMeasuredHeight();

        //设置canvas
        mBitmap = Bitmap.createBitmap(viewWidth, viewHeight, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
        Bitmap b = BitmapFactory.decodeResource(getResources(), R.mipmap.fg_guaguaka);
        mCanvas.drawRoundRect(new RectF(0, 0, viewWidth, viewHeight), 30, 30, topPaint);
        mCanvas.drawBitmap(b, null, new RectF(0, 0, viewWidth, viewHeight), null);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawText(mText, viewWidth / 2 - mRect.width() / 2, viewHeight / 2 + mRect.height() / 2, underPaint);
        if (!isCompleted) {
            drawPath();
            canvas.drawBitmap(mBitmap,0,0,null);
        }
    }

    /**
     * 画路径
     */
    private void drawPath() {
        topPaint.setStyle(Paint.Style.STROKE);
        topPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        mCanvas.drawPath(mPath, topPaint);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        float x =  event.getX();
        float y =  event.getY();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                lastX = (int) x ;
                lastY = (int) y;
                mPath.moveTo(x,y);

                break;
            case MotionEvent.ACTION_MOVE:

                mPath.lineTo(x,y);

                break;
            case MotionEvent.ACTION_UP:
                new AsyncTask<Void,Void,Boolean>(){
                    @Override
                    protected Boolean doInBackground(Void... params) {
                        int w = viewWidth;
                        int h = viewHeight;
                        int wipeArea = 0;
                        int totalArea = w * h;
                        int [] pxiels = new int[w*h];

                        //获得bitmap上所有的信息
                        mBitmap.getPixels(pxiels,0,w,0,0,w,h);

                        for (int i=0;i<w;i++) {
                            for (int j=0;j<h;j++) {
                                int index = i + j * w;
                                if (pxiels[index]==0) {
                                    wipeArea++;
                                }
                            }
                        }

                        if ( wipeArea*100/totalArea >50 && wipeArea>0) {
                            isCompleted = true;
                            return true;
                        }

                        return false;
                    }

                    @Override
                    protected void onPostExecute(Boolean aVoid) {
                        if (aVoid) {
                            invalidate();
                        }
                    }
                }.execute();
                break;

        }
        invalidate();
        return true;
    }
}
