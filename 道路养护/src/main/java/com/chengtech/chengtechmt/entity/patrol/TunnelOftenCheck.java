package com.chengtech.chengtechmt.entity.patrol;

import com.chengtech.chengtechmt.entity.BaseModel;
import com.chengtech.chengtechmt.entity.tunnel.Tunnel;
import com.chengtech.chengtechmt.util.DateUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 隧道经常检查
 *
 * @author liutao 2014-11-14 09:30
 */
@SuppressWarnings("serial")
public class TunnelOftenCheck extends BaseModel {


    public String routeId;                      //路线id
    public String routeCode;                    //路线编号

    public String head;                          //负责人

    public String record;                        //记录人

    public String supervisor;                    //监察人

    public String routeName;                     //路线名称

    public String weather;                       //天气

    public String overallEvaluation;             //总体评价

    public List<TunnelRecord> listTunnelRecords = new ArrayList<>(); //子记录
    public String secondDeptName;
    public String thirdDeptName;
    public String sectionId;                     //路段
    public String year;                          // 年份
    public String month;                         // 月份
    public String patrolDeptId; //巡查单位
    public String patrolDate;         //巡查日期
    public String tunnelCode;       //隧道编码
    public List<Tunnel> listTunnel;
    public List<String> listWeather;
    public List<String> listProjectName;
    public Map<String,Integer> savePosition = new HashMap<>(); //该map是用于保存spinner上一次选择的结果的位置，复写回上次保存的结果

    @Override
    public List<String> getContent() {
        List<String> values = new ArrayList<>();
        values.add(routeCode == null ? "" : routeCode);
//        values.add(routeCode == null ? "" : routeCode);
        values.add(thirdDeptName == null ? "" : thirdDeptName);
        values.add(secondDeptName == null ? "" : secondDeptName);
        values.add(weather == null ? "" : weather);
        values.add(tunnelCode == null ? "" : tunnelCode);
        values.add(name == null ? "" : name);
        values.add(patrolDate == null ? "" : DateUtils.convertDate2(patrolDate));
        values.add(overallEvaluation == null ? "" : (overallEvaluation.equals("null")?"":overallEvaluation));
        return values;
    }

    @Override
    public List<String> getTitles() {
        List<String> values = new ArrayList<>();
        values.add("路线编码");
//        values.add("路线名称");
        values.add("巡查单位");
        values.add("管理单位");
        values.add("天气");
        values.add("隧道编码");
        values.add("隧道名称");
        values.add("巡查时间");
        values.add("技术状况评价");
        return values;
    }

    @Override
    public List<String> getPropertyName() {
        return null;
    }
}
