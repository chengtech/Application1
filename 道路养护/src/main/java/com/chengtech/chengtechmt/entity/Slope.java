package com.chengtech.chengtechmt.entity;

import android.content.Context;

import com.chengtech.chengtechmt.entity.gson.SectionG;
import com.chengtech.chengtechmt.presenter.WorkSectionPresenter;
import com.chengtech.chengtechmt.util.HttpclientUtil;
import com.chengtech.chengtechmt.util.MyConstants;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者: LiuFuYingWang on 2016/6/29 11:47.
 * 边坡挡墙实体类
 */
public class Slope extends BaseModel {

    public WorkSectionPresenter presenter;
    public List<String> propetryValues;
    public int totalRows;
    public String url = MyConstants.PRE_URL+"mt/dbm/road/slope/listSlopeJson.action";

    public String longitude; //经度
    public String latitude;  //纬度
    public String startStake; 		// 起点桩号
    public String endStake; 		// 终点桩号
    public String direction; 		// 方向
    public String protectionType; 	// 护面形式
    public String structureForm; 	// 结构形式
    public String completionDate; 	// 竣工日期
    public String designUnit; 		// 设计单位
    public String constructionUnit;// 施工单位
    public String antiGrade; 		// 抗震等级
    public String geologyCase; 	// 地质情况
    public String height; 			// 边坡高度
    public String heights; 		// 挡土墙高度
    public String position; 		// 边坡位置
    public String positions; 		// 挡土墙位置
    public String averagePitch; 	// 平均坡度
    public String num; 			// 数目(陂级)
    public String entrenchRate; 	// 防护坡率(陂级)
    public String bigOrSmall;		// 大小
    public String spacing; 		// 间距
    public String sideView; 		// 边坡正面照
    public String wallView; 		// 挡土墙正面照
    public String inspectionRecord;// 检查记录
    public String detectionRecord; // 监测记录
    public String serviceRecord; 	// 维修加固记录
    public String checkPoint; 		// 检测点
    public String serviceChannel; 	// 检修通道
    public String routeCode;

    @Override
    public List<String> getContent() {
        if (propetryValues==null) {
            propetryValues = new ArrayList<>();
            propetryValues.add(startStake==null?"":startStake);
            propetryValues.add(endStake==null?"":endStake);
            propetryValues.add(direction==null?"":direction);
            propetryValues.add(protectionType==null?"":protectionType);
            propetryValues.add(structureForm==null?"":structureForm);
            propetryValues.add(completionDate==null?"":completionDate);
            propetryValues.add(designUnit==null?"":designUnit);
            propetryValues.add(constructionUnit==null?"":constructionUnit);
            propetryValues.add(antiGrade==null?"":antiGrade);
            propetryValues.add(geologyCase==null?"":geologyCase);
            propetryValues.add(height==null?"":height);
            propetryValues.add(heights==null?"":heights);
            propetryValues.add(position==null?"":position);
            propetryValues.add(positions==null?"":positions);
            propetryValues.add(averagePitch==null?"":averagePitch);
            propetryValues.add(num==null?"":num);
            propetryValues.add(entrenchRate==null?"":entrenchRate);
            propetryValues.add(bigOrSmall==null?"":bigOrSmall);
            propetryValues.add(spacing==null?"":spacing);
            propetryValues.add(sideView==null?"":sideView);
            propetryValues.add(wallView==null?"":wallView);
            propetryValues.add(inspectionRecord==null?"":inspectionRecord);
            propetryValues.add(detectionRecord==null?"":detectionRecord);
            propetryValues.add(serviceRecord==null?"":serviceRecord);
            propetryValues.add(checkPoint==null?"":checkPoint);
            propetryValues.add(serviceChannel==null?"":serviceChannel);
        }
        return propetryValues;
    }

    @Override
    public List<String> getTitles() {
        List<String> values = new ArrayList<>();
        values.add("起点桩号");
        values.add("终点桩号");
        values.add("方向");
        values.add("边坡-护面形式");
        values.add("挡土墙-结构形式");
        values.add("竣工日期 ");
        values.add("设计单位 ");
        values.add("施工单位 ");
        values.add("抗震等级 ");
        values.add("地质情况");
        values.add("边坡高度");
        values.add("挡土墙高度");
        values.add("边坡位置");
        values.add("挡土墙位置");
        values.add("平均坡度 ");
        values.add("陂级-数目 ");
        values.add("陂级-防护坡率");
        values.add("泄水孔-大小(毫米)");
        values.add("泄水孔-间距(米)");
        values.add("边坡正面照 ");
        values.add("挡土墙正面照 ");
        values.add("检查记录");
        values.add("监测记录");
        values.add("维修加固记录 ");
        values.add("检测点 ");
        values.add("检修通道");
        return values;
    }

    @Override
    public List<String> getPropertyName() {
        List<String> values = new ArrayList<>();
        return values;
    }


    public void getData(Context context, int pageNo, int pageSize, final String type, String arg) {
        AsyncHttpClient client = HttpclientUtil.getInstance(context);

        AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {

                try {
                    String data = new String(arg2, "utf-8");
                    Gson gson  = new Gson();
                    data = data.replace("pager.","");
                    SlopeG slopeG = gson.fromJson(data, SlopeG.class);
                    totalRows = slopeG.totalRows;
                    presenter.loadDataSuccess(slopeG.rows,type);
//
                } catch (Exception e) {
                    presenter.loadDataFailed();
                    e.printStackTrace();
                }
                super.onSuccess(arg0, arg1, arg2);
            }

            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2,
                                  Throwable arg3) {
                presenter.loadDataFailed();
                super.onFailure(arg0, arg1, arg2, arg3);
            }
        };
        client.get(url+"?pager.pageNo="+pageNo+"&pager.pageSize="+pageSize+"&sort=routeGrade,code" +
                        "&direction=asc"+"&deptIds="+arg,
                responseHandler);

    }

    public Slope(WorkSectionPresenter presenter) {
        this.presenter = presenter;
    }

    public class SlopeG {
        public String pageNo ;
        public int totalRows;
        public List<Slope> rows;
    }
}
