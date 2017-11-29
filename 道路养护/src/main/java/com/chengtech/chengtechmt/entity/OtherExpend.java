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
import java.util.ArrayList;
import java.util.List;

/**
 * 资金支出
 * 作者: LiuFuYingWang on 2016/7/7 17:21.
 */
public class OtherExpend implements Serializable {

    public Presenter presenter;
    public List<String> propertyValue;
    public String projectId;//项目ID
    public String projectName;//项目名称
    public String money;//金额
    public String applyDate;//申请时间
    public String memo;
//    public String sessionName;
    public String sessionId;
    public String expendType; //支出类型
    public String expendContractRate; //支出所占比率

    
    public OtherExpend (Presenter presenter) {
        this.presenter = presenter;
    }

    public List<String> getList(){
        if (propertyValue==null) {
            propertyValue = new ArrayList<>();
            propertyValue.add(projectName==null?"":projectName);
            propertyValue.add(money==null?"":money);
            propertyValue.add(applyDate==null?"": DateUtils.convertDate(applyDate));
            propertyValue.add(memo==null?"":memo);
            propertyValue.add(sessionId==null?"":sessionId);
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
            public void onSuccess(int i, cz.msebera.android.httpclient.Header[] headers, byte[] bytes) {
                try {
                    String data = new String(bytes, "utf-8");
                    Gson gson  = new Gson();
                    OtherExpendG expendG = gson.fromJson(data, OtherExpendG.class);
                    presenter.loadDataSuccess(expendG.rows,type);
                } catch (Exception e) {
                    presenter.loadDataFailed();
                }
            }

            @Override
            public void onFailure(int i, cz.msebera.android.httpclient.Header[] headers, byte[] bytes, Throwable throwable) {
                presenter.loadDataFailed();
            }

        };

        client.get(url,
                responseHandler);

    }

    public class OtherExpendG {
        public List<OtherExpend> rows;
        public int totalRows;
    }
}
