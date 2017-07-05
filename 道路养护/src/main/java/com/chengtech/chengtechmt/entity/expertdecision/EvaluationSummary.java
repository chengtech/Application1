package com.chengtech.chengtechmt.entity.expertdecision;

import com.chengtech.chengtechmt.entity.BaseModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者: LiuFuYingWang on 2017/2/17 14:19.
 */

public class EvaluationSummary extends BaseModel {

    public String routeName;            //路线名称
    public String year;                //年
    public String month;                //月
    public Double evaluationLength = 0d;        //评定长度
    public Double verageMqi = 0d;                //平均mqi
    public Double upMqi = 0d;                    //上行mqi
    public Double downMqi = 0d;                    //下行mqi
    public Double upEvaluationLength = 0d;        //上行评定长度
    public String evaluationLevel;            //评定等级
    public String upEvaluationLevel;        //上行评定等级
    public String downEvaluationLevel;        //下行评定等级
    public Double downEvaluationLength = 0d;    //下行评定长度
    public String direction;                // 方向
    public String laneType;    // 车道类型
    public String startStake;                //里程起点
    public String endStake;                //里程终点
    public String routeCode;                //路线编码

    @Override
    public List<String> getContent() {
        List<String> content = new ArrayList<>();
        content.add(routeName == null ? "" : routeName);
        content.add(evaluationLength == null ? "" : String.valueOf(evaluationLength));
        content.add(verageMqi == null ? "" : String.valueOf(verageMqi));
        content.add(evaluationLevel == null ? "" : evaluationLevel);
        content.add(upMqi == null ? "" : String.valueOf(upMqi));
        content.add(upEvaluationLevel == null ? "" : upEvaluationLevel);
        content.add(upEvaluationLength == null ? "" : String.valueOf(upEvaluationLength));
        content.add(downMqi == null ? "" : String.valueOf(downMqi));
        content.add(downEvaluationLevel == null ? "" : downEvaluationLevel);
        content.add(downEvaluationLength == null ? "" : String.valueOf(downEvaluationLength));
        return content;
    }

    @Override
    public List<String> getTitles() {
        List<String> titles = new ArrayList<>();
        titles.add("路线名称：");
        titles.add("评定长度(km)：");
        titles.add("平均MQI：");
        titles.add("评定等级：");
        titles.add("平均MQI（上行）：");
        titles.add("评定等级（上行）：");
        titles.add("上行评定长度（km）：");
        titles.add("平均MQI（下行）：");
        titles.add("评定等级（下行）：");
        titles.add("下行评定长度（km）：");
        return titles;
    }

    @Override
    public List<String> getPropertyName() {
        return null;
    }
}
