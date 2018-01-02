package com.chengtech.chengtechmt.activity.expertdecision;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.chengtech.chengtechmt.BaseActivity;
import com.chengtech.chengtechmt.R;
import com.chengtech.chengtechmt.activity.IView;
import com.chengtech.chengtechmt.adapter.RecycleViewAdapter2;
import com.chengtech.chengtechmt.entity.expertdecision.BitumenRoadDamage;
import com.chengtech.chengtechmt.entity.expertdecision.CementRoadDamage;
import com.chengtech.chengtechmt.entity.expertdecision.Evaluationdetail;
import com.chengtech.chengtechmt.presenter.ExpertDecisionPresenter;
import com.chengtech.chengtechmt.presenter.Presenter;
import com.chengtech.chengtechmt.util.DateUtils;
import com.chengtech.nicespinner.NiceSpinner;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 评定明细
 */
public class EvaluationdetailListActivity extends BaseActivity implements IView<Object>, View.OnClickListener {
    private NiceSpinner firstSpinner, secondSpinner, thirdSpinner, fourthSpinner;
    private List<String> fristData, secondData, thirdData, fourthData;
    private String urlParams;
    private String type;
    private ExpertDecisionPresenter presenter;
    private List<Evaluationdetail> evaluationdetailList;
    private EditText et1, et2;
    private TextView tv1, tv2, tv3, tv4, tv5, tv6;
    private RecyclerView recyclerView;
    private AlertDialog deptDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addContentView(R.layout.activity_evaluationdetail_list);

        setNavigationIcon(true);

        type = getIntent().getStringExtra("type");
        initData();
        initView();


    }

    private void initView() {

        presenter = new ExpertDecisionPresenter(this, type);
        tv1 = (TextView) findViewById(R.id.tv1);
        tv2 = (TextView) findViewById(R.id.tv2);
        tv3 = (TextView) findViewById(R.id.tv3);
        tv4 = (TextView) findViewById(R.id.tv4);
        tv5 = (TextView) findViewById(R.id.tv5);
        tv6 = (TextView) findViewById(R.id.tv6);
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

    private void initData() {
        fristData = Arrays.asList(new String[]{"请选择", "G105", "G106", "G107"});
        secondData = Arrays.asList(new String[]{"请选择", "车道1", "车道2", "车道3", "车道4"});
        thirdData = Arrays.asList(new String[]{"请选择", "上行", "下行"});
        fourthData = Arrays.asList(new String[]{"请选择", "2014", "2015", "2016", "2017", "2018", "2019", "2020"});

    }

    private void inflateSpnnier() {
        if (deptDialog != null) {
            deptDialog.show();
            return;
        }
        View view = LayoutInflater.from(this).inflate(R.layout.evaluation_detail_spinner, null);
        firstSpinner = (NiceSpinner) view.findViewById(R.id.firstSpinner);
        secondSpinner = (NiceSpinner) view.findViewById(R.id.secondSpinner);
        thirdSpinner = (NiceSpinner) view.findViewById(R.id.thirdSpinner);
        fourthSpinner = (NiceSpinner) view.findViewById(R.id.fourthSpinner);
        et1 = (EditText) view.findViewById(R.id.et1);
        et2 = (EditText) view.findViewById(R.id.et2);
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
                String startStake = et1.getText().toString();
                String endStake = et2.getText().toString();
                if ("请选择".equals(routeCode)) {
                    Toast.makeText(EvaluationdetailListActivity.this, "请选择路线！", Toast.LENGTH_SHORT).show();
                    cancleDialog(dialog, false);
                    return;
                }
                if ("请选择".equals(lane)) {
                    Toast.makeText(EvaluationdetailListActivity.this, "请选择车道！", Toast.LENGTH_SHORT).show();
                    cancleDialog(dialog, false);
                    return;
                }
                if ("请选择".equals(direction)) {
                    Toast.makeText(EvaluationdetailListActivity.this, "请选择方向！", Toast.LENGTH_SHORT).show();
                    cancleDialog(dialog, false);
                    return;
                }
                if ("请选择".equals(year)) {
                    Toast.makeText(EvaluationdetailListActivity.this, "请选择年份！", Toast.LENGTH_SHORT).show();
                    cancleDialog(dialog, false);
                    return;
                }

                urlParams = "?year=" + year + "&investigateDirection=" + direction + "&startStake=" + startStake + "&endStake=" + endStake
                        + "&routeCode=" + routeCode + "&laneType=" + lane + "&collectFlag=0&sort=startStake&direction=asc" + "&pager.pageSize=" + pageSize;
                presenter.getData(EvaluationdetailListActivity.this, urlParams + "&pager.pageNo=" + pageNo, type);

            }
        });

        firstSpinner.attachDataSource(fristData);
        secondSpinner.attachDataSource(secondData);
        thirdSpinner.attachDataSource(thirdData);
        fourthSpinner.attachDataSource(fourthData);
        int[] ints = DateUtils.calculateDate();
        fourthSpinner.setText(String.valueOf(ints[0]));
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cancleDialog(dialog, true);
            }
        });
        deptDialog = builder.create();
        deptDialog.show();

    }

    private void cancleDialog(DialogInterface dialog, boolean isShow) {
        try {
            Field mShowing = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
            mShowing.setAccessible(true);
            mShowing.set(dialog, isShow);
        } catch (Exception e) {
            e.printStackTrace();
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
    public void loadDataSuccess(Object object, String className) {
        switch (className) {
            case "Evaluationdetail":
                evaluationdetailList = (List<Evaluationdetail>) object;

                if (evaluationdetailList != null && evaluationdetailList.size() == pageSize)
                    maxPage++;

                tv1.setText("路线名称：" + firstSpinner.getText().toString());
                tv2.setText("技术等级：" + evaluationdetailList.get(0).techLevel);
                tv4.setText("车道：" + secondSpinner.getText().toString());
                tv5.setText("监测方向：" + thirdSpinner.getText().toString());
                tv6.setText("监测时间：" + fourthSpinner.getText().toString());

                Map hashMap = new HashMap<String, String>();
                for (int i = 0; i < evaluationdetailList.size(); i++) {
                    hashMap.put(evaluationdetailList.get(i).roadType, evaluationdetailList.get(i).roadType);
                }
                Object[] roadType = hashMap.keySet().toArray();
                String s = "";
                for (int i = 0; i < roadType.length; i++) {
                    s += (i == 0 ? "" : "/") + String.valueOf(roadType[i]);
                }
                tv3.setText("路面类型：" + s);
                List<String[]> titleArrayList2 = new ArrayList<>();
                if (evaluationdetailList != null && evaluationdetailList.size() > 0) {
                    for (Evaluationdetail e : evaluationdetailList) {
                        String[] titleArray = new String[12];
                        titleArray[0] = "路段桩号：" + e.pileNumber;
                        titleArray[1] = "长度（M）：" + e.roadLength;
                        titleArray[2] = "<br/>MQI：" + e.mqi;
                        titleArray[3] = "<br/>路面PQI：" + e.pqi;
                        titleArray[4] = "<br/>路面分项指标PCI：" + (e.pci == -100.0 ? "--" : e.pci);
                        titleArray[5] = "<br/>路面分项指标RQI：" + (e.rqi == -100.0 ? "--" : e.rqi);
                        titleArray[6] = "<br/>路面分项指标RDI：" + (e.rdi == -100.0 ? "--" : e.rdi);
                        titleArray[7] = "<br/>路面分项指标SRI：" + (e.sri == -100.0 ? "--" : e.sri);
                        titleArray[8] = "<br/>路面分项指标PSSI：" + (e.pssi == -100.0 ? "--" : e.pssi);
                        titleArray[9] = "<br/>路基SCI：" + (e.sci == -100.0 ? "--" : e.sci);
                        titleArray[10] = "<br/>桥隧构造物BCI：" + (e.bci == -100.0 ? "--" : e.bci);
                        titleArray[11] = "<br/>沿线设施TCI：" + (e.tci == -100.0 ? "--" : e.tci);
                        titleArrayList2.add(titleArray);
                    }
                } else {
                    Toast.makeText(EvaluationdetailListActivity.this, "没有数据", Toast.LENGTH_SHORT).show();
                    pageNo--;
                    pageNo_tv.setText(pageNo + "");
                    maxPage--;
                }
                RecycleViewAdapter2 adapter2 = new RecycleViewAdapter2(this, titleArrayList2, R.layout.item_recycle3);
                recyclerView.setAdapter(adapter2);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
////                recyclerView.addItemDecoration(new RecycleViewDivider(this,LinearLayout.VERTICAL));
//                adapter2.setOnItemClickListener(this);

                break;
            case "CementRoadDamage":

                break;

        }
    }

    @Override
    public void loadDataFailure() {

    }

    @Override
    public void hasError() {

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
}
