package com.chengtech.chengtechmt.activity.integratequery.gis.DiseaseRecord;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.chengtech.chengtechmt.R;
import com.chengtech.chengtechmt.activity.integratequery.gis.MaintenanceSituation.MaintenanceSituationActivity;
import com.chengtech.chengtechmt.adapter.gis.DiseaseRecordAdapter;
import com.chengtech.chengtechmt.adapter.gis.ProjectAndMSgExpandableAdapter;
import com.chengtech.chengtechmt.divider.RecycleViewDivider;
import com.chengtech.chengtechmt.entity.gis.DiseaseRecord;
import com.chengtech.chengtechmt.entity.gis.ProjectAndMsg;
import com.chengtech.chengtechmt.entity.gis.ProjectAndMsgItem;
import com.chengtech.chengtechmt.fragment.BaseFragment2;
import com.chengtech.chengtechmt.util.HttpclientUtil;
import com.chengtech.chengtechmt.util.MyConstants;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者: LiuFuYingWang on 2018/1/24 16:21.
 * 基本信息
 */

public class DiseaseRecordQueryFragment extends BaseFragment2 {
    private String url = MyConstants.PRE_URL + "mt/integratequery/diseaserecord/mobileListDiseaseRecordQuery.action?";
    private View rootView;
    private RecyclerView recyclerView;
    private List<DiseaseRecord> diseaseRecordList;
    private DiseaseRecordAdapter adapter;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String json = (String) msg.obj;
            Gson gson = new Gson();
            switch (msg.what) {
                case 0x11:
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        JSONArray rows = jsonObject.getJSONArray("rows");
                        diseaseRecordList = new ArrayList<>();
                        if (rows != null && rows.length() > 0) {
                            for (int i = 0; i < rows.length(); i++) {
                                String rowsString = rows.getString(i);
                                DiseaseRecord diseaseRecord = gson.fromJson(rowsString, DiseaseRecord.class);
                                diseaseRecordList.add(diseaseRecord);
                            }
                        }
                        adapter = new DiseaseRecordAdapter(diseaseRecordList);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        recyclerView.addItemDecoration(new RecycleViewDivider(getContext(), LinearLayout.VERTICAL));
                        recyclerView.setAdapter(adapter);
                    } catch (Exception e) {

                    }
                    break;

            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null)
            rootView = inflater.inflate(R.layout.fragment_diseaserecord_query, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);

    }

    public void getData(String filter) {
        fetchData();
    }


    @Override
    public void fetchData() {
        DiseaseRecordActivity activity = (DiseaseRecordActivity) getActivity();
        if (!TextUtils.isEmpty(activity.filter)) {
            HttpclientUtil.getData(getActivity(), url + activity.filter, handler, 0x11);
        }
    }
}

