package com.chengtech.chengtechmt.entity;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.chengtech.chengtechmt.entity.gson.RouteG;
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
 * 作者: LiuFuYingWang on 2016/5/16 14:25.
 */
public class Section extends BaseModel {

    public WorkSectionPresenter presenter;
    public List<String> propetryValues;
    public int totalRows;
    public String url = MyConstants.PRE_URL + "mt/dbm/road/section/listSectionJson.action";
    public String techGrade;         //技术等级
    public String startStake;        //里程起点
    public String endStake;          //里程终点
    public Dept mgDept;            //管理单位
    public Dept mtDept;            //养护单位
    public String length;            //长度
    public String driveType;         //车道类型
    public String discountedMileage; //折算里程
    public String rampMileage;      //匝道连接线里程
    public String roadWide;          //路面均宽
    public String regionCode;        //政区编码
    public String roadType;          //路面类型
    public Route route;

    @Override
    public List<String> getContent() {
        if (propetryValues == null) {
            propetryValues = new ArrayList<>();
            propetryValues.add(TextUtils.isEmpty(code) ? "" : code);
            propetryValues.add(TextUtils.isEmpty(name) ? "" : name);
            propetryValues.add(TextUtils.isEmpty(techGrade) ? "" : techGrade);
            propetryValues.add(TextUtils.isEmpty(startStake) ? "" : startStake);
            propetryValues.add(TextUtils.isEmpty(endStake) ? "" : endStake);
            propetryValues.add(TextUtils.isEmpty(mgDept.name) ? "" : mgDept.name);
            propetryValues.add(TextUtils.isEmpty(mtDept.name) ? "" : mtDept.name);
            propetryValues.add(TextUtils.isEmpty(length) ? "" : length);
            propetryValues.add(TextUtils.isEmpty(driveType) ? "" : driveType);
            propetryValues.add(TextUtils.isEmpty(discountedMileage) ? "" : discountedMileage);
            propetryValues.add(TextUtils.isEmpty(rampMileage) ? "" : rampMileage);
            propetryValues.add(TextUtils.isEmpty(roadWide) ? "" : roadWide);
            propetryValues.add(TextUtils.isEmpty(regionCode) ? "" : regionCode);
            propetryValues.add(TextUtils.isEmpty(roadType) ? "" : roadType);
        }
        return propetryValues;
    }

    @Override
    public List<String> getTitles() {
        List<String> titles = new ArrayList<>();
        titles = new ArrayList<>();
        titles.add("路线编号");
        titles.add("路线名称");
        titles.add("技术等级");
        titles.add("里程起点");
        titles.add("里程终点");
        titles.add("管理单位");
        titles.add("养护单位");
        titles.add("长度");
        titles.add("车道类型");
        titles.add("折算里程");
        titles.add("匝道连接线里程");
        titles.add("路面均宽");
        titles.add("政区编码");
        titles.add("路面类型");
        return titles;
    }

    @Override
    public List<String> getPropertyName() {
        List<String> values = new ArrayList<>();
        values.add("code");
        values.add("name");
        values.add("techGrade");
        values.add("startStake");
        values.add("endStake");
        values.add("mgDept");
        values.add("mtDept");
        values.add("length");
        values.add("driveType");
        values.add("discountedMileage");
        values.add("rampMileage");
        values.add("roadWide");
        values.add("regionCode");
        values.add("roadType");
        return values;
    }

    public Section(WorkSectionPresenter presenter) {
        this.presenter = presenter;
    }

    public void getData(Context context, int pageNo, int pageSize, final String type, String arg) {
        AsyncHttpClient client = HttpclientUtil.getInstance(context);

        AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onSuccess(int i, cz.msebera.android.httpclient.Header[] headers, byte[] bytes) {
                try {
                    String data = new String(bytes, "utf-8");
                    Gson gson = new Gson();
                    data = data.replace("pager.", "");
                    SectionG sectionG = gson.fromJson(data, SectionG.class);
                    totalRows = sectionG.totalRows;
                    presenter.loadDataSuccess(sectionG.rows, type);
                } catch (Exception e) {
                    presenter.hasError();
                }
            }

            @Override
            public void onFailure(int i, cz.msebera.android.httpclient.Header[] headers, byte[] bytes, Throwable throwable) {
                presenter.loadDataFailed();
            }

        };
        client.get(url + "?pager.pageNo=" + pageNo + "&pager.pageSize=" + pageSize + "&sort=mgDeptId,mtDeptId,sortOrderCode" +
                        "&direction=asc" + "&deptIds=" + arg,
                responseHandler);

    }
}
