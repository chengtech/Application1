package com.chengtech.chengtechmt.activity;

/**
 * Created by liufuyingwang on 2016/3/21.
 */
public interface IView<T> {

    public void showDialog();

    public void dismssDialog();

    public void loadDataSuccess(T t);

    public void loadDataSuccess(T t,int type);

    public void loadDataSuccess(T t,String clasName);


    public void loadDataFailure();

    public void hasError();
}
