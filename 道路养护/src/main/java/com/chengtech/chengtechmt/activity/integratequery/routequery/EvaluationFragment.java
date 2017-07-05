package com.chengtech.chengtechmt.activity.integratequery.routequery;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.TextView;

import com.chengtech.chengtechmt.R;
import com.chengtech.chengtechmt.entity.expertdecision.Evaluationdetail;
import com.chengtech.chengtechmt.fragment.BaseFragment;
import com.chengtech.chengtechmt.util.HttpclientUtil;
import com.chengtech.chengtechmt.util.MyConstants;
import com.chengtech.chengtechmt.view.MyHorizontalScrollView2;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.view.ColumnChartView;

/**
 * 作者: LiuFuYingWang on 2016/7/26 16:16.
 * 历史评定明细数据
 */
public class EvaluationFragment extends BaseFragment {

    private String[] colorCode = new String []{"#FF7F50","#87CEFA","#DA70D6"};
    private ColumnChartData chartData;
    private float[][] columnData;
    private String[][] columnLable;
    private ArrayList<Evaluationdetail> evaluationdetails;
    String [] xLabel ;
    private String routeId;
    private String url = MyConstants.PRE_URL+"mt/integratequery/basicdataquery/routedataquery/evaluateGraph.action?routeId=";
    private String url2 = MyConstants.PRE_URL+"mt/expertdecision/roadtechevaluation/evaluationdetail/listHistoryEvaluationJson.action?direction=asc&sort=checkDate,pileNumber,laneType&routeId=";
    private TextView textView;
    private String chartTitle;
    private Button detail_bt;
//    private RecyclerView recyclerView;
    MyHorizontalScrollView2 myHorizontalScrollView2;
    private ColumnChartView columnChartView;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            String json = (String) msg.obj;
            Gson gson = new Gson();
            switch (msg.what) {
                case 0:
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        JSONObject title = jsonObject.getJSONObject("title");
                        chartTitle = title.getString("text");
                        textView.setText(chartTitle);

                        JSONObject xAxis = (JSONObject) jsonObject.getJSONArray("xAxis").get(0);
                        JSONArray data = xAxis.getJSONArray("data");
                        xLabel = new String[data.length()];
                        for (int i=0;i<data.length();i++) {
                            xLabel[i] = data.getString(i);
                        }

                        columnData = new float[xLabel.length][3];
                        columnLable = new String[xLabel.length][3];
                        JSONArray series = jsonObject.getJSONArray("series");
                        for (int i=0;i<series.length();i++) {
                            JSONObject jsonObject1 = series.getJSONObject(i);
                            JSONArray data1 = jsonObject1.getJSONArray("data");
                            for (int j=0;j<data1.length();j++) {
                                int anInt = data1.getInt(j);
//                                double value = data1.getDouble(j);
//                                columnData[j][i] = (float)value;
                                columnData[j][i] = anInt;
                                columnLable[j][0] = xLabel[j];
                            }

                        }
                        showColumnChartView();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case 1:
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        JSONArray jsonArray = jsonObject.getJSONArray("rows");
                        evaluationdetails = new ArrayList<>();
                        for (int i=0;i<jsonArray.length();i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            Evaluationdetail evaluationdetail = gson.fromJson(jsonObject1.toString(), Evaluationdetail.class);
                            evaluationdetails.add(evaluationdetail);
                        }
                        showDetailData();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       /* TextView textView = new TextView(inflater.getContext());
        textView.setText("没有数据");
        textView.setGravity(Gravity.CENTER);
        return textView;*/
        View view = inflater.inflate(R.layout.fragment_evaluation,container,false);
        textView = (TextView) view.findViewById(R.id.chartTitle);
        detail_bt = (Button) view.findViewById(R.id.detail_bt);
        myHorizontalScrollView2 = (MyHorizontalScrollView2) view.findViewById(R.id.recyclerView);
        detail_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myHorizontalScrollView2.isShown()) {
                    myHorizontalScrollView2.setVisibility(View.GONE);
                }else {
                    if (evaluationdetails==null) {
                        HttpclientUtil.getData(getContext(),url2+routeId,handler,1);
                    }else {
                        if (!myHorizontalScrollView2.hasData())
                            showDetailData();
                    }
                    myHorizontalScrollView2.setVisibility(View.VISIBLE);
                }
            }
        });
        columnChartView = (ColumnChartView) view.findViewById(R.id.columnChart);

        return view;

    }

    //展示详细列表数据
    private void showDetailData() {
        final List<List<String>> data = new ArrayList<>();
        data.add(evaluationdetails.get(0).getTitles());
        for (int i = 0; i < evaluationdetails.size(); i++) {
            data.add(evaluationdetails.get(i).getContent());
        }
//        SparseIntArray sparseIntArray = new SparseIntArray();
//        sparseIntArray.put(1, 150);
//        sparseIntArray.put(2, 150);
//        sparseIntArray.put(3, 150);
//        sparseIntArray.put(4, 150);
//        sparseIntArray.put(5, 150);
//        sparseIntArray.put(6, 100);
//        sparseIntArray.put(7, 100);
//        sparseIntArray.put(8, 100);
//        sparseIntArray.put(9, 100);
//        sparseIntArray.put(10, 100);
//        sparseIntArray.put(11, 100);
//        parent.setRectWidthAndHeight(sparseIntArray);
        myHorizontalScrollView2.setData(data);
//        parent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                for (int i = 0; i < parent.contentRV.getChildCount(); i++) {
//                    View view = parent.contentRV.getChildAt(i);
//                    int measuredHeight = view.getMeasuredHeight();
//                    View childAt = parent.nameRV.getChildAt(i);
//                    ViewGroup.LayoutParams layoutParams = childAt.getLayoutParams();
//                    layoutParams.height = measuredHeight;
//                    childAt.setLayoutParams(layoutParams);
//                }
//                parent.getViewTreeObserver().removeGlobalOnLayoutListener(this);
//            }
//        });
//        parent.setOnItemClickListener(new MyHorizontalScrollView2.onItemClickListener() {
//            @Override
//            public void onClick(View view, int position) {
//                String maintainItemId = maintainTaskItemList.get(position).id;
//                String url = MyConstants.PRE_URL
//                        + "mt/business/tinkermaintainpatrol/maintaintask/listTaskDetailJson.action?maintainTaskItemId="
//                        + maintainItemId;
//
//                presenter.getData(MaintenanceListActivity.this, url, "TaskDetail", 0);
//                horizontalDialog.dismiss();
//            }
//        });

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        routeId = bundle.getString("routeId");
        if (savedInstanceState!=null) {
            Serializable data = savedInstanceState.getSerializable("data");
            if (data!=null) {
                columnData = (float[][]) data;
            }
            Serializable lable = savedInstanceState.getSerializable("xLable");
            if (lable!=null) {
                columnLable = (String[][]) lable;
            }
            ArrayList<Evaluationdetail> evaluationList = (ArrayList<Evaluationdetail>) savedInstanceState.getSerializable("evaluationList");
            if (evaluationList!=null) {
                evaluationdetails = evaluationList;
            }
        }

    }

    private void showColumnChartView() {


//        for (int i = 0; i < xLabel.length; i++) {
//            for (int j = 0; j < 3; j++) {
//                columnData[i][j] = (i+j)*10;
//                columnLable[i][0] = xLabel[i];
//            }
//        }

//        Bundle bundle = new Bundle();
//        bundle.putSerializable("columnData", columnData);
//        bundle.putSerializable("columnLable", columnLable);
//
//        ColumnChartFragment columnChartFragment = new ColumnChartFragment();
//        columnChartFragment.setArguments(bundle);
//        getChildFragmentManager().beginTransaction().add(R.id.container,columnChartFragment)
//                .commit();

        List<Column> columnList = new ArrayList<>();
        List<SubcolumnValue> values;
        for (int i = 0; i < columnData.length; i++) {

            values = new ArrayList<>();
            for (int j = 0; j < columnData[i].length; j++) {
                values.add(new SubcolumnValue(columnData[i][j], Color.parseColor(colorCode[j%colorCode.length])));
            }

            Column column = new Column(values);
            column.setHasLabels(false);
            column.setHasLabelsOnlyForSelected(true);
            columnList.add(column);
        }

        chartData = new ColumnChartData(columnList);
        if (true) {
            Axis axisX = new Axis();
            List<AxisValue> axisValueList = new ArrayList<>();
            for (int i = 0; i < columnData.length; i++) {
                AxisValue axisValue = new AxisValue(i);
                axisValue.setValue(i);
                axisValue.setLabel(columnLable[i][0]);
                axisValueList.add(axisValue);
            }
            axisX.setValues(axisValueList);

            Axis axisY = new Axis().setHasLines(true);
//            axisY.setMaxLabelChars(10);
            List<AxisValue> axisValueY = new ArrayList<>();
            for (int i = 1; i < 11; i ++) {
                AxisValue axisValue = new AxisValue(i*10);
                axisValue.setLabel(i*10 + "");
                axisValueY.add(axisValue);
            }
            axisY.setValues(axisValueY);
            chartData.setAxisXBottom(axisX);
            chartData.setAxisYLeft(axisY);
        } else {
            chartData.setAxisXBottom(null);
            chartData.setAxisYLeft(null);
        }
        columnChartView.setColumnChartData(chartData);
    }


    @Override
    protected void onLazyLoad() {
        if(columnData==null) {
            HttpclientUtil.getData(getContext(),url+routeId,handler,0);
        }else {
            showColumnChartView();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (outState!=null) {
            outState.putSerializable("data",columnData);
            outState.putSerializable("xLable",columnLable);
            outState.putSerializable("evaluationList",evaluationdetails);
        }
    }
}
