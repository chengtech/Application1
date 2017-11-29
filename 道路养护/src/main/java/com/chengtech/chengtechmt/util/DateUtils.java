package com.chengtech.chengtechmt.util;

import android.app.DatePickerDialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {


    /**
     * 将日期转换成年月日的格式
     */
    public static String convertDate(String date) {
        if (TextUtils.isEmpty(date))
            return null;
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.CHINESE);
        return dateFormat.format(Date.parse(date));
    }


    /**
     * 将日期转换成xxxx-xx-xx的格式
     */
    public static String convertDate2(String date) {
//		DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.FULL);
        if (TextUtils.isEmpty(date))
            return "";
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(Date.parse(date));
    }

    /**
     * 将日期转换成xxxx-xx-xx hh:mm:ss的格式
     */
    public static String convertDate3(String date) {
//		DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.FULL);
        if (TextUtils.isEmpty(date))
            return "";
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(Date.parse(date));
    }

    /**
     * 将日期转换成xxxx-xx-xx的格式
     */
    public static String convertDate2(Date date) {
//		DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.FULL);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }

    /**
     * 将日期转换成xxxx-xx-xx的格式
     */
    public static String convertDate3(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(date);
    }

    public static int[] calculateDate() {
        Calendar calendar = Calendar.getInstance();
        return new int[]{calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DATE)};
    }

}
