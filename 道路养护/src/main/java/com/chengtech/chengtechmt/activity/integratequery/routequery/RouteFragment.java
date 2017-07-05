package com.chengtech.chengtechmt.activity.integratequery.routequery;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chengtech.chengtechmt.R;
import com.chengtech.chengtechmt.adapter.RecycleViewAdapter3;
import com.chengtech.chengtechmt.entity.Route;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 作者: LiuFuYingWang on 2016/7/22 16:10.
 */
public class RouteFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_route_query,container,false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        Route route = (Route) bundle.getSerializable("data");
        if (route!=null) {
            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycleView);
            List<String> content = route.getArray();
            String [] subtitle = getResources().getStringArray(R.array.route_detail);
            RecycleViewAdapter3 adapter3 = new RecycleViewAdapter3(getActivity(), Arrays.asList(subtitle),content,R.layout.item_expandblelistview_child_2);
            recyclerView.setAdapter(adapter3);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }

    }
}
