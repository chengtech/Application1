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
import com.chengtech.chengtechmt.entity.business.TaskRegist;
import com.chengtech.chengtechmt.util.CommonUtils;
import com.chengtech.chengtechmt.util.DateUtils;
import com.chengtech.chengtechmt.util.HttpclientUtil;
import com.chengtech.chengtechmt.util.MyConstants;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * 小修保养作业记录表
 */
public class TaskRegistActivity extends BaseActivity {
    private static final int TASK_REGIST = 0x11;
    private TextView routeNamePeg_tv;             //线路、桩号
    private TextView workBasis_tv;                //作业依据
    private TextView implementationPlans_tv;      //实施方案及作业内容要点
    private TextView approvedSheet_tv;            //数量现场核定表（桩号、计算过程、简图及工料机消耗，可附页）
    private TextView checkDate_tv;                  //日期
    private TextView taskRecordNo_tv;             //编号
    private TextView thirdDeptName_tv;            //养护作业单位
    private TextView name_tv;            //养护作业名称
    private Button attachment_bt;
    private String sessionId;

//    private SweetAlertDialog sweetAlertDialog;
    private String url = MyConstants.PRE_URL + "mt/business/tinkermaintainpatrol/minorrepair/addEditTaskRegistJsonByMobile.action";

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Gson gson = new Gson();
            String json = (String) msg.obj;
            switch (msg.what) {
                case TASK_REGIST:
//                    sweetAlertDialog.dismiss();
                    if (!TextUtils.isEmpty(json)) {
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            if (jsonObject.getBoolean("success")) {
                                JSONObject dataObject = jsonObject.getJSONObject("data");
                                String data = dataObject.toString();
                                TaskRegist taskRegist = gson.fromJson(data, TaskRegist.class);
                                sessionId = taskRegist.sessionId;
                                showMessage(taskRegist);
                            }
                        } catch (JSONException e) {

                        }
                    }
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addContentView(R.layout.activity_task_regist);

        setNavigationIcon(true);
        hidePagerNavigation(true);

        toolbar.setTitle("小修保养作业记录表");
        initView();
        getIntentData();
        getWebData();
    }

    private void initView() {
        thirdDeptName_tv = (TextView) findViewById(R.id.tv1);
        taskRecordNo_tv = (TextView) findViewById(R.id.tv2);
        name_tv = (TextView) findViewById(R.id.tv3);
        routeNamePeg_tv = (TextView) findViewById(R.id.tv4);
        workBasis_tv = (TextView) findViewById(R.id.tv5);
        implementationPlans_tv = (TextView) findViewById(R.id.tv6);
        approvedSheet_tv = (TextView) findViewById(R.id.tv7);
        checkDate_tv = (TextView) findViewById(R.id.tv9);
        attachment_bt = (Button) findViewById(R.id.attachment_bt);
        attachment_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(sessionId)) {
                    CommonUtils.getAttachmentsFromSessionId(TaskRegistActivity.this, sessionId);
                } else {
                    Toast.makeText(TaskRegistActivity.this, "没有附件", Toast.LENGTH_SHORT).show();
                }
            }
        });
//        routeNamePeg_tv = (TextView) findViewById(R.id.tv9);

    }

    public static void startAction(Context context, String maintainTaskItemId, String inOutPlanType) {
        Intent intent = new Intent(context, TaskRegistActivity.class);
        intent.putExtra("maintainTaskItemId", maintainTaskItemId);
        intent.putExtra("inOutPlanType", inOutPlanType);
        context.startActivity(intent);
    }

    private void getWebData() {
//        sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
//        sweetAlertDialog.setContentText("正在加载...").setTitleText("");
//        sweetAlertDialog.show();
        HttpclientUtil.getData(this, url, handler, TASK_REGIST);
    }

    private void getIntentData() {
        Intent intent = getIntent();
        String maintainTaskItemId = intent.getStringExtra("maintainTaskItemId");
        String inOutPlanType = intent.getStringExtra("inOutPlanType");
        url = url + "?isRead=true&inOutPlanType=" + inOutPlanType + "&maintainTaskItemId=" + maintainTaskItemId;
    }

    private void showMessage(TaskRegist taskRegist) {
        if (taskRegist != null) {
            thirdDeptName_tv.setText(taskRegist.thirdDeptName);
            taskRecordNo_tv.setText(taskRegist.taskRecordNo);
            name_tv.setText(taskRegist.name);
            routeNamePeg_tv.setText(taskRegist.routeNamePeg);
            workBasis_tv.setText(taskRegist.workBasis);
            implementationPlans_tv.setText(taskRegist.implementationPlans);
            approvedSheet_tv.setText(taskRegist.approvedSheet);
            checkDate_tv.setText(TextUtils.isEmpty(taskRegist.checkDate)?"":taskRegist.checkDate.substring(0, 10));
        }
    }
}
