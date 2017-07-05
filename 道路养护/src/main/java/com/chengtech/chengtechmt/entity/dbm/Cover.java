package com.chengtech.chengtechmt.entity.dbm;

import com.chengtech.chengtechmt.entity.BaseModel;
import com.chengtech.chengtechmt.util.DateUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 井盖卡片实体类
 * 作者: LiuFuYingWang on 2016/11/22 11:33.
 */

public class Cover extends BaseModel {
    public ArrayList<String> detailContent;
    public ArrayList<String> detailTitles;
    public ArrayList<String> detailFieldNames;

    public Date installTime;        //安装时间
    public String installLocation;//安装地点
    public String deviceSize;    //规格尺寸
    public String materialType;    //材料类型
    public String longitude; //经度
    public String latitude;  //纬度
    public String state;     //状态
    public String mgDept;            //管养单位
    public String deviceType;        //设备类型
    public String telephone;        //电话

    @Override
    public ArrayList<String> getContent() {
        if (detailContent == null)
            detailContent = new ArrayList<>();
        detailContent.add(code == null ? "" : code);
        detailContent.add(installTime == null ? "" : DateUtils.convertDate2(installTime));
        detailContent.add(installLocation == null ? "" : installLocation);
        detailContent.add(deviceSize == null ? "" : deviceSize);
        detailContent.add(materialType == null ? "" : materialType);
        detailContent.add(longitude == null ? "" : longitude);
        detailContent.add(latitude == null ? "" : latitude);
        detailContent.add(state == null ? "" : state);
        detailContent.add(mgDept == null ? "" : mgDept);
        detailContent.add(deviceType == null ? "" : deviceType);
        detailContent.add(telephone == null ? "" : telephone);
        return detailContent;
    }

    public ArrayList<String> getTitles() {
        if (detailTitles == null)
            detailTitles = new ArrayList<>();
        detailTitles.add("编号");
        detailTitles.add("安装时间");
        detailTitles.add("安装地点");
        detailTitles.add("规格尺寸");
        detailTitles.add("材料类型");
        detailTitles.add("经度");
        detailTitles.add("纬度");
        detailTitles.add("状态");
        detailTitles.add("管养单位");
        detailTitles.add("设备类型");
        detailTitles.add("电话");
        return detailTitles;
    }

    @Override
    public List<String> getPropertyName() {
        return null;
    }

    public ArrayList<String> getFieldNames() {
        if (detailFieldNames == null)
            detailFieldNames = new ArrayList<>();
        detailFieldNames.add("code");
        detailFieldNames.add("installTime");
        detailFieldNames.add("installLocation");
        detailFieldNames.add("deviceSize");
        detailFieldNames.add("materialType");
        detailFieldNames.add("longitude");
        detailFieldNames.add("latitude");
        detailFieldNames.add("state");
        detailFieldNames.add("mgDept");
        detailFieldNames.add("deviceType");
        detailFieldNames.add("telephone");
        return detailFieldNames;
    }

    public class CoverG{
        public ArrayList<Cover> rows;
    }
}
