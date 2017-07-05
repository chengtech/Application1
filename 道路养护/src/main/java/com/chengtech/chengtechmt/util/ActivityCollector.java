package com.chengtech.chengtechmt.util;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者: LiuFuYingWang on 2017/3/13 15:23.
 */

public class ActivityCollector {

    public static List<Activity> activityList = new ArrayList<>();

    public static void addActivity(Activity activity) {
        activityList.add(activity);
    }

    public static void removeActivity(Activity activity) {
        activityList.remove(activity);
    }

    public static void finishAll() {
        for (Activity activity :activityList) {
            if (!activity.isFinishing())
                activity.finish();
        }
    }
}
