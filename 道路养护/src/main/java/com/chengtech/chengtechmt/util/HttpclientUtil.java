package com.chengtech.chengtechmt.util;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;

import org.apache.http.client.protocol.ClientContext;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.CookieStore;
import cz.msebera.android.httpclient.cookie.Cookie;


public class HttpclientUtil {
    public static final int SAVE_SUCCESS = 0x11;
    public static final int SAVE_FAILED = 0x12;
    public static final int UPDATE_SUCCESS = 0x13;
    public static final int ACCESS_ERROR = 0x30;
    private static AsyncHttpClient client;

    public static synchronized AsyncHttpClient getInstance(Context context) {
        if (client == null) {
            client = new AsyncHttpClient();
            client.setTimeout(10 * 1000);
            PersistentCookieStore cookieStore = new PersistentCookieStore(
                    context);
            client.setCookieStore(cookieStore);

        }

        return client;
    }

    public static void clear() {
        client = null;
    }

    public static void getData(final Context context, String url, final Handler handler, final int resultCode) {

        if (((Activity) context).isFinishing())
            return;
        final SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.setContentText("正在加载...").setTitleText("");

        AsyncHttpClient client = HttpclientUtil.getInstance(context);

        AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                sweetAlertDialog.show();
            }

            @Override
            public void onSuccess(int i, cz.msebera.android.httpclient.Header[] headers, byte[] bytes) {
                if (sweetAlertDialog != null && sweetAlertDialog.isShowing())
                    sweetAlertDialog.dismiss();
                try {
                    String data = new String(bytes, "utf-8");
                    Message message = handler.obtainMessage();
                    message.what = resultCode;
                    message.obj = data;
                    handler.sendMessage(message);
                } catch (Exception e) {
//                    Toast.makeText(context, "数据解析出错。", Toast.LENGTH_SHORT).show();
                    sweetAlertDialog.setContentText("数据解析出错。").changeAlertType(SweetAlertDialog.ERROR_TYPE);
                }
            }


            @Override
            public void onFailure(int i, cz.msebera.android.httpclient.Header[] headers, byte[] bytes, Throwable throwable) {
//                Toast.makeText(context, "连接服务器出错。", Toast.LENGTH_SHORT).show();
                sweetAlertDialog.setContentText("连接服务器出错。").changeAlertType(SweetAlertDialog.ERROR_TYPE);
            }


        };
        client.get(url,
                responseHandler);
    }

    public static void getData(final Context context, String url, final Handler handler, final int resultCode, final boolean isShowDialog) {
        AsyncHttpClient client = HttpclientUtil.getInstance(context);

        AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
//                if (isShowDialog) {
//                    dialog.show();
//                }

            }

            @Override
            public void onSuccess(int i, cz.msebera.android.httpclient.Header[] headers, byte[] bytes) {
                try {
                    String data = new String(bytes, "utf-8");
                    Message message = handler.obtainMessage();
                    message.what = resultCode;
                    message.obj = data;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    Toast.makeText(context, "数据解析出错", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int i, cz.msebera.android.httpclient.Header[] headers, byte[] bytes, Throwable throwable) {
                Toast.makeText(context, "连接服务器出错", Toast.LENGTH_SHORT).show();
            }


        };
        client.get(url,
                responseHandler);
    }

    public static void postObject(final Context context, String url, final Handler handler, final int resultCode, RequestParams requestParams) {
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
                    Message message = new Message();
                    message.what = resultCode;
                    message.obj = data;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    Toast.makeText(context, "数据解析出错", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int i, cz.msebera.android.httpclient.Header[] headers, byte[] bytes, Throwable throwable) {
                handler.sendEmptyMessage(SAVE_FAILED);
            }

            @Override
            public void onPostProcessResponse(ResponseHandlerInterface instance, HttpResponse response) {

                super.onPostProcessResponse(instance, response);
            }
        };
//        RequestParams params = new RequestParams();
//        if (filePaths != null) {
////            String address = "/data/data/com.chengtech.chengtechmt/files/2222.png";
//            try {
//                for (int i = 0; i < filePaths.size(); i++) {
//                    File file = new File(filePaths.get(i));
//                    if (file.exists()) {
//                        params.put("attachment" + (i + 1), file);
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        params.put("data", object);
//        params.put("modelDir", "BridgeOftenCheck");
//        params.put("sessionId", "");
//        client.addHeader("Content-Type", "multipart/form-data");
//        client.addHeader("Content-Type", "audio/wav");
//        client.addHeader("Content-Type", "image/jpeg") ;
//        client.addHeader("Content-Type", "application/octet-stream");
//        client.addHeader("Content-Disposition", "multipart/form-data");
//        client.addHeader("enctype", "multipart/form-data");
        client.post(url, requestParams, responseHandler);
    }

    public static void setCookie(Context context) {
        String sessionId = null;
        CookieStore cookieStore = (CookieStore) client.getHttpContext()
                .getAttribute(ClientContext.COOKIE_STORE);
        List<Cookie> cookies = cookieStore.getCookies();
        for (Cookie c : cookies) {
            if ("JSESSIONID".equals(c.getName())) {
                sessionId = c.getValue();
            }
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            CookieSyncManager.createInstance(context);
        }
        CookieManager manager = CookieManager.getInstance();
        manager.setCookie(MyConstants.PRE_URL, "JSESSIONID=" + sessionId);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            CookieSyncManager.getInstance().sync();
        }
    }
}
