package com.chengtech.chengtechmt.activity.business;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.chengtech.chengtechmt.BaseActivity;
import com.chengtech.chengtechmt.R;
import com.chengtech.chengtechmt.activity.IView;
import com.chengtech.chengtechmt.activity.dbm.DbmListActivity;
import com.chengtech.chengtechmt.adapter.RecycleViewAdapter2;
import com.chengtech.chengtechmt.divider.RecycleViewDivider;
import com.chengtech.chengtechmt.entity.Dept;
import com.chengtech.chengtechmt.entity.MediumPlanprogress;
import com.chengtech.chengtechmt.entity.ProjectManagement;
import com.chengtech.chengtechmt.entity.Tree;
import com.chengtech.chengtechmt.entity.gson.DeptG;
import com.chengtech.chengtechmt.impl.OnItemClickListener;
import com.chengtech.chengtechmt.presenter.ListPagePre;
import com.chengtech.chengtechmt.util.CommonUtils;
import com.chengtech.chengtechmt.util.MyConstants;
import com.chengtech.chengtechmt.util.MyDialogUtil;
import com.chengtech.chengtechmt.util.UserUtils;
import com.chengtech.chengtechmt.view.TitleLayout;
import com.chengtech.nicespinner.NiceSpinner;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * 小额专项维修，大中修list页面
 */
public class ListPageActivity extends BaseActivity implements IView<Object>, View.OnClickListener, OnItemClickListener {

    //    public EditText year_et;
//    public Button search_bt;
    private String[] years;
    public String yearString;
    //    public ImageView delete_iv;
    public DeptG deptG;
    public List<MediumPlanprogress> mediumPlanprogressList;
    public List<ProjectManagement> projectManagementList;

    private List<String> firstDept;
    private Map<String, List<String>> secondDept;
    private int firstPosition;
    //    private Spinner firstSpinner;
//    private Spinner secondSpinner;
//    private Spinner yearSpinner;
    private ArrayAdapter<String> firstAdapter;
    private ArrayAdapter<String> secondAdapter;
    public String firstDeptId;
    public String secondDeptId;


    public ListPagePre presenter;
    public String type;
    public RecyclerView recyclerView;

    public String urlList; //从上一个acitivity传过来的url，
    public String url; //在当前页面重新拼接的新url。
    private AlertDialog deptDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addContentView(R.layout.activity_list_page);

        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        urlList = intent.getStringExtra("urlList");

        initView();

        setNavigationIcon(true);
        setAppBarLayoutScroll(false);

        initData();

        initEvent();
        String url = MyConstants.PRE_URL + "mt/common/index.action";
        presenter.getData(this, url, "DeptG", 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_business_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.filter) {
            if (deptG != null)
                inflateSpnnier();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void inflateSpnnier() {
        if (deptDialog != null) {
            deptDialog.show();
            return;
        }
        View contentView = LayoutInflater.from(this).inflate(R.layout.dept_spinner_selector, null);
        LinearLayout parent = (LinearLayout) contentView.findViewById(R.id.parent);
        parent.getChildAt(6).setVisibility(View.VISIBLE);
        parent.getChildAt(7).setVisibility(View.VISIBLE);
        NiceSpinner firstS = (NiceSpinner) contentView.findViewById(R.id.firstDept);
        final NiceSpinner secondS = (NiceSpinner) contentView.findViewById(R.id.secondDept);
        NiceSpinner yearSpinner = (NiceSpinner) contentView.findViewById(R.id.year);
        yearSpinner.setVisibility(View.VISIBLE);
        firstS.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                secondS.attachDataSource(secondDept.get(firstDept.get(position)));
                firstPosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        secondS.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                firstDeptId = deptG.listFirstDept.get(firstPosition).id;

                String selectedDeptName = secondDept.get(firstDept.get(firstPosition)).get(position);
                if ("请选择".equals(selectedDeptName)) {
                    secondDeptId = "";
                } else {
                    for (Dept d : deptG.listSecondDept) {
                        if (d.name.equals(selectedDeptName)) {
                            secondDeptId = d.id;
                            break;
                        }
                    }
                }


                //重新开始计算页码
                pageNo = 1;
                maxPage = 1;
                pageNo_tv.setText(pageNo + "");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                yearString = years[position].replace("年", "");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        firstS.attachDataSource(firstDept);
        yearSpinner.attachDataSource(Arrays.asList(years));
        yearSpinner.setTextIsSelectable(true);
        yearSpinner.setText("2017年");


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(contentView);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (yearString.equals("请选择"))
                    yearString = "";
                switch (type) {
                    case "MediumPlanprogress":
                        url = urlList + "?firstDeptId=" + firstDeptId
                                + "&secondDeptId=" + secondDeptId
                                + "&fillYear=" + yearString
                                + "&pager.pageSize=" + pageSize;
                        break;
                    case "ProjectManagement":
                        url = urlList + "?firstDeptId=" + firstDeptId
                                + "&secondDeptId=" + secondDeptId
                                + "&fillDate=" + yearString + "&pager.pageSize=" + pageSize
                        ;
                }
                presenter.getData(ListPageActivity.this, url, type, pageNo);
            }
        });

        builder.setNegativeButton("取消", null);
        deptDialog = builder.create();

        deptDialog.show();
    }

    private void initEvent() {

//        search_bt.setOnClickListener(this);
//        yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                yearString = years[position].replace("年", "");
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

    }

    private void initData() {
        firstDept = new ArrayList<String>();
        secondDept = new HashMap<String, List<String>>();
        years = new String[]{"请选择", "2020年", "2019年", "2018年", "2017年", "2016年", "2015年", "2014年","2013年","2012年","2011年"
        };
//        yearSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, years));
    }

    private void initView() {
//        firstSpinner = (Spinner) findViewById(R.id.workSection_firstDept);
//        secondSpinner = (Spinner) findViewById(R.id.workSection_secondDept);

        presenter = new ListPagePre(this, type);
//        search_bt = (Button) findViewById(R.id.search);
//        yearSpinner = (Spinner) findViewById(R.id.yearSpinner);

        recyclerView = (RecyclerView) findViewById(R.id.recycleView);


        pageNo_tv = (TextView) findViewById(R.id.current_pageno);
        up_action.setOnClickListener(this);
        down_action.setOnClickListener(this);
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
            case "DeptG":
                deptG = (DeptG) object;
                setSpinnerData();
                break;
            case "MediumPlanprogress":
                mediumPlanprogressList = (List<MediumPlanprogress>) object;

                if (mediumPlanprogressList.size() == pageSize)
                    maxPage++;
                List<String[]> titleArrayList = new ArrayList<>();
                DecimalFormat df = new DecimalFormat("#.###");
                if (mediumPlanprogressList != null && mediumPlanprogressList.size() > 0) {
                    for (MediumPlanprogress m : mediumPlanprogressList) {
                        String[] titleArray = new String[6];
                        titleArray[0] = (m.deptName == null ? "" : m.deptName) + "\n填报年份:" + m.fillYear;
//                        titleArray[1] = "总实施里程（公里）:  " + df.format(m.maintainLengths);
//                        titleArray[2] = "<br/>总预算资金（万元）:  " + df.format(m.budgetFunds);
//                        titleArray[3] = "<br/>总批复预算金额（万元）:  " + df.format(m.replyFunds);
//                        titleArray[4] = "<br/>总支付资金（万元）:  " + df.format(m.paidFunds);
//                        titleArray[5] = "<br/>未支付金额（万元）:  " + df.format(m.notPaidFunds);
//                        titleArray[6] = "<br/>总路面工程数量（m2）:  " + df.format(m.projectNums);
                        titleArray[1] = "总计:  " + m.sumcount;
                        titleArray[2] = "<br/>未开工:  " + m.nocount;
                        titleArray[3] = "<br/>正在施工:  " + m.incount;
                        titleArray[4] = "<br/>已完工:  " + m.finishcount;
                        titleArray[5] = "<br/>未检测:  " + m.notDetectedCount;
                        titleArrayList.add(titleArray);
                    }

                } else {
                    Toast.makeText(ListPageActivity.this, "没有数据", Toast.LENGTH_SHORT).show();
                    if (pageNo != 1) {
                        pageNo--;
                    }
                    pageNo_tv.setText(pageNo + "");
                    maxPage--;
                }
                RecycleViewAdapter2 adapter = new RecycleViewAdapter2(this, titleArrayList, R.layout.item_recycle3);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
//                recyclerView.addItemDecoration(new RecycleViewDivider(this,LinearLayout.VERTICAL));
                adapter.setOnItemClickListener(this);
                break;

            case "ProjectManagement":
                projectManagementList = (List<ProjectManagement>) object;
                if (projectManagementList.size() == pageSize)
                    maxPage++;

                List<String[]> titleArrayList2 = new ArrayList<>();
                if (projectManagementList != null && projectManagementList.size() > 0) {
                    for (ProjectManagement p : projectManagementList) {
                        String[] titleArray = new String[5];
                        titleArray[0] = p.projectName;
                        titleArray[1] = "总投资（元）：<font size=\"5\" color=\"red\">" + CommonUtils.getFormat(",###.00",
                                new BigDecimal(p.totalInvestment)) + "</font>";
                        titleArray[2] = "<br/>建设规模（公里）：" + p.subtotal;
                        titleArray[3] = "<br/>填报年份:" + (p.fillDate == null ? "" : p.fillDate);
                        titleArray[4] = "<br/>是否使用省部补助资金：" + (p.isProvincialCapital == null ? "" : p.isProvincialCapital);
                        titleArrayList2.add(titleArray);
                    }
                } else {
                    Toast.makeText(ListPageActivity.this, "没有数据", Toast.LENGTH_SHORT).show();
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
        }
    }

    @Override
    public void loadDataFailure() {
        Toast.makeText(ListPageActivity.this, "连接服务器失败", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void hasError() {
        UserUtils.reLogin(this, loadDialog);
    }


    //设置两个spinner的内容
    protected void setSpinnerData() {
        List<Dept> firstDeptList = deptG.listFirstDept;
        List<Dept> secondDeptList = deptG.listSecondDept;
        for (Dept d : firstDeptList) {
            firstDept.add(d.name);
            List<String> temp = new ArrayList<String>();

            for (Dept d2 : secondDeptList) {
                if (d2.parentId.equals(d.id)) {
                    temp.add(d2.name);
                }
            }
            if (temp.size()>1)
                temp.add("请选择");
            secondDept.put(d.name, temp);
        }
//        inflateSpnnier();
//        firstAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, firstDept);
        //firstAdapter.setDropDownViewResource(android.R.layout.select_dialog_item);
//        firstSpinner.setAdapter(firstAdapter);

//        firstSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//
//            @Override
//            public void onItemSelected(AdapterView<?> arg0, View arg1,
//                                       int position, long arg3) {
//                secondAdapter = new ArrayAdapter<String>(ListPageActivity.this, android.R.layout.simple_spinner_item, secondDept.get(firstDept.get(position)));
//                secondSpinner.setAdapter(secondAdapter);
//                firstPosition = position;
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> arg0) {
//
//            }
//        });

//        secondSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//
//            @Override
//            public void onItemSelected(AdapterView<?> arg0, View arg1,
//                                       int position, long arg3) {
//
//                firstDeptId = deptG.listFirstDept.get(firstPosition).id;
//
//                String selectedDeptName = secondDept.get(firstDept.get(firstPosition)).get(position);
//                for (Dept d : deptG.listSecondDept) {
//                    if (d.name.equals(selectedDeptName)) {
//                        secondDeptId = d.id;
//                        break;
//                    }
//                }
//                //重新置为第一页
//                pageNo = 1;
//                maxPage = 1;
//                pageNo_tv.setText(pageNo + "");
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> arg0) {
//
//            }
//        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.year:
//                //弹出一个时间选择器
//                showDatePickerDialog(v);
//                break;

//            case R.id.search:
//                if (yearString.equals("请选择"))
//                    yearString = "";
//                switch (type) {
//                    case "MediumPlanprogress":
//                        url = urlList + "?firstDeptId=" + firstDeptId
//                                + "&secondDeptId=" + secondDeptId
//                                + "&fillYear=" + yearString
//                                + "&mobile=phone" + "&pager.pageSize=" + pageSize;
//                        break;
//                    case "ProjectManagement":
//                        url = urlList + "?firstDeptId=" + firstDeptId
//                                + "&secondDeptId=" + secondDeptId
//                                + "&fillDate=" + yearString + "&pager.pageSize=" + pageSize
//                        ;
//                }
//                //访问网络获取数据
//                presenter.getData(this, url, type, pageNo);
//                break;

            case R.id.perPage:
                if (pageNo != 1 && !TextUtils.isEmpty(url)) {
                    pageNo--;
                    pageNo_tv.setText(pageNo + "");
                    presenter.getData(ListPageActivity.this, url, type, pageNo);
                } else {
                    Toast.makeText(this, "当前是最新页", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.nextPage:
                if (pageNo < maxPage && !TextUtils.isEmpty(url)) {
                    pageNo++;
                    pageNo_tv.setText(pageNo + "");
                    presenter.getData(ListPageActivity.this, url, type, pageNo);
                } else {
                    Toast.makeText(ListPageActivity.this, "当前已经是最后一页", Toast.LENGTH_SHORT).show();
                }

                break;

        }
    }

    /**
     * 时间选择器
     */
    private void showDatePickerDialog(final View targetView) {
        DatePickerDialog pickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                ((TextView) targetView).setText(year + "");
                //重新置于第一页
                pageNo = 1;
                maxPage = 1;
            }


        }, 2016, 6, 2) {
            @Override
            public void onDateChanged(DatePicker view, int year, int month, int day) {
                //super.onDateChanged(view, year, month, day);
                this.setTitle("选择填表年份");
            }
        };
//		pickerDialog.setTitle("选择填表年份");
        //把月份和日去掉，要根据系统的语言来去，因为英文系统的是：月份-日-年，中文系统：年-月-日
        // 获取当前系统的语言
        Locale locale = getResources().getConfiguration().locale;
        String language = locale.getLanguage();

        DatePicker dp = (DatePicker) pickerDialog.getDatePicker();
        if (language.endsWith("zh")) {
            //月份
            ((ViewGroup) ((ViewGroup) dp.getChildAt(0)).getChildAt(0))
                    .getChildAt(1).setVisibility(View.GONE);
            //日
            ((ViewGroup) ((ViewGroup) dp.getChildAt(0)).getChildAt(0))
                    .getChildAt(2).setVisibility(View.GONE);
        } else {
            ((ViewGroup) ((ViewGroup) dp.getChildAt(0)).getChildAt(0))
                    .getChildAt(0).setVisibility(View.GONE);
            ((ViewGroup) ((ViewGroup) dp.getChildAt(0)).getChildAt(0))
                    .getChildAt(1).setVisibility(View.GONE);
        }
        pickerDialog.show();
    }

    @Override
    public void onItemClick(View view, int position) {
        try {
            switch (type) {
                case "MediumPlanprogress":
                    Intent intent = new Intent(this, Class.forName("com.chengtech.chengtechmt.activity.business.MediumReportActivity"));
                    intent.putExtra("data", (ArrayList) (mediumPlanprogressList.get(position).mediumPlanprogressItem));
                    startActivity(intent);
                    break;
                case "ProjectManagement":
                    Intent intent2 = new Intent(this, ProjectManagementActivity.class);
                    intent2.putExtra("data", projectManagementList.get(position));
                    startActivity(intent2);
                    break;
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
