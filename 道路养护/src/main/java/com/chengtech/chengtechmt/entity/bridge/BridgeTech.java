package com.chengtech.chengtechmt.entity.bridge;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BridgeTech implements Serializable {

	public String  bridgeTechId;
	public String 	checkYear;          //检查年月	
	public String	checkType;          //检查类型		
	public String	rateGrade;          //评定等级		
	public String	abumentGrade;       //桥台与基础评定等级
	public String	pierFoundation;     //桥墩与基础	
	public String	foundationScource;  //地基冲刷		
	public String	upperStructure;     //上部结构		
	public String	support	;           //支座		
	public String	maintenanceRepair;  //经常保养小修	
	public String	treatment;          //处治对策		
	public String 	nextCheckyear;      //下次检查年份

	public List<String> propetryValues;
	public List<String> getList(){
		if (propetryValues==null) {
			propetryValues = new ArrayList<>();

		propetryValues.add(checkYear==null?"":checkYear);
		propetryValues.add(checkType==null?"":checkType);
		propetryValues.add(rateGrade==null?"":rateGrade);
		propetryValues.add(abumentGrade==null?"":abumentGrade);
		propetryValues.add(pierFoundation==null?"":pierFoundation);
		propetryValues.add(foundationScource==null?"":foundationScource);
		propetryValues.add(upperStructure==null?"":upperStructure);
		propetryValues.add(support==null?"":support);
		propetryValues.add(maintenanceRepair==null?"":maintenanceRepair);
		propetryValues.add(treatment==null?"":treatment);
		propetryValues.add(nextCheckyear==null?"":nextCheckyear);
		}
		return propetryValues;
	}
}
