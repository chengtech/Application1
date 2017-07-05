package com.chengtech.chengtechmt.entity.dbm;

import com.chengtech.chengtechmt.entity.BaseModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者: LiuFuYingWang on 2017/3/17 15:17.
 */

public class RoadGreeningSub extends BaseModel {

    public String stake;					// 桩号
    public Double leftZonalGreenArea;		// 左侧分带绿化面积
    public Double mediumZonalGreenArea;	// 中分带绿化面积
    public Double rightZonalGreenArea;		// 右侧分带绿化面积
    public Double sidewalkGreenArea;		// 人行道绿化面积
    public Double sideGreenArea;			// 俩侧绿化面积
    public Double slopeGreenArea;			// 边坡绿化面积
    public Double greenTotalArea;			// 绿化总面积
    public String greeningTreeSpecies;		// 主要绿化树种


    @Override
    public List<String> getContent() {
        List<String> values = new ArrayList<>();
        values.add(stake==null?"":stake);
        values.add(leftZonalGreenArea==null?"":String.valueOf(leftZonalGreenArea));
        values.add(mediumZonalGreenArea==null?"":String.valueOf(mediumZonalGreenArea));
        values.add(rightZonalGreenArea==null?"":String.valueOf(rightZonalGreenArea));
        values.add(sidewalkGreenArea==null?"":String.valueOf(sidewalkGreenArea));
        values.add(sideGreenArea==null?"":String.valueOf(sideGreenArea));
        values.add(slopeGreenArea==null?"":String.valueOf(slopeGreenArea));
        values.add(greenTotalArea==null?"":String.valueOf(greenTotalArea));
        values.add(greeningTreeSpecies==null?"":greeningTreeSpecies);
        values.add(memo==null?"":memo);
        return values;
    }

    @Override
    public List<String> getTitles() {
        List<String> values = new ArrayList<>();
        values.add("桩号");
        values.add("左侧分带绿化面积");
        values.add("中分带绿化面积");
        values.add("右侧分带绿化面积");
        values.add("人行道绿化面积");
        values.add("俩侧绿化面积");
        values.add("边坡绿化面积");
        values.add("绿化总面积");
        values.add("主要绿化树种");
        values.add("备注");
        return values;
    }

    @Override
    public List<String> getPropertyName() {
        return null;
    }
}
