package com.chengtech.chengtechmt.entity;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.chengtech.chengtechmt.entity.gson.SectionG;
import com.chengtech.chengtechmt.entity.gson.WorkSectionG;
import com.chengtech.chengtechmt.presenter.Presenter;
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
 * 作者: LiuFuYingWang on 2016/5/17 15:42.
 */
public class WorkSection extends BaseModel {

    private String sort = "mgDeptId,mtDeptId,sortOrderCode,startStake";
    public List<String> propertyValue;
    public WorkSectionPresenter presenter;
    public String url = MyConstants.PRE_URL + "mt/dbm/road/worksection/listWorkSectionJson.action";
    public int totalRows;
    public String techGrade;            // 技术等级
    public String startStake;            // 里程起点
    public String endStake;            // 里程终点
    public Dept mgDept;                // 管理单位
    public Dept mtDept;                // 养护单位
    public String length;                // 长度
    public String driveType;            // 车道类型
    public String discountedMileage;    // 折算里程
    public String rampMileage;        // 匝道连接线里程
    public String roadWide;            // 路面均宽
    public String regionCode;            // 政区编码
    public String roadType;            // 路面类型
    public String direction;            // 方向
    public String ahighSpeed;            // 是否一幅高速
    public String designSpeed;        // 设计时速
    public String constructYear;        // 修建年度
    public String reconstructYear;    // 改建年度
    public String lastMajorRepairYear; // 最近一次大中修年度
    public String landformType;        // 地貌类型
    public String subgradeLength;      // 路基长度
    public String startStakeName;    // 起点名称
    public String endStakeName;    // 终点名称

    @Override
    public List<String> getContent() {
        if (propertyValue == null) {
            propertyValue = new ArrayList<>();
            propertyValue.add(TextUtils.isEmpty(code) ? "" : code);
            propertyValue.add(TextUtils.isEmpty(name) ? "" : name);
            propertyValue.add(TextUtils.isEmpty(techGrade) ? "" : techGrade);
            propertyValue.add(TextUtils.isEmpty(startStake) ? "" : startStake);
            propertyValue.add(TextUtils.isEmpty(endStake) ? "" : endStake);
            propertyValue.add(TextUtils.isEmpty(mgDept.name) ? "" : mgDept.name);
            propertyValue.add(TextUtils.isEmpty(mtDept.name) ? "" : mtDept.name);
            propertyValue.add(TextUtils.isEmpty(length) ? "" : length);
            propertyValue.add(TextUtils.isEmpty(driveType) ? "" : driveType);
            propertyValue.add(TextUtils.isEmpty(discountedMileage) ? "" : discountedMileage);
            propertyValue.add(TextUtils.isEmpty(rampMileage) ? "" : rampMileage);
            propertyValue.add(TextUtils.isEmpty(roadWide) ? "" : roadWide);
            propertyValue.add(TextUtils.isEmpty(regionCode) ? "" : regionCode);
            propertyValue.add(TextUtils.isEmpty(roadType) ? "" : roadType);
            propertyValue.add(TextUtils.isEmpty(direction) ? "" : direction);
            propertyValue.add(TextUtils.isEmpty(ahighSpeed) ? "" : ahighSpeed);
            propertyValue.add(TextUtils.isEmpty(designSpeed) ? "" : designSpeed);
            propertyValue.add(TextUtils.isEmpty(constructYear) ? "" : constructYear);
            propertyValue.add(TextUtils.isEmpty(reconstructYear) ? "" : reconstructYear);
            propertyValue.add(TextUtils.isEmpty(lastMajorRepairYear) ? "" : lastMajorRepairYear);
            propertyValue.add(TextUtils.isEmpty(landformType) ? "" : landformType);
            propertyValue.add(TextUtils.isEmpty(subgradeLength) ? "" : subgradeLength);
        }
        return propertyValue;
    }

    @Override
    public List<String> getTitles() {
        List<String> titles = new ArrayList<>();
        titles.add("养护路段编号");
        titles.add("养护路段名称");
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
        titles.add("方向");
        titles.add("是否一幅高速");
        titles.add("设计时速");
        titles.add("修建年度");
        titles.add("改建年度");
        titles.add("最近一次大中修年度");
        titles.add("地貌类型");
        titles.add("路基长度");
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
        values.add("mgDept.name");
        values.add("mtDept.name");
        values.add("length");
        values.add("driveType");
        values.add("discountedMileage");
        values.add("rampMileage");
        values.add("roadWide");
        values.add("regionCode");
        values.add("roadType");
        values.add("direction");
        values.add("ahighSpeed");
        values.add("designSpeed");
        values.add("constructYear");
        values.add("reconstructYear");
        values.add("lastMajorRepairYear");
        values.add("landformType");
        values.add("subgradeLength");
        return values;
    }

    public WorkSection(WorkSectionPresenter presenter) {
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
                    WorkSectionG workSectionG = gson.fromJson(data, WorkSectionG.class);
                    totalRows = workSectionG.totalRows;
                    presenter.loadDataSuccess(workSectionG.rows, type);
                } catch (Exception e) {
                    presenter.hasError();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int i, cz.msebera.android.httpclient.Header[] headers, byte[] bytes, Throwable throwable) {
                presenter.loadDataFailed();
            }

        };
        client.get(url + "?pager.pageNo=" + pageNo + "&pager.pageSize=" + pageSize + "&sort=" + sort +
                        "&direction=asc" + "&deptIds=" + arg,
                responseHandler);
    }
}
