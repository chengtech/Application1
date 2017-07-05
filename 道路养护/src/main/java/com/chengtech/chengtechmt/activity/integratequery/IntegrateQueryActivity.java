package com.chengtech.chengtechmt.activity.integratequery;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.chengtech.chengtechmt.BaseActivity;
import com.chengtech.chengtechmt.R;
import com.chengtech.chengtechmt.activity.IView;
import com.chengtech.chengtechmt.activity.dbm.DbmActivity;
import com.chengtech.chengtechmt.adapter.MyExpandableAdapter;
import com.chengtech.chengtechmt.entity.Menu;
import com.chengtech.chengtechmt.fragment.SubMenuDialogFragment;
import com.chengtech.chengtechmt.presenter.IntegrateQueryPresenter;
import com.chengtech.chengtechmt.util.MyConstants;
import com.chengtech.chengtechmt.util.UserUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IntegrateQueryActivity extends BaseActivity implements IView<List<Menu>>,SubMenuDialogFragment.ExchangeDataListener{
//    private RecyclerView recyclerView;
    private ExpandableListView expandableListView;
    private MyExpandableAdapter expandableAdapter;
    private Map<String, List<String>> groupData;
    private String [] subTitle;
//    private int [] imageIds;
    private String [] urls;
    private String [] ids;
    private IntegrateQueryPresenter presenter;
    private List<String> urlList = new ArrayList<>();
    private String [] groupName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addContentView(R.layout.activity_integrate_query);

        initView();

        setNavigationIcon(true);
        hidePagerNavigation(true);
        setAppBarLayoutScroll(false);
        initData();

//        RecycleViewAdapter recycleViewAdapter = new RecycleViewAdapter(this,subTitle,imageIds,R.layout.item_recycle1);
//        recyclerView.setAdapter(recycleViewAdapter);
//        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

//        recycleViewAdapter.onItemClick(new RecycleViewAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                if (position==2) {
//                    Toast.makeText(IntegrateQueryActivity.this, "该模块未完善。", Toast.LENGTH_SHORT).show();
//                }else {
//                    presenter.getData(IntegrateQueryActivity.this,ids[position],urls[0]);
//
//                }
//            }
//
//            @Override
//            public void setOnItemLongClickListener(View view, int position) {
//                Toast.makeText(IntegrateQueryActivity.this, "长按事件，位置："+position, Toast.LENGTH_SHORT).show();
//            }
//        });
        groupData.put(groupName[0],Arrays.asList(getResources().getStringArray(R.array.interquery_dbm))) ;
        expandableAdapter = new MyExpandableAdapter(this, groupName, groupData);
        int width = getResources().getDisplayMetrics().widthPixels;
        expandableListView.setIndicatorBounds(width - 150, width - 70);
        expandableListView.setAdapter(expandableAdapter);
        expandableListView.expandGroup(0);
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Boolean hasRights = MyConstants.userRights.get(groupData.get(groupName[groupPosition]).get(childPosition));
                if (hasRights !=null && hasRights) {
                    onExchangData(groupData.get(groupName[groupPosition]).get(childPosition));
                }else {
                    Toast.makeText(IntegrateQueryActivity.this, "没有权限", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
    }

    private void initView() {
//        recyclerView = (RecyclerView) findViewById(R.id.recycleView);
        expandableListView = (ExpandableListView) findViewById(R.id.expandlist);
        presenter = new IntegrateQueryPresenter(this);

    }

    private void initData() {
        subTitle = new String[] {"基础数据查询","业务数据查询","GIS可视化查询"};
        groupName = new String[] {"基础数据查询","业务数据查询","GIS可视化查询"};
//        imageIds = new int[ ] {R.mipmap.data_search,R.mipmap.search_book,R.mipmap.integrate_query_map};
        urls = new String[] {MyConstants.PRE_URL+"ms/common/subMenuList.action"};
        ids = new String[] {"40288a6749cffccf0149d1a5488a001a","40288a67497452770149745ee5d30071"};
        groupData = new HashMap<>();

    }

    @Override
    public void showDialog() {
        loadDialog.show();
    }

    @Override
    public void dismssDialog() {
        loadDialog.dismiss();
    }

    @Override
    public void loadDataSuccess(List<Menu> menus) {
//        RecycleViewAdapter adapter2 = new RecycleViewAdapter(this,menus,null,R.layout.item_recycle1);
//        recyclerView.setAdapter(adapter2);
//        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
//        adapter2.onItemClick(new RecycleViewAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//
//            }
//
//            @Override
//            public void setOnItemLongClickListener(View view, int position) {
//
//            }
//        });

        SubMenuDialogFragment dialogFragment = new SubMenuDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("groupData", (Serializable) menus);
        dialogFragment.setArguments(bundle);
        dialogFragment.show(getFragmentManager(),"SubMenuDialogFragment");
    }

    @Override
    public void loadDataSuccess(List<Menu> menus, int type) {

    }

    @Override
    public void loadDataSuccess(List<Menu> menus, String clasName) {

    }

    @Override
    public void loadDataFailure() {
        Toast.makeText(this, "服务器连接出错", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void hasError() {
        UserUtils.reLogin(IntegrateQueryActivity.this, loadDialog);
    }

    @Override
    public void onExchangData(Object object) {
//        Toast.makeText(IntegrateQueryActivity.this, "点击位置"+object, Toast.LENGTH_SHORT).show();

        String itemName = (String) object;
        if (!TextUtils.isEmpty(itemName)){
            Intent intent = new Intent(this,QueryListActivity.class);
            switch (itemName) {
                case "道路数据" :
                    intent.putExtra("treeUrl",MyConstants.PRE_URL + "mt/integratequery/basicdataquery/routedataquery/getTree.action");
                    intent.putExtra("url",MyConstants.PRE_URL+"mt/integratequery/basicdataquery/routedataquery/listRouteJson.action");
                    intent.putExtra("type","Route");
                    intent.putExtra("title","道路数据查询");
                    startActivity(intent);
                    break;
                case "设施量统计" :
                    intent.putExtra("treeUrl",MyConstants.PRE_URL+"mt/integratequery/basicdataquery/routedataquery/getTree.action");
                    intent.putExtra("url",MyConstants.PRE_URL+"mt/integratequery/basicdataquery/facilityquantityquery/routeDbmQueryReport.action");
                    intent.putExtra("type","Facilityquantity");
                    intent.putExtra("title","设施量统计");
                    startActivity(intent);
                    break;
                default:
                    Toast.makeText(this,"该模块未开放",Toast.LENGTH_SHORT).show();
                    break;
            }


        }

    }

}
