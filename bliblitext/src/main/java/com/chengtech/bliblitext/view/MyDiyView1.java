package com.chengtech.bliblitext.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;

/**
 * Created by yingwang on 2016/1/20.
 */
public class MyDiyView1 extends View {

    private int lastX;
    private int lastY;
    private Scroller scroller;

    public MyDiyView1(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyDiyView1(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        scroller = new Scroller(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = x;
                lastY = y;
                break;

            case MotionEvent.ACTION_MOVE:
                int offSetX = x - lastX;
                int offSetY = y - lastY;

//                layout(getLeft()+offSetX,getTop()+offSetY,getRight()+offSetX,getBottom()+offSetY);
//                offsetLeftAndRight(offSetX);
//                offsetTopAndBottom(offSetY);
                ((View)getParent()).scrollBy(-offSetX,-offSetY);
                break;

            case MotionEvent.ACTION_UP :
                View viewGroup = (View) getParent();
                scroller.startScroll(viewGroup.getScrollX(),viewGroup.getScrollY(),
                        -viewGroup.getScrollX(),-viewGroup.getScrollY(),1000);
//                scroller.startScroll((int)event.getRawX(),(int)event.getRawY(),
//                       100,100);
                invalidate();
                break;
        }

        return true;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();

        if (scroller.computeScrollOffset()) {
//            offsetLeftAndRight(scroller.getCurrX());
//            offsetTopAndBottom(scroller.getCurrY());
            ((View)getParent()).scrollTo(scroller.getCurrX(),scroller.getCurrY());
            invalidate();
        }
    }
}
