package com.chengtech.etcdemo;

import android.content.Context;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 作者: LiuFuYingWang on 2017/4/13 16:32.
 * 网络请求对象retrofit单例
 */

public class HttpUtil {

    private static Retrofit retrofit;
    private static String BASE_URL = "";

    public static synchronized Retrofit getInstance() {

        if (retrofit == null) {
            retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(BASE_URL).build();
        }
        return retrofit;
    }

    public static void changUrl(String baseUrl) {
        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .build();
    }
}
