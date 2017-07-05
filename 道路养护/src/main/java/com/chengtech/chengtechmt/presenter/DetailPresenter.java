package com.chengtech.chengtechmt.presenter;

import android.content.Context;

import com.chengtech.chengtechmt.activity.IView;
import com.chengtech.chengtechmt.entity.Attachment;
import com.chengtech.chengtechmt.entity.ResultBean;

/**
 * 作者: LiuFuYingWang on 2016/5/12 14:34.
 */
public class DetailPresenter implements Presenter<IView,Object>{

    public IView detailActivity;
    public ResultBean bean;
    public Attachment attachment;


    public DetailPresenter(IView view) {
        attachView(view);
        bean = new ResultBean(this);
        attachment = new Attachment(this);

    }

    @Override
    public void attachView(IView view) {
        this.detailActivity = view;
    }

    @Override
    public void detchView(IView view) {
        view=null;
    }

    @Override
    public void loadDataSuccess(Object result) {
        detailActivity.dismssDialog();
        detailActivity.loadDataSuccess(result);
    }

    @Override
    public void loadDataSuccess(Object resultBean, int type) {

    }

    @Override
    public void loadDataSuccess(Object object, String className) {
        detailActivity.dismssDialog();
        detailActivity.loadDataSuccess(object, className);
    }

    @Override
    public void loadDataFailed() {
        detailActivity.dismssDialog();
        detailActivity.loadDataFailure();
    }

    @Override
    public void hasError() {
        detailActivity.hasError();
    }


    public void getData(Context context,String url,String json) {
        detailActivity.showDialog();
        bean.getData(context,url,json);
    }

    public void getData(Context context,String sessionId) {
        detailActivity.showDialog();
        attachment.getData(context,sessionId);
    }
}
