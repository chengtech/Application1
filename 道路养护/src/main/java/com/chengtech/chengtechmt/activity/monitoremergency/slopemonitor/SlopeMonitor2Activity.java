package com.chengtech.chengtechmt.activity.monitoremergency.slopemonitor;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;

import com.chengtech.chengtechmt.BaseActivity;
import com.chengtech.chengtechmt.R;
import com.chengtech.chengtechmt.activity.dbm.DbmActivity;
import com.chengtech.chengtechmt.activity.monitoremergency.MonitorEmergencyActivity;
import com.chengtech.chengtechmt.adapter.MyExpandableAdapter;
import com.chengtech.chengtechmt.entity.monitoremergency.SideMonitorTree;
import com.chengtech.chengtechmt.entity.monitoremergency.SideMonitorType;
import com.chengtech.chengtechmt.util.HttpclientUtil;
import com.chengtech.chengtechmt.util.MyConstants;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SlopeMonitor2Activity extends BaseActivity {

    private static final int RESULT_CODE_1 = 0x01;
    private ExpandableListView expandableListView;
    private MyExpandableAdapter expandableAdapter;
    private LinearLayout head_layout;
    private String[] groupName = new String[1];
    private Map<String, List<String>> groupList;
    public Map<String, List<SideMonitorType>> sideMonitorTypeMap = new HashMap<>();
    private String getSlopeIdUrl = MyConstants.PRE_URL + "mt/monitoremergency/homepage/indexMobile.action";

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Gson gson = new Gson();
            String json = (String) msg.obj;
            switch (msg.what) {
                case RESULT_CODE_1:
                    try {
                        JSONArray jsonArray = new JSONArray(json);
                        if (jsonArray != null && jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                String sideMonitorTreeStr = jsonArray.getString(i);

                                SideMonitorTree sideMonitorTree = gson.fromJson(sideMonitorTreeStr, SideMonitorTree.class);
                                groupName[i] = sideMonitorTree.name;
                                List<SideMonitorTree> sideMonitorTreeList = sideMonitorTree.listSideMonitorTreeByParentId;
                                List<String> data = new ArrayList<String>();
                                for (int j = 0; j < sideMonitorTreeList.size(); j++) {
                                    String treeName = sideMonitorTreeList.get(j).name;
                                    List<SideMonitorType> sideMonitorTypeList = sideMonitorTreeList.get(j).listSideMonitorType;
                                    data.add(treeName);
                                    sideMonitorTypeMap.put(treeName, sideMonitorTypeList);
                                }
                                groupList.put(sideMonitorTree.name, data);

                                expandableAdapter = new MyExpandableAdapter(SlopeMonitor2Activity.this, groupName, groupList);
                                int width = getResources().getDisplayMetrics().widthPixels;
                                expandableListView.setIndicatorBounds(width - 150, width - 70);
                                expandableListView.setAdapter(expandableAdapter);

                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addContentView(R.layout.activity_slope_monitor2);

        setNavigationIcon(true);
        hidePagerNavigation(true);
        setAppBarLayoutScroll(false);

        initView();
        HttpclientUtil.getData(this, getSlopeIdUrl, handler, RESULT_CODE_1, false);

        initData();
    }

    private void initData() {
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {

                try {
                    String selectedItem = groupList.get(groupName[groupPosition]).get(childPosition);
                    if (sideMonitorTypeMap != null && sideMonitorTypeMap.size() > 0) {
                        List<SideMonitorType> sideMonitorTypes = sideMonitorTypeMap.get(selectedItem);
                        if (sideMonitorTypes != null) {
                            if (selectedItem.contains("温湿度")) {
                                String jspUrl = MyConstants.PRE_URL + "mt/monitoremergency/sidemonitor/temphumidata/listTempHumiDataPhone.jsp";
                                String listUrl = MyConstants.PRE_URL + "mt/monitoremergency/sidemonitor/temphumidata/listTempHumiDataJson.action";
                                SideMonitorTypeActivity.startAction(SlopeMonitor2Activity.this, selectedItem, sideMonitorTypes, jspUrl, listUrl);
                            } else if (selectedItem.contains("地下水")) {
                                String jspUrl = MyConstants.PRE_URL + "mt/monitoremergency/sidemonitor/vibratingwiredata/listVibratingWireDataPhone.jsp";
                                String listUrl = MyConstants.PRE_URL + "mt/monitoremergency/sidemonitor/vibratingwiredata/listVibratingWireDataJson.action";
                                SideMonitorTypeActivity.startAction(SlopeMonitor2Activity.this, selectedItem, sideMonitorTypes, jspUrl, listUrl);
                            } else if (selectedItem.contains("拉线式")) {
                                String jspUrl = MyConstants.PRE_URL + "mt/monitoremergency/sidemonitor/lvdtdata/listLVDTDataPhone.jsp";
                                String listUrl = MyConstants.PRE_URL + "mt/monitoremergency/sidemonitor/lvdtdata/listLvdtDataJson.action";
                                SideMonitorTypeActivity.startAction(SlopeMonitor2Activity.this, selectedItem, sideMonitorTypes, jspUrl, listUrl);
                            } else if (selectedItem.contains("内部位移")) {
                                String jspUrl = MyConstants.PRE_URL + "mt/monitoremergency/sidemonitor/inclinationdata/listInclinationDataPhone.jsp";
                                String listUrl = MyConstants.PRE_URL + "mt/monitoremergency/sidemonitor/inclinationdata/listInclinationDataJson.action";
                                SideMonitorTypeActivity.startAction(SlopeMonitor2Activity.this, selectedItem, sideMonitorTypes, jspUrl, listUrl);
                            } else if (selectedItem.contains("降雨量")) {
                                String jspUrl = MyConstants.PRE_URL + "mt/monitoremergency/sidemonitor/rainfalldata/listRainFallDataPhone.jsp";
                                String listUrl = MyConstants.PRE_URL + "mt/monitoremergency/sidemonitor/rainfalldata/listRainFallDataJson.action";
                                SideMonitorTypeActivity.startAction(SlopeMonitor2Activity.this, selectedItem, sideMonitorTypes, jspUrl, listUrl);
                            } else if (selectedItem.contains("GPS")) {
                                String jspUrl = MyConstants.PRE_URL + "mt/monitoremergency/sidemonitor/cdmonitorsession/listCdMonitorSessionPhone.jsp";
                                String listUrl = MyConstants.PRE_URL + "mt/monitoremergency/sidemonitor/cdmonitorsession/listCdMonitorSessionJson.action";
                                SideMonitorTypeActivity.startAction(SlopeMonitor2Activity.this, selectedItem, sideMonitorTypes, jspUrl, listUrl);
                            }
                        }
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
                Intent intent = new Intent(SlopeMonitor2Activity.this, SlopeMonitorActivity.class);
                startActivity(intent);
            }
        });
        expandableListView = (ExpandableListView) findViewById(R.id.expand_listview);
        groupList = new HashMap<String, List<String>>();

    }
}
