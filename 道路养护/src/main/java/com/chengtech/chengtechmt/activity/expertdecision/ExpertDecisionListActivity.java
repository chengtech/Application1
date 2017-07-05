package com.chengtech.chengtechmt.activity.expertdecision;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.chengtech.chengtechmt.BaseActivity;
import com.chengtech.chengtechmt.R;
import com.chengtech.chengtechmt.activity.IView;
import com.chengtech.chengtechmt.activity.WebViewActivity;
import com.chengtech.chengtechmt.activity.business.ListPageActivity;
import com.chengtech.chengtechmt.activity.dbm.DbmListActivity;
import com.chengtech.chengtechmt.activity.integratequery.QueryListActivity;
import com.chengtech.chengtechmt.adapter.HorizotalScrollAdapter;
import com.chengtech.chengtechmt.adapter.RecycleViewAdapter2;
import com.chengtech.chengtechmt.entity.ProjectManagement;
import com.chengtech.chengtechmt.entity.expertdecision.BitumenRoadDamage;
import com.chengtech.chengtechmt.entity.expertdecision.CementRoadDamage;
import com.chengtech.chengtechmt.impl.OnItemClickListener;
import com.chengtech.chengtechmt.presenter.ExpertDecisionPresenter;
import com.chengtech.chengtechmt.util.MyConstants;
import com.chengtech.nicespinner.NiceSpinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExpertDecisionListActivity extends BaseActivity implements IView<Object>, View.OnClickListener,OnItemClickListener {

    private NiceSpinner firstSpinner, secondSpinner, thirdSpinner, fourthSpinner, fifthSpinner;
    private List<String> fristData, secondData, thirdData, fourthData, fifthData;
    private ExpertDecisionPresenter presenter;
    private String type;
    private RecyclerView recyclerView;
    private String urlParams;
    private String bitumenUrl = "mt/expertdecision/roadtechevaluation/bitumenroaddamage/addEditBitumenRoadDamage.action?isRead=true&mobile=phone&id=";
    private String cementUrl = "mt/expertdecision/roadtechevaluation/cementroaddamage/addEditCementRoadDamage.action?isRead=true&mobile=phone&id=";
    private List<BitumenRoadDamage> bitumenRoadDamageList;
    private List<CementRoadDamage> cementRoadDamageList;
    private AlertDialog deptDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addContentView(R.layout.activity_expert_decision_list);

        setNavigationIcon(true);

        type = getIntent().getStringExtra("type");
        initView();
        initData();

    }

    private void initData() {
        fristData = Arrays.asList(new String[]{"请选择", "G105", "G106", "G107"});
        secondData = Arrays.asList(new String[]{"请选择", "车道1", "车道2", "车道3", "车道4"});
        thirdData = Arrays.asList(new String[]{"请选择", "上行", "下行"});
        fourthData = Arrays.asList(new String[]{"请选择", "2014", "2015", "2016", "2017", "2018", "2019", "2020"});
        fifthData = Arrays.asList(new String[]{"请选择", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"});

    }

    private void initView() {
        presenter = new ExpertDecisionPresenter(this, type);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        up_action.setOnClickListener(this);
        down_action.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_work_section, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.filter) {
            inflateSpnnier();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 创建条件筛选框
     */
    private void inflateSpnnier() {
        if (deptDialog!=null) {
            deptDialog.show();
            return;
        }
        View view = LayoutInflater.from(this).inflate(R.layout.expert_decision_spinner, null);
        firstSpinner = (NiceSpinner) view.findViewById(R.id.firstSpinner);
        secondSpinner = (NiceSpinner) view.findViewById(R.id.secondSpinner);
        thirdSpinner = (NiceSpinner) view.findViewById(R.id.thirdSpinner);
        fourthSpinner = (NiceSpinner) view.findViewById(R.id.fourthSpinner);
        fifthSpinner = (NiceSpinner) view.findViewById(R.id.fifthSpinner);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                pageNo = 1;
                pageNo_tv.setText(String.valueOf(pageNo));
                String routeCode = firstSpinner.getText().toString();
                String lane = secondSpinner.getText().toString();
                String direction = thirdSpinner.getText().toString();
                String year = fourthSpinner.getText().toString();
                String month = fifthSpinner.getText().toString();
                if ("请选择".equals(routeCode)) {
                    routeCode = "";
                }
                if ("请选择".equals(lane)) {
                    lane = "";
                }
                if ("请选择".equals(direction)) {
                    direction = "";
                }
                if ("请选择".equals(year)) {
                    year = "";
                }
                if ("请选择".equals(month)) {
                    month = "";
                }

                urlParams = "?year=" + year + "&month=" + month + "&investigateDirection=" + direction
                        + "&routeCode=" + routeCode + "&laneType=" + lane + "&sort=startStake&direction=desc" + "&pager.pageSize=" + pageSize;
                presenter.getData(ExpertDecisionListActivity.this, urlParams + "&pager.pageNo=" + pageNo, type);

            }
        });

        firstSpinner.attachDataSource(fristData);
        secondSpinner.attachDataSource(secondData);
        thirdSpinner.attachDataSource(thirdData);
        fourthSpinner.attachDataSource(fourthData);
        fifthSpinner.attachDataSource(fifthData);
        builder.setNegativeButton("取消", null);
        deptDialog = builder.create();
        deptDialog.show();
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
    public void loadDataSuccess(Object object, String className) {
        switch (className) {
            case "BitumenRoadDamage":
                bitumenRoadDamageList = (List<BitumenRoadDamage>) object;

                if (bitumenRoadDamageList != null && bitumenRoadDamageList.size() == pageSize)
                    maxPage++;
                List<String[]> titleArrayList2 = new ArrayList<>();
                if (bitumenRoadDamageList != null && bitumenRoadDamageList.size() > 0) {
                    for (BitumenRoadDamage b : bitumenRoadDamageList) {
                        String[] titleArray = new String[7];
                        titleArray[0] = "路线编号:" + b.routeCode;
                        titleArray[1] = "起点桩号:" + b.startStake + " \t" +
                                "终点桩号：" + b.endStake;
                        titleArray[2] = "<br/>调查方向：" + b.investigateDirection + "\t\t\t调查时间:" + b.investigateTime;
                        titleArray[3] = "<br/>调查人员:" + b.investigator;
                        titleArray[4] = "<br/>路面宽度(m):" + b.width;
                        titleArray[5] = "<br/>路段长度(m):" + b.length;
                        titleArray[6] = "<br/><font color=\"red\" size=\"5\">DR:" + b.drValue + "\t\t\t PCI:" + b.pciValue + "</font>";

                        titleArrayList2.add(titleArray);
                    }
                } else {
                    Toast.makeText(ExpertDecisionListActivity.this, "没有数据", Toast.LENGTH_SHORT).show();
                    pageNo--;
                    pageNo_tv.setText(pageNo + "");
                    maxPage--;
                }
                RecycleViewAdapter2 adapter2 = new RecycleViewAdapter2(this, titleArrayList2, R.layout.item_recycle3);
                recyclerView.setAdapter(adapter2);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
//                recyclerView.addItemDecoration(new RecycleViewDivider(this,LinearLayout.VERTICAL));
                adapter2.setOnItemClickListener(this);

                break;
            case "CementRoadDamage":
                cementRoadDamageList = (List<CementRoadDamage>) object;
                if (cementRoadDamageList != null && cementRoadDamageList.size() == pageSize)
                    maxPage++;
                List<String[]> titleArrayList3 = new ArrayList<>();
                if (cementRoadDamageList != null && cementRoadDamageList.size() > 0) {
                    for (CementRoadDamage b : cementRoadDamageList) {
                        String[] titleArray = new String[7];
                        titleArray[0] = "路线编号:" + b.routeCode;
                        titleArray[1] = "起点桩号:" + b.startStake + " \t" +
                                "终点桩号：" + b.endStake;
                        titleArray[2] = "<br/>调查方向：" + b.investigateDirection + "\t\t\t调查时间:" + b.investigateTime;
                        titleArray[3] = "<br/>调查人员:" + b.investigator;
                        titleArray[4] = "<br/>路段长度(m):" + b.length;
                        titleArray[5] = "<br/>路面宽度(m):" + b.width;
                        titleArray[6] = "<br/><font color=\"red\" size=\"5\">DR:" + b.drValue + "\t\t\t PCI:" + b.pciValue + "</font>";

                        titleArrayList3.add(titleArray);
                    }
                } else {
                    Toast.makeText(ExpertDecisionListActivity.this, "没有数据", Toast.LENGTH_SHORT).show();
                    pageNo--;
                    pageNo_tv.setText(pageNo + "");
                    maxPage--;
                }
                RecycleViewAdapter2 cementRoadDamageAdapter = new RecycleViewAdapter2(this, titleArrayList3, R.layout.item_recycle3);
                recyclerView.setAdapter(cementRoadDamageAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
//                recyclerView.addItemDecoration(new RecycleViewDivider(this,LinearLayout.VERTICAL));
                cementRoadDamageAdapter.setOnItemClickListener(this);

                break;

        }
    }

    @Override
    public void loadDataFailure() {
        Toast.makeText(this, "数据结构解释出错", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void hasError() {
        Toast.makeText(this, "数据结构解释出错", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.perPage:
                if (pageNo != 1) {
                    pageNo--;
                    pageNo_tv.setText(pageNo + "");
                    presenter.getData(this, urlParams + "&pager.pageNo=" + pageNo, type);
                } else {
                    Toast.makeText(this, "当前是最新页", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.nextPage:
                if (pageNo < maxPage) {
                    pageNo++;
                    pageNo_tv.setText(pageNo + "");
                    presenter.getData(this, urlParams + "&pager.pageNo=" + pageNo, type);
                } else {
                    Toast.makeText(this, "当前已经是最后一页", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        switch (type) {
            case "BitumenRoadDamage":
                if (bitumenRoadDamageList!=null) {
                    Intent intent = new Intent(this, WebViewActivity.class);
                    intent.putExtra("url", MyConstants.PRE_URL+bitumenUrl+bitumenRoadDamageList.get(position).id);
                    intent.putExtra("title","沥青路面损坏调查表");
                    startActivity(intent);
                }
                break;
            case "CementRoadDamage":
                if (cementRoadDamageList!=null) {
                    Intent intent = new Intent(this, WebViewActivity.class);
                    intent.putExtra("url", MyConstants.PRE_URL+cementUrl+cementRoadDamageList.get(position).id);
                    intent.putExtra("title","水泥路面损坏调查表");
                    startActivity(intent);
                }
                break;
        }
    }
}
