package com.chengtech.chengtechmt.entity.gson;

import android.content.Context;

import com.chengtech.chengtechmt.entity.Dept;
import com.chengtech.chengtechmt.presenter.Presenter;
import com.chengtech.chengtechmt.util.HttpclientUtil;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.util.List;

/**
 * 作者: LiuFuYingWang on 2016/6/28 15:08.
 */
public class DeptG {

    public List<Dept> listFirstDept;
    public List<Dept> listSecondDept;
    public List<Dept> listThirdDept;

    public Presenter presenter;

    public DeptG(Presenter presenter) {
        this.presenter = presenter;
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
                    data = data.replace("{\"entity\":","");
                    data = data.substring(0,data.length()-1);
                    DeptG deptg = gson.fromJson(data,DeptG.class);
                    presenter.loadDataSuccess(deptg,type);
                } catch (Exception e) {
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
}
