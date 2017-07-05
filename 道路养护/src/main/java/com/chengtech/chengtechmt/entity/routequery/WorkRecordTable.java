package com.chengtech.chengtechmt.entity.routequery;

import com.chengtech.chengtechmt.util.DateUtils;
import com.chengtech.chengtechmt.util.MyConstants;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * 作业记录表
 */
public class WorkRecordTable {

    public String id;
    public String routeNamePeg;             //线路、桩号
    public String thirdDeptId;
    public String workBasis;                //作业依据

    public String implementationPlans;      //实施方案及作业内容要点

    public String approvedSheet;            //数量现场核定表（桩号、计算过程、简图及工料机消耗，可附页）

    public Date checkDate;                   //日期

    public boolean result;                   //判断流程处理者
    public String name;
    public String taskOrganizationId;         //任务单Id
    public String workRecordTableNo;          //编号

    public String routeId;                        //路线
    public String sectionId;                      //路段
    public String year;                           // 年份
    public String month;                          // 月份

    public String readerId;                //接收接短信人


    public List<String> getArray() {
        List<String> valus = new ArrayList<>();

        valus.add(MyConstants.deptTree.get(thirdDeptId));
        valus.add(DateUtils.convertDate2(checkDate));
        valus.add(name);
        valus.add(workRecordTableNo);
        valus.add(routeNamePeg);
        valus.add(workBasis);
        valus.add(implementationPlans);
        valus.add(approvedSheet);
        return valus;
    }
}