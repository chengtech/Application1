package com.chengtech.chengtechmt.activity;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.chengtech.chengtechmt.BaseActivity;
import com.chengtech.chengtechmt.R;
import com.chengtech.chengtechmt.adapter.AboutMeExpandAdapter;
import com.chengtech.chengtechmt.entity.User;
import com.chengtech.chengtechmt.entity.gson.UserG;
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
import com.shelwee.uilistview.ui.UiListView;

import org.apache.http.Header;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AboutMeActivity extends BaseActivity {

    private UiListView uiListView;
    private ExpandableListView expandableListView;
    private String aboutMeUrl = MyConstants.PRE_URL + "ms/sys/user/addEditUser.action?mobile=phone&id=";
    private int refreshCount = 0;
    private String[] groupName = new String[]{"基本信息", "联系方式", "其他信息"};
    private Map<Integer, List<String>> titleData;
    private Map<Integer, List<String>> contentData;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            String json = (String) msg.obj;
            Gson gson = new Gson();
            try {
                UserG userG = gson.fromJson(json, UserG.class);
                User user = userG.data;
                if (user != null) {
//                    uiListView.addBasicItem("\t用户名：" + (user.name == null ? "" : user.name));
//                    uiListView.addBasicItem("\t账号：" + (user.userAccount == null ? "" : user.userAccount));
//                    uiListView.addBasicItem("\t员工编号：" + (user.userNo == null ? "" : user.userNo));
//                    uiListView.addBasicItem("取回密码问题：" + (user.question == null ? "" : user.question));
//                    uiListView.addBasicItem("取回密码答案：" + (user.answer == null ? "" : user.answer));
//                    uiListView.addBasicItem("性别：" + (user.sex.equals("1") ? "男" : "女"));
//                    uiListView.addBasicItem("职位：" + (user.jobPosition == null ? "" : user.jobPosition));
//                    uiListView.addBasicItem("出生年月：" + (user.birthday == null ? "" : user.birthday));
//                    uiListView.addBasicItem("移动电话：" + (user.mobile == null ? "" : user.mobile));
//                    uiListView.addBasicItem("住宅地址：" + (user.homeAddress == null ? "" : user.homeAddress));
//                    uiListView.addBasicItem("邮箱：" + (user.email == null ? "" : user.email));
//                    uiListView.addBasicItem("最后登陆时间：" + (user.lastLoginTime == null ? "" : user.lastLoginTime));
//                    uiListView.commit();
                    loadDialog.dismiss();

                    titleData.put(0, user.getTitleA());
                    titleData.put(1, user.getTitleB());
                    titleData.put(2, user.getTitleC());
                    contentData.put(0, user.getContentA());
                    contentData.put(1, user.getContentB());
                    contentData.put(2, user.getContentC());
                    AboutMeExpandAdapter adapter = new AboutMeExpandAdapter(AboutMeActivity.this, groupName, titleData, contentData);
                    expandableListView.setAdapter(adapter);
                    expandableListView.setGroupIndicator(null);
                    Drawable drawable = getResources().getDrawable(R.drawable.lines);
//                    expandableListView.setDivider(drawable);
//                    expandableListView.setChildDivider(drawable);
//                    expandableListView.setDividerHeight(2);
//                    expandableListView.setChildIndicator(null);
                    expandableListView.expandGroup(0);
                    expandableListView.expandGroup(1);
                    expandableListView.expandGroup(2);
                    expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                        @Override
                        public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                            return true;
                        }
                    });
//                    aCache.put(AppAccount.userId, user, ACache.TIME_DAY);


                }
            } catch (Exception e) {
                UserUtils.reLogin(AboutMeActivity.this, loadDialog);
            }
        }

        ;
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        addContentView(R.layout.activity_about_me);

        setNavigationIcon(true);
        hidePagerNavigation(true);
        initView();
        //User aboutMe = (User) aCache.getAsObject(AppAccount.userId);
        //if (aboutMe==null) {
        getData();
        //}else {
//            Message message = new Message();
//            message.obj = aboutMe;
//            handler.sendMessage(message);
//       }

    }

    private void getData() {
        AsyncHttpClient client = HttpclientUtil.getInstance(this);
        AsyncHttpResponseHandler asyncHttpResponseHandler = new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                loadDialog.show();
                super.onStart();
            }

            @Override
            public void onSuccess(int i, cz.msebera.android.httpclient.Header[] headers, byte[] bytes) {
                loadDialog.dismiss();
                try {
                    String data = new String(bytes, "utf-8");
                    Message message = new Message();
                    message.obj = data;
                    handler.sendMessage(message);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int i, cz.msebera.android.httpclient.Header[] headers, byte[] bytes, Throwable throwable) {
                loadDialog.dismiss();
                Toast.makeText(AboutMeActivity.this, "服务器断开连接", Toast.LENGTH_SHORT).show();
            }


        };

        client.get(aboutMeUrl + AppAccount.userId, asyncHttpResponseHandler);

    }

    private void initView() {
        uiListView = (UiListView) findViewById(R.id.uilist);
        expandableListView = (ExpandableListView) findViewById(R.id.expandlist);
        titleData = new HashMap<>();
        contentData = new HashMap<>();

    }

    @Override
    protected void onResume() {
        super.onResume();
        toolbar.setTitle("个人信息");
    }
}
