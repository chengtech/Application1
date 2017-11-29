package com.chengtech.chengtechmt.entity.monitoremergency;

/**
 * 作者: LiuFuYingWang on 2017/11/27 16:43.
 */

public class Lvdtdata {
    public String moduleNo;            //模块号
    public String channelId;            //通道号
    public String acquisitionDatetime; //采集时间
    //	scanFlag;	//扫描位置标志
    //fsid;	//用于sqlite数据导入
    // id
    // delFlag
    // sortOrder

    public Double originalDisplayment;    // 采集位移值
    public Double offsetDisplayment;        // 与初始值的位移变量
    public Double offsetDisplaymentIncr;    // 与初始值的位移变量增量
    public String incrFlag;                // 增量标识
}
