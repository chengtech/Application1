package com.chengtech.chengtechmt.activity.monitoremergency.slopemonitor;

import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapsInitializer;
import com.amap.api.maps2d.SupportMapFragment;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MarkerOptions;
import com.chengtech.chengtechmt.BaseActivity;
import com.chengtech.chengtechmt.R;
import com.chengtech.chengtechmt.entity.Route;
import com.chengtech.chengtechmt.entity.Slope;
import com.chengtech.chengtechmt.entity.monitoremergency.SideMonitorTree;
import com.chengtech.chengtechmt.entity.monitoremergency.SideMonitorType;
import com.chengtech.chengtechmt.entity.monitoremergency.WarningManage;
import com.chengtech.chengtechmt.util.HttpclientUtil;
import com.chengtech.chengtechmt.util.MyConstants;
import com.chengtech.chengtechmt.view.MyViewPager;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * 边坡监测activity-2017-06-05
 */
public class SlopeMonitorActivity extends BaseActivity {

    private static final int RESULT_CODE_1 = 0x01;
    private String getSlopeIdUrl = MyConstants.PRE_URL + "mt/monitoremergency/homepage/indexMobile.action";
    public TabLayout tabLayout;
    public MyViewPager viewPager;
    public Route route;
    public String deptId;
    public List<Fragment> fragmentList = new ArrayList<>();
    ;
    public String[] mTitle = new String[]{"地理位置", "列表信息", "告警级别", "已/未确认图形展示"};
    public SweetAlertDialog sweetAlertDialog;
    public List<SideMonitorTree> slopeList = new ArrayList<>();
    private Spinner slopeSpinner, monitorTypeSpinner;
    public List<WarningManage> warningManages = new ArrayList<>();
    public Map<String, Integer> charData = new HashMap<>();
    public Map<String, Integer> charData2 = new HashMap<>();
    public Map<String, List<SideMonitorType>> sideMonitorTypeMap = new HashMap<>();


    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Gson gson = new Gson();
            String json = (String) msg.obj;
            switch (msg.what) {
                case RESULT_CODE_1:
                    if (sweetAlertDialog != null && sweetAlertDialog.isShowing())
                        sweetAlertDialog.dismiss();
                    try {
                        JSONArray jsonArray = new JSONArray(json);
                        if (jsonArray != null && jsonArray.length() > 0) {
                            List<String> data = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                String sideMonitorTreeStr = jsonArray.getString(i);

                                SideMonitorTree sideMonitorTree = gson.fromJson(sideMonitorTreeStr, SideMonitorTree.class);
                                slopeList.add(sideMonitorTree);
                                data.add(sideMonitorTree.name);
                            }

                            slopeSpinner.setAdapter(new ArrayAdapter<String>(SlopeMonitorActivity.this,
                                    android.R.layout.simple_spinner_item, data));
                            slopeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    SideMonitorTree sideMonitorTreeParent = slopeList.get(position);
                                    List<SideMonitorTree> sideMonitorTreeList = sideMonitorTreeParent.listSideMonitorTreeByParentId;
                                    List<String> data = new ArrayList<String>();
                                    data.add("请选择");
                                    for (int i = 0; i < sideMonitorTreeList.size(); i++) {
                                        String treeName = sideMonitorTreeList.get(i).name;
                                        List<SideMonitorType> sideMonitorTypeList = sideMonitorTreeList.get(i).listSideMonitorType;
                                        data.add(treeName);
                                        sideMonitorTypeMap.put(treeName, sideMonitorTypeList);

                                    }
                                    monitorTypeSpinner.setAdapter(new ArrayAdapter<String>(SlopeMonitorActivity.this,
                                            android.R.layout.simple_spinner_item, data));

                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });


                            initData();
                            addMapMarker(slopeList);
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
        addContentView(R.layout.activity_slope_monitor);
        toolbar.setTitle("边坡在线监测");
        setNavigationIcon(true);
        hidePagerNavigation(true);
        setAppBarLayoutScroll(false);
        initView();
        initEvent();

//        if (sweetAlertDialog==null) {
//            sweetAlertDialog =new SweetAlertDialog(this,SweetAlertDialog.PROGRESS_TYPE);
//            sweetAlertDialog.setTitleText("正在加载数据....");
//        }
//        sweetAlertDialog.show();

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        }).start();

        HttpclientUtil.getData(SlopeMonitorActivity.this, getSlopeIdUrl, handler, RESULT_CODE_1, false);


    }

    private void initEvent() {
        monitorTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) parent.getSelectedItem();
                if (sideMonitorTypeMap != null && sideMonitorTypeMap.size() > 0) {
                    List<SideMonitorType> sideMonitorTypes = sideMonitorTypeMap.get(selectedItem);
                    if (sideMonitorTypes != null) {
                        if (selectedItem.contains("温湿度")) {
                            String jspUrl = MyConstants.PRE_URL + "mt/monitoremergency/sidemonitor/temphumidata/listTempHumiDataPhone.jsp";
                            SideMonitorTypeActivity.startAction(SlopeMonitorActivity.this, selectedItem, sideMonitorTypes, jspUrl);
                        } else if (selectedItem.contains("地下水")) {
                            String jspUrl = MyConstants.PRE_URL + "mt/monitoremergency/sidemonitor/vibratingwiredata/listVibratingWireDataPhone.jsp";
                            SideMonitorTypeActivity.startAction(SlopeMonitorActivity.this, selectedItem, sideMonitorTypes, jspUrl);
                        } else if (selectedItem.contains("拉线式")) {
                            String jspUrl = MyConstants.PRE_URL + "mt/monitoremergency/sidemonitor/lvdtdata/listLVDTDataPhone.jsp";
                            SideMonitorTypeActivity.startAction(SlopeMonitorActivity.this, selectedItem, sideMonitorTypes, jspUrl);
                        } else if (selectedItem.contains("内部位移")) {
                            String jspUrl = MyConstants.PRE_URL + "mt/monitoremergency/sidemonitor/inclinationdata/listInclinationDataPhone.jsp";
                            SideMonitorTypeActivity.startAction(SlopeMonitorActivity.this, selectedItem, sideMonitorTypes, jspUrl);
                        } else if (selectedItem.contains("降雨量")) {
                            String jspUrl = MyConstants.PRE_URL + "mt/monitoremergency/sidemonitor/rainfalldata/listRainFallDataPhone.jsp";
                            SideMonitorTypeActivity.startAction(SlopeMonitorActivity.this, selectedItem, sideMonitorTypes, jspUrl);
                        } else if (selectedItem.contains("GPS")) {
                            String jspUrl = MyConstants.PRE_URL + "mt/monitoremergency/sidemonitor/cdmonitorsession/listCdMonitorSessionPhone.jsp";
                            SideMonitorTypeActivity.startAction(SlopeMonitorActivity.this, selectedItem, sideMonitorTypes, jspUrl);
                        }
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void addMapMarker(List<SideMonitorTree> sideMonitorTreeList) {
        SupportMapFragment mapFragment = (SupportMapFragment) fragmentList.get(0);
        for (int i = 0; i < sideMonitorTreeList.size(); i++) {
            SideMonitorTree sideMonitorTree = sideMonitorTreeList.get(i);
            Slope slope = sideMonitorTree.slope;
            LatLng latLng = new LatLng(Double.parseDouble(slope.latitude), Double.parseDouble(slope.longitude));
            if (i == 0) {
                mapFragment.getMap().moveCamera(CameraUpdateFactory.zoomTo(mapFragment.getMap().getMaxZoomLevel() - 3));
                mapFragment.getMap().moveCamera(CameraUpdateFactory.changeLatLng(latLng));
            }
            mapFragment.getMap().addMarker(new MarkerOptions().position(latLng).title("边坡路堤").snippet(slope.code));
        }

    }

    private void initData() {

        Bundle bundle = new Bundle();
        bundle.putSerializable("data", (Serializable) slopeList);
        bundle.putString("type", WarningMangePieChartFragment.TYPE_1);
        //地理位置
        SupportMapFragment mapFragment = new SupportMapFragment();
        try {
            MapsInitializer.initialize(this);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        //列表信息
        WraningManageFragment wraningManageFragment = new WraningManageFragment();
        wraningManageFragment.setArguments(bundle);


        //告警级别图形展示
        WarningMangePieChartFragment pieChartFragment = new WarningMangePieChartFragment();
        pieChartFragment.setArguments(bundle);

        //已确认/未确认图形展示
        WarningMangePieChartFragment pieChartFragment2 = new WarningMangePieChartFragment();
        Bundle bundle2 = new Bundle();
        bundle2.putString("type", WarningMangePieChartFragment.TYPE_2);
        pieChartFragment2.setArguments(bundle2);

        fragmentList.add(mapFragment);
        fragmentList.add(wraningManageFragment);
        fragmentList.add(pieChartFragment);
        fragmentList.add(pieChartFragment2);

        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);
            }

            @Override
            public int getCount() {
                return mTitle.length;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return mTitle[position];
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                super.instantiateItem(container, position);
                Fragment fragment = getItem(position);
                getSupportFragmentManager().beginTransaction().show(fragment).commitAllowingStateLoss();
                return fragment;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                Fragment fragment = getItem(position);
                getSupportFragmentManager().beginTransaction().hide(fragment).commitAllowingStateLoss();
            }

        });
        tabLayout.setupWithViewPager(viewPager);
    }

    private void initView() {
        slopeSpinner = (Spinner) findViewById(R.id.slope);
        monitorTypeSpinner = (Spinner) findViewById(R.id.monitorType);
        tabLayout = (TabLayout) findViewById(R.id.id_tabLayout);
        viewPager = (MyViewPager) findViewById(R.id.id_viewPager);
        viewPager.setScrollable(false);
    }
}
