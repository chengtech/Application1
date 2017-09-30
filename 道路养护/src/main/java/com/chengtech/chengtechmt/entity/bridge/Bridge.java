package com.chengtech.chengtechmt.entity.bridge;

import android.content.Context;
import android.util.Log;

import com.chengtech.chengtechmt.entity.Dept;
import com.chengtech.chengtechmt.entity.User;
import com.chengtech.chengtechmt.presenter.Presenter;
import com.chengtech.chengtechmt.util.HttpclientUtil;
import com.chengtech.chengtechmt.util.MyConstants;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import io.realm.RealmObject;

public class Bridge implements Serializable{

	private String sort="sortOrderCode";
	public Presenter presenter;
	public List<String> propetryValues;
	public int totalRows;
	public String url = MyConstants.PRE_URL+"mt/dbm/road/bridge/listBridgeJson.action";


	public final static String FlAG = "flag";
	public String id ;
	public String name;					//名称
	public String code;					//编号
	public String longitude   ;          //经度
	public String latitude ;			  //纬度
	public String pileId;                //桥位中心桩号
	public String functionType;          //功能类型
	public String beneathChannelName;    //下穿通道名
	public String beneathChannelStake;   //下穿通道桩号
	public String designLoad;            //设计荷载
	public String trafficLoad;           //通行载重
	public String curveSlope;            //弯斜坡度
	public String deck;                  //桥面铺装
	public String structureType;         //构造类型
	public String scaleType;             //桥梁规模类型
	public String startStake;            //起始桩号
	public String endStake;              //终点桩号
	public String originalStake;         //原始桩号
	public String correctStake;          //纠正桩号
	public String bridgePainting;        //桥梁左右幅
	public String completionLife;        //建成年月/设计使用年限
	public String length;                //桥长
	public String deckWidth;             //桥面总宽
	public String roadwayWidth;          //车行道宽
	public String deckElevation;         //桥面标高
	public String subHeight;             //桥下净高
	public String upperHeight;           //桥上净高
	public String approachWidth;         //引道总宽
	public String citedWidth;            //引道路面宽
	public String linearApproach;        //引道线形
	public String expansionType;         //伸缩缝类型
	public String supportForm;           //支座形式
	public String peakCoefficient;       //地震动峰值加速度系数
	public String abutment;              //桥台护坡
	public String spier;                 //护墩体
	public String modulate;              //调治构造物
	public String normalLevel;           //常水位
	public String designLevel;           //设计水位
	public String historicalFloodLevel;  //历史洪水位
	public String designDrawing;         //设计图纸
	public String designFile;            //设计文件
	public String constructDocument;     //施工文件
	public String finBuiltDrawing;       //竣工图纸
	public String acceptanceDocument;    //验收文件
	public String administrativeDocument;//行政文件
	public String regularCheckReport;    //定期检查报告
	public String specialCheckReport;    //特殊检查报告
	public String maintainPreference;    //历次维修资料
	public String recordNo;              //档案号
	public String registerFile;          //存档案
	public String   filingYear;            //建档年/月
	public String verticalView;          //立面照
	public String frontalView;           //桥面正面照
	public String mainPrincipal;         //主管负责人
	public String writeUserId;           //填卡人
	public User writeUser;
	public String   fillcardDate;          //填卡日期
	public String sessionName;		      //附件名称
	public String sessionId;   			//附件id
	public Dept belongDept;
	public String belongDeptId;	//所属管理单位id
	public String routeCode;		//路线编码
	public String deptIds;			//用于数据查询，不用存放在数据库
	public String routeName;		//路线名称
	public String routeGrade;		//路线等级
	public List<BridgeTech> bridgeTechs;
	public List<BridgeRecords> bridgeRecords;
	public List<BridgeSupStructure> bridgeSupStructures;
	public List<BridgeSubStructure> bridgeSubStructures;
	public String memo;

	public Bridge(Presenter presenter) {
		this.presenter = presenter;
	}

	public List<String> getLevelA() {
		List<String> list = new ArrayList<String>();
		list.add(routeCode==null?"":routeCode);
		list.add(routeName==null?"":routeName);
		list.add(routeGrade==null?"":routeGrade);
		list.add(code==null?"":code);
		list.add(name==null?"":name);
		list.add(scaleType==null?"":scaleType);
		list.add(structureType==null?"":structureType);
		list.add(belongDept==null?"":belongDept.name);
		list.add(originalStake==null?"":originalStake);
		list.add(startStake==null?"":startStake);
		list.add(endStake==null?"":endStake);
		list.add(correctStake==null?"":correctStake);
		list.add(pileId==null?"":pileId);
		list.add(longitude==null?"":longitude);
		list.add(latitude==null?"":latitude);
		list.add(bridgePainting==null?"":bridgePainting);
		list.add(deck==null?"":deck);
		list.add(curveSlope==null?"":curveSlope);
		list.add(functionType==null?"":functionType);
		list.add(beneathChannelName==null?"":beneathChannelName);
		list.add(beneathChannelStake==null?"":beneathChannelStake);
		list.add(designLoad==null?"":designLoad);
		list.add(trafficLoad==null?"":trafficLoad);
		list.add(completionLife==null?"":completionLife);
		return list;
	}
	
	public List<String> getLevelB(){
		List<String> listb = new ArrayList<String>();
		listb.add(length==null?"":length);
		listb.add(deckWidth==null?"":deckWidth);
		listb.add(roadwayWidth==null?"":roadwayWidth);
		listb.add(deckElevation==null?"":deckElevation);
		listb.add(subHeight==null?"":subHeight);
		listb.add(upperHeight==null?"":upperHeight);
		listb.add(approachWidth==null?"":approachWidth);
		listb.add(citedWidth==null?"":citedWidth);
		listb.add(linearApproach==null?"":linearApproach);
		listb.add(expansionType==null?"":expansionType);
		listb.add(supportForm==null?"":supportForm);
		listb.add(peakCoefficient==null?"":peakCoefficient);
		listb.add(abutment==null?"":abutment);
		listb.add(spier==null?"":spier);
		listb.add(modulate==null?"":modulate);
		listb.add(normalLevel==null?"":normalLevel);
		listb.add(designLevel==null?"":designLevel);
		listb.add(historicalFloodLevel==null?"":historicalFloodLevel);
		listb.add(FlAG);  //上部结构
		listb.add(FlAG); //下部结构
		return listb;
		
	}
	
	public List<String> getLevelC(){
		List<String> listc = new ArrayList<String>();
		listc.add(designDrawing==null?"":designDrawing);
		listc.add(designFile==null?"":designFile);
		listc.add(constructDocument==null?"":constructDocument);
		listc.add(finBuiltDrawing==null?"":finBuiltDrawing);
		listc.add(acceptanceDocument==null?"":acceptanceDocument);
		listc.add(administrativeDocument==null?"":administrativeDocument);
		listc.add(regularCheckReport==null?"":regularCheckReport);
		listc.add(specialCheckReport==null?"":specialCheckReport);
		listc.add(maintainPreference==null?"":maintainPreference);
		listc.add(recordNo==null?"":recordNo);
		listc.add(registerFile==null?"":registerFile);
		listc.add(filingYear==null?"":filingYear);
		
		return listc;
		
	}
	
	public List<String> getLevelD(){
		List<String> listd = new ArrayList<String>();
		
		return listd;
		
	}


	public void getData(Context context,int pageNo,int pageSize, final String type,String arg) {
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
					BridgeG bridgeG = gson.fromJson(data, BridgeG.class);
					totalRows = bridgeG.totalRows;
					presenter.loadDataSuccess(bridgeG.rows,type);
//
				} catch (Exception e) {
					presenter.loadDataFailed();
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(int i, cz.msebera.android.httpclient.Header[] headers, byte[] bytes, Throwable throwable) {
				presenter.loadDataFailed();
			}

		};

		client.get(url+"?pager.pageNo="+pageNo+"&pager.pageSize="+pageSize+"&sort=" +sort+
						"&direction=asc"+"&deptIds="+arg+"&mobile=phone",
				responseHandler);

	}


	public class BridgeG {
		public String pageNo ;
		public int totalRows;
		public List<Bridge> rows;
	}
}
