package com.chengtech.etc;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * 作者: LiuFuYingWang on 2017/4/18 16:21.
 */

public interface ETCService {
    @GET("url")
    Call<String> getFile0019(@Query("file")String file0015);


}
