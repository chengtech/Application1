package com.chengtech.chengtechmt.entity.patrol;

import com.chengtech.chengtechmt.entity.BaseModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者: LiuFuYingWang on 2017/4/5 14:49.
 * 涵洞经常检查记录表
 */

public class CulvertOftenRecord extends BaseModel {

    public String culertOftenId;         //父表id

    public String culvertName;              //涵洞名称
    public String checkSituation;          //检查情况
    public String diseaseSituation;      //病害情况
    public String treatmentOpinionOne;      //处理意见1
    public String treatmentOpinionTwo;      //处理意见2
    public String treatmentOpinionThree; //处理意见3
    public String sortOrder ;  //排序

    public String[] listCheckSituationByConstantDD;
    public String[] listTreatmentOpinion;

    @Override
    public List<String> getContent() {
        List<String> values = new ArrayList<>();
        values.add(sortOrder==null?"":sortOrder);
        values.add(culvertName==null?"":culvertName);
        values.add(checkSituation==null?"":(checkSituation.equals("0")?"否":"是"));
        values.add(diseaseSituation==null?"":diseaseSituation);
        values.add(treatmentOpinionOne==null?"":treatmentOpinionOne);
        return values;
    }

    @Override
    public List<String> getTitles() {
        List<String> values = new ArrayList<>();
        values.add("序号");
        values.add("部件名称");
        values.add("检查情况");
        values.add("病害情况");
        values.add("处理意见");
        return values;
    }

    @Override
    public List<String> getPropertyName() {
        return null;
    }
}
