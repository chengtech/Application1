package com.chengtech.chengtechmt.activity.integratequery.gis.DiseaseRecord;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.chengtech.chengtechmt.R;
import com.chengtech.chengtechmt.activity.integratequery.gis.MaintenanceSituation.MaintenanceSituationActivity;
import com.chengtech.chengtechmt.fragment.BaseFragment2;
import com.chengtech.chengtechmt.util.MyConstants;

/**
 * 作者: LiuFuYingWang on 2018/1/24 15:04.
 * 病害分布情况
 */

public class HighwayDiseaseFragment extends BaseFragment2 {
    private String url1 = MyConstants.PRE_URL + "mt/integratequery/diseaserecord/highwayDiseaseEcharts.action?";
    private WebView webView;
    private View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null)
            rootView = inflater.inflate(R.layout.fragment_capital_construction, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        webView = (WebView) view.findViewById(R.id.webView);


    }

    public void getData(String filter) {
        if (getUserVisibleHint() && isVisible()) {
            fetchData();
        }
    }

    private void setWebViewCookie() {
        WebSettings ws = webView.getSettings();
        ws.setJavaScriptEnabled(true);
        ws.setAllowFileAccess(true);
        ws.setCacheMode(WebSettings.LOAD_NO_CACHE);
        ws.setDomStorageEnabled(true);
        ws.setAppCacheEnabled(true);
        ws.setSupportZoom(true);
        ws.setBuiltInZoomControls(true);
//        ws.setUseWideViewPort(true);  //将内容调整到控件大小
//        ws.setLoadWithOverviewMode(true);  //将内容调整到屏幕大小
//        ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS); //支持将内容重新布局
    }

    @Override
    public void fetchData() {
        DiseaseRecordActivity activity = (DiseaseRecordActivity) getActivity();
        if (!TextUtils.isEmpty(activity.filter)) {
            if (webView != null) {
                setWebViewCookie();
                webView.loadUrl(url1 + activity.filter);

            }
        }
    }
}
