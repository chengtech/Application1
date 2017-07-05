package com.chengtech.chengtechmt.entity;

import android.content.Context;
import android.text.TextUtils;

import com.chengtech.chengtechmt.entity.gson.SafetyFacilitiesG;
import com.chengtech.chengtechmt.entity.gson.WorkSectionG;
import com.chengtech.chengtechmt.presenter.Presenter;
import com.chengtech.chengtechmt.presenter.WorkSectionPresenter;
import com.chengtech.chengtechmt.util.HttpclientUtil;
import com.chengtech.chengtechmt.util.MyConstants;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者: LiuFuYingWang on 2016/5/18 11:04.
 * 交安设施
 */
public class SafetyFacilities extends BaseModel {

    public List<String> propertyValue;
    public Presenter presenter;
    public String url = MyConstants.PRE_URL+"mt/dbm/road/safetyfacilities/listSafetyFacilitiesJson.action";
    public int totalRows;
    private String sort = "sortOrderCode,direction,mileageStake";

    public String routeCode;                  //路线编码、
    public String mileageStake;				//里程桩号
    public String startStake;             		//起点桩号
    public String endStake; 			        //终点桩号
    public String facilitiesLocation;			//设施位置
    public String facilitiesSpecification ;	    //设施规格
    public String facilitiesUse;				//设施用途
    public String buildDate;					//建立时间
    public String direction;				    //方向
    public String  lifePeriod;					//使用年限
    public String picture;						//照片
    public String longitude   ;          //经度
    public String latitude ;			  //纬度

    public SafetyFacilities(Presenter presenter) {
        this.presenter = presenter;
    }


    @Override
    public List<String> getContent() {
        if (propertyValue==null) {
            propertyValue = new ArrayList<>();
            propertyValue.add(TextUtils.isEmpty(routeCode)?"":routeCode);
            propertyValue.add(TextUtils.isEmpty(name)?"":name);
            propertyValue.add(TextUtils.isEmpty(mileageStake)?"":mileageStake);
            propertyValue.add(TextUtils.isEmpty(startStake)?"":startStake);
            propertyValue.add(TextUtils.isEmpty(endStake)?"":endStake);
            propertyValue.add(TextUtils.isEmpty(facilitiesLocation)?"":facilitiesLocation);
            propertyValue.add(TextUtils.isEmpty(facilitiesSpecification)?"":facilitiesSpecification);
            propertyValue.add(TextUtils.isEmpty(facilitiesUse)?"":facilitiesUse);
            propertyValue.add(TextUtils.isEmpty(buildDate)?"":buildDate);
            propertyValue.add(TextUtils.isEmpty(direction)?"":direction);
            propertyValue.add(TextUtils.isEmpty(lifePeriod)?"":lifePeriod);
            propertyValue.add(TextUtils.isEmpty(longitude)?"":longitude);
            propertyValue.add(TextUtils.isEmpty(latitude)?"":latitude);

        }
        return propertyValue;
    }

    @Override
    public List<String> getTitles() {
        List<String> values = new ArrayList<>();
        values.add("路线编码");
        values.add("设施名称");
        values.add("里程桩号");
        values.add("起点桩号");
        values.add("终点桩号");
        values.add("设施位置");
        values.add("设施规格");
        values.add("设施用途");
        values.add("建立时间");
        values.add("方向");
        values.add("使用年限");
        values.add("经度");
        values.add("纬度");
        return values;
    }

    @Override
    public List<String> getPropertyName() {
        List<String> values = new ArrayList<>();
        values.add("routeCode");
        values.add("name");
        values.add("mileageStake");
        values.add("startStake");
        values.add("endStake");
        values.add("facilitiesLocation");
        values.add("facilitiesSpecification");
        values.add("facilitiesUse");
        values.add("buildDate");
        values.add("direction");
        values.add("lifePeriod");
        values.add("longitude");
        values.add("latitude");
        return values;
    }

    public void getData(Context context,int pageNo,int pageSize, final String type,String arg) {
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
                    data = data.replace("pager.", "");
                    SafetyFacilitiesG safetyFacilitiesG = gson.fromJson(data, SafetyFacilitiesG.class);
                    totalRows = safetyFacilitiesG.totalRows;
                    presenter.loadDataSuccess(safetyFacilitiesG.rows,type);
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
        client.get(url+"?pager.pageNo="+pageNo+"&pager.pageSize="+pageSize+"&sort="+sort +
                        "&direction=asc"+"&deptIds="+arg,
                responseHandler);
    }
}
