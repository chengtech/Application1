package com.chengtech.chengtechmt.entity.tunnel;

import android.content.Context;

import com.chengtech.chengtechmt.entity.Dept;
import com.chengtech.chengtechmt.entity.User;
import com.chengtech.chengtechmt.presenter.Presenter;
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
 * 隧道卡片实体类
 */
public class Tunnel implements Serializable {

	public Presenter presenter;
	public int totalRows;
	public String url = MyConstants.PRE_URL+"mt/dbm/road/tunnel/listTunnelJson.action";
	private String sort="sortOrderCode,code";


	public final static String FlAG = "flag";
	public String id ;

	//a
	public String routeCode;		//路线编码
	public String routeName;		//路线名称
	public String routeGrade;		//路线等级
	public String code;					//编号
	public String name;					//名称
	public String  stake;                     //隧道桩号
	public String  startStake;                //起始桩号
	public String	endStake;                  //终点桩号
	public String	originalStake;             //原始桩号
	public String	correctStake;              //纠正桩号
	public String	classification;            //隧道分类
	public String	completionLife;            //建成年限
	public String	bridgePainting;            //桥梁左右幅
	public String longitude;                 //经度
	public String latitude ;                 //纬度
	public String sectionName;     // 所属养护段
	public Dept belongDept; 		//管理单位
	//b
	public String	structureForm;             //隧道结构形式
	public String	upwardHoleLength;          //上行洞长
	public String	openingWidth;              //隧道净宽
	public String	drivewayWidth;             //行车道宽度
	public String	downHoleLength;            //下行洞长
	public String	sideStrip;                 //路缘带
	public String	lateralWidth;              //侧向余宽
	public String	accessChannelWidth;        //检修道宽
	public String	height;                    //隧道净高
	public String	ventilate;                 //通风
	public String	carCrossChannel;           //汽车横通道
	public String	pedestrianCrossChannel;    //人行横通道
	public String	longitudinalUpstreamHole;  //上行洞纵坡
	public String	longitudinalDownstreamHole;//下行洞纵坡
	public String	crossSectionalStructure;   //横断面结构
	public String	openCaveLiningStructure;   //明洞衬砌结构
	public String	upstreamWallMaterial;      //上行端及其端墙材料
	public String	darkCaveLiningStructure;   //暗洞
	public String	downstreamWallMaterials;   //下行端及其端墙材料
	public String	liningType;                //衬砌类型
	public String	liningStick;               //衬砌厚度
	public String	liningLength;              //衬砌长度
	public String	sideWallDecoration;        //侧墙饰面
	public String	innerWallDecoration;       //拱部内壁饰面
	public String	mainRoadMaterial;          //主线路面结构材料
	public String	crossRoadMaterial;         //横通路面结构材料
	
	//c
	public String	designDrawing;             //设计图纸
	public String	designFile;                //设计文件
	public String	constructDocument;         //施工文件
	public String	finBuiltDrawing;           //竣工图纸
	public String	acceptanceDocument;        //验收文件
	public String	administrativeDocument;    //行政文件
	public String	regularCheckReport;        //定期检查报告
	public String	specialCheckReport;        //特殊检查报告
	public String	maintainPreference;        //历次维修资料
	public String	recordNo;                  //档案号
	public String	registerFile;              //存档案
	public String	filingYear;                //建档年/月
	
	
	public String  mainPrincipal;             //主管负责人
	public String	writeUserId;               //填卡人
	public User writeUser;
	public String	fillCardDate;               //填卡日期
	public String sessionName;		            //附件名称
	public String sessionId;		            //附件id
	public String belongDeptId;	//所属管理单位id
	public String deptIds;			//用于数据查询，不用存放在数据库
	public List<TunnelTech> tunnelTechs;
	public List<TunnelRecords> tunnelRecords;
	public String memo;
	
	public List<String> getLevelA() {
		List<String> lista = new ArrayList<String>();
		
		lista.add(routeCode==null?"":routeCode);
		lista.add(routeName==null?"":routeName);
		lista.add(routeGrade==null?"":routeGrade);
		lista.add(code==null?"":code);
		lista.add(name==null?"":name);
		lista.add(stake==null?"":stake);
		lista.add(startStake==null?"":startStake);
		lista.add(endStake==null?"":endStake);
		lista.add(originalStake==null?"":originalStake);
		lista.add(correctStake==null?"":correctStake);
		lista.add(classification==null?"":classification);
		lista.add(completionLife==null?"":completionLife);
		lista.add(bridgePainting==null?"":bridgePainting);
		lista.add(longitude==null?"":longitude);
		lista.add(latitude==null?"":latitude);
		lista.add(sectionName==null?"":sectionName);
		lista.add(belongDept==null?"":belongDept.name);
		
		return lista;
	}
	
	public List<String> getLevelB () {
		List<String> listb = new ArrayList<String>();
		
		listb.add(structureForm==null?"":structureForm);
		listb.add(upwardHoleLength==null?"":upwardHoleLength);
		listb.add(openingWidth==null?"":openingWidth);
		listb.add(drivewayWidth==null?"":drivewayWidth);
		listb.add(downHoleLength==null?"":downHoleLength);
		listb.add(sideStrip==null?"":sideStrip);
		listb.add(lateralWidth==null?"":lateralWidth);
		listb.add(accessChannelWidth==null?"":accessChannelWidth);
		listb.add(height==null?"":height);
		listb.add(ventilate==null?"":ventilate);
		listb.add(carCrossChannel==null?"":carCrossChannel);
		listb.add(pedestrianCrossChannel==null?"":pedestrianCrossChannel);
		listb.add(longitudinalUpstreamHole==null?"":longitudinalUpstreamHole);
		listb.add(longitudinalDownstreamHole==null?"":longitudinalDownstreamHole);
		listb.add(crossSectionalStructure==null?"":crossSectionalStructure);
		listb.add(openCaveLiningStructure==null?"":openCaveLiningStructure);
		listb.add(upstreamWallMaterial==null?"":upstreamWallMaterial);
		listb.add(darkCaveLiningStructure==null?"":darkCaveLiningStructure);
		listb.add(downstreamWallMaterials==null?"":downstreamWallMaterials);
		listb.add(liningType==null?"":liningType);
		listb.add(liningStick==null?"":liningStick);
		listb.add(liningLength==null?"":liningLength);
		listb.add(sideWallDecoration==null?"":sideWallDecoration);
		listb.add(innerWallDecoration==null?"":innerWallDecoration);
		listb.add(mainRoadMaterial==null?"":mainRoadMaterial);
		listb.add(crossRoadMaterial==null?"":crossRoadMaterial);
		
		return listb;
		
	}
	
	public List<String> getLevelC () {
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
		listc.add(filingYear==null?"":filingYear);
		
		return listc;
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
					data = data.replace("pager.","");
					TunnelG tunnelG = gson.fromJson(data, TunnelG.class);
					totalRows = tunnelG.totalRows;
					presenter.loadDataSuccess(tunnelG.rows,type);
//
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
		client.get(url + "?pager.pageNo=" + pageNo + "&pager.pageSize=" + pageSize + "&sort=" +sort+
						"&direction=asc" + "&deptIds=" + arg+"&mobile=phone",
				responseHandler);

	}

	public Tunnel(Presenter presenter) {
		this.presenter = presenter;
	}

	public class TunnelG {
		public String pageNo ;
		public int totalRows;
		public List<Tunnel> rows;
	}
}
