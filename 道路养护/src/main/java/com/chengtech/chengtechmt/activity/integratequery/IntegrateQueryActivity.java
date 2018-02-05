package com.chengtech.chengtechmt.activity.integratequery;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.chengtech.chengtechmt.BaseActivity;
import com.chengtech.chengtechmt.R;
import com.chengtech.chengtechmt.activity.integratequery.gis.DiseaseRecord.DiseaseRecordActivity;
import com.chengtech.chengtechmt.activity.integratequery.gis.MaintenanceSituation.MaintenanceSituationActivity;
import com.chengtech.chengtechmt.adapter.MyExpandableAdapter;
import com.chengtech.chengtechmt.presenter.IntegrateQueryPresenter;
import com.chengtech.chengtechmt.util.MyConstants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IntegrateQueryActivity extends BaseActivity  {
    private ExpandableListView expandableListView;
    private MyExpandableAdapter expandableAdapter;
    private Map<String, List<String>> groupData;
    private String[] urls;
    private IntegrateQueryPresenter presenter;
    private List<String> urlList = new ArrayList<>();
    private String[] groupName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addContentView(R.layout.activity_integrate_query);

        initView();

        setNavigationIcon(true);
        hidePagerNavigation(true);
        setAppBarLayoutScroll(false);
        initData();

        groupData.put(groupName[0], Arrays.asList(getResources().getStringArray(R.array.interquery_dbm)));
        groupData.put(groupName[2], Arrays.asList(new String[]{"评定数据", "维修实施情况", "病害分析查询"}));
        expandableAdapter = new MyExpandableAdapter(this, groupName, groupData);
        int width = getResources().getDisplayMetrics().widthPixels;
        expandableListView.setIndicatorBounds(width - 150, width - 70);
        expandableListView.setAdapter(expandableAdapter);
        expandableListView.expandGroup(0);
        expandableListView.expandGroup(2);
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Boolean hasRights = MyConstants.userRights.get(groupData.get(groupName[groupPosition]).get(childPosition));
//                if (hasRights != null && hasRights) {
                    onExchangData(groupData.get(groupName[groupPosition]).get(childPosition));
//                } else {
//                    Toast.makeText(IntegrateQueryActivity.this, "没有权限", Toast.LENGTH_SHORT).show();
//                }
                return false;
            }
        });
    }

    private void initView() {
        expandableListView = (ExpandableListView) findViewById(R.id.expandlist);
//        presenter = new IntegrateQueryPresenter(this);

    }

    private void initData() {
        groupName = new String[]{"基础数据查询", "业务数据查询", "GIS可视化查询"};
        urls = new String[]{MyConstants.PRE_URL + "ms/common/subMenuList.action"};
        groupData = new HashMap<>();

    }

//    @Override
//    public void showDialog() {
//        loadDialog.show();
//    }
//
//    @Override
//    public void dismssDialog() {
//        loadDialog.dismiss();
//    }
//
//    @Override
//    public void loadDataSuccess(List<Menu> menus) {
//
//        SubMenuDialogFragment dialogFragment = new SubMenuDialogFragment();
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("groupData", (Serializable) menus);
//        dialogFragment.setArguments(bundle);
//        dialogFragment.show(getFragmentManager(), "SubMenuDialogFragment");
//    }
//
//    @Override
//    public void loadDataSuccess(List<Menu> menus, int type) {
//
//    }
//
//    @Override
//    public void loadDataSuccess(List<Menu> menus, String clasName) {
//
//    }
//
//    @Override
//    public void loadDataFailure() {
//        Toast.makeText(this, "服务器连接出错", Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void hasError() {
//        UserUtils.reLogin(IntegrateQueryActivity.this, loadDialog);
//    }
//
//    @Override
    public void onExchangData(Object object) {

        String itemName = (String) object;
        if (!TextUtils.isEmpty(itemName)) {
            Intent intent = new Intent(this, QueryListActivity.class);
            switch (itemName) {
                case "道路数据":
                    intent.putExtra("treeUrl", MyConstants.PRE_URL + "mt/integratequery/basicdataquery/routedataquery/getTree.action");
                    intent.putExtra("url", MyConstants.PRE_URL + "mt/integratequery/basicdataquery/routedataquery/listRouteJson.action");
                    intent.putExtra("type", "Route");
                    intent.putExtra("title", "道路数据查询");
                    startActivity(intent);
                    break;
                case "设施量统计":
                    intent.putExtra("treeUrl", MyConstants.PRE_URL + "mt/integratequery/basicdataquery/routedataquery/getTree.action");
                    intent.putExtra("url", MyConstants.PRE_URL + "mt/integratequery/basicdataquery/facilityquantityquery/routeDbmQueryReport.action");
                    intent.putExtra("type", "Facilityquantity");
                    intent.putExtra("title", "设施量统计");
                    startActivity(intent);
                    break;
                case "维修实施情况" :
                    MaintenanceSituationActivity.startAction(this);
                    break;
                case "病害分析查询" :
                    DiseaseRecordActivity.startAction(this);
                    break;
                default:
                    Toast.makeText(this, "该模块未开放", Toast.LENGTH_SHORT).show();
                    break;
            }


        }

    }

}
