package com.chengtech.chengtechmt.activity.business;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chengtech.chengtechmt.BaseActivity;
import com.chengtech.chengtechmt.R;
import com.chengtech.chengtechmt.adapter.business.MaintenanceTaskItemAdapter;
import com.chengtech.chengtechmt.entity.MaintainTaskItem;

import java.io.Serializable;
import java.util.List;

/**
 * 展示保养作业，小修作业中的详细信息界面。
 */
public class MaintainTaskItemActivity extends BaseActivity {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addContentView(R.layout.activity_maintain_task);

        setNavigationIcon(true);
        hidePagerNavigation(true);

        initView();
        Intent intent = getIntent();
        toolbar.setTitle(intent.getStringExtra("title"));
        List<MaintainTaskItem> data = (List<MaintainTaskItem>) intent.getSerializableExtra("data");
        if (data != null) {
            MaintenanceTaskItemAdapter adapter = null;
            if (toolbar.getTitle().toString().contains("保养")) {
                adapter = new MaintenanceTaskItemAdapter(data);
            } else {
                adapter = new MaintenanceTaskItemAdapter(data, true);
            }
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }

    }

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
    }

    public static void startAction(Context context, List<MaintainTaskItem> data, String title) {
        Intent intent = new Intent(context, MaintainTaskItemActivity.class);
        intent.putExtra("data", (Serializable) data);
        intent.putExtra("title", title);
        context.startActivity(intent);
    }
}
