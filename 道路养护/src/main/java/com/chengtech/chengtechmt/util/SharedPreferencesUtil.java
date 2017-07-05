package com.chengtech.chengtechmt.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 作者: LiuFuYingWang on 2016/9/5 18:26.
 */
public class SharedPreferencesUtil {


    //存储的sharedpreferences文件名
    private static final String FILE_NAME = "mySharePreference01";

    private static String SPECIAL_DIVIDER = ",";

    /**
     * 保存数据到文件
     *
     * @param context
     * @param key
     * @param data
     */
    public static void saveData(Context context, String key, Object data) {

        String type = data.getClass().getSimpleName();
        SharedPreferences sharedPreferences = context
                .getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if ("Integer".equals(type)) {
            editor.putInt(key, (Integer) data);
        } else if ("Boolean".equals(type)) {
            editor.putBoolean(key, (Boolean) data);
        } else if ("String".equals(type)) {
            editor.putString(key, (String) data);
        } else if ("Float".equals(type)) {
            editor.putFloat(key, (Float) data);
        } else if ("Long".equals(type)) {
            editor.putLong(key, (Long) data);
        }

        editor.commit();
    }

    /**
     * 从文件中读取数据
     *
     * @param context
     * @param key
     * @param defValue
     * @return
     */
    public static Object getData(Context context, String key, Object defValue) {

        String type = defValue.getClass().getSimpleName();
        SharedPreferences sharedPreferences = context.getSharedPreferences
                (FILE_NAME, Context.MODE_PRIVATE);

        //defValue为为默认值，如果当前获取不到数据就返回它
        if ("Integer".equals(type)) {
            return sharedPreferences.getInt(key, (Integer) defValue);
        } else if ("Boolean".equals(type)) {
            return sharedPreferences.getBoolean(key, (Boolean) defValue);
        } else if ("String".equals(type)) {
            return sharedPreferences.getString(key, (String) defValue);
        } else if ("Float".equals(type)) {
            return sharedPreferences.getFloat(key, (Float) defValue);
        } else if ("Long".equals(type)) {
            return sharedPreferences.getLong(key, (Long) defValue);
        }

        return null;
    }

    public static void saveList(Context context,String key,List<String> data) {
        if (data!=null && data.size()>0) {
            StringBuffer sb = new StringBuffer();
            for (int i=0;i<data.size();i++) {
                sb.append(data.get(i));
                sb.append(SPECIAL_DIVIDER);
            }
            saveData(context,key,sb.toString());
        }
    }

    public static List<String> getList(Context context, String key, Object defValue){
        List<String > list = new ArrayList<>();
        String content = (String) getData(context,key,defValue);
        if (TextUtils.isEmpty(content)) {
            return null;
        }
        String [] array = content.split(SPECIAL_DIVIDER);
        for (int i=0;i<array.length;i++) {
            list.add(array[i]);
        }
        return list;

    }

}

