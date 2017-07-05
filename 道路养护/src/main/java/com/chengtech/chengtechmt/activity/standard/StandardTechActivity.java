package com.chengtech.chengtechmt.activity.standard;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chengtech.chengtechmt.BaseActivity;
import com.chengtech.chengtechmt.R;
import com.chengtech.chengtechmt.activity.dbm.DetailActivity;
import com.chengtech.chengtechmt.activity.dbm.OnePictureDisplayActivity;
import com.chengtech.chengtechmt.adapter.AttachmentAdapter;
import com.chengtech.chengtechmt.divider.RecycleViewDivider;
import com.chengtech.chengtechmt.entity.Attachment;
import com.chengtech.chengtechmt.entity.attachment.AttachmentInfo;
import com.chengtech.chengtechmt.impl.OnItemClickListener;
import com.chengtech.chengtechmt.util.CommonUtils;
import com.chengtech.chengtechmt.util.HttpclientUtil;
import com.chengtech.chengtechmt.util.MyConstants;
import com.chengtech.chengtechmt.util.RealmUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

public class StandardTechActivity extends BaseActivity implements OnItemClickListener {
    private RecyclerView recyclerView;
    private TextView title_tv, memo_tv;
    private String id;
    private String type;
    List<Attachment> attachmentList;
    private String url = "ms/sys/attachment/listTechAttachmentJsonByMobile.action";
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String json = (String) msg.obj;
            try {
                JSONObject jsonObject = new JSONObject(json);
                JSONObject data = jsonObject.getJSONObject("data");
                if (data.has("name")) {
                    String title = data.getString("name");
                    title_tv.setText(title);
                }
                if (data.has("memo")){
                    String memo = data.getString("memo");
                    memo_tv.setText(memo);
                }
                JSONArray attachments = jsonObject.getJSONArray("attachments");
                attachmentList = new ArrayList<>();
                if (attachments != null && attachments.length() > 0) {
                    for (int i = 0; i < attachments.length(); i++) {
                        JSONObject attachmentsJSONObject = attachments.getJSONObject(i);
                        Attachment attachment = new Attachment();
                        attachment.id = attachmentsJSONObject.getString("id");
                        attachment.fileName = attachmentsJSONObject.getString("originalFileName");
                        attachment.filePath = attachmentsJSONObject.getString("filePath") + attachmentsJSONObject.getString("fileName");
                        attachment.size = CommonUtils.ByteConversionGBMBKB((int) attachmentsJSONObject.getInt("fileSize"));
                        attachmentList.add(attachment);
                    }
                }
                AttachmentAdapter adapter = new AttachmentAdapter(StandardTechActivity.this, attachmentList);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(StandardTechActivity.this));
                adapter.setOnItemClickListener(StandardTechActivity.this);
                recyclerView.addItemDecoration(new RecycleViewDivider(StandardTechActivity.this, LinearLayout.VERTICAL));


            } catch (Exception e) {
                Toast.makeText(StandardTechActivity.this, "解析出错", Toast.LENGTH_SHORT).show();
            }
        }
    };

    public StandardTechActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addContentView(R.layout.activity_standard_tech);

        setNavigationIcon(true);
        hidePagerNavigation(true);

        String title = (String) toolbar.getTitle();
        initView();
//        List<Attachment> data = new ArrayList<>();
//        for (int i = 0; i < 2; i++) {
//            Attachment attachment = new Attachment();
//            attachment.fileName = "研究所的日常生产记录表-"+i+".pdf";
//            attachment.size = (i*1024+1024)+"";
//            data.add(attachment);
//        }
//        AttachmentAdapter adapter = new AttachmentAdapter(this, data);
//        recyclerView.setAdapter(adapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        adapter.setOnItemClickListener(this);
//        recyclerView.addItemDecoration(new RecycleViewDivider(this, LinearLayout.VERTICAL));

        if (title.contains("公路技术")) {
            id = "40288a3746418fbb014641b7b7930042";
            type = "routeTech";
        } else if (title.contains("桥梁")) {
            id="40288a9749a1a1120149a1a2b411000f";
            type="bridgeTech";
        } else if (title.contains("桥涵")) {
            id="297ec33949b8f0d00149b8fea138022a";
            type="culvertTech";
        } else if (title.contains("隧道")) {
            id="40288a6649c7cf540149c7d906d0001a";
            type="tunnelTech";
        }

        HttpclientUtil.getData(this, MyConstants.PRE_URL + url + "?id=" + id + "&type=" + type,
                handler, 0);


    }

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        title_tv = (TextView) findViewById(R.id.title);
        memo_tv = (TextView) findViewById(R.id.memo);
    }

    @Override
    public void onItemClick(View view, int position) {
        try {
            final Realm realm = RealmUtil.getInstance(this);
            String ids = attachmentList.get(position).id;
            String fileName = attachmentList.get(position).fileName;
            String filePath = MyConstants.PRE_URL + attachmentList.get(position).filePath;
            AttachmentInfo attachmentInfo = realm.where(AttachmentInfo.class).equalTo("id", ids).findFirst();
            if (fileName.contains(".jpg") || fileName.contains(".png") || fileName.contains(".jpeg")) {
                Intent intent = new Intent(this, OnePictureDisplayActivity.class);
                intent.putExtra("url", filePath);
                startActivity(intent);
            } else if (attachmentInfo == null) {
                //使用自带的下载器下载文件
                CommonUtils.downFile(this, filePath, fileName, ids);
            } else {
                CommonUtils.openFile(this, attachmentInfo.getFilePath());
            }
            realm.close();
        } catch (Exception e) {
            Toast.makeText(this, "数据库查询出错。", Toast.LENGTH_SHORT).show();

        }

    }
}
