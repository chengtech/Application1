package com.chengtech.chengtechmt.entity.routequery;

import com.chengtech.chengtechmt.entity.BaseModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者: LiuFuYingWang on 2016/12/13 15:29.
 */

public class TrafficVolume extends BaseModel {

    private String routeCode; //路线编号
    //    private Observatory	observatory;
    private String observatoryId;                 //观测站
    private String observatoryNumber; // 观测站编号
    private String observatoryName; //观测站名称
    private String startStake; //起始桩号
    private String endStake;  //终点桩号
    private String registrationYear; //年份
    private String equivalents; //当量
    private String adaptiveTraffic; //自适应交通量
    private Double congestionDegree; //交通拥挤度
//    private Dept belongDept;
//    private String belongDeptId;	//所属管理单位id
//    private String deptIds;			//查询用，不保存数据库
//    private Double doubleStartStake; //原始的起始桩号
//    private Double doubleEndStake; //原始的止点桩号

    @Override
    public List<String> getContent() {
        List<String> content = new ArrayList<>();
        content.add(routeCode == null ? "" : routeCode);
        content.add(observatoryNumber == null ? "" : observatoryNumber);
        content.add(observatoryName == null ? "" : observatoryName);
        content.add(startStake == null ? "" : startStake);
        content.add(endStake == null ? "" : endStake);
        content.add(equivalents == null ? "" : equivalents);
        content.add(adaptiveTraffic == null ? "" : adaptiveTraffic);
        content.add(congestionDegree == null ? "" : congestionDegree + "");
        content.add(registrationYear == null ? "" : registrationYear);
        return content;
    }

    public List<String> getTitles() {
        List<String> titles = new ArrayList<>();

        titles.add("路线：");
        titles.add("观测点编号：");
        titles.add("观测点名称：");
        titles.add("起始桩号：");
        titles.add("终点桩号：");
        titles.add("当量数合计：");
        titles.add("适应交通量(辆／日)：");
        titles.add("交通拥挤度：");
        titles.add("填报日期：");
        return titles;
    }

    @Override
    public List<String> getPropertyName() {
        return null;
    }


}
