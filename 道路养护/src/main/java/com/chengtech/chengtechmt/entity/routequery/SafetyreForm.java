package com.chengtech.chengtechmt.entity.routequery;

import java.util.Date;


/**
 * 安全检查（整改）记录表
 */
public class SafetyreForm {

    public String id;                  //ID
    public String workUnit; //作业单位
    public String examineDept; //检查部门
    public String examineType; //检查类型
    public Date examineDate; //检查时间
    public String examineProject; //检查项目
    public String examineContent; //检查内容
    public String existMatter; //存在问题
    public String reformStep; //整改措施
    public String reformResult; //整改结果
    public boolean result;           //判断流程处理者
    public String year;//作业年
    public String month;//作业月
    public String taskOrganizationId;//任务单Id
    public String safetyFormNo;            //编号


}
