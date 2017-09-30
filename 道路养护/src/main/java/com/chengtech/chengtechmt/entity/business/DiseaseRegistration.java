package com.chengtech.chengtechmt.entity.business;

import com.chengtech.chengtechmt.entity.BaseModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者: LiuFuYingWang on 2017/8/16 14:59.
 * 病害登记实体类
 */

public class DiseaseRegistration extends BaseModel implements Serializable {

    public String patrolTime; //登记日期
    public String patrolMan;  //登记人
    public String diseaseDescription; //语音描述内容
    public List<DiseaseVoiceRecord> listDiseaseVoiceRecord; //多条语音集合
    public ArrayList<String> picPaths = new ArrayList<>(); //图片保存的路径集合
    public boolean isUpload;  //是否已经上存服务器
    public String recordState = "0"; //记录状态，"0"表示是新增(默认)，"1"表示是已保存在本地，是编辑状态；
    public String site;//位置信息
    public String weather;//天气
    public String longitude; //经度
    public String latitude;  //纬度
    public String path;

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
