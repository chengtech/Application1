package com.chengtech.chengtechmt.entity;

import java.io.Serializable;

public class FlowUserHandleInfo implements Serializable {
	public String id;
	public String operatorType;
	public String className;
	public String businessId;
	public String name;
	public String actionUrl;
	@Override
	public String toString() {
		return "FlowUserHandleInfo [id=" + id + ", operatorType="
				+ operatorType + ", className=" + className + ", businessId="
				+ businessId + ", name=" + name + ", actionUrl=" + actionUrl
				+ "]";
	}
	
}
