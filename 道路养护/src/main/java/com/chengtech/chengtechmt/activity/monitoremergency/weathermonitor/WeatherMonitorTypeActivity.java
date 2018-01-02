package com.chengtech.chengtechmt.activity.monitoremergency.weathermonitor;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.chengtech.chengtechmt.BaseActivity;
import com.chengtech.chengtechmt.R;
import com.chengtech.chengtechmt.activity.monitoremergency.slopemonitor.SideMonitorTypeActivity;
import com.chengtech.chengtechmt.divider.RecycleViewDivider;
import com.chengtech.chengtechmt.entity.monitoremergency.SideMonitorType;
import com.chengtech.chengtechmt.util.HttpclientUtil;
import com.chengtech.chengtechmt.util.MyConstants;
import com.github.jjobes.slidedatetimepicker.SlideDateTimeListener;
import com.github.jjobes.slidedatetimepicker.SlideDateTimePicker;
import com.loopj.android.http.AsyncHttpClient;

import org.apache.http.client.protocol.ClientContext;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import cz.msebera.android.httpclient.client.CookieStore;
import cz.msebera.android.httpclient.cookie.Cookie;

public class WeatherMonitorTypeActivity extends BaseActivity {

    private String jspUrl;
    private Spinner dateSpinner;

    private LinearLayout dateLayout;
    private Button search;
    private WebView webView;
    private TextView startDate, endDate;
    private SweetAlertDialog sweetAlertDialog;
    private String stationName;
    private String dateStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addContentView(R.layout.activity_weather_monitor_type);

//        "selectDate="+selectDate+"&startDate="+startDate+"&endDate="+endDate + "&stationName=蚌湖气象站";

        setNavigationIcon(true);
        hidePagerNavigation(true);
        setAppBarLayoutScroll(false);

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        stationName = title.substring(0, title.indexOf("-"));
        toolbar.setTitle(title);
        jspUrl = intent.getStringExtra("url");

        initView();

        initData();

    }

    private void initData() {
        dateSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, new String[]{"最近一天", "最近一周", "最近一月", "其他"}));
        dateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) parent.getSelectedItem();
                if (selectedItem.equals("其他")) {
                    dateLayout.setVisibility(View.VISIBLE);
                } else {
                    dateLayout.setVisibility(View.GONE);
                }
                dateStr = selectedItem;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        SlideDateTimeListener listener = new SlideDateTimeListener() {

            @Override
            public void onDateTimeSet(Date date) {
                // Do something with the date. This Date object contains
                // the date and time that the user has selected.
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                boolean isClicking = (boolean) startDate.getTag();
                if (isClicking) {
                    startDate.setText(format.format(date));
                } else {
                    endDate.setText(format.format(date));
                }

            }

            @Override
            public void onDateTimeCancel() {
                // Overriding onDateTimeCancel() is optional.
            }
        };

        final SlideDateTimePicker picker = new SlideDateTimePicker.Builder(getSupportFragmentManager())
                .setListener(listener)
                .setInitialDate(new Date())
                .setIs24HourTime(true)
                .build();
        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDate.setTag(true);
                picker.show();
            }
        });

        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDate.setTag(false);
                picker.show();
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String params = "stationName=" + stationName + "&selectDate=" + dateStr + "&startDate=" + startDate.getText().toString() + "&endDate=" + endDate.getText().toString();
                webView.loadUrl("javascript:queryFn('" + params + "')");
            }
        });
    }

    private void initView() {
        webView = (WebView) findViewById(R.id.webView);
        dateSpinner = (Spinner) findViewById(R.id.date);
        dateLayout = (LinearLayout) findViewById(R.id.dateLayout);
        startDate = (TextView) findViewById(R.id.startDate);
        endDate = (TextView) findViewById(R.id.endDate);
        search = (Button) findViewById(R.id.search);

        sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.setContentText("正在加载.....");
        sweetAlertDialog.setTitleText("");
        sweetAlertDialog.show();
        webViewSetting();
    }

    private void webViewSetting() {
        WebSettings settings = webView.getSettings();

        settings.setJavaScriptEnabled(true);

        settings.setPluginState(WebSettings.PluginState.ON);
        settings.setAllowFileAccess(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setUseWideViewPort(true);  //将内容调整到控件大小
        settings.setLoadWithOverviewMode(true);  //将内容调整到屏幕大小
//        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS); //支持将内容重新布局
        // settings.setUseWideViewPort(true);
        // settings.setLoadWithOverviewMode(true);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);


        setWebViewCookie();
        //让js能调用这里的方法
//        webView.addJavascriptInterface(this, "javato");
        webView.loadUrl(jspUrl);


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

                sweetAlertDialog.dismiss();

                String params = "stationName=" + stationName + "&selectDate=最近一天" + "&startDate=" + startDate.getText().toString() + "&endDate=" + endDate.getText().toString();
                webView.loadUrl("javascript:queryFn('" + params + "')");
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


    public static void startAction(Context context, String title, String jspUrl) {
        Intent intent = new Intent(context, WeatherMonitorTypeActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("url", jspUrl);
        context.startActivity(intent);
    }

}
