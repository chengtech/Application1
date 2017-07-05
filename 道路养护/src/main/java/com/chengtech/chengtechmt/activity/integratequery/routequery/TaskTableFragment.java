package com.chengtech.chengtechmt.activity.integratequery.routequery;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chengtech.chengtechmt.R;
import com.chengtech.chengtechmt.adapter.RecycleViewAdapter3;

import java.util.Arrays;
import java.util.List;

/**
 * 作者: LiuFuYingWang on 2016/8/11 11:43.
 */
public class TaskTableFragment extends Fragment {

    private RecyclerView recyclerView;
    List<String> content ;
    String [] subtitle ;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_history_disease,container,false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycleView);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        content = (List<String>) bundle.getSerializable("content");
        subtitle = bundle.getStringArray("subTitle");
        if (content!=null && subtitle!=null) {
            RecycleViewAdapter3 adapter3 = new RecycleViewAdapter3(getActivity(), Arrays.asList(subtitle),content,R.layout.item_expandblelistview_child_2);
            recyclerView.setAdapter(adapter3);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
    }
}
