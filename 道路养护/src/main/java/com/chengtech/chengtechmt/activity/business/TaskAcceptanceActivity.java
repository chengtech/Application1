package com.chengtech.chengtechmt.activity.business;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.chengtech.chengtechmt.BaseActivity;
import com.chengtech.chengtechmt.R;
import com.chengtech.chengtechmt.entity.business.TaskAcceptance;
import com.chengtech.chengtechmt.entity.business.TaskRegist;
import com.chengtech.chengtechmt.util.CommonUtils;
import com.chengtech.chengtechmt.util.HttpclientUtil;
import com.chengtech.chengtechmt.util.MyConstants;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * 小修保养作业（专项）验收表
 */
public class TaskAcceptanceActivity extends BaseActivity {
    private TextView routeNamePeg_tv;             //线路、桩号
    private TextView cost_tv;                //造价
    private TextView mainContent_tv;      //主要内容
    private TextView numberApproved_tv;            //数量核定
    private TextView beginDate_tv;                  //开工日期
    private TextView finishDate_tv;                  //完工日期
    private TextView acceptanceNo_tv;             //编号
    private TextView acceptanceDeptName_tv;            //养护作业单位
    private TextView name_tv;            //养护作业名称
    private Button attachment_bt;
    private String sessionId;

    private static final int TASK_ACCEPTANCE = 0x12;
    private String url = MyConstants.PRE_URL + "mt/business/tinkermaintainpatrol/minorrepair/addEditTaskAcceptanceJsonByMobile.action";
//    private SweetAlertDialog sweetAlertDialog;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Gson gson = new Gson();
            String json = (String) msg.obj;
            switch (msg.what) {
                case TASK_ACCEPTANCE:
//                    sweetAlertDialog.dismiss();
                    if (!TextUtils.isEmpty(json)) {
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            if (jsonObject.getBoolean("success")) {
                                JSONObject dataObject = jsonObject.getJSONObject("data");
                                String data = dataObject.toString();
                                TaskAcceptance taskAcceptance = gson.fromJson(data, TaskAcceptance.class);
                                sessionId = taskAcceptance.sessionId;
                                showMessage(taskAcceptance);
                            }
                        } catch (JSONException e) {

                        }
                    }
                    break;
            }
        }
    };

    private void showMessage(TaskAcceptance taskAcceptance) {
        if (taskAcceptance != null) {
            acceptanceDeptName_tv.setText(taskAcceptance.acceptanceDeptName);
            acceptanceNo_tv.setText(taskAcceptance.acceptanceNo);
            name_tv.setText(taskAcceptance.name);
            routeNamePeg_tv.setText(taskAcceptance.routeNamePeg);
            cost_tv.setText(taskAcceptance.cost);
            beginDate_tv.setText(TextUtils.isEmpty(taskAcceptance.beginDate)?"":taskAcceptance.beginDate.substring(0, 10));
            finishDate_tv.setText(TextUtils.isEmpty(taskAcceptance.finishDate)?"":taskAcceptance.finishDate.substring(0, 10));
            mainContent_tv.setText(taskAcceptance.mainContent);
            numberApproved_tv.setText(taskAcceptance.numberApproved);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addContentView(R.layout.activity_task_acceptance);

        setNavigationIcon(true);
        hidePagerNavigation(true);

        toolbar.setTitle("小修保养作业（专项）验收表");
        initView();
        getIntentData();
        getWebData();
    }

    private void initView() {
        acceptanceDeptName_tv = (TextView) findViewById(R.id.tv1);
        acceptanceNo_tv = (TextView) findViewById(R.id.tv2);
        name_tv = (TextView) findViewById(R.id.tv3);
        routeNamePeg_tv = (TextView) findViewById(R.id.tv4);
        cost_tv = (TextView) findViewById(R.id.tv5);
        beginDate_tv = (TextView) findViewById(R.id.tv6);
        finishDate_tv = (TextView) findViewById(R.id.tv7);
        mainContent_tv = (TextView) findViewById(R.id.tv8);
        numberApproved_tv = (TextView) findViewById(R.id.tv9);
        attachment_bt = (Button) findViewById(R.id.attachment_bt);
        attachment_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(sessionId)) {
                    CommonUtils.getAttachmentsFromSessionId(TaskAcceptanceActivity.this, sessionId);
                } else {
                    Toast.makeText(TaskAcceptanceActivity.this, "没有附件", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getWebData() {
//        sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
//        sweetAlertDialog.setContentText("正在加载...").setTitleText("");
//        sweetAlertDialog.show();
        HttpclientUtil.getData(this, url, handler, TASK_ACCEPTANCE);
    }

    public static void startAction(Context context, String maintainTaskItemId, String inOutPlanType) {
        Intent intent = new Intent(context, TaskAcceptanceActivity.class);
        intent.putExtra("maintainTaskItemId", maintainTaskItemId);
        intent.putExtra("inOutPlanType", inOutPlanType);
        context.startActivity(intent);
    }

    private void getIntentData() {
        Intent intent = getIntent();
        String maintainTaskItemId = intent.getStringExtra("maintainTaskItemId");
        String inOutPlanType = intent.getStringExtra("inOutPlanType");
        url = url + "?isRead=true&inOutPlanType=" + inOutPlanType + "&maintainTaskItemId=" + maintainTaskItemId;
    }
}
