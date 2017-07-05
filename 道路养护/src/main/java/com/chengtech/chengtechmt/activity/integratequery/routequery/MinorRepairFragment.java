package com.chengtech.chengtechmt.activity.integratequery.routequery;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseIntArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chengtech.chengtechmt.R;
import com.chengtech.chengtechmt.adapter.RecycleViewAdapter2;
import com.chengtech.chengtechmt.adapter.business.MaintenanceTaskAdapter;
import com.chengtech.chengtechmt.entity.MaintainTask;
import com.chengtech.chengtechmt.entity.MaintainTaskItem;
import com.chengtech.chengtechmt.fragment.BaseFragment;
import com.chengtech.chengtechmt.impl.OnItemClickListener;
import com.chengtech.chengtechmt.util.HttpclientUtil;
import com.chengtech.chengtechmt.util.MyConstants;
import com.chengtech.chengtechmt.view.MyHorizontalScrollView2;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者: LiuFuYingWang on 2016/12/13 14:40.
 * 历史小修作业数据
 */

public class MinorRepairFragment extends BaseFragment implements OnItemClickListener {
    private String routeId;
    private List<MaintainTask> maintainTaskList = new ArrayList<>();
    private String url;
    private String deptId;
    private List<MaintainTaskItem> maintainTaskItemList = new ArrayList<>();

    public Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            String json = (String) msg.obj;
            Gson gson = new Gson();
            switch (msg.what) {
                case 1:
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        JSONArray rows = jsonObject.getJSONArray("rows");
                        maintainTaskList.clear();
                        for (int i = 0; i < rows.length(); i++) {
                            JSONObject jsonObject1 = rows.getJSONObject(i);
                            MaintainTask maintainTask = gson.fromJson(jsonObject1.toString(), MaintainTask.class);
                            maintainTaskList.add(maintainTask);
                        }
                        if (maintainTaskList.size() > 0) {
                            createAdapetr(maintainTaskList);
                        } else {
                            Toast.makeText(getContext(), "没有数据。", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "数据结构出错。", Toast.LENGTH_SHORT).show();
                    }
                    break;

                case 2:
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        JSONArray rows = jsonObject.getJSONArray("rows");
                        maintainTaskItemList.clear();
                        for (int i = 0; i < rows.length(); i++) {
                            JSONObject jsonObject1 = rows.getJSONObject(i);
                            MaintainTaskItem maintainTaskItem = gson.fromJson(jsonObject1.toString(), MaintainTaskItem.class);
                            maintainTaskItemList.add(maintainTaskItem);
                        }
                        if (maintainTaskItemList.size() > 0) {
//                            createAdapetr(maintainTaskList);
//                            showDialog(maintainTaskItemList);
                            showBottomSheetDialog();
                        } else {
                            Toast.makeText(getContext(), "没有数据。", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "数据结构出错。", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        LinearLayout relativeLayout = new LinearLayout(inflater.getContext());
        relativeLayout.setBackgroundResource(R.color.light_sky_blue2);
        relativeLayout.setPadding(10, 10, 10, 10);
        TextView textView = new TextView(inflater.getContext());
        textView.setText("没有数据");
        textView.setTextSize(16);
        textView.setGravity(Gravity.CENTER);
//        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//        lp.addRule(RelativeLayout.CENTER_IN_PARENT);
//        textView.setLayoutParams(lp);
        RecyclerView recyclerView = new RecyclerView(inflater.getContext());
        recyclerView.setVisibility(View.GONE);
        relativeLayout.addView(recyclerView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        relativeLayout.addView(textView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        return relativeLayout;
    }

    public MinorRepairFragment() {
//        url = MyConstants.PRE_URL+"mt/integratequery/gisvisualization/listMinorRepairInGisJson.action?" +
//                "taskMark=1&subState=1&routeId=";
        url = MyConstants.PRE_URL + "mt/integratequery/basicdataquery/routedataquery/listMinorRepairJson.action?" +
                "taskMark=1&subState=1&routeId=";
    }

    @Override
    protected void onLazyLoad() {
        //网络请求
        if (deptId != null && maintainTaskList.size() == 0) {
            HttpclientUtil.getData(getContext(), url + routeId + "&deptIds=" + deptId, handler, 1);
        } else if (deptId == null && maintainTaskList.size() == 0) {
            url = MyConstants.PRE_URL + "mt/integratequery/gisvisualization/listMinorRepairInGisJson.action?" +
                    "taskMark=1&subState=1&routeId=";
            HttpclientUtil.getData(getContext(), url + routeId, handler, 1);
        } else {
            createAdapetr(maintainTaskList);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        routeId = bundle.getString("routeId");
        deptId = bundle.getString("deptId");
//        //网络请求
//        HttpclientUtil.getData(getContext(),url+routeId,handler,1);
    }

    private void createAdapetr(List<MaintainTask> maintainTaskList) {
        RecyclerView recyclerView = (RecyclerView) ((LinearLayout) getView()).getChildAt(0);
        recyclerView.setVisibility(View.VISIBLE);
        List<String[]> titleArrayList3 = new ArrayList<>();
        List<String[]> planData = new ArrayList<>(); //计划内任务与新增任务
        if (maintainTaskList != null && maintainTaskList.size() > 0) {
            for (MaintainTask m : maintainTaskList) {
                String[] titleArray = new String[4];
                titleArray[0] = m.secondDeptName + "-" + m.thirdDeptName;
                titleArray[1] = "日期:" + m.workYear + "-" + m.workMonth;


                titleArray[2] = "已下达任务：" + m.planInsideTaskCount
                        + "\t未实施：" + m.planInsideNotImpleCount
                        + "\t未验收：" + m.planInsideImpleCount
                        + "\t已完成：" + m.planInsideAcceptCount;
                titleArray[3] = "已新增任务：" + m.planOutsideTaskCount
                        + "\t未实施：" + m.planOutsideNotImpleCount
                        + "\t未验收：" + m.planOutsideImpleCount
                        + "\t已完成：" + m.planOutsideAcceptCount;

                titleArrayList3.add(titleArray);
            }
        } else {
            Toast.makeText(getContext(), "没有数据", Toast.LENGTH_SHORT).show();
        }
        MaintenanceTaskAdapter adapter3 = new MaintenanceTaskAdapter(getContext(), titleArrayList3);
        adapter3.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter3);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onItemClick(View view, int position) {
        int viewId = view.getId();

        String inOutPlanType = null;
        switch (viewId) {
            case R.id.plan_layout:
                inOutPlanType = "0";
                break;
            case R.id.add_layout:
                inOutPlanType = "1";
                break;
        }
        String url = MyConstants.PRE_URL
                + "mt/business/tinkermaintainpatrol/maintaintask/listMaintainTaskItemJson.action?maintainTaskId="
                + maintainTaskList.get(position).id
                + "&inOutPlanType="
                + inOutPlanType + "&routeId=" + routeId;
        HttpclientUtil.getData(getContext(), url, handler, 2);
        return;

    }

    private void showDialog(final List<MaintainTaskItem> maintainTaskItemList) {
        final MyHorizontalScrollView2 parent = new MyHorizontalScrollView2(getContext(), null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(parent);
        final List<List<String>> data = new ArrayList<>();
        data.add(maintainTaskItemList.get(0).getTitles());
        for (int i = 0; i < maintainTaskItemList.size(); i++) {
            data.add(maintainTaskItemList.get(i).getContent());
        }
        SparseIntArray sparseIntArray = new SparseIntArray();
        sparseIntArray.put(1, 100);
        sparseIntArray.put(2, 100);
        sparseIntArray.put(3, 100);
        sparseIntArray.put(4, 100);
        sparseIntArray.put(5, 100);
        sparseIntArray.put(6, 100);
        sparseIntArray.put(7, 100);
        sparseIntArray.put(8, 100);
        sparseIntArray.put(9, 100);
        parent.setRectWidthAndHeight(sparseIntArray);
        parent.setData(data);
        parent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                for (int i = 0; i < parent.contentRV.getChildCount(); i++) {
                    View view = parent.contentRV.getChildAt(i);
                    int measuredHeight = view.getMeasuredHeight();
                    View childAt = parent.nameRV.getChildAt(i);
                    ViewGroup.LayoutParams layoutParams = childAt.getLayoutParams();
                    layoutParams.height = measuredHeight;
                    childAt.setLayoutParams(layoutParams);
                }
                parent.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });
        final AlertDialog dialog = builder.create();
        parent.setOnItemClickListener(new MyHorizontalScrollView2.onItemClickListener() {
            @Override
            public void onClick(View view, int position) {
//                String maintainItemId = maintainTaskItemList.get(position ).id;
//                String url = MyConstants.PRE_URL
//                        + "mt/business/tinkermaintainpatrol/maintaintask/listTaskDetailJson.action?maintainTaskItemId="
//                        + maintainItemId;
//                presenter.getData(MaintenanceListActivity.this, url, "TaskDetail", 0);
//                dialog.dismiss();
            }
        });

        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    private void showBottomSheetDialog() {
        final MyHorizontalScrollView2 parent = new MyHorizontalScrollView2(getContext(), null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(parent);
        final List<List<String>> data = new ArrayList<>();
        data.add(maintainTaskItemList.get(0).getTitles());
        for (int i = 0; i < maintainTaskItemList.size(); i++) {
            data.add(maintainTaskItemList.get(i).getContent());
        }
        SparseIntArray sparseIntArray = new SparseIntArray();
        sparseIntArray.put(1, 150);
        sparseIntArray.put(2, 150);
        sparseIntArray.put(3, 150);
        sparseIntArray.put(4, 150);
        sparseIntArray.put(5, 150);
        sparseIntArray.put(6, 100);
        sparseIntArray.put(7, 100);
        sparseIntArray.put(8, 100);
        sparseIntArray.put(9, 100);
        sparseIntArray.put(10, 100);
        sparseIntArray.put(11, 100);
        parent.setRectWidthAndHeight(sparseIntArray);
        parent.setData(data);
        parent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                for (int i = 0; i < parent.contentRV.getChildCount(); i++) {
                    View view = parent.contentRV.getChildAt(i);
                    int measuredHeight = view.getMeasuredHeight();
                    View childAt = parent.nameRV.getChildAt(i);
                    ViewGroup.LayoutParams layoutParams = childAt.getLayoutParams();
                    layoutParams.height = measuredHeight;
                    childAt.setLayoutParams(layoutParams);
                }
                parent.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });
        AlertDialog horizontalDialog = builder.create();
//        parent.setOnItemClickListener(new MyHorizontalScrollView2.onItemClickListener() {
//            @Override
//            public void onClick(View view, int position) {
//                String maintainItemId = maintainTaskItemList.get(position).id;
//                String url = MyConstants.PRE_URL
//                        + "mt/business/tinkermaintainpatrol/maintaintask/listTaskDetailJson.action?maintainTaskItemId="
//                        + maintainItemId;
//
//                presenter.getData(MaintenanceListActivity.this, url, "TaskDetail", 0);
//                horizontalDialog.dismiss();
//            }
//        });

        horizontalDialog.setCanceledOnTouchOutside(true);
        horizontalDialog.show();
    }
}
