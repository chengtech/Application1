package com.chengtech.chengtechmt.activity.monitoremergency.slopemonitor;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.chengtech.chengtechmt.R;
import com.chengtech.chengtechmt.entity.Slope;
import com.chengtech.chengtechmt.entity.monitoremergency.SideMonitorTree;
import com.chengtech.chengtechmt.entity.monitoremergency.WarningManage;
import com.chengtech.chengtechmt.thirdlibrary.wxpictureselector.MyPictureAdapter;
import com.chengtech.chengtechmt.util.HttpclientUtil;
import com.chengtech.chengtechmt.util.MyConstants;
import com.chengtech.chengtechmt.view.MyHorizontalScrollView3;
import com.github.jjobes.slidedatetimepicker.SlideDateTimeListener;
import com.github.jjobes.slidedatetimepicker.SlideDateTimePicker;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * 作者: LiuFuYingWang on 2017/6/6 15:33.
 * 边坡告警信息列表fragment
 */

public class WraningManageFragment extends Fragment {
    private static final int RESULT_CODE_1 = 0x01;
    private Spinner slopeIdSpinner, levelSpinner, stateSpinner;
    private TextView startDate, endDate;
    private Button search_bt;
    private List<WarningManage> warningManageList = new ArrayList<>();
    private List<SideMonitorTree> data;

    private MyHorizontalScrollView3 horizontalScrollView;
    private String listUrl = MyConstants.PRE_URL+"mt/monitoremergency/sidemonitor/warningmanage/listWarningManageJson.action";

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Gson gson = new Gson();
            String json = (String) msg.obj;
            switch (msg.what) {
                case RESULT_CODE_1:
                    JSONObject jsonObject = null;
                    try {
                        List<List<String>> data = new ArrayList<>();
                        if (!TextUtils.isEmpty(json)) {
                            jsonObject = new JSONObject(json);
                            JSONArray rows = jsonObject.getJSONArray("rows");
                            if (rows != null && rows.length() > 0) {
                                for (int i = 0; i < rows.length(); i++) {
                                    String content = rows.getString(i);
                                    WarningManage warningManage = gson.fromJson(content, WarningManage.class);
                                    data.add(warningManage.getContent());
                                    warningManageList.add(warningManage);
                                }
                            }
                        }
                        data.add(0, new WarningManage().getTitles());
//                        horizontalScrollView.setPercentage(new double[]{});
                        for (int i = 0; i < 3; i++) {
                            WarningManage warningManage = new WarningManage();
                            warningManage.sideMonitorTypeId = "000" + (i + 1);
                            warningManage.warningLevel = "告警级别" + (i + 1);
                            warningManage.createDate = "000" + (i + 1);
                            warningManage.cause = "000" + (i + 1);
                            warningManage.warningMessage = "000" + (i + 1);
                            warningManage.count = (i + 1);
                            warningManage.detaiMessage = "000" + (i + 1);
                            warningManage.state = "000" + (i + 1);
                            warningManageList.add(warningManage);
                            data.add(warningManage.getContent());
                        }
                        horizontalScrollView.setData(data);
                        List<String> list = new ArrayList<>();
                        list.add("告警级别1");
                        list.add("告警级别3");
                        list.add("告警级别1");
                        list.add("告警级别2");
                        list.add("告警级别2");
                        list.add("告警级别2");
                        list.add("告警级别1");
                        list.add("告警级别1");
                        list.add("告警级别3");
                        list.add("告警级别3");
                        list.add("告警级别1");
                        list.add("告警级别2");
                        SlopeMonitorActivity slopeMonitorActivity = (SlopeMonitorActivity) getActivity();
                        slopeMonitorActivity.charData.put("告警级别1", Collections.frequency(list, "告警级别1"));
                        slopeMonitorActivity.charData.put("告警级别2", Collections.frequency(list, "告警级别2"));
                        slopeMonitorActivity.charData.put("告警级别3", Collections.frequency(list, "告警级别3"));
                        slopeMonitorActivity.charData2.put("已确认", Collections.frequency(list, "告警级别2"));
                        slopeMonitorActivity.charData2.put("未确认", Collections.frequency(list, "告警级别3"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_warning_manage, container, false);
        slopeIdSpinner = (Spinner) view.findViewById(R.id.slope);
        levelSpinner = (Spinner) view.findViewById(R.id.warningLevel);
        stateSpinner = (Spinner) view.findViewById(R.id.state);
        startDate = (TextView) view.findViewById(R.id.startDate);
        endDate = (TextView) view.findViewById(R.id.endDate);
        search_bt = (Button) view.findViewById(R.id.search);
        horizontalScrollView = (MyHorizontalScrollView3) view.findViewById(R.id.horizontalScrollView);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            warningManageList = (List<WarningManage>) savedInstanceState.getSerializable("data");
            List<List<String>> data = new ArrayList<>();
            data.add(new WarningManage().getTitles());
            for (int i = 0; i < warningManageList.size(); i++) {
                data.add(warningManageList.get(i).getContent());
            }
            horizontalScrollView.setData(data);
        }
        Bundle bundle = getArguments();
        data = (List<SideMonitorTree>) bundle.getSerializable("data");
        List<String> data2 = new ArrayList<>();
        data2.add("请选择");
        for (int i = 0; i < data.size(); i++) {
            data2.add(data.get(i).name);
        }
        slopeIdSpinner.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, data2));
        levelSpinner.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, new String[]{"请选择", "一级", "二级", "三级"}));
        stateSpinner.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, new String[]{"请选择", "已确认", "未确认"}));

        SlideDateTimeListener listener = new SlideDateTimeListener() {

            @Override
            public void onDateTimeSet(Date date) {
                // Do something with the date. This Date object contains
                // the date and time that the user has selected.
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                boolean isClicking = (boolean) startDate.getTag();
                if (isClicking) {
                    startDate.setText(format.format(date));
                } else {
                    endDate.setText(format.format(date));
                }

            }

            @Override
            public void onDateTimeCancel() {
                // Overriding onDateTimeCancel() is optional.
            }
        };
        final SlideDateTimePicker picker = new SlideDateTimePicker.Builder(getFragmentManager())
                .setListener(listener)
                .setInitialDate(new Date())
                .setIs24HourTime(true)
                .build();
        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDate.setTag(true);
                picker.show();
            }
        });
        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDate.setTag(false);
                picker.show();
            }
        });
        search_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                handler.sendEmptyMessage(RESULT_CODE_1);
                //进行网络请求
                int position = slopeIdSpinner.getSelectedItemPosition();
                if (position!=0) {
                    String sideMonitorTreeId = data.get(slopeIdSpinner.getSelectedItemPosition()-1).id;
                    String warningLevel = (String) levelSpinner.getSelectedItem();
                    String warningState = (String) stateSpinner.getSelectedItem();
                    String startDateStr = startDate.getText().toString();
                    String endDateStr = endDate.getText().toString();
                    String url = listUrl + "?sideMonitorTreeId=" +sideMonitorTreeId+"&warningLevel="
                            +(warningLevel.equals("请选择")?"":warningLevel)+"&state="+
                            (warningState.equals("请选择")?"":warningState)+"&startDate="+startDateStr+"&endDate="+endDateStr;
                    HttpclientUtil.getData(getContext(), url, handler, RESULT_CODE_1);
                }else {
                    Toast.makeText(getContext(), "请选择边坡", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("data", (Serializable) warningManageList);
    }
}
