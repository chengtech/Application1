package com.chengtech.chengtechmt.entity.routequery;

import java.io.Serializable;
import java.util.List;

/**
 * 作者: LiuFuYingWang on 2016/7/26 17:43.
 */
public class PatrolRecord implements Serializable {

    public String id;
    public String beginPileCode;
    public String diseaseName;
    public String direct;
    public String diseasePosition;
    public String weather;
    public String endPileCode;
    public String diseaseNum;
    public String measureUnit;
    public String unitLong;
    public String unitWidth;
    public String scheme;
    public String unitHeigth;
    public String diseaseSeverity;
    public String roadway;
    public String isRepair;
    public String dealSituation;

    public class PatrolRecordG {
        public List<PatrolRecord> rows;
    }
}
