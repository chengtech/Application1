package com.chengtech.chengtechmt.entity.gson;

import java.util.List;

import com.chengtech.chengtechmt.entity.User;
import com.chengtech.chengtechmt.entity.UserRight;


public class LoginInfoG {
	public boolean success;
	public String id;			//账户id
	public String name;			//账户名
	public List<UserRight> data ;  //用户所对应的登陆模块
	public User userInfo;
	public String msg;   
}
