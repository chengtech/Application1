package com.chengtech.chengtechmt.entity;

import android.content.Context;

import com.chengtech.chengtechmt.presenter.Presenter;
import com.chengtech.chengtechmt.util.CommonUtils;
import com.chengtech.chengtechmt.util.DateUtils;
import com.chengtech.chengtechmt.util.HttpclientUtil;
import com.chengtech.chengtechmt.util.MyConstants;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 大中修实体类
 * 作者: LiuFuYingWang on 2016/7/5 10:27.
 */
public class ProjectManagement implements Serializable {

    public Presenter presenter;
    //    public List<String> propetryValues;
    public int totalRows;

    public String id;
    public String fillDate;                       //填报日期
    public String projectName;                 //项目名称
    public String other;                     //其他
    public String beginPeg;                 //开始桩号
    public String endPeg;                     //结束桩号
    public Double totalInvestment;             //总投资
    public String buildUnit;                 //建设单位
    public String subtotal;                 //小计
    public String classA;                     //一级
    public String classB;                     //二级
    public String classC;                     //三级
    public String classD;                     //四级
    public String planApprovalNumber;         //方案文号
    public String approvalDate;                 //方案审批时间
    public String approvalUnit;             //方案审批单位
    public String approvalEstimate;
    ;         //方案审批估算
    public String constructionApprovalNumber;//施工图审批文号
    public String constructionDate;             //施工审批时间
    public String constructionUnit;         //施工审批单位
    public String constructionEstimate;
    ;     //施工审批预算
    public String designFinishDate;             //设计招标完成时间
    public String constructionFinishDate;     //施工招标完成时间
    public String supervisorFinishDate;    //监理招标完成时间

    public String designPerson;             //设计招标中标人
    public String constructionPerson;     //施工招标中标人
    public String supervisorPerson;    //监理招标中标人

    public String contractPrice;                    //合同价
    public String budgetForehead;                    //预算评审审定额
    public String settlementForehead;                //结算评审审定额
    public String isProvincialCapital;                //是否使用省部补助资金
    public String provincialCapital;                //省部资金
    public String localtionCapital;                    //地方资金
    public String designAmount;        //设计金额
    public String superviseAmount;        // 监理金额
    public String constructAmount;        // 施工金额
    public String checkAmount;            // 检测金额
    public String agencyAmount;        // 代理金额
    public String consultAmount;        // 咨询金额
    public String otherAmount;            // 其他金额
    public String finalEvaluationAmount; // 决算评审定额金额
    public String capitalSum;            //省部/地方配套资金总计


    public String mainContent;                            //主要实施内容
    public String permitFlag;                            //是否办理施工许可
    public String superviseFlag;                        //是否申报质量监督
    public String contractDays;                        //合同工期


    public String beginDate;                            //开始时间
    public String constructionProgress;                    //施工进度
    public String finishDate;                            //结束时间
    public String surveySessionId;          //建设概况附件的SessionID(多媒体编号)
    public String scaleSessionId;           //规模附件的SessionID(多媒体编号)
    public String buildProgramSessionId;    //建设方案附件的SessionID(多媒体编号)
    public String designSessionId;          //施工设计附件的SessionID(多媒体编号)
    public String tenderSessionId;          //招投标附件的SessionID(多媒体编号)
    public String constructionSessionId;    //工程实施附件的SessionID(多媒体编号)
    public String acceptSessionId;            //工程验收附件的SessionID(多媒体编号)
    public String investSessionId;            //工程投资附件的SessionID(多媒体编号)


    public String sectionIds;               //区段ids
    public String sectionNames;             //区段名称s

    public String bridgeIds;               //桥梁ids
    public String bridgeNames;             //桥梁名称s

    public String dateAccept;               //交工验收
    public String completeAccept;             //竣工验收
    public String memo;


    public ProjectManagement(Presenter presenter) {
        this.presenter = presenter;
    }

    public List<String> getListOne() {
        List<String> propetryValues = new ArrayList<>();
        propetryValues.add(sectionNames == null ? "" : sectionNames);
        propetryValues.add(bridgeNames == null ? "" : bridgeNames);
        propetryValues.add(beginPeg == null ? "" : beginPeg);
        propetryValues.add(endPeg == null ? "" : endPeg);
        propetryValues.add(totalInvestment == null ? "" : CommonUtils.getFormat(",###.00", new BigDecimal(totalInvestment)));
        propetryValues.add(buildUnit == null ? "" : buildUnit);
        propetryValues.add(surveySessionId == null ? "" : surveySessionId);
        return propetryValues;
    }

    public List<String> getListTwo() {
        List<String> propetryValues = new ArrayList<>();
        propetryValues.add(subtotal == null ? "" : subtotal);
        propetryValues.add(classA == null ? "" : classA);
        propetryValues.add(classB == null ? "" : classB);
        propetryValues.add(classC == null ? "" : classC);
        propetryValues.add(classD == null ? "" : classD);
        propetryValues.add(scaleSessionId == null ? "" : scaleSessionId);
        return propetryValues;
    }

    public List<String> getListThree() {
        List<String> propetryValues = new ArrayList<>();
        propetryValues.add(planApprovalNumber == null ? "" : planApprovalNumber);
        propetryValues.add(approvalDate == null ? "" : DateUtils.convertDate(approvalDate));
        propetryValues.add(approvalUnit == null ? "" : approvalUnit);
        propetryValues.add(approvalEstimate == null ? "" : CommonUtils.getCommalFormat(new BigDecimal(approvalEstimate)));
        propetryValues.add(buildProgramSessionId == null ? "" : buildProgramSessionId);
        return propetryValues;
    }

    public List<String> getListFour() {
        List<String> propetryValues = new ArrayList<>();
        propetryValues.add(constructionApprovalNumber == null ? "" : constructionApprovalNumber);
        propetryValues.add(constructionDate == null ? "" : DateUtils.convertDate(constructionDate));
        propetryValues.add(constructionUnit == null ? "" : constructionUnit);
        propetryValues.add(constructionEstimate == null ? "" : CommonUtils.getCommalFormat(new BigDecimal(constructionEstimate)));
        propetryValues.add(designSessionId == null ? "" : designSessionId);
        return propetryValues;
    }

    public List<String> getListFive() {
        List<String> propetryValues = new ArrayList<>();
        propetryValues.add(designFinishDate == null ? "" : DateUtils.convertDate(designFinishDate));
        propetryValues.add(constructionFinishDate == null ? "" : DateUtils.convertDate(constructionFinishDate));
        propetryValues.add(supervisorFinishDate == null ? "" : DateUtils.convertDate(supervisorFinishDate));
        propetryValues.add(designPerson == null ? "" : designPerson);
        propetryValues.add(constructionPerson == null ? "" : constructionPerson);
        propetryValues.add(supervisorPerson == null ? "" : supervisorPerson);
        propetryValues.add(tenderSessionId == null ? "" : tenderSessionId);
        return propetryValues;
    }

    public List<String> getListSix() {
        List<String> propetryValues = new ArrayList<>();
        propetryValues.add(contractPrice == null ? "0.00" : CommonUtils.getCommalFormat(new BigDecimal(contractPrice)));
        propetryValues.add(designAmount == null ? "0.00" : CommonUtils.getCommalFormat(new BigDecimal(designAmount)));
        propetryValues.add(superviseAmount == null ? "0.00" : CommonUtils.getCommalFormat(new BigDecimal(superviseAmount)));
        propetryValues.add(constructAmount == null ? "0.00" : CommonUtils.getCommalFormat(new BigDecimal(constructAmount)));
        propetryValues.add(checkAmount == null ? "0.00" : CommonUtils.getCommalFormat(new BigDecimal(checkAmount)));
        propetryValues.add(agencyAmount == null ? "0.00" : CommonUtils.getCommalFormat(new BigDecimal(agencyAmount)));
        propetryValues.add(consultAmount == null ? "0.00" : CommonUtils.getCommalFormat(new BigDecimal(consultAmount)));
        propetryValues.add(otherAmount == null ? "0.00" : CommonUtils.getCommalFormat(new BigDecimal(otherAmount)));
        propetryValues.add(budgetForehead == null ? "0.00" : CommonUtils.getCommalFormat(new BigDecimal(budgetForehead)));
        propetryValues.add(settlementForehead == null ? "0.00" : CommonUtils.getCommalFormat(new BigDecimal(settlementForehead)));
        propetryValues.add(finalEvaluationAmount == null ? "0.00" : CommonUtils.getCommalFormat(new BigDecimal(finalEvaluationAmount)));
        propetryValues.add(isProvincialCapital == null ? "0.00" : isProvincialCapital);
        propetryValues.add(provincialCapital == null ? "0.00" : CommonUtils.getCommalFormat(new BigDecimal(provincialCapital)));
        propetryValues.add(localtionCapital == null ? "0.00" : CommonUtils.getCommalFormat(new BigDecimal(localtionCapital)));
        propetryValues.add(capitalSum == null ? "0.00" : CommonUtils.getCommalFormat(new BigDecimal(capitalSum)));
        propetryValues.add(constructionSessionId == null ? "0.00" : constructionSessionId);
        return propetryValues;
    }

    public List<String> getListEight() {
        List<String> propetryValues = new ArrayList<>();
        propetryValues.add(mainContent == null ? "" : mainContent);
        propetryValues.add(permitFlag == null ? "" : permitFlag);
        propetryValues.add(superviseFlag == null ? "" : superviseFlag);
        propetryValues.add(contractDays == null ? "" : contractDays);
        propetryValues.add(beginDate == null ? "" : DateUtils.convertDate(beginDate));
        propetryValues.add(constructionProgress == null ? "" : constructionProgress);
        propetryValues.add(finishDate == null ? "" : DateUtils.convertDate(finishDate));
        propetryValues.add(acceptSessionId == null ? "" : acceptSessionId);
        return propetryValues;
    }

    public List<String> getListNine() {
        List<String> propetryValues = new ArrayList<>();
        propetryValues.add(dateAccept == null ? "" : dateAccept);
        propetryValues.add(completeAccept == null ? "" : completeAccept);
        propetryValues.add(investSessionId == null ? "" : investSessionId);
        return propetryValues;
    }


    public void getData(Context context, String url, final String type, int pageNo) {
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
                    ProjectManagementG m = gson.fromJson(data, ProjectManagementG.class);
                    presenter.loadDataSuccess(m.rows, type);
                } catch (Exception e) {
                    presenter.loadDataFailed();
                }
            }

            @Override
            public void onFailure(int i, cz.msebera.android.httpclient.Header[] headers, byte[] bytes, Throwable throwable) {
                presenter.loadDataFailed();
            }

        };

        client.get(url + "&sort=fillDate&direction=desc&pager.pageNo=" + pageNo,
                responseHandler);
    }

    public class ProjectManagementG {
        public String pageNo;
        public int totalRows;
        public List<ProjectManagement> rows;
    }
}
