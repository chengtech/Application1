package com.chengtech.imooc_festsms.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.chengtech.imooc_festsms.R;
import com.chengtech.imooc_festsms.bean.Festival;
import com.chengtech.imooc_festsms.bean.FestivalLab;

import java.util.List;

/**
 * Created by Administrator on 2015/12/16.
 */
public class FestivalCategoryFragment extends Fragment{

    private LayoutInflater miInflater;
    private GridView gridView;
    private ArrayAdapter<Festival> gridViewAdapter;
    private List<Festival> festivalList;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.miInflater = inflater;
        return inflater.inflate(R.layout.fragment_festival_category,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        gridView = (GridView) view.findViewById(R.id.gridView);
        FestivalLab festivalLab = FestivalLab.getFestivalLab();
        festivalList = festivalLab.getFestivalList();
        gridViewAdapter = new ArrayAdapter<Festival>(getActivity(), -1, festivalList)
        {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView==null) {
                    convertView = miInflater.inflate(R.layout.item_gridview,parent,false);
                }
                TextView tv = (TextView) convertView.findViewById(R.id.tv_item_gridView);
                tv.setText(festivalList.get(position).getContent());
                return convertView;
            }
        };
        gridView.setAdapter(gridViewAdapter);
    }
}
