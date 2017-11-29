package com.chengtech.chengtechmt.activity.business;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.chengtech.chengtechmt.BaseActivity;
import com.chengtech.chengtechmt.R;
import com.chengtech.chengtechmt.adapter.ImageAddAdapter;
import com.chengtech.chengtechmt.adapter.RecordAdapter;
import com.chengtech.chengtechmt.entity.business.DiseaseRegistration;
import com.chengtech.chengtechmt.entity.business.DiseaseVoiceRecord;
import com.chengtech.chengtechmt.entity.business.FellowMen;
import com.chengtech.chengtechmt.fragment.FellowMenDialogFragment;
import com.chengtech.chengtechmt.runnable.WarterMarkRunnable;
import com.chengtech.chengtechmt.util.AppAccount;
import com.chengtech.chengtechmt.util.CommonUtils;
import com.chengtech.chengtechmt.util.JsonParser;
import com.chengtech.chengtechmt.util.ObjectSaveUtils;
import com.chengtech.nicespinner.NiceSpinner;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.RecognizerListener;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.chengtech.chengtechmt.activity.business.DiseaseRegistrationListActivity.DISEASE_REGISTRATION_LIST;
import static com.chengtech.chengtechmt.util.HttpclientUtil.SAVE_FAILED;
import static com.chengtech.chengtechmt.util.HttpclientUtil.SAVE_SUCCESS;

/**
 * 病害登记表
 */
public class DiseaseRegistrationActivity extends BaseActivity implements AMapLocationListener, FellowMenDialogFragment.OnDismissListener {

    private static final int GET_LOCAL_INFO_SUCCESS = 0x13;
    private static final int GET_LOCAL_INFO_FAILED = 0x14;
    private ArrayList<String> picPaths;
    private String describeMsg;
    private String site;
    private String weather;
    private String fellowMen;
    private List<DiseaseVoiceRecord> myRecordList;
    AMapLocationClient client;
    private RecyclerView recyclerView;
    private RecyclerView recordRecyclerView;
    private ImageAddAdapter imageAddAdapter;
    private EditText resultShow_et;
    private TextView site_tv;
    private TextView fellowMen_et;
    private ImageView fellowMen_add;
    private NiceSpinner weatherSpinner;
    private Button startSpeech;
    private long recognizeStartTime;
    private long recognizeEndTime;
    private FellowMenDialogFragment fellowMenDialogFragment;

    private DiseaseRegistration diseaseRegistration;
    private int diseaseRegPosition;
    private SweetAlertDialog sweetAlertDialog;
    //    private AMapLocation myAMapLocation;
    private String longitude, latitude;
    private boolean isSaveLocal;
    private RecyclerView.AdapterDataObserver adapterDataObserver;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case WarterMarkRunnable.WATER_MARK_SUCCESS:
                    imageAddAdapter.notifyDataSetChanged();
                    break;
                case SAVE_SUCCESS:
                    sweetAlertDialog.setContentText("保存成功!").changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                    isSaveLocal = true;
                    break;
                case GET_LOCAL_INFO_SUCCESS:
                    AMapLocation location = (AMapLocation) msg.obj;
                    site_tv.setText(location.getAddress());
                    site = location.getAddress();
                    longitude = String.valueOf(location.getLongitude());
                    latitude = String.valueOf(location.getLatitude());
                    break;
                case GET_LOCAL_INFO_FAILED:
                    site_tv.setText("未知位置");
                    site = "未能识别地址";
            }
        }
    };

    // 用HashMap存储听写结果
    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();
    //    private RecognizerDialog recognizerDialog;
    private SpeechRecognizer speechRecognizer;
    private RecordAdapter recordAdapter;


    /**
     * 听写监听器。
     */
    private RecognizerListener mRecognizerListener = new RecognizerListener() {

        @Override
        public void onBeginOfSpeech() {
            // 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
            showTip("开始说话");
        }

        @Override
        public void onError(SpeechError error) {
            // Tips：
            // 错误码：10118(您没有说话)，可能是录音机权限被禁，需要提示用户打开应用的录音权限。
            // 如果使用本地功能（语记）需要提示用户开启语记的录音权限。
            showTip(error.getPlainDescription(true));
        }

        @Override
        public void onEndOfSpeech() {
            // 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
//            recognizeEndTime = System.currentTimeMillis();
            showTip("结束说话");
        }

        @Override
        public void onResult(RecognizerResult results, boolean isLast) {
//            Log.i("tag", "jihao");
            if (isLast) {
            }
            printResult(results);

        }

        @Override
        public void onVolumeChanged(int volume, byte[] data) {
            showTip("当前正在说话，音量大小：" + volume);
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话id为null
            //	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            //		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            //		Log.d(TAG, "session id =" + sid);
            //	}
        }
    };

    private void printResult(RecognizerResult results) {
        String text = JsonParser.parseIatResult(results.getResultString());

        String sn = null;
        // 读取json结果中的sn字段
        try {
            JSONObject resultJson = new JSONObject(results.getResultString());
            sn = resultJson.optString("sn");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mIatResults.put(sn, text);
        StringBuffer resultBuffer = new StringBuffer();
        for (String key : mIatResults.keySet()) {
            resultBuffer.append(mIatResults.get(key));
        }
        myRecordList.get(myRecordList.size() - 1).recordContent = resultBuffer.toString();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < myRecordList.size(); i++) {
            sb.append(myRecordList.get(i).recordContent);
        }
        resultShow_et.setText(sb.toString());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addContentView(R.layout.activity_disease_registration);

        setNavigationIcon(true);
        hidePagerNavigation(true);
        toolbar.setTitle("病害详细信息登记");
        Intent intent = getIntent();
        diseaseRegistration = (DiseaseRegistration) intent.getSerializableExtra("data");
        diseaseRegPosition = intent.getIntExtra("position", 0);
        initView();
        showSavedInstanceState();
        initData();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!diseaseRegistration.isUpload) {
                    if (!isSaveLocal) {
                        if (sweetAlertDialog != null) {
                            if (sweetAlertDialog.isShowing())
                                sweetAlertDialog.dismiss();
                            sweetAlertDialog.setTitleText("是否保存当前信息。").setContentText("").setConfirmText("保存").setCancelText("取消").changeAlertType(SweetAlertDialog.WARNING_TYPE);
                            sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    saveInLoacal();
                                    sweetAlertDialog.dismiss();

                                }
                            });
                            sweetAlertDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                    finish();
                                }
                            });
                            sweetAlertDialog.show();
                        } else {
                            sweetAlertDialog = new SweetAlertDialog(DiseaseRegistrationActivity.this, SweetAlertDialog.WARNING_TYPE);

                            sweetAlertDialog.setTitleText("是否保存当前信息。").setConfirmText("保存").setCancelText("取消");
                            sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    saveInLoacal();
                                    sweetAlertDialog.dismiss();
                                }
                            });
                            sweetAlertDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                    finish();
                                }
                            });
                            sweetAlertDialog.show();
                        }
                    } else {
                        finish();
                    }
                } else {
                    finish();
                }
            }
        });
    }

    private void initData() {
        adapterDataObserver = new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                super.onItemRangeRemoved(positionStart, itemCount);
                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < myRecordList.size(); i++) {
                    sb.append(myRecordList.get(i).recordContent);
                }
                resultShow_et.setText(sb.toString());
            }
        };
        recordAdapter.registerAdapterDataObserver(adapterDataObserver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (diseaseRegistration != null) {
            if (!diseaseRegistration.isUpload)
                menu.add("保存").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        } else {
            menu.add("保存").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        saveInLoacal();
        return super.onOptionsItemSelected(item);
    }

    private void showSavedInstanceState() {
        List<FellowMen> fellowMens = (List<FellowMen>) ObjectSaveUtils.getObject(this, FellowMenDialogFragment.FELLOW_MEN_LIST_1);
        if (diseaseRegistration != null) {
            picPaths = diseaseRegistration.picPaths;
            describeMsg = diseaseRegistration.diseaseDescription;
            myRecordList = diseaseRegistration.listDiseaseVoiceRecord;
            site = diseaseRegistration.site;
            weather = diseaseRegistration.weather;
            longitude = diseaseRegistration.longitude;
            latitude = diseaseRegistration.latitude;
            fellowMen = diseaseRegistration.fellowMen;
        } else {
            diseaseRegistration = new DiseaseRegistration();
            picPaths = new ArrayList<>();
            describeMsg = "";
            site = "";
            longitude = "0.0";
            latitude = "0.0";
            weather = "晴";//默认为晴
            fellowMen = "";
            if (fellowMens != null && fellowMens.size() > 0) {
                for (int i = 0; i < fellowMens.size(); i++) {
                    FellowMen f = fellowMens.get(i);
                    if (f.isChecked)
                        fellowMen = fellowMen + f.name + ",";
                }
            }
            myRecordList = new ArrayList<>();
            getLocationInfo();
        }
        imageAddAdapter = new ImageAddAdapter(this, picPaths);
        recyclerView.setAdapter(imageAddAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recordAdapter = new RecordAdapter(this, myRecordList);
        recordRecyclerView.setAdapter(recordAdapter);
        recordRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        resultShow_et.setText(describeMsg);
        site_tv.setText(site);
        fellowMen_et.setText(fellowMen);
        weatherSpinner.setText(weather);

    }

    private void initView() {
        recordRecyclerView = (RecyclerView) findViewById(R.id.recordRecyclerView);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        resultShow_et = (EditText) findViewById(R.id.showMsg);
        fellowMen_et = (TextView) findViewById(R.id.fellowMen);
        fellowMen_add = (ImageView) findViewById(R.id.fellowMenAdd);
        fellowMen_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fellowMenDialogFragment == null) {
                    fellowMenDialogFragment = new FellowMenDialogFragment();
                }
                fellowMenDialogFragment.show(getFragmentManager(), "FellowMen");

            }
        });
        site_tv = (TextView) findViewById(R.id.site);
        weatherSpinner = (NiceSpinner) findViewById(R.id.weatherSpinner);
        weatherSpinner.attachDataSource(Arrays.asList(new String[]{"", "晴", "阴", "小雨", "大雨"}));

//        recognizerDialog = new RecognizerDialog(this, null);
        speechRecognizer = SpeechRecognizer.createRecognizer(this, null);
//        recognizerDialog.setListener(recognizerDialogListener);

        startSpeech = (Button) findViewById(R.id.startSpeech);
        startSpeech.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        DiseaseVoiceRecord record = new DiseaseVoiceRecord();
                        String dateStr = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                        record.recordPath = getExternalCacheDir() + "/record/iat-" + dateStr + ".wav";
                        myRecordList.add(record);
                        setParameter();
                        recognizeStartTime = System.currentTimeMillis();
                        speechRecognizer.startListening(mRecognizerListener);
                        break;

                    case MotionEvent.ACTION_UP:
                        recognizeEndTime = System.currentTimeMillis();
                        myRecordList.get(myRecordList.size() - 1).recordLength = String.valueOf((recognizeEndTime - recognizeStartTime) / 1000);
                        speechRecognizer.stopListening();
                        recordRecyclerView.getAdapter().notifyDataSetChanged();
                        break;
                }
                return true;
            }
        });
    }

    private void setParameter() {
        speechRecognizer.setParameter(SpeechConstant.PARAMS, null); //清空上次的所有参数
        speechRecognizer.setParameter(SpeechConstant.DOMAIN, "iat"); //语音识别引擎
        speechRecognizer.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD); //语音听写在线
        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        speechRecognizer.setParameter(SpeechConstant.VAD_BOS, "4000");

        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        speechRecognizer.setParameter(SpeechConstant.VAD_EOS, "4000");

        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        speechRecognizer.setParameter(SpeechConstant.ASR_PTT, "1");

        //设置方言
        speechRecognizer.setParameter(SpeechConstant.ACCENT, "mandarin");

        //设置语言
        speechRecognizer.setParameter(SpeechConstant.LANGUAGE, "zh_en");

        //设置保存的格式
        speechRecognizer.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        //设置保存的路径
        speechRecognizer.setParameter(SpeechConstant.ASR_AUDIO_PATH, myRecordList.get(myRecordList.size() - 1).recordPath);
        // 设置听写结果是否结果动态修正，为“1”则在听写过程中动态递增地返回结果，否则只在听写结束之后返回最终结果
        // 注：该参数暂时只对在线听写有效
        speechRecognizer.setParameter(SpeechConstant.ASR_DWA, "0");
//        recognizerDialog.setParameter(SpeechConstant.KEY_SPEECH_TIMEOUT, "60000");
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CommonUtils.TAKE_PHOTO && resultCode == RESULT_OK) {
            String picPath = imageAddAdapter.getCameraCachePath();

            try {
                File file = new File(picPath);
                if (!file.exists())
                    return;
                Map<String, Object> waterMarkParam = new HashMap<>();
                Date date = new Date(System.currentTimeMillis());
                DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
                String picName = "DiseaseRecord-" + format.format(date) + ".jpg";
                StringBuffer sb = new StringBuffer();
                sb.append("巡查人:" + AppAccount.name + ";拍照时间：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date) + ";");
//                if (!TextUtils.isEmpty(site)) {
                sb.append("经度:" + longitude + "\u3000\u3000");
                sb.append("纬度:" + latitude + ";");
                sb.append("位置信息：" + site);
//                }
                waterMarkParam.put("picName", picName);
                waterMarkParam.put("waterMarkMsg", sb.toString());
                waterMarkParam.put("imgPath", picPath);
                waterMarkParam.put("imgPaths", picPaths);
                waterMarkParam.put("handler", handler);
                new Thread(new WarterMarkRunnable(this, waterMarkParam)).start();
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (requestCode == ImageAddAdapter.SELECT_IMG_RESULT && resultCode == RESULT_OK) {
            ArrayList<String> picturePath = data.getStringArrayListExtra("PicturePath");
            Map<String, Object> waterMarkParam = new HashMap<>();
            for (int i = 0; i < picturePath.size(); i++) {
                Date date = new Date(System.currentTimeMillis() + i * 1000); //这里之所以要加上i*1000,是为了防止名称重复，造成重复加载。
                DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
                String picName = "DiseaseRecord-" + format.format(date) + ".jpg";
                StringBuffer sb = new StringBuffer();
                sb.append("登记人:超级管理员;拍照时间：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date) + ";");
//                if (!TextUtils.isEmpty(site)) {
                sb.append("经度:" + longitude + "\u3000\u3000");
                sb.append("纬度:" + latitude + ";");
                sb.append("位置信息：" + site);
//                }
                waterMarkParam.put("picName", picName);
                waterMarkParam.put("waterMarkMsg", sb.toString());
                waterMarkParam.put("imgPath", picturePath.get(i));
                waterMarkParam.put("imgPaths", picPaths);
                waterMarkParam.put("handler", handler);
                new Thread(new WarterMarkRunnable(this, waterMarkParam)).start();
            }
        }
    }

    public static void startAction(Context context, DiseaseRegistration diseaseRegistration, int position) {
        Intent intent = new Intent(context, DiseaseRegistrationActivity.class);
        intent.putExtra("data", diseaseRegistration);
        intent.putExtra("position", position);
        context.startActivity(intent);
    }

    private void saveInLoacal() {
        if (diseaseRegistration == null)
            diseaseRegistration = new DiseaseRegistration();

        diseaseRegistration.diseaseDescription = resultShow_et.getText().toString().trim();
        diseaseRegistration.picPaths = picPaths;
        diseaseRegistration.listDiseaseVoiceRecord = myRecordList;
        diseaseRegistration.site = site;
        diseaseRegistration.weather = weatherSpinner.getText().toString();
        diseaseRegistration.longitude = longitude;
        diseaseRegistration.latitude = latitude;
        diseaseRegistration.fellowMen = fellowMen_et.getText().toString().trim();

        sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.setTitleText("");
        sweetAlertDialog.setContentText("正在保存中...");
        sweetAlertDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<DiseaseRegistration> data = (List<DiseaseRegistration>) ObjectSaveUtils.getObject(DiseaseRegistrationActivity.this, DISEASE_REGISTRATION_LIST);
                if (data == null)
                    data = new ArrayList<DiseaseRegistration>();
                if (diseaseRegistration.recordState.equals("0")) {
                    diseaseRegistration.recordState = "1";
                    diseaseRegistration.patrolMan = AppAccount.name;
                    diseaseRegistration.patrolTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                    data.add(0, diseaseRegistration);
                    EventBus.getDefault().post(new DiseaseRegistrationListActivity.MessageEvent(diseaseRegistration, 0, true));
                } else {
                    data.set(diseaseRegPosition, diseaseRegistration);
                    EventBus.getDefault().post(new DiseaseRegistrationListActivity.MessageEvent(diseaseRegistration, diseaseRegPosition, false));
                }
                int resultCode = ObjectSaveUtils.saveObject(DiseaseRegistrationActivity.this, DISEASE_REGISTRATION_LIST, data);
                if (resultCode == -1) {
                    handler.sendEmptyMessage(SAVE_SUCCESS);
                } else {
                    handler.sendEmptyMessage(SAVE_FAILED);
                }
            }
        }).start();
    }

    public void getLocationInfo() {
        LocationManager manger = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean providerEnabled = manger.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (providerEnabled) {
            //使用高德定位
            AMapLocationClientOption option = new AMapLocationClientOption();
            option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            option.setHttpTimeOut(2000);
            option.setInterval(10000);
            client = new AMapLocationClient(DiseaseRegistrationActivity.this);
            client.setLocationOption(option);
            client.setLocationListener(this);
            client.startLocation();
        } else {
            sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
            sweetAlertDialog.setTitleText("GPS定位服务未打开")
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
            sweetAlertDialog.show();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {

            if (!diseaseRegistration.isUpload) {
                if (!isSaveLocal) {
//                    if (sweetAlertDialog != null) {
//                        sweetAlertDialog.setTitleText("是否保存当前信息。").setConfirmText("保存").setCancelText("取消").changeAlertType(SweetAlertDialog.WARNING_TYPE);
//                        sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                            @Override
//                            public void onClick(SweetAlertDialog sweetAlertDialog) {
//                                saveInLoacal();
//                                sweetAlertDialog.dismiss();
//                            }
//                        });
//                        sweetAlertDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                            @Override
//                            public void onClick(SweetAlertDialog sweetAlertDialog) {
//                                sweetAlertDialog.dismiss();
//                                finish();
//                            }
//                        });
//                        sweetAlertDialog.setCanceledOnTouchOutside(true);
////                        sweetAlertDialog.show();
//                    } else {
                    sweetAlertDialog = new SweetAlertDialog(DiseaseRegistrationActivity.this, SweetAlertDialog.WARNING_TYPE);
                    sweetAlertDialog.setTitleText("是否保存当前信息。").setConfirmText("保存").setCancelText("取消");
                    sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            saveInLoacal();
                            sweetAlertDialog.dismiss();
                        }
                    });
                    sweetAlertDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                            finish();
                        }
                    });
                    sweetAlertDialog.show();
//                    }
                } else {
                    finish();
                }
            } else {
                finish();
            }
            return super.onKeyDown(keyCode, event);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (client != null)
            client.onDestroy();

        if (recordAdapter != null)
            recordAdapter.unregisterAdapterDataObserver(adapterDataObserver);
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        Log.i("tag", aMapLocation.getErrorCode() + "");
        if (aMapLocation.getErrorCode() == 0) {
//            myAMapLocation = aMapLocation;
            Message message = handler.obtainMessage();
            message.obj = aMapLocation;
            message.what = GET_LOCAL_INFO_SUCCESS;
            handler.sendMessage(message);
//            site_tv.setText(aMapLocation.getAddress());
//            site = aMapLocation.getAddress();
//            longitude = String.valueOf(aMapLocation.getLongitude());
//            latitude = String.valueOf(aMapLocation.getLatitude());
        } else {
            Toast.makeText(this, "错误码:" + aMapLocation.getErrorCode(), Toast.LENGTH_SHORT).show();
            handler.sendEmptyMessage(GET_LOCAL_INFO_FAILED);
//            site_tv.setText("未知位置");
        }

    }


    @Override
    public void onDismiss(Object data) {
        fellowMen = "";
        List<FellowMen> fellowMenList = (List<FellowMen>) data;
        if (fellowMenList != null && fellowMenList.size() > 0) {
            for (int i = 0; i < fellowMenList.size(); i++) {
                FellowMen f = fellowMenList.get(i);
                if (f.isChecked)
                    fellowMen = fellowMen + f.name + ",";
            }
        }
        fellowMen_et.setText(fellowMen);
    }
}


