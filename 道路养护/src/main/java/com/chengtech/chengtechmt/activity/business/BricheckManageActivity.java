package com.chengtech.chengtechmt.activity.business;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.chengtech.chengtechmt.BaseActivity;
import com.chengtech.chengtechmt.R;
import com.chengtech.chengtechmt.adapter.RecycleViewAdapter2;
import com.chengtech.chengtechmt.entity.Route;
import com.chengtech.chengtechmt.entity.Section;
import com.chengtech.chengtechmt.entity.Tree;
import com.chengtech.chengtechmt.entity.patrol.BriOftenCheck;
import com.chengtech.chengtechmt.entity.patrol.CulvertOftenCheck;
import com.chengtech.chengtechmt.entity.patrol.TunnelOftenCheck;
import com.chengtech.chengtechmt.fragment.SubMenuDialogFragment;
import com.chengtech.chengtechmt.impl.OnItemClickListener;
import com.chengtech.chengtechmt.thirdlibrary.wxpictureselector.PictureSelectorActivity;
import com.chengtech.chengtechmt.util.DateUtils;
import com.chengtech.chengtechmt.util.DeptSpinnerUtil;
import com.chengtech.chengtechmt.util.HttpclientUtil;
import com.chengtech.chengtechmt.util.LogUtils;
import com.chengtech.chengtechmt.util.MyConstants;
import com.chengtech.nicespinner.NiceSpinner;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 桥隧涵经常检查
 */
public class BricheckManageActivity extends BaseActivity implements OnItemClickListener, View.OnClickListener, SubMenuDialogFragment.ExchangeDataListener {
    public static final String BIR_OFTEN_CHECK = "briOftenCheck"; //桥梁经常检查
    public static final String CUL_OFTEN_CHECK = "culvertOftenCheck";//涵洞经常检查
    public static final String TUN_OFTEN_CHECK = "tunnelOftenCheck"; //隧道经常检查
    public String MODEL_TYPE;
    public String[] modelName = new String[]{
            "表2-1桥梁经常(汛期)检查记录表", "表2-3涵洞经常(汛期)检查记录表", "表2-5隧道经常检查记录表"
    };
    private AlertDialog deptDialog;
    private AlertDialog menuDialog;
    SubMenuDialogFragment menuDialogFragment;
    private Map<String, String> name2Id = new HashMap<>(); //根据选择的内容查找对应id，如：部门名称对应这部门id
    private Map<String, String> id2Name = new HashMap<>(); //根据选择的id查找对应单位名，如：部门名称对应这部门id
    private List<Tree> itemTree = new ArrayList<>();
    private Map<String, List<String>> linkContent = new HashMap<>(); //联动内容
    private List<String> firstDept = new ArrayList<>();//管理局
    private List<String> secondDept = new ArrayList<>();//管理单位
    private List<String> thirdDept = new ArrayList<>();//养护单位
    private String deptUrl = MyConstants.PRE_URL + "mt/common/deptRouteSectionLinkageJson.action";
    private String[] title = new String[]{"管理局", "管理单位", "养护单位", "路线", "区间路段", "年份", "月份"};
    public RecyclerView recyclerView;
    public List<BriOftenCheck> briOftenCheckList;
    public List<CulvertOftenCheck> culOftenCheckList;
    public List<TunnelOftenCheck> tulOftenCheckList;
    public String tempUrl;//根据筛选后的条件组合成的链接

//    private String briOfcheckUrl = MyConstants.PRE_URL + "mt/business/patrol/bricheckmanage/briOftenCheck/listBriOftenCheckJson.action";
    private String briOfcheckUrl = MyConstants.PRE_URL + "mt/business/patrol/bricheckmanage/bridgeoftencheck/listBridgeOftenCheckJson.action";
    private String culOfcheckUrl = MyConstants.PRE_URL + "mt/business/patrol/culvertcheck/culvertOftenCheck/listCulvertOftenCheckJson.action";
//    private String tunOfcheckUrl = MyConstants.PRE_URL + "mt/business/patrol/tunnelcheck/tunnelOftenCheck/listTunnelOftenCheckJson.action";
    private String tunOfcheckUrl = MyConstants.PRE_URL + "mt/business/patrol/tunnelcheck/tunnelofteninspect/listTunnelOftenInspectJson.action";


    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String json = (String) msg.obj;
            Gson gson = new Gson();
            switch (msg.what) {
                case 1:
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        JSONArray deptJson = jsonObject.getJSONArray("deptJson");
                        for (int i = 0; i < deptJson.length(); i++) {
                            Tree tree = gson.fromJson(deptJson.getString(i), Tree.class);
                            name2Id.put(tree.text, tree.id);
                            id2Name.put(tree.id,tree.text);
                            itemTree.add(tree);
                            //部门类型分为三种，分别是：中心，养护所，养护站。因为中心的位置是联动的首项，先确定首项。
                            if (tree.deptType.equals("中心")) {
                                firstDept.add(tree.text);
                            }
                        }
                        linkContent.put("管理局", firstDept);
                        List<String> selectAll = new ArrayList<>();
                        selectAll.add("");
                        linkContent.put("全选", selectAll);
                        //循环联动内容的首项，来筛选联动第二项内容
                        for (int i = 0; i < firstDept.size(); i++) {
                            String deptName = firstDept.get(i);
                            String deptId = name2Id.get(deptName);
                            List<String> depts = new ArrayList<>();
                            depts.add("全选");
                            for (Tree tree : itemTree) {
                                if ((tree.parentId).equals(deptId)) {
                                    depts.add(tree.text);
                                }
                            }
                            linkContent.put(deptName, depts);
                        }

                        //联动的第三项
                        for (Tree tree : itemTree) {
                            if ("养护所".equals(tree.deptType)) {
                                List<String> dept = new ArrayList<>();
                                dept.add("全选");
                                for (Tree tree1 : itemTree) {
                                    if (tree.id.equals(tree1.parentId)) {
                                        dept.add(tree1.text);
                                    }
                                }
                                linkContent.put(tree.text, dept);
                            }
                        }

                        JSONArray listSection = jsonObject.getJSONArray("listSection");
                        if (listSection != null && listSection.length() > 0) {
                            for (int i = 0; i < listSection.length(); i++) {
                                String sectionJson = listSection.getString(i);
                                Section section = gson.fromJson(sectionJson, Section.class);
                                //把名称和id放进映射集合里
//                                Log.i("tag",section.route.name+section.route.code+section.route.id);
                                name2Id.put(section.name, section.id);
                                Route route = section.route;
                                if (route == null) {
                                    name2Id.put(section.name, "没有路线？？");
                                } else {
                                    name2Id.put(section.route.name + "(" + section.route.code + ")", section.route.id);
                                    //设置路线与路段的联动关系
                                    String mtDeptName = section.mtDept.name; //养护单位名称
                                    String routeName = section.route.name + "(" + section.route.code + ")";//路线名称
                                    //使用养护单位名称+路线名称，来确定唯一的区间路段
                                    List<String> list = null;
                                    list = linkContent.get(routeName + mtDeptName);
                                    if (list == null) {
                                        list = new ArrayList<>();
                                    }
                                    list.add(section.name);
                                    linkContent.put(routeName + mtDeptName, list);

                                    //设置养护站与对应的路线的联动关系

                                    List<String> list2 = null;
                                    list2 = linkContent.get(mtDeptName);
                                    if (list2 == null) {
                                        list2 = new ArrayList<>();
                                        list2.add("全选");
                                    }
                                    list2.add(routeName);
                                    linkContent.put(mtDeptName, list2);
                                }


                            }
                        }

                        //年份，月份
                        String[] year = new String[]{"全选", "2014", "2015", "2016", "2017", "2018", "2019", "2020",};
                        String[] month = new String[]{"全选", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12",};
                        linkContent.put("年份", Arrays.asList(year));
                        linkContent.put("月份", Arrays.asList(month));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        JSONArray rows = jsonObject.getJSONArray("rows");
                        briOftenCheckList = new ArrayList<>();
                        if (rows != null && rows.length() > 0) {
                            for (int i = 0; i < rows.length(); i++) {
                                String content = rows.getString(i);
                                BriOftenCheck briOftenCheck = gson.fromJson(content, BriOftenCheck.class);
                                briOftenCheckList.add(briOftenCheck);
                            }
                        }
                        List<String[]> titleArrayList = new ArrayList<>();
                        if (briOftenCheckList.size() == pageSize)
                            maxPage++;
                        if (briOftenCheckList.size() > 0 && briOftenCheckList != null) {
                            for (BriOftenCheck briOftenCheck : briOftenCheckList) {
                                String[] titleArray = new String[6];
                                titleArray[0] = briOftenCheck.briName;
                                titleArray[1] = "路线名称:" + briOftenCheck.routeName;
                                titleArray[2] = "<br/>桥梁编号:  " + briOftenCheck.brino;
                                titleArray[3] = "<br/>桥位桩号:  " + briOftenCheck.bripeg;
                                titleArray[4] = "<br/>巡查时间:  " + (briOftenCheck.checkDate==null?"":(DateUtils.convertDate2(briOftenCheck.checkDate)));
                                titleArray[5] = "<br/>巡查单位:  " + id2Name.get(briOftenCheck.checkDeptId);
                                titleArrayList.add(titleArray);
                            }
                        } else {
                            Toast.makeText(BricheckManageActivity.this, "没有数据", Toast.LENGTH_SHORT).show();
                            if (pageNo!=1)
                                pageNo--;
                            pageNo_tv.setText(pageNo + "");
                        }
                        RecycleViewAdapter2 adapter = new RecycleViewAdapter2(BricheckManageActivity.this, titleArrayList, R.layout.item_recycle3);
                        adapter.setOnItemClickListener(BricheckManageActivity.this);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(BricheckManageActivity.this));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                //涵洞
                case 3:
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        JSONArray rows = jsonObject.getJSONArray("rows");
                        culOftenCheckList = new ArrayList<>();
                        if (rows != null && rows.length() > 0) {
                            for (int i = 0; i < rows.length(); i++) {
                                String content = rows.getString(i);
                                CulvertOftenCheck culvertOftenCheck = gson.fromJson(content, CulvertOftenCheck.class);
                                culOftenCheckList.add(culvertOftenCheck);
                            }
                        }
                        List<String[]> titleArrayList = new ArrayList<>();
                        if (culOftenCheckList.size() == pageSize)
                            maxPage++;
                        if (culOftenCheckList.size() > 0 && culOftenCheckList != null) {
                            for (CulvertOftenCheck culvertOftenCheck : culOftenCheckList) {
                                String[] titleArray = new String[5];
                                titleArray[0] = culvertOftenCheck.routeCode;
                                titleArray[1] = "路线名称:" + culvertOftenCheck.routeName;
                                titleArray[2] = "<br/>涵洞编号:  " + culvertOftenCheck.culvertCode;
                                titleArray[3] = "<br/>巡查时间:  " + (culvertOftenCheck.checkDate==null?"":
                                DateUtils.convertDate2(culvertOftenCheck.checkDate));
                                titleArray[4] = "<br/>养护单位:  " + id2Name.get(culvertOftenCheck.thirdDeptId);
                                titleArrayList.add(titleArray);
                            }
                        } else {
                            Toast.makeText(BricheckManageActivity.this, "没有数据", Toast.LENGTH_SHORT).show();
                            if (pageNo!=1)
                                pageNo--;
                            pageNo_tv.setText(pageNo + "");
                        }
                        RecycleViewAdapter2 adapter = new RecycleViewAdapter2(BricheckManageActivity.this, titleArrayList, R.layout.item_recycle3);
                        adapter.setOnItemClickListener(BricheckManageActivity.this);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(BricheckManageActivity.this));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;

                case 4:
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        JSONArray rows = jsonObject.getJSONArray("rows");
                        tulOftenCheckList = new ArrayList<>();
                        if (rows != null && rows.length() > 0) {
                            for (int i = 0; i < rows.length(); i++) {
                                String content = rows.getString(i);
                                TunnelOftenCheck tunnelOftenCheck = gson.fromJson(content, TunnelOftenCheck.class);
                                tulOftenCheckList.add(tunnelOftenCheck);
                            }
                        }
                        List<String[]> titleArrayList = new ArrayList<>();
                        if (tulOftenCheckList.size() == pageSize)
                            maxPage++;
                        if (tulOftenCheckList.size() > 0 && tulOftenCheckList != null) {
                            for (TunnelOftenCheck tunnelOftenCheck : tulOftenCheckList) {
                                String[] titleArray = new String[4];
                                titleArray[0] = tunnelOftenCheck.name;
                                titleArray[1] = "路线名称:" + tunnelOftenCheck.routeCode;
                                titleArray[2] = "<br/>巡查时间:  " + DateUtils.convertDate2(tunnelOftenCheck.patrolDate);
                                titleArray[3] = "<br/>养护单位:  " + id2Name.get(tunnelOftenCheck.thirdDeptId);
                                titleArrayList.add(titleArray);
                            }
                        } else {
                            Toast.makeText(BricheckManageActivity.this, "没有数据", Toast.LENGTH_SHORT).show();
                            if (pageNo!=1)
                                pageNo--;
                            pageNo_tv.setText(pageNo + "");
                        }
                        RecycleViewAdapter2 adapter = new RecycleViewAdapter2(BricheckManageActivity.this, titleArrayList, R.layout.item_recycle3);
                        adapter.setOnItemClickListener(BricheckManageActivity.this);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(BricheckManageActivity.this));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;

                case DeptSpinnerUtil.FILTER_SUCCESS:
                    pageNo=1;
                    pageNo_tv.setText(pageNo+"");
                    String[] filterArray = new String[DeptSpinnerUtil.niceSpinners.size()];
                    for (int i = 0; i < DeptSpinnerUtil.niceSpinners.size(); i++) {
                        NiceSpinner niceSpinner = DeptSpinnerUtil.niceSpinners.get(i);
                        String filter = niceSpinner.getText().toString();
                        if (filter == null || filter == "" || filter == "全选") {
                            filterArray[i] = "";
                        } else {
                            filterArray[i] = (name2Id.get(filter) == null ? filter : name2Id.get(filter));
                        }
                    }
                    if (BIR_OFTEN_CHECK.equals(MODEL_TYPE)) {
                        tempUrl = briOfcheckUrl + "?firstDeptId=" + filterArray[0] +
                                "&secondDeptId=" + filterArray[1] + "&thirdDeptId=" + filterArray[2] +
                                "&routeId=" + filterArray[3] + "&sectionId=" + filterArray[4] + "&year=" + filterArray[5] +
                                "&month=" + filterArray[6] + "&sort=checkDate&direction=desc&pager.pageSize=100";
                        HttpclientUtil.getData(BricheckManageActivity.this, tempUrl + "&pager.pageNo=" + pageNo, handler, 2);

                    } else if (CUL_OFTEN_CHECK.equals(MODEL_TYPE)) {
                        tempUrl = culOfcheckUrl + "?firstDeptId=" + filterArray[0] +
                                "&secondDeptId=" + filterArray[1] + "&thirdDeptId=" + filterArray[2] +
                                "&routeId=" + filterArray[3] + "&sectionId=" + filterArray[4] + "&year=" + filterArray[5] +
                                "&month=" + filterArray[6] + "&sort=checkDate&direction=desc&pager.pageSize=100";
                        HttpclientUtil.getData(BricheckManageActivity.this, tempUrl + "&pager.pageNo=" + pageNo, handler, 3);
                    } else if (TUN_OFTEN_CHECK.equals(MODEL_TYPE)) {
                        tempUrl = tunOfcheckUrl + "?firstDeptId=" + filterArray[0] +
                                "&secondDeptId=" + filterArray[1] + "&thirdDeptId=" + filterArray[2] +
                                "&routeId=" + filterArray[3] + "&sectionId=" + filterArray[4] + "&year=" + filterArray[5] +
                                "&month=" + filterArray[6] + "&sort=checkDate&direction=desc&pager.pageSize=100";
                        HttpclientUtil.getData(BricheckManageActivity.this, tempUrl + "&pager.pageNo=" + pageNo, handler, 4);
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addContentView(R.layout.activity_bricheck_manage);
        setNavigationIcon(true);

        initView();
        showMenuDialog();
    }

    private void showMenuDialog() {
        if (menuDialogFragment == null) {
            menuDialogFragment = new SubMenuDialogFragment();
            List<com.chengtech.chengtechmt.entity.Menu> menuList = new ArrayList<>();
            for (int i = 0; i < modelName.length; i++) {
                com.chengtech.chengtechmt.entity.Menu menu = new com.chengtech.chengtechmt.entity.Menu();
                menu.itemName = modelName[i];
                menuList.add(menu);
            }
            Bundle bundle = new Bundle();
            bundle.putSerializable("data", (Serializable) menuList);
            menuDialogFragment.setArguments(bundle);
        }
        menuDialogFragment.show(getFragmentManager(), "SubMenuDialogFragment");
    }

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        up_action.setOnClickListener(this);
        down_action.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_bricheck_manage, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuId = item.getItemId();
        if (menuId == R.id.filter) {
            if (deptDialog == null) {
                deptDialog = DeptSpinnerUtil.createDeptSpinner2(this, handler, title, linkContent, new boolean[]{true, true, true, true, false, false});
                if (MODEL_TYPE==null)
                    Toast.makeText(this, "请先选择模块。", Toast.LENGTH_SHORT).show();
            }
            if (deptDialog != null)
                deptDialog.show();
        } else if (menuId == R.id.model) {
            showMenuDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(this, OftenCheckDetailActivity.class);
        switch (MODEL_TYPE) {
            case BIR_OFTEN_CHECK:
                intent.putExtra("title", "桥梁检查记录");
                intent.putExtra("data", briOftenCheckList.get(position));
                startActivity(intent);
                break;
            case CUL_OFTEN_CHECK:
                intent.putExtra("title", "涵洞检查记录");
                intent.putExtra("data", culOftenCheckList.get(position));
                startActivity(intent);
                break;
            case TUN_OFTEN_CHECK:
                intent.putExtra("title", "隧道检查记录");
                intent.putExtra("data", tulOftenCheckList.get(position));
                startActivity(intent);
                break;
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.perPage:
                if (pageNo != 1 && !TextUtils.isEmpty(tempUrl)) {
                    pageNo--;
                    pageNo_tv.setText(pageNo + "");
                    HttpclientUtil.getData(BricheckManageActivity.this, tempUrl + "&pager.pageNo=" + pageNo, handler, 2);
                } else {
                    Toast.makeText(this, "当前是最新页", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.nextPage:
                if (pageNo < maxPage && !TextUtils.isEmpty(tempUrl)) {
                    pageNo++;
                    pageNo_tv.setText(pageNo + "");
                    HttpclientUtil.getData(BricheckManageActivity.this, tempUrl + "&pager.pageNo=" + pageNo, handler, 2);
                } else {
                    Toast.makeText(this, "当前已经是最后一页", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onExchangData(Object object) {
        String itemName = (String) object;
        switch (itemName) {
            case "表2-1桥梁经常(汛期)检查记录表":
                toolbar.setTitle("桥梁经常(汛期)检查");
                MODEL_TYPE = BIR_OFTEN_CHECK;
                recyclerView.setAdapter(null);
                break;
            case "表2-3涵洞经常(汛期)检查记录表":
                toolbar.setTitle("涵洞经常(汛期)检查");
                MODEL_TYPE = CUL_OFTEN_CHECK;
                recyclerView.setAdapter(null);
                break;
            case "表2-5隧道经常检查记录表":
                toolbar.setTitle("隧道经常检查");
                MODEL_TYPE = TUN_OFTEN_CHECK;
                recyclerView.setAdapter(null);
                break;
        }
        if (itemTree == null || itemTree.size() == 0)
            HttpclientUtil.getData(BricheckManageActivity.this, deptUrl, handler, 1);
    }

    /**
     * 新增一个条目
     * @param view
     */
    public void onAddNewItem(View view) {

        if (DeptSpinnerUtil.niceSpinners.size()==0) {
            Toast.makeText(this, "请选择筛选条件", Toast.LENGTH_SHORT).show();
            return;
        }
        String [] filterArray = new String[DeptSpinnerUtil.niceSpinners.size()];
        for (int i = 0; i < DeptSpinnerUtil.niceSpinners.size(); i++) {
            NiceSpinner niceSpinner = DeptSpinnerUtil.niceSpinners.get(i);
            String filter = niceSpinner.getText().toString();
            if (filter == null || filter == "" || filter == "全选") {
                filterArray[i] = "";
            } else {
                filterArray[i] = (name2Id.get(filter) == null ? filter : name2Id.get(filter));
            }
        }
        for(int i=0;i<DeptSpinnerUtil.niceSpinners.size();i++) {
            if (i==1) {
                String string = DeptSpinnerUtil.niceSpinners.get(i).getText().toString();
                if (string==null || string=="全选" || string=="") {
                    Toast.makeText(this, "请选择管理单位", Toast.LENGTH_SHORT).show();
                    break;
                }
            }
            if (i==2) {
                String string = DeptSpinnerUtil.niceSpinners.get(i).getText().toString();
                if (string==null || string=="全选" || string=="") {
                    Toast.makeText(this, "请选择养护单位", Toast.LENGTH_SHORT).show();
                    break;
                }
            }
            if (i==3) {
                String string = DeptSpinnerUtil.niceSpinners.get(i).getText().toString();
                if (string==null || string=="全选" || string=="") {
                    Toast.makeText(this, "请选择路线", Toast.LENGTH_SHORT).show();
                    break;
                }
            }
            if (i==4) {
                String string = DeptSpinnerUtil.niceSpinners.get(i).getText().toString();
                if (string==null || string=="全选" || string=="") {
                    Toast.makeText(this, "请选择区间路段", Toast.LENGTH_SHORT).show();
                    break;
                }



                String filter =
                        "firstDeptId="+filterArray[0]+
                        "&secondDeptId="+filterArray[1]+
                        "&thirdDeptId="+filterArray[2]+"&routeId="+filterArray[3]+
                        "&sectionId="+filterArray[4]+"&year="+filterArray[5]+
                        "&month="+filterArray[6];

                if (MODEL_TYPE.equals(BIR_OFTEN_CHECK)) {
                    BridgeOftenCheckAddActivity.startAction(this,filter);
                }else if (MODEL_TYPE.equals(CUL_OFTEN_CHECK)){
                    CulvertOftenCheckAddActivity.startAction(this,filter);
                }else if (MODEL_TYPE.equals(TUN_OFTEN_CHECK)) {
                    TunnelOftenCheckAddActivity.startAction(this,filter);
                }

            }
        }
    }



}
