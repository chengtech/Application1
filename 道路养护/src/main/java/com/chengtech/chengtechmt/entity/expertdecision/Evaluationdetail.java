package com.chengtech.chengtechmt.entity.expertdecision;

import android.content.Context;

import com.chengtech.chengtechmt.entity.BaseModel;
import com.chengtech.chengtechmt.presenter.Presenter;
import com.chengtech.chengtechmt.util.HttpclientUtil;
import com.chengtech.chengtechmt.util.MyConstants;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者: LiuFuYingWang on 2017/1/10 15:18.
 * 评定明细实体类
 */

public class Evaluationdetail extends BaseModel {
    public String sectionId;    // 路段id
    public String laneType ;    // 车道类型

    public String routeId;      // 路线id
    public String routeName;    // 路线名称
    public String techLevel;    // 技术等级
    public String roadType;     // 路线类型
    public String investigateDirection;    // 检测方向
    public String pileNumber;   // 桩号
    public Double roadLength;   // 长度
    public String checkDate;    // 检测时间
    public Double mqi = 0.0;    // 公路技术状况指数
    public Double pqi = 0.0;    // 路面使用性能
    public Double pci = 0.0;    // 路面损坏
    public Double rqi = -100.0;  // 路面平整度
    public Double rdi = -100.0;  // 路面车辙
    public Double sri = -100.0;  // 路面抗滑性能
    public Double pssi = -100.0; // 结构强度
    public Double sci = 0.0;    // 路基SCI
    public Double bci = 0.0;    // 桥隧构造物BCI
    public Double tci = 0.0;    // 沿线设施TCI
    public String year;         // 年
    public String month;        // 月
    public String routeCode;


    private Presenter presenter;
    private String default_url = MyConstants.PRE_URL+"mt/expertdecision/roadtechevaluation/evaluationdetail/listEvaluationDetailJson.action";

    public Evaluationdetail(Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public List<String> getContent() {
        List<String> values = new ArrayList<>();
        values.add(routeCode==null?"":routeCode);
        values.add(pileNumber==null?"":pileNumber);
        values.add(investigateDirection==null?"":investigateDirection);
        values.add(laneType==null?"":laneType);
        values.add(roadType==null?"":roadType);
        values.add(pqi==null?"":String.valueOf(pqi));
        values.add(pci==null?"":String.valueOf(pci));
        values.add(rqi==null?"":String.valueOf(rqi));
        values.add(checkDate==null?"":checkDate);
        return values;
    }

    @Override
    public List<String> getTitles() {
        List<String> titles = new ArrayList<>();
        titles.add("路线编号");
        titles.add("桩号");
        titles.add("监测方向");
        titles.add("车道类型");
        titles.add("路面类型");
        titles.add("PQI");
        titles.add("PCI");
        titles.add("RQI");
        titles.add("监测时间");
        return titles;
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
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {

                try {
                    String data = new String(arg2, "utf-8");
                    Gson gson  = new Gson();
                    JSONObject jsonObject = new JSONObject(data);
                    JSONArray jsonArray = jsonObject.getJSONArray("rows");
                    List<Evaluationdetail> evaluationdetails = new ArrayList<>();
                    for (int i=0;i<jsonArray.length();i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        Evaluationdetail evaluationdetail = gson.fromJson(jsonObject1.toString(), Evaluationdetail.class);
                        evaluationdetails.add(evaluationdetail);
                    }
                    presenter.loadDataSuccess(evaluationdetails,"Evaluationdetail");
                } catch (Exception e) {
                    presenter.hasError();
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
        client.get(default_url+urlParams,
                responseHandler);

    }
}
