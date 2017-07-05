package com.chengtech.chengtechmt.fragment;

import android.support.v4.app.Fragment;

import com.chengtech.chengtechmt.util.HttpclientUtil;

/**
 * 作者: LiuFuYingWang on 2016/12/15 10:50.
 */

public abstract class BaseFragment extends Fragment {

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isVisible()) {
           onLazyLoad();
        }
    }

    protected abstract void onLazyLoad() ;
}
