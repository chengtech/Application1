package com.chengtech.chengtechmt.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;
import android.widget.Toast;

import com.chengtech.chengtechmt.entity.gson.LoginInfoG;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.apache.http.client.CookieStore;
import org.apache.http.client.protocol.ClientContext;


public class UserUtils {

    protected static final String SEED = "chengtech";

    private static boolean hasOnce = false;

    public static void logoutUser(final Context context,
                                  boolean isClearPreference) {
        SharedPreferences sp = context.getSharedPreferences("login",
                Context.MODE_PRIVATE);
        String userId = sp.getString("id", null);

        // 清除本地的偏好设置的信息
        if (isClearPreference) {
            if (sp != null) {
                Editor edit = sp.edit();
                edit.putString("username", null);
                edit.putString("password", null);
                edit.putString("name", null);
                edit.putString("id", null);
                edit.commit();
            }

        }

        // 在服务器端注销
//		AsyncHttpClient client = HttpclientUtil.getInstance(context);
//		client.get(Constant.LOGOUT_URL + userId,
//				new AsyncHttpResponseHandler() {
//					@Override
//					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
//						try {
//							String data = new String(arg2, "utf-8");
//							LogUtils.i(data);
//						} catch (UnsupportedEncodingException e) {
//						}
//						super.onSuccess(arg0, arg1, arg2);
//					}
//				});

        // 清除本地的cookie信息
        CookieStore attribute = (CookieStore) HttpclientUtil
                .getInstance(context)
                .getHttpContext()
                .getAttribute(
                        ClientContext.COOKIE_STORE);
        attribute.clear();

    }

    /*
     * 重新登陆
     */
    public static void reLogin(Context context, Dialog loadDialog) {

        // 清除cookie信息
        CookieStore attribute = (CookieStore) HttpclientUtil
                .getInstance(context).getHttpContext()
                .getAttribute(ClientContext.COOKIE_STORE);
        attribute.clear();
        HttpclientUtil.clear();


        SharedPreferences prefer = context.getSharedPreferences("login", 0);
        if (prefer != null) {
            String account = null;
            String password = null;
            try {
                account = AESEncryptor.decrypt(SEED, prefer.getString("username", null));
                password = AESEncryptor.decrypt(SEED, prefer.getString("password", null));
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (!TextUtils.isEmpty(account) && !TextUtils.isEmpty(password)) {
                // 当账号和密码都不为空的时候进行验证；
                accesNetWork(account, password, MyConstants.LOGIN_URL, context, loadDialog);
            }
        }
    }

    /**
     * 进行用户名的登陆
     *
     * @param account
     * @param password
     * @param url
     */
    private static void accesNetWork(String account, String password,
                                     String url, final Context context, final Dialog loadDialog) {
        final String account1 = account;
        final String password1 = password;
        AsyncHttpClient client = HttpclientUtil.getInstance(context);
        AsyncHttpResponseHandler response = new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, cz.msebera.android.httpclient.Header[] headers, byte[] bytes) {
                try {
                    String data = new String(bytes, "utf-8");
                    Gson gson = new Gson();
                    LoginInfoG loginInfoG = gson.fromJson(data, LoginInfoG.class);

                    if (loginInfoG.success) {

                        loadDialog.dismiss();
//						((Activity)context).finish();

                    } else {
                        String msg = loginInfoG.msg;
                        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(int i, cz.msebera.android.httpclient.Header[] headers, byte[] bytes, Throwable throwable) {
                Toast.makeText(context, "服务器连接失败", Toast.LENGTH_SHORT).show();
            }

        };
        RequestParams params = new RequestParams();
        params.put("userAccount", account1);
        params.put("password", password1);
        client.post(url, params, response);

    }

    public static boolean hasPermission(String permission) {
        Boolean aBoolean = MyConstants.userRights.get(permission);
        if (aBoolean == null)
            return false;
        return aBoolean;
    }

    /**
     * 检查session是否过期了。
     *
     * @param json
     */
    public static void checkSessionTimeOut(Context context, String json) {

    }

}
