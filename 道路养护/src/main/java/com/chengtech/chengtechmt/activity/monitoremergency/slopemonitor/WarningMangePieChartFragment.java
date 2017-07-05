package com.chengtech.chengtechmt.activity.monitoremergency.slopemonitor;

import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.ColorUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chengtech.chengtechmt.R;
import com.chengtech.chengtechmt.activity.monitoremergency.slopemonitor.SlopeMonitorActivity;
import com.chengtech.chengtechmt.entity.monitoremergency.WarningManage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import lecho.lib.hellocharts.model.ChartData;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.PieChartView;

/**
 * 作者: LiuFuYingWang on 2017/6/12 15:15.
 */

public class WarningMangePieChartFragment extends Fragment {

    public static final String TYPE_1 = "type_1";
    public static final String TYPE_2 = "type_2";

    private PieChartView chart;
    private PieChartData data;
    private LinearLayout titleParent;

    private boolean hasLabels = true;
    private boolean hasLabelsOutside = false;
    private boolean hasCenterCircle = false;
    private boolean hasCenterText1 = false;
    private boolean hasCenterText2 = false;
    private boolean isExploded = false;
    private boolean hasLabelForSelected = false;
    private String[] colorCode = new String[]{"#FF7F50", "#87CEFA", "#DA70D6", "#FF4444", "#FFBB33", "#99CC00"
            , "#AA66CC", "#33B5E5", "#DDDDDD", "#DFDFDF"};
    private String type;
    private boolean isCreated;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.fragment_pie_chart, container, false);

        chart = (PieChartView) rootView.findViewById(R.id.chart);
        titleParent = (LinearLayout) rootView.findViewById(R.id.titleParent);
//        chart.setOnValueTouchListener(new ValueTouchListener());

        isCreated = true;


        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        type = arguments.getString("type");
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isCreated) {
            if (type != null && TYPE_1.equals(type)) {
                SlopeMonitorActivity slopeMonitorActivity = (SlopeMonitorActivity) getActivity();
                Map<String, Integer> charData = slopeMonitorActivity.charData;
                if (charData.size() > 0) {
                    generateData(charData);
                }
            } else if (type != null && TYPE_2.equals(type)) {
                SlopeMonitorActivity slopeMonitorActivity = (SlopeMonitorActivity) getActivity();
                Map<String, Integer> charData = slopeMonitorActivity.charData2;
                if (charData.size() > 0) {
                    generateData(charData);
                }
            }
        }
    }

    private void generateData(Map<String, Integer> charData) {
        titleParent.removeAllViews();
        List<SliceValue> values = new ArrayList<SliceValue>();
        int i = 0;
        for (Map.Entry entrySet : charData.entrySet()) {
            int backgroundColor = Color.parseColor(colorCode[i]);
            i++;
            TextView textView1 = new TextView(getContext());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ChartUtils.dp2px(getContext().getResources().getDisplayMetrics().density, 30),
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(20, 0, 0, 0);
            textView1.setBackgroundColor(backgroundColor);
            textView1.setLayoutParams(layoutParams);

            TextView textView2 = new TextView(getContext());
            LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ChartUtils.dp2px(getContext().getResources().getDisplayMetrics().density, 40));
            textView1.setBackgroundColor(backgroundColor);
            textView2.setLayoutParams(layoutParams2);
            textView2.setText((String) entrySet.getKey());
            titleParent.addView(textView1);
            titleParent.addView(textView2);
//            SliceValue sliceValue = new SliceValue((float) Math.random() * 30 + 15, Color.parseColor(colorCode[i % colorCode.length]));
            SliceValue sliceValue = new SliceValue((float) ((Integer) entrySet.getValue()).intValue(), backgroundColor);
            values.add(sliceValue);
        }

        data = new PieChartData(values);
        data.setHasLabels(hasLabels);
        data.setHasLabelsOnlyForSelected(hasLabelForSelected);
        data.setHasLabelsOutside(hasLabelsOutside);
        data.setHasCenterCircle(hasCenterCircle);

        if (isExploded) {
            data.setSlicesSpacing(24);
        }

        if (hasCenterText1) {
            data.setCenterText1("Hello!");

            // Get roboto-italic font.
//            Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Italic.ttf");
//            data.setCenterText1Typeface(tf);

            // Get font size from dimens.xml and convert it to sp(library uses sp values).
            data.setCenterText1FontSize(ChartUtils.px2sp(getResources().getDisplayMetrics().scaledDensity,
                    (int) getResources().getDimension(R.dimen.pie_chart_text1_size)));
        }

        if (hasCenterText2) {
            data.setCenterText2("Charts (Roboto Italic)");

//            Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Italic.ttf");

//            data.setCenterText2Typeface(tf);
            data.setCenterText2FontSize(ChartUtils.px2sp(getResources().getDisplayMetrics().scaledDensity,
                    (int) getResources().getDimension(R.dimen.pie_chart_text2_size)));
        }

        chart.setPieChartData(data);
    }

}
