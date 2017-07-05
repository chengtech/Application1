package com.chengtech.chengtechmt.view;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

import com.chengtech.chengtechmt.R;

public class MyDialog extends Dialog {
	private Context context;
	private TextView titile_tv, message_tv, positive_tv, nagetive_tv;
	private static int default_width = 250; // 默认宽度
	private static int default_height = 200;// 默认高度


	public MyDialog(Context context, int theme) {
		super(context, theme);
		this.context = context;
		
		setView();
		
		// 设置window参数,定义窗口的高度和宽度
		Window window = getWindow();
		LayoutParams params = window.getAttributes();
		params.width = (int) dpToxp(default_width);
		params.height = (int) dpToxp(default_height);
		window.setAttributes(params);
	}

	//设置标题
	public void setTitle(String title) {
		titile_tv.setText(title);
	}
	//设置内容
	public void setMessage(String msg) {
		message_tv.setText(msg);
	}
	private void setView() {
		
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.diy_dialog, null);
		titile_tv = (TextView) view.findViewById(R.id.dialog_title);
		message_tv = (TextView) view.findViewById(R.id.dialog_message);
		positive_tv = (TextView) view.findViewById(R.id.dialog_positive);
		nagetive_tv = (TextView) view.findViewById(R.id.dialog_nagetive);
		setContentView(view);
		
	}

	public void setOnPositiveClickListener(String str,
			View.OnClickListener listener) {
		positive_tv.setText(str);
		positive_tv.setOnClickListener(listener);
	}

	public void setOnNagetiveClickListener(String str,
			View.OnClickListener listener) {
		nagetive_tv.setText(str);
		nagetive_tv.setOnClickListener(listener);
	}

	public float dpToxp(int dp) {
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		return metrics.density * dp;
	}

}
