package com.chengtech.chengtechmt.util;

import com.chengtech.chengtechmt.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.PopupWindow;

public class PopupWindowUtils {

	private Context context;

	private int screenWidth;

	private int screenHeigh;

	private PopupWindow popupWindow;

	View view;
	View downview;

	public PopupWindowUtils(Context context, int screenWidth, int screenHeigh,
			View view, View downview) {
		super();
		this.context = context;
		this.screenWidth = screenWidth;
		this.screenHeigh = screenHeigh;
		this.view = view;
		this.downview = downview;
		init();
	}

	public void init() {

		popupWindow = new PopupWindow(view, screenWidth*2/3, screenHeigh);
		// view.setBackgroundResource(R.drawable.popupwindow);
		/*** 这2句很重要 ***/
		ColorDrawable cd = new ColorDrawable(-0000);
		popupWindow.setBackgroundDrawable(cd);

		// popupWindow.showAsDropDown(down);
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);// 设置外部能点击
//		popupWindow.showAtLocation(downview, Gravity.BOTTOM | Gravity.RIGHT, 0,
//				0);
		popupWindow.showAsDropDown(downview, screenWidth*2/6+screenWidth/2, 0);
		popupWindow.setAnimationStyle(R.style.PopupAnimation);
		backgroundAlpha(0.5f);
		popupWindow.setOnDismissListener(new poponDismissListener());
		popupWindow.update();

		// setting popupWindow d点击消失
		popupWindow.setTouchInterceptor(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				/**** 如果点击了popupwindow的外部，popupwindow也会消失 ****/
				if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
					popupWindow.dismiss();
					return true;
				}
				return false;
			}
		});

		
		

	}

	/**
	 * 添加新笔记时弹出的popWin关闭的事件，主要是为了将背景透明度改回来
	 * 
	 */
	public void backgroundAlpha(float f) {
		LayoutParams param = ((Activity) context).getWindow().getAttributes();
		param.alpha = f;
		((Activity) context).getWindow().setAttributes(param);
//		downview.setAlpha(f);

	}
	
	public void dismissPopupwindow(){
		popupWindow.dismiss();
	}
	
	class poponDismissListener implements PopupWindow.OnDismissListener {

		@Override
		public void onDismiss() {
			backgroundAlpha(1f);
		}

	}

}
