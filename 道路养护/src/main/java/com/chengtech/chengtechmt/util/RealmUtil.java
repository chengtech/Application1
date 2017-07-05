package com.chengtech.chengtechmt.util;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.PersistentCookieStore;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * 作者: LiuFuYingWang on 2016/12/28 16:18.
 * 创建统一realm数据库操作对象
 */

public class RealmUtil {


    public static synchronized Realm getInstance(Context context) {
        RealmConfiguration config = new RealmConfiguration.Builder(context).name("chengtechmt").deleteRealmIfMigrationNeeded().build();
        Realm realm = Realm.getInstance(config);
        return realm;
    }
}
