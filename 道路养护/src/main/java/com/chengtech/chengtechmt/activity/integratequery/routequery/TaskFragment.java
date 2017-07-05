package com.chengtech.chengtechmt.activity.integratequery.routequery;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chengtech.chengtechmt.R;
import com.chengtech.chengtechmt.adapter.RecycleViewAdapter2;
import com.chengtech.chengtechmt.entity.routequery.TaskOrganization;
import com.chengtech.chengtechmt.entity.routequery.TaskTable;
import com.chengtech.chengtechmt.fragment.TaskTableDialogFragment;
import com.chengtech.chengtechmt.impl.OnItemClickListener;
import com.chengtech.chengtechmt.util.HttpclientUtil;
import com.chengtech.chengtechmt.util.MyConstants;
import com.chengtech.chengtechmt.util.MyDialogUtil;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者: LiuFuYingWang on 2016/7/26 16:16.
 */
public class TaskFragment extends Fragment {

    private RecyclerView recyclerView;
    private Dialog loadDialog;
    private static final int ONE_ACCESS_REQUEST = 1;
    private static final int TWO_ACCESS_REQUEST = 2;
    private String url1 = MyConstants.PRE_URL + "mt/integratequery/basicdataquery/routedataquery/listTaskJson.action?sectionId=&routeId=";
    private String url2 = MyConstants.PRE_URL + "mt/business/projectmanager/constructiontaskarrange/taskreceive/showTaskTable.action?pageNo=1&mobile=phone&id=";
    private String taskId;
    private String routeId;
    private List<TaskOrganization> tasks;
    public List<String[]> data;
    public RecycleViewAdapter2 adapter;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String json = (String) msg.obj;
            Gson gson = new Gson();
            switch (msg.what) {
                case ONE_ACCESS_REQUEST:
                    try {
                        if (!TextUtils.isEmpty(json)) {
                            TaskOrganization.TaskOrganizationG g = gson.fromJson(json, TaskOrganization.TaskOrganizationG.class);
                            tasks = g.rows;
                            if (tasks != null && tasks.size() > 0) {
                                for (TaskOrganization task : tasks) {
                                    String[] content = new String[3];
                                    content[0] = "工程名称:" + task.maintenance;
                                    content[1] = "作业单编号:" + task.noticeNo;
                                    if (!"0".equals(task.taskSource)) {
                                        switch (task.state) {
                                            case "1":
                                                content[2] = "处理情况:" + Html.fromHtml("<font color='red'>已签收</font>");
                                                break;
                                            default:
                                                content[2] = "处理情况:" + Html.fromHtml("<font color='black'>未签收</font>");
                                                break;
                                        }
                                    } else {
                                        switch (task.state) {
                                            case "2":
                                                content[2] = "处理情况:" + Html.fromHtml("<font color='#99CC00'>已做交底</font>");
                                                break;
                                            case "22":
                                                content[2] = "处理情况:" + Html.fromHtml("<font color='#990000'>已做报备</font>");
                                                break;
                                            case "3":
                                                content[2] = "处理情况:" + Html.fromHtml("<font color='FF6633'>已做作业记录</font>");
                                                break;
                                            case "25":
                                            case "36":
                                                content[2] = "处理情况:" + Html.fromHtml("<font color='#996600'>已做整改</font>");
                                                break;
                                            case "4":
                                                content[2] = "处理情况:" + Html.fromHtml("<font color='#CC0033'>已做验收</font>");
                                                break;
                                            default:
                                                content[2] = "处理情况:" + Html.fromHtml("<font color='#CC0033'>未做交底</font>");
                                                break;
                                        }
                                    }
                                    data.add(content);
                                }
                                recyclerView.setVisibility(View.VISIBLE);
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }catch (Exception e) {
                        Toast.makeText(getContext(), "数据解析出错", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case TWO_ACCESS_REQUEST:
                    try {
                        TaskTable taskTable = gson.fromJson(json,TaskTable.class);
                        TaskTableDialogFragment dialogFragment = new TaskTableDialogFragment();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("taskTable",taskTable);
                        dialogFragment.setArguments(bundle);
                        dialogFragment.show(getFragmentManager(),"taskTable");
                    }catch (Exception e){
                        Toast.makeText(getContext(), "数据解析出错", Toast.LENGTH_SHORT).show();
                    }
                    break;

            }


        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task, container, false);

        loadDialog = MyDialogUtil.createDialog(getActivity(), "正在加载中...");
        loadDialog.show();
        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        routeId = bundle.getString("routeId");

        recyclerView = (RecyclerView) view.findViewById(R.id.recycleView);
        recyclerView.setVisibility(View.GONE);

        data = new ArrayList<>();
        adapter = new RecycleViewAdapter2(getActivity(), data, R.layout.item_recycle3);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                loadDialog.show();
                getData(url2 + tasks.get(position).id, TWO_ACCESS_REQUEST);

            }
        });
        //网络请求
        getData(url1 + routeId, ONE_ACCESS_REQUEST);

    }

    private void getData(String url, final int type) {
        AsyncHttpClient client = HttpclientUtil.getInstance(getActivity());

        AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                loadDialog.dismiss();
                try {
                    String data = new String(arg2, "utf-8");
                    data = data.replace("pager.", "");
                    Message message = Message.obtain();
                    message.obj = data;
                    message.what = type;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                super.onSuccess(arg0, arg1, arg2);
            }

            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2,
                                  Throwable arg3) {
                loadDialog.dismiss();
                super.onFailure(arg0, arg1, arg2, arg3);
            }
        };
        client.get(url,
                responseHandler);

    }
}
