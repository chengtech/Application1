package com.chengtech.chengtechmt.entity.routequery;

import java.util.Date;
import java.util.List;

/**
 * 作者: LiuFuYingWang on 2016/8/10 16:25.
 */
public class TaskOrganization {

    public String id;
    public String noticeNo;        //通知单号
    public String direction;      //方向 //暂存路线名称跟桩号
    public String maintenance;    //保养工程
    public String numberDays;     //天数
    public String billMan;        //发单人//发单单位
    public Date billDate;         //发单日期
    public String orderMan;       //接单人
    public Date orderDate;        //接单日期
    public String caseDescription;//情况说明
    //    public List<TaskRecord> taskRecords = new ArrayList<TaskRecord>();
    public String year;
    public String month;
    public String isSub;           //是否提交 1 已提交 0 未提交

    public String twoAuditMan;//二级审核人
    public Date twoAuditDate;//二级审核日期

    public Date payDate;          //支付施工日期
    public Date finishDate;       //完工日期
    public String workPlanIds;    //计划任务Ids
    public double money;    //金额

    //用来保存选择任务单是保存计划施工队 桩号
    public String workTeam;
    public String routePeg;

    public String taskSource;        //任务单来源    0  计划   1  编制 建议
    public String state; // state 状态 //1已签收 2 已做交底 22 已做报备 3 已做作业记录 25 已做作业整改 4 已验收 5正在整改
    public String munitPatrolIds;                //日常巡查表ID

    public String briOftenIds;                   //桥梁经常表ID
    public String briRegularIds;                 //桥梁定期表ID
    public String culvertOftenIds;               //涵洞经常表ID
    public String culvertRegularIds;             //涵洞定期表ID
    public String tunnelOftenIds;                //隧道经常表ID
    public String tunnelRegularIds;              //隧道定期表ID
    public String roadSurveyIds;                 //路况调查ID
    public String routeName;                     //路线名称
    public String startpeg;                      //开始桩号
    public String endpeg;                        //结束桩号

    public class TaskOrganizationG {
        public List<TaskOrganization> rows;
    }
}
