package com.chengtech.chengtechmt.activity.business;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chengtech.chengtechmt.BaseActivity;
import com.chengtech.chengtechmt.R;
import com.chengtech.chengtechmt.adapter.DetailAdapter2;
import com.chengtech.chengtechmt.divider.RecycleViewDivider;
import com.chengtech.chengtechmt.entity.BaseModel;
import com.chengtech.chengtechmt.entity.patrol.BriOftenCheck;
import com.chengtech.chengtechmt.entity.patrol.BridgeRecord;
import com.chengtech.chengtechmt.entity.patrol.CulvertOftenCheck;
import com.chengtech.chengtechmt.entity.patrol.CulvertOftenRecord;
import com.chengtech.chengtechmt.entity.patrol.TunnelOftenCheck;
import com.chengtech.chengtechmt.entity.patrol.TunnelRecord;
import com.chengtech.chengtechmt.view.MyHorizontalScrollView3;

import java.util.ArrayList;
import java.util.List;

public class OftenCheckDetailActivity extends BaseActivity {
    private RecyclerView recyclerView1;
    private RecyclerView recyclerView2;
    private MyHorizontalScrollView3 horizontalScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addContentView(R.layout.activity_often_check_detail);
        setNavigationIcon(true);
        hidePagerNavigation(true);

        initView();
        Intent intent = getIntent();
        BaseModel model = (BaseModel) intent.getSerializableExtra("data");
        ArrayList<String> subTitle = (ArrayList<String>) model.getTitles();
        List<String> content = model.getContent();

        DetailAdapter2 adapter = new DetailAdapter2(this, R.layout.item_recycle4, subTitle, content);
        recyclerView1.setAdapter(adapter);
        recyclerView1.addItemDecoration(new RecycleViewDivider(this, LinearLayoutManager.VERTICAL));
        recyclerView1.setLayoutManager(new LinearLayoutManager(this));

        List<List<String>> data = new ArrayList<>();
        if (toolbar.getTitle().toString().contains("桥梁")) {
            BriOftenCheck briOftenCheck = (BriOftenCheck) model;
            List<BridgeRecord> checkRecords = briOftenCheck.listBridgeCheckRecord;
            if (checkRecords != null && checkRecords.size() > 0) {
                data.add(checkRecords.get(0).getTitles());
                for (int i = 0; i < checkRecords.size(); i++) {
                    data.add(checkRecords.get(i).getContent());
                }
            }
            horizontalScrollView.setPercentage(new double[]{0.1, 0.2, 0.2, 0.2, 0.3});
        }else if (toolbar.getTitle().toString().contains("涵洞")) {
            CulvertOftenCheck culvertOftenCheck = (CulvertOftenCheck) model;
            List<CulvertOftenRecord> checkRecords = culvertOftenCheck.listCulvertoftencheck;
            if (checkRecords != null && checkRecords.size() > 0) {
                data.add(checkRecords.get(0).getTitles());
                for (int i = 0; i < checkRecords.size(); i++) {
                    data.add(checkRecords.get(i).getContent());
                }
            }
            horizontalScrollView.setPercentage(new double[]{0.1, 0.2, 0.1, 0.3, 0.3});
        }else if (toolbar.getTitle().toString().contains("隧道")) {
            TunnelOftenCheck tunnelOftenCheck = (TunnelOftenCheck) model;
            List<TunnelRecord> checkRecords = tunnelOftenCheck.listTunnelRecords;
            if (checkRecords != null && checkRecords.size() > 0) {
                data.add(checkRecords.get(0).getTitles());
                for (int i = 0; i < checkRecords.size(); i++) {
                    data.add(checkRecords.get(i).getContent());
                }
            }
            horizontalScrollView.setPercentage(new double[]{0.2, 0.2, 0.2, 0.2, 0.2});
        }
        horizontalScrollView.setData(data);


    }

    private void initView() {
        recyclerView1 = (RecyclerView) findViewById(R.id.recyclerView1);
//        recyclerView2 = (RecyclerView) findViewById(R.id.recyclerView2);
        horizontalScrollView = (MyHorizontalScrollView3) findViewById(R.id.horizontalScrollView);
    }
}
