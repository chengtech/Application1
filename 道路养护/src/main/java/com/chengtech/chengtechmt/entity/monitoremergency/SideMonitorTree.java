package com.chengtech.chengtechmt.entity.monitoremergency;

import com.chengtech.chengtechmt.entity.BaseModel;
import com.chengtech.chengtechmt.entity.Slope;

import java.util.List;

/**
 * 作者: LiuFuYingWang on 2017/6/16 16:03.
 */

public class SideMonitorTree extends BaseModel{

    public String url; // URL
    public String typeName; // 类型名称
    public String isShowType; // 是否显示检测类型
    public String nodeType; // 节点类型，如模板管理，结构物等，暂时用于模板管理节点
    public Slope slope;
    public String slopeId; // 边坡id
    // 边坡属性
    public String svgName; // 边坡svg文件名
    public Long frequency; // 边坡采集频率
    public String sname; // 边坡通信节点名称

    // 监测类型属性
    public String sideMonitorTypeName;

    // 2017-6-15字段添加(不存数据库),用于手机端
    public List<SideMonitorTree> listSideMonitorTreeByParentId; // 根据父id获取树节点下的子节点
    public List<SideMonitorType> listSideMonitorType; // 某监测设备下的所有的具体的监测设备

    @Override
    public List<String> getContent() {
        return null;
    }

    @Override
    public List<String> getTitles() {
        return null;
    }

    @Override
    public List<String> getPropertyName() {
        return null;
    }
}
