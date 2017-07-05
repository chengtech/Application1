package com.chengtech.chengtechmt.entity.tunnel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者: LiuFuYingWang on 2016/7/4 14:29.
 */
public class TunnelTech implements Serializable {
    public String	checkYear;	             //检查年月
    public String	checkType;	             //检查类型
    public String	rateGrade;	             //评定等级
    public String	entranceAssessment;      //洞口评定
    public String	portalAssessment;        //洞门评定
    public String	liningAssessment;        //衬砌评定
    public String	pavementAssessment;      //路面评定
    public String	maintenanceAssessment;   //检修道评定
    public String	drainageSystemAssessment;//排水系统评定
    public String	ceilingAssessment;	     //内装

    public List<String> propetryValues;
    public List<String> getList(){
        if (propetryValues==null) {
            propetryValues = new ArrayList<>();
            propetryValues.add(checkYear==null?"":checkYear);
            propetryValues.add(checkType==null?"":checkType);
            propetryValues.add(rateGrade==null?"":rateGrade);
            propetryValues.add(entranceAssessment==null?"":entranceAssessment);
            propetryValues.add(portalAssessment==null?"":portalAssessment);
            propetryValues.add(liningAssessment==null?"":liningAssessment);
            propetryValues.add(pavementAssessment==null?"":pavementAssessment);
            propetryValues.add(maintenanceAssessment==null?"":maintenanceAssessment);
            propetryValues.add(drainageSystemAssessment==null?"":drainageSystemAssessment);
            propetryValues.add(ceilingAssessment==null?"":ceilingAssessment);
        }
        return propetryValues;
    }
}
