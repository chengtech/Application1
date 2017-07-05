package com.chengtech.chengtechmt.presenter;

import android.content.Context;

import com.chengtech.chengtechmt.activity.IView;
import com.chengtech.chengtechmt.entity.expertdecision.BitumenRoadDamage;
import com.chengtech.chengtechmt.entity.expertdecision.CementRoadDamage;
import com.chengtech.chengtechmt.entity.expertdecision.Evaluationdetail;

/**
 * 作者: LiuFuYingWang on 2016/12/29 11:03.
 */

public class ExpertDecisionPresenter implements Presenter<IView,Object> {

    private IView view;
    private BitumenRoadDamage bitumenRoadDamage;
    private CementRoadDamage cementRoadDamage;
    private Evaluationdetail evaluationdetail;

    public ExpertDecisionPresenter(IView view,String type) {
        attachView(view);
        switch(type) {
            case "BitumenRoadDamage":
                bitumenRoadDamage = new BitumenRoadDamage(this);
                break;
            case "CementRoadDamage":
                cementRoadDamage = new CementRoadDamage(this);
                break;
            case "Evaluationdetail" :
                evaluationdetail = new Evaluationdetail(this);
                break;

        }
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
    public void loadDataSuccess(Object object, String className) {
        view.dismssDialog();
        view.loadDataSuccess(object,className);
    }

    @Override
    public void loadDataFailed() {
        view.dismssDialog();
    }

    @Override
    public void hasError() {
        view.dismssDialog();
    }

    public void getData(Context context, String urlParams, String type){
        view.showDialog();
        switch (type) {
            case "BitumenRoadDamage" :
                bitumenRoadDamage.getData(context,urlParams);
                break;
            case "CementRoadDamage" :
                cementRoadDamage.getData(context,urlParams);
                break;
            case "Evaluationdetail":
                evaluationdetail.getData(context,urlParams);
                break;
        }
    }
}
