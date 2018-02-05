package com.chengtech.chengtechmt.activity.integratequery.gis.MaintenanceSituation;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.chengtech.chengtechmt.R;
import com.chengtech.chengtechmt.adapter.gis.ProjectAndMSgExpandableAdapter;
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
 * 作者: LiuFuYingWang on 2018/1/10 15:05.
 */

public class ProjectAndMsgFragment extends BaseFragment2 implements ExpandableListView.OnGroupExpandListener,ExpandableListView.OnGroupClickListener {
    private String url = MyConstants.PRE_URL + "mt/integratequery/maintenancesituation/listProjectAndMediumMsgJson.action?";
    private String itemUrl = MyConstants.PRE_URL + "mt/integratequery/maintenancesituation/listProjectAndMediumMsgInfoJson.action?";
    private ExpandableListView expandableListView;
    private View rootView;
    private List<ProjectAndMsg> projectAndMsgList;
    private String filter_cache = "";
    private ProjectAndMSgExpandableAdapter adapter;

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
                        projectAndMsgList = new ArrayList<>();
                        if (rows != null && rows.length() > 0) {
                            for (int i = 0; i < rows.length(); i++) {
                                String rowsString = rows.getString(i);
                                ProjectAndMsg projectAndMsg = gson.fromJson(rowsString, ProjectAndMsg.class);
                                projectAndMsgList.add(projectAndMsg);
                            }
                        }
                        adapter = new ProjectAndMSgExpandableAdapter(getActivity(), projectAndMsgList);
                        expandableListView.setAdapter(adapter);

//                        expandableListView.setOnGroupExpandListener(ProjectAndMsgFragment.this);
                    } catch (Exception e) {

                    }
                    break;
                default:
                    List<ProjectAndMsgItem> projectAndMsgItems = new ArrayList<>();
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        JSONArray rows = jsonObject.getJSONArray("rows");
                        if (rows != null && rows.length() > 0) {
                            for (int i = 0; i < rows.length(); i++) {
                                String rowsString = rows.getString(i);
                                ProjectAndMsgItem projectAndMsgItem = gson.fromJson(rowsString, ProjectAndMsgItem.class);
                                projectAndMsgItems.add(projectAndMsgItem);
                            }
                        }
                    } catch (Exception e) {

                    }
                    if (projectAndMsgList != null && projectAndMsgList.size() > 0) {
                        ProjectAndMsg projectAndMsg = projectAndMsgList.get(msg.what);
                        projectAndMsg.projectAndMsgItem = projectAndMsgItems;
                    }
                    adapter.notifyDataSetChanged();
                    expandableListView.expandGroup(msg.what,true);

                    break;
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_project_and_msg, container, false);
            expandableListView = (ExpandableListView) rootView.findViewById(R.id.expandlistview);
            expandableListView.setOnGroupClickListener(ProjectAndMsgFragment.this);
            int width = getResources().getDisplayMetrics().widthPixels;
            expandableListView.setIndicatorBounds(width - 50, width - 30);
        }
        return rootView;
    }

    public void getData(String filter) {
        if (getUserVisibleHint() && isVisible()) {
            fetchData();
        }
    }

    @Override
    public void fetchData() {
        MaintenanceSituationActivity activity = (MaintenanceSituationActivity) getActivity();
        if (!TextUtils.isEmpty(activity.filter)) {
            HttpclientUtil.getData(getActivity(), url + activity.filter, handler, 0x11);
        }
    }

    @Override
    public void onGroupExpand(int groupPosition) {
        MaintenanceSituationActivity activity = (MaintenanceSituationActivity) getActivity();
        ProjectAndMsg projectAndMsg = projectAndMsgList.get(groupPosition);
        if (activity.filter.equals(filter_cache)) {
            if (projectAndMsg.projectAndMsgItem != null)
                return;
            //如果ProjectAndMsg里面的projectAndMsgItem为null，则需要请求网络
            String filter = activity.filter.substring(0, activity.filter.indexOf("&projectItemType="));
            filter = filter + "&projectItemType=" + projectAndMsg.projectType + "&implementSituation=" +
                    projectAndMsg.implementSituation;
            HttpclientUtil.getData(getActivity(), itemUrl + filter, handler, groupPosition);
        } else {
            filter_cache = activity.filter;
            if (!TextUtils.isEmpty(activity.filter)) {
                String filter = activity.filter.substring(0, activity.filter.indexOf("&projectItemType="));
                filter = filter + "&projectItemType=" + projectAndMsg.projectType + "&implementSituation=" +
                        projectAndMsg.implementSituation;
                HttpclientUtil.getData(getActivity(), itemUrl + filter, handler, groupPosition);
            }
        }

    }

    @Override
    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
        MaintenanceSituationActivity activity = (MaintenanceSituationActivity) getActivity();
        ProjectAndMsg projectAndMsg = projectAndMsgList.get(groupPosition);
        if (activity.filter.equals(filter_cache)) {
            if (projectAndMsg.projectAndMsgItem != null)
                return false;
            //如果ProjectAndMsg里面的projectAndMsgItem为null，则需要请求网络
            String filter = activity.filter.substring(0, activity.filter.indexOf("&projectItemType="));
            filter = filter + "&projectItemType=" + projectAndMsg.projectType + "&implementSituation=" +
                    projectAndMsg.implementSituation;
            HttpclientUtil.getData(getActivity(), itemUrl + filter, handler, groupPosition);
        } else {
            filter_cache = activity.filter;
            if (!TextUtils.isEmpty(activity.filter)) {
                String filter = activity.filter.substring(0, activity.filter.indexOf("&projectItemType="));
                filter = filter + "&projectItemType=" + projectAndMsg.projectType + "&implementSituation=" +
                        projectAndMsg.implementSituation;
                HttpclientUtil.getData(getActivity(), itemUrl + filter, handler, groupPosition);
            }
        }
        return true;
    }
}
