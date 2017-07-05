package com.chengtech.chengtechmt.util;

import java.util.Calendar;
import java.util.TimeZone;

public class MyTime {
	private static String mYear;
	private static String mMonth;
	private static String mDay;
	private static String mWay;

	public static String getCurrentTime() {
		// SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
		// Date date = new Date(System.currentTimeMillis());
		// String time = format.format(date);
		// return time;

		final Calendar c = Calendar.getInstance();
		c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
		mYear = String.valueOf(c.get(Calendar.YEAR)); // 获取当前年份
		mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
		mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
		mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
		if ("1".equals(mWay)) {
			mWay = "天";
		} else if ("2".equals(mWay)) {
			mWay = "一";
		} else if ("3".equals(mWay)) {
			mWay = "二";
		} else if ("4".equals(mWay)) {
			mWay = "三";
		} else if ("5".equals(mWay)) {
			mWay = "四";
		} else if ("6".equals(mWay)) {
			mWay = "五";
		} else if ("7".equals(mWay)) {
			mWay = "六";
		}
		return mYear + "年" + mMonth + "月" + mDay + "日" + "-星期" + mWay;
	}

	public static int getYear() {
		return Integer.parseInt(mYear);
	}

	public static int getMonth() {
		return Integer.parseInt(mMonth);
	}

	public static int getDay() {
		return Integer.parseInt(mDay);
	}

}
