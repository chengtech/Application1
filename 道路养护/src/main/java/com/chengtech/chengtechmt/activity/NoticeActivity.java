package com.chengtech.chengtechmt.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.widget.Toast;

import com.chengtech.chengtechmt.R;
import com.chengtech.chengtechmt.entity.SysBulletin;
import com.chengtech.chengtechmt.entity.gson.SysBulletinG;
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
import com.shelwee.uilistview.ui.UiListView.ClickListener;

import org.apache.http.Header;

import java.io.UnsupportedEncodingException;
import java.util.List;

public class NoticeActivity extends Activity {

    private TitleLayout layout;
    private UiListView uiListView;
    private Dialog loadDialog;
    private ACache aCache;
    private List<SysBulletin> sysBulletins;
    private int refreshCount = 0;
    private Handler handler = new Handler(){

        public void handleMessage(android.os.Message msg) {
            String json = (String) msg.obj;
            Gson gson = new Gson();
            if (!TextUtils.isEmpty(json)) {
                try {
                    SysBulletinG sysBulletinG = gson.fromJson(json, SysBulletinG.class);
                    sysBulletins = sysBulletinG.rows;
                    for (SysBulletin s : sysBulletins) {
                        String readUser = s.userClick;
                        if (readUser!=null && readUser.contains(AppAccount.userId)) {
                            BasicItem basicItem = null;
                            if ("普通".equals(s.urgency)) {
                                String content = "主题："+s.subject;
                                SpannableStringBuilder builder = new SpannableStringBuilder(content);
                                basicItem = new BasicItem(R.mipmap.all_email, "", builder);
                            }else  {
                                String content = "(紧急)主题："+s.subject;
                                SpannableStringBuilder builder = new SpannableStringBuilder(content);
                                builder.setSpan(new ForegroundColorSpan(Color.RED), 1, 3, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                                basicItem = new BasicItem(R.mipmap.all_email, "", builder);
                            }

                            uiListView.addBasicItem(basicItem);
                        }else {
                            BasicItem basicItem = null;
                            if ("普通".equals(s.urgency)) {
                                String content = "[未读]主题："+s.subject;
                                SpannableStringBuilder builder = new SpannableStringBuilder(content);
                                builder.setSpan(new ForegroundColorSpan(Color.BLUE), 1, 3, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                                basicItem = new BasicItem(R.mipmap.all_email, "", builder);
                            }else  {
                                String content = "[未读]主题："+s.subject+"(紧急)";
                                SpannableStringBuilder builder = new SpannableStringBuilder(content);
                                builder.setSpan(new ForegroundColorSpan(Color.RED), content.length()-3, content.length()-1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                                builder.setSpan(new ForegroundColorSpan(Color.BLUE), 1, 3, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                                basicItem = new BasicItem(R.mipmap.all_email, "", builder);
                            }
                            uiListView.addBasicItem(basicItem);
                        }
                    }
                    uiListView.commit();
                    loadDialog.dismiss();
                } catch (Exception e) {
                    UserUtils.reLogin(NoticeActivity.this,loadDialog);
                }

            }
            else {
                loadDialog.dismiss();
            }

        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_notice);


        initView();
        uiListView.setClickListener(new ClickListener() {

            @Override
            public void onClick(int index) {
                Intent intent = new Intent(NoticeActivity.this,NoticeDetailActivity.class);
                intent.putExtra("data", sysBulletins.get(index));
                startActivity(intent);
            }
        });
        getData();

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
                Toast.makeText(NoticeActivity.this, "服务器断开连接",Toast.LENGTH_SHORT).show();
                super.onFailure(arg0, arg1, arg2, arg3);
            }
        };
        client.get(MyConstants.SYSBULLETIN_URL, responseHandler);
    }

    private void initView() {
        uiListView = (UiListView) findViewById(R.id.uilist);

        loadDialog = MyDialogUtil.createDialog(NoticeActivity.this, "正在加载..");
        aCache = ACache.get(this);

        layout = (TitleLayout) findViewById(R.id.mytitle);
        layout.setTitle("公告公示");

    }


}
