package com.chengtech.chengtechmt.activity.dbm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;

import com.chengtech.chengtechmt.BaseActivity;
import com.chengtech.chengtechmt.R;
import com.chengtech.chengtechmt.adapter.MyExpandableAdapter;
import com.chengtech.chengtechmt.entity.UserRight;
import com.chengtech.chengtechmt.util.ACache;
import com.chengtech.chengtechmt.util.AppAccount;
import com.chengtech.chengtechmt.util.MyConstants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liufuyingwang
 *         下午3:27:29
 */
public class DbmActivity extends BaseActivity {

//    private ACache aCache;
    private ExpandableListView expandableListView;
    private MyExpandableAdapter expandableAdapter;
    private String[] groupName = {"静态数据管理", "动态数据管理"};
    private Map<String, List<String>> groupList;
    private List<String> childUrl1;
    private List<String> childClassname1;
    private List<String> childList1;
    //    private int [] type_array = new int []{0,1,2,8,9,10,6,7,3,4,5};
    private String[] type_order = new String[]{
            "Route", "Section", "WorkSection", "Bridge", "Tunnel", "Culvert", "RoadSideFacilities",
            "Slope", "SafetyFacilities", "Greening", "Observatory","RoadGreening"
    };
    private List<Integer> order = new ArrayList<Integer>(); //用于根据权限来判断不同用户具有不用的功能项


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addContentView(R.layout.activity_dbm);

        initView();
        setNavigationIcon(true);
        hidePagerNavigation(true);
        setAppBarLayoutScroll(false);

        expandableListView.setOnChildClickListener(new OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {

                try {
                    Class clazz = Class.forName(childClassname1.get(childPosition));
                    Intent intent = new Intent(DbmActivity.this, clazz);
                    intent.putExtra("url", childUrl1.get(childPosition));
                    intent.putExtra("type", type_order[childPosition]);
                    intent.putExtra("title", childList1.get(childPosition));
                    startActivity(intent);
                    return true;
                } catch (Exception e) {
                }
                return false;
            }
        });
        expandableListView.expandGroup(0);

    }

    private void initView() {
//        aCache = ACache.get(this);

        expandableListView = (ExpandableListView) findViewById(R.id.expand_listview);
        groupList = new HashMap<String, List<String>>();
//        childList1 = new ArrayList<String>();
        childUrl1 = new ArrayList<String>();
//        childClassname1 = new ArrayList<String>();
        //根据权限显示该用户具有的功能项
        String[] array_childUrl1 = getResources().getStringArray(R.array.dbm_group_1_url);
        String[] array_childClassname1 = getResources().getStringArray(R.array.dbm_group_1_className);
        String[] array_childList1 = getResources().getStringArray(R.array.dbm_group_1);
//        List<UserRight> userRights = AppAccount.userRights;
//        for (int i = 0; i < array_childList1.length; i++) {
//            for (UserRight u : userRights) {
//                if (array_childList1[i].equals(u.itemName)) {
//                    if (u.hasRight) {
//                        order.add(i); //得到了权限的顺序
//                    }
//                    break;
//                }
//            }
//        }
        //根据权限的顺序来获取相应的功能模块链接
//        for (int i = 0; i < order.size(); i++) {
//            childList1.add(array_childList1[order.get(i)]);
//            childUrl1.add(MyConstants.PRE_URL+array_childUrl1[order.get(i)]);
//            childClassname1.add(array_childClassname1[order.get(i)]);
//            type_order[i] = type_array[order.get(i)]; //在worksectionActivity中决定是解析哪个实体的order
//
//        }

        childList1 = Arrays.asList(array_childList1);
        for (int i = 0; i < array_childUrl1.length; i++) {
            childUrl1.add(MyConstants.PRE_URL + array_childUrl1[i]);
        }
        childClassname1 = Arrays.asList(array_childClassname1);

        groupList.put(groupName[0], childList1);
        expandableAdapter = new MyExpandableAdapter(this, groupName, groupList);
        int width = getResources().getDisplayMetrics().widthPixels;
        expandableListView.setIndicatorBounds(width - 150, width - 70);
        expandableListView.setAdapter(expandableAdapter);
    }

}
