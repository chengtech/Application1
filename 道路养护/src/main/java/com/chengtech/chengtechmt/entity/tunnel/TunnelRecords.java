package com.chengtech.chengtechmt.entity.tunnel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 隧道修建记录 实体类
 * 作者: LiuFuYingWang on 2016/7/4 14:26.
 */
public class TunnelRecords implements Serializable{
    public String  tunnelRecordsId;   // id
    public String	constructDate;     //施工日期		
    public String	completeDate;      //竣工时间		
    public String  builtClass;        //修建类别		
    public String  builtReasons;      //修建原因		
    public String  projectScope;      //工程范围		
    public String	projectCost;       //工程费用(万元)		
    public String	fundSource;        //经费来源		
    public String	qualityEvaluation; //质量评定		
    public String	constructUnits;    //建设单位

    public List<String> propetryValues;
    public List<String> getList(){
        if (propetryValues==null) {
            propetryValues = new ArrayList<>();
            propetryValues.add(constructDate==null?"":constructDate);
            propetryValues.add(completeDate==null?"":completeDate);
            propetryValues.add(builtClass==null?"":builtClass);
            propetryValues.add(builtReasons==null?"":builtReasons);
            propetryValues.add(projectScope==null?"":projectScope);
            propetryValues.add(projectCost==null?"":projectCost);
            propetryValues.add(fundSource==null?"":fundSource);
            propetryValues.add(qualityEvaluation==null?"":qualityEvaluation);
            propetryValues.add(constructUnits==null?"":constructUnits);
        }
        return propetryValues;
    }
}
