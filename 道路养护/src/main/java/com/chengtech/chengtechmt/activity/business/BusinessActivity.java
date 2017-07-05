package com.chengtech.chengtechmt.activity.business;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.chengtech.chengtechmt.BaseActivity;
import com.chengtech.chengtechmt.R;
import com.chengtech.chengtechmt.adapter.RecycleViewAdapter;
import com.chengtech.chengtechmt.divider.RecycleViewDivider;
import com.chengtech.chengtechmt.util.MyConstants;

/**
 * 养护业务管理页面
 */

public class BusinessActivity extends BaseActivity {

    public RecyclerView recyclerView;

    String [] title;
    int [] imageIds;
    String [] urlList;
    String [] typeArray;
    String [] acitivtyName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addContentView(R.layout.activity_business);

        initView();

        setNavigationIcon(true);
        hidePagerNavigation(true);
        initData();
        
        initEvent();

    }

    private void initEvent() {
    }

    private void initData() {
        imageIds = new int[] {R.mipmap.business_a_01,R.mipmap.business_a_02,R.mipmap.month_checked,R.mipmap.maintaintask,
        R.mipmap.minorrepair,R.mipmap.minorrepair};
        title = new String[]{"月度检查","保养作业","小修作业","小额专项维修","大中修、改造（善）及省部补助项目","桥隧涵养护"};
        urlList = new String[]{
                MyConstants.PRE_URL+"mt/business/maintainworkcheck/monthlyinspect/maintainregister/listMaintainRegisterJson.action",
                MyConstants.PRE_URL+"mt/business/tinkermaintainpatrol/maintaintask/listMaintainTaskJson.action",
                MyConstants.PRE_URL+"mt/business/tinkermaintainpatrol/minorrepair/listMinorRepairJson.action",
                MyConstants.PRE_URL+"mt/business/maintenance/projectaccount/mediumplanprogress/listMediumPlanprogressJson.action",
                MyConstants.PRE_URL+"mt/business/maintenance/projectaccount/projectmanagement/listProjectManagementJson.action",
                "f"
        };
        typeArray = new String [] {
                "MaintainRegister",
                "MaintainTask",
                "MinorRepair",
                "MediumPlanprogress",
                "ProjectManagement",
                "Patrol"
        };

        acitivtyName = new String [] {
                "com.chengtech.chengtechmt.activity.business.MaintenanceListActivity",
                "com.chengtech.chengtechmt.activity.business.MaintenanceListActivity",
                "com.chengtech.chengtechmt.activity.business.MaintenanceListActivity",
                "com.chengtech.chengtechmt.activity.business.ListPageActivity",
                "com.chengtech.chengtechmt.activity.business.ListPageActivity",
                "com.chengtech.chengtechmt.activity.business.BricheckManageActivity",
        };
        RecycleViewAdapter adapter = new RecycleViewAdapter(this,title,imageIds,R.layout.item_recycle2);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new RecycleViewDivider(this, LinearLayout.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter.setOnItemClickListener(new RecycleViewAdapter.OnItemClickListener() {
            @Override
            public void setOnItemClickListener(View view, int position) {
                Intent intent = null;
                try {

                    intent = new Intent(BusinessActivity.this, Class.forName(acitivtyName[position]));
                    intent.putExtra("type",typeArray[position]);
                    intent.putExtra("title",title[position]);
                    intent.putExtra("urlList",urlList[position]);
                    startActivity(intent);
                } catch (ClassNotFoundException e) {
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
