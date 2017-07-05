package com.chengtech.chengtechmt.entity;

import android.content.Context;
import android.text.TextUtils;

import com.chengtech.chengtechmt.entity.gson.SafetyFacilitiesG;
import com.chengtech.chengtechmt.presenter.Presenter;
import com.chengtech.chengtechmt.util.HttpclientUtil;
import com.chengtech.chengtechmt.util.MyConstants;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者: LiuFuYingWang on 2016/5/18 14:06.
 */
public class Greening extends BaseModel {

    public List<String> propertyValue;
    public Presenter presenter;
    public String url = MyConstants.PRE_URL+"mt/dbm/road/greening/listGreeningJson.action";
    public int totalRows;

    public String registrationDate;         //登记日期
    public String startStake;             //起点桩号
    public String endStake; 			         //终点桩号
    public String greeningMileage;        //可绿化里程(KM)
    public String trees;                  //乔木(株)
    public String totalArea;              //绿化总面积(M2)
    public String shrubs;                 //花灌(株)
    public String zoningArea;             //中分带绿化面积(M2)
    public String hedge;                  //绿篱(米)
    public String lateralZonationArea;    //侧分带绿化面积(M2)
    public String lawn;                   //草坪(M2)
    public String sidewalkArea;           //人行道绿化面积(M2)
    public String otherVegetation;        //其他地被(M2)
    public String bothsideSpaceArea;      //两侧绿地面积(M2)
    public String afforestationRate;      //绿化率(%)
    public String onSlopeArea;            //上边坡绿化面积(M2)
    public String coverageRate;           //覆盖率(%)
    public String underSlopeArea;         //下边坡绿化面积(M2)
    public String routeCode;              //路线编码
    public String sectionCode;            //路段编码

    public String longitude; //经度
    public String latitude;  //纬度

    public Greening(Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public List<String> getContent() {
        if (propertyValue==null) {
            propertyValue = new ArrayList<>();
            propertyValue.add(TextUtils.isEmpty(routeCode)?"":routeCode);
            propertyValue.add(TextUtils.isEmpty(code)?"":code);
            propertyValue.add(TextUtils.isEmpty(registrationDate)?"":registrationDate);
            propertyValue.add(TextUtils.isEmpty(startStake)?"":startStake);
            propertyValue.add(TextUtils.isEmpty(endStake)?"":endStake);
            propertyValue.add(TextUtils.isEmpty(greeningMileage)?"":greeningMileage);
            propertyValue.add(TextUtils.isEmpty(trees)?"":trees);
            propertyValue.add(TextUtils.isEmpty(totalArea)?"":totalArea);
            propertyValue.add(TextUtils.isEmpty(shrubs)?"":shrubs);
            propertyValue.add(TextUtils.isEmpty(zoningArea)?"":zoningArea);
            propertyValue.add(TextUtils.isEmpty(hedge)?"":hedge);
            propertyValue.add(TextUtils.isEmpty(lateralZonationArea)?"":lateralZonationArea);
            propertyValue.add(TextUtils.isEmpty(lawn)?"":lawn);
            propertyValue.add(TextUtils.isEmpty(sidewalkArea)?"":sidewalkArea);
            propertyValue.add(TextUtils.isEmpty(otherVegetation)?"":otherVegetation);
            propertyValue.add(TextUtils.isEmpty(bothsideSpaceArea)?"":bothsideSpaceArea);
            propertyValue.add(TextUtils.isEmpty(afforestationRate)?"":afforestationRate);
            propertyValue.add(TextUtils.isEmpty(onSlopeArea)?"":onSlopeArea);
            propertyValue.add(TextUtils.isEmpty(coverageRate)?"":coverageRate);
            propertyValue.add(TextUtils.isEmpty(underSlopeArea)?"":underSlopeArea);
            propertyValue.add(TextUtils.isEmpty(memo)?"":memo);
        }

        return propertyValue;
    }

    @Override
    public List<String> getTitles() {
        List<String> values = new ArrayList<>();
        values.add("路线编码");
        values.add("路段编码");
        values.add("登记日期");
        values.add("起点桩号");
        values.add("终点桩号");
        values.add("可绿化里程(KM)");
        values.add("乔木(株)");
        values.add("绿化总面积(M2)");
        values.add("花灌(株)");
        values.add("中分带绿化面积(M2)");
        values.add("绿篱(米)");
        values.add("侧分带绿化面积(M2)");
        values.add("草坪(M2)");
        values.add("人行道绿化面积(M2)");
        values.add("其他地被(M2)");
        values.add("两侧绿地面积(M2)");
        values.add("绿化率(%)");
        values.add("上边坡绿化面积(M2)");
        values.add("覆盖率(%)");
        values.add("下边坡绿化面积(M2)");
        values.add("备注");
        return values;
    }

    @Override
    public List<String> getPropertyName() {
        List<String> values = new ArrayList<>();
        values.add("routeCode");
        values.add("sectionCode");
        values.add("registrationDate");
        values.add("startStake");
        values.add("endStake");
        values.add("greeningMileage");
        values.add("trees");
        values.add("totalArea");
        values.add("shrubs");
        values.add("zoningArea");
        values.add("hedge");
        values.add("lateralZonationArea");
        values.add("lawn");
        values.add("sidewalkArea");
        values.add("otherVegetation");
        values.add("bothsideSpaceArea");
        values.add("afforestationRate");
        values.add("onSlopeArea");
        values.add("coverageRate");
        values.add("underSlopeArea");
        values.add("memo");
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
                    GreeningG greeningG = gson.fromJson(data, GreeningG.class);
                    totalRows = greeningG.totalRows;
                    presenter.loadDataSuccess(greeningG.rows,type);
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


    public class GreeningG {
        public String pageNo ;
        public int totalRows;
        public List<Greening> rows;

    }
}
