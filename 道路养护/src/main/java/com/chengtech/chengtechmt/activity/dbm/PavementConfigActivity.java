package com.chengtech.chengtechmt.activity.dbm;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.chengtech.chengtechmt.R;
import com.chengtech.chengtechmt.util.ACache;
import com.chengtech.chengtechmt.util.HttpclientUtil;
import com.chengtech.chengtechmt.util.MyDialogUtil;
import com.chengtech.chengtechmt.util.SpinnerPopupwindow;
import com.chengtech.chengtechmt.util.SpinnerPopupwindow.MyItemOnClickListener;
import com.chengtech.chengtechmt.util.UserUtils;
import com.chengtech.chengtechmt.view.TitleLayout;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.shelwee.uilistview.ui.UiListView;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PavementConfigActivity extends Activity implements OnClickListener{

    private static final int TYPE1 = 1;
    private static final int TYPE2 = 2;
    private TitleLayout layout;
    private UiListView uiListView;
    private Dialog loadDialog;
    private ACache aCache;
    private TextView tv1,tv2;
    private SpinnerPopupwindow spinnerPopupwindow;
    private ArrayAdapter<String> arrayAdapter;
    private List<String> data;
    private Map<String,String> deptMap = new HashMap<String,String>();

    private Handler handler = new Handler() {

        public void handleMessage(android.os.Message msg) {
            String json = (String) msg.obj;
            Gson gson = new Gson();
            switch (msg.what) {
                case TYPE1:
                    if (!TextUtils.isEmpty(json)) {
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            JSONObject jsonObject2 = jsonObject.getJSONObject("entity");
                            JSONArray array = jsonObject2.getJSONArray("listSecondDept");
                            for (int i=0;i<array.length();i++) {
                                String deptName = array.getJSONObject(i).getString("name");
                                String deptId = array.getJSONObject(i).getString("id");
                                deptMap.put(deptName, deptId);
                            }

                            loadDialog.dismiss();
                        } catch (Exception e) {
                            UserUtils.reLogin(PavementConfigActivity.this,loadDialog);
                        }

                    } else {
                        loadDialog.dismiss();
                    }
                    break;

                case TYPE2:

                    break;
            }
        };
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pavement_config);
        initView();

        Intent intent = getIntent();
        layout.setTitle(intent.getStringExtra("title"));
        getData(intent.getStringExtra("url"),TYPE1);
    }

    private void initView() {
        layout = (TitleLayout) findViewById(R.id.mytitle);
        uiListView = (UiListView) findViewById(R.id.uilist);
        loadDialog = MyDialogUtil.createDialog(PavementConfigActivity.this, "正在加载..");
        aCache = ACache.get(this);

        tv1 = (TextView) findViewById(R.id.pavementconfig_tv1);
        tv1.setOnClickListener(this);

        tv2 = (TextView) findViewById(R.id.pavementconfig_tv2);
        tv2.setOnClickListener(this);

        spinnerPopupwindow = new SpinnerPopupwindow(this);
        data = new ArrayList<String>();
        arrayAdapter = new ArrayAdapter<String>(this, R.layout.item_spinner_popup, data);
        spinnerPopupwindow.setAdapter(arrayAdapter);
        spinnerPopupwindow.setOnItemClickListener(new MyItemOnClickListener() {

            @Override
            public void onItemClcik(int position) {
                String name = data.get(position);
                if (name!=null && name.contains("所")) {
                    tv1.setText(name);
                }else {
                    tv2.setText(name);
                }

            }
        });
    }

    private void getData(String url, final int type) {
        AsyncHttpClient client = HttpclientUtil.getInstance(this);
        AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                loadDialog.show();
                super.onStart();
            }

            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {

                try {
                    String data = new String(arg2, "utf-8");
                    Message message = new Message();
                    message.what = type;
                    message.obj = data;
                    handler.sendMessage(message);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                super.onSuccess(arg0, arg1, arg2);
            }

            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2,
                                  Throwable arg3) {

                loadDialog.dismiss();

                Toast.makeText(PavementConfigActivity.this, "服务器断开连接",
                        Toast.LENGTH_SHORT).show();
                super.onFailure(arg0, arg1, arg2, arg3);
            }
        };
        client.get(url, responseHandler);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pavementconfig_tv1:
                data.clear();
                data.add("南城养护所");
                data.add("东城养护所");
                data.add("北城养护所");
                spinnerPopupwindow.refreshData();
                spinnerPopupwindow.setWidth(v.getWidth());
                spinnerPopupwindow.showAsDropDown(v,0,5);
                break;

            case R.id.pavementconfig_tv2:
                data.clear();
                data.add("面层结构配置");
                data.add("基层机构配置");
                data.add("地面结构配置");
                spinnerPopupwindow.refreshData();
                spinnerPopupwindow.setWidth(v.getWidth());
                spinnerPopupwindow.showAsDropDown(v,0,5);
                break;

        }

    }



}
