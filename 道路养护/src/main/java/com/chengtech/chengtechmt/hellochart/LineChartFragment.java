package com.chengtech.chengtechmt.hellochart;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chengtech.chengtechmt.R;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.listener.LineChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * 作者: LiuFuYingWang on 2016/9/8 14:44.
 */
public class LineChartFragment extends Fragment {

    private LineChartView chartView;
    private LineChartData chartData;
    private String[][] axisXLable;
    private List<AxisValue> axisValues = new ArrayList<>();
    private float[][] data;

    private boolean hasAxes = true;
    private boolean hasAxesNames = false;
    private boolean hasLines = true;
    private boolean hasPoints = true;
    private ValueShape shape = ValueShape.CIRCLE;
    private boolean isFilled = false;
    private boolean hasLabels = true;
    private boolean isCubic = false;
    private boolean hasLabelForSelected = false;
    private boolean pointsHaveDifferentColor;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_line_chart, container, false);
        chartView = (LineChartView) rootView.findViewById(R.id.chart);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        data = (float[][]) bundle.get("lineData");
        axisXLable = (String[][]) bundle.get("lineLable");
        generateLineChart();
    }

    private void generateLineChart() {

        getAxisLables();//获取x轴的标注
        initLineChart();//初始化

    }

    private void initLineChart() {
        if (data != null && data.length > 0) {
            List<Line> lines = new ArrayList<>();
            for (int i = 0; i < data.length; i++) {
                List<PointValue> pointValues = new ArrayList<>();
                for (int j = 0; j < data[i].length; j++) {
                    PointValue pointValues1 = new PointValue(j, data[i][j]);
                pointValues1.setLabel(String.valueOf(data[i][j]));
//                pointValues.add(new PointValue(j, data[i][j]));
                    pointValues.add(pointValues1);
                }

                Line line = new Line(pointValues);
                line.setColor(ChartUtils.pickColor());
                line.setCubic(isCubic);
                line.setHasLabels(hasLabels);
                line.setHasLabelsOnlyForSelected(true);
                line.setFilled(isFilled);
                line.setShape(ValueShape.CIRCLE);
                line.setHasLines(hasLines);
                line.setHasPoints(hasPoints);
                lines.add(line);
            }

            chartData = new LineChartData(lines);


            //坐标轴
            Axis axisX = new Axis();
            axisX.setValues(axisValues);
            axisX.setHasLines(true);
            axisX.setHasTiltedLabels(true);//true,就是斜向显示；false就是横向显示
            Axis axisY = new Axis().setHasLines(hasLines);
            axisY.setMaxLabelChars(3);

            if (hasAxes) {
                if (hasAxesNames) {
                    axisX.setName("x轴");
                    axisY.setName("Y轴");
                }
                chartData.setAxisYLeft(axisY);
                chartData.setAxisXBottom(axisX);
            } else {
                chartData.setAxisYLeft(null);
                chartData.setAxisXBottom(null);
            }

            chartView.setHorizontalFadingEdgeEnabled(true);
            chartView.setOnValueTouchListener(new LineChartOnValueSelectListener() {
                @Override
                public void onValueSelected(int lineIndex, int pointIndex, PointValue value) {
                    Toast.makeText(getContext(), value.getY()+"", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onValueDeselected() {

                }
            });
            chartView.setHorizontalScrollBarEnabled(true);
            chartView.setLineChartData(chartData);
            Viewport viewport = new Viewport(0,chartView.getMaximumViewport().height()*1.25f,15,0);
            chartView.setMaximumViewport(new Viewport(0,chartView.getMaximumViewport().height()*1.25f,data[0].length,0));
            chartView.setCurrentViewport(viewport);
            chartView.setScrollEnabled(true);
            chartView.setViewportCalculationEnabled(false);
        }

    }


    private void getAxisLables() {
        if (axisXLable != null && axisXLable.length > 0) {
            for (int i = 0; i < axisXLable[0].length; i++) {
                axisValues.add(new AxisValue(i).setLabel(axisXLable[0][i]));
            }
        }
    }
}
