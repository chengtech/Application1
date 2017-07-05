package com.chengtech.chengtechmt.activity.standard;

import com.google.gson.Gson;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.camera2.params.ColorSpaceTransform;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.chengtech.chengtechmt.BaseActivity;
import com.chengtech.chengtechmt.R;
import com.chengtech.chengtechmt.adapter.RecycleViewAdapter3;
import com.chengtech.chengtechmt.adapter.SimpleTreeListViewAdapter;
import com.chengtech.chengtechmt.adapter.TreeListViewAdapter;
import com.chengtech.chengtechmt.entity.Tree;
import com.chengtech.chengtechmt.entity.gson.TreeG;
import com.chengtech.chengtechmt.util.HttpclientUtil;
import com.chengtech.chengtechmt.util.Node;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.Line;

/**
 * 标准规范管理的列表页面
 */
public class StandardListActivity extends BaseActivity implements TreeListViewAdapter.OnTreeNodeClickListener {
    private ListView treeListView;
    private TextView treeDialogTitle;
    private String selectedTitle;
    private List<Tree> treeList;
    private String url;
    private String urlList;
    private String id;
    private SimpleTreeListViewAdapter<Tree> adapter;
    private RecyclerView recyclerView;
    private ScrollView diseaseMaintenance;  //维修方案布局，一开始被隐藏了
    private TextView diseaseMaintenance_tv,memo_tv;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            loadDialog.dismiss();
            String json = (String) msg.obj;
            Gson gson = new Gson();
            switch (msg.what) {
                case 1:
                    try {
                        json = "{\"success\":true,\"data\" : " + json + "}";
                        TreeG treeG = gson.fromJson(json, TreeG.class);
                        treeList = treeG.data;
                        if (treeList != null && treeList.size() > 0) {
                            inflateTreeDialog();
                        }

                    } catch (Exception e) {
                        Toast.makeText(StandardListActivity.this, "数据解析出错", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 2:
                    diseaseMaintenance.setVisibility(View.GONE);
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        JSONArray rows = jsonObject.getJSONArray("rows");
                        List<String> subTitle = new ArrayList<>();
                        List<String> content = new ArrayList<>();
                        for (int i = 0; i < rows.length(); i++) {
                            JSONObject jsonObject1 = rows.getJSONObject(i);
                            String name = jsonObject1.getString("name");
                            subTitle.add(name);
                            content.add(selectedTitle);
                        }
                        RecycleViewAdapter3 adapter = new RecycleViewAdapter3(StandardListActivity.this
                                , subTitle, content, R.layout.item_standar_child);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(StandardListActivity.this));
                    } catch (Exception e) {
                        Toast.makeText(StandardListActivity.this, "数据结构解析出错。", Toast.LENGTH_SHORT).show();
                    }
                    break;

                case 3:
                    diseaseMaintenance.setVisibility(View.VISIBLE);
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        JSONObject diseaseMaintenance = (JSONObject) jsonObject.get("data");
                        String maintenance = diseaseMaintenance.getString("maintenance");
                        diseaseMaintenance_tv.setText(TextUtils.isEmpty(maintenance)?"":maintenance);
                        LinearLayout parent = (LinearLayout) diseaseMaintenance_tv.getParent();
                        TextView title_tv = (TextView) parent.getChildAt(0);
                        SpannableString ss = new SpannableString("("+selectedTitle+")维修方案：");
                        ss.setSpan(new ForegroundColorSpan(Color.RED),0,selectedTitle.length()+2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        ss.setSpan(new ForegroundColorSpan(Color.parseColor("#ff33b5e5")),selectedTitle.length()+2,ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        title_tv.setText(ss);
                        if (json.contains("\"memo\":")) {
                            String memo = diseaseMaintenance.getString("memo");
                            memo_tv.setText(TextUtils.isEmpty(memo)?"":memo);
                        }
                    } catch (Exception e) {
                        Toast.makeText(StandardListActivity.this, "数据结构解析出错。", Toast.LENGTH_SHORT).show();
                    }

                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addContentView(R.layout.activity_standard_list);

        setNavigationIcon(true);
        hidePagerNavigation(true);
        setAppBarLayoutScroll(false);

        initView();

        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        urlList = intent.getStringExtra("urlList");

        treeList = new ArrayList<>();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_work_section, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.filter) {
            HttpclientUtil.getData(this, url, handler, 1);
            loadDialog.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void inflateTreeDialog() throws Exception {
        View view = LayoutInflater.from(this).inflate(R.layout.tree_listview, null, false);
        treeListView = (ListView) view.findViewById(R.id.treeListView);
        treeDialogTitle = (TextView) view.findViewById(R.id.selection);
        adapter = new SimpleTreeListViewAdapter<>(treeListView, StandardListActivity.this, treeList, 1);
        adapter.setOnTreeNodeClickListener(StandardListActivity.this);
        treeListView.setAdapter(adapter);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //如果是维修方案，就要进入第三种json解析
                if (urlList.contains("diseasemaintenanceplan")){
                    HttpclientUtil.getData(StandardListActivity.this, urlList + id, handler, 3);
                }else {
                    HttpclientUtil.getData(StandardListActivity.this, urlList + id, handler, 2);
                }
                loadDialog.show();
            }
        });

        builder.setNegativeButton("取消", null);
        AlertDialog dialog = builder.create();

        dialog.show();
    }


    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        diseaseMaintenance = (ScrollView) findViewById(R.id.diseaseMaintenance);
        diseaseMaintenance_tv = (TextView) findViewById(R.id.diseaseMaintenance_tv);
        memo_tv = (TextView) findViewById(R.id.memo);
    }

    @Override
    public void onClick(Node node, int position) {
        if (node.isLeaf()) {
            treeDialogTitle.setText(node.getName());
            selectedTitle = node.getName();
            id = node.getId();
        }
    }
}
