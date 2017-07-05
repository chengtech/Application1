package com.chengtech.chengtechmt.util;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.sql.Ref;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CookieJar;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 作者: LiuFuYingWang on 2016/11/22 15:55.
 * 进行retrofit类的一些初始化工作
 */

public class RetrofitUtil {
    public static int DEFAULT_TIMEOUT = 10;
    public static Cache cache;
    public static Interceptor interceptor;
    public static OkHttpClient httpClient;
    public static Retrofit retrofit;

    public static void changUrl(String baseUrl) {
        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(httpClient)
                .build();
    }

    public static <S> S createService(Class<S> serviceClass) {
        return retrofit.create(serviceClass);
    }

    public static synchronized void getInstance(Context context) {
        //该方法最好在application中进行创建使用，
        if (httpClient == null) {
            createCacheFile(context);
            createCacheInterceprot(context);
            httpClient = new OkHttpClient.Builder()
                    .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                    .addNetworkInterceptor(interceptor)
                    .cache(cache)
                    .build();
        }
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(MyConstants.PRE_URL)
                    .client(httpClient)
                    .build();
        }
    }

    private static void createCacheFile(Context context) {
        File cacheFile = new File(context.getCacheDir(), "httpCache");
        int cacheSize = 10 * 1024 * 1024;
        if (cache == null) {
            cache = new Cache(cacheFile, cacheSize);
        }
    }

    private static void createCacheInterceprot(final Context context) {
        if (interceptor == null) {
            interceptor = new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request request = chain.request();
                    Response response = chain.proceed(request);
                    if (NetWorkUtil.isNetworkAvailable(context)) {
                        String cacheControl = request.cacheControl().toString();
                        return response.newBuilder()
                                .removeHeader("Pragma")//清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                                .addHeader("Cache-Control", cacheControl)
                                .build();
                    }
                    return response;
                }
            };
        }

    }


}
