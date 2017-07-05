package com.chengtech.chengtechmt.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.chengtech.chengtechmt.BaseActivity;
import com.chengtech.chengtechmt.R;
import com.chengtech.chengtechmt.util.HttpclientUtil;
import com.chengtech.chengtechmt.util.MyConstants;
import com.chengtech.chengtechmt.view.TitleLayout;
import com.loopj.android.http.AsyncHttpClient;

import org.apache.http.client.CookieStore;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.cookie.Cookie;

import java.util.List;

public class WebViewActivity extends Activity {

    private WebView webView; // 浏览器
    private String target_url;
    private ProgressBar webview_progressbar;
    private TitleLayout layout;
    private String url;
    CookieManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        initView();

        Intent intent = getIntent();
        layout.setTitle(intent.getStringExtra("title"));
//        layout.setVisibility(View.GONE);

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

        });

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                webview_progressbar.setProgress(newProgress);
                if (newProgress == 100) {
                    webview_progressbar.setVisibility(View.GONE);
                }
                super.onProgressChanged(view, newProgress);
            }
        });

        webView.setOnKeyListener(new OnKeyListener() {

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

    private void initView() {
        webView = (WebView) findViewById(R.id.webviewdetail_webview);
        webview_progressbar = (ProgressBar) findViewById(R.id.webview_progressbar);

        layout = (TitleLayout) findViewById(R.id.mytitle);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        manager.removeSessionCookie();
    }
}
