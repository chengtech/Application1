package com.chengtech.chengtechmt.entity.routequery;

import com.chengtech.chengtechmt.util.DateUtils;
import com.chengtech.chengtechmt.util.MyConstants;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * 公路小修保养作业（专项）验收
 */
public class Acceptance {

    public String id;
public String name;
    public String routeNamePeg;            //线路、桩号范围
    public String thirdDeptId;
    public String cost;                    //造价

    public String numberApproved;          //数量核定

    public String mainContent;             //主要内容

    public Date beginDate;                 //开工日期

    public Date finishDate;                //完工日期

    public boolean result;                 //判断流程处理者

    public String taskOrganizationId;//任务单Id

    public String acceptanceNo;             //编号

    public Date acceptDate;                 //验收日期

    public String routeId;                        //路线
    public String sectionId;                      //路段
    public String year;                           // 年份
    public String month;                          // 月份

    public List<String> getArray() {
        List<String> valus = new ArrayList<>();
        valus.add(MyConstants.deptTree.get(thirdDeptId));
        valus.add(acceptanceNo);
        valus.add(name);
        valus.add(routeNamePeg);
        valus.add(cost);
        valus.add(DateUtils.convertDate2(beginDate));
        valus.add(DateUtils.convertDate2(finishDate));
        valus.add(mainContent);
        valus.add(numberApproved);
        return valus;
    }

}