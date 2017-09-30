package com.chengtech.chengtechmt.entity;

import android.content.Context;
import android.text.TextUtils;

import com.chengtech.chengtechmt.presenter.Presenter;
import com.chengtech.chengtechmt.util.HttpclientUtil;
import com.chengtech.chengtechmt.util.MyConstants;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者: LiuFuYingWang on 2016/10/26 13:41.
 * 附件类
 */

public class
Attachment {

    public String id;
    public String filePath;
    public String fileName;
    public String size;
    private Presenter presenter;
    public String url = MyConstants.PRE_URL + "ms/sys/attachment/listAttachmentJsonBySessionId.action?sessionId=";

    public Attachment(String fileName, String filePath, String id) {
        this.filePath = filePath;
        this.fileName = fileName;
        this.id = id;
    }

    public Attachment() {
    }

    public Attachment(Presenter presenter) {
        this.presenter = presenter;
    }

    public void getData(Context context, String sessionId) {
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
                    if (TextUtils.isEmpty(data)) {
                        presenter.loadDataSuccess(new ArrayList<Attachment>(), "Attachment");
                        return;
                    }
                    JSONArray array = new JSONArray(data);
                    List<Attachment> attachmentList = new ArrayList<>();
                    for (int j = 0; j < array.length(); j++) {
                        JSONObject object = array.getJSONObject(j);
                        attachmentList.add(new Attachment(object.getString("fileName"),
                                object.getString("filePath"),
                                object.getString("id")));
                    }
                    presenter.loadDataSuccess(attachmentList, "Attachment");

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
        client.get(url + sessionId,
                responseHandler);
    }

}
