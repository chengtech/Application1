package com.chengtech.chengtechmt.activity.dbm;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.chengtech.chengtechmt.BaseActivity;
import com.chengtech.chengtechmt.R;
import com.chengtech.chengtechmt.activity.business.MaintenanceListActivity;
import com.chengtech.chengtechmt.adapter.RecycleViewAdapter2;
import com.chengtech.chengtechmt.entity.MaintainRegister;
import com.chengtech.chengtechmt.entity.Tree;
import com.chengtech.chengtechmt.entity.dbm.RoadGreening;
import com.chengtech.chengtechmt.impl.OnItemClickListener;
import com.chengtech.chengtechmt.util.DeptSpinnerUtil;
import com.chengtech.chengtechmt.util.HttpclientUtil;
import com.chengtech.chengtechmt.util.MyConstants;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 公路绿化
 */

public class RoadGreeningActivity extends BaseActivity implements OnItemClickListener,View.OnClickListener {

    private static final int TYPE1 = 1;
    private static final int TYPE2 = 2;
    private static final int TYPE5 = 5;
    private List<Tree> trees = new ArrayList<>();
    private List<RoadGreening> roadGreenings = new ArrayList<>();
    private AlertDialog deptSpinnerDialog;
    private String deptId;
    private RecyclerView recyclerView;

    private String deptUrl = MyConstants.PRE_URL+"mt/dbm/road/roadgreening/getTree.action";
    private String listUrl = MyConstants.PRE_URL+"mt/dbm/road/roadgreening/listRoadGreeningJson.action?deptIds=";

    private Handler handler = new Handler() {

        public void handleMessage(android.os.Message msg) {
            String json = (String) msg.obj;
            Gson gson = new Gson();
            switch (msg.what) {
                case TYPE1:
                    try {
                        if (!TextUtils.isEmpty(json)) {
                            trees = new ArrayList<>();
                            JSONArray jsonArray = new JSONArray(json);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                Tree tree = gson.fromJson(jsonArray.getString(i), Tree.class);
                                deptTree.put(tree.id,tree.text);
                                trees.add(tree);
                            }
                        }
                    } catch (Exception e) {
                        Toast.makeText(RoadGreeningActivity.this, "数据解析出错。", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case TYPE2:
                    try {
                        if (!TextUtils.isEmpty(json)) {
                            JSONObject jsonObject = new JSONObject(json);
                            JSONArray rows = jsonObject.getJSONArray("rows");
                            if(rows.length()==pageSize)
                                maxPage++;
                            roadGreenings.clear();
                            for (int i = 0; i < rows.length(); i++) {
                                String objectString = rows.getString(i);
                                RoadGreening roadGreening = gson.fromJson(objectString, RoadGreening.class);
                                roadGreenings.add(roadGreening);
                            }
                            if (roadGreenings.size() >0){
                                recyclerView.setVisibility(View.VISIBLE);
                                showRecyclerView();
                            }else {
                                Toast.makeText(RoadGreeningActivity.this, "无数据", Toast.LENGTH_SHORT).show();
                                recyclerView.setVisibility(View.GONE);
                            }
                        }
                    } catch (Exception e) {
                        Toast.makeText(RoadGreeningActivity.this, "数据解析出错。", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case TYPE5:
                    deptId = json;
                    break;
            }
        }

        ;
    };

    private void showRecyclerView() {
        List<String[]> titleArrayList2 = new ArrayList<>();

            for (RoadGreening roadGreening : roadGreenings) {
                String[] titleArray = new String[3];
                titleArray[0] =
                        "序号:" + roadGreening.code;
                titleArray[1] = "管养单位:" + deptTree.get(roadGreening.belongDeptId);
                titleArray[2] = "<br/>桩号:" + roadGreening.stake;
                titleArrayList2.add(titleArray);
            }

        RecycleViewAdapter2 adapter2 =
                new RecycleViewAdapter2(this, titleArrayList2, R.layout.item_recycle3);
        recyclerView.setAdapter(adapter2);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter2.setOnItemClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addContentView(R.layout.activity_road_greening);

        setNavigationIcon(true);

        initView();
        HttpclientUtil.getData(this, deptUrl, handler, TYPE1);
    }

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_work_section, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.filter) {
            if (trees.size() > 0) {
                if (deptSpinnerDialog == null) {
                    deptSpinnerDialog = DeptSpinnerUtil.createDeptSpinner(this, trees, listUrl, handler, TYPE2);
                }
                deptSpinnerDialog.show();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(this,RoadGreeningDetailActivity.class);
        intent.putExtra("title", "公路绿化基本信息");
        intent.putExtra("id",roadGreenings.get(position).id);
        intent.putExtra("content", (Serializable) roadGreenings.get(position).getContent());
        intent.putExtra("sessionId", roadGreenings.get(position).sessionId);
        intent.putExtra("subtitle", (Serializable)roadGreenings.get(position).getTitles());
        intent.putExtra("subtitleProperty", (Serializable) roadGreenings.get(position).getPropertyName());
        startActivity(intent);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.perPage:
                if (pageNo != 1) {
                    pageNo--;
                    pageNo_tv.setText(pageNo + "");
                    HttpclientUtil.getData(this,listUrl+deptId+"&pager.pageNo="+pageNo+"&pager.pageSize="+pageSize
                                    +"&sort=sortOrder&direction=asc",
                            handler,TYPE2);
                } else {
                    Toast.makeText(this, "当前是最新页", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.nextPage:
                if (pageNo < maxPage) {
                    pageNo++;
                    pageNo_tv.setText(pageNo + "");
                    HttpclientUtil.getData(this,listUrl+deptId+"&pager.pageNo="+pageNo+"&pager.pageSize="+pageSize
                                    +"&sort=sortOrder&direction=asc",
                            handler,TYPE2);
                } else {
                    Toast.makeText(this, "当前已经是最后一页", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
