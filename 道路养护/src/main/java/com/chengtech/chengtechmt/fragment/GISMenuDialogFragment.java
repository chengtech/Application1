package com.chengtech.chengtechmt.fragment;

import com.chengtech.chengtechmt.MainActivity;
import com.chengtech.chengtechmt.activity.MapQueryActivity;
import com.chengtech.chengtechmt.entity.MapEntity;
import com.google.gson.Gson;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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
import com.chengtech.chengtechmt.activity.IView;
import com.chengtech.chengtechmt.adapter.gis.GisListRouteAdapter;
import com.chengtech.chengtechmt.entity.Route;
import com.chengtech.chengtechmt.entity.gson.RouteG;
import com.chengtech.chengtechmt.util.CommonUtils;
import com.chengtech.chengtechmt.util.HttpclientUtil;
import com.chengtech.chengtechmt.util.MyConstants;
import com.chengtech.chengtechmt.util.MyDialogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 作者: LiuFuYingWang on 2016/7/20 15:37.
 */
public class GISMenuDialogFragment extends DialogFragment implements View.OnClickListener {

    public  final int LIST_ROUTE = 0;
    public  final int EVALUATE = 1;
    private ExchangeDataListener listener;
    public HashMap<String,Boolean> selectedPosMap = new HashMap<>();
    TextView routeGrade,routeName,evaluate_tv,year_tv;
    DisplayMetrics displayMetrics;
    public Context mContext;
    public String [] routeGradeItems = new String[]{
            "G-国道","S-省道","X-县道","Y-乡道","Z-专用公路","C-村道"
    };
    public boolean [] routeGradeCheckeds = new boolean[6];
    public String [] poiItems = new String[]{
            "PQI","PCI","RQI","RDI","MQI"
    };
    public int poiCheckeds =0;
    public String[] yearItems = new String[]{
            "请选择","2014","2015","2016","2017","2018","2019","2020"
    };
    public int yearChecked = 0;


    public Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            String json = (String) msg.obj;
            Gson gson = new Gson();
            switch (msg.what) {
                case LIST_ROUTE:
                    try{
//                        loadDialog.dismiss();
                        RouteG routeG = gson.fromJson(json, RouteG.class);
                        List<Route> routes = routeG.rows;
                        if (routes!=null && routes.size()>0) {
                            createMultiDialog(routes);
                        }else {
                            Toast.makeText(mContext, "无数据。", Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e) {

                    }
                    break;

                case EVALUATE:
                    try{
//                        loadDialog.dismiss();
                        if (json!=null && json.length()>10) {
                            if (listener!=null) {
                                listener.onExchangData(json);
                                GISMenuDialogFragment.this.dismiss();
                            }
                        }else {
                            Toast.makeText(mContext, "暂无评定数据", Toast.LENGTH_SHORT).show();
                        }
//                        Intent intent = new Intent(mContext, MapQueryActivity.class);
//                        MapEntity mapEntity = new MapEntity(null,null,null,null,json);
//                        intent.putExtra("map",mapEntity);
//                        startActivity(intent);

                    }catch (Exception e) {

                    }
                    break;
            }
        }
    };

    public interface ExchangeDataListener {
        public void onExchangData(Object object);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        displayMetrics = getActivity().getResources().getDisplayMetrics();
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.gismenu_dialog_fragment, container, true);
        mContext = inflater.getContext();
//        loadDialog = MyDialogUtil.createDialog(mContext,"正在加载");
//        loadDialog.setCanceledOnTouchOutside(false);
        routeGrade = (TextView) view.findViewById(R.id.routeGrade);
        routeGrade.setOnClickListener(this);
        routeName = (TextView) view.findViewById(R.id.routeNames);
        routeName.setOnClickListener(this);
        evaluate_tv = (TextView) view.findViewById(R.id.evaluate);
        evaluate_tv.setOnClickListener(this);
        year_tv = (TextView) view.findViewById(R.id.year);
        year_tv.setOnClickListener(this);
        Button search = (Button) view.findViewById(R.id.search);
        search.setOnClickListener(this);


        //设置dialog的位置和大小
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listener = (ExchangeDataListener) activity;
    }


    //    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        DisplayMetrics displayMetrics = getActivity().getResources().getDisplayMetrics();
//        // 使用不带theme的构造器，获得的dialog边框距离屏幕仍有几毫米的缝隙。
//        // Dialog dialog = new Dialog(getActivity());
//        Dialog dialog = new Dialog(getActivity(), R.style.SubMenuDialog);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // must be called before set content
//
//        View view = LayoutInflater.from(getActivity()).inflate(R.layout.submenu_dialog_fragment, null, true);
//
//        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
//        layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
//        layoutParams.height = (int) (displayMetrics.widthPixels*0.5);
//        view.setLayoutParams(layoutParams);
//
//        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycleView);
//
//        Bundle bundle = getArguments();
//        menus = (List<Menu>) bundle.getSerializable("data");
//        RecycleViewAdapter adapter2 = new RecycleViewAdapter(getActivity(), menus, null, R.layout.item_recycle1);
//        recyclerView.setAdapter(adapter2);
//        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
//
//        dialog.setContentView(view);
//        dialog.setCanceledOnTouchOutside(true);
//
//        // 设置宽度为屏宽、靠近屏幕底部。
//        Window window = dialog.getWindow();
//        WindowManager.LayoutParams wlp = window.getAttributes();
//        wlp.gravity = Gravity.BOTTOM;
//        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
//
//        wlp.height = (int) (displayMetrics.heightPixels * 0.5);
//        window.setAttributes(wlp);
//
//        return dialog;
//    }


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
    public void onClick(final View view) {
        switch (view.getId()) {
            case R.id.routeGrade:
                AlertDialog.Builder builder  = new AlertDialog.Builder(mContext);
                builder.setMultiChoiceItems(routeGradeItems, routeGradeCheckeds, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {

                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        StringBuffer sb = new StringBuffer();
                        for (int i=0;i<routeGradeCheckeds.length;i++) {
                            if (routeGradeCheckeds[i])
                                sb.append(routeGradeItems[i]+",");
                        }
                        if (!TextUtils.isEmpty(sb)){
                            ((TextView)view).setText(sb.toString().substring(0,sb.length()-1));
                        }else {
                            ((TextView)view).setText("");
                        }


                    }
                });
                builder.create().show();
                break;

            case R.id.routeNames:
                String listRouteUrl = MyConstants.PRE_URL+"mt/integratequery/gisvisualization/listRouteInGisJson.action?routeGrade=";
                String routeGrade = this.routeGrade.getText().toString().trim();
                if(!TextUtils.isEmpty(routeGrade)){
                    String[] split = routeGrade.split(",");
                    for (int i=0;i<split.length;i++) {
                        listRouteUrl = listRouteUrl+split[i].substring(0,1)+",";
                    }
                }
                HttpclientUtil.getData(mContext,listRouteUrl,handler,LIST_ROUTE);
//                loadDialog.show();;
                break;

            case R.id.evaluate:
                AlertDialog.Builder builder2  = new AlertDialog.Builder(mContext);
                builder2.setSingleChoiceItems(poiItems, poiCheckeds, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        poiCheckeds = which;
                    }
                });
                builder2.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        evaluate_tv.setText(poiItems[poiCheckeds]);
                    }
                });
                builder2.create().show();
                break;

            case R.id.year:
                AlertDialog.Builder builder3  = new AlertDialog.Builder(mContext);
                builder3.setSingleChoiceItems(yearItems, yearChecked, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        yearChecked = which;
                    }
                });
                builder3.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        year_tv.setText(yearItems[yearChecked]);
                    }
                });
                builder3.create().show();
                break;

            case R.id.search:
                String evaluateMsgUrl = MyConstants.PRE_URL+"mt/integratequery/gisvisualization/getEvaluateMsg.action?routeGrade=";
                String routeGrade2 = this.routeGrade.getText().toString().trim();
                String evaluateIndex = evaluate_tv.getText().toString().trim();
                String year = year_tv.getText().toString().trim();
                if ("请选择".equals(year))
                    year = "";
                String routeNames = routeName.getText().toString().trim();
                StringBuffer routeNamesCache = new StringBuffer();
                if (!TextUtils.isEmpty(routeNames)){
                    String[] split = routeNames.split(",");
                    for (int i=0;i<split.length;i++) {
                        split[i] = split[i].substring(split[i].indexOf("(")+1,split[i].length()-1);
                        routeNamesCache.append(split[i]+"\',\'");
                    }
                }
                evaluateMsgUrl = evaluateMsgUrl+routeGrade2+"&routeCode="+routeNamesCache.toString()+"&evaluateIndex="+evaluateIndex
                        +"&year="+year;
                HttpclientUtil.getData(mContext,evaluateMsgUrl,handler,EVALUATE);
//                loadDialog.show();;
                break;
        }
    }

    private void createMultiDialog(List<Route> routes) {
        final GisListRouteAdapter adapter = new GisListRouteAdapter(routes,selectedPosMap);
        final RecyclerView recyclerView = new RecyclerView(mContext);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        AlertDialog.Builder builder  = new AlertDialog.Builder(mContext);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                StringBuffer sb = new StringBuffer();
                List<Route> checkedItems = adapter.getCheckedItems();
                for (int i=0;i<checkedItems.size();i++) {
                    sb.append(checkedItems.get(i).name+"("+checkedItems.get(i).code+"),");
                }
                if (!TextUtils.isEmpty(sb)){
                    routeName.setText(sb.toString());
                }else {
                    routeName.setText("");
                }
            }
        });
        builder.setView(recyclerView);
        builder.create().show();

    }
}
