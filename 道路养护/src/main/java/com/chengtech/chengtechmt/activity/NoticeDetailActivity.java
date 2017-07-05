package com.chengtech.chengtechmt.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.chengtech.chengtechmt.R;
import com.chengtech.chengtechmt.entity.AccessRecord;
import com.chengtech.chengtechmt.entity.SysBulletin;
import com.chengtech.chengtechmt.entity.gson.AccessRecordG;
import com.chengtech.chengtechmt.util.ACache;
import com.chengtech.chengtechmt.util.HttpclientUtil;
import com.chengtech.chengtechmt.util.LogUtils;
import com.chengtech.chengtechmt.util.MyConstants;
import com.chengtech.chengtechmt.util.MyDialogUtil;
import com.chengtech.chengtechmt.util.UserUtils;
import com.chengtech.chengtechmt.view.TitleLayout;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NoticeDetailActivity extends Activity {

    private TitleLayout layout;
    private Dialog loadDialog;
    private ACache aCache;
    private SysBulletin sysBulletin;
    private int refreshCount = 0;
    private TextView subject_tv, urgency_tv, attachment_tv, content_tv;
    private ListView listView;
    private SimpleAdapter adapter;
    private ScrollView sv;
    private List<Map<String, Object>> data;

    private Handler handler = new Handler() {

        public void handleMessage(android.os.Message msg) {
            String json = (String) msg.obj;
            LogUtils.i(json);
            Gson gson = new Gson();
            if (!TextUtils.isEmpty(json)) {
                try {
                    AccessRecordG accessRecordG = gson.fromJson(json,
                            AccessRecordG.class);
                    List<AccessRecord> records = accessRecordG.data;
                    if (records != null && records.size() > 0) {
                        for (AccessRecord a : records) {
                            Map<String,Object> map = new HashMap<String, Object>();
                            map.put("name", a.userName);
                            map.put("time", a.accessTime);
                            data.add(map);
                        }
                        adapter = new SimpleAdapter(NoticeDetailActivity.this,
                                data, R.layout.item_noticedetail_listview, new String[]{"name","time"}, new int[]{R.id.noticedetail_username,R.id.noticedetail_time});
                        setListViewHeight(listView, adapter, records.size());
                        //srcollview滑动到顶端，必须使用post方法，放到消息队列。
                        handler.post(new Runnable() {

                            @Override
                            public void run() {
                                sv.fullScroll(ScrollView.FOCUS_UP);

                            }
                        });
                        listView.setAdapter(adapter);
                    }
                    loadDialog.dismiss();
                } catch (Exception e) {
                    UserUtils.reLogin(NoticeDetailActivity.this,loadDialog);
                }
            } else {
                loadDialog.dismiss();
            }
        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_notice_detail);

        initView();
        Intent intent = getIntent();
        sysBulletin = (SysBulletin) intent.getSerializableExtra("data");

        subject_tv.setText(sysBulletin.subject == null ? ""
                : sysBulletin.subject);
        urgency_tv.setText(sysBulletin.urgency == null ? ""
                : sysBulletin.urgency);
        if ("紧急".equals(sysBulletin.urgency)) {
            urgency_tv.setTextColor(Color.RED);
        }
        content_tv.setText(sysBulletin.content == null ? ""
                : sysBulletin.content);

        getData();
    }

    private void getData() {
        AsyncHttpClient client = HttpclientUtil.getInstance(this);
        AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                loadDialog.show();
                super.onStart();
            }

            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {

                try {
                    String data = new String(arg2, "utf-8");
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
                Toast.makeText(NoticeDetailActivity.this, "服务器断开连接",
                        Toast.LENGTH_SHORT).show();
                super.onFailure(arg0, arg1, arg2, arg3);
            }
        };
        client.get(MyConstants.SYSBULLETIN_DETAIL_URL+sysBulletin.id, responseHandler);

    }

    private void initView() {
        loadDialog = MyDialogUtil.createDialog(NoticeDetailActivity.this,
                "正在加载..");
        aCache = ACache.get(this);

        layout = (TitleLayout) findViewById(R.id.mytitle);
        layout.setTitle("公告公示");

        subject_tv = (TextView) findViewById(R.id.noticedetail_subject);
        urgency_tv = (TextView) findViewById(R.id.noticedetail_urgency);
        attachment_tv = (TextView) findViewById(R.id.noticedetail_attachment);
        content_tv = (TextView) findViewById(R.id.noticedetail_content);

        listView = (ListView) findViewById(R.id.noticedetail_listview_1);
        data = new ArrayList<Map<String, Object>>();
        sv = (ScrollView) findViewById(R.id.noticedetail_scrollview);

        //解决scrollview和listview
        listView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP || event.getAction()==MotionEvent.ACTION_MOVE){
                    sv.requestDisallowInterceptTouchEvent(false);
                }else{
                    sv.requestDisallowInterceptTouchEvent(true);
                }
                return false;
            }

        });
    }

    /**
     * 动态设置listView的高度
     * count 总条目
     */
    private void setListViewHeight(ListView listView, BaseAdapter adapter,
                                   int count) {
        int totalHeight = 0;
        for (int i = 0; i < count; i++) {
            View listItem = adapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * count);
        listView.setLayoutParams(params);
    }

}
