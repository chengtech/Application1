package com.chengtech.chengtechmt.util;

import java.util.List;

import com.chengtech.chengtechmt.entity.UserRight;

public class AppAccount {
	public static String name;  //账户名
	public static String userId;	//账户id
	public static int mScreenWidth;  //屏幕宽度
	public static int mScreenHeight; //屏幕高度
	public static boolean isFirst  = true;   //开始启动程序
	public static List<UserRight> userRights; //用户的权限信息
}
