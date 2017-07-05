package com.chengtech.chengtechmt.activity.integratequery.routequery;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chengtech.chengtechmt.R;
import com.chengtech.chengtechmt.activity.business.MediumReportActivity;
import com.chengtech.chengtechmt.adapter.RecycleViewAdapter2;
import com.chengtech.chengtechmt.entity.MediumPlanprogress;
import com.chengtech.chengtechmt.fragment.BaseFragment;
import com.chengtech.chengtechmt.impl.OnItemClickListener;
import com.chengtech.chengtechmt.util.HttpclientUtil;
import com.chengtech.chengtechmt.util.MyConstants;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者: LiuFuYingWang on 2016/7/26 16:16.
 * 历史小额
 */
public class MediumPlanprogressFragment extends BaseFragment implements OnItemClickListener {

    public RecyclerView recyclerView;
    public String routeId;
    public String url = MyConstants.PRE_URL + "mt/integratequery/basicdataquery/routedataquery/listMediumPlanprogressJson.action?mobile=phone&sectionId=&routeId=";
    public RecycleViewAdapter2 adapter;
    public  List<MediumPlanprogress> mediumPlanprogressList = new ArrayList<>();

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            String json = (String) msg.obj;
            Gson gson = new Gson();
            try {
                JSONObject jsonObject = new JSONObject(json);
                JSONArray rows = jsonObject.getJSONArray("data");
                if (rows.length()>0) {
                    for (int i = 0; i < rows.length(); i++) {
                        JSONObject jsonObject1 = rows.getJSONObject(i);
                        MediumPlanprogress mediumPlanprogress = gson.fromJson(jsonObject1.toString(), MediumPlanprogress.class);
                        mediumPlanprogressList.add(mediumPlanprogress);
                    }
                }
                if (mediumPlanprogressList.size()>0) {
                    createAdapter(mediumPlanprogressList);
                }else {
                    Toast.makeText(getContext(), "没有数据。", Toast.LENGTH_SHORT).show();

                }
            }catch (Exception e){
                Toast.makeText(getContext(), "数据结构出错。", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_medium_progress,container,false);
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
        if(mediumPlanprogressList.size()==0) {
            HttpclientUtil.getData(getContext(),url+routeId,handler,1);
        }else {
            createAdapter(mediumPlanprogressList);
        }
    }

    private void createAdapter(final List<MediumPlanprogress> mediumPlanprogressList) {
        DecimalFormat df = new DecimalFormat("#.###");
        List<String[]> data = new ArrayList<>();
        if (mediumPlanprogressList != null && mediumPlanprogressList.size() > 0) {
            for (MediumPlanprogress m : mediumPlanprogressList) {
                String[] titleArray = new String[7];
                titleArray[0] = (m.deptName==null?"":m.deptName)+"\n填报年份:" + m.fillYear;
                titleArray[1] = "总实施里程:" + df.format(m.maintainLengths);
                titleArray[2] = "<br/>总预算资金(万元):" +df.format(m.budgetFunds);
                titleArray[3] = "<br/>总批复预算金额(万元):" +df.format(m.replyFunds);
                titleArray[4] = "<br/>总支付资金(万元):" + df.format(m.paidFunds);
                titleArray[5] = "<br/>未支付金额(万元):" + df.format(m.notPaidFunds);
                titleArray[6] = "<br/>总路面工程数量(m2):" + df.format(m.projectNums);
                data.add(titleArray);
            }
            recyclerView.setVisibility(View.VISIBLE);
        }

        adapter = new RecycleViewAdapter2(getActivity(),data,R.layout.item_recycle3);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(getActivity(), MediumReportActivity.class);
        intent.putExtra("data", (ArrayList) (mediumPlanprogressList.get(position).mediumPlanprogressItem));
        startActivity(intent);
    }
}
