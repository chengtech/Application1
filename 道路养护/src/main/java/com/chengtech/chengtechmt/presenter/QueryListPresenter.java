package com.chengtech.chengtechmt.presenter;

import android.content.Context;

import com.chengtech.chengtechmt.activity.IView;
import com.chengtech.chengtechmt.entity.Route;
import com.chengtech.chengtechmt.entity.Tree;

/**
 * 作者: LiuFuYingWang on 2016/7/21 17:16.
 */
public class QueryListPresenter implements Presenter<IView, Object> {

    public IView view;
    public Tree tree;
    public Route route;


    public QueryListPresenter(IView view, String type) {
        attachView(view);
        switch (type) {
            case "Route" :
                route = new Route(this);
                break;
        }
        tree = new Tree(this);
    }

    public void getData(Context context, String url, String type) {
        view.showDialog();
        switch (type) {
            case "Tree":
                tree.getData(context, url);
                break;
            case "Route" :
                route.getData(context,url,type);
                break;
        }
    }


    @Override
    public void attachView(IView view) {
        this.view = view;
    }

    @Override
    public void detchView(IView view) {
        view = null;
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
        view.loadDataSuccess(object, className);
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
}
