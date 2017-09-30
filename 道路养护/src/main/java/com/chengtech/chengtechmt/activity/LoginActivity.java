package com.chengtech.chengtechmt.activity;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chengtech.chengtechmt.MainActivity;
import com.chengtech.chengtechmt.R;
import com.chengtech.chengtechmt.entity.User;
import com.chengtech.chengtechmt.entity.gson.LoginInfoG;
import com.chengtech.chengtechmt.fragment.IpSelectorDialogFragment;
import com.chengtech.chengtechmt.util.ACache;
import com.chengtech.chengtechmt.util.AESEncryptor;
import com.chengtech.chengtechmt.util.AppAccount;
import com.chengtech.chengtechmt.util.HttpclientUtil;
import com.chengtech.chengtechmt.util.LogUtils;
import com.chengtech.chengtechmt.util.MyConstants;
import com.chengtech.chengtechmt.util.MyDialogUtil;
import com.chengtech.chengtechmt.util.SharedPreferencesUtil;
import com.chengtech.chengtechmt.util.UpdateManger;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.pgyersdk.crash.PgyCrashManager;
import com.pgyersdk.update.PgyUpdateManager;

import org.apache.http.Header;
import org.apache.http.client.protocol.ClientContext;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.client.CookieStore;

public class LoginActivity extends Activity implements OnClickListener {
    protected static final String SEED = "chengtech";
    protected static final int ONE = 1;
    protected static final int TWO = 2;
    private TextView login_title, login_eng_title, login_app_title;
    private EditText login_account_et, login_pwd_et;
    private Button login_btn;
    private InputMethodManager inputMethodManager; // 触摸屏幕是隐藏输入法
    private ImageView login_account_iv, login_pwd_iv, account_clear_iv,
            pwd_show_iv, pwd_close_iv, login_bg, login_logo;
    private View account_line, pwd_line;
    private LinearLayout login_linear;
    private boolean loadAnim = false;
    private String account, password;
    private Dialog loadDialog;
    private ACache aCache;
    private long lastTime;
    private UpdateManger updateManger;

    private Handler handler = new Handler() {

        public void handleMessage(android.os.Message msg) {

            switch (msg.what) {
                case ONE:
                    Bundle bundle = msg.getData();
                    login_account_et.setText(bundle.getString("account"));
                    login_pwd_et.setText(bundle.getString("password"));
                    break;

                case TWO:
                    String data = (String) msg.obj;
                    Gson gson = new Gson();
                    try {
                        final LoginInfoG loginInfoG = gson.fromJson(data, LoginInfoG.class);
                        if (loginInfoG.success) {
                            Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_SHORT)
                                    .show();

                            // 保存用户信息
                            new Thread(new Runnable() {

                                @Override
                                public void run() {
                                    try {
                                        saveAccountMessage(account, password, loginInfoG.name == null ? null : loginInfoG.name,
                                                loginInfoG.id == null ? null : loginInfoG.id,loginInfoG.userInfo);

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }

                            }).start();

                            //保存用户的权限信息
                            AppAccount.userRights = loginInfoG.data;
                            // 跳转到下一个activity
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("data", (ArrayList) loginInfoG.data);
                            startActivity(intent);

                            finish();
                        } else {
                            String content = loginInfoG.msg;
                            Toast.makeText(LoginActivity.this, content, Toast.LENGTH_SHORT).show();
                            // 登陆失败清除用户输入信息
                            if (content.contains("不存在")) {
                                login_account_et.setText("");
                                login_account_et.requestFocus();
                            } else {
                                login_pwd_et.setText("");
                                login_pwd_et.requestFocus();
                            }

                        }
                    } catch (Exception e) {
                        Toast.makeText(LoginActivity.this, "服务器出错失败，请重新登陆", Toast.LENGTH_SHORT)
                                .show();
                    }
                    break;
            }

        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login2);

        //首先获得ip地址
        //TODO
        MyConstants.PRE_URL = (String) SharedPreferencesUtil.getData(this,IpSelectorDialogFragment.IP_ADDRESS,MyConstants.PRE_URL);
        MyConstants.setConstant();

        PgyCrashManager.register(this); //蒲公英的注册crash接口
        PgyUpdateManager.register(this); //自动更新
        updateManger = new UpdateManger(this);
        updateManger.checkVersion();

        initView();


    }

    /**
     * 保存用户信息
     *
     * @param account2
     * @param password2
     * @param name
     * @param id
     * @author liufuyingwang
     * 2015-9-9 2:53:24
     */
    protected void saveAccountMessage(String account2, String password2,
                                      String name, String id,User userInfo) throws Exception {
        SharedPreferences preferences = this.getSharedPreferences("login",
                MODE_PRIVATE);
        Editor edit = preferences.edit();
        edit.putString("username", AESEncryptor.encrypt(SEED, account));
        edit.putString("password", AESEncryptor.encrypt(SEED, password));
        //保存用户信息
        aCache.put(id,userInfo,ACache.TIME_DAY*1);
        if (name != null) {
            edit.putString("name", AESEncryptor.encrypt(SEED, name));
        }
        if (id != null) {
            edit.putString("id", AESEncryptor.encrypt(SEED, id));
        }
        AppAccount.name = name;
        AppAccount.userId = id;
        edit.commit();
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        AppAccount.mScreenWidth = metrics.widthPixels;
        AppAccount.mScreenHeight = metrics.heightPixels;
    }

    @Override
    protected void onStart() {
        super.onStart();
        // 检查本地是否有保存用户名和密码
        // 这里必须还要使用AES来解密用户信息和密码
        new Thread(new Runnable() {

            @Override
            public void run() {
                SharedPreferences prefer = getSharedPreferences("login",
                        MODE_PRIVATE);
                if (prefer != null) {
                    String account = null;
                    String password = null;
                    try {
                        account = AESEncryptor.decrypt(SEED, prefer.getString("username", null));
                        password = AESEncryptor.decrypt(SEED, prefer.getString("password", null));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (!TextUtils.isEmpty(account)
                            && !TextUtils.isEmpty(password)) {
                        //进行信息的回填
                        Message message = new Message();
                        message.what = ONE;
                        Bundle bundle = new Bundle();
                        bundle.putString("account", account);
                        bundle.putString("password", password);
                        LogUtils.i(bundle.getString("account"));
                        message.setData(bundle);
                        handler.sendMessage(message);
                    }
                }
            }
        }).start();

    }

    private void initView() {
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        login_app_title = (TextView) findViewById(R.id.login_app_title);
        login_account_iv = (ImageView) findViewById(R.id.login_account);
        login_pwd_iv = (ImageView) findViewById(R.id.login_pwd);
        login_pwd_iv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                IpSelectorDialogFragment fragment = new IpSelectorDialogFragment();
                fragment.show(getFragmentManager(),"ipSelector");
                return true;
            }
        });
        account_line = findViewById(R.id.account_line);
        pwd_line = findViewById(R.id.pwd_line);
        login_linear = (LinearLayout) findViewById(R.id.login_linear);
//        login_bg = (ImageView) findViewById(R.id.login_bg);
//        login_logo = (ImageView) findViewById(R.id.login_logo);

        account_clear_iv = (ImageView) findViewById(R.id.clear_account_edit);
        account_clear_iv.setOnClickListener(this);


        login_account_et = (EditText) findViewById(R.id.login_edit_account);
        login_account_et.setOnClickListener(this);


        login_pwd_et = (EditText) findViewById(R.id.login_edit_pwd);
        login_pwd_et.setOnClickListener(this);


        pwd_show_iv = (ImageView) findViewById(R.id.show_pwd);
        pwd_show_iv.setOnClickListener(this);
        pwd_close_iv = (ImageView) findViewById(R.id.close_pwd);
        pwd_close_iv.setOnClickListener(this);


        login_btn = (Button) findViewById(R.id.user_to_login);
        login_btn.setOnClickListener(this);


        login_account_et.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    login_account_iv
                            .setImageResource(R.mipmap.login_phone_select);
                    account_line.setBackgroundColor(getResources().getColor(
                            R.color.shenlv));
                } else {
                    login_account_iv.setImageResource(R.mipmap.login_phone);
                    account_line.setBackgroundColor(getResources().getColor(
                            R.color.white));
                }

            }
        });
        login_pwd_et.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    login_pwd_iv.setImageResource(R.mipmap.login_pwd_select);
                    pwd_line.setBackgroundColor(getResources().getColor(
                            R.color.shenlv));
                } else {
                    login_pwd_iv.setImageResource(R.mipmap.login_pwd);
                    pwd_line.setBackgroundColor(getResources().getColor(
                            R.color.white));
                }

            }
        });

        aCache = ACache.get(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (getCurrentFocus() != null
                    && getCurrentFocus().getWindowToken() != null) {
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus()
                        .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.clear_account_edit:
                login_account_et.setText("");
                break;


            case R.id.show_pwd:
                pwd_close_iv.setVisibility(View.VISIBLE);
                pwd_show_iv.setVisibility(View.GONE);
                login_pwd_et.setInputType(InputType.TYPE_CLASS_TEXT
                        | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                break;
            case R.id.close_pwd:
                pwd_close_iv.setVisibility(View.GONE);
                pwd_show_iv.setVisibility(View.VISIBLE);
                login_pwd_et
                        .setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                break;
            case R.id.login_edit_account:
                break;
            case R.id.login_edit_pwd:
//                if (!loadAnim) {
//                    loadAnim = true;
//                    PropertyValuesHolder p1 = PropertyValuesHolder.ofFloat("alpha",
//                            0);
//                    PropertyValuesHolder p2 = PropertyValuesHolder.ofFloat(
//                            "TranslationY", 0 - login_bg.getLayoutParams().height);
//
//                    ObjectAnimator.ofPropertyValuesHolder(login_bg, p1, p2)
//                            .setDuration(1000).start();
//
//                    ObjectAnimator.ofFloat(login_bg, "TranslationY", -80)
//                            .setDuration(1000).start();
//                    ObjectAnimator.ofFloat(login_linear, "TranslationY", -80)
//                            .setDuration(1000).start();
//
//                    PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat(
//                            "scaleX", 1f, 1, 0.8f);
//                    PropertyValuesHolder pvhZ = PropertyValuesHolder.ofFloat(
//                            "scaleY", 1f, 1, 0.8f);
//                    PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat(
//                            "TranslationY", -50);
//
//                    ObjectAnimator
//                            .ofPropertyValuesHolder(login_app_title, pvhX, pvhY, pvhZ)
//                            .setDuration(1000).start();
//
//                    ObjectAnimator
//                            .ofPropertyValuesHolder(login_logo, pvhX, pvhY, pvhZ)
//                            .setDuration(1000).start();
//
//                }
                break;

            case R.id.user_to_login:
                account = login_account_et.getText().toString().trim();
                password = login_pwd_et.getText().toString().trim();
                if (TextUtils.isEmpty(account)) {
                    login_account_et.requestFocus();
                    Toast.makeText(this, "用户名为空", Toast.LENGTH_SHORT).show();
                    break;
                } else if (TextUtils.isEmpty(password)) {
                    login_pwd_et.requestFocus();
                    Toast.makeText(this, "密码为空", Toast.LENGTH_SHORT).show();
                    break;
                }

                //隐藏的ip配置开关
//                String specilaAccount = getResources().getString(R.string.specil_account);
//                String specilaPwd = getResources().getString(R.string.specil_account_password);
//                if (specilaAccount.equals(account) && specilaPwd.equals(password)) {
//                    IpSelectorDialogFragment fragment = new IpSelectorDialogFragment();
//                    fragment.show(getFragmentManager(),"ipSelector");
//                    break;
//                }
                loadDialog = MyDialogUtil.createDialog(this, "");
                loadDialog.show();

                //登陆之前先把上个用户的记录清掉
                HttpclientUtil.clear();
                landAccount(account, password, MyConstants.LOGIN_URL);
                break;
        }
    }

    /**
     * 登陆账户
     *
     * @param account
     * @param pwd
     * @param url
     * @author liufuyingwang
     * 2015-9-9 2:32:05
     */
    private void landAccount(String account, String pwd, String url) {

        AsyncHttpClient client = HttpclientUtil.getInstance(this);
        AsyncHttpResponseHandler asyncHttpResponseHandler = new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                loadDialog.show();
                ;
                super.onStart();
            }

            @Override
            public void onSuccess(int i, cz.msebera.android.httpclient.Header[] headers, byte[] bytes) {
                loadDialog.dismiss();
                try {
                    String data = new String(bytes, "utf-8");
                    Message msg = new Message();
                    msg.what = TWO;
                    msg.obj = data;
                    handler.sendMessage(msg);
                } catch (UnsupportedEncodingException e) {
                }
            }

            @Override
            public void onFailure(int i, cz.msebera.android.httpclient.Header[] headers, byte[] bytes, Throwable throwable) {
                loadDialog.dismiss();
                Toast.makeText(LoginActivity.this, "服务器出错，连接失败", Toast.LENGTH_SHORT).show();
            }

        };
        RequestParams params = new RequestParams();
        params.put("userAccount", account);
        params.put("password", pwd);
        client.post(url, params, asyncHttpResponseHandler);

    }

    /**
     * 连续按两次退出
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - lastTime) > 2000) {
                Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
                lastTime = System.currentTimeMillis();
            } else {
                finish();
                CookieStore attribute = (CookieStore) HttpclientUtil.getInstance(this).getHttpContext().getAttribute(ClientContext.COOKIE_STORE);
                attribute.clear();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onStop() {
        super.onStop();
        updateManger.cancleRequest();
    }
}
