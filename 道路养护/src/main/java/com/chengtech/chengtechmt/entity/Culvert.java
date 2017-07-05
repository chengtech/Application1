package com.chengtech.chengtechmt.entity;

import android.content.Context;
import android.text.TextUtils;

import com.chengtech.chengtechmt.presenter.WorkSectionPresenter;
import com.chengtech.chengtechmt.util.HttpclientUtil;
import com.chengtech.chengtechmt.util.MyConstants;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * 涵洞实体类
 */
public class Culvert extends BaseModel implements Serializable {

    public WorkSectionPresenter presenter;
    public List<String> propetryValues;
    public int totalRows;
    public String url = MyConstants.PRE_URL + "mt/dbm/road/culvert/listCulvertJson.action";

    public String routeCode;            //路线编码
    public Dept belongDept;            //所属单位
    public String holeNumber;            // 孔数
    public String culvertBottomScope;  // 涵底坡度
    public String entranceWidth;        // 进口宽度
    public String entranceForm;        // 进口形式
    public String useClassification;    // 用途分类
    public String mileageStake;        // 里程桩号
    public String aperture;            // 孔径
    public String fillHeight;            // 填土高
    public String entranceHeight;        // 进口高度
    public String exitForm;            // 出口形式
    public String useCase;            // 使用情况
    public String materialType;        // 材料类型
    public String length;                // 涵长
    public String clearHeight;        // 净高
    public String exitWidth;            // 出口宽度
    public String exitHeight;            // 出口高度
    public String attachmentNumber;    // 附件编号
    public String techGrade;  //技术状况
    public String designLoad; //设计荷载
    public String bottomArea; //护底材料与面积
    public String basicThick; //基础种类及厚度(米)
    public String plateThick; //墙/拱圈盖板或管厚(米)

    public String longitude; //经度
    public String latitude;  //纬度

    @Override
    public List<String> getContent() {
        if (propetryValues == null) {
            propetryValues = new ArrayList<>();
            propetryValues.add(TextUtils.isEmpty(code) ? "" : code);
            propetryValues.add(TextUtils.isEmpty(name) ? "" : name);
            propetryValues.add(TextUtils.isEmpty(routeCode) ? "" : routeCode);            //路线编码
            propetryValues.add(belongDept == null ? "" : belongDept.name);            //所属单位
            propetryValues.add(TextUtils.isEmpty(holeNumber) ? "" : holeNumber);            // 孔数
            propetryValues.add(TextUtils.isEmpty(culvertBottomScope) ? "" : culvertBottomScope);  // 涵底坡度
            propetryValues.add(TextUtils.isEmpty(entranceWidth) ? "" : entranceWidth);        // 进口宽度
            propetryValues.add(TextUtils.isEmpty(entranceForm) ? "" : entranceForm);        // 进口形式
            propetryValues.add(TextUtils.isEmpty(useClassification) ? "" : useClassification);    // 用途分类
            propetryValues.add(TextUtils.isEmpty(mileageStake) ? "" : mileageStake);        // 里程桩号
            propetryValues.add(TextUtils.isEmpty(aperture) ? "" : aperture);            // 孔径
            propetryValues.add(TextUtils.isEmpty(fillHeight) ? "" : fillHeight);            // 填土高
            propetryValues.add(TextUtils.isEmpty(entranceHeight) ? "" : entranceHeight);        // 进口高度
            propetryValues.add(TextUtils.isEmpty(exitForm) ? "" : exitForm);            // 出口形式
            propetryValues.add(TextUtils.isEmpty(useCase) ? "" : useCase);            // 使用情况
            propetryValues.add(TextUtils.isEmpty(materialType) ? "" : materialType);        // 材料类型
            propetryValues.add(TextUtils.isEmpty(length) ? "" : length);                // 涵长
            propetryValues.add(TextUtils.isEmpty(clearHeight) ? "" : clearHeight);        // 净高
            propetryValues.add(TextUtils.isEmpty(exitWidth) ? "" : exitWidth);            // 出口宽度
            propetryValues.add(TextUtils.isEmpty(exitHeight) ? "" : exitHeight);            // 出口高度
            propetryValues.add(TextUtils.isEmpty(attachmentNumber) ? "" : attachmentNumber);    // 附件编号
            propetryValues.add(TextUtils.isEmpty(techGrade) ? "" : techGrade);  //技术状况
            propetryValues.add(TextUtils.isEmpty(designLoad) ? "" : designLoad); //设计荷载
            propetryValues.add(TextUtils.isEmpty(bottomArea) ? "" : bottomArea); //护底材料与面积
            propetryValues.add(TextUtils.isEmpty(basicThick) ? "" : basicThick); //基础种类及厚度(米)
            propetryValues.add(TextUtils.isEmpty(plateThick) ? "" : plateThick); //墙/拱圈盖板或管厚(米)
        }
        return propetryValues;
    }

    @Override
    public List<String> getTitles() {
        List<String> values = new ArrayList<>();
        values.add("涵洞编号");
        values.add("涵洞名称");
        values.add("路线编码");
        values.add("所属单位");
        values.add("孔数");
        values.add("涵底坡度");
        values.add("进口宽度");
        values.add("进口形式");
        values.add("用途分类");
        values.add("里程桩号");
        values.add("孔径");
        values.add("填土高");
        values.add("进口高度");
        values.add("出口形式");
        values.add("使用情况");
        values.add("材料类型");
        values.add("涵长");
        values.add("净高");
        values.add("出口宽度");
        values.add("出口高度");
        values.add("附件编号");
        values.add("技术状况");
        values.add("设计荷载");
        values.add("护底材料与面积");
        values.add("基础种类及厚度(米)");
        values.add("墙/拱圈盖板或管厚(米)");
        return values;
    }

    @Override
    public List<String> getPropertyName() {
        List<String> values = new ArrayList<>();
        values.add("code");
        values.add("name");
        values.add("routeCode");
        values.add("belongDept");
        values.add("holeNumber");
        values.add("culvertBottomScope");
        values.add("entranceWidth");
        values.add("entranceForm");
        values.add("useClassification");
        values.add("mileageStake");
        values.add("aperture");
        values.add("fillHeight");
        values.add("entranceHeight");
        values.add("exitForm");
        values.add("useCase");
        values.add("materialType");
        values.add("length");
        values.add("clearHeight");
        values.add("exitWidth");
        values.add("exitHeight");
        values.add("attachmentNumber");
        values.add("techGrade");
        values.add("designLoad");
        values.add("bottomArea");
        values.add("basicThick");
        values.add("plateThick");
        return values;
    }

    public Culvert(WorkSectionPresenter presenter) {
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
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {

                try {
                    String data = new String(arg2, "utf-8");
                    Gson gson = new Gson();
                    data = data.replace("pager.", "");
                    CulvertG culvertG = gson.fromJson(data, CulvertG.class);
                    totalRows = culvertG.totalRows;
                    presenter.loadDataSuccess(culvertG.rows, type);
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
        client.get(url + "?pager.pageNo=" + pageNo + "&pager.pageSize=" + pageSize + "&sort=routeGrade,code" +
                        "&direction=asc" + "&deptIds=" + arg,
                responseHandler);

    }


    public class CulvertG {
        public String pageNo;
        public int totalRows;
        public List<Culvert> rows;
    }
}
