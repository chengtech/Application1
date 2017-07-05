package com.chengtech.chengtechmt.entity;

import java.io.Serializable;
import java.util.Map;

public class UserRight implements Serializable{

	public String id;
	public boolean hasRight;			//对应的模块是否有权限
	public String itemName;				//模块的名称
}
