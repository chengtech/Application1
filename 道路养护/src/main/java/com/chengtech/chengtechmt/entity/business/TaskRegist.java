package com.chengtech.chengtechmt.entity.business;

import com.chengtech.chengtechmt.entity.BaseModel;

import java.util.Date;
import java.util.List;

/**
 * 作者: LiuFuYingWang on 2017/11/14 15:25.
 * 小修作业记录表
 */

public class TaskRegist extends BaseModel {
    public String routeNamePeg;             //线路、桩号

    public String workBasis;                //作业依据

    public String implementationPlans;      //实施方案及作业内容要点

    public String approvedSheet;            //数量现场核定表（桩号、计算过程、简图及工料机消耗，可附页）

    public String checkDate;                  //日期

    public String taskRecordNo;             //编号

    public String thirdDeptName;            //养护作业单位

    public String maintainTaskItemId;       //作业任务id

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
