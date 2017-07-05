package com.chengtech.chengtechmt.presenter;

import android.content.Context;

import com.chengtech.chengtechmt.activity.IView;
import com.chengtech.chengtechmt.entity.Attachment;
import com.chengtech.chengtechmt.entity.CapitalSource;
import com.chengtech.chengtechmt.entity.MediumPlanprogress;
import com.chengtech.chengtechmt.entity.OtherExpend;
import com.chengtech.chengtechmt.entity.ProjectManagement;
import com.chengtech.chengtechmt.entity.gson.DeptG;

/**
 * 作者: LiuFuYingWang on 2016/6/28 15:04.
 */
public class ProjectManagerPre implements Presenter<IView, Object> {

    public IView view;
    public CapitalSource capitalSource;
    public OtherExpend otherExpend;
    public Attachment attachment;

    public ProjectManagerPre(IView view) {
        attachView(view);

        capitalSource = new CapitalSource(this);
        otherExpend = new OtherExpend(this);
        attachment = new Attachment(this);
    }

    @Override
    public void attachView(IView iView) {
        this.view = iView;
    }

    @Override
    public void detchView(IView iView) {
        iView = null;
    }

    @Override
    public void loadDataSuccess(Object o) {

    }

    @Override
    public void loadDataSuccess(Object o, int type) {

    }

    @Override
    public void loadDataSuccess(Object o, String className) {
        view.dismssDialog();
        view.loadDataSuccess(o, className);
    }

    @Override
    public void loadDataFailed() {
        view.dismssDialog();
        view.loadDataFailure();
    }

    @Override
    public void hasError() {
        view.dismssDialog();
        view.hasError();
    }

    public void getData(Context context, String url, String className) {
        view.showDialog();
        switch (className) {
            case "CapitalSource":
                capitalSource.getData(context, url, className);
                break;
            case "OtherExpend" :
                otherExpend.getData(context,url,className);
                break;
            case "Attachment" :
                attachment.getData(context,url);
                break;

        }
    }
}
