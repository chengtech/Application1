package com.chengtech.chengtechmt.entity;

import android.content.Context;
import android.text.TextUtils;

import com.chengtech.chengtechmt.entity.gson.SectionG;
import com.chengtech.chengtechmt.presenter.Presenter;
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
 * 作者: LiuFuYingWang on 2016/5/19 13:48.
 */
public class Observatory extends BaseModel {
    public Presenter presenter;
    public List<String> propetryValues;
    public int totalRows;
    private String sort = "sortOrderCode,mtDeptId,startStake";
    public String url = MyConstants.PRE_URL + "mt/dbm/road/observatory/listObservatoryJson.action";

    public String routeCode;        //路线编码
    public String routeName;        //路线名称
    public String observatoryNumber;        //调查站编号
    public String sectionCode;        //所属养护路段
    public String startStake;     //起点桩号
    public String endStake;       //终点桩号
    public String year;                        //年份
    public String administrativeDivision;        //行政区划
    public String observatoryType;                //观测站类型
    public String createTime;                    //建站时间
    public String surveyMethod;                //调查方法
    public String stake;                        //桩号
    public String startName;                    //起点名称
    public String endName;                        //终点名称
    public String upName;                        //上行名称
    public String downName;                    //下行名称
    public String representMileage;            //代表里程
    public String techGrade;                    //技术等级
    public String techGradeUnique;                //技术等级唯一
    public String laneFeature;                    //车道特征
    public String roadType;                    //路面类型
    public String roadWidth;                    //路面宽度
    public String designSpeed;                    //设计时速
    public String landformType;                //地貌
    public String applicableTraffic;            //适用交通量
    public String powerSupply;                    //供电方式
    public String communicationMode;            //通讯方式
    public String roadFunction;                //公路功能
    public String observatoryState;            //观测站状态
    public String proportionStartStake;        //比重起点桩号
    public String proportionEndStake;            //比重止点桩号
    public String proportionRepresentMileage;    //比重代表里程
    public String outAdministrativeDivision; //出临行政区划
    public String testMonth;     //停测月份
    public String firstMileage;  //一级里程
    public String secondMileage; //二级里程
    public String thirdMileage;  //三级里程
    public String fourMileage;   //四级里程
    public String highMileage;   //高速里程
    public String suchMileage;   //等外里程
    public String investigatorsNum;//调查人员数量
    public String longitude; //经度
    public String latitude;  //纬度

    public Observatory(Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public List<String> getContent() {
        if (propetryValues == null) {
            propetryValues = new ArrayList<>();
            propetryValues.add(TextUtils.isEmpty(routeCode) ? "" : routeCode);
            propetryValues.add(TextUtils.isEmpty(routeName) ? "" : routeName);
            propetryValues.add(TextUtils.isEmpty(sectionCode) ? "" : sectionCode);
            propetryValues.add(TextUtils.isEmpty(startStake) ? "" : startStake);
            propetryValues.add(TextUtils.isEmpty(endStake) ? "" : endStake);
            propetryValues.add(TextUtils.isEmpty(year) ? "" : year);
            propetryValues.add(TextUtils.isEmpty(administrativeDivision) ? "" : administrativeDivision);
            propetryValues.add(TextUtils.isEmpty(observatoryType) ? "" : observatoryType);
            propetryValues.add(TextUtils.isEmpty(createTime) ? "" : createTime);
            propetryValues.add(TextUtils.isEmpty(surveyMethod) ? "" : surveyMethod);
            propetryValues.add(TextUtils.isEmpty(stake) ? "" : stake);
            propetryValues.add(TextUtils.isEmpty(startName) ? "" : startName);
            propetryValues.add(TextUtils.isEmpty(endName) ? "" : endName);
            propetryValues.add(TextUtils.isEmpty(upName) ? "" : upName);
            propetryValues.add(TextUtils.isEmpty(downName) ? "" : downName);
            propetryValues.add(TextUtils.isEmpty(representMileage) ? "" : representMileage);
            propetryValues.add(TextUtils.isEmpty(techGrade) ? "" : techGrade);
            propetryValues.add(TextUtils.isEmpty(techGradeUnique) ? "" : techGradeUnique);
            propetryValues.add(TextUtils.isEmpty(laneFeature) ? "" : laneFeature);
            propetryValues.add(TextUtils.isEmpty(roadType) ? "" : roadType);
            propetryValues.add(TextUtils.isEmpty(roadWidth) ? "" : roadWidth);
            propetryValues.add(TextUtils.isEmpty(designSpeed) ? "" : designSpeed);
            propetryValues.add(TextUtils.isEmpty(landformType) ? "" : landformType);
            propetryValues.add(TextUtils.isEmpty(applicableTraffic) ? "" : applicableTraffic);
            propetryValues.add(TextUtils.isEmpty(powerSupply) ? "" : powerSupply);
            propetryValues.add(TextUtils.isEmpty(communicationMode) ? "" : communicationMode);
            propetryValues.add(TextUtils.isEmpty(roadFunction) ? "" : roadFunction);
            propetryValues.add(TextUtils.isEmpty(observatoryState) ? "" : observatoryState);
            propetryValues.add(TextUtils.isEmpty(proportionStartStake) ? "" : proportionStartStake);
            propetryValues.add(TextUtils.isEmpty(proportionEndStake) ? "" : proportionEndStake);
            propetryValues.add(TextUtils.isEmpty(proportionRepresentMileage) ? "" : proportionRepresentMileage);
            propetryValues.add(TextUtils.isEmpty(outAdministrativeDivision) ? "" : outAdministrativeDivision);
            propetryValues.add(TextUtils.isEmpty(testMonth) ? "" : testMonth);
            propetryValues.add(TextUtils.isEmpty(firstMileage) ? "" : firstMileage);
            propetryValues.add(TextUtils.isEmpty(secondMileage) ? "" : secondMileage);
            propetryValues.add(TextUtils.isEmpty(thirdMileage) ? "" : thirdMileage);
            propetryValues.add(TextUtils.isEmpty(fourMileage) ? "" : fourMileage);
            propetryValues.add(TextUtils.isEmpty(highMileage) ? "" : highMileage);
            propetryValues.add(TextUtils.isEmpty(suchMileage) ? "" : suchMileage);
            propetryValues.add(TextUtils.isEmpty(investigatorsNum) ? "" : investigatorsNum);
        }
        return propetryValues;
    }

    @Override
    public List<String> getTitles() {
        List<String> values = new ArrayList<>();
        values.add("路线编码");
        values.add("路线名称");
        values.add("所属养护路段");
        values.add("起点桩号");
        values.add("终点桩号");
        values.add("年份");
        values.add("行政区划");
        values.add("观测站类型");
        values.add("建站时间");
        values.add("调查方法");
        values.add("桩号");
        values.add("起点名称");
        values.add("终点名称");
        values.add("上行名称");
        values.add("下行名称");
        values.add("代表里程");
        values.add("技术等级");
        values.add("技术等级唯一");
        values.add("车道特征");
        values.add("路面类型");
        values.add("路面宽度");
        values.add("设计时速");
        values.add("地貌");
        values.add("适用交通量");
        values.add("供电方式");
        values.add("通讯方式");
        values.add("公路功能");
        values.add("观测站状态");
        values.add("比重起点桩号");
        values.add("比重止点桩号");
        values.add("比重代表里程");
        values.add("出临行政区划");
        values.add("停测月份");
        values.add("一级里程");
        values.add("二级里程");
        values.add("三级里程");
        values.add("四级里程");
        values.add("高速里程");
        values.add("等外里程");
        values.add("调查人员数量");
        return values;
    }

    @Override
    public List<String> getPropertyName() {
        List<String> values = new ArrayList<>();
        values.add("routeCode");
        values.add("routeName");
        values.add("sectionCode");
        values.add(" startStake");
        values.add(" endStake");
        values.add("year");
        values.add("administrativeDivision");
        values.add("observatoryType");
        values.add("createTime");
        values.add("surveyMethod");
        values.add("stake");
        values.add("startName");
        values.add("endName");
        values.add("upName");
        values.add("downName");
        values.add("representMileage");
        values.add("techGrade");
        values.add("techGradeUnique");
        values.add("laneFeature");
        values.add("roadType");
        values.add("roadWidth");
        values.add("designSpeed");
        values.add("landformType");
        values.add("applicableTraffic");
        values.add("powerSupply");
        values.add("communicationMode");
        values.add("roadFunction");
        values.add("observatoryState");
        values.add("proportionStartStake");
        values.add("proportionEndStake");
        values.add("proportionRepresentMileage");
        values.add("outAdministrativeDivision");
        values.add("testMonth");
        values.add("firstMileage");
        values.add("secondMileage");
        values.add("thirdMileage");
        values.add("fourMileage");
        values.add("highMileage");
        values.add("suchMileage");
        values.add("investigatorsNum");
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
                    Gson gson = new Gson();
                    data = data.replace("pager.", "");
                    ObservatoryG observatoryG = gson.fromJson(data, ObservatoryG.class);
                    totalRows = observatoryG.totalRows;
                    presenter.loadDataSuccess(observatoryG.rows, type);
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
        client.get(url + "?pager.pageNo=" + pageNo + "&pager.pageSize=" + pageSize + "&sort=" + sort +
                        "&direction=asc" + "&deptIds=" + arg,
                responseHandler);

    }


    public class ObservatoryG {
        public String pageNo;
        public int totalRows;
        public List<Observatory> rows;
    }
}
