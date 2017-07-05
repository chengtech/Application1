package com.chengtech.chengtechmt.entity.bridge;

import com.chengtech.chengtechmt.entity.BaseModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 桥梁上部结构
 * 作者: LiuFuYingWang on 2016/7/1 9:28.
 */
public class BridgeSupStructure extends BaseModel implements Serializable {

    public String holeNo;                 //孔号
    public String form;                 //形式
    public String span;                 //跨径
    public String material;             //材料

    public List<String> propetryValues;

    public List<String> getContent() {
        if (propetryValues == null) {
            propetryValues = new ArrayList<>();
            propetryValues.add(holeNo == null ? "" : holeNo);
            propetryValues.add(form == null ? "" : form);
            propetryValues.add(span == null ? "" : span);
            propetryValues.add(material == null ? "" : material);
        }
        return propetryValues;
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
