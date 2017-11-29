package com.chengtech.chengtechmt.entity.monitoremergency;

/**
 * 作者: LiuFuYingWang on 2017/11/28 11:31.
 * 内部位移监测
 */

public class Inclinationdata {
    public String moduleNo;            //模块号
    public String channelId;            //通道号
    public String acquisitionDatetime; //采集时间
    public Double angleOriginalX; 		// x方向角度值
    public Double angleOriginalY; 		// y方向角度值
    public Double angleOffsetX; 		// 计算后x方向偏移量
    public Double angleOffsetY; 		// 计算后y方向偏移量
}
