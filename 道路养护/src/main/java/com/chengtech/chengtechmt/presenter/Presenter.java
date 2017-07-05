package com.chengtech.chengtechmt.presenter;

/**
 * Created by liufuyingwang on 2016/3/21.
 */
public interface Presenter<V,T> {

    public void attachView(V v);

    public void detchView(V v);

    public void loadDataSuccess(T t);

    public void loadDataSuccess(T t,int type);
    public void loadDataSuccess(T t,String className);

    public void loadDataFailed();

    public void hasError();
}
