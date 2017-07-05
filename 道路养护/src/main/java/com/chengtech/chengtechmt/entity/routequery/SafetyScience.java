package com.chengtech.chengtechmt.entity.routequery;

import com.chengtech.chengtechmt.util.DateUtils;
import com.chengtech.chengtechmt.util.MyConstants;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * 安全技术交底
 */
public class SafetyScience {

    public String id;                  //ID

    public String conserveUnit; //作业单位
    public String serNumber; //编号
    public String workName; //作业名称
    public String workTeam; //作业班组
    public Date disclosureDate; //交底日期
    public String disclosureContent; //交底内容
    public String thirdDeptId;
    public boolean result;           //判断流程处理者
    public String year;//作业年
    public String month;//作业月

    public String taskOrganizationId;  //任务单Ids

    public String templateType;  //交底模板类型


    public List<String> getArray() {
        List<String> valus = new ArrayList<>();

        valus.add(MyConstants.deptTree.get(thirdDeptId));
        valus.add(serNumber);
        valus.add(workName);
        valus.add(workTeam);
        valus.add(DateUtils.convertDate2(disclosureDate));
        valus.add(disclosureContent);
        return valus;
    }
}
