package com.chengtech.chengtechmt.entity.expertdecision;

import android.content.Context;

import com.chengtech.chengtechmt.entity.BaseModel;
import com.chengtech.chengtechmt.entity.gson.RouteG;
import com.chengtech.chengtechmt.presenter.Presenter;
import com.chengtech.chengtechmt.util.HttpclientUtil;
import com.chengtech.chengtechmt.util.MyConstants;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者: LiuFuYingWang on 2016/12/29 11:08.
 * 沥青路面状况评定表
 */

public class BitumenRoadDamage extends BaseModel {

    public String default_url =
            MyConstants.PRE_URL + "mt/expertdecision/roadtechevaluation/bitumenroaddamage/listBitumenRoadDamageJson.action";

    public String investigator;            // 调查人员
    public String investigateTime;            // 调查时间
    public String startStake;                // 起点桩号
    public String endStake;                // 终点桩号
    public Double length = 0.0d;            // 路段长度
    public Double width = 0.0d;            // 路面宽度
    public Double drValue = 0.0d;                    //DR
    public Double pciValue = 0.0d;                //PCI
    public String year;                    //年份
    public String month;                    //月份
    public String investigateDirection;    //调查方向
    public String laneType;    // 车道类型
    public Double originalDr;    //原始DR
    public Double originalPci; //原始PCI
    public String routeCode;//路线编号
    public String techLevel;//技术等级
    public String roadType;//路面类型;


    private Presenter presenter;


    public BitumenRoadDamage(Presenter presenter) {
        this.presenter = presenter;
    }


    @Override
    public List<String> getContent() {
        return null;
    }

    @Override
    public List<String> getTitles() {
        return null;
    }

    @Override
    public List<String> getPropertyName() {
        return null;
    }

    public void getData(Context context, String urlParams) {
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
                    JSONObject jsonObject = new JSONObject(data);
                    JSONArray jsonArray = jsonObject.getJSONArray("rows");
                    List<BitumenRoadDamage> bitumenRoadDamages = new ArrayList<>();
                    for (int j = 0; j < jsonArray.length(); j++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(j);
                        BitumenRoadDamage bitumenRoadDamage = gson.fromJson(jsonObject1.toString(), BitumenRoadDamage.class);
                        bitumenRoadDamages.add(bitumenRoadDamage);
                    }
                    presenter.loadDataSuccess(bitumenRoadDamages, "BitumenRoadDamage");
                } catch (Exception e) {
                    presenter.hasError();
                }
            }

            @Override
            public void onFailure(int i, cz.msebera.android.httpclient.Header[] headers, byte[] bytes, Throwable throwable) {
                presenter.loadDataFailed();
            }

        };
        client.get(default_url + urlParams,
                responseHandler);
    }
}
