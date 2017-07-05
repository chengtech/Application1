package com.chengtech.chengtechmt.entity;

import android.content.Context;

import com.chengtech.chengtechmt.presenter.Presenter;
import com.chengtech.chengtechmt.util.DateUtils;
import com.chengtech.chengtechmt.util.HttpclientUtil;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

/**
 * 资金来源实体类
 * 作者: LiuFuYingWang on 2016/7/7 16:32.
 */
public class CapitalSource implements Serializable {

    public Presenter presenter;
    public List<String> propertyValue;
    public String referenceNumber;//文号
    public String moneyCounts;//资金数量
    public String capitalSoureDate; // 资金来源日期
    public String projectId;//项目ID
    public String surplusMoney;  //剩余金额资金
    public String memo;
    public String sessionName;

    public CapitalSource (Presenter presenter) {
        this.presenter = presenter;
    }

    public List<String> getList(){
        if (propertyValue==null) {
            propertyValue = new ArrayList<>();
            propertyValue.add(referenceNumber==null?"":referenceNumber);
            propertyValue.add(moneyCounts==null?"":moneyCounts);
            propertyValue.add(capitalSoureDate==null?"": DateUtils.convertDate(capitalSoureDate));
            propertyValue.add(memo==null?"":memo);
            propertyValue.add(sessionName==null?"":sessionName);
        }

        return propertyValue;
    }

    public void getData(Context context, String url, final String type) {
        AsyncHttpClient client = HttpclientUtil.getInstance(context);

        AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                try {
                    String data = new String(arg2, "utf-8");
                    Gson gson  = new Gson();
                    CapitalSourceG sourceG = gson.fromJson(data, CapitalSourceG.class);
                    presenter.loadDataSuccess(sourceG.rows,type);
                } catch (UnsupportedEncodingException e) {
                    presenter.hasError();
                    e.printStackTrace();
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

        client.get(url,
                responseHandler);

    }


    public class CapitalSourceG {
        public List<CapitalSource> rows;
        public int totalRows;


    }
}
