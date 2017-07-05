package com.chengtech.chengtechmt.activity.integratequery;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.chengtech.chengtechmt.BaseActivity;
import com.chengtech.chengtechmt.R;
import com.chengtech.chengtechmt.activity.IView;
import com.chengtech.chengtechmt.activity.WebViewActivity;
import com.chengtech.chengtechmt.activity.business.ListPageActivity;
import com.chengtech.chengtechmt.activity.integratequery.routequery.RouteQueryActivity;
import com.chengtech.chengtechmt.adapter.RecycleViewAdapter2;
import com.chengtech.chengtechmt.entity.ProjectManagement;
import com.chengtech.chengtechmt.entity.Route;
import com.chengtech.chengtechmt.entity.Tree;
import com.chengtech.chengtechmt.hellochart.HelloChartActivity;
import com.chengtech.chengtechmt.impl.OnItemClickListener;
import com.chengtech.chengtechmt.presenter.QueryListPresenter;
import com.chengtech.chengtechmt.util.ACache;
import com.chengtech.chengtechmt.util.MyConstants;
import com.chengtech.chengtechmt.util.MyDialogUtil;
import com.chengtech.chengtechmt.view.TitleLayout;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryListActivity extends BaseActivity implements View.OnClickListener, IView<Object>, OnItemClickListener {

    public RecyclerView recyclerView;
    public List<Route> routes;
    private int flag = 1;


    //部门联动
    public List<String> firstDept;
    public Map<String, List<String>> secondDept;
    public int firstPosition;
    public Spinner firstSpinner;
    public Spinner secondSpinner;
    public ArrayAdapter<String> firstAdapter;
    public ArrayAdapter<String> secondAdapter;
    public List<Tree> trees;
    public String deptId = null;


    public QueryListPresenter presenter;
    public String type;

    public String url;
    public String appendUrl;
    public String treeUrl;
    public ACache aCache;

    private LinearLayout otherLayout;
    private TextView facility_a,facility_b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addContentView(R.layout.activity_query_list);

        Intent intent = getIntent();
        treeUrl = bundle.getString("treeUrl");
        url = bundle.getString("url");
        type = bundle.getString("type");

        initView();
        setNavigationIcon(true);


        initData();

        initEvent();

        presenter.getData(this, treeUrl, "Tree");

    }

    private void initEvent() {

    }

    private void initData() {
        firstDept = new ArrayList<String>();
        secondDept = new HashMap<String, List<String>>();

    }

    private void initView() {
        aCache = ACache.get(this);
        recyclerView = (RecyclerView) findViewById(R.id.recycleView);

        firstSpinner = (Spinner) findViewById(R.id.workSection_firstDept);
        secondSpinner = (Spinner) findViewById(R.id.workSection_secondDept);

        up_action.setOnClickListener(this);
        down_action.setOnClickListener(this);

        presenter = new QueryListPresenter(this, type);

        otherLayout = (LinearLayout) findViewById(R.id.otherLayout);
        facility_a = (TextView) findViewById(R.id.facility_a);
        facility_b = (TextView) findViewById(R.id.facility_b);

    }


    @Override
    public void onClick(View v) {
        flag = 0;
        switch (v.getId()) {
            case R.id.perPage:
                if (pageNo != 1 && !TextUtils.isEmpty(url)) {
                    pageNo--;
                    pageNo_tv.setText(pageNo + "");
                    presenter.getData(QueryListActivity.this, appendUrl + pageNo, type);
                } else {
                    Toast.makeText(this, "当前是最新页", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.nextPage:
                if (pageNo < maxPage && !TextUtils.isEmpty(url)) {
                    pageNo++;
                    pageNo_tv.setText(pageNo + "");
                    presenter.getData(QueryListActivity.this, appendUrl + pageNo, type);
                } else {
                    Toast.makeText(QueryListActivity.this, "当前已经是最后一页", Toast.LENGTH_SHORT).show();
                }
                break;
        }

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
    public void loadDataSuccess(Object o) {

    }

    @Override
    public void loadDataSuccess(Object o, int type) {

    }

    @Override
    public void loadDataSuccess(Object object, String type) {

        switch (type) {
            case "Dept":
                trees = (List<Tree>) object;
                setSpinnerData();
                break;
            case "Route":
                otherLayout.setVisibility(View.GONE);
                routes = (List<Route>) object;
                if (routes.size() == pageSize)
                    maxPage++;

                List<String[]> titleArrayList2 = new ArrayList<>();
                if (routes != null && routes.size() > 0) {
                    for (Route r : routes) {
                        titleArrayList2.add(r.getItemTitle());
                    }
                } else {
                    if (flag ==0) {
                        pageNo--;
                        maxPage--;
                    }
                    Toast.makeText(QueryListActivity.this, "没有数据", Toast.LENGTH_SHORT).show();
                    pageNo_tv.setText(pageNo+"");

                }
                RecycleViewAdapter2 adapter2 = new RecycleViewAdapter2(this, titleArrayList2, R.layout.item_recycle3);
                recyclerView.setAdapter(adapter2);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                adapter2.setOnItemClickListener(this);
                break;
            case "Facilityquantity":

                //隐藏页码导航
                hidePagerNavigation(true);
                recyclerView.setVisibility(View.GONE);
                otherLayout.setVisibility(View.VISIBLE);

                facility_a.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(QueryListActivity.this, WebViewActivity.class);
                        intent.putExtra("url",appendUrl);
                        intent.putExtra("title","设施量情况表");
                        startActivity(intent);
                    }
                });
                facility_b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(QueryListActivity.this, HelloChartActivity.class);
                        intent.putExtra("url",MyConstants.PRE_URL+"mt/integratequery/basicdataquery/facilityquantityquery/getGraph.action?mobile=phone&sectionId=&routeId=&deptIds="+deptId);
                        intent.putExtra("title","设施量图表");
                        startActivity(intent);
                    }
                });
                break;
        }
    }

    @Override
    public void loadDataFailure() {
        Toast.makeText(QueryListActivity.this, "连接服务器失败。", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void hasError() {

    }

    //设置两个spinner的内容
    protected void setSpinnerData() {
        for (Tree t : trees) {
            MyConstants.deptTree.put(t.id, t.text);
            if (t.id.equals(t.secondDeptId)) {
                firstDept.add(t.text);
                List<String> temp = new ArrayList<String>();
                for (Tree t2 : trees) {
                    if (t.id.equals(t2.parentId)) {
                        temp.add(t2.text);
                    }
                }
                temp.add("全选");
                secondDept.put(t.text, temp);
            }
        }

        firstAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, firstDept);
        firstSpinner.setAdapter(firstAdapter);

        firstSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int position, long arg3) {
                secondAdapter = new ArrayAdapter<String>(QueryListActivity.this, android.R.layout.simple_spinner_item, secondDept.get(firstDept.get(position)));
                secondSpinner.setAdapter(secondAdapter);
                firstPosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        secondSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int position, long arg3) {
                String selectedDept = secondDept.get(firstDept.get(firstPosition)).get(position);
                if (selectedDept.equals("全选")) {
                    for (Tree t : trees) {
                        if (t.text.equals(firstDept.get(firstPosition))) {
                            deptId = t.id;
//                            for (Tree t2 : trees) {
//                                if (t.id.equals(t2.parentId))
//                                    deptId = deptId + "\',\'" + t2.id;
//
//                            }
                            break;
                        }
                    }
                } else {
                    for (Tree t : trees) {
                        if (t.text.equals(selectedDept)) {
                            deptId = t.id;
                            break;
                        }
                    }
                }
                //重新置为第一页
                pageNo = 1;
                maxPage = 1;
                pageNo_tv.setText(pageNo+"");
                appendUrl = url + "?mobile=phone&deptIds=" + deptId /*+ "&pager.pageSize="+pageSize+"&pager.pageNo="*/;
                if (type.equals("Facilityquantity")) {
                    loadDataSuccess(null, type);
                    return;
                }

                flag = 1;
                presenter.getData(QueryListActivity.this, appendUrl+"&pager.pageSize="+pageSize+"&pager.pageNo=" + pageNo, type);

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {
        switch (type) {
            case "Route":
                Intent intent = new Intent(this, RouteQueryActivity.class);
                intent.putExtra("data", routes.get(position));
                intent.putExtra("deptId",deptId);
                startActivity(intent);
                break;
        }

    }
}
