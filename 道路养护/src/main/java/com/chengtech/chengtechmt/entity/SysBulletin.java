package com.chengtech.chengtechmt.entity;

import java.io.Serializable;

public class SysBulletin implements Serializable{
	
	public String id;
	public String subject; // 主题
	public String category;// 类别
	public String content;// 内容
	public String pubdate;// 创建日期
	public String releasedate;// 发布日期
	public String viewscope;// 是否全体阅读
	public String viewrole;// 可查看角色
	public String viewdept;// 可查看部门
	public String userClick;// 点击次数
	public String releasestate;// 发布状态
	public User author;
	public Dept dept;
	public String authorId;// 作者ID
	public String deptId;// 部门ID
	public String reader;// 读者
	public String urgency;//紧急程度
	public String sessionName;

}
