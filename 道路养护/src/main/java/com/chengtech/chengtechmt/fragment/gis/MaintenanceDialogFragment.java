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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.chengtech.chengtechmt.R;
import com.chengtech.chengtechmt.adapter.gis.GisListRouteAdapter;
import com.chengtech.chengtechmt.entity.Route;
import com.chengtech.chengtechmt.entity.gson.RouteG;
import com.chengtech.chengtechmt.fragment.GISMenuDialogFragment;
import com.chengtech.chengtechmt.util.HttpclientUtil;
import com.chengtech.chengtechmt.util.MyConstants;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;

import static com.chengtech.chengtechmt.R.id.routeGrade;

/**
 * 作者: LiuFuYingWang on 2018/1/3 16:02.
 * 维修实施情况-筛选条件的fragment
 */

public class MaintenanceDialogFragment extends DialogFragment implements View.OnClickListener {
    public final int LIST_ROUTE = 0;
    private DisplayMetrics displayMetrics;
    private ExchangeDataListener listener;
    private HashMap<String, Boolean> selectedPosMap = new HashMap<>();
    private Context mContext;
    public TextView routeGrade, routeName, projectItemType_tv, implementSituation_tv, year_tv;
    private String[] routeGradeItems = new String[]{
            "G-国道", "S-省道", "X-县道", "Y-乡道", "Z-匝道", "C-村道"
    };
    private boolean[] routeGradeCheckeds = new boolean[6];

    public String[] projectItemTypeItem = new String[]{
            "请选择", "大中修", "小额专项"
    };
    public int projectItemTypeCheckeds = 0;
    public String[] implementSituationItem = new String[]{
            "请选择", "实施中", "已完成"
    };
    public int implementSituationCheckeds = 0;
    public String[] yearItem = new String [21] ;
    public int yearCheckeds = 0;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String json = (String) msg.obj;
            Gson gson = new Gson();
            switch (msg.what) {
                case LIST_ROUTE:
                    try {
                        RouteG routeG = gson.fromJson(json, RouteG.class);
                        List<Route> routes = routeG.rows;
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
        View view = inflater.inflate(R.layout.maintenance_dialog_fragment, container, true);
        mContext = getActivity();
        routeGrade = (TextView) view.findViewById(R.id.routeGrade);
        routeGrade.setOnClickListener(this);
        routeName = (TextView) view.findViewById(R.id.routeNames);
        routeName.setOnClickListener(this);
        projectItemType_tv = (TextView) view.findViewById(R.id.projectItemType);
        projectItemType_tv.setOnClickListener(this);
        implementSituation_tv = (TextView) view.findViewById(R.id.implementSituation);
        implementSituation_tv.setOnClickListener(this);
        year_tv = (TextView) view.findViewById(R.id.year);
        year_tv.setText("2018");
        year_tv.setOnClickListener(this);
        Button search = (Button) view.findViewById(R.id.search);
        search.setOnClickListener(this);

        for (int i=0;i<21;i++) {
            if (i == 0) {
                yearItem[0] = "请选择";
            } else {
                yearItem[i] = String.valueOf(2004 + i) ;
            }
        }

        //设置dialog的位置和大小
        return view;
    }

    @Override
    public void onClick(final View view) {
        switch (view.getId()) {
            case R.id.routeGrade:
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setMultiChoiceItems(routeGradeItems, routeGradeCheckeds, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {

                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        StringBuffer sb = new StringBuffer();
                        for (int i = 0; i < routeGradeCheckeds.length; i++) {
                            if (routeGradeCheckeds[i])
                                sb.append(routeGradeItems[i] + ",");
                        }
                        if (!TextUtils.isEmpty(sb)) {
                            ((TextView) view).setText(sb.toString().substring(0, sb.length() - 1));
                        } else {
                            ((TextView) view).setText("");
                        }

                        routeName.setText("");


                    }
                });
                builder.create().show();
                break;

            case R.id.routeNames:
                String listRouteUrl = MyConstants.PRE_URL + "mt/integratequery/gisvisualization/listRouteInGisJson.action?routeGrade=";
                String routeGrade = this.routeGrade.getText().toString().trim();
                if (!TextUtils.isEmpty(routeGrade)) {
                    String[] split = routeGrade.split(",");
                    for (int i = 0; i < split.length; i++) {
                        listRouteUrl = listRouteUrl + split[i].substring(0, 1) + ",";
                    }
                }
                HttpclientUtil.getData(mContext, listRouteUrl, handler, LIST_ROUTE);
                break;

            case R.id.projectItemType:
                AlertDialog.Builder builder2 = new AlertDialog.Builder(mContext);
                builder2.setSingleChoiceItems(projectItemTypeItem, projectItemTypeCheckeds, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        projectItemTypeCheckeds = which;
                    }
                });
                builder2.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        projectItemType_tv.setText(projectItemTypeItem[projectItemTypeCheckeds]);
                    }
                });
                builder2.create().show();
                break;

            case R.id.year:
                AlertDialog.Builder builder3 = new AlertDialog.Builder(mContext);
                builder3.setSingleChoiceItems(yearItem, yearCheckeds, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        yearCheckeds = which;
                    }
                });
                builder3.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        year_tv.setText(yearItem[yearCheckeds]);
                    }
                });
                builder3.create().show();
                break;

            case R.id.implementSituation:
                AlertDialog.Builder builder4 = new AlertDialog.Builder(mContext);
                builder4.setSingleChoiceItems(implementSituationItem, implementSituationCheckeds, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        implementSituationCheckeds = which;
                    }
                });
                builder4.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        implementSituation_tv.setText(implementSituationItem[implementSituationCheckeds]);
                    }
                });
                builder4.create().show();
                break;

            case R.id.search:
                String routeGradeStr = this.routeGrade.getText().toString().trim();
                String routeNameStr = this.routeName.getText().toString().trim();
                String projectItemTypeStr = this.projectItemType_tv.getText().toString().trim();
                String implementSituationStr = this.implementSituation_tv.getText().toString().trim();
                String year = year_tv.getText().toString().trim();
                if ("请选择".equals(year))
                    year = "";
                if ("请选择".equals(implementSituationStr))
                    implementSituationStr = "";
                if ("请选择".equals(projectItemTypeStr))
                    projectItemTypeStr = "";
                StringBuffer routeGradeCache = new StringBuffer();
                if (!TextUtils.isEmpty(routeGradeStr)) {
                    String[] split = routeGradeStr.split(",");
                    for (int i = 0; i < split.length; i++) {
                        split[i] = split[i].substring(0, 1);

                        if (i== split.length-1) { routeGradeCache.append(split[i] );}else {
                            routeGradeCache.append(split[i] + ",");
                        }
                    }
                }
                StringBuffer routeNamesCache = new StringBuffer();
                if (!TextUtils.isEmpty(routeNameStr)) {
                    String[] split = routeNameStr.split(",");
                    for (int i = 0; i < split.length; i++) {
                        split[i] = split[i].substring(split[i].indexOf("(") + 1, split[i].length() - 1);
                        if (i==split.length-1) {
                            routeNamesCache.append(split[i] );
                        }else {
                            routeNamesCache.append(split[i] + ",");
                        }

                    }

                }

                String filter = "routeGrade="+routeGradeCache.toString()+
                        "&routeCode="+routeNamesCache.toString()+"&year="+year+"&projectItemType="+projectItemTypeStr+"&implementSituation="+implementSituationStr;
                filter = filter.replace("大中修","ProjectManagement").replace("小额专项","MediumPlanprogressItem")
                .replace("实施中","0").replace("已完成","1");
                listener.onExchangData(filter);
                dismiss();
                break;
        }
    }

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

    public interface ExchangeDataListener {
        public void onExchangData(Object object);
    }
}
