package com.chengtech.chengtechmt.activity.integratequery.routequery;


import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chengtech.chengtechmt.R;
import com.chengtech.chengtechmt.divider.RecycleViewDivider;
import com.chengtech.chengtechmt.entity.routequery.TrafficVolume;
import com.chengtech.chengtechmt.fragment.BaseFragment;
import com.chengtech.chengtechmt.util.CommonUtils;
import com.chengtech.chengtechmt.util.HttpclientUtil;
import com.chengtech.chengtechmt.util.MyConstants;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.chengtech.chengtechmt.R.id.view;

/**
 * 作者: LiuFuYingWang on 2016/12/13 15:11.
 * 交通量汇总数据
 */

public class TrafficVolumeFragment extends BaseFragment {

    private String routeId;
    private String trafficVolumeListUrl;
    private List<TrafficVolume> trafficVolumeList = new ArrayList<>();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String json = (String) msg.obj;
            Gson gson = new Gson();
            switch (msg.what) {
                case 1:
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        JSONArray rows = jsonObject.getJSONArray("rows");
                        for (int i = 0; i < rows.length(); i++) {
                            JSONObject jsonObject1 = rows.getJSONObject(i);
                            TrafficVolume trafficVolume = gson.fromJson(jsonObject1.toString(), TrafficVolume.class);
                            trafficVolumeList.add(trafficVolume);
                        }
                        if (trafficVolumeList.size()>0) {
                            createAdapetr(trafficVolumeList);
                        }else {
                            Toast.makeText(getContext(), "没有数据。", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "数据结构出错。", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RecyclerView recyclerView = new RecyclerView(inflater.getContext());
        return recyclerView;
    }

    public TrafficVolumeFragment() {
        trafficVolumeListUrl = MyConstants.PRE_URL + "mt/integratequery/basicdataquery/routedataquery" +
                "/listTrafficVolumeJson.action?routeId=";
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        routeId = bundle.getString("routeId");

    }

    private void createAdapetr(final List<TrafficVolume> trafficVolumeList) {
        RecyclerView recyclerView = (RecyclerView) getView();
        recyclerView.setAdapter(new RecyclerView.Adapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                final TextView textView = new TextView(getContext());
                ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
                textView.setGravity(Gravity.LEFT);
                textView.setTextSize(14);
                int padding = CommonUtils.dp2px(getContext(),16);
                textView.setPadding(padding,padding,padding,padding);
                textView.setLayoutParams(layoutParams);
                textView.setBackgroundColor(getResources().getColor(R.color.tabBlue));
                textView.setTextColor(Color.WHITE);
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int delta = CommonUtils.dp2px(getContext(),8);
                        PropertyValuesHolder pvhTranslateX = PropertyValuesHolder.ofKeyframe(View.TRANSLATION_X,
                                Keyframe.ofFloat(0f, 0),
                                Keyframe.ofFloat(.10f, -delta),
                                Keyframe.ofFloat(.20f, delta),
                                Keyframe.ofFloat(.30f, -delta),
                                Keyframe.ofFloat(.40f, delta),
                                Keyframe.ofFloat(.50f, -delta),
                                Keyframe.ofFloat(.50f, delta),
                                Keyframe.ofFloat(.60f, -delta),
                                Keyframe.ofFloat(.70f, delta),
                                Keyframe.ofFloat(.80f, -delta),
                                Keyframe.ofFloat(.90f, delta),
                                Keyframe.ofFloat(1f, 0f)
                        );
                        ObjectAnimator.ofPropertyValuesHolder(v, pvhTranslateX).
                                setDuration(500).start();
                    }
                });
                RecyclerView.ViewHolder viewHolder = new RecyclerView.ViewHolder(textView) {
                    @Override
                    public String toString() {
                        return super.toString();
                    }
                };
                return viewHolder;
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                TrafficVolume trafficVolume = trafficVolumeList.get(position);
                TextView  textView = (TextView) holder.itemView;
                StringBuffer sb = new StringBuffer();
                for (int i=0;i<trafficVolume.getTitles().size();i++){
                    if (i!=trafficVolume.getTitles().size()-1) {
                        sb.append(trafficVolume.getTitles().get(i) + trafficVolume.getContent().get(i) + "\n");
                    }else{
                        sb.append(trafficVolume.getTitles().get(i) + trafficVolume.getContent().get(i));
                    }

                }
                textView.setText(sb.toString());

            }

            @Override
            public int getItemCount() {
                return trafficVolumeList.size();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new RecycleViewDivider(getContext(), LinearLayout.VERTICAL));

    }

    @Override
    protected void onLazyLoad() {
        //网络请求
        if (trafficVolumeList.size()==0) {
            HttpclientUtil.getData(getContext(), trafficVolumeListUrl + routeId, handler, 1);
        }else {
            createAdapetr(trafficVolumeList);
        }
    }
}
