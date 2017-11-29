package com.chengtech.chengtechmt.entity;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;

import com.chengtech.chengtechmt.R;
import com.chengtech.chengtechmt.entity.gson.RouteG;
import com.chengtech.chengtechmt.presenter.Presenter;
import com.chengtech.chengtechmt.util.DateUtils;
import com.chengtech.chengtechmt.util.HttpclientUtil;
import com.chengtech.chengtechmt.util.MyConstants;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Route  implements Serializable {

	public String id;
	public String name;
	public Presenter presenter;
	public String url = MyConstants.PRE_URL+"mt/dbm/road/route/listRouteJson.action";
	public int totalRows; //查出来总的数目
	public List<String> propetryValues;
	public String code; 					//路线编号
	public String routeGrade;				//路线等级
	public String routeGradeName;				//路线等级名称
	public String	startStake;			    //里程起点
	public String endStake;			    //里程终点
	public Dept   dept; 					//管理单位
	public String	length;                 //长度
	public String	routeDirections;        //路线走向
	public String constructionDate;       //建工日期
	public String	openingDate;            //通车日期
	public String 	memo;			//备注
	public String sessionName;		   // 附件名称
	public String sessionId;
	public String	roadType;          //路面类型
	public String  techGrade;         //技术等级

	private String sort="routeGrade,code";

	public HashMap<String,String> sparseArray = new HashMap<>();


	public Route(Presenter presenter) {
		this.presenter = presenter;
		sparseArray.put("1","G-国道");
		sparseArray.put("2","S-省道");
		sparseArray.put("3","X-县道");
		sparseArray.put("4","Y-乡道");
		sparseArray.put("5","Z-专用公路");
		sparseArray.put("6","C-村道");

	}

	public List<String> getArray() {
		if (propetryValues==null ) {
			propetryValues = new ArrayList<>();
			propetryValues.add(TextUtils.isEmpty(code)?"":code);
			propetryValues.add(TextUtils.isEmpty(routeGradeName)?"":routeGradeName);
			propetryValues.add(TextUtils.isEmpty(name)?"":name);
			propetryValues.add(TextUtils.isEmpty(startStake)?"":startStake);
			propetryValues.add(TextUtils.isEmpty(endStake)?"":endStake);
			propetryValues.add(TextUtils.isEmpty(dept.name)?"":dept.name);
			propetryValues.add(TextUtils.isEmpty(length)?"":length);
			propetryValues.add(TextUtils.isEmpty(routeDirections)?"":routeDirections);
			propetryValues.add(constructionDate==null?"": constructionDate);
			propetryValues.add(openingDate==null?"":openingDate);
			propetryValues.add(roadType==null?"":roadType);
			propetryValues.add(techGrade==null?"":techGrade);
			propetryValues.add(TextUtils.isEmpty(memo)?"":memo);
		}

		return propetryValues;
	}

	public String [] getItemTitle() {
		String[] titleArray = new String[5];
		titleArray[0] = name;
		titleArray[1] = "路线编号:"+ (code==null?"":code);
		titleArray[2] = "<br/>路线等级：" + (routeGrade==null?"":routeGrade);
		titleArray[3] = "<br/>长度(km)：" + (length==null?"":length);
		titleArray[4] = "<br/>路线走向：" + (routeDirections==null?"":routeDirections);
		return titleArray;
	}


	public void getData(Context context,int pageNo,int pageSize) {
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
					Gson gson  = new Gson();
					data = data.replace("pager.","");
					RouteG routeG = gson.fromJson(data, RouteG.class);
					totalRows = routeG.totalRows;
					presenter.loadDataSuccess(routeG.rows);
//
				} catch (Exception e) {
					presenter.hasError();
				}
			}

			@Override
			public void onFailure(int i, cz.msebera.android.httpclient.Header[] headers, byte[] bytes, Throwable throwable) {
				presenter.loadDataFailed();
			}

		};
		client.get(url+"?pager.pageNo="+pageNo+"&pager.pageSize="+pageSize+"&sort="+sort +
						"&direction=asc",
				responseHandler);

	}

	public void getData(Context context, String url, final String type) {
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
					Gson gson  = new Gson();
					data = data.replace("pager.","");
					RouteG routeG = gson.fromJson(data, RouteG.class);
					presenter.loadDataSuccess(routeG.rows,type);
				} catch (UnsupportedEncodingException e) {
					presenter.hasError();
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(int i, cz.msebera.android.httpclient.Header[] headers, byte[] bytes, Throwable throwable) {
				presenter.loadDataFailed();
			}

		};
		client.get(url,
				responseHandler);
	}

}
