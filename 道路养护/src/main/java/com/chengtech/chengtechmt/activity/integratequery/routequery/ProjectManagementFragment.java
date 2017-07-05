package com.chengtech.chengtechmt.activity.integratequery.routequery;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chengtech.chengtechmt.R;
import com.chengtech.chengtechmt.activity.business.MediumReportActivity;
import com.chengtech.chengtechmt.activity.business.ProjectManagementActivity;
import com.chengtech.chengtechmt.adapter.RecycleViewAdapter2;
import com.chengtech.chengtechmt.entity.MediumPlanprogress;
import com.chengtech.chengtechmt.entity.ProjectManagement;
import com.chengtech.chengtechmt.fragment.BaseFragment;
import com.chengtech.chengtechmt.impl.OnItemClickListener;
import com.chengtech.chengtechmt.util.HttpclientUtil;
import com.chengtech.chengtechmt.util.MyConstants;
import com.chengtech.chengtechmt.util.MyDialogUtil;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者: LiuFuYingWang on 2016/7/26 16:16.
 */
public class ProjectManagementFragment extends BaseFragment implements OnItemClickListener {

    public RecyclerView recyclerView;
    public String routeId;
    public String url = MyConstants.PRE_URL + "mt/integratequery/basicdataquery/routedataquery/listProjectManagementJson.action?&sectionId=&routeId=";
    public RecycleViewAdapter2 adapter;
    public List<ProjectManagement> projectManagementList = new ArrayList<>();

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            String json = (String) msg.obj;
            Gson gson = new Gson();
            try {
                ProjectManagement.ProjectManagementG projectManagementG = gson.fromJson(json, ProjectManagement.ProjectManagementG.class);
                projectManagementList = projectManagementG.rows;
                if (projectManagementList.size() > 0) {
                    createAdapter(projectManagementList);
                } else {
                    Toast.makeText(getContext(), "没有数据。", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Toast.makeText(getContext(), "数据结构出错。", Toast.LENGTH_SHORT).show();
            }

        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_project_management, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        routeId = bundle.getString("routeId");

        recyclerView = (RecyclerView) view.findViewById(R.id.recycleView);
        recyclerView.setVisibility(View.GONE);


    }

    @Override
    protected void onLazyLoad() {
        if (projectManagementList.size() == 0) {
            HttpclientUtil.getData(getContext(), url + routeId, handler, 1);
        } else {
            createAdapter(projectManagementList);
        }
    }

    private void createAdapter(List<ProjectManagement> projectManagementList) {
        List<String[]> data = new ArrayList<>();
        if (projectManagementList != null && projectManagementList.size() > 0) {
            for (ProjectManagement p : projectManagementList) {
                String[] titleArray = new String[3];
                titleArray[0] = p.projectName;
                titleArray[1] = "填报年份:" + p.fillDate;
                titleArray[2] = "<br/>是否使用省部补助资金：" + p.isProvincialCapital;
                data.add(titleArray);
            }
            recyclerView.setVisibility(View.VISIBLE);
        }
        adapter = new RecycleViewAdapter2(getActivity(), data, R.layout.item_recycle3);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(getActivity(), ProjectManagementActivity.class);
        intent.putExtra("data", projectManagementList.get(position));
        startActivity(intent);
    }
}
