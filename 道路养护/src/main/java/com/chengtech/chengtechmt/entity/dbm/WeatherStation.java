package com.chengtech.chengtechmt.entity.dbm;

import com.chengtech.chengtechmt.entity.BaseModel;
import com.chengtech.chengtechmt.util.DateUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者: LiuFuYingWang on 2016/11/22 11:47.
 */

public class WeatherStation extends BaseModel {

    public ArrayList<String> detailContent;
    public ArrayList<String> detailTitles;
    public ArrayList<String> detailFieldNames;

    public String location;// 位置
    public String manager; // 管理人
    public String sim;    // sim卡
    public String frequency; // 数据采集频率
    public String longitude; //经度
    public String latitude;  //纬度

    @Override
    public ArrayList<String> getContent() {
        if (detailContent == null)
            detailContent = new ArrayList<>();
        detailContent.add(code == null ? "" : code);
        detailContent.add(name == null ? "" : name);
        detailContent.add(location == null ? "" : location);
        detailContent.add(manager == null ? "" : manager);
        detailContent.add(sim == null ? "" : sim);
        detailContent.add(frequency == null ? "" : frequency);
        detailContent.add(longitude == null ? "" : longitude);
        detailContent.add(latitude == null ? "" : latitude);
        return detailContent;
    }

    public ArrayList<String> getTitles() {
        if (detailTitles == null)
            detailTitles = new ArrayList<>();
        detailTitles.add("编号");
        detailTitles.add("名称");
        detailTitles.add("位置");
        detailTitles.add("管理人");
        detailTitles.add("sim卡号码");
        detailTitles.add("数据采集频率（分）");
        detailTitles.add("经度");
        detailTitles.add("纬度");
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
        detailFieldNames.add("name");
        detailFieldNames.add("location");
        detailFieldNames.add("manager");
        detailFieldNames.add("sim");
        detailFieldNames.add("frequency");
        detailFieldNames.add("longitude");
        detailFieldNames.add("latitude");
        return detailFieldNames;
    }

    public class WeatherStationG{
        public ArrayList<WeatherStation> rows;
    }
}
