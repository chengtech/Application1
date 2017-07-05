package com.chengtech.chengtechmt.entity;

import java.io.Serializable;

/**
 * 作者: LiuFuYingWang on 2016/6/30 10:13.
 */
public class MediumPlanprogressItem implements Serializable{

    public String mediumPlanprogressId;	//父表ID
    public String projectName;				//工程名称
    public String startPeg;			//开始桩号
    public String endPeg; //结束桩号
    public String maintainLength;//维修长度
    public String projectScope;//工程范围
    public String projectContent;//工程内容
    public String upOrDown;//上下行；上行下行
    public String projectNum;  //路面工程数量(m2)
    public String budgetFund;				//预算下达资金
    public String replyFund;				//批复预算金额
    public String paidFund;				//已支付金额
    public String notPaidFund;				//未支付金额
    public String progressSituation;		//项目进度情况
    public String beginDate;	                //开工时间
    public String finishDate;	            //完工时间
    public String subitemType;//分项类型
    public String checkSituation;		//检测情况
    public String routeCode;             //区段编号
    public String sortOrderCode;             //区段排序
    public String memo;
}
