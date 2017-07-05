package com.chengtech.chengtechmt;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chengtech.chengtechmt.behavior.ScrollAwareFABBehavior;
import com.chengtech.chengtechmt.util.ActivityCollector;
import com.chengtech.chengtechmt.util.MyDialogUtil;

import java.util.HashMap;
import java.util.Map;

public class BaseActivity extends AppCompatActivity {
    protected Toolbar toolbar;
    protected Bundle bundle;
    protected FloatingActionButton up_action, down_action;
    protected TextView pageNo_tv;
    protected FrameLayout container;
    protected ViewGroup contentView;
    protected Dialog loadDialog;
    protected AppBarLayout appBarLayout;
    public int pageNo = 1;
    public int pageSize = 100;
    public int maxPage = 1; //默认最大页为1，然后进行加减
    public Map<String,String> deptTree = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        ActivityCollector.addActivity(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        bundle = getIntent().getExtras();
        if (bundle != null) {
            toolbar.setTitle(bundle.getString("title") == null ? "" : bundle.getString("title"));
        }
        setSupportActionBar(toolbar);


//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        up_action = (FloatingActionButton) findViewById(R.id.perPage);
        down_action = (FloatingActionButton) findViewById(R.id.nextPage);
        container = (FrameLayout) findViewById(R.id.container);
        pageNo_tv = (TextView) findViewById(R.id.current_pageno);
        loadDialog = MyDialogUtil.createDialog(this, "正在加载..");
        appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        hidePagerNavigation(false);
    }

    public void addContentView(int layoutId) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        contentView = (ViewGroup) inflater.inflate(layoutId, null, false);
        ViewGroup.LayoutParams lp = new GridLayoutManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        contentView.setLayoutParams(lp);
        contentView.setBackground(null);
        if (null != container) {
            container.addView(contentView);
        }
    }

    public void addContentView(View view) {
        if (null != container) {
            container.addView(view);
        }
    }

    public void setAppBarLayoutScroll(boolean isScroll) {
        AppBarLayout.LayoutParams layoutP = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
        //让toolbar不在滑动
        if (!isScroll) {
            layoutP.setScrollFlags(0);
        }
        toolbar.setLayoutParams(layoutP);
    }

    public void setNavigationIcon(boolean setNaviga) {
        if (setNaviga) {
            toolbar.setNavigationIcon(R.drawable.arrow_back2);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }

    //是否显示页码导航，默认：显示
    public void hidePagerNavigation(boolean isHide) {
        if (isHide) {
            CoordinatorLayout.LayoutParams lp1 = (CoordinatorLayout.LayoutParams) up_action.getLayoutParams();
            lp1.setBehavior(null);
            up_action.setLayoutParams(lp1);
            CoordinatorLayout.LayoutParams lp2 = (CoordinatorLayout.LayoutParams) down_action.getLayoutParams();
            lp2.setBehavior(null);
            down_action.setLayoutParams(lp2);
            up_action.setVisibility(View.GONE);
            down_action.setVisibility(View.GONE);
            pageNo_tv.setVisibility(View.GONE);
        } else {

            CoordinatorLayout.LayoutParams lp1 = (CoordinatorLayout.LayoutParams) up_action.getLayoutParams();
            lp1.setBehavior(new ScrollAwareFABBehavior(this, null));
            up_action.setLayoutParams(lp1);
            CoordinatorLayout.LayoutParams lp2 = (CoordinatorLayout.LayoutParams) down_action.getLayoutParams();
            lp2.setBehavior(new ScrollAwareFABBehavior(this, null));
            down_action.setLayoutParams(lp2);
            up_action.setVisibility(View.VISIBLE);
            down_action.setVisibility(View.VISIBLE);
            pageNo_tv.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}
