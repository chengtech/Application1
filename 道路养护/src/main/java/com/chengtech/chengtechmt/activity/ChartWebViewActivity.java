package com.chengtech.chengtechmt.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chengtech.chengtechmt.R;
import com.chengtech.chengtechmt.util.HttpclientUtil;
import com.chengtech.chengtechmt.util.MyConstants;
import com.chengtech.chengtechmt.view.TitleLayout;
import com.loopj.android.http.AsyncHttpClient;

import org.apache.http.client.protocol.ClientContext;

import java.util.List;

import cz.msebera.android.httpclient.client.CookieStore;
import cz.msebera.android.httpclient.cookie.Cookie;

public class ChartWebViewActivity extends AppCompatActivity {
    private WebView webView;
    private ProgressBar webview_progressbar;
    private CookieManager manager;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart_web_view);
        toolbar = (Toolbar) findViewById(R.id.toolbar);


        Intent intent = getIntent();
        toolbar.setTitle(intent.getStringExtra("title"));
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.arrow_back2);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        initView();
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setPluginState(WebSettings.PluginState.ON);
        settings.setAllowFileAccess(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setUseWideViewPort(true);  //将内容调整到控件大小
        settings.setLoadWithOverviewMode(true);  //将内容调整到屏幕大小
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS); //支持将内容重新布局
        // settings.setUseWideViewPort(true);
        // settings.setLoadWithOverviewMode(true);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        // 设置cookie
        setWebViewCookie();
        webView.loadUrl(intent.getStringExtra("url"));
    }

    private void initView() {
        webView = (WebView) findViewById(R.id.webviewdetail_webview);
        webview_progressbar = (ProgressBar) findViewById(R.id.webview_progressbar);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case 0x90:
                toolbar.setTitle("设施量统计");
                webView.loadUrl(MyConstants.PRE_URL + "ms/sys/homechart/maintenanceBusinessEchart.action");
                break;
            case 0x91:
                toolbar.setTitle("道路、桥梁分析");
                webView.loadUrl(MyConstants.PRE_URL + "ms/sys/homechart/dbmSectionAndBridgeEchart.action");
                break;
            case 0x92:
                toolbar.setTitle("养护业务-资金数据分析");
                webView.loadUrl(MyConstants.PRE_URL + "ms/sys/homechart/projectCapitalEchart.action");
                break;
            case 0x93:
                toolbar.setTitle("养护业务数据提交及时率");
                webView.loadUrl(MyConstants.PRE_URL + "ms/sys/homechart/maintenanceScheduleEchart.action");
                break;
            case 0x94:
                toolbar.setTitle("机械设备年度利用率、完好率");
                webView.loadUrl(MyConstants.PRE_URL + "ms/sys/homechart/equipmentRunRecordEchart.action");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 0x90, 90, "设施量统计").setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        menu.add(0, 0x91, 91, "道路、桥梁分析").setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        menu.add(0, 0x92, 92, "养护业务-资金数据分析").setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        menu.add(0, 0x93, 93, "养护业务数据提交及时率").setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        menu.add(0, 0x94, 94, "机械设备年度利用率、完好率").setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        return super.onCreateOptionsMenu(menu);
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
        manager = CookieManager.getInstance();
        manager.setCookie(MyConstants.LOGIN_URL, "JSESSIONID=" + sessionId);
        CookieSyncManager.getInstance().sync();
//        webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                webview_progressbar.setVisibility(View.VISIBLE);
                super.onPageStarted(view, url, favicon);
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                webview_progressbar.setProgress(newProgress);
                if (newProgress == 100) {
                    webview_progressbar.setVisibility(View.INVISIBLE);
                }
                super.onProgressChanged(view, newProgress);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        manager.removeSessionCookie();
    }

    public static void startAction(Context context, String url, String toolbarTitle) {
        Intent intent = new Intent(context, ChartWebViewActivity.class);
        intent.putExtra("title", toolbarTitle);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }

}
