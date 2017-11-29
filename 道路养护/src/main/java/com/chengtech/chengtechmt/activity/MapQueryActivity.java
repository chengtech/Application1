package com.chengtech.chengtechmt.activity;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chengtech.chengtechmt.BaseActivity;
import com.chengtech.chengtechmt.R;
import com.chengtech.chengtechmt.activity.integratequery.routequery.RouteQueryActivity;
import com.chengtech.chengtechmt.entity.MapEntity;
import com.chengtech.chengtechmt.entity.Route;
import com.chengtech.chengtechmt.fragment.GISMenuDialogFragment;
import com.chengtech.chengtechmt.util.HttpclientUtil;
import com.chengtech.chengtechmt.util.MyConstants;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;

import org.apache.http.client.protocol.ClientContext;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.client.CookieStore;
import cz.msebera.android.httpclient.cookie.Cookie;

public class MapQueryActivity extends BaseActivity implements GISMenuDialogFragment.ExchangeDataListener {
    private WebView webView;
    private boolean isFirst = false;
    String url;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String json = (String) msg.obj;
            Gson gson = new Gson();

            try {
                switch (msg.what) {
                    case 1:
                        isFirst = false;
                        onExchangData(json);
                        break;
                    default:
                        JSONObject jsonObject = new JSONObject(json);
                        if (jsonObject.getBoolean("success")) {
                            JSONObject dataObject = jsonObject.getJSONObject("data");
                            String data = dataObject.toString();
                            Route route = gson.fromJson(data, Route.class);
                            Intent intent = new Intent(MapQueryActivity.this, RouteQueryActivity.class);
                            intent.putExtra("data", route);
                            startActivity(intent);
                        }
                        break;
                }

            } catch (Exception e) {

            }

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        addContentView(R.layout.web);

//        btnBack = (RelativeLayout) findViewById(R.id.left_area);
//        right_tv = (RelativeLayout) findViewById(R.id.right_area);
//        txtTitle = (TextView) findViewById(R.id.titleTxt);
//        txtTitle.setText("地图查询定位");
        webView = (WebView) findViewById(R.id.web);
        toolbar.setTitle("地图查询定位");
        setNavigationIcon(true);
        hidePagerNavigation(true);

        //获取参数
        Intent intent = getIntent();
        isFirst = intent.getBooleanExtra("isFirstLoad", false);
//		//基础数据查询功能中的定位，传入layer、code变量
//		String layer = intent.getStringExtra("layer");
//		String code = intent.getStringExtra("code");
//
//		//日常巡查采集功能中绘制图像，draw变量传入字符串"1"
//		String draw = intent.getStringExtra("draw");
//
//		//病害数据查询功能中进行定位，传入poi变量
//		String poi = intent.getStringExtra("poi");
        //基础数据查询功能中的定位，传入layer、code变量
        MapEntity mapEntity = (MapEntity) intent.getSerializableExtra("map");
        String layer = mapEntity == null ? null : mapEntity.layer;
        String code = mapEntity == null ? null : mapEntity.code;


        //日常巡查采集功能中绘制图像，draw变量传入字符串"1"
        String draw = mapEntity == null ? null : mapEntity.draw;

        //病害数据查询功能中进行定位，传入poi变量
        String poi = mapEntity == null ? null : mapEntity.poi;

        //路段查询参数
        String lineData = mapEntity == null ? null : mapEntity.lineData;

        //定位查询参数
        String xyData = mapEntity == null ? null : mapEntity.xy;

        url = MyConstants.PRE_URL + "map/appgis/query.html";

        if (layer != null && layer.length() > 0) {
            url += "?layer=" + layer + "&code=" + code;
        } else if (draw != null && draw.length() > 0) {
            url += "?draw=1";
        } else if (poi != null && poi.length() > 0) {
            url += "?poi=" + poi;
        }

		/*
        * 路段查询，直接在URL中传入data参数，
		* 或者页面加载完成后调用js方法： function queryPath(data)
		*/
        if (lineData != null && lineData.length() > 0) {
            if (url.indexOf("?") < 0) {
                url += "?data=" + lineData;
            } else {
                url += "&data=" + lineData;
            }
        }

        /**
         * 定位坐标点
         */
        if (xyData != null && xyData.length() > 0) {
            if (url.indexOf("?") < 0) {
                url += "?xy=" + xyData;
            } else {
                url += "&xy=" + xyData;
            }
        }

        WebSettings ws = webView.getSettings();
        ws.setJavaScriptEnabled(true);
        ws.setAllowFileAccess(true);
        ws.setCacheMode(WebSettings.LOAD_NO_CACHE);
        ws.setDomStorageEnabled(true);
        ws.setAppCacheEnabled(true);
        ws.setSupportZoom(true);
        ws.setBuiltInZoomControls(true);

        setWebViewCookie();
        //让js能调用这里的方法
        webView.addJavascriptInterface(this, "javato");

        webView.loadUrl(url);


    }

    /*
     * 地图页面绘制完成后，js调用这个方法，在这里得到绘制图形的json字符串，保存起来。
     * 后面定位的时候poi参数就是传入这个json
     */
    @JavascriptInterface
    public void OnDrawFinish(String json) {
        Log.i("tag", "JS回调:");
    }

    @JavascriptInterface
    public void OnOpenURL(String url) {
        url = url.substring(url.indexOf("=")+1);
        url = url.replace("...","?");
        url = url.replace("***","&");
        url = url + "&mobile=phone";
        HttpclientUtil.getData(this, MyConstants.PRE_URL+url, handler, 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_map_query, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.evaluate) {
            GISMenuDialogFragment dialogFragment = new GISMenuDialogFragment();
            dialogFragment.show(getFragmentManager(), "GIS");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onExchangData(Object object) {
        if (object != null) {
            String mapdata = (String) object;
            webView.loadUrl("javascript:queryPath('" + mapdata + "')");
        }
    }

    private void setWebViewCookie() {
        String sessionId = null;
        AsyncHttpClient client = HttpclientUtil.getInstance(this);
        CookieStore cookieStore = (CookieStore) client.getHttpContext()
                .getAttribute(ClientContext.COOKIE_STORE);
        List<Cookie> cookies = cookieStore.getCookies();
        for (Cookie c : cookies) {
            if ("JSESSIONID".equals(c.getName())) {
                sessionId = c.getValue();
            }
        }
        CookieSyncManager.createInstance(this);
        CookieManager manager = CookieManager.getInstance();
        manager.setCookie(MyConstants.LOGIN_URL, "JSESSIONID=" + sessionId);
        CookieSyncManager.getInstance().sync();
        //webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                //在一开始进来就加载一条数据
                if (isFirst) {
//                    HttpclientUtil.getData(MapQueryActivity.this, MyConstants.PRE_URL + "mt/integratequery/gisvisualization/getEvaluateMsg.action?routeGrade=&routeCode=&evaluateIndex=PQI&year=",
//                            handler, 1);

                    HttpclientUtil.getData(MapQueryActivity.this, MyConstants.PRE_URL + "mt/integratequery/gisevaluation/getGisEvaluationMsg.action?routeGrade=&routeCode=&evaluateIndex=PQI&year=",
                            handler, 1);
                }
            }
        });


        webView.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()
                        && event.getAction() == KeyEvent.ACTION_DOWN) { // 表示按返回键
                    // 时的操作
                    webView.goBack(); // 后退
                    return true; // 已处理
                }
                return false;
            }
        });

    }


}
