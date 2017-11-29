package com.chengtech.chengtechmt.entity;

import android.content.Context;

import com.chengtech.chengtechmt.presenter.Presenter;
import com.chengtech.chengtechmt.util.DateUtils;
import com.chengtech.chengtechmt.util.FormatUtil;
import com.chengtech.chengtechmt.util.HttpclientUtil;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者: LiuFuYingWang on 2016/10/19 10:35.
 */

public class MaintainTaskItem extends BaseModel implements Serializable{

    public Presenter presenter;

    public String routeName;                     //实施路线名称

    public String routePeg;                      //实施范围（路线桩号段）

    public String workContent;                   //主要作业内容

    public Date planStartDate;                      //计划作业开始日期
    public Date planEndDate;                      //计划作业结束日期
    public String workDay;                       //计划作业工期
    public Date startDate;                          //实际开始日期
    public Date finishDate;                      //实际完工日期

    public String inOutPlanType;                 //计划内or计划外；0计划内，1计划外

    public String registOrAcceptanceStatus;      //状态 0/""：表示未实施，1：未验收，2：已完成

    //工程项目类型  只用来显示
    public String projectType;                   //工程项目类型

    public String workTeam;                      //作业班组

    //2016.11.15
    public String floatRoutePeg;                  //实施范围(带小数点),比如k0013+020,浮点数为13.020


    //20161202
    public String mobileRouteNames;                    // 用于手机端获取实施路线值
    public String taskType;                            //类型
    public Double plannedTaskQuantity;                //计划工程量
    public Double plannedTaskDay;                    //计划工日
    public String plannedWorkDay;                      //计划工期
    public String plannedUnit;                        //计划单位
    public Double actualTaskQuantity;                    //实际工程量
    public Double actualTaskDay;                        //实际工日
    public String actualUnit;                            //实际单位
    public String actualWorkDay;                        //实际工期

    public MaintainTaskItem(Presenter presenter) {
        this.presenter = presenter;

    }

    public List<String> getContent() {
        List<String> list = new ArrayList<>();
        Map<String, String> typeMap = new HashMap<>();
        typeMap.put("4046e26f5692acd4015692c1e779004f", "路面");
        typeMap.put("4046e26f5692acd4015692c43829006c", "绿化");
        typeMap.put("4081a8e558c270740158c3d9241109e4", "桥梁");
        typeMap.put("4081a8e558c270740158c3baa72008e8", "路缘带及人行道");
        typeMap.put("4081a8e558c270740158c3cd1c73092f", "标志和标线等");
        typeMap.put("4081a8e558c270740158c3e088320ac5", "隧道");
        typeMap.put("4081a8e558c270740158c36145ee067e", "排水设施	");
        typeMap.put("4081a8e558c270740158c372b209080d", "路肩及边坡");
        typeMap.put("4081a8e558c270740158c37869e50893", "挡土墙及防撞设施");
        typeMap.put("4081a8e558c270740158c3de59050a85", "涵洞");
        typeMap.put("4028835f58d333760158d3487f08010e", "其他");

        list.add(registOrAcceptanceStatus == null ? "" : (registOrAcceptanceStatus.equals("0") ? "已核定" : "未核定"));
        list.add(workTeam == null ? "" : workTeam);
        list.add(mobileRouteNames == null ? "" : mobileRouteNames);
        list.add(routePeg == null ? "" : routePeg);
        list.add(typeMap.get(taskType) == null ? "" : typeMap.get(taskType));
        list.add(workContent == null ? "" : workContent);
        list.add(plannedTaskQuantity == null ? "" : FormatUtil.double2String(plannedTaskQuantity));
        list.add(plannedUnit == null ? "" : plannedUnit);
        list.add(plannedTaskDay == null ? "" : FormatUtil.double2String(plannedTaskDay));
        list.add(actualTaskQuantity == null ? "" : FormatUtil.double2String(actualTaskQuantity));
        list.add(actualUnit == null ? "" : actualUnit);
        list.add(actualTaskDay == null ? "" : FormatUtil.double2String(actualTaskDay));
        return list;
    }

    public List<String> getTitles() {
        List<String> title = new ArrayList<>();
        title.add("状态");
        title.add("作业班组");
        title.add("实施路线");
        title.add("实施范围");
        title.add("类型");
        title.add("主要作业内容");
        title.add("计划工程量");
        title.add("计划单位");
        title.add("计划工日");
        title.add("实际工程量");
        title.add("实际单位");
        title.add("实际工日");
        return title;
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
                    MaintainTaskItem.MaintainTaskItemG m = gson.fromJson(data, MaintainTaskItem.MaintainTaskItemG.class);
                    presenter.loadDataSuccess(m.rows, type);
                } catch (Exception e) {
                    presenter.loadDataFailed();
                    e.printStackTrace();
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

    public class MaintainTaskItemG {
        List<MaintainTaskItem> rows;
    }
}
