package com.chengtech.chengtechmt.activity.business;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.chengtech.chengtechmt.BaseActivity;
import com.chengtech.chengtechmt.R;
import com.chengtech.chengtechmt.adapter.ImageAddAdapter;
import com.chengtech.chengtechmt.entity.business.DiseaseRegistration;
import com.chengtech.chengtechmt.runnable.WarterMarkRunnable;
import com.chengtech.chengtechmt.util.AppAccount;
import com.chengtech.chengtechmt.util.CommonUtils;
import com.chengtech.chengtechmt.util.JsonParser;
import com.chengtech.chengtechmt.util.ObjectSaveUtils;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
public class DiseaseRegistrationActivity extends BaseActivity {

    private ArrayList<String> picPaths;
    private String describeMsg;
    private String recordPath;
    private String recordLength;

    private RecyclerView recyclerView;
    private ImageAddAdapter imageAddAdapter;
    private ImageButton imageButton;
    private EditText resultShow_et;
    private long recognizeStartTime;
    private long recognizeEndTime;

    private Button saveInLocal;
    private TextView videoLength;
    private CardView videoCardView;
    private DiseaseRegistration diseaseRegistration;
    private int diseaseRegPosition;
    private SweetAlertDialog sweetAlertDialog;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case WarterMarkRunnable.WATER_MARK_SUCCESS:
                    imageAddAdapter.notifyDataSetChanged();
                    break;
                case SAVE_SUCCESS:
                    sweetAlertDialog.setContentText("保存成功!").changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                    break;
            }
        }
    };

    // 用HashMap存储听写结果
    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();
    private RecognizerDialog recognizerDialog;

    private RecognizerDialogListener recognizerDialogListener = new RecognizerDialogListener() {
        @Override
        public void onResult(RecognizerResult recognizerResult, boolean b) {
            recognizeEndTime = System.currentTimeMillis();
            printResult(recognizerResult);
        }

        @Override
        public void onError(SpeechError speechError) {

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
        long recognizeTime = (recognizeEndTime - recognizeStartTime - 2000) / 1000;
        recordLength = String.valueOf(recognizeTime);
        videoLength.setText(recognizeTime + "\"");
        resultShow_et.setText(resultBuffer.toString());
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

//        diseaseRegistration = (DiseaseRegistration) ObjectSaveUtils.getObject(this, "DiseaseRegistration");
        initView();
        showSavedInstanceState();
    }

    private void showSavedInstanceState() {
        if (diseaseRegistration != null) {
            picPaths = diseaseRegistration.picPaths;
            describeMsg = diseaseRegistration.describeMsg;
            recordPath = diseaseRegistration.recordPaths;
            recordLength = diseaseRegistration.recordLength;
            if (TextUtils.isEmpty(recordLength)) {
                videoCardView.setVisibility(View.GONE);
            } else {
                videoCardView.setVisibility(View.VISIBLE);
            }
        } else {
            picPaths = new ArrayList<>();
            describeMsg = "";
            recordPath = "";
            recordLength = "";
            videoCardView.setVisibility(View.GONE);
        }
        imageAddAdapter = new ImageAddAdapter(this, picPaths);
        recyclerView.setAdapter(imageAddAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        resultShow_et.setText(describeMsg);
        videoLength.setText(recordLength + "\"");
    }

    private void initView() {
        saveInLocal = (Button) findViewById(R.id.saveInLocal);
        saveInLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveInLoacal();
            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        resultShow_et = (EditText) findViewById(R.id.showMsg);
        videoLength = (TextView) findViewById(R.id.videoLength);
        videoCardView = (CardView) findViewById(R.id.video);
        imageButton = (ImageButton) findViewById(R.id.addRecord);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoCardView.setVisibility(View.VISIBLE);
                setParameter();
                recognizeStartTime = System.currentTimeMillis();
                recognizerDialog.show();
            }
        });
        videoCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final MediaPlayer mediaPlayer = new MediaPlayer();
                try {
                    mediaPlayer.setDataSource("file://" + recordPath);
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            mediaPlayer.release();
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        recognizerDialog = new RecognizerDialog(this, null);
        recognizerDialog.setListener(recognizerDialogListener);
    }

    private void setParameter() {
        recognizerDialog.setParameter(SpeechConstant.PARAMS, null); //清空上次的所有参数
        recognizerDialog.setParameter(SpeechConstant.DOMAIN, "iat"); //语音识别引擎
        recognizerDialog.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD); //语音听写在线
        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        recognizerDialog.setParameter(SpeechConstant.VAD_BOS, "4000");

        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        recognizerDialog.setParameter(SpeechConstant.VAD_EOS, "2000");

        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        recognizerDialog.setParameter(SpeechConstant.ASR_PTT, "1");

        //设置方言
        recognizerDialog.setParameter(SpeechConstant.ACCENT, "mandarin");

        //设置语言
        recognizerDialog.setParameter(SpeechConstant.LANGUAGE, "zh_en");

        //设置保存的格式
        recognizerDialog.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        //设置保存的路径
        String dateStr = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        recordPath = getExternalCacheDir() + "/record/iat-" + dateStr + ".wav";
        recognizerDialog.setParameter(SpeechConstant.ASR_AUDIO_PATH, recordPath);
        // 设置听写结果是否结果动态修正，为“1”则在听写过程中动态递增地返回结果，否则只在听写结束之后返回最终结果
        // 注：该参数暂时只对在线听写有效
        recognizerDialog.setParameter(SpeechConstant.ASR_DWA, "0");
//        recognizerDialog.setParameter(SpeechConstant.KEY_SPEECH_TIMEOUT, "60000");
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CommonUtils.TAKE_PHOTO && resultCode == RESULT_OK) {
            String picPath = imageAddAdapter.getCameraCachePath();

            Bitmap bitmap = null;
            try {
                File file = new File(picPath);
                if (!file.exists())
                    return;
                Map<String, Object> waterMarkParam = new HashMap<>();
                Date date = new Date(System.currentTimeMillis());
                DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
                String picName = "病害登记-" + format.format(date) + ".jpg";
                String waterMarkMsg = "登记人:超级管理员;拍照时间：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
                waterMarkParam.put("picName", picName);
                waterMarkParam.put("waterMarkMsg", waterMarkMsg);
                waterMarkParam.put("imgPath", picPath);
                waterMarkParam.put("imgPaths", picPaths);
                waterMarkParam.put("handler", handler);
                new Thread(new WarterMarkRunnable(this, waterMarkParam)).run();
//                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(picUri));
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (requestCode == ImageAddAdapter.SELECT_IMG_RESULT && resultCode == RESULT_OK) {
            ArrayList<String> picturePath = data.getStringArrayListExtra("PicturePath");
            Map<String, Object> waterMarkParam = new HashMap<>();
            for (int i = 0; i < picturePath.size(); i++) {
                Date date = new Date(System.currentTimeMillis() + i * 1000); //这里之所以要加上i*1000,是为了防止名称重复，造成重复加载。
                DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
                String picName = "病害登记-" + format.format(date) + ".jpg";
                String waterMarkMsg = "登记人:超级管理员;拍照时间：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
                waterMarkParam.put("picName", picName);
                waterMarkParam.put("waterMarkMsg", waterMarkMsg);
                waterMarkParam.put("imgPath", picturePath.get(i));
                waterMarkParam.put("imgPaths", picPaths);
                waterMarkParam.put("handler", handler);
                new Thread(new WarterMarkRunnable(this, waterMarkParam)).run();
            }
        }
    }


//    /**
//     * 压缩图片
//     *
//     * @param path
//     * @param reqWidth
//     * @param reqHeight
//     * @return
//     */
    /*private Bitmap compressBitmapFromPath(String path, int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        return bitmap;
    }*/

   /* private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int width = options.outWidth;
        int height = options.outHeight;

        int inSampleSize = 1;
        if (width > reqWidth || height > reqHeight) {
            int widthRadio = Math.round(width * 1.0f / reqWidth);
            int heightRadio = Math.round(height * 1.0f / reqHeight);
            inSampleSize = Math.max(widthRadio, heightRadio);
        }
        return inSampleSize;
    }*/

    public static void startAction(Context context, DiseaseRegistration diseaseRegistration, int position) {
        Intent intent = new Intent(context, DiseaseRegistrationActivity.class);
        intent.putExtra("data", diseaseRegistration);
        intent.putExtra("position", position);
        context.startActivity(intent);
    }

    private void saveInLoacal() {
        if (diseaseRegistration == null)
            diseaseRegistration = new DiseaseRegistration();

        diseaseRegistration.describeMsg = resultShow_et.getText().toString().trim();
        diseaseRegistration.picPaths = picPaths;
        diseaseRegistration.recordLength = recordLength;
        diseaseRegistration.recordPaths = recordPath;

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
                    diseaseRegistration.recordMan = AppAccount.name;
                    diseaseRegistration.recordDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                    data.add(0, diseaseRegistration);
                    EventBus.getDefault().post(new DiseaseRegistrationListActivity.MessageEvent(diseaseRegistration, 0, true));
                } else {
                    data.set(diseaseRegPosition, diseaseRegistration);
                    EventBus.getDefault().post(new DiseaseRegistrationListActivity.MessageEvent(diseaseRegistration, diseaseRegPosition, false));
                }
                int resultCode = ObjectSaveUtils.saveObject(DiseaseRegistrationActivity.this, DISEASE_REGISTRATION_LIST, data);
                if (resultCode == -1) {
                    handler.sendEmptyMessageDelayed(SAVE_SUCCESS, 2000);
                } else {
                    handler.sendEmptyMessage(SAVE_FAILED);
                }
            }
        }).run();
    }
}


