package com.chengtech.chengtechmt.activity.integratequery.gis.MaintenanceSituation;


import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.CoordinateConverter;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MarkerOptions;
import com.chengtech.chengtechmt.R;
import com.chengtech.chengtechmt.fragment.BaseFragment2;
import com.chengtech.chengtechmt.util.HttpclientUtil;
import com.chengtech.chengtechmt.util.MyConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 作者: LiuFuYingWang on 2018/1/9 14:18.
 */

public class MainFragment extends BaseFragment2 {
//    String url1 = MyConstants.PRE_URL + "mt/integratequery/maintenancesituation/mediumAndProjectEchartIndex.action?";
    String url2 = MyConstants.PRE_URL + "mt/integratequery/maintenancesituation/getProjectMapMsg.action?";
    private WebView webView;
    private MapView mapView;
    private View rootView;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String json = (String) msg.obj;
            switch (msg.what) {
                case 0x01:
                    try {
                        mapView.getMap().clear();
                        JSONArray jsonArray = new JSONArray(json);
                        if (jsonArray.length() > 0) {
                            mapView.getMap().clear();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String x = jsonObject.getString("x");
                                String y = jsonObject.getString("y");
                                String name = jsonObject.getString("name");
                                LatLng latLng = new LatLng(Double.parseDouble(y), Double.parseDouble(x));
                                CoordinateConverter converter = new CoordinateConverter();
                                converter.from(CoordinateConverter.CoordType.GPS);
                                converter.coord(latLng);
                                LatLng convert = converter.convert();

                                int iconId = R.mipmap.ic_launcher; //默认
                                String snippet = jsonArray.getString(i);
                                if(snippet.contains("green-small")) {
                                    iconId = R.mipmap.green_small;
                                }else if (snippet.contains("green-big")) {
                                    iconId = R.mipmap.green_big;
                                }else if (snippet.contains("red-small")) {
                                    iconId = R.mipmap.red_small;
                                }else if (snippet.contains("red-big")) {
                                    iconId = R.mipmap.red_big;
                                }
                                snippet = snippet.substring(snippet.indexOf("\"项目名称"), snippet.lastIndexOf(","));
                                snippet = snippet.replace(",", ",\n");
                                snippet = snippet.replace("\"","");

                                mapView.getMap().addMarker(new MarkerOptions().position(convert).title(name).snippet(snippet)
                                .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(),iconId))));
                                if (i == 0) {
                                    mapView.getMap().moveCamera(CameraUpdateFactory.zoomTo(mapView.getMap().getMaxZoomLevel() - 10));
                                    mapView.getMap().moveCamera(CameraUpdateFactory.changeLatLng(convert));
                                }
                            }
                        }
                    } catch (JSONException e) {

                    }
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            rootView = inflater.inflate(R.layout.fragment_main_gis, container, false);
            webView = (WebView) rootView.findViewById(R.id.webView);
            mapView = (MapView) rootView.findViewById(R.id.mapView);


        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapView.onCreate(savedInstanceState);
        //定位到广州
        mapView.getMap().moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(Double.parseDouble("23.118988"),Double.parseDouble("113.285392"))));

    }

    public void getData(String filter) {
        if (getUserVisibleHint() && isVisible()) {
            fetchData();
        }
    }


//    private void setWebViewCookie() {
//        WebSettings ws = webView.getSettings();
////        settings.setJavaScriptEnabled(true);
////        settings.setPluginState(WebSettings.PluginState.ON);
////        settings.setAllowFileAccess(true);
////        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
////        settings.setUseWideViewPort(true);  //将内容调整到控件大小
////        settings.setLoadWithOverviewMode(true);  //将内容调整到屏幕大小
////        settings.setSupportZoom(true);
////        settings.setBuiltInZoomControls(true);
//        ws.setJavaScriptEnabled(true);
//        ws.setPluginState(WebSettings.PluginState.OFF);
//        ws.setAllowFileAccess(true);
////        ws.setCacheMode(WebSettings.LOAD_DEFAULT);
////        ws.setDomStorageEnabled(true);
////        ws.setSupportMultipleWindows(true);
//        ws.setCacheMode(WebSettings.LOAD_DEFAULT);
//        ws.setDomStorageEnabled(true);
////        ws.setAppCacheEnabled(true);
//        ws.setSupportZoom(true);
//        ws.setBuiltInZoomControls(true);
////        ws.setUseWideViewPort(true);  //将内容调整到控件大小
////        ws.setLoadWithOverviewMode(true);  //将内容调整到屏幕大小
////        ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS); //支持将内容重新布局
////        webView.setWebViewClient(new WebViewClient(){
////            @Override
////            public boolean shouldOverrideUrlLoading(WebView view, String url) {
////                view.loadUrl(url);
////                return true;
////            }
////        });
//    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mapView.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mapView.onPause();
    }

    @Override
    public void fetchData() {
        MaintenanceSituationActivity activity = (MaintenanceSituationActivity) getActivity();
        if (!TextUtils.isEmpty(activity.filter)){
            HttpclientUtil.getData(getActivity(), url2 + activity.filter, handler, 0x01);
//            setWebViewCookie();
//            webView.loadUrl(url1 + activity.filter);
        }
    }
}
