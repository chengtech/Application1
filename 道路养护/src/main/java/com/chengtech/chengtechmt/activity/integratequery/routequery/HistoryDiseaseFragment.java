package com.chengtech.chengtechmt.activity.integratequery.routequery;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.chengtech.chengtechmt.R;
import com.chengtech.chengtechmt.adapter.RecycleViewAdapter2;
import com.chengtech.chengtechmt.adapter.RecycleViewAdapter3;
import com.chengtech.chengtechmt.entity.routequery.MUnitPatrol;
import com.chengtech.chengtechmt.entity.routequery.PatrolRecord;
import com.chengtech.chengtechmt.impl.OnItemClickListener;
import com.chengtech.chengtechmt.util.HttpclientUtil;
import com.chengtech.chengtechmt.util.MyConstants;
import com.chengtech.chengtechmt.util.MyDialogUtil;
import com.chengtech.chengtechmt.util.PopupWindowUtils;
import com.chengtech.chengtechmt.view.TitleLayout;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 作者: LiuFuYingWang on 2016/7/26 16:16.
 */
public class HistoryDiseaseFragment extends Fragment {

    public Dialog loadDialog;
    public String routeId;
    //    public String url = MyConstants.PRE_URL + "mt/business/patrol/road/munitPatrol/listHistoryDiseaseJson.action?sectionId=&routeId=";
//    public String detail_url = MyConstants.PRE_URL + "mt/business/patrol/road/munitPatrol/addEditMUnitPatrol.action?isRead=true&flag=1&finish=1&mobile=phone&id=";
    public String url;
    public String detail_url;
    public RecycleViewAdapter2 adapter;
    public List<String[]> data;
    public RecyclerView recyclerView;
    public RecyclerView popup_recyclerView;
    private View popupView;
    private View downView;
    DisplayMetrics metrics;
    private List<PatrolRecord> patrolRecords;

    public Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            String json = (String) msg.obj;
            Gson gson = new Gson();
            switch (msg.what) {
                case 1:
                    PatrolRecord.PatrolRecordG patrolRecordG = gson.fromJson(json, PatrolRecord.PatrolRecordG.class);
                    patrolRecords = patrolRecordG.rows;
                    for (PatrolRecord p : patrolRecords) {
                        String[] content = new String[6];
                        content[0] = "病害名称:" + p.diseaseName;
                        content[1] = "开始桩号:" + p.beginPileCode;
                        content[2] = "结束桩号:" + p.endPileCode;
                        content[3] = "方向:" + p.direct;
                        content[4] = "位置:" + p.diseasePosition;
                        content[5] = "数量:" + p.diseaseNum;
                        data.add(content);
                    }
                    if (patrolRecords != null && patrolRecords.size() > 0) {
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                    adapter.notifyDataSetChanged();
                    break;
                case 2:
                    MUnitPatrol mUnitPatrol = gson.fromJson(json, MUnitPatrol.class);
                    MUnitPatrol.MunitPatrolKey key = mUnitPatrol.munitPatrolKey;

                    String[] sunTitle = getResources().getStringArray(R.array.histroyDisease_munitPatrol);
                    RecycleViewAdapter3 detail_adapter = new RecycleViewAdapter3(getActivity(), Arrays.asList(sunTitle), key.getArray(), R.layout.item_expandblelistview_child_2);
                    popup_recyclerView.setAdapter(detail_adapter);
                    popup_recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    showPopup();
                    break;
            }
        }
    };

    public HistoryDiseaseFragment() {
        url = MyConstants.PRE_URL + "mt/business/patrol/road/munitPatrol/listHistoryDiseaseJson.action?sectionId=&routeId=";
        detail_url = MyConstants.PRE_URL + "mt/business/patrol/road/munitPatrol/addEditMUnitPatrol.action?isRead=true&flag=1&finish=1&mobile=phone&id=";
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_history_disease, container, false);
        popupView = inflater.inflate(R.layout.historydise_popup, null, false);
        popup_recyclerView = (RecyclerView) popupView.findViewById(R.id.id_recycleView);
        downView = view.findViewById(R.id.view);
        loadDialog = MyDialogUtil.createDialog(getActivity(), "正在加载中...");
        loadDialog.show();
        return view;


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        routeId = bundle.getString("routeId");
        //网络请求
        getData(url + routeId, 1);
        metrics = getActivity().getResources().getDisplayMetrics();

        recyclerView = (RecyclerView) view.findViewById(R.id.recycleView);
        recyclerView.setVisibility(View.GONE);

        data = new ArrayList<>();
        adapter = new RecycleViewAdapter2(getActivity(), data, R.layout.item_recycle3);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                getData(detail_url + patrolRecords.get(position).id, 2);


            }
        });

    }

    //弹出popupwindow
    private void showPopup() {
        final PopupWindow popupWindow1 = new PopupWindow(popupView, metrics.widthPixels, metrics.heightPixels);
        popupWindow1.setBackgroundDrawable(new ColorDrawable(0000));
        popupWindow1.setFocusable(true);
        popupWindow1.setOutsideTouchable(true);
        WindowManager.LayoutParams param = getActivity().getWindow().getAttributes();
        param.alpha = 0.5f;
        getActivity().getWindow().setAttributes(param);
        popupWindow1.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams param = getActivity().getWindow().getAttributes();
                param.alpha = 1f;
                getActivity().getWindow().setAttributes(param);
            }
        });
        popupWindow1.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                /**** 如果点击了popupwindow的外部，popupwindow也会消失 ****/
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    popupWindow1.dismiss();
                    return true;
                }
                return false;
            }
        });

        popupWindow1.setAnimationStyle(R.style.PopupAnimationVertical);
        popupWindow1.showAsDropDown(downView);

//      popupWindow1.showAtLocation((View) view.getParent(),Gravity.TOP,0,0);
    }


    private void getData(String url, final int type) {
        AsyncHttpClient client = HttpclientUtil.getInstance(getActivity());

        AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onSuccess(int i, cz.msebera.android.httpclient.Header[] headers, byte[] bytes) {
                loadDialog.dismiss();
                try {
                    String data = new String(bytes, "utf-8");
                    data = data.replace("pager.", "");
                    Message message = Message.obtain();
                    message.obj = data;
                    message.what = type;
                    handler.sendMessage(message);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int i, cz.msebera.android.httpclient.Header[] headers, byte[] bytes, Throwable throwable) {
                loadDialog.dismiss();
            }

        };
        client.get(url,
                responseHandler);

    }
}
