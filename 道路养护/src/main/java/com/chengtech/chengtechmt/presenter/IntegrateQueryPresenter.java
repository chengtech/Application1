package com.chengtech.chengtechmt.presenter;


import android.content.Context;

import com.chengtech.chengtechmt.activity.IView;
import com.chengtech.chengtechmt.entity.Menu;

import java.util.List;

/**
 * 作者: LiuFuYingWang on 2016/6/27 9:21.
 */
public class IntegrateQueryPresenter implements Presenter<IView,List<Menu>> {

    public IView integrateQueryActivity;
    public Menu menu;

    public IntegrateQueryPresenter(IView iView) {
        attachView(iView);
        menu = new Menu(this);
    }

    @Override
    public void attachView(IView view) {
        this.integrateQueryActivity = view;
    }

    @Override
    public void detchView(IView view) {
        view = null;
    }

    @Override
    public void loadDataSuccess(List<Menu> menuList) {
        integrateQueryActivity.dismssDialog();
        integrateQueryActivity.loadDataSuccess(menuList);
    }

    @Override
    public void loadDataSuccess(List<Menu> menuList, int type) {

    }

    @Override
    public void loadDataSuccess(List<Menu> menus, String className) {

    }

    @Override
    public void loadDataFailed() {
        integrateQueryActivity.dismssDialog();
        integrateQueryActivity.loadDataFailure();
    }

    @Override
    public void hasError() {
        integrateQueryActivity.dismssDialog();
        integrateQueryActivity.hasError();
    }

    public void getData(Context context,String id ,String url) {
        integrateQueryActivity.showDialog();
        menu.getData(context,id,url);
    }
}
