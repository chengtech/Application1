package com.chengtech.chengtechmt.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

/**
 * 作者: LiuFuYingWang on 2018/1/11 15:45.
 */

public abstract class BaseFragment2 extends Fragment {
    protected boolean isViewInitiated;
    protected boolean isVisibleToUser;
//    protected boolean isDataInitiated;



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isViewInitiated = true;
        prepareFetchData();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        prepareFetchData();
    }

    public abstract void fetchData();

    public boolean prepareFetchData() {
        return prepareFetchData(false);
    }

    public boolean prepareFetchData(boolean forceUpdate) {
        if (isVisibleToUser && isViewInitiated ) {
            fetchData();
            return true;
        }
        return false;
    }

}
