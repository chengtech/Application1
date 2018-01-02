package com.chengtech.chengtechmt.activity.monitoremergency;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.chengtech.chengtechmt.BaseActivity;
import com.chengtech.chengtechmt.R;
import com.chengtech.chengtechmt.activity.business.BusinessActivity;
import com.chengtech.chengtechmt.activity.monitoremergency.slopemonitor.SlopeMonitor2Activity;
import com.chengtech.chengtechmt.activity.monitoremergency.slopemonitor.SlopeMonitorActivity;
import com.chengtech.chengtechmt.activity.monitoremergency.weathermonitor.WeatherMonitorActivity;
import com.chengtech.chengtechmt.adapter.RecycleViewAdapter;
import com.chengtech.chengtechmt.divider.RecycleViewDivider;

public class MonitorEmergencyActivity extends BaseActivity {

    public RecyclerView recyclerView;
    String[] title;
    int[] imageIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addContentView(R.layout.activity_monitor_emergency);

        initView();

        setNavigationIcon(true);
        hidePagerNavigation(true);
        initData();
    }


    private void initData() {

        imageIds = new int[]{R.mipmap.ic_launcher, R.mipmap.ic_launcher};
        title = new String[]{"边坡在线监测", "公路气象监测"};

        RecycleViewAdapter adapter = new RecycleViewAdapter(this, title, imageIds, R.layout.item_recycle2);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new RecycleViewDivider(this, LinearLayout.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter.setOnItemClickListener(new RecycleViewAdapter.OnItemClickListener() {
            @Override
            public void setOnItemClickListener(View view, int position) {
                Intent intent = null;
                try {
                    switch (position) {
                        case 0:
                            intent = new Intent(MonitorEmergencyActivity.this, SlopeMonitor2Activity.class);
                            startActivity(intent);
                            break;
                        case 1:
                            intent = new Intent(MonitorEmergencyActivity.this, WeatherMonitorActivity.class);
                            startActivity(intent);
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void setOnItemLongClickListener(View view, int position) {

            }
        });
    }

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.recycleView);
    }
}
