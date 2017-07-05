package com.chengtech.chengtechmt.entity.patrol;

import com.chengtech.chengtechmt.entity.BaseModel;

import java.util.ArrayList;
import java.util.List;


/**
 * 隧道经常检查记录
 *
 * @author liutao 2014-11-13 14:30
 */
@SuppressWarnings("serial")
public class TunnelRecord extends BaseModel {

//    public String checkContents;                 //检查内容
//
//    public String stateDiscription;             //状态描述
//
//    public String judgeResult;                     //评定结果

    public String tunnelOftenInspectId; 	// 主表id
    public String projectName = "";                    //项目名称
    public String mileageMark ="";                    //里程桩号
    public String checkContents =""; 			// 检查内容
    public String stateDiscription =""; 		// 状态描述
    public String judgeResult =""; 			// 评定结果



    @Override
    public List<String> getContent() {
        List<String> values = new ArrayList<>();
        values.add(mileageMark == null ? "" : mileageMark);
        values.add(projectName == null ? "" : projectName);
        values.add(checkContents == null ? "" : (checkContents.equals("null")?"":checkContents));
        values.add(stateDiscription == null ? "" : (stateDiscription.equals("null")?"":stateDiscription));
        values.add(judgeResult == null ? "" : (judgeResult.equals("null")?"":judgeResult));
        return values;
    }

    @Override
    public List<String> getTitles() {
        List<String> values = new ArrayList<>();
        values.add("里程桩号");
        values.add("项目名称");
        values.add("检查内容");
        values.add("状态描述");
        values.add("评定结果");
        return values;
    }

    @Override
    public List<String> getPropertyName() {
        return null;
    }
}
