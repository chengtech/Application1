package com.chengtech.chengtechmt.fragment.gis;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.chengtech.chengtechmt.R;
import com.chengtech.chengtechmt.adapter.gis.GisListRouteAdapter;
import com.chengtech.chengtechmt.entity.Dept;
import com.chengtech.chengtechmt.entity.Route;
import com.chengtech.chengtechmt.entity.gson.DeptG;
import com.chengtech.chengtechmt.entity.gson.RouteG;
import com.chengtech.chengtechmt.util.DateUtils;
import com.chengtech.chengtechmt.util.HttpclientUtil;
import com.chengtech.chengtechmt.util.MyConstants;
import com.chengtech.nicespinner.NiceSpinner;
import com.chengtech.nicespinner.NiceSpinnerAdapter;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

/**
 * 作者: LiuFuYingWang on 2018/1/3 16:02.
 * 病害分析情况-筛选条件的fragment
 */

public class DiseaseRecordDialogFragment extends DialogFragment implements View.OnClickListener {
    private View rootView;
    private DeptG deptG;
    private NiceSpinner firstS;
    private NiceSpinner secondS;
    private NiceSpinner thirdSpinner;
    private NiceSpinner yearSpinner;
    private NiceSpinner implementSituationSpinner;
    private List<String> firstDept = new ArrayList<>();
    private Map<String, List<String>> secondDept = new HashMap<>();
    private Map<String, List<String>> thirdDept = new HashMap<>();
    private String firstDeptId;
    private String secondDeptId;
    private String thirdDeptId;
    public final int LIST_ROUTE = 0;
    private DisplayMetrics displayMetrics;
    private ExchangeDataListener listener;
    private HashMap<String, Boolean> selectedPosMap = new HashMap<>();
    private Context mContext;
    public TextView routeName;
    public List<Route> routes;
    public int firstPosition;
    public int secondPosition;
    public String[] implementSituationItem = new String[]{
            "请选择", "未处理", "处理中", "已处理"
    };
    public String[] yearItem = new String[]{
            "请选择", "2014", "2015", "2016", "2017", "2018"
    };

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String json = (String) msg.obj;
            Gson gson = new Gson();
            switch (msg.what) {
                case LIST_ROUTE:
                    try {
                        RouteG routeG = gson.fromJson(json, RouteG.class);
                        routes = routeG.rows;
                        if (routes != null && routes.size() > 0) {
                            createMultiDialog(routes);
                        } else {
                            Toast.makeText(mContext, "无数据。", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {

                    }
                    break;

            }
        }
    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        displayMetrics = getActivity().getResources().getDisplayMetrics();
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.diseaserecord_dialog_fragment, container, true);
            mContext = getActivity();
            firstS = (NiceSpinner) rootView.findViewById(R.id.firstDept);
            secondS = (NiceSpinner) rootView.findViewById(R.id.secondDept);
            thirdSpinner = (NiceSpinner) rootView.findViewById(R.id.thirdDept);
            yearSpinner = (NiceSpinner) rootView.findViewById(R.id.year);
            implementSituationSpinner = (NiceSpinner) rootView.findViewById(R.id.implementSituation);
            routeName = (TextView) rootView.findViewById(R.id.routeNames);
            routeName.setOnClickListener(this);
            Button search = (Button) rootView.findViewById(R.id.search);
            search.setOnClickListener(this);
        }

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        deptG = (DeptG) bundle.getSerializable("Dept");
        setSpinnerData(deptG);
        inflaterSpnnier();
    }

    private void inflaterSpnnier() {
        firstS.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                firstPosition = position;
                firstDeptId = deptG.listFirstDept.get(position).id;
                secondS.attachDataSource(secondDept.get(firstDept.get(position)));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        secondS.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                secondPosition = position;
                String selectedDeptName = secondDept.get(firstDept.get(firstPosition)).get(position);

                if (selectedDeptName.equals("请选择")) {
                    secondDeptId = "";
                } else {
                    for (Dept d : deptG.listSecondDept) {
                        if (d.name.equals(selectedDeptName)) {
                            secondDeptId = d.id;
                            break;
                        }
                    }
                }
                if (thirdDept.get(selectedDeptName) == null) {
                    List<String> list = new ArrayList<String>();
                    list.add("请选择");
                    thirdSpinner.attachDataSource(list);
                } else {
                    thirdSpinner.attachDataSource(thirdDept.get(selectedDeptName));
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        thirdSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedDeptName =
                        thirdDept.get(secondDept.get(firstDept.get(firstPosition)).get(secondPosition))
                                .get(position);
                if (selectedDeptName == null || selectedDeptName.equals("请选择")) {
                    thirdDeptId = "";
                } else {
                    for (Dept d : deptG.listThirdDept) {
                        if (d.name.equals(selectedDeptName)) {
                            thirdDeptId = d.id;
                            break;
                        }
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        firstS.attachDataSource(firstDept);
//        int[] ints = DateUtils.calculateDate();
        yearSpinner.attachDataSource(Arrays.asList(yearItem));
        yearSpinner.setSelectedIndex(yearItem.length-1);
        implementSituationSpinner.attachDataSource(Arrays.asList(implementSituationItem));
    }

//    @Override
//    public void onClick(final View view) {
//        switch (view.getId()) {
//            case R.id.routeGrade:
//                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
//                builder.setMultiChoiceItems(routeGradeItems, routeGradeCheckeds, new DialogInterface.OnMultiChoiceClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
//
//                    }
//                });
//                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        StringBuffer sb = new StringBuffer();
//                        for (int i = 0; i < routeGradeCheckeds.length; i++) {
//                            if (routeGradeCheckeds[i])
//                                sb.append(routeGradeItems[i] + ",");
//                        }
//                        if (!TextUtils.isEmpty(sb)) {
//                            ((TextView) view).setText(sb.toString().substring(0, sb.length() - 1));
//                        } else {
//                            ((TextView) view).setText("");
//                        }
//
//
//                    }
//                });
//                builder.create().show();
//                break;
//
//            case R.id.routeNames:
//                String listRouteUrl = MyConstants.PRE_URL + "mt/integratequery/gisvisualization/listRouteInGisJson.action?routeGrade=";
//                String routeGrade = this.routeGrade.getText().toString().trim();
//                if (!TextUtils.isEmpty(routeGrade)) {
//                    String[] split = routeGrade.split(",");
//                    for (int i = 0; i < split.length; i++) {
//                        listRouteUrl = listRouteUrl + split[i].substring(0, 1) + ",";
//                    }
//                }
//                HttpclientUtil.getData(mContext, listRouteUrl, handler, LIST_ROUTE);
//                break;
//
//            case R.id.projectItemType:
//                AlertDialog.Builder builder2 = new AlertDialog.Builder(mContext);
//                builder2.setSingleChoiceItems(projectItemTypeItem, projectItemTypeCheckeds, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        projectItemTypeCheckeds = which;
//                    }
//                });
//                builder2.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        projectItemType_tv.setText(projectItemTypeItem[projectItemTypeCheckeds]);
//                    }
//                });
//                builder2.create().show();
//                break;
//
//            case R.id.year:
//                AlertDialog.Builder builder3 = new AlertDialog.Builder(mContext);
//                builder3.setSingleChoiceItems(yearItem, yearCheckeds, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        yearCheckeds = which;
//                    }
//                });
//                builder3.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        year_tv.setText(yearItem[yearCheckeds]);
//                    }
//                });
//                builder3.create().show();
//                break;
//
//            case R.id.implementSituation:
//                AlertDialog.Builder builder4 = new AlertDialog.Builder(mContext);
//                builder4.setSingleChoiceItems(implementSituationItem, implementSituationCheckeds, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        implementSituationCheckeds = which;
//                    }
//                });
//                builder4.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        implementSituation_tv.setText(implementSituationItem[implementSituationCheckeds]);
//                    }
//                });
//                builder4.create().show();
//                break;
//
//            case R.id.search:
//                String routeGradeStr = this.routeGrade.getText().toString().trim();
//                String routeNameStr = this.routeName.getText().toString().trim();
//                String projectItemTypeStr = this.projectItemType_tv.getText().toString().trim();
//                String implementSituationStr = this.implementSituation_tv.getText().toString().trim();
//                String year = year_tv.getText().toString().trim();
//                if ("请选择".equals(year))
//                    year = "";
//                if ("请选择".equals(implementSituationStr))
//                    implementSituationStr = "";
//                if ("请选择".equals(projectItemTypeStr))
//                    projectItemTypeStr = "";
//                StringBuffer routeGradeCache = new StringBuffer();
//                if (!TextUtils.isEmpty(routeGradeStr)) {
//                    String[] split = routeGradeStr.split(",");
//                    for (int i = 0; i < split.length; i++) {
//                        split[i] = split[i].substring(0, 1);
//
//                        if (i == split.length - 1) {
//                            routeGradeCache.append(split[i]);
//                        } else {
//                            routeGradeCache.append(split[i] + ",");
//                        }
//                    }
//                }
//                StringBuffer routeNamesCache = new StringBuffer();
//                if (!TextUtils.isEmpty(routeNameStr)) {
//                    String[] split = routeNameStr.split(",");
//                    for (int i = 0; i < split.length; i++) {
//                        split[i] = split[i].substring(split[i].indexOf("(") + 1, split[i].length() - 1);
//                        if (i == split.length - 1) {
//                            routeNamesCache.append(split[i]);
//                        } else {
//                            routeNamesCache.append(split[i] + ",");
//                        }
//
//                    }
//
//                }
//
//                String filter = "routeGrade=" + routeGradeCache.toString() +
//                        "&routeCode=" + routeNamesCache.toString() + "&year=" + year + "&projectItemType=" + projectItemTypeStr + "&implementSituation=" + implementSituationStr;
//                filter = filter.replace("大中修", "ProjectManagement").replace("小额专项", "MediumPlanprogressItem")
//                        .replace("实施中", "0").replace("已完成", "1");
//                listener.onExchangData(filter);
//                dismiss();
//                break;
//        }
//    }

    private void createMultiDialog(List<Route> routes) {
        final GisListRouteAdapter adapter = new GisListRouteAdapter(routes, selectedPosMap);
        final RecyclerView recyclerView = new RecyclerView(mContext);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                StringBuffer sb = new StringBuffer();
                List<Route> checkedItems = adapter.getCheckedItems();
                for (int i = 0; i < checkedItems.size(); i++) {
                    sb.append(checkedItems.get(i).name + "(" + checkedItems.get(i).code + "),");
                }
                if (!TextUtils.isEmpty(sb)) {
                    routeName.setText(sb.toString());
                } else {
                    routeName.setText("");
                }
            }
        });
        builder.setView(recyclerView);
        builder.create().show();

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listener = (ExchangeDataListener) activity;
    }

    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setGravity(Gravity.CENTER);
        WindowManager.LayoutParams lp = getDialog().getWindow().getAttributes();
        lp.width = displayMetrics.widthPixels - 20;
        lp.height = (int) (displayMetrics.heightPixels * 0.7);
        getDialog().getWindow().setAttributes(lp);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.routeNames:
                if (routes==null) {
                    String listRouteUrl = MyConstants.PRE_URL + "mt/integratequery/gisvisualization/listRouteInGisJson.action?routeGrade=";
                    HttpclientUtil.getData(mContext, listRouteUrl, handler, LIST_ROUTE);
                }else {
                    createMultiDialog(routes);
                }
                break;
            case R.id.search:
                String routeNameStr = this.routeName.getText().toString().trim();
                String implementSituationStr = implementSituationSpinner.getText().toString().trim();
                String year = yearSpinner.getText().toString().trim();
                if ("请选择".equals(year))
                    year = "";
                if ("请选择".equals(implementSituationStr))
                    implementSituationStr = "";
                StringBuffer routeNamesCache = new StringBuffer();
                if (!TextUtils.isEmpty(routeNameStr)) {
                    String[] split = routeNameStr.split(",");
                    for (int i = 0; i < split.length; i++) {
                        split[i] = split[i].substring(split[i].indexOf("(") + 1, split[i].length() - 1);
                        if (i == split.length - 1) {
                            routeNamesCache.append(split[i]);
                        } else {
                            routeNamesCache.append(split[i] + ",");
                        }

                    }
                }
                String filter ="firstDeptId="+firstDeptId+"&secondDeptId="+secondDeptId+"&thirdDeptId="+thirdDeptId+
                        "&routeCode=" + routeNamesCache.toString() + "&year=" + year + "&dealSituation=" +implementSituationStr;
                filter = filter.replace("未处理", "0").replace("处理中", "1")
                        .replace("已处理", "2");
                listener.onExchangData(filter);
                dismiss();
                break;
        }
    }

    public interface ExchangeDataListener {
        public void onExchangData(Object object);
    }

    protected void setSpinnerData(DeptG deptG) {
        if (firstDept.size() > 0)
            return;
        List<Dept> firstDeptList = deptG.listFirstDept;
        final List<Dept> secondDeptList = deptG.listSecondDept;
        List<Dept> thirdDeptList = deptG.listThirdDept;
        for (Dept d : firstDeptList) {
            firstDept.add(d.name);
            List<String> temp = new ArrayList<String>();
            for (Dept d2 : secondDeptList) {
                if (d2.parentId.equals(d.id)) {
                    temp.add(d2.name);
                }
                List<String> temp2 = new ArrayList<String>();

                for (Dept d3 : thirdDeptList) {
                    if (d3.parentId.equals(d2.id)) {
                        temp2.add(d3.name);
                    }
                }
                if (temp2.size() > 1)
                    temp2.add("请选择");

                thirdDept.put(d2.name, temp2);

            }
            List<String> list = new ArrayList<>();
            list.add("请选择");
            thirdDept.put("请选择", list);
            if (temp.size() > 1)
                temp.add("请选择");
            secondDept.put(d.name, temp);
        }

    }
}
