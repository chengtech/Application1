package com.chengtech.chengtechmt.entity;

import android.content.Context;

import com.chengtech.chengtechmt.presenter.Presenter;
import com.chengtech.chengtechmt.util.HttpclientUtil;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.util.List;

/**
 * 作者: LiuFuYingWang on 2016/6/29 10:02.
 * 小额专修实体类
 */
public class MediumPlanprogress {

    public Presenter presenter;

    public  String fillDate;   //填报日期
    public List<MediumPlanprogressItem> mediumPlanprogressItem;	//子记录
    public String fillYear;	//填报年份
    public String deptName; //填表单位

    //2016-6-29 (英旺)以下的几个成员属性是为了查询，不写入数据库，只用public修饰，不严格要求。
    public double maintainLengths ;		//实施里程合计
    public double budgetFunds ;				//预算下达资金合计
    public double replyFunds;				//批复预算金额合计
    public double paidFunds ;				//已支付金额合计
    public double notPaidFunds;			//未支付金额合计
    public double projectNums;			//总路面工程数量

    public int sumcount = 0;				//总计
    public int nocount = 0;				//未开工
    public int incount = 0;			    //正在施工
    public int finishcount = 0;			//已完工
    public int notDetectedCount = 0;	//未检测

    public MediumPlanprogress(Presenter presenter) {
        this.presenter = presenter;
    }

    public void getData(Context context, String url, final String type,int pageNo) {
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
                    MediumPlanprogressG m = gson.fromJson(data,MediumPlanprogressG.class);
                    presenter.loadDataSuccess(m.rows,type);
                } catch (Exception e) {
                    presenter.hasError();
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
        client.get(url+"&pager.pageNo="+pageNo,
                responseHandler);
    }

    public class MediumPlanprogressG {
        public List<MediumPlanprogress> rows;
    }
}
