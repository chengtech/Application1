package com.chengtech.chengtechmt.activity.monitoremergency.slopemonitor;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
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
import android.widget.Toast;

import com.chengtech.chengtechmt.BaseActivity;
import com.chengtech.chengtechmt.R;
import com.chengtech.chengtechmt.adapter.sidemonitor.CdMonitorSessionAdapter;
import com.chengtech.chengtechmt.adapter.sidemonitor.InclinationDataAdapter;
import com.chengtech.chengtechmt.adapter.sidemonitor.LvdtdataAdapter;
import com.chengtech.chengtechmt.adapter.sidemonitor.RainFallDataAdapter;
import com.chengtech.chengtechmt.adapter.sidemonitor.TempHumiDataAdapter;
import com.chengtech.chengtechmt.adapter.sidemonitor.VibratingWireDataAdapter;
import com.chengtech.chengtechmt.divider.RecycleViewDivider;
import com.chengtech.chengtechmt.entity.monitoremergency.CdMonitorSession;
import com.chengtech.chengtechmt.entity.monitoremergency.Inclinationdata;
import com.chengtech.chengtechmt.entity.monitoremergency.Lvdtdata;
import com.chengtech.chengtechmt.entity.monitoremergency.RainFallData;
import com.chengtech.chengtechmt.entity.monitoremergency.SideMonitorType;
import com.chengtech.chengtechmt.entity.monitoremergency.TempHumiData;
import com.chengtech.chengtechmt.entity.monitoremergency.VibratingWireData;
import com.chengtech.chengtechmt.hellochart.LineChartFragment;
import com.chengtech.chengtechmt.util.HttpclientUtil;
import com.chengtech.chengtechmt.util.MyConstants;
import com.github.jjobes.slidedatetimepicker.SlideDateTimeListener;
import com.github.jjobes.slidedatetimepicker.SlideDateTimePicker;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;

import org.apache.http.client.protocol.ClientContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import cz.msebera.android.httpclient.client.CookieStore;
import cz.msebera.android.httpclient.cookie.Cookie;
import lecho.lib.hellocharts.view.LineChartView;

public class SideMonitorTypeActivity extends BaseActivity {

    private static final int RESULT_CODE_1 = 0x01;
    private LineChartView lineChartView;
    private List<SideMonitorType> sideMonitorTypes;
    private Spinner sideMonitorTypeSpinner, dateSpinner;
    private String jspUrl;
    private String listUrl;
    private LinearLayout dateLayout;
    private String sideMonitorId, dateStr;
    private TextView startDate, endDate;
    private RecyclerView recyclerView;
    private float[][] lineData;
    private String[][] lineLable;
    private WebView webView;
    private AlertDialog filterDialog;
    private SweetAlertDialog sweetAlertDialog;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Gson gson = new Gson();
            String json = (String) msg.obj;
            switch (msg.what) {
                case RESULT_CODE_1:
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        JSONArray rows = jsonObject.getJSONArray("rows");
                        List<TempHumiData> tempHumiDatas = new ArrayList<>();
                        if (rows != null && rows.length() > 0) {
                            for (int i = 0; i < rows.length(); i++) {
                                String tempHumiDatasString = rows.getString(i);
                                TempHumiData tempHumiData = gson.fromJson(tempHumiDatasString, TempHumiData.class);
                                tempHumiDatas.add(tempHumiData);
                            }
                            lineData = new float[2][tempHumiDatas.size()];
                            lineLable = new String[1][tempHumiDatas.size()];
                            for (int j = 0; j < tempHumiDatas.size(); j++) {
                                lineData[0][j] = tempHumiDatas.get(tempHumiDatas.size() - 1 - j).temperatureValue;
                                lineData[1][j] = tempHumiDatas.get(tempHumiDatas.size() - 1 - j).humilityValue;
                                lineLable[0][j] = tempHumiDatas.get(tempHumiDatas.size() - 1 - j).acquisitionDatetime;
                            }

                            Bundle bundle = new Bundle();
                            bundle.putSerializable("lineData", lineData);
                            bundle.putSerializable("lineLable", lineLable);
                            LineChartFragment fragment = new LineChartFragment();
                            fragment.setArguments(bundle);
                            getSupportFragmentManager().beginTransaction().add(R.id.frameLayout, fragment).commit();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;

                case 0x11:
                    if (listUrl.contains("lvdtdata")) {
                        List<Lvdtdata> lvdtdatas = new ArrayList<>();
                        setDataAdapter(json, lvdtdatas, Lvdtdata.class);
                        LvdtdataAdapter adapter = new LvdtdataAdapter(lvdtdatas);
                        recyclerView.setAdapter(adapter);
                    } else if (listUrl.contains("temphumidata")) {
                        List<TempHumiData> tempHumiDatas = new ArrayList<>();
                        setDataAdapter(json, tempHumiDatas, TempHumiData.class);
                        TempHumiDataAdapter adapter = new TempHumiDataAdapter(tempHumiDatas);
                        recyclerView.setAdapter(adapter);
                    } else if (listUrl.contains("vibratingwiredata")) {
                        List<VibratingWireData> vibratingWireDatas = new ArrayList<>();
                        setDataAdapter(json, vibratingWireDatas, VibratingWireData.class);
                        VibratingWireDataAdapter adapter = new VibratingWireDataAdapter(vibratingWireDatas);
                        recyclerView.setAdapter(adapter);
                    } else if (listUrl.contains("inclinationdata")) {
                        List<Inclinationdata> inclinationdatas = new ArrayList<>();
                        setDataAdapter(json, inclinationdatas, Inclinationdata.class);
                        InclinationDataAdapter adapter = new InclinationDataAdapter(inclinationdatas);
                        recyclerView.setAdapter(adapter);
                    } else if (listUrl.contains("rainfalldata")) {
                        List<RainFallData> rainFallDatas = new ArrayList<>();
                        setDataAdapter(json, rainFallDatas, RainFallData.class);
                        RainFallDataAdapter adapter = new RainFallDataAdapter(rainFallDatas);
                        recyclerView.setAdapter(adapter);
                    } else if (listUrl.contains("cdmonitorsession")) {
                        List<CdMonitorSession> cdMonitorSessions = new ArrayList<>();
                        setDataAdapter(json, cdMonitorSessions, CdMonitorSession.class);
                        CdMonitorSessionAdapter adapter = new CdMonitorSessionAdapter(cdMonitorSessions);
                        recyclerView.setAdapter(adapter);
                    }

                    recyclerView.setLayoutManager(new LinearLayoutManager(SideMonitorTypeActivity.this));

                    break;
            }
        }
    };

    /**
     * 设置adapter
     *
     * @param json
     * @param clazz
     */
    private <T> void setDataAdapter(String json, List data, Class<T> clazz) {
        Gson gson = new Gson();
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray rows = jsonObject.getJSONArray("rows");
//            List<Lvdtdata> lvdtdatas = new ArrayList<>();
            if (rows != null && rows.length() > 0) {
                for (int i = 0; i < rows.length(); i++) {
                    String rowsString = rows.getString(i);
                    data.add(gson.fromJson(rowsString, clazz));
//                    lvdtdatas.add(lvdtdata);
                }

            }

//            LvdtdataAdapter adapter = new LvdtdataAdapter(lvdtdatas);
        } catch (JSONException e) {
            Toast.makeText(this, "json解析出错", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addContentView(R.layout.activity_side_monitor_type);
        toolbar.setTitle("边坡在线监测");
        setNavigationIcon(true);
        hidePagerNavigation(true);
        setAppBarLayoutScroll(false);


        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        toolbar.setTitle(title);
        sideMonitorTypes = (List<SideMonitorType>) intent.getSerializableExtra("data");
        jspUrl = intent.getStringExtra("url");
        listUrl = intent.getStringExtra("listUrl");

        initView();

        initData();


    }

    private void initData() {
        if (sideMonitorTypes != null) {
            List<String> spinnerItem = new ArrayList<>();
            for (int i = 0; i < sideMonitorTypes.size(); i++) {
                SideMonitorType sideMonitorType = sideMonitorTypes.get(i);
                spinnerItem.add(sideMonitorType.name);
            }

            sideMonitorTypeSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerItem));
            sideMonitorTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    SideMonitorType sideMonitorType = sideMonitorTypes.get(position);
                    sideMonitorId = sideMonitorType.id;
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }

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

        Button search = (Button) findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String params = "selectDate=" + dateStr + "&sensorSetId=" + sideMonitorId + "&sideMonitorTypeId=" + sideMonitorId + "&startDate=" + startDate.getText().toString() + "&endDate=" + endDate.getText().toString();
                webView.loadUrl("javascript:queryFn('" + params + "')");
                HttpclientUtil.getData(SideMonitorTypeActivity.this, listUrl + "?" + params, handler, 0x11);
            }
        });
    }

    private void initView() {
        webView = (WebView) findViewById(R.id.webView);
        sideMonitorTypeSpinner = (Spinner) findViewById(R.id.sideMonitorType);
        dateSpinner = (Spinner) findViewById(R.id.date);
        dateLayout = (LinearLayout) findViewById(R.id.dateLayout);
        startDate = (TextView) findViewById(R.id.startDate);
        endDate = (TextView) findViewById(R.id.endDate);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.addItemDecoration(new RecycleViewDivider(SideMonitorTypeActivity.this, LinearLayout.VERTICAL));

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

                String params = "selectDate=最近一天&sensorSetId=" + sideMonitorId + "&sideMonitorTypeId=" + sideMonitorId + "&startDate=" + startDate.getText().toString() + "&endDate=" + endDate.getText().toString();
                webView.loadUrl("javascript:queryFn('" + params + "')");
                HttpclientUtil.getData(SideMonitorTypeActivity.this, listUrl + "?" + params, handler, 0x11);
                //在一开始进来就加载一条数据
//                if (isFirst) {
//                    HttpclientUtil.getData(MapQueryActivity.this, MyConstants.PRE_URL + "mt/integratequery/gisvisualization/getEvaluateMsg.action?routeGrade=&routeCode=&evaluateIndex=PQI&year=",
//                            handler, 1);
//                }
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

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_work_section, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int itemId = item.getItemId();
//        if (itemId == R.id.filter) {
//            if (filterDialog == null) {
////                showFilterDialog();
//            } else {
//                filterDialog.show();
//            }
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

//    private void showFilterDialog() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        View view = LayoutInflater.from(this).inflate(R.layout.filter_side_monitor_type, null, false);
//        sideMonitorTypeSpinner = (Spinner) view.findViewById(R.id.sideMonitorType);
//        dateSpinner = (Spinner) view.findViewById(R.id.date);
//        dateLayout = (LinearLayout) view.findViewById(R.id.dateLayout);
//        startDate = (TextView) view.findViewById(R.id.startDate);
//        endDate = (TextView) view.findViewById(R.id.endDate);
//
//        if (sideMonitorTypes != null) {
//            List<String> spinnerItem = new ArrayList<>();
//            for (int i = 0; i < sideMonitorTypes.size(); i++) {
//                SideMonitorType sideMonitorType = sideMonitorTypes.get(i);
//                spinnerItem.add(sideMonitorType.name);
//            }
//
//            sideMonitorTypeSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerItem));
//            sideMonitorTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                @Override
//                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                    SideMonitorType sideMonitorType = sideMonitorTypes.get(position);
//                    sideMonitorId = sideMonitorType.id;
//                }
//
//                @Override
//                public void onNothingSelected(AdapterView<?> parent) {
//
//                }
//            });
//        }
//
//        dateSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, new String[]{"最近一天", "最近一周", "最近一月", "其他"}));
//        dateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                String selectedItem = (String) parent.getSelectedItem();
//                if (selectedItem.equals("其他")) {
//                    dateLayout.setVisibility(View.VISIBLE);
//                } else {
//                    dateLayout.setVisibility(View.INVISIBLE);
//                }
//                dateStr = selectedItem;
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//
//        SlideDateTimeListener listener = new SlideDateTimeListener() {
//
//            @Override
//            public void onDateTimeSet(Date date) {
//                // Do something with the date. This Date object contains
//                // the date and time that the user has selected.
//                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                boolean isClicking = (boolean) startDate.getTag();
//                if (isClicking) {
//                    startDate.setText(format.format(date));
//                } else {
//                    endDate.setText(format.format(date));
//                }
//
//            }
//
//            @Override
//            public void onDateTimeCancel() {
//                // Overriding onDateTimeCancel() is optional.
//            }
//        };
//        final SlideDateTimePicker picker = new SlideDateTimePicker.Builder(getSupportFragmentManager())
//                .setListener(listener)
//                .setInitialDate(new Date())
//                .setIs24HourTime(true)
//                .build();
//        startDate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startDate.setTag(true);
//                picker.show();
//            }
//        });
//
//        endDate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startDate.setTag(false);
//                picker.show();
//            }
//        });
//
//        Button search = (Button) view.findViewById(R.id.search);
//        search.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String params = "selectDate=" + dateStr + "&sensorSetId=" + sideMonitorId + "&sideMonitorTypeId=" + sideMonitorId + "&startDate=" + startDate.getText().toString() + "&endDate=" + endDate.getText().toString();
//                webView.loadUrl("javascript:queryFn('" + params + "')");
//
//                filterDialog.dismiss();
//
//            }
//        });
//        builder.setView(view);
//        filterDialog = builder.create();
//        filterDialog.show();
//    }

    public static void startAction(Context context, String title, List<SideMonitorType> sideMonitorTypeList, String jspUrl, String listUrl) {
        Intent intent = new Intent(context, SideMonitorTypeActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("data", (Serializable) sideMonitorTypeList);
        intent.putExtra("url", jspUrl);
        intent.putExtra("listUrl", listUrl);
        context.startActivity(intent);
    }

    public void onSearch(View view) {
//        SideMonitorType sideMonitorType = sideMonitorTypes.get(0);
//        String url = sideMonitorType.sideMonitorTree.url;
//        String url2 = MyConstants.PRE_URL + "mt/" + url.substring(0, url.lastIndexOf(".")) + "Json" + url.substring(url.lastIndexOf("."));
//        url2 = url2 + "?selectDate=" + dateStr + "&sideMonitorTypeId=" + sideMonitorId+"&startDate="+startDate.getText().toString()+"&endDate="+endDate.getText().toString();
//        Toast.makeText(this, url2, Toast.LENGTH_LONG).show();
//        HttpclientUtil.getData(this,url2,handler,RESULT_CODE_1);

//        String preUrl = MyConstants.PRE_URL + "mt/monitoremergency/sidemonitor/temphumidata/getTempHumiDataGraph.action";
        String params = "selectDate=" + dateStr + "&sensorSetId=" + sideMonitorId + "&sideMonitorTypeId=" + sideMonitorId + "&startDate=" + startDate.getText().toString() + "&endDate=" + endDate.getText().toString();
//        webView.loadUrl("javascript:generaEchart('" + preUrl + "','"+params+"')");
        webView.loadUrl("javascript:queryFn('" + params + "')");

//        Intent intent = new Intent(this, WebViewActivity.class);
//        intent.putExtra("url","http://192.1.40.44:8080/chengtechmt/mt/monitoremergency/sidemonitor/temphumidata/listTempHumiData.jsp");
//        startActivity(intent);
    }
}
