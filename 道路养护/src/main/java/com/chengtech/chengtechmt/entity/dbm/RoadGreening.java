package com.chengtech.chengtechmt.entity.dbm;

import com.chengtech.chengtechmt.entity.BaseModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者: LiuFuYingWang on 2017/3/16 15:45.
 */

public class RoadGreening extends BaseModel {

    public String belongDeptId;		// 管养单位
    public String routeCode;			// 路线编号
    public String stake;				// 桩号
    public Double mileage;				// 线路里程
    public Double greenArea;			// 绿化面积
    public Double greenMileage;		// 可绿化里程
    public Double beGreenedMileage;	// 已绿化里程
    public Double greenRatio;			// 绿化率
    public String plantGrowthStatus;	// 植物成长状态
    public String plantSpecies;		// 主要植物种类
    public String isHandOver ; //是否移交
    public String tabPerson;			// 制表人
    public String checkPerson;			// 审核
    public String tabTime;				// 制表日期
    public String image;				// 照片
    public String deptIds; //不保存数据库，便于查询数据
    @Override
    public List<String> getContent() {
        List<String> values = new ArrayList<>();
        values.add(code==null?"":code);
        values.add(routeCode==null?"":routeCode);
        values.add(stake==null?"":stake);
        values.add(mileage==null?"":String.valueOf(mileage));
        values.add(greenArea==null?"":String.valueOf(greenArea));
        values.add(greenMileage==null?"":String.valueOf(greenMileage));
        values.add(beGreenedMileage==null?"":String.valueOf(beGreenedMileage));
        values.add(greenRatio==null?"":String.valueOf(greenRatio));
        values.add(plantGrowthStatus==null?"":plantGrowthStatus);
        values.add(plantSpecies==null?"":plantSpecies);
        values.add(isHandOver==null?"":isHandOver);
        values.add(tabPerson==null?"":tabPerson);
        values.add(checkPerson==null?"":checkPerson);
        values.add(tabTime==null?"":tabTime);
        return values;
    }

    @Override
    public List<String> getTitles() {
        List<String> value = new ArrayList<>();
        value.add("序号");
        value.add("路线名称");
        value.add("桩号");
        value.add("线路里程");
        value.add("绿化面积(M2)");
        value.add("可绿化里程(公里)");
        value.add("已绿化里程（公里）");
        value.add("绿化率（%）");
        value.add("植物成长状态");
        value.add("主要植物种类");
        value.add("是否移交");
        value.add("制表");
        value.add("审核");
        value.add("制表日期");
        return value;
    }

    @Override
    public List<String> getPropertyName() {
        return null;
    }
}
