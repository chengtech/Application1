package com.chengtech.chengtechmt.entity;

import android.content.Context;

import com.chengtech.chengtechmt.presenter.Presenter;
import com.chengtech.chengtechmt.util.DateUtils;
import com.chengtech.chengtechmt.util.HttpclientUtil;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 作者: LiuFuYingWang on 2016/10/11 9:46.
 * 月度检查实体类
 */

public class MaintainRegister extends BaseModel {

    public Presenter presenter;

    public String examineUnit;// 检查单位
    public String byExamineUnit;// 被检查单位
    public String examineSite;// 检查地点
    public String templates;// 指标模板
    public String examineNature;// 检查性质
    public Date examineDate;// 检查时间
    public int examineYear; //受检年
    public String examineNumber;// 检查编号
    public String maintainTargetId;//指标ID
    public String year;//查询年
    public String month;//查询月份

    public MaintainRegister(Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public List<String> getContent() {
        List<String> list = new ArrayList<>();
        list.add(examineSite == null ? "" : examineSite);
        list.add(examineDate == null ? "" : DateUtils.convertDate2(examineDate));
        return list;
    }

    @Override
    public List<String> getTitles() {
        return null;
    }

    @Override
    public List<String> getPropertyName() {
        return null;
    }

    public void getData(Context context, String url, final String type, int pageNo) {
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
                    Gson gson = new Gson();
                    MaintainRegisterG m = gson.fromJson(data, MaintainRegisterG.class);
                    presenter.loadDataSuccess(m.rows, type);
                } catch (Exception e) {
                    presenter.loadDataFailed();
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
        client.get(url + "&pager.pageNo=" + pageNo + "&taskMark=0" + "&direction=desc&sort=examineDate",
                responseHandler);
    }

    public class MaintainRegisterG {
        public List<MaintainRegister> rows;
    }
}
