package com.chengtech.chengtechmt.presenter;

import android.content.Context;

import com.chengtech.chengtechmt.activity.IView;
import com.chengtech.chengtechmt.entity.Route;

import java.util.List;

/**
 * 作者: LiuFuYingWang on 2016/4/28 15:30.
 */
public class RoutePresenter implements Presenter<IView,List<Route>> {

    private IView routeView;
    private Route route;


    public RoutePresenter(IView view) {
        attachView(view);
        route = new Route(this);

    }

    @Override
    public void attachView(IView view) {
        this.routeView = view;
    }

    @Override
    public void detchView(IView iView) {
        routeView = null;
    }

    @Override
    public void loadDataSuccess(List<Route> routes) {
        routeView.dismssDialog();
        routeView.loadDataSuccess(routes);
    }

    @Override
    public void loadDataSuccess(List<Route> routes, int type) {

    }

    @Override
    public void loadDataSuccess(List<Route> routes, String className) {

    }

    @Override
    public void loadDataFailed() {
        routeView.dismssDialog();
        routeView.loadDataFailure();
    }

    @Override
    public void hasError() {
        routeView.hasError();
    }

    public void getData(Context context,int pageNo,int pageSize) {
        routeView.showDialog();
        route.getData(context,pageNo,pageSize);
    }

    public Route getRoute() {
        return route;
    }

}
