package com.chengtech.chengtechmt.activity.dbm;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ScrollView;
import android.widget.Toast;

import com.chengtech.chengtechmt.BaseActivity;
import com.chengtech.chengtechmt.R;
import com.chengtech.chengtechmt.activity.IView;
import com.chengtech.chengtechmt.activity.business.ListPageActivity;
import com.chengtech.chengtechmt.adapter.HorizotalScrollAdapter;
import com.chengtech.chengtechmt.adapter.RecycleViewAdapter2;
import com.chengtech.chengtechmt.entity.MapEntity;
import com.chengtech.chengtechmt.entity.MediumPlanprogress;
import com.chengtech.chengtechmt.entity.Route;
import com.chengtech.chengtechmt.presenter.RoutePresenter;
import com.chengtech.chengtechmt.util.ACache;
import com.chengtech.chengtechmt.util.AppAccount;
import com.chengtech.chengtechmt.util.MyDialogUtil;
import com.chengtech.chengtechmt.util.UserUtils;
import com.chengtech.chengtechmt.view.TitleLayout;
import com.shelwee.uilistview.ui.UiListView;
import com.shelwee.uilistview.ui.UiListView.ClickListener;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RouteActivity extends BaseActivity implements IView<List<Route>>, View.OnClickListener, com.chengtech.chengtechmt.impl.OnItemClickListener {

    //    private UiListView uiListView;
//    private ACache aCache;
    private int pageNo = 1;
    private int pageSize = 100;
    private List<Route> routes;
    private RoutePresenter routePresenter;
    public int maxPage;
    //    public NestedScrollView sv;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addContentView(R.layout.activity_route);
        initView();

        setNavigationIcon(true);

//        toolbar.setNavigationIcon(R.drawable.arrow_back2);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });


//        String maxPageStr = aCache.getAsString(AppAccount.userId + getClass().getSimpleName() + "maxPage");
//        maxPage = Integer.parseInt(TextUtils.isEmpty(maxPageStr) ? "3" : maxPageStr);

        //从缓存中取数据，如果没有就从网络中加载，有就直接使用
//        routes = (List<Route>) aCache.getAsObject(AppAccount.userId + getClass().getSimpleName() + pageNo);
//        if (routes == null || routes.size() < 0) {
        routePresenter.getData(this, pageNo, pageSize);
//        } else {
//            loadDataFromCache(routes);
//        }

//        uiListView.setClickListener(new ClickListener() {
//
//            @Override
//            public void onClick(int index) {
////                Intent intent = new Intent(RouteActivity.this, WebViewActivity.class);
////                intent.putExtra("url", MyConstants.ROUTE_DETAIL_URL + routes.get(index).id);
////                intent.putExtra("title", routes.get(index).name);
////                startActivity(intent);
//
//                Intent intent = new Intent(RouteActivity.this, DetailActivity.class);
////                intent.putExtra("url", MyConstants.ROUTE_DETAIL_URL + routes.get(index).id);
//                intent.putExtra("title", routes.get(index).name);
//                intent.putExtra("className", "Route");
//                intent.putExtra("id", routes.get(index).id);
//                intent.putExtra("sessionId",routes.get(index).sessionId);
//                intent.putExtra("map",new MapEntity("路线",routes.get(index).code,null,null));
//                intent.putExtra("content", (ArrayList) routes.get(index).getArray());
//                intent.putExtra("subtitle", (Serializable) Arrays.asList(getResources().getStringArray(R.array.route_detail)));
//                intent.putExtra("subtitleProperty", (Serializable) Arrays.asList(getResources().getStringArray(R.array.route_detail_property)));
//                startActivity(intent);
//            }
//        });

    }

    private void initView() {

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
//        uiListView = (UiListView) findViewById(R.id.uilist);

//        aCache = ACache.get(this);
        routePresenter = new RoutePresenter(this);
//        sv = (NestedScrollView) findViewById(R.id.scrollView);

        up_action.setOnClickListener(this);
        down_action.setOnClickListener(this);
    }

    @Override
    public void showDialog() {
        loadDialog.show();
    }

    @Override
    public void dismssDialog() {
        loadDialog.dismiss();
    }

    @Override
    public void loadDataSuccess(List<Route> routes) {
        List<String[]> titleArrayList = new ArrayList<>();
        if (routes.size() > 0 && routes != null) {
            //先清除之前的缓存
//            uiListView.clear();
            for (Route route : routes) {
                String[] titleArray = new String[5];
                titleArray[0] = route.name;
                titleArray[1] = "路线编号:" + route.code;
                titleArray[2] = "<br/>管养单位:  " + route.dept.name;
                titleArray[3] = "<br/>长度(km):  " + route.length;
                titleArray[4] = "<br/>路线走向:  " + (route.routeDirections=="null"?"":route.routeDirections);
                titleArrayList.add(titleArray);
            }
        } else {
            Toast.makeText(RouteActivity.this, "没有数据", Toast.LENGTH_SHORT).show();
            pageNo--;
            pageNo_tv.setText(pageNo + "");
        }
        RecycleViewAdapter2 adapter = new RecycleViewAdapter2(this, titleArrayList, R.layout.item_recycle3);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.setOnItemClickListener(this);
        maxPage = (int) Math.ceil(routePresenter.getRoute().totalRows / pageSize);

//        aCache.put(AppAccount.userId + getClass().getSimpleName() + pageNo, (ArrayList) routes, ACache.TIME_DAY * 30);
//        aCache.put(AppAccount.userId + getClass().getSimpleName() + "maxPage", maxPage + "", ACache.TIME_DAY * 30);

        this.routes = routes;
        //释放对象，减少内存
        routes = null;

        //srcollview滑动到顶端，必须使用post方法，放到消息队列。
//        new Handler().post(new Runnable() {
//
//            @Override
//            public void run() {
//                sv.fullScroll(ScrollView.FOCUS_UP);
//
//            }
//        });
    }

    @Override
    public void loadDataSuccess(List<Route> routes, int type) {

    }

    @Override
    public void loadDataSuccess(List<Route> routes, String clasName) {

    }

    @Override
    public void loadDataFailure() {
        Toast.makeText(this, "服务器连接出错", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void hasError() {
        UserUtils.reLogin(RouteActivity.this, loadDialog);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.perPage:
                if (pageNo != 1) {
                    pageNo--;
                    pageNo_tv.setText(pageNo + "");
                    //判断缓存是否有数据
//                    routes = (List<Route>) aCache.getAsObject(AppAccount.userId + getClass().getSimpleName() + pageNo);
                    if (routes == null || routes.size() < 0) {
                        routePresenter.getData(this, pageNo, pageSize);
                    } else {
                        loadDataFromCache(routes);
                    }
                } else {
//                    Snackbar.make(v, "当前是最新页", Snackbar.LENGTH_SHORT).show();
                    Toast.makeText(this, "当前是最新页", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.nextPage:
                if (pageNo < maxPage) {
                    pageNo++;
                    pageNo_tv.setText(pageNo + "");
                    //判断缓存是否有数据
//                    routes = (List<Route>) aCache.getAsObject(AppAccount.userId + getClass().getSimpleName() + pageNo);
                    if (routes == null || routes.size() < 0) {
                        routePresenter.getData(this, pageNo, pageSize);

                    } else {
                        loadDataFromCache(routes);
                    }
                } else {
//                    Snackbar.make(v, "当前已经是最后一页", Snackbar.LENGTH_SHORT).show();
                    Toast.makeText(RouteActivity.this, "当前已经是最后一页", Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }

    public void loadDataFromCache(List<Route> routes) {
//        if (routes.size() > 0 && routes != null) {
//            //先清除之前的缓存
//            uiListView.clear();
//            for (Route d : routes) {
//                uiListView.addBasicItem(d.name);
//            }
//            uiListView.commit();
//        }

        //srcollview滑动到顶端，必须使用post方法，放到消息队列。
//        new Handler().post(new Runnable() {
//
//            @Override
//            public void run() {
//                sv.fullScroll(ScrollView.FOCUS_UP);
//
//            }
//        });

    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(RouteActivity.this, DetailActivity.class);
//                intent.putExtra("url", MyConstants.ROUTE_DETAIL_URL + routes.get(index).id);
        intent.putExtra("title", routes.get(position).name);
        intent.putExtra("className", "Route");
        intent.putExtra("id", routes.get(position).id);
        intent.putExtra("sessionId", routes.get(position).sessionId);
        intent.putExtra("map", new MapEntity("路线", routes.get(position).code, null, null));
        intent.putExtra("content", (ArrayList) routes.get(position).getArray());
        intent.putExtra("subtitle", (Serializable) Arrays.asList(getResources().getStringArray(R.array.route_detail)));
        intent.putExtra("subtitleProperty", (Serializable) Arrays.asList(getResources().getStringArray(R.array.route_detail_property)));
        startActivity(intent);
    }
}
