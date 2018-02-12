package com.chengtech.chengtechmt.activity.integratequery.gis.DiseaseRecord;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ExpandableListView;

import com.chengtech.chengtechmt.R;
import com.chengtech.chengtechmt.activity.integratequery.gis.MaintenanceSituation.ProjectAndMsgFragment;
import com.chengtech.chengtechmt.adapter.gis.DiseaseRecordHandleExpandableAdapter;
import com.chengtech.chengtechmt.adapter.gis.ProjectAndMSgExpandableAdapter;
import com.chengtech.chengtechmt.entity.gis.DiseaseRecordHandle;
import com.chengtech.chengtechmt.entity.gis.DiseaseRecordHandleItem;
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
 * 作者: LiuFuYingWang on 2018/1/24 16:16.
 * 病害处理情况
 */

public class DiseaseHandlerFragment extends BaseFragment2 implements ExpandableListView.OnGroupClickListener {

    private String url = MyConstants.PRE_URL + "mt/integratequery/diseaserecord/listDiseaseRecordHandleJson.action?";
    private String itemUrl = MyConstants.PRE_URL + "mt/integratequery/diseaserecord/listDetailedInfoJson.action?";
    private WebView webView;
    private ExpandableListView expandableListView;
    private View rootView;
    private List<DiseaseRecordHandle> diseaseRecordHandleList;
    private String filter_cache = "";
    private DiseaseRecordHandleExpandableAdapter adapter;

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
                        diseaseRecordHandleList = new ArrayList<>();
                        if (rows != null && rows.length() > 0) {
                            for (int i = 0; i < rows.length(); i++) {
                                String rowsString = rows.getString(i);
                                DiseaseRecordHandle diseaseRecordHandle = gson.fromJson(rowsString, DiseaseRecordHandle.class);
                                diseaseRecordHandleList.add(diseaseRecordHandle);
                            }
                        }
                        adapter = new DiseaseRecordHandleExpandableAdapter(getActivity(), diseaseRecordHandleList);
                        expandableListView.setAdapter(adapter);
                    } catch (Exception e) {

                    }
                    break;
                default:
                    List<DiseaseRecordHandleItem> handleItems = new ArrayList<>();
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        JSONArray rows = jsonObject.getJSONArray("rows");
                        if (rows != null && rows.length() > 0) {
                            for (int i = 0; i < rows.length(); i++) {
                                String rowsString = rows.getString(i);
                                DiseaseRecordHandleItem handleItem = gson.fromJson(rowsString, DiseaseRecordHandleItem.class);
                                handleItems.add(handleItem);
                            }
                        }
                    } catch (Exception e) {

                    }
                    if (diseaseRecordHandleList != null && diseaseRecordHandleList.size() > 0) {
                        DiseaseRecordHandle diseaseRecordHandle = diseaseRecordHandleList.get(msg.what);
                        diseaseRecordHandle.diseaseRecordHandleItem = handleItems;
                    }
                    adapter.notifyDataSetChanged();
                    expandableListView.expandGroup(msg.what, true);

                    break;
            }
        }
    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_disease_record_handle, container, false);
            expandableListView = (ExpandableListView) rootView.findViewById(R.id.expandlistview);
            expandableListView.setOnGroupClickListener(DiseaseHandlerFragment.this);
            int width = getResources().getDisplayMetrics().widthPixels;
            expandableListView.setIndicatorBounds(width - 60, width - 30);
        }
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        webView = (WebView) view.findViewById(R.id.webView);


    }

    public void getData(String filter) {
        if (getUserVisibleHint() && isVisible()) {
            fetchData();
        }
    }


    @Override
    public void fetchData() {
        DiseaseRecordActivity activity = (DiseaseRecordActivity) getActivity();
        if (!TextUtils.isEmpty(activity.filter)) {
            HttpclientUtil.getData(getActivity(), url + activity.filter, handler, 0x11);
        }
    }

    @Override
    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
        DiseaseRecordActivity activity = (DiseaseRecordActivity) getActivity();
        DiseaseRecordHandle diseaseRecordHandle = diseaseRecordHandleList.get(groupPosition);
        if (activity.filter.equals(filter_cache)) {
            if (diseaseRecordHandle.diseaseRecordHandleItem != null)
                return false;
            //如果ProjectAndMsg里面的projectAndMsgItem为null，则需要请求网络
            String filter = activity.filter.substring(0, activity.filter.lastIndexOf("&") + 1);
            filter = filter + "situation=" + diseaseRecordHandle.dealSituation;
            HttpclientUtil.getData(getActivity(), itemUrl + filter, handler, groupPosition);
        } else {
            filter_cache = activity.filter;
            if (!TextUtils.isEmpty(activity.filter)) {
                String filter = activity.filter.substring(0, activity.filter.lastIndexOf("&") + 1);
                filter = filter + "situation=" + diseaseRecordHandle.dealSituation;
                HttpclientUtil.getData(getActivity(), itemUrl + filter, handler, groupPosition);
            }
        }
        return true;
    }
}
