package com.chengtech.chengtechmt.activity.dbm;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.chengtech.chengtechmt.R;
import com.chengtech.chengtechmt.activity.IView;
import com.chengtech.chengtechmt.activity.MapQueryActivity;
import com.chengtech.chengtechmt.adapter.DetailAdapter;
import com.chengtech.chengtechmt.adapter.MyExpandableAdapter;
import com.chengtech.chengtechmt.adapter.SupSubAdapter;
import com.chengtech.chengtechmt.entity.Attachment;
import com.chengtech.chengtechmt.entity.MapEntity;
import com.chengtech.chengtechmt.entity.bridge.Bridge;
import com.chengtech.chengtechmt.entity.bridge.BridgeRecords;
import com.chengtech.chengtechmt.entity.bridge.BridgeTech;
import com.chengtech.chengtechmt.entity.tunnel.Tunnel;
import com.chengtech.chengtechmt.entity.tunnel.TunnelRecords;
import com.chengtech.chengtechmt.entity.tunnel.TunnelTech;
import com.chengtech.chengtechmt.presenter.CardAPresenter;
import com.chengtech.chengtechmt.util.CommonUtils;
import com.chengtech.chengtechmt.util.DateUtils;
import com.chengtech.chengtechmt.util.HttpclientUtil;
import com.chengtech.chengtechmt.util.MyConstants;
import com.chengtech.chengtechmt.util.MyDialogUtil;
import com.chengtech.chengtechmt.util.PopupWindowUtils;
import com.chengtech.chengtechmt.util.NestedScrollViewUtil;
import com.chengtech.chengtechmt.util.UserUtils;
import com.chengtech.chengtechmt.view.TitleLayout;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.http.Url;

public class CardActivity extends Activity implements MyExpandableAdapter.OnChildItemClickListener, IView<Object>, View.OnClickListener {
    private TitleLayout layout;
    private Dialog loadDialog;
    private ImageView img1;
    private ImageView img2;
    private TextView tv1, tv2, tv3, tv4, tv5;
    private Bridge bridge;
    private Tunnel tunnel;
    private String sessionId; //附件id
    private ExpandableListView expandableListView;
    private String[] groupName = new String[]{"A.行政识别数据", "B.结构技术数据", "C.档案资料", "D:技术状况评定", "E:修建记录"};
    private Map<String, List<String>> childMap1;
    private Map<String, List<String>> childMap2;
    private MyExpandableAdapter expandableAdapter;

    private PopupWindowUtils popupWindowUtils;
    private int mScreenWidth;
    private int mScreenHeight;
    private View popup_layout;
    private ListView popup_listview;
    private TextView popup_title;
    private String className;
    private CardAPresenter presenter;
    private LinearLayout photoContainer;
    public FloatingActionButton fab_loaction, fab_gps, fab_attachment;
    public MapEntity mapEntity;
    private boolean isFirstLoadPicture = true;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    isFirstLoadPicture = false;
                    if (!TextUtils.isEmpty(bridge.verticalView)){
                        String url1 = bridge.verticalView.replace("imageUpload","zoomImageUpload");
                        CommonUtils.loadNetWorkPicture(CardActivity.this, MyConstants.PRE_URL + url1, img1);

                    }
                    if (!TextUtils.isEmpty(bridge.frontalView)){
                        String url2 = bridge.frontalView.replace("imageUpload","zoomImageUpload");
                            CommonUtils.loadNetWorkPicture(CardActivity.this, MyConstants.PRE_URL + url2, img2);
                    }
                    break;
            }
//            AMapLocation aMapLocation = (AMapLocation) msg.obj;
//            if (subtitleProperty.contains("longitude") && subtitleProperty.contains("latitude")) {
//                int x = subtitleProperty.indexOf("longitude");
//                int y = subtitleProperty.indexOf("latitude");
//                originalData.remove(x);
//                originalData.add(x, aMapLocation.getLongitude()+"");
//                originalData.remove(y);
//                originalData.add(y, aMapLocation.getLatitude()+"");
//                //将修改过的数据的位置和内容保存在一个hashmap中，
//                editData.put(x, aMapLocation.getLongitude()+"");
//                editData.put(y, aMapLocation.getLatitude()+"");
//                adapter.notifyDataSetChanged();
//            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

        initView();

        Intent intent = getIntent();
        className = intent.getStringExtra("className");
        mapEntity = (MapEntity) intent.getSerializableExtra("map");
        if (DbmListActivity.BRIDGE.equals(className)) {
            bridge = (Bridge) intent.getSerializableExtra(DbmListActivity.CARD_DATA);
            sessionId = bridge.sessionId;
            tv1.setText("主管负责人：" + (bridge.mainPrincipal == null ? "" : bridge.mainPrincipal));
            tv2.setText("填卡人：" + bridge.writeUser.name);
            tv3.setText("填卡日期：" + DateUtils.convertDate2(bridge.fillcardDate == null ? "" : bridge.fillcardDate));
            tv5.setText("备注:" + (bridge.memo == null ? "" : bridge.memo));

            //技术状况
            List<BridgeTech> bridgeTechs = bridge.bridgeTechs;
            List<String> bridgeTechString = new ArrayList<String>();
            if (bridgeTechs != null && bridgeTechs.size() > 0) {
                if (bridgeTechs != null && bridgeTechs.size() > 0) {
                    for (BridgeTech b : bridgeTechs) {
                        if (!TextUtils.isEmpty(b.checkYear)) {
                            bridgeTechString.add(DateUtils.convertDate(b.checkYear));

                        }
                    }
                }

            }

            //修建日期
            List<BridgeRecords> bridgeRecordses = bridge.bridgeRecords;
            List<String> recordsString = new ArrayList<>();
            if (bridgeRecordses != null && bridgeRecordses.size() > 0) {
                for (int i = 0; i < bridgeRecordses.size(); i++) {
                    recordsString.add("第" + String.valueOf(i + 1) + "条记录");
                }
            }
            childMap1.put(groupName[0], bridge.getLevelA());
            childMap1.put(groupName[1], bridge.getLevelB());
            childMap1.put(groupName[2], bridge.getLevelC());
            childMap1.put(groupName[3], bridgeTechString);
            childMap1.put(groupName[4], recordsString);
            String[] bridgeA = this.getResources().getStringArray(R.array.bridge_a);
            String[] bridgeB = this.getResources().getStringArray(R.array.bridge_b);
            String[] bridgeC = this.getResources().getStringArray(R.array.bridge_c);
            childMap2.put(groupName[0], Arrays.asList(bridgeA));
            childMap2.put(groupName[1], Arrays.asList(bridgeB));
            childMap2.put(groupName[2], Arrays.asList(bridgeC));
            childMap2.put(groupName[3], new ArrayList<String>());
            childMap2.put(groupName[4], new ArrayList<String>());
            expandableAdapter = new MyExpandableAdapter(this, groupName, childMap1, childMap2, R.layout.item_expanlist_group_2, R.layout.item_expandblelistview_child_2);
            expandableAdapter.setOnChildItemClickListener(this);
            expandableListView.setAdapter(expandableAdapter);

        } else if (DbmListActivity.TUNNEL.equals(className)) {
            tunnel = (Tunnel) intent.getSerializableExtra(DbmListActivity.CARD_DATA);
            sessionId = tunnel.sessionId;
            tv1.setText("主管负责人：" + (tunnel.mainPrincipal == null ? "" : tunnel.mainPrincipal));
            tv2.setText("填卡人：" + tunnel.writeUser.name);
            tv3.setText("填卡日期：" + DateUtils.convertDate2(tunnel.fillCardDate == null ? "" : tunnel.fillCardDate));
            tv5.setText("备注:" + (tunnel.memo == null ? "" : tunnel.memo));
            photoContainer.setVisibility(View.GONE); //隧道卡片没有正面照
            //技术状况评定
            List<TunnelTech> tunnelTechs = tunnel.tunnelTechs;
            List<String> tunnelTechsString = new ArrayList<String>();
            if (tunnelTechs != null && tunnelTechs.size() > 0) {
                if (tunnelTechs != null && tunnelTechs.size() > 0) {
                    for (TunnelTech t : tunnelTechs) {
                        if (!TextUtils.isEmpty(t.checkYear)) {
                            tunnelTechsString.add(DateUtils.convertDate(t.checkYear));
                        }
                    }
                }
            }

            //修建记录
            List<TunnelRecords> tunnelRecordses = tunnel.tunnelRecords;
            List<String> recordsString = new ArrayList<>();
            if (tunnelRecordses != null && tunnelRecordses.size() > 0) {
                for (int i = 0; i < tunnelRecordses.size(); i++) {
                    recordsString.add("第" + String.valueOf(i + 1) + "条记录");
                }
            }

            childMap1.put(groupName[0], tunnel.getLevelA());
            childMap1.put(groupName[1], tunnel.getLevelB());
            childMap1.put(groupName[2], tunnel.getLevelC());
            childMap1.put(groupName[3], tunnelTechsString);
            childMap1.put(groupName[4], recordsString);
            String[] tunnelA = this.getResources().getStringArray(R.array.tunnel_a);
            String[] tunnelB = this.getResources().getStringArray(R.array.tunnel_b);
            String[] tunnelC = this.getResources().getStringArray(R.array.tunnel_c);
            childMap2.put(groupName[0], Arrays.asList(tunnelA));
            childMap2.put(groupName[1], Arrays.asList(tunnelB));
            childMap2.put(groupName[2], Arrays.asList(tunnelC));
            expandableAdapter = new MyExpandableAdapter(this, groupName, childMap1, childMap2, R.layout.item_expanlist_group_2, R.layout.item_expandblelistview_child_2);
            expandableAdapter.setOnChildItemClickListener(this);
            expandableListView.setAdapter(expandableAdapter);
        }

        NestedScrollViewUtil.setExpandableListViewHeightBasedOnChildren(expandableListView);
        layout.setTitle(intent.getStringExtra("title"));
        initEvent();

    }

    private void initEvent() {

        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                NestedScrollViewUtil.setExpandableListViewHeightBasedOnChildren(expandableListView);
            }
        });

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                NestedScrollViewUtil.setExpandableListViewHeightBasedOnChildren(expandableListView);
            }
        });

        expandableListView.setOnChildClickListener(new OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                if (groupPosition == 3 && DbmListActivity.BRIDGE.equals(className)) {
//                    popup_title.setText("技术状况评定");
//
//                    String [] subtitle = getResources().getStringArray(R.array.bridgeTech);
//                    List<String> content = bridge.bridgeTechs.get(childPosition).getList();
//                    DetailAdapter adapter = new DetailAdapter(CardActivity.this,R.layout.item_detail_display,subtitle,content);
////                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(CardActivity.this,
////                            R.layout.item_popup, array);
//                    popup_listview.setAdapter(adapter);
//                    popupWindowUtils = new PopupWindowUtils(CardActivity.this,mScreenWidth,mScreenHeight,popup_layout,layout);
                } else if (groupPosition == 4 && DbmListActivity.BRIDGE.equals(className)) {
                    popup_title.setText("修建记录");
                    String[] subtitle = getResources().getStringArray(R.array.bridgeRecord);
                    List<String> content = bridge.bridgeRecords.get(childPosition).getList();
                    DetailAdapter adapter = new DetailAdapter(CardActivity.this, R.layout.item_detail_display, subtitle, content);
                    popup_listview.setAdapter(adapter);
                    popupWindowUtils = new PopupWindowUtils(CardActivity.this, mScreenWidth, mScreenHeight, popup_layout, layout);
                } else if (groupPosition == 4 && DbmListActivity.TUNNEL.equals(className)) {
                    popup_title.setText("修建记录");
                    String[] subtitle = getResources().getStringArray(R.array.tunnelRecord);
                    List<String> content = tunnel.tunnelRecords.get(childPosition).getList();
                    DetailAdapter adapter = new DetailAdapter(CardActivity.this, R.layout.item_detail_display, subtitle, content);
                    popup_listview.setAdapter(adapter);
                    popupWindowUtils = new PopupWindowUtils(CardActivity.this, mScreenWidth, mScreenHeight, popup_layout, layout);
                }
                return true;
            }
        });

        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CardActivity.this, OnePictureDisplayActivity.class);
                intent.putExtra("url", MyConstants.PRE_URL + bridge.verticalView);
                startActivity(intent);
            }
        });
        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CardActivity.this, OnePictureDisplayActivity.class);
                intent.putExtra("url", MyConstants.PRE_URL + bridge.frontalView);
                startActivity(intent);
            }
        });
    }

    private void initView() {
        layout = (TitleLayout) findViewById(R.id.mytitle);
        expandableListView = (ExpandableListView) findViewById(R.id.card_activity_expandlistview);
        loadDialog = MyDialogUtil.createDialog(CardActivity.this,
                "正在加载..");
        childMap1 = new HashMap<String, List<String>>();
        childMap2 = new HashMap<String, List<String>>();
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        mScreenWidth = metrics.widthPixels;
        mScreenHeight = metrics.heightPixels;

        popup_layout = LayoutInflater.from(this).inflate(R.layout.popup_layout, null, false);
        popup_listview = (ListView) popup_layout.findViewById(R.id.popup_listview);
        popup_title = (TextView) popup_layout.findViewById(R.id.popup_title);

        tv1 = (TextView) findViewById(R.id.tv1);
        tv2 = (TextView) findViewById(R.id.tv2);
        tv3 = (TextView) findViewById(R.id.tv3);
        tv4 = (TextView) findViewById(R.id.tv4);
        tv5 = (TextView) findViewById(R.id.tv5);
        img1 = (ImageView) findViewById(R.id.img1);
        img2 = (ImageView) findViewById(R.id.img2);


        photoContainer = (LinearLayout) findViewById(R.id.photoContainer);
        presenter = new CardAPresenter(this);

        fab_attachment = (FloatingActionButton) findViewById(R.id.fab_attachment);
        fab_loaction = (FloatingActionButton) findViewById(R.id.fab_location);
        fab_gps = (FloatingActionButton) findViewById(R.id.fab_gps);
        fab_attachment.setOnClickListener(this);
        fab_loaction.setOnClickListener(this);
        fab_gps.setOnClickListener(this);

    }


    @Override
    public void onChildItemClick(View view, int groupPosition, int childPosition) {
        //判断是否是技术状况评定
        if (view instanceof Button && groupPosition == 3 && DbmListActivity.BRIDGE.equals(className)) {
            popup_title.setText("技术状况评定");
            String[] subtitle = getResources().getStringArray(R.array.bridgeTech);
            List<String> content = bridge.bridgeTechs.get(childPosition).getList();
            DetailAdapter adapter = new DetailAdapter(CardActivity.this, R.layout.item_detail_display, subtitle, content);
            popup_listview.setAdapter(adapter);
            popupWindowUtils = new PopupWindowUtils(CardActivity.this, mScreenWidth, mScreenHeight, popup_layout, layout);
        } else if (view instanceof EditText && groupPosition == 1) {
            String title = childMap2.get(groupName[groupPosition]).get(childPosition);
            showSupSubDialog(title);

        } else if (view instanceof Button && groupPosition == 3 && DbmListActivity.TUNNEL.equals(className)) {
            popup_title.setText("技术状况评定");
            String[] subtitle = getResources().getStringArray(R.array.tunnelTech);
            List<String> content = tunnel.tunnelTechs.get(childPosition).getList();
            DetailAdapter adapter = new DetailAdapter(CardActivity.this, R.layout.item_detail_display, subtitle, content);
            popup_listview.setAdapter(adapter);
            popupWindowUtils = new PopupWindowUtils(CardActivity.this, mScreenWidth, mScreenHeight, popup_layout, layout);
        }
    }

    public void showSupSubDialog(String title) {
        Dialog dialog = new Dialog(this);
        dialog.setTitle(title);
        View v = null;
        SupSubAdapter a = null;
        if ("上部结构".equals(title)) {
            v = LayoutInflater.from(this).inflate(R.layout.bridge_sup_construct, null, false);
            a = new SupSubAdapter<>(this, bridge.bridgeSupStructures);
        } else if ("下部结构".equals(title)) {
            v = LayoutInflater.from(this).inflate(R.layout.bridge_sub_construct, null, false);
            a = new SupSubAdapter<>(this, bridge.bridgeSubStructures);

        }
        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.recycleView);
        recyclerView.setAdapter(a);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.setContentView(v);
        //获取屏幕宽高
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        Window dialogWindown = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindown.getAttributes();
        lp.width = (int) (metrics.widthPixels * 0.7);
        lp.height = (int) (metrics.heightPixels * 0.7);
        dialogWindown.setAttributes(lp);
        dialog.show();
    }

    public void showSheetDialog() {
        if (!TextUtils.isEmpty(sessionId)) {
            presenter.getData(this, sessionId);
        } else {
            Toast.makeText(this, "无附件", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showDialog() {
        loadDialog.show();
    }

    @Override
    public void dismssDialog() {
        loadDialog.dismiss();
    }

    @Override
    public void loadDataSuccess(Object o) {

    }

    @Override
    public void loadDataSuccess(Object o, int type) {

    }

    @Override
    public void loadDataSuccess(Object object, String className) {
        switch (className) {
            case "Attachment":
                final List<Attachment> attachments = (List<Attachment>) object;
                final BottomSheetDialog dialog = new BottomSheetDialog(this);
                ListView listView = new ListView(this);
                String[] fileNames = new String[attachments.size()];
                for (int i = 0; i < attachments.size(); i++) {
                    fileNames[i] = attachments.get(i).fileName;
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, fileNames);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String ids = attachments.get(position).id;
                        String fileName = attachments.get(position).fileName;
                        String filePath = MyConstants.PRE_URL + attachments.get(position).filePath;
                        if (fileName.contains(".jpg") || fileName.contains(".png") || fileName.contains(".jpeg")) {
                            Intent intent = new Intent(CardActivity.this, OnePictureDisplayActivity.class);
                            intent.putExtra("url", filePath);
                            startActivity(intent);
                        } else {
                            //使用自带的下载器下载文件
                            // TODO: 2016/10/27
                            CommonUtils.downFile(CardActivity.this, filePath, fileName, ids);
                            dialog.dismiss();


                        }

                    }
                });
                dialog.setContentView(listView);
                dialog.show();
                break;
        }
    }

    @Override
    public void loadDataFailure() {
        Toast.makeText(this, "连接服务器出错", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void hasError() {
        UserUtils.reLogin(CardActivity.this, loadDialog);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_attachment:
                showSheetDialog();
                break;
            case R.id.fab_location:
                Intent intent = new Intent(this, MapQueryActivity.class);
                intent.putExtra("map", mapEntity);
                startActivity(intent);
                break;
            case R.id.fab_gps:
                //判断是否打开gps
                CommonUtils.getGpsLocation(this, handler);
                break;
        }
        FloatingActionsMenu parent = (FloatingActionsMenu) v.getParent();
        parent.toggle();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isFirstLoadPicture){
            HttpclientUtil.getData(CardActivity.this,MyConstants.PRE_URL+"mt/common/zoomImage.action?imagePath="+
            bridge.verticalView+",,,"+bridge.frontalView,handler,0);
        }
    }
}
