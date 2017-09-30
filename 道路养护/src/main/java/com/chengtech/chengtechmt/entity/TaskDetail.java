package com.chengtech.chengtechmt.entity;

import android.content.Context;

import com.chengtech.chengtechmt.presenter.Presenter;
import com.chengtech.chengtechmt.util.HttpclientUtil;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者: LiuFuYingWang on 2016/10/26 9:06.
 */

public class TaskDetail extends BaseModel {
    private List<String> values;
    private Presenter presenter;

    public String taskType;                      //类型

    public String taskFine;                      //细项

    public String engineerQuantity;              //计划工程量
    public String actualengineerQuantity;        //核定工程量

    public String taskUnit;                      //单位

    public String maintainTaskItemId;            //作业任务id

    public String workContent;                   //主要作业内容


    public TaskDetail(Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public List<String> getContent() {
        //"value":"4046e26f5692acd4015692c00f0f0048","key":"路基"},
        // {"value":"4046e26f5692acd4015692c1e779004f","key":"路面"},
        // {"value":"4046e26f5692acd4015692c2b6f0005c","key":"桥梁、涵洞、隧道"},
        // {"value":"4046e26f5692acd4015692c2cece005f","key":"交通工程及沿线设施"},
        // {"value":"4046e26f5692acd4015692c43829006c","key":"绿化"},
        // {"value":"4046e26f569616c90156961c79e20014","key":"其他"
        Map<String, String> typeMap = new HashMap<>();
        typeMap.put("4046e26f5692acd4015692c00f0f0048", "路基");
        typeMap.put("4046e26f5692acd4015692c1e779004f", "路面");
        typeMap.put("4046e26f5692acd4015692c2b6f0005c", "桥梁、涵洞、隧道");
        typeMap.put("4046e26f5692acd4015692c2cece005f", "交通工程及沿线设施");
        typeMap.put("4046e26f5692acd4015692c43829006c", "绿化");
        typeMap.put("4046e26f569616c90156961c79e20014", "其他");
        if (values == null) {
            values = new ArrayList<>();
        }
        values.add(typeMap.get(taskType) == null ? "" : typeMap.get(taskType));
        values.add(taskFine == null ? "" : taskFine);
        values.add(workContent == null ? "" : workContent);
        values.add(engineerQuantity == null ? "" : engineerQuantity);
        values.add(taskUnit == null ? "" : taskUnit);
        values.add(actualengineerQuantity == null ? "" : actualengineerQuantity);
        return values;
    }

    public List<String> getTitles() {
        List<String> titles = new ArrayList<>();
        titles.add("类型");
        titles.add("细项");
        titles.add("主要作业内容");
        titles.add("计划工程量");
        titles.add("单位");
        titles.add("核定工程量");
        return titles;
    }

    @Override
    public List<String> getPropertyName() {
        return null;
    }

    public void getData(Context context, String url, final String type, int pageNo) {
        AsyncHttpClient client = HttpclientUtil.getInstance(context);

        AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onSuccess(int i, cz.msebera.android.httpclient.Header[] headers, byte[] bytes) {
                try {
                    String data = new String(bytes, "utf-8");
                    Gson gson = new Gson();
                    TaskDetailG m = gson.fromJson(data, TaskDetailG.class);
                    presenter.loadDataSuccess(m.rows, type);
                } catch (Exception e) {
                    presenter.loadDataFailed();
                }
            }

            @Override
            public void onFailure(int i, cz.msebera.android.httpclient.Header[] headers, byte[] bytes, Throwable throwable) {
                presenter.loadDataFailed();
            }

        };
        client.get(url + "&pager.pageNo=" + pageNo,
                responseHandler);
    }

    private class TaskDetailG {

        List<TaskDetail> rows;
    }
}
