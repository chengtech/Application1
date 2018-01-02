package com.chengtech.chengtechmt.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.chengtech.chengtechmt.R;
import com.chengtech.chengtechmt.adapter.WebViewAdapter;
import com.chengtech.chengtechmt.util.MyConstants;

import java.util.ArrayList;
import java.util.List;

public class MultiChartWebViewActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private List<String> urlList = new ArrayList<>();
    private List<String> titleList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_chart_web_view);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setTitle("图表");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.arrow_back2);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        initView();
        initData();
        recyclerView.setAdapter(new WebViewAdapter(urlList, titleList));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initData() {
        titleList.add("设施量统计");
        titleList.add("道路、桥梁分析");
        titleList.add("养护业务-资金数据分析");
        titleList.add("养护业务数据提交及时率");
        titleList.add("机械设备年度利用率、完好率");
        titleList.add("");
        urlList.add(MyConstants.PRE_URL + "ms/sys/homechart/maintenanceBusinessEchart.action");
        urlList.add(MyConstants.PRE_URL + "ms/sys/homechart/dbmSectionAndBridgeEchart.action");
        urlList.add(MyConstants.PRE_URL + "ms/sys/homechart/projectCapitalEchart.action");
        urlList.add(MyConstants.PRE_URL + "ms/sys/homechart/maintenanceScheduleEchart.action");
        urlList.add(MyConstants.PRE_URL + "ms/sys/homechart/equipmentRunRecordEchart.action");
        urlList.add(MyConstants.PRE_URL + "ms/sys/homechart/mediumplanprogressProjectManagementCompletionEchart.action");
    }

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
    }
}
