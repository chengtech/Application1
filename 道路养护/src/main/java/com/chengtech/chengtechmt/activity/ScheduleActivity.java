package com.chengtech.chengtechmt.activity;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

import com.chengtech.chengtechmt.R;
import com.chengtech.chengtechmt.entity.FlowUserHandleInfo;
import com.chengtech.chengtechmt.entity.gson.FlowUserHandlerG;
import com.chengtech.chengtechmt.util.ACache;
import com.chengtech.chengtechmt.util.AppAccount;
import com.chengtech.chengtechmt.util.HttpclientUtil;
import com.chengtech.chengtechmt.util.MyConstants;
import com.chengtech.chengtechmt.util.MyDialogUtil;
import com.chengtech.chengtechmt.util.UserUtils;
import com.chengtech.chengtechmt.view.TitleLayout;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.shelwee.uilistview.model.BasicItem;
import com.shelwee.uilistview.ui.UiListView;

import org.apache.http.Header;

import java.io.UnsupportedEncodingException;
import java.util.List;

public class ScheduleActivity extends Activity {
    protected static final int SAVE_DATA = 1;
    private TitleLayout layout;
    private UiListView uiListView;
    private Dialog loadDialog;
    private ACache aCache;
    private int refreshCount = 0;
    private Handler handler = new Handler(){

        public void handleMessage(android.os.Message msg) {
            String json = (String) msg.obj;
            Gson gson = new Gson();
            if (!TextUtils.isEmpty(json)) {
                try {
                    FlowUserHandlerG handlerG = gson.fromJson(json,FlowUserHandlerG.class);
                    List<FlowUserHandleInfo> flowUserHandleInfo = handlerG.data;
                    if (flowUserHandleInfo!=null && flowUserHandleInfo.size()>0) {
                        for (FlowUserHandleInfo f : flowUserHandleInfo) {
                            String title = MyConstants.flowUserHandlerDict.get(f.className.toLowerCase());
                            BasicItem basicItem = null;
                            if (title==null) {
                                basicItem = new BasicItem("[未配置]",f.name);
                            }else {
                                basicItem = new BasicItem("["+title+"]",f.name,Color.parseColor("#00BFFF"));
                            }
                            uiListView.addBasicItem(basicItem);
                        }
                        uiListView.commit();
//						if (msg.arg1==SAVE_DATA) {
//							aCache.put("ScheduleActivity", json,ACache.TIME_HOUR);
//						}
                        loadDialog.dismiss();
                    }

                } catch (Exception e) {
                    UserUtils.reLogin(ScheduleActivity.this,loadDialog);
                }
            }else {
                loadDialog.dismiss();
            }
        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_schedule);

        initView();
        getData();

//		String json = aCache.getAsString("ScheduleActivity");
//		if (TextUtils.isEmpty(json)) {
//		}else {
//			Message mes = new Message();
//			mes.obj = json;
//			handler.sendMessage(mes);
//		}


    }

    private void getData() {
        AsyncHttpClient client = HttpclientUtil.getInstance(this);
        AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler(){
            @Override
            public void onStart() {
                loadDialog.show();
                super.onStart();
            }

            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {

                try {
                    String data = new String(arg2,"utf-8");
                    Message message = new Message();
                    message.obj = data;
//					message.arg1 = SAVE_DATA;
                    handler.sendMessage(message);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                super.onSuccess(arg0, arg1, arg2);
            }
            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2,
                                  Throwable arg3) {
                loadDialog.dismiss();
                Toast.makeText(ScheduleActivity.this, "服务器断开连接",Toast.LENGTH_SHORT).show();
                super.onFailure(arg0, arg1, arg2, arg3);
            }
        };
        client.get(MyConstants.FLOW_HANDLER_URL+AppAccount.userId, responseHandler);
    }

    private void initView() {
        layout = (TitleLayout) findViewById(R.id.mytitle);
        layout.setTitle("待办事项");
        uiListView = (UiListView) findViewById(R.id.uilist);
        loadDialog = MyDialogUtil.createDialog(ScheduleActivity.this, "正在加载..");
        aCache = ACache.get(this);
    }


}
