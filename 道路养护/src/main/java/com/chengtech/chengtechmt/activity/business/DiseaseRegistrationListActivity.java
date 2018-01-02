package com.chengtech.chengtechmt.activity.business;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.chengtech.chengtechmt.R;
import com.chengtech.chengtechmt.adapter.business.DiseaseRegAdapter;
import com.chengtech.chengtechmt.entity.business.DiseaseRegistration;
import com.chengtech.chengtechmt.fragment.FellowMenDialogFragment;
import com.chengtech.chengtechmt.util.HttpclientUtil;
import com.chengtech.chengtechmt.util.MyConstants;
import com.chengtech.chengtechmt.util.ObjectSaveUtils;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class DiseaseRegistrationListActivity extends AppCompatActivity implements FellowMenDialogFragment.OnDismissListener {
    public static final String DISEASE_REGISTRATION_LIST = "disease_registration_list";
    private Toolbar toolbar;
    private FloatingActionButton floatingActionButton;
    private RecyclerView recyclerView;
    private List<DiseaseRegistration> data;
    private DiseaseRegAdapter diseaseRegAdapter;
    private RelativeLayout edit_layout;
    private String updateUrl = MyConstants.PRE_URL + "mt/business/tinkermaintainpatrol/diseaserecord/saveOrUpdateDiseaseRecordMobile.action";
    private String updatePicture = MyConstants.PRE_URL + "mobileUploader";
    private SweetAlertDialog dialog;
    private DiseaseRegistration currentDisReg;
    private int currentUploadPosition;
    private boolean isModifed;  //被修改过的
    private List checkedIndex;
    private int count;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Gson gson = new Gson();
            String json = (String) msg.obj;
            switch (msg.what) {
                case HttpclientUtil.UPDATE_SUCCESS:
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        String sessionId = jsonObject.getString("sessionId");
                        String path = jsonObject.getString("path");
                        if (sessionId != null) {
                            if (TextUtils.isEmpty(sessionId)) {
                                if (dialog != null && dialog.isShowing()) {
                                    dialog.setTitleText("保存失败！").changeAlertType(SweetAlertDialog.ERROR_TYPE);
                                }
                            } else {
                                if (currentDisReg != null) {
                                    currentDisReg.sessionId = sessionId;
                                    currentDisReg.path = path;
                                }
                                if (dialog != null && dialog.isShowing()) {
                                    dialog.setContentText(dialog.getContentText() + "\n上传附件成功\n正在保存实体类...");
                                    String data = gson.toJson(currentDisReg, DiseaseRegistration.class);
                                    RequestParams params = new RequestParams();
                                    params.put("data", data);
                                    params.put("sessionId", currentDisReg.sessionId);
                                    HttpclientUtil.postObject(DiseaseRegistrationListActivity.this, updateUrl, handler, HttpclientUtil.SAVE_SUCCESS, params);
                                }

                            }
                        }

                    } catch (JSONException e) {
                        if (dialog != null && dialog.isShowing()) {
//                            dialog.setTitleText("json解析失败").setContentText(e.toString()).changeAlertType(SweetAlertDialog.ERROR_TYPE);
                            dialog.setTitleText("网络信号差，上传不成功。").setContentText(e.toString()).changeAlertType(SweetAlertDialog.ERROR_TYPE);
                        }
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
                                data.get(currentUploadPosition).isUpload = true;
                                diseaseRegAdapter.notifyDataSetChanged();
                                isModifed = true;
                                checkedIndex.remove(0);
                                if (checkedIndex.size() > 0) {
                                    dialog.dismiss();
                                    count++;
                                    upLoadData(checkedIndex);

                                }
                            }
                        } else {
                            if (dialog != null && dialog.isShowing()) {
                                dialog.setTitleText("保存失败！").setContentText(dialog.getContentText() + "\n保存实体类失败").changeAlertType(SweetAlertDialog.ERROR_TYPE);
                            }
                        }
                    } catch (JSONException e) {
                        if (dialog != null && dialog.isShowing()) {
//                            dialog.setTitleText("json解析失败").setContentText(e.toString()).changeAlertType(SweetAlertDialog.ERROR_TYPE);
                            dialog.setTitleText("网络信号差，上传不成功。").setContentText(e.toString()).changeAlertType(SweetAlertDialog.ERROR_TYPE);
                        }
                    }
                    break;
                case HttpclientUtil.SAVE_FAILED:
                    if (dialog != null && dialog.isShowing()) {
                        dialog.setTitleText("保存失败！").setContentText("").changeAlertType(SweetAlertDialog.ERROR_TYPE);
                    }
                    break;
            }
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (isModifed) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ObjectSaveUtils.saveObject(DiseaseRegistrationListActivity.this, DISEASE_REGISTRATION_LIST, data);
                    }
                }).start();
            }
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disease_registration_list);
        EventBus.getDefault().register(this);
        initView();
        initDataAndEvent();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isModifed) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            ObjectSaveUtils.saveObject(DiseaseRegistrationListActivity.this, DISEASE_REGISTRATION_LIST, data);
                        }
                    }).start();
                }
                finish();
            }
        });
        openGpsService();
    }

    private void openGpsService() {
        LocationManager manger = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean providerEnabled = manger.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!providerEnabled) {
            if (dialog == null)
                dialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
            dialog.setTitleText("GPS定位服务未打开")
                    .setContentText("是否前往设置中打开GPS？")
                    .setConfirmText("确定,前往。")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            Intent intent2 = new Intent(
                                    Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivityForResult(intent2, 0);
                            sweetAlertDialog.dismiss();
                        }
                    })
                    .setCanceledOnTouchOutside(true);
            dialog.show();

        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent messageEvent) {
        int pos = messageEvent.position;
        DiseaseRegistration diseaseRegistration = messageEvent.t;
        if (messageEvent.isNew) {
            data.add(pos, diseaseRegistration);
            recyclerView.getAdapter().notifyDataSetChanged();
        } else {
            data.set(pos, diseaseRegistration);
            recyclerView.getAdapter().notifyItemChanged(pos);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    private void initDataAndEvent() {
        toolbar.setTitle("病害登记");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.arrow_back2);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DiseaseRegistrationActivity.startAction(DiseaseRegistrationListActivity.this, null, -1);
            }
        });

        data = (List<DiseaseRegistration>) ObjectSaveUtils.getObject(this, DISEASE_REGISTRATION_LIST);
        if (data == null) {
            data = new ArrayList<>();
        }
        diseaseRegAdapter = new DiseaseRegAdapter(this, data);
        recyclerView.setAdapter(diseaseRegAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case 0x11:
                diseaseRegAdapter.setEditMode(!diseaseRegAdapter.getEditMode());
                if (diseaseRegAdapter.getEditMode()) {
                    edit_layout.setVisibility(View.VISIBLE);
                    floatingActionButton.setVisibility(View.GONE);
                } else {
                    edit_layout.setVisibility(View.GONE);
                    floatingActionButton.setVisibility(View.VISIBLE);
                }
                break;
            case 0x12:
                FellowMenDialogFragment fragment = new FellowMenDialogFragment();
                fragment.show(getFragmentManager(), "FellowMen");
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 0x11, 20, "编辑").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu.add(0, 0x12, 21, "陪同人员").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    private void initView() {
        edit_layout = (RelativeLayout) findViewById(R.id.edit_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.add);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        Button upload = (Button) edit_layout.findViewById(R.id.upload);
        Button cancel = (Button) edit_layout.findViewById(R.id.cancel);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkedIndex = diseaseRegAdapter.getCheckedIndex();
                if (checkedIndex != null && checkedIndex.size() > 0) {
                    count = 1;
                    upLoadData(checkedIndex);
                } else {
                    Toast.makeText(DiseaseRegistrationListActivity.this, "当前没有选中任何记录", Toast.LENGTH_SHORT).show();
                }

//                Toast.makeText(DiseaseRegistrationListActivity.this, "ffffff", Toast.LENGTH_SHORT).show();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                diseaseRegAdapter.setEditMode(false);
                edit_layout.setVisibility(View.GONE);
                floatingActionButton.setVisibility(View.VISIBLE);
            }
        });
        CheckedTextView checkedTextView = (CheckedTextView) edit_layout.findViewById(R.id.checkAll);

        checkedTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((CheckedTextView) v).setChecked(!((CheckedTextView) v).isChecked());
                diseaseRegAdapter.checkAll(((CheckedTextView) v).isChecked());
            }
        });
    }

    private void upLoadData(List checkedIndex) {
        if (checkedIndex.size() > 0) {


            currentUploadPosition = (int) checkedIndex.get(0);
            currentDisReg = data.get(currentUploadPosition);
            if (!currentDisReg.isUpload) {
                dialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
                dialog.setTitleText("正在执行第" + count + "个记录上传操作....");
                dialog.setContentText("正在上传..");
                dialog.show();
                RequestParams params = new RequestParams();
                String contentType = RequestParams.APPLICATION_OCTET_STREAM;
                if ((currentDisReg.picPaths != null && currentDisReg.picPaths.size() > 0) || currentDisReg.listDiseaseVoiceRecord.size() > 0) {
                    try {
                        for (int j = 0; j < currentDisReg.picPaths.size(); j++) {
                            File file = new File(currentDisReg.picPaths.get(j));
                            if (file.exists()) {
                                params.put("attachment" + (j + 1), file, contentType);
                            }
                        }
                        for (int k = currentDisReg.picPaths.size(); k < currentDisReg.picPaths.size() + currentDisReg.listDiseaseVoiceRecord.size(); k++) {
                            File file = new File(currentDisReg.listDiseaseVoiceRecord.get(k - currentDisReg.picPaths.size()).recordPath);
                            if (file.exists()) {
                                params.put("attachment" + (k + 1), file, contentType);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    params.put("sessionId", currentDisReg.sessionId == null ? "" : currentDisReg.sessionId);
                    params.put("modelDir", "DiseaseRecord");
                    params.setHttpEntityIsRepeatable(true);
                    params.setUseJsonStreamer(false);
                    HttpclientUtil.postObject(this, updatePicture, handler, HttpclientUtil.UPDATE_SUCCESS, params);
                } else {
                    Gson gson = new Gson();
                    String data = gson.toJson(currentDisReg, DiseaseRegistration.class);
                    params.put("data", data);
                    params.put("sessionId", currentDisReg.sessionId);
                    HttpclientUtil.postObject(this, updateUrl, handler, HttpclientUtil.SAVE_SUCCESS, params);
                }
            } else {
                checkedIndex.remove(0);
                upLoadData(checkedIndex);
            }
        } else {
            Toast.makeText(this, "请选择需要上传的记录！", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDismiss(Object data) {

    }


    public static class MessageEvent {
        public int position;
        public DiseaseRegistration t;
        public boolean isNew;

        public MessageEvent(DiseaseRegistration t, int position, boolean isNew) {
            this.position = position;
            this.t = t;
            this.isNew = isNew;
        }
    }

}
