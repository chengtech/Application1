package com.chengtech.chengtechmt.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.chengtech.chengtechmt.R;
import com.chengtech.chengtechmt.activity.integratequery.routequery.TaskTableFragment;
import com.chengtech.chengtechmt.entity.routequery.TaskTable;
import com.chengtech.chengtechmt.util.MyDialogUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者: LiuFuYingWang on 2016/8/11 10:53.
 */
public class TaskTableDialogFragment extends DialogFragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TaskTable taskTable;
    private String[] mTitle = new String[]{"报备信息", "交底信息", "记录信息", "整改信息"
            , "验收信息"};


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_table, container, false);
        tabLayout = (TabLayout) view.findViewById(R.id.id_tabLayout);
        viewPager = (ViewPager) view.findViewById(R.id.id_viewPager);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        taskTable = (TaskTable) bundle.getSerializable("taskTable");
        viewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                Bundle bundle1 = new Bundle();
                switch (position) {
                    case 0:
                        break;
                    case 1:
                        if (taskTable.safetyScience!=null && !TextUtils.isEmpty(taskTable.safetyScience.id)){
                            bundle1.putSerializable("content", (Serializable) taskTable.safetyScience.getArray());
                            bundle1.putSerializable("subTitle",getResources().getStringArray(R.array.taskTable_jiaodi));
                        }
                        break;
                    case 2:
                        if (taskTable.workRecordTable!=null && !TextUtils.isEmpty(taskTable.workRecordTable.id)){
                        bundle1.putSerializable("content", (Serializable) taskTable.workRecordTable.getArray());
                        bundle1.putSerializable("subTitle",getResources().getStringArray(R.array.taskTable_jilu));
                        }
                        break;
                    case 3:
                        break;
                    case 4:
                        if (taskTable.acceptance!=null && !TextUtils.isEmpty(taskTable.acceptance.id)) {
                            bundle1.putSerializable("content", (Serializable) taskTable.acceptance.getArray());
                            bundle1.putSerializable("subTitle", getResources().getStringArray(R.array.taskTable_yanshou));
                        }
                        break;
                }
                TaskTableFragment ttf = new TaskTableFragment();
                ttf.setArguments(bundle1);
                return ttf;
            }


            @Override
            public int getCount() {
                return mTitle.length;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return mTitle[position];
            }
        });

        tabLayout.setupWithViewPager(viewPager);


    }
}
