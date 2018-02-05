package com.chengtech.chengtechmt.activity.integratequery.gis.MaintenanceSituation;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.chengtech.chengtechmt.BaseActivity;
import com.chengtech.chengtechmt.R;
import com.chengtech.chengtechmt.fragment.gis.MaintenanceDialogFragment;
import com.chengtech.chengtechmt.view.MyViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * 维修实施情况
 */
public class MaintenanceSituationActivity extends BaseActivity implements MaintenanceDialogFragment.ExchangeDataListener {
    private TabLayout tabLayout;
    private MyViewPager viewPager;
    List<Fragment> fragmentList;
    private FragmentPagerAdapter fragmentPagerAdapter;
    public String filter = "";
    public String[] mTitle = new String[]{"分布图","图表", "基本概况", "建设情况", "资金分布情况"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addContentView(R.layout.activity_maintenance_situation);


        setNavigationIcon(true);
        hidePagerNavigation(true);

        initView();

    }

    private void initView() {
        tabLayout = (TabLayout) findViewById(R.id.id_tabLayout);
        viewPager = (MyViewPager) findViewById(R.id.id_viewPager);
        viewPager.setScrollable(false);
        tabLayout.setupWithViewPager(viewPager);
        fragmentList = new ArrayList<>();

         MainFragment mainFragment = new MainFragment();
        ChartFragment chartFragment = new ChartFragment();
        ProjectAndMsgFragment fragment2 = new ProjectAndMsgFragment();
        CapitalConstructionFragment fragment3 = new CapitalConstructionFragment();

        CapitalFragment fragment4 = new CapitalFragment();
        fragmentList.add(mainFragment);
        fragmentList.add(chartFragment);
        fragmentList.add(fragment2);
        fragmentList.add(fragment3);
        fragmentList.add(fragment4);

        fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);
            }

            @Override
            public int getCount() {
                return fragmentList.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return mTitle[position];
            }

            @Override
            public int getItemPosition(Object object) {
                if (object instanceof MainFragment) {
                    ((MainFragment) object).getData(filter);
                } else if (object instanceof ProjectAndMsgFragment) {
                    ((ProjectAndMsgFragment) object).getData(filter);
                } else if (object instanceof CapitalConstructionFragment) {
                    ((CapitalConstructionFragment) object).getData(filter);
                } else if (object instanceof CapitalFragment) {
                    ((CapitalFragment) object).getData(filter);
                }
                else if (object instanceof ChartFragment) {
                    ((ChartFragment) object).getData(filter);
                }
                return super.getItemPosition(object);
            }
        };
        viewPager.setAdapter(fragmentPagerAdapter);
    }

    public static void startAction(Context context) {
        Intent intent = new Intent(context, MaintenanceSituationActivity.class);
        intent.putExtra("title", "维修实施情况");
        context.startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(1, 0x01, 1, "筛选").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 0x01:
                MaintenanceDialogFragment dialogFragment = new MaintenanceDialogFragment();
                dialogFragment.show(getFragmentManager(), "MaintenanceDialogFragment");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onExchangData(Object object) {
        String filter = (String) object;
        this.filter = filter;
        fragmentPagerAdapter.notifyDataSetChanged();

    }

}
