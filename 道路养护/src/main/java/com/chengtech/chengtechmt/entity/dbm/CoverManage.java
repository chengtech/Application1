package com.chengtech.chengtechmt.entity.dbm;

import com.chengtech.chengtechmt.entity.BaseModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 井盖管理卡片信息
 * 作者: LiuFuYingWang on 2017/3/14 15:44.
 */

public class CoverManage extends BaseModel {

    public String line;            // 路线
    public String stake;            // 桩号
    public String coverNumber;    // 编号
    public String materialType;    // 材料类型
    public String deviceSize;        // 规格尺寸
    public String position;        // 具体位置
    public String isNeedReform;    // 改造

    @Override
    public List<String> getContent() {
        List<String> value = new ArrayList<>();
        value.add(line == null ? "" : line);
        value.add(stake == null ? "" : stake);
        value.add(coverNumber == null ? "" : coverNumber);
        value.add(materialType == null ? "" : materialType);
        value.add(deviceSize == null ? "" : deviceSize);
        value.add(position == null ? "" : position);
        value.add(isNeedReform == null ? "" : isNeedReform);
        value.add(memo == null ? "" : memo);
        return value;
    }

    @Override
    public List<String> getTitles() {
        List<String> value = new ArrayList<>();
        value.add("路线");
        value.add("桩号");
        value.add("编号");
        value.add("材质");
        value.add("规格");
        value.add("具体位置");
        value.add("是否改造");
        value.add("备注");
        return value;
    }

    @Override
    public List<String> getPropertyName() {
        return null;
    }
}
