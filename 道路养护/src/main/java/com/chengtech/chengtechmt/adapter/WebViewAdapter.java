package com.chengtech.chengtechmt.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.loopj.android.http.AsyncHttpClient;

import org.apache.http.client.protocol.ClientContext;

import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.client.CookieStore;
import cz.msebera.android.httpclient.cookie.Cookie;

/**
 * 作者: LiuFuYingWang on 2017/10/27 11:15.
 */

public class WebViewAdapter extends RecyclerView.Adapter {
    private List<String> data;
    private List<String> titles;
    private Context mContext;


    public WebViewAdapter(List<String> data, List<String> titles) {
        this.data = data;
        this.titles = titles;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null)
            mContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_chart_web_view, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder viewHolder = (MyViewHolder) holder;
        String url = data.get(position);
        String title = titles.get(position);
        viewHolder.webView.loadUrl(url);
        viewHolder.title.setText(title);
        viewHolder.title.setVisibility(View.VISIBLE);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {
        private WebView webView;
//        private ProgressBar webview_progressbar;
        private CookieManager manager;
        private TextView title;

        public MyViewHolder(View itemView) {
            super(itemView);
            webView = (WebView) itemView.findViewById(R.id.webviewdetail_webview);
//            webview_progressbar = (ProgressBar) itemView.findViewById(R.id.webview_progressbar);
            title = (TextView) itemView.findViewById(R.id.title);
            setWebViewSetting();
        }

        private void setWebViewSetting() {
            WebSettings settings = webView.getSettings();
            settings.setJavaScriptEnabled(true);
            settings.setPluginState(WebSettings.PluginState.ON);
            settings.setAllowFileAccess(true);
            settings.setCacheMode(WebSettings.LOAD_DEFAULT);
            settings.setUseWideViewPort(true);  //将内容调整到控件大小
            settings.setLoadWithOverviewMode(true);  //将内容调整到屏幕大小
//            settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS); //支持将内容重新布局
            // settings.setUseWideViewPort(true);
            // settings.setLoadWithOverviewMode(true);
            settings.setSupportZoom(true);
            settings.setBuiltInZoomControls(true);
            // 设置cookie
            setWebViewCookie();
        }

        private void setWebViewCookie() {
            String sessionId = null;
            AsyncHttpClient client = HttpclientUtil.getInstance(mContext);
            CookieStore cookieStore = (CookieStore) client.getHttpContext()
                    .getAttribute(ClientContext.COOKIE_STORE);
            List<Cookie> cookies = cookieStore.getCookies();
            for (Cookie c : cookies) {
                if ("JSESSIONID".equals(c.getName())) {
                    sessionId = c.getValue();
                }
            }
            CookieSyncManager.createInstance(mContext);
            manager = CookieManager.getInstance();
            manager.setCookie(MyConstants.LOGIN_URL, "JSESSIONID=" + sessionId);
            CookieSyncManager.getInstance().sync();

//            webView.setWebViewClient(new WebViewClient() {
//                @Override
//                public boolean shouldOverrideUrlLoading(WebView view, String url) {
////                    view.loadUrl(url);
//                    return true;
//                }
//
//                @Override
//                public void onPageStarted(WebView view, String url, Bitmap favicon) {
//                    webview_progressbar.setVisibility(View.VISIBLE);
//                    super.onPageStarted(view, url, favicon);
//                }
//            });

//            webView.setWebChromeClient(new WebChromeClient() {
//                @Override
//                public void onProgressChanged(WebView view, int newProgress) {
//                    webview_progressbar.setProgress(newProgress);
//                    if (newProgress == 100) {
//                        webview_progressbar.setVisibility(View.INVISIBLE);
//                    }
//                    super.onProgressChanged(view, newProgress);
//                }
//
//            });


        }
    }
}
