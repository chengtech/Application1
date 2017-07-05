package com.chengtech.chengtechmt.entity.bridge;

import com.chengtech.chengtechmt.entity.BaseModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者: LiuFuYingWang on 2016/7/1 9:32.
 */
public class BridgeSubStructure extends BaseModel implements Serializable {

    public String pier;                 //下部墩台
    public String forms;                //下部结构形式
    public String materials;            //下部材料
    public String basicForm;            //下部基础形式

    public List<String> propetryValues;

    public List<String> getContent() {
        if (propetryValues == null) {
            propetryValues = new ArrayList<>();
            propetryValues.add(pier == null ? "" : pier);
            propetryValues.add(forms == null ? "" : forms);
            propetryValues.add(materials == null ? "" : materials);
            propetryValues.add(basicForm == null ? "" : basicForm);
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
