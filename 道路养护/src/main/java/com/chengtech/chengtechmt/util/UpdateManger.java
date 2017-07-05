package com.chengtech.chengtechmt.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.pdf.PdfDocument;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.style.BulletSpan;
import android.util.Log;
import android.webkit.DownloadListener;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 作者: LiuFuYingWang on 2017/2/14 14:21.
 */

public class UpdateManger {

    private static final String VERSION_NO = "version_no";
    private Context mContext;
    private SharedPreferences sharedPreferences;
    private int CURRENT_VERSION_NO ;
    private AsyncHttpClient asyncHttpClient;
    public  String checkVersionUrl = MyConstants.PRE_URL+"ms/sys/app/getAppVersionMessage.action?phoneApplication=0";
    public  Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            String json = (String) msg.obj;
            switch (msg.what) {
                case 0:
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        boolean success = jsonObject.getBoolean("success");
                        if (success) {
                            JSONObject data = jsonObject.getJSONObject("data");
//                            String version = data.getString("version");
//                            String updateMsg = data.getString("memo");
                            String updateMsg = data.getString("appUpdateDescription");
                            String versionStr = data.getString("appVersion");
                            int version = Integer.valueOf(versionStr);
                            String apkName = data.getString("appName");
                            String apkPath = data.getString("appfilePath");
                            //把版本号保存在本地
//                            saveVersionNum(version);

                            //弹窗提示有新版本可以下载
                            if (version>CURRENT_VERSION_NO) {
                                showUpdatedDialog(updateMsg,apkPath+apkName+".apk");
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };

    private void showUpdatedDialog(String updateMsg, final String apkPath) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(Html.fromHtml("<font color=#38B259>有新的版本可以升级</font>"));
        builder.setMessage(updateMsg);
        builder.setPositiveButton("升级", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                downloadApk("http://14.23.99.106:8666/"+apkPath);
                downloadApk(MyConstants.PRE_URL+apkPath);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消",null);
        builder.create().show();
    }

    private void downloadApk(String apkPath) {
        CommonUtils.downApk(mContext,apkPath,"道路养护"+CURRENT_VERSION_NO+".apk");
    }

    public UpdateManger(Context context) {
        mContext = context;
    }

    public  void checkVersion() {
        getCurrentVersionNum();
        asyncHttpClient = HttpclientUtil.getInstance(mContext);
        AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                try {
                    String data = new String(arg2, "utf-8");
                    Message message = handler.obtainMessage();
                    message.what = 0;
                    message.obj = data;
                    handler.sendMessage(message);
                } catch (Exception e) {
                }
                super.onSuccess(arg0, arg1, arg2);
            }

        };
        asyncHttpClient.get(checkVersionUrl,
                responseHandler);
    }

    public void cancleRequest(){
        asyncHttpClient.cancelRequests(mContext,true);
    }

    private void getCurrentVersionNum() {
        PackageManager packageManager = mContext.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(mContext.getPackageName(), 0);
            CURRENT_VERSION_NO = packageInfo.versionCode;
            Log.i("tag",CURRENT_VERSION_NO+"");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
//        sharedPreferences = mContext.getSharedPreferences(VERSION_NO,Context.MODE_PRIVATE);
//        CURRENT_VERSION_NO = sharedPreferences.getString("version_num","1");
//        CURRENT_VERSION_NO = "1";
    }
//    private void saveVersionNum(String version) {
//        SharedPreferences.Editor edit = sharedPreferences.edit();
//        edit.putString("version_num",version);
//        edit.commit();
//    }

}
