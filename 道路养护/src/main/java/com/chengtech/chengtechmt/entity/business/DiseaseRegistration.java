package com.chengtech.chengtechmt.entity.business;

import android.app.SearchableInfo;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 作者: LiuFuYingWang on 2017/8/16 14:59.
 * 病害登记实体类
 */

public class DiseaseRegistration implements Serializable {

    public String recordTime;
    public String describeMsg;
    public String recordPaths;
    public String recordLength;
    public ArrayList<String> picPaths = new ArrayList<>();
}
