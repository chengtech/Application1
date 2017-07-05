package com.chengtech.chengtechmt.entity.monitoremergency;

import com.chengtech.chengtechmt.entity.BaseModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者: LiuFuYingWang on 2017/6/9 15:00.
 */

public class WarningManage extends BaseModel {

    public SideMonitorType sideMonitorType;
    public String sideMonitorTypeId;                //监测设备id
    public String warningLevel;                            //告警级别
    public String createDate;                        //告警产生时间
    public String cause;                            //告警可能原因
    public String warningMessage;                    //告警信息
    public Integer count;                                //告警次数
    public String detaiMessage;                    //详细信息
    public String state;                            //状态


    @Override
    public List<String> getContent() {
        List<String> values = new ArrayList<>();
        values.add(sideMonitorTypeId==null?"":sideMonitorTypeId);
        values.add("");
        values.add(warningLevel==null?"":warningLevel);
        values.add(createDate==null?"":createDate);
        values.add(cause==null?"":cause);
        values.add(warningMessage==null?"":warningMessage);
        values.add(count==null?"":String.valueOf(count));
        values.add(detaiMessage==null?"":detaiMessage);
        values.add(state==null?"":state);
        return values;
    }

    @Override
    public List<String> getTitles() {
        List<String> titles = new ArrayList<>();
        titles.add("所属边坡");
        titles.add("告警源");
        titles.add("告警级别");
        titles.add("告警产生时间");
        titles.add("告警可能原因");
        titles.add("告警信息");
        titles.add("告警次数");
        titles.add("详细信息");
        titles.add("状态");
        return titles;
    }

    @Override
    public List<String> getPropertyName() {
        return null;
    }
}
