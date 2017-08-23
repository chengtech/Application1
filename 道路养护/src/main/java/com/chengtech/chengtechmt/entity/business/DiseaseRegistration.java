package com.chengtech.chengtechmt.entity.business;

import android.app.SearchableInfo;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 作者: LiuFuYingWang on 2017/8/16 14:59.
 * 病害登记实体类
 */

public class DiseaseRegistration implements Serializable {

    public String recordDate; //登记日期
    public String recordMan;  //登记人
    public String describeMsg; //语音描述内容
    public String recordPaths; //语音保存路径
    public String recordLength; //语音长度
    public ArrayList<String> picPaths = new ArrayList<>(); //图片保存的路径集合
    public boolean isUpload;  //是否已经上存服务器
    public String recordState = "0"; //记录状态，"0"表示是新增(默认)，"1"表示是已保存在本地，是编辑状态；

}
