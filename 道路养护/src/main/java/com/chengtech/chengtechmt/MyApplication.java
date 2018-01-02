package com.chengtech.chengtechmt;

import android.app.Application;
import android.app.DownloadManager;
import android.content.IntentFilter;

import com.chengtech.chengtechmt.receiver.DownCompleteReceiver;
import com.chengtech.chengtechmt.util.MyConstants;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.pgyersdk.crash.PgyCrashManager;


/**
 * 作者: LiuFuYingWang on 2016/5/25 16:26.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
//        Fresco.initialize(this);
        PgyCrashManager.register(this);
        initSpeech();
        initReceiver();
        MyConstants.imageDict.put("路线信息", R.mipmap.file);
        MyConstants.imageDict.put("道路数据", R.mipmap.analysis);
        MyConstants.imageDict.put("区间路段信息", R.mipmap.file);
        MyConstants.imageDict.put("养护路段信息", R.mipmap.file);
        MyConstants.imageDict.put("桥梁卡片", R.mipmap.document_blue);
        MyConstants.imageDict.put("桥梁数据", R.mipmap.analysis);
        MyConstants.imageDict.put("隧道卡片", R.mipmap.document_blue);
        MyConstants.imageDict.put("隧道数据", R.mipmap.analysis);
        MyConstants.imageDict.put("涵洞卡片", R.mipmap.document_blue);
        MyConstants.imageDict.put("涵洞数据", R.mipmap.analysis);
        MyConstants.imageDict.put("沿线设施", R.mipmap.analysis);
        MyConstants.imageDict.put("设施量统计", R.mipmap.analysis);
        MyConstants.imageDict.put("边坡路堤挡墙", R.mipmap.file);
        MyConstants.imageDict.put("边坡数据", R.mipmap.analysis);
        MyConstants.imageDict.put("交安设施", R.mipmap.file);
        MyConstants.imageDict.put("服务交安设施", R.mipmap.analysis);
        MyConstants.imageDict.put("绿化信息", R.mipmap.file);
        MyConstants.imageDict.put("绿化数据", R.mipmap.analysis);
        MyConstants.imageDict.put("交通量观测站", R.mipmap.file);
        MyConstants.imageDict.put("交通量观测点数据", R.mipmap.analysis);
        MyConstants.imageDict.put("规范标准管理", R.mipmap.tools);
        MyConstants.imageDict.put("基础数据管理", R.mipmap.main_img2_b);
        MyConstants.imageDict.put("养护业务管理", R.mipmap.blockdevice);
        MyConstants.imageDict.put("监测及应急保障", R.mipmap.task_management);
        MyConstants.imageDict.put("科学决策", R.mipmap.main_img5_b);
        MyConstants.imageDict.put("综合查询", R.mipmap.interquery);
        MyConstants.imageDict.put("病害字典", R.mipmap.dictionary);
        MyConstants.imageDict.put("静态数据管理", R.mipmap.database);
        MyConstants.imageDict.put("动态数据管理", R.mipmap.datebase2);
        MyConstants.imageDict.put("评定病害类型", R.mipmap.disease);
        MyConstants.imageDict.put("病害维修方案", R.mipmap.disease);
        MyConstants.imageDict.put("评定病害位置", R.mipmap.disease);
        MyConstants.imageDict.put("大中修、改造（善）及省部补助项目", R.mipmap.folder);
        MyConstants.imageDict.put("小额专项维修", R.mipmap.increase);
        MyConstants.imageDict.put("边坡在线监测", R.mipmap.increase);
        MyConstants.imageDict.put("月度检查", R.mipmap.check);
        MyConstants.imageDict.put("保养作业", R.mipmap.books_three);
        MyConstants.imageDict.put("小修作业", R.mipmap.busines_info);
        MyConstants.imageDict.put("基础数据查询", R.mipmap.dictionary);
        MyConstants.imageDict.put("业务数据查询", R.mipmap.search_book);
        MyConstants.imageDict.put("GIS可视化查询", R.mipmap.map);
        MyConstants.imageDict.put("公路技术状况评定标准", R.mipmap.file2);
        MyConstants.imageDict.put("公路桥梁技术状况评定标准", R.mipmap.file2);
        MyConstants.imageDict.put("公路桥涵养护规范", R.mipmap.file2);
        MyConstants.imageDict.put("公路隧道养护技术规范", R.mipmap.file2);
        MyConstants.imageDict.put("技术状况评定标准", R.mipmap.standard);
        MyConstants.imageDict.put("公路技术状况评定", R.mipmap.file3);
        MyConstants.imageDict.put("沥青路面状况", R.mipmap.file3);
        MyConstants.imageDict.put("水泥路面状况", R.mipmap.file3);
        MyConstants.imageDict.put("评定明细", R.mipmap.file4);
        MyConstants.imageDict.put("评定汇总", R.mipmap.order_history);
        MyConstants.imageDict.put("桥隧涵养护", R.mipmap.order_history);
        MyConstants.imageDict.put("病害登记", R.mipmap.file4);
        MyConstants.imageDict.put("公路气象监测", R.mipmap.file4);
    }

    private void initSpeech() {
        SpeechUtility.createUtility(this, SpeechConstant.APPID + "= 595dd926");
    }

    private void initReceiver() {
    }
}
