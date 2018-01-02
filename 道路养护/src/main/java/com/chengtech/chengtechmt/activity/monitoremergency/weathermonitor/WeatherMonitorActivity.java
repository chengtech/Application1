package com.chengtech.chengtechmt.activity.monitoremergency.weathermonitor;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;

import com.chengtech.chengtechmt.BaseActivity;
import com.chengtech.chengtechmt.R;
import com.chengtech.chengtechmt.adapter.MyExpandableAdapter;
import com.chengtech.chengtechmt.util.MyConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 气象观测站activity
 */
public class WeatherMonitorActivity extends BaseActivity {
    private static final int RESULT_CODE_1 = 0x01;
    private ExpandableListView expandableListView;
    private MyExpandableAdapter expandableAdapter;
    private LinearLayout head_layout;
    private String[] groupName;
    //获取气象站信息url，这里因为写死了5个，所以用不上该url，如果动态显示气象站，就要利用该url获取到的内容。
//    private String weatherMonitorUrl = MyConstants.PRE_URL+"mt/monitoremergency/weathermonitor/weathermonitortree/getPhoneTree.action";
    private Map<String, List<String>> childList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addContentView(R.layout.activity_weather_monitor);

        setNavigationIcon(true);
        hidePagerNavigation(true);

        toolbar.setTitle("公路气象监测");

        initView();

        initData();

    }

    private void initData() {
        childList = new HashMap<String, List<String>>();
        groupName = new String[]{"蚌湖气象站", "龙归气象站", "太和气象站", "南城东片气象站", "夏园气象站", "南城北片气象站"};
        for (int i = 0; i < groupName.length; i++) {
            List<String> child = new ArrayList<>();
            child.add("能见度");
            child.add("温湿度");
            childList.put(groupName[i], child);
        }

        expandableAdapter = new MyExpandableAdapter(this, groupName, childList);
        int width = getResources().getDisplayMetrics().widthPixels;
        expandableListView.setIndicatorBounds(width - 150, width - 70);
        expandableListView.setAdapter(expandableAdapter);

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                try {
                    String stationName = groupName[groupPosition];
                    String selectedItem = childList.get(groupName[groupPosition]).get(childPosition);
                    switch (selectedItem) {
                        case "能见度":
                            String url1 = MyConstants.PRE_URL + "mt/monitoremergency/weathermonitor/weathermonitordata/listWeatherMonitorDataVisPhone.jsp";
                            WeatherMonitorTypeActivity.startAction(WeatherMonitorActivity.this, stationName + "-" + selectedItem, url1);
                            break;

                        case "温湿度":
                            String url2 = MyConstants.PRE_URL + "mt/monitoremergency/weathermonitor/weathermonitordata/listWeatherMonitorDataPhone.jsp";
                            WeatherMonitorTypeActivity.startAction(WeatherMonitorActivity.this, stationName + "-" + selectedItem, url2);
                            break;
                    }
                    return true;
                } catch (Exception e) {
                }
                return false;
            }
        });
    }

    private void initView() {
        head_layout = (LinearLayout) findViewById(R.id.head);
        head_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mapData = null;
                mapData = "[{\"name\":\"蚌湖气象站 \",\"x\":113.25967,\"y\":23.29839,\"xydwid\":\"40288ae5505ed67001505ede57a6001d\"},{\"name\":\"龙归气象站 \",\"x\":113.3075,\"y\":23.28219,\"xydwid\":\"40288ae551a849180151a895193e00bd\"},{\"name\":\"太和气象站 \",\"x\":113.256398,\"y\":23.147852,\"xydwid\":\"40288ae551a849180151a895638e00c4\"},{\"name\":\"南城东片气象站 \",\"x\":113.235698,\"y\":23.452178,\"xydwid\":\"40288ae551a849180151a8959d4300ca\"},{\"name\":\"夏园气象站 \",\"x\":113.45718,\"y\":23.09489,\"xydwid\":\"40288ae551a849180151a895d6ab00d0\"},{\"name\":\"南城北片气象站 \",\"x\":113.789654,\"y\":23.456321,\"xydwid\":\"40288ae551a849180151a896483200d8\"}]";
                WeatherMonitorLocationActivity.startAction(WeatherMonitorActivity.this, mapData);
            }
        });
        expandableListView = (ExpandableListView) findViewById(R.id.expand_listview);


    }
}
