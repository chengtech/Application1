package com.chengtech.chengtechmt.activity.expertdecision;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.chengtech.chengtechmt.BaseActivity;
import com.chengtech.chengtechmt.R;
import com.chengtech.chengtechmt.activity.standard.StandardActivity;
import com.chengtech.chengtechmt.activity.standard.StandardListActivity;
import com.chengtech.chengtechmt.activity.standard.StandardTechActivity;
import com.chengtech.chengtechmt.adapter.MyExpandableAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExpertDecisionActivity extends BaseActivity {
    private ExpandableListView expandableListView;

    private String[] groupName = {"公路技术状况评定"};
    private ArrayList<String> firstChild;
    private ArrayList<String> firstChildTreeUrl;
    private String[] firstChildType;
    private Map<String, List<String>> groupData;
    private MyExpandableAdapter expandableAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addContentView(R.layout.activity_expert_decision);
        setNavigationIcon(true);
        hidePagerNavigation(true);

        initView();
        initData();

        expandableAdapter = new MyExpandableAdapter(this, groupName, groupData);
        expandableListView.setAdapter(expandableAdapter);
        expandableListView.expandGroup(0);
        expandableListView.setGroupIndicator(null);
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        });

        initEvent();

    }

    private void initEvent() {
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                try {
                    if (groupPosition == 0) {
                        if (childPosition <= 1) {
                            Intent intent = new Intent(ExpertDecisionActivity.this, ExpertDecisionListActivity.class);
                            intent.putExtra("title", firstChild.get(childPosition));
                            intent.putExtra("type", firstChildType[childPosition]);
                            startActivity(intent);
                        } else if (childPosition == 2) {
                            Intent intent = new Intent(ExpertDecisionActivity.this, EvaluationdetailListActivity.class);
                            intent.putExtra("title", firstChild.get(childPosition));
                            intent.putExtra("type", firstChildType[childPosition]);
                            startActivity(intent);
                        } else {
//                            Intent intent = new Intent(ExpertDecisionActivity.this, EvaluationSummaryActivity.class);
//                            intent.putExtra("title", firstChild.get(childPosition));
//                            intent.putExtra("type", firstChildType[childPosition]);
//                            startActivity(intent);
                            Toast.makeText(ExpertDecisionActivity.this, "该模块未开发", Toast.LENGTH_SHORT).show();
                        }
//                    }
//                    if (groupPosition==1) {
//                        Intent intent = new Intent(StandardActivity.this, StandardListActivity.class);
//                        intent.putExtra("url", childTreeUrl1.get(childPosition));
//                        intent.putExtra("urlList",childListUrl1.get(childPosition));
//                        intent.putExtra("title", childList1.get(childPosition));
//                        startActivity(intent);
                    }
                    return true;
                } catch (Exception e) {
                }
                return false;
            }
        });
    }

    private void initData() {
        firstChild.add("沥青路面状况");
        firstChild.add("水泥路面状况");
        firstChild.add("评定明细");
        firstChild.add("评定汇总");
        firstChildType = new String[]{"BitumenRoadDamage", "CementRoadDamage", "Evaluationdetail", "EvaluationSummary"};
        groupData.put(groupName[0], firstChild);
    }

    private void initView() {
        expandableListView = (ExpandableListView) findViewById(R.id.expandlist);
        groupData = new HashMap<String, List<String>>();
        firstChild = new ArrayList<String>();
        firstChildTreeUrl = new ArrayList<String>();
    }
}
