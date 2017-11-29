package com.chengtech.chengtechmt.entity.business;

import com.chengtech.chengtechmt.entity.BaseModel;

import java.util.List;

/**
 * 作者: LiuFuYingWang on 2017/11/15 9:52.
 * 小修作业验收表
 */

public class TaskAcceptance extends BaseModel{

    public String routeNamePeg;            //线路、桩号范围

    public String cost;                    //造价

    public String mainContent;             //主要内容

    public String beginDate;                 //开工日期

    public String finishDate;                //完工日期

    public String numberApproved;          //数量核定

    public String acceptanceDeptName;      //验收单位

    public String acceptanceNo;            //验收编号

    public String maintainTaskItemId;      //作业任务id
    @Override
    public List<String> getContent() {
        return null;
    }

    @Override
    public List<String> getTitles() {
        return null;
    }

    @Override
    public List<String> getPropertyName() {
        return null;
    }
}
