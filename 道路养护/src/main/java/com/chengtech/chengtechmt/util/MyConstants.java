package com.chengtech.chengtechmt.util;

import java.util.HashMap;
import java.util.Map;

public class MyConstants {

    //    public static String PRE_URL = "http://192.1.40.44:8080/chengtechmt/";
    public static String PRE_URL = "http://14.23.99.106:8666/";
    public static String LOGIN_URL = PRE_URL + "loginUser.action?mobile=mobileRand&mobileType=0";

    public static Map<String, String> deptTree = new HashMap<>();  //部门树，根据id找到对应的部门名称。
    public static Map<String, Boolean> userRights = new HashMap<>();  //权限集合，根据模块名称来查找是否有该权限
//    public static String[] MAIN_CONTENT = {"基础数据管理", "养护业务管理",
//            "综合查询"};
    public static String[] MAIN_CONTENT = { "规范标准管理","基础数据管理", "养护业务管理", "监测及应急保障",
            "科学决策", "综合查询"};


    //待办信息
    public static String FLOW_HANDLER_URL = PRE_URL + "mt/android/flowUserHandling.action?userId=";

    //待办事项中接受的类型的字典
    public static Map<String, String> flowUserHandlerDict = new HashMap<String, String>();

    //图片与对应栏目的字典
    public static final Map<String,Integer> imageDict = new HashMap<>(); //在程序myapplication中添加


    //公告信息列表
    //public static String SYSBULLETIN_URL = "http://192.1.40.44:8080/chengtechmt/ms/sys/sysbulletin/listSysBulletinByUserJson.action?mobile=phone";
    public static String SYSBULLETIN_URL = PRE_URL + "ms/sys/sysbulletin/listSysBulletinByUserJson.action";

    //公告信息详细页
    public static String SYSBULLETIN_DETAIL_URL = PRE_URL + "ms/sys/sysbulletin/addEditSysBulletin.action?mobile=phone&id=";

    //数据基础管理-路线信息（详细）
    public static String ROUTE_DETAIL_URL = PRE_URL + "mt/dbm/road/route/addEditRoute.action?pageNo=1&isRead=true&mobile=phone&id=";

    //数据基础管理-区间路段信息（列表）
    public static String SECTION_LIST_URL = PRE_URL + "mt/dbm/road/section/listSection.action?mobile=phone&deptIds=";

    //数据基础管理-区间路段信息（详细）
    public static String SECTION_DETAIL_URL = PRE_URL + "mt/dbm/road/section/addEditSection.action?mobile=phone&pageNo=1&isRead=true&id=";


    //因为每次更改ip的常量的时候，都要重新设置给其他引用了该常量的变量。
    public static void setConstant() {
        LOGIN_URL = PRE_URL + "loginUser.action?mobile=mobileRand&mobileType=0";

        FLOW_HANDLER_URL = PRE_URL + "mt/android/flowUserHandling.action?userId=";


        SYSBULLETIN_URL = "http://192.1.40.44:8080/chengtechmt/ms/sys/sysbulletin/listSysBulletinByUserJson.action?mobile=phone";
        SYSBULLETIN_URL = PRE_URL + "ms/sys/sysbulletin/listSysBulletinByUserJson.action";

        SYSBULLETIN_DETAIL_URL = PRE_URL + "ms/sys/sysbulletin/addEditSysBulletin.action?mobile=phone&id=";
        ROUTE_DETAIL_URL = PRE_URL + "mt/dbm/road/route/addEditRoute.action?pageNo=1&isRead=true&mobile=phone&id=";
        SECTION_LIST_URL = PRE_URL + "mt/dbm/road/section/listSection.action?mobile=phone&deptIds=";
        SECTION_DETAIL_URL = PRE_URL + "mt/dbm/road/section/addEditSection.action?mobile=phone&pageNo=1&isRead=true&id=";
    }
}
