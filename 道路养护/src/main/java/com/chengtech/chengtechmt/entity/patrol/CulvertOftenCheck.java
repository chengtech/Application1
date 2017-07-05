package com.chengtech.chengtechmt.entity.patrol;

import com.chengtech.chengtechmt.entity.BaseModel;
import com.chengtech.chengtechmt.entity.Culvert;
import com.chengtech.chengtechmt.util.DateUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者: LiuFuYingWang on 2017/4/5 14:36.
 * 涵洞经常检查
 */

public class CulvertOftenCheck extends BaseModel {

    public String head;                          //负责人

    public String checkMan;                      //检查人

    public String routeId;                      //路线Id
    public String routeCode;                    //路线Code
    public String routeName;                    //路线Name

    public String checkDate;                       //巡查时间

    public String culvertId;                     //涵洞Id
    public String culvertCode;                      //涵洞编号
    public String culvertPeg;                      //涵洞桩号
    public String sectionId;                      //区段id
    public String year;                         // 年份
    public String month;                        // 月份
    public String secondDeptName;
    public String thirdDeptName;


    public List<CulvertOftenRecord> listCulvertoftencheck;
    public List<Culvert> listCulvert;
    public Map<String,Integer> savePosition = new HashMap<>(); //该map是用于保存spinner上一次选择的结果的位置，复写回上次保存的结果

    @Override
    public List<String> getContent() {
        List<String> values = new ArrayList<>();
        values.add(routeCode == null ? "" : routeCode);
        values.add(routeName == null ? "" : routeName);
        values.add(culvertCode == null ? "" : culvertCode);
        values.add(culvertPeg == null ? "" : culvertPeg);
        values.add(checkDate == null ? "" : DateUtils.convertDate2(checkDate));
        values.add(thirdDeptName == null ? "" : thirdDeptName);
        values.add(head == null ? "" : head);
        values.add(checkMan == null ? "" : checkMan);
        return values;
    }

    @Override
    public List<String> getTitles() {
        List<String> values = new ArrayList<>();
        values.add("路线编号");
        values.add("路线名称");
        values.add("涵洞编号");
        values.add("涵洞桩号");
        values.add("巡查日期");
        values.add("养护单位");
        values.add("负责人");
        values.add("检查人");
        return values;
    }

    @Override
    public List<String> getPropertyName() {
        return null;
    }
}
