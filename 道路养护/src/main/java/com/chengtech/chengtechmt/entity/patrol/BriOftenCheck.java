package com.chengtech.chengtechmt.entity.patrol;

import com.chengtech.chengtechmt.entity.BaseModel;
import com.chengtech.chengtechmt.entity.bridge.Bridge;
import com.chengtech.chengtechmt.util.DateUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者: LiuFuYingWang on 2017/3/29 15:11.
 * 桥梁经常检查
 */

public class BriOftenCheck extends BaseModel {

    public String briName;
    public String routeName;
    public String secondDeptName;
    public String thirdDeptName;
    public String routeCode;
    public String head;
    public String record;
    public String weather;
    public String routeId;
    public String brino;        // 桥梁编号
    public String bripeg;        // 桥梁桩号
    public String checkDate;    // 检查日期
    public String checkDeptId; // 管理处(检查单位)
    public List<BridgeRecord> listBridgeCheckRecord;

    public Map<String,Integer> savePosition = new HashMap<>(); //该map是用于保存spinner上一次选择的结果的位置，复写回上次保存的结果
    public List<Bridge> listBridge;


    @Override
    public List<String> getContent() {
        List<String> value = new ArrayList<>();
        value.add(secondDeptName == null ? "" : secondDeptName);
//        value.add(routeCode == null ? "" : routeCode);
        value.add(routeName == null ? "" : routeName);
        value.add(bripeg == null ? "" : bripeg);
        value.add(brino == null ? "" : brino);
        value.add(briName == null ? "" : briName);
        value.add(thirdDeptName == null ? "" : thirdDeptName);
        value.add(head == null ? "" : (head.equals("null") ? "" : head));
        value.add(record == null ? "" : record);
        value.add(checkDate == null ? "" : DateUtils.convertDate2(checkDate));
        return value;
    }

    @Override
    public List<String> getTitles() {
        List<String> value = new ArrayList<>();
        value.add("管理单位");
//        value.add("路线编码");
        value.add("路线名称");
        value.add("桥位中心桩号");
        value.add("桥梁编码");
        value.add("桥梁名称");
        value.add("巡查单位");
        value.add("负责人");
        value.add("记录人");
        value.add("巡查时间");
        return value;
    }

    @Override
    public List<String> getPropertyName() {
        return null;
    }


}
