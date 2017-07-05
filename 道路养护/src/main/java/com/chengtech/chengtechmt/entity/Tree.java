package com.chengtech.chengtechmt.entity;

import android.content.Context;
import android.util.Log;

import com.chengtech.chengtechmt.entity.gson.RouteG;
import com.chengtech.chengtechmt.entity.gson.TreeG;
import com.chengtech.chengtechmt.presenter.Presenter;
import com.chengtech.chengtechmt.util.HttpclientUtil;
import com.google.gson.Gson;

import com.chengtech.chengtechmt.util.annotation.TreeNodeId;
import com.chengtech.chengtechmt.util.annotation.TreeNodeName;
import com.chengtech.chengtechmt.util.annotation.TreeNodePid;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;

public class Tree implements Serializable {

	private Presenter presenter;
	@TreeNodeId
	public String id;
	@TreeNodeName
	public String text;
	public String type;
	@TreeNodePid
	public String parentId;
	public String name;
	public String secondId;
	public String code;
	public String secondDeptId;
	public String url;
	public String deptType;


	public void getData(Context context,String url) {
		AsyncHttpClient client = HttpclientUtil.getInstance(context);

		AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				super.onStart();
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {

				try {
					String json = new String(arg2, "utf-8");
					Gson gson  = new Gson();
					json = "{\"success\":true,\"data\" : " + json + "}";
					TreeG treeG = gson.fromJson(json, TreeG.class);
					presenter.loadDataSuccess(treeG.data,0);
					presenter.loadDataSuccess(treeG.data,"Dept");
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
		client.get(url, responseHandler);
	}

	public Tree(Presenter presenter) {
		this.presenter = presenter;
	}

	public Tree(String id,String pId,String text){
		this.id = id;
		this.parentId = pId;
		this.text = text;
	}
}
