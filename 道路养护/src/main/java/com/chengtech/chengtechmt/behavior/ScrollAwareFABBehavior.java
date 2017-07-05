package com.chengtech.chengtechmt.behavior;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

/**
 * 作者: LiuFuYingWang on 2016/9/20 16:01.
 */
public class ScrollAwareFABBehavior extends FloatingActionButton.Behavior {

    /**
     * 因为是在XML中使用app:layout_behavior定义静态的这种行为,
     * 必须实现一个构造函数使布局的效果能够正常工作。
     * 否则 Could not inflate Behavior subclass error messages.
     *
     */
    public ScrollAwareFABBehavior(Context context, AttributeSet attr) {
        super();
    }


    /**
     * 处理垂直方向上的滚动事件
     *
     */
    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View directTargetChild, View target, int nestedScrollAxes) {
        return nestedScrollAxes== ViewCompat.SCROLL_AXIS_VERTICAL || super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, nestedScrollAxes);
    }

    /**
     * 检查Y的位置，并决定按钮是否动画进入或退出
     *
     */
    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child,
                               View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed,
                dyUnconsumed);

        //方式一：向上滚动，出现按钮；向下滚动，隐藏按钮
//        if (dyConsumed > 0 && child.getVisibility() == View.VISIBLE) {
//            // User scrolled down and the FAB is currently visible -> hide the FAB
//            child.hide();
//        } else if (dyConsumed < 0 && child.getVisibility() != View.VISIBLE) {
//            // User scrolled up and the FAB is currently not visible -> show the FAB
//            child.show();
//        }

        //方式二：

        child.hide();
    }

    //方式二：滚动时，隐藏按钮；停止时出现按钮
    @Override
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View target) {
        super.onStopNestedScroll(coordinatorLayout, child, target);
        child.show();
    }

}
