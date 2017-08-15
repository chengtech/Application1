package com.chengtech.chengtechmt.activity.business;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;

import com.chengtech.chengtechmt.BaseActivity;
import com.chengtech.chengtechmt.R;
import com.chengtech.chengtechmt.adapter.ImageAddAdapter;
import com.chengtech.chengtechmt.adapter.business.BridgeOftenCheckAddAdapter;
import com.chengtech.chengtechmt.entity.patrol.BriOftenCheck;
import com.chengtech.chengtechmt.thirdlibrary.wxpictureselector.PictureSelectorActivity;
import com.chengtech.chengtechmt.util.HttpclientUtil;
import com.chengtech.chengtechmt.util.LogUtils;
import com.chengtech.chengtechmt.util.MyConstants;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * 桥梁经常检查新增页面
 */
public class BridgeOftenCheckAddActivity extends BaseActivity {
    private String addUrl = MyConstants.PRE_URL + "mt/business/patrol/bricheckmanage/bridgeoftencheck/addEditBridgeOftenCheckMobile.action?";
    private String updateUrl = MyConstants.PRE_URL + "mt/business/patrol/bricheckmanage/bridgeoftencheck/saveOrUpdateBridgeOftenCheckMobile.action";
    private String updatePicture = MyConstants.PRE_URL + "mobileUploader";
    private RecyclerView recyclerView;
    BriOftenCheck briOftenCheck;
    SweetAlertDialog dialog;
    private ArrayList<String> picPaths = new ArrayList<>();


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Gson gson = new Gson();
            String json = (String) msg.obj;
            switch (msg.what) {
                case 0:
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        String data = jsonObject.getString("data");
                        briOftenCheck = gson.fromJson(data, BriOftenCheck.class);

                        BridgeOftenCheckAddAdapter addAdapter = new BridgeOftenCheckAddAdapter(BridgeOftenCheckAddActivity.this,
                                briOftenCheck, picPaths);
                        recyclerView.setLayoutManager(new LinearLayoutManager(BridgeOftenCheckAddActivity.this));
                        recyclerView.setAdapter(addAdapter);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case HttpclientUtil.UPDATE_SUCCESS:
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        String sessionId = jsonObject.getString("sessionId");
                        if (sessionId != null) {
                            if (TextUtils.isEmpty(sessionId)) {
                                if (dialog != null && dialog.isShowing()) {
                                    dialog.setTitleText("保存失败！").changeAlertType(SweetAlertDialog.ERROR_TYPE);
                                }
                            } else {
                                if (briOftenCheck != null) {
                                    briOftenCheck.sessionId = sessionId;
                                }
                                if (dialog != null && dialog.isShowing()) {
                                    dialog.setContentText(dialog.getContentText() + "\n上传附件成功\n正在保存实体类...");
                                    String data = gson.toJson(briOftenCheck, BriOftenCheck.class);
                                    RequestParams params = new RequestParams();
                                    params.put("data", data);
                                    params.put("sessionId", briOftenCheck.sessionId);
                                    HttpclientUtil.postObject(BridgeOftenCheckAddActivity.this, updateUrl, handler, HttpclientUtil.SAVE_SUCCESS, params);
                                }

                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case HttpclientUtil.SAVE_SUCCESS:
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(json);
                        boolean saveSuccess = jsonObject.getBoolean("success");
                        if (saveSuccess) {
                            if (dialog != null && dialog.isShowing()) {
                                dialog.setTitleText("保存成功！").setContentText(dialog.getContentText() + "\n保存实体类成功").changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                            }
                        } else {
                            if (dialog != null && dialog.isShowing()) {
                                dialog.setTitleText("保存失败！").setContentText(dialog.getContentText() + "\n保存实体类失败").changeAlertType(SweetAlertDialog.ERROR_TYPE);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case HttpclientUtil.SAVE_FAILED:
                    if (dialog != null && dialog.isShowing()) {
                        dialog.setTitleText("保存失败！").changeAlertType(SweetAlertDialog.ERROR_TYPE);
                    }
                    break;
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addContentView(R.layout.activity_often_check_add);
        setNavigationIcon(true);
        hidePagerNavigation(true);

        Intent intent = getIntent();
        addUrl = addUrl + intent.getStringExtra("filter");
        toolbar.setTitle("桥梁新增记录表");
        initView();

        HttpclientUtil.getData(this, addUrl, handler, 0, true);

    }

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuId = item.getItemId();
        if (menuId == R.id.save) {
// TODO: 2017/5/16
//保存该新增的实体类到本地持久化
            final ArrayList<BriOftenCheck> briOftenCheckMap = new ArrayList<>();
            briOftenCheckMap.add(briOftenCheck);
            dialog = new SweetAlertDialog(BridgeOftenCheckAddActivity.this, SweetAlertDialog.PROGRESS_TYPE);
            dialog.setTitleText("正在执行保存操作....");
            dialog.setContentText("正在上传..");
            dialog.show();
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    int resultCode = ObjectSaveUtils.saveObject(BridgeOftenCheckAddActivity.this, "BridgeOftenCheckList", briOftenCheckMap);
//                    if (resultCode == -1) {
//                        handler.sendEmptyMessageDelayed(SAVE_SUCCESS,2000);
//                    } else {
//                        handler.sendEmptyMessage(SAVE_FAILED);
//                    }
//                }
//            }).start();

            //上传到服务器
            RequestParams params = new RequestParams();
            if (picPaths != null && picPaths.size() > 0) {
                try {
                    for (int i = 0; i < picPaths.size(); i++) {
                        File file = new File(picPaths.get(i));
                        if (file.exists()) {
                            params.put("attachment" + (i + 1), file);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                params.put("modelDir", "BridgeOftenCheck");
                params.put("sessionId", briOftenCheck.sessionId == null ? "" : briOftenCheck.sessionId);
                params.put("filename", "");
                HttpclientUtil.postObject(this, updatePicture, handler, HttpclientUtil.UPDATE_SUCCESS, params);
            }else{
                Gson gson = new Gson();
                String data = gson.toJson(briOftenCheck,BriOftenCheck.class);
                params.put("data", data);
                params.put("sessionId", briOftenCheck.sessionId);
                HttpclientUtil.postObject(BridgeOftenCheckAddActivity.this, updateUrl, handler, HttpclientUtil.SAVE_SUCCESS, params);
            }
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_bri_often_check_add, menu);
        return true;
    }

    public static void startAction(Context context, String filter) {
        Intent intent = new Intent(context, BridgeOftenCheckAddActivity.class);
        intent.putExtra("filter", filter);
        context.startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0x00 && resultCode == RESULT_OK && data != null) {
            picPaths.clear();
            ArrayList<String> picturePath = data.getStringArrayListExtra("PicturePath");
            for (int i = 0; i < picturePath.size(); i++) {
                picPaths.add(picturePath.get(i));
            }
            BridgeOftenCheckAddAdapter adapter = (BridgeOftenCheckAddAdapter) recyclerView.getAdapter();
            ImageAddAdapter imageAdapter = adapter.getImageAdapter();
            if (imageAdapter != null)
                imageAdapter.notifyDataSetChanged();
        }
    }
}
