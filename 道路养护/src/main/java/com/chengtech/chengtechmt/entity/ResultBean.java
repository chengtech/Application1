package com.chengtech.chengtechmt.entity;

import android.content.Context;

import com.chengtech.chengtechmt.presenter.DetailPresenter;
import com.chengtech.chengtechmt.util.HttpclientUtil;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.io.UnsupportedEncodingException;

/**
 * 作者: LiuFuYingWang on 2016/5/12 14:40.
 */
public class ResultBean {

    public DetailPresenter presenter;
    public boolean success;
    public String msg;



    public ResultBean(DetailPresenter presenter) {
        this.presenter = presenter;
    }


    public void getData(Context context,String url,String json){
        AsyncHttpClient client = HttpclientUtil.getInstance(context);
        AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler(){
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {

                try {
                    String data = new String(arg2, "utf-8");
                    Gson gson  = new Gson();
                    ResultBean bean = gson.fromJson(data,ResultBean.class);
                    presenter.loadDataSuccess(bean);
//
                } catch (Exception e) {
                    presenter.hasError();
                }
                super.onSuccess(arg0, arg1, arg2);
            }

            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2,
                                  Throwable arg3) {
                presenter.loadDataFailed();
                super.onFailure(arg0, arg1, arg2, arg3);
            }
        };
        RequestParams params = new RequestParams();
        params.add("jsonData",json);
        client.post(url,params,responseHandler);

    }
}
