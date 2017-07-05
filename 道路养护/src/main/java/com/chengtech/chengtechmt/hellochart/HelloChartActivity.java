package com.chengtech.chengtechmt.hellochart;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chengtech.chengtechmt.BaseActivity;
import com.chengtech.chengtechmt.R;
import com.chengtech.chengtechmt.entity.gson.RouteG;
import com.chengtech.chengtechmt.util.HttpclientUtil;
import com.chengtech.chengtechmt.util.MyDialogUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class HelloChartActivity extends BaseActivity {

    private FragmentManager fragmentManager;
    private Fragment currentFragment;
    private ColumnChartFragment column;
    private LineChartFragment line;
    private int[][] lineData;
    private float[][] columnData;
    private String[][] lineLable;
    private String[][] columnLable;
    private String url;
    private int[] data2 ;
    private String[] lable2;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String json = (String) msg.obj;
            try {
                JSONArray jsonArray = new JSONArray(json);
                if (jsonArray.length()>0) {
                    data2= new int[jsonArray.length()];
                    lable2 = new String[jsonArray.length()];
                    for (int i=0;i<jsonArray.length();i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String name = jsonObject.getString("name");
                        int count = jsonObject.getInt("count");
                        data2[i] = count;
                        lable2[i] = name;
                    }
                    showChart();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addContentView(R.layout.activity_hello_chart);

        initView();
        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        getData();

        setNavigationIcon(true);
        hidePagerNavigation(true);
        toolbar.setTitle("设施量情况表");

        //让toolbar不在滑动
        AppBarLayout.LayoutParams layoutP = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
        layoutP.setScrollFlags(0);
        toolbar.setLayoutParams(layoutP);

//        toolbar.setNavigationIcon(R.drawable.all_back_selector);


        line = new LineChartFragment();
        fragmentManager = getSupportFragmentManager();
        column = new ColumnChartFragment();
    }

    private void showChart() {

        columnData = new float[7][1];
        columnLable = new String[7][1];
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 1; j++) {
                columnData[i][j] = data2[i];
                columnLable[i][j] = lable2[i];
            }
        }

        lineData = new int[1][7];
        lineLable = new String[1][7];

        for (int i = 0; i < 1; i++) {
            for (int j = 0; j < 7; j++) {
                lineData[i][j] = data2[j];
                lineLable[i][j] = lable2[j];
            }
        }


        Bundle bundle = new Bundle();
        bundle.putSerializable("columnData", columnData);
        bundle.putSerializable("columnLable", columnLable);
        bundle.putSerializable("lineData", lineData);
        bundle.putSerializable("lineLable", lineLable);
        column.setArguments(bundle);


        line.setArguments(bundle);

        fragmentManager.beginTransaction().add(R.id.parent_container, column).commit();
        currentFragment = column;
    }

    public void getData() {
        AsyncHttpClient client = HttpclientUtil.getInstance(this);

        AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {

                try {
                    String data = new String(arg2, "utf-8");
                    Message msg = new Message();
                    msg.obj = data;
                    handler.sendMessage(msg);

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                super.onSuccess(arg0, arg1, arg2);
            }

            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2,
                                  Throwable arg3) {
                super.onFailure(arg0, arg1, arg2, arg3);
            }
        };
        client.get(url,
                responseHandler);

    }

    private void initView() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_hello_chart, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_line) {
            if (!(currentFragment instanceof LineChartFragment)) {
                fragmentManager.beginTransaction().replace(R.id.parent_container, line).commit();
                currentFragment = line;
            }
            return true;
        }
        if (id == R.id.action_column) {
            if (!(currentFragment instanceof ColumnChartFragment)) {
                fragmentManager.beginTransaction().replace(R.id.parent_container, column).commit();
                currentFragment = column;
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
