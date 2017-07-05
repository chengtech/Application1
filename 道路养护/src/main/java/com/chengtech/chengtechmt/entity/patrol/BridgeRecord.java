package com.chengtech.chengtechmt.entity.patrol;

import com.chengtech.chengtechmt.entity.BaseModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者: LiuFuYingWang on 2017/3/29 15:13.
 * 桥梁经常检查记录数
 */

public class BridgeRecord extends BaseModel {


    public String bridgeOftenCheckId; // 主表id
    public String defectType ="";           // 缺损类型

    public String defectArea ="";           // 缺损范围

    public String repairView ="";           // 保养意见

    public int partNo;                  // 部件编号

    public String partName;

    public String [] listDefectTypeByConstantDD;  //缺损类型的字符串数组


    @Override
    public List<String> getContent() {
        List<String> values = new ArrayList<>();
        values.add(String.valueOf(partNo));
        values.add(partName == null ? "" : partName);
        values.add(defectType == null ? "" : defectType);
        values.add(defectArea == null ? "" : (defectArea.equals("null") ? "" : defectArea));
        values.add(repairView == null ? "" : (repairView.equals("null") ? "" : repairView));
        return values;
    }

    @Override
    public List<String> getTitles() {
        List<String> values = new ArrayList<>();
        values.add("部件编号");
        values.add("部件名称");
        values.add("缺损类型");
        values.add("缺损范围");
        values.add("保养措施意见");
        return values;
    }

    @Override
    public List<String> getPropertyName() {
        return null;
    }
}
