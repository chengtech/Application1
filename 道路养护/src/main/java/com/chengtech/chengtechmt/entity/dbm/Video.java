package com.chengtech.chengtechmt.entity.dbm;

import com.chengtech.chengtechmt.entity.BaseModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者: LiuFuYingWang on 2016/11/21 16:20.
 * 视频设备实体类
 */

public class Video extends BaseModel {

    public ArrayList<String> detailContent;
    public ArrayList<String> detailTitles;
    public ArrayList<String> detailFieldNames;

    public String num;                        //编号
    public String monitorLocation;            //监控地点
    public String frontEquipmentIp;        //前端设备Ip
    public String frontNationalId;        //前端国际ID
    public String channel;                    //通道号
    public String installLocation;        //安装地点
    public String dept;                    //所属单位
    public String onProject;                //所属项目
    public String unitConstruction;        //承建单位
    public String ownerConstruction;        //业主单位
    public String maintenancePeople;        //维护人
    public String maintenanceMoblie;        //维护电话
    public String installWay;                //安装方式
    public String systemCategory;            //系统类别
    public String infrared;                //红外
    public String stars;                    //星光
    public String wideDynamic;                //宽动态
    public String net;                        //网络
    public String equipmentType;            //设备类型
    public String equipmentManufacturer;    //设备厂家
    public String frontEquipmentModel;        //前端设备型号
    public String saveFrontNationalId;        //对应储蓄国标Id号
    public String ip;                        //对应储蓄IP
    public String account;                    //对应存储登录用户名
    public String password;                //对应存储登录密码
    public String saveName;                //存储名称
    public String saveDate;                //存储时间
    public String codeFormat;                //编码格式
    public String fillLight;                //补光
    public String saveSystemMessage;        //存储系统通道信息
    public String installTime;                //安装时间
    public String resolution;                //分辨率
    private String observatoryName;                //所属观测站Name
    private String videoUrl;                                //视频url

    public String longitude; //经度
    public String latitude;  //纬度

    @Override
    public ArrayList<String> getContent() {
        if (detailContent == null)
            detailContent = new ArrayList<>();

        detailContent.add(num == null ? "" : num);                        //编号
        detailContent.add(name == null ? "" : name);                        //监控点名称
        detailContent.add(monitorLocation == null ? "" : monitorLocation);            //监控地点
        detailContent.add(frontEquipmentIp == null ? "" : frontEquipmentIp);        //前端设备Ip
        detailContent.add(frontNationalId == null ? "" : frontNationalId);        //前端国际ID
        detailContent.add(channel == null ? "" : channel);                    //通道号
        detailContent.add(installLocation == null ? "" : installLocation);        //安装地点
        detailContent.add(dept == null ? "" : dept);                    //所属单位
        detailContent.add(onProject == null ? "" : onProject);                //所属项目
        detailContent.add(unitConstruction == null ? "" : unitConstruction);        //承建单位
        detailContent.add(ownerConstruction == null ? "" : ownerConstruction);        //业主单位
        detailContent.add(maintenancePeople == null ? "" : maintenancePeople);        //维护人
        detailContent.add(maintenanceMoblie == null ? "" : maintenanceMoblie);        //维护电话
        detailContent.add(installWay == null ? "" : installWay);                //安装方式
        detailContent.add(systemCategory == null ? "" : systemCategory);            //系统类别
        detailContent.add(infrared == null ? "" : infrared);                //红外
        detailContent.add(stars == null ? "" : stars);                    //星光
        detailContent.add(wideDynamic == null ? "" : wideDynamic);                //宽动态
        detailContent.add(net == null ? "" : net);                        //网络
        detailContent.add(equipmentType == null ? "" : equipmentType);            //设备类型
        detailContent.add(equipmentManufacturer == null ? "" : equipmentManufacturer);    //设备厂家
        detailContent.add(frontEquipmentModel == null ? "" : frontEquipmentModel);        //前端设备型号
        detailContent.add(saveFrontNationalId == null ? "" : saveFrontNationalId);        //对应储蓄国标Id号
        detailContent.add(ip == null ? "" : ip);                        //对应储蓄IP
        detailContent.add(account == null ? "" : account);                    //对应存储登录用户名
        detailContent.add(password == null ? "" : password);                //对应存储登录密码
        detailContent.add(saveName == null ? "" : saveName);                //存储名称
        detailContent.add(saveDate == null ? "" : saveDate);                //存储时间
        detailContent.add(codeFormat == null ? "" : codeFormat);                //编码格式
        detailContent.add(fillLight == null ? "" : fillLight);                //补光
        detailContent.add(saveSystemMessage == null ? "" : saveSystemMessage);        //存储系统通道信息
        detailContent.add(installTime == null ? "" : installTime);                //安装时间
        detailContent.add(resolution == null ? "" : resolution);                //分辨率
        detailContent.add(observatoryName == null ? "" : observatoryName);                //所属观测站Name
        detailContent.add(videoUrl == null ? "" : videoUrl);                                //视频url

        return detailContent;
    }

    public ArrayList<String> getTitles() {
        if (detailTitles == null)
            detailTitles = new ArrayList<>();


        detailTitles.add("编号");
        detailTitles.add("监控点名称");
        detailTitles.add("监控地点");
        detailTitles.add("前端设备Ip");
        detailTitles.add("前端国际ID");
        detailTitles.add("通道号");
        detailTitles.add("安装地点");
        detailTitles.add("所属单位");
        detailTitles.add("所属项目");
        detailTitles.add("承建单位");
        detailTitles.add("业主单位");
        detailTitles.add("维护人");
        detailTitles.add("维护电话");
        detailTitles.add("安装方式");
        detailTitles.add("系统类别");
        detailTitles.add("红外");
        detailTitles.add("星光");
        detailTitles.add("宽动态");
        detailTitles.add("网络");
        detailTitles.add("设备类型");
        detailTitles.add("设备厂家");
        detailTitles.add("前端设备型号");
        detailTitles.add("对应储蓄国标Id号");
        detailTitles.add("对应储蓄IP");
        detailTitles.add("对应存储登录用户名");
        detailTitles.add("对应存储登录密码");
        detailTitles.add("存储名称");
        detailTitles.add("存储时间");
        detailTitles.add("编码格式");
        detailTitles.add("补光");
        detailTitles.add("存储系统通道信息");
        detailTitles.add("安装时间");
        detailTitles.add("分辨率");
        detailTitles.add("所属观测站");
        detailTitles.add("视频url");

        return detailTitles;
    }

    @Override
    public List<String> getPropertyName() {
        return null;
    }

    public ArrayList<String> getFieldNames() {
        if (detailFieldNames == null)
            detailFieldNames = new ArrayList<>();


        detailFieldNames.add("num");                        //编号                  
        detailFieldNames.add("name");                        //编号
        detailFieldNames.add("monitorLocation");            //监控地点
        detailFieldNames.add("frontEquipmentIp");        //前端设备Ip                 
        detailFieldNames.add("frontNationalId");        //前端国际ID                  
        detailFieldNames.add("channel");                    //通道号                 
        detailFieldNames.add("installLocation");        //安装地点                    
        detailFieldNames.add("dept");                    //所属单位                   
        detailFieldNames.add("onProject");                //所属项目                  
        detailFieldNames.add("unitConstruction");        //承建单位                   
        detailFieldNames.add("ownerConstruction");        //业主单位                  
        detailFieldNames.add("maintenancePeople");        //维护人                   
        detailFieldNames.add("maintenanceMoblie");        //维护电话                  
        detailFieldNames.add("installWay");                //安装方式                 
        detailFieldNames.add("systemCategory");            //系统类别                 
        detailFieldNames.add("infrared");                //红外                     
        detailFieldNames.add("stars");                    //星光                    
        detailFieldNames.add("wideDynamic");                //宽动态                 
        detailFieldNames.add("net");                        //网络                  
        detailFieldNames.add("equipmentType");            //设备类型                  
        detailFieldNames.add("equipmentManufacturer");    //设备厂家                  
        detailFieldNames.add("frontEquipmentModel");        //前端设备型号              
        detailFieldNames.add("saveFrontNationalId");        //对应储蓄国标Id号           
        detailFieldNames.add("ip");                        //对应储蓄IP               
        detailFieldNames.add("account");                    //对应存储登录用户名           
        detailFieldNames.add("password");                //对应存储登录密码               
        detailFieldNames.add("saveName");                //存储名称                   
        detailFieldNames.add("saveDate");                //存储时间                   
        detailFieldNames.add("codeFormat");                //编码格式                 
        detailFieldNames.add("fillLight");                //补光                    
        detailFieldNames.add("saveSystemMessage");        //存储系统通道信息              
        detailFieldNames.add("installTime");                //安装时间                
        detailFieldNames.add("resolution");                //分辨率                  
        detailFieldNames.add("observatoryName");                //所属观测站Name       
        detailFieldNames.add("videoUrl");                                //视频url  
        return detailFieldNames;
    }

    public class VideoG{

        public ArrayList<Video> rows;
    }
}
