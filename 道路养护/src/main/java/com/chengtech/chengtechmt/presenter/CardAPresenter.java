package com.chengtech.chengtechmt.presenter;

import android.content.Context;

import com.chengtech.chengtechmt.activity.IView;
import com.chengtech.chengtechmt.entity.Attachment;
import com.chengtech.chengtechmt.entity.ResultBean;

/**
 * 作者: LiuFuYingWang on 2016/5/12 14:34.
 */
public class CardAPresenter implements Presenter<IView,Object>{

    public IView iview;
    public Attachment attachment;


    public CardAPresenter(IView view) {
        attachView(view);
        attachment = new Attachment(this);

    }

    @Override
    public void attachView(IView view) {
        this.iview = view;
    }

    @Override
    public void detchView(IView view) {
        view=null;
    }

    @Override
    public void loadDataSuccess(Object result) {
        iview.dismssDialog();
        iview.loadDataSuccess(result);
    }

    @Override
    public void loadDataSuccess(Object resultBean, int type) {

    }

    @Override
    public void loadDataSuccess(Object object, String className) {
        iview.dismssDialog();
        iview.loadDataSuccess(object, className);
    }

    @Override
    public void loadDataFailed() {
        iview.dismssDialog();
        iview.loadDataFailure();
    }

    @Override
    public void hasError() {
        iview.hasError();
    }


    public void getData(Context context,String url,String json) {
    }

    public void getData(Context context,String sessionId) {
        iview.showDialog();
        attachment.getData(context,sessionId);
    }
}
