package com.chengtech.chengtechmt.entity;

import java.io.Serializable;
import java.util.List;

import io.realm.RealmObject;

/**
 * 作者: LiuFuYingWang on 2016/5/16 14:22.
 */
public abstract class BaseModel  implements Serializable {

    public String id;
    public String name ;
    public String code;
    public String sessionName;
    public String memo;
    public String sessionId = "";
    public String firstDeptId;		//管理局id
    public String secondDeptId;		// 管养单位
    public String thirdDeptId;		// 巡查单位

    public abstract List<String> getContent();
    public abstract List<String> getTitles();
    public abstract List<String> getPropertyName();
}
