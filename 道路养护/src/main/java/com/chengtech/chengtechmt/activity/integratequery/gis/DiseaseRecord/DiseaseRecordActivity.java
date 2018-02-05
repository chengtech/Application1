package com.chengtech.chengtechmt.activity.integratequery.gis.DiseaseRecord;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.chengtech.chengtechmt.BaseActivity;
import com.chengtech.chengtechmt.R;
import com.chengtech.chengtechmt.activity.integratequery.gis.MaintenanceSituation.CapitalConstructionFragment;
import com.chengtech.chengtechmt.activity.integratequery.gis.MaintenanceSituation.CapitalFragment;
import com.chengtech.chengtechmt.activity.integratequery.gis.MaintenanceSituation.MainFragment;
import com.chengtech.chengtechmt.activity.integratequery.gis.MaintenanceSituation.ProjectAndMsgFragment;
import com.chengtech.chengtechmt.entity.gson.DeptG;
import com.chengtech.chengtechmt.fragment.gis.DiseaseRecordDialogFragment;
import com.chengtech.chengtechmt.util.HttpclientUtil;
import com.chengtech.chengtechmt.util.MyConstants;
import com.chengtech.chengtechmt.view.MyViewPager;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * 病害状况分析
 */
public class DiseaseRecordActivity extends BaseActivity implements DiseaseRecordDialogFragment.ExchangeDataListener {

    public DeptG deptG;
    private TabLayout tabLayout;
    private MyViewPager viewPager;
    List<Fragment> fragmentList;
    private FragmentPagerAdapter fragmentPagerAdapter;
    public String filter = "";
    public String[] mTitle = new String[]{"分布图","图表", "基本信息", "病害类型统计", "病害分布情况", "病害处理情况", "病害趋势分析"};
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String json = (String) msg.obj;
            Gson gson = new Gson();
            switch (msg.what) {
                case DEPT_INFO_OK:
                    json = json.replace("{\"entity\":", "");
                    json = json.substring(0, json.length() - 1);
                    deptG = gson.fromJson(json, DeptG.class);
                    break;
            }

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addContentView(R.layout.activity_disease_record);

        setNavigationIcon(true);
        hidePagerNavigation(true);

        String url = MyConstants.PRE_URL + "mt/common/index.action?mobile=phone";
        HttpclientUtil.getData(this, url, handler, DEPT_INFO_OK);
        initView();

    }

    private void initView() {
        tabLayout = (TabLayout) findViewById(R.id.id_tabLayout);
        viewPager = (MyViewPager) findViewById(R.id.id_viewPager);
        viewPager.setScrollable(false);
        tabLayout.setupWithViewPager(viewPager);
        fragmentList = new ArrayList<>();

        HeadFragment headFragment = new HeadFragment();
        DiseaseRecordChartFragment diseaseRecordChartFragment = new DiseaseRecordChartFragment();
        DiseaseRecordQueryFragment diseaseRecordQueryFragment = new DiseaseRecordQueryFragment();
        DiseaseTypeFragment diseaseTypeFragment = new DiseaseTypeFragment();
        HighwayDiseaseFragment highwayDiseaseFragment = new HighwayDiseaseFragment();
        DiseaseRecordTrend diseaseRecordTrend = new DiseaseRecordTrend();
        DiseaseHandlerFragment diseaseHandlerFragment = new DiseaseHandlerFragment();
//        CapitalConstructionFragment fragment3 = new CapitalConstructionFragment();
//
//        CapitalFragment fragment4 = new CapitalFragment();
        fragmentList.add(headFragment);
        fragmentList.add(diseaseRecordChartFragment);
        fragmentList.add(diseaseRecordQueryFragment);
        fragmentList.add(diseaseTypeFragment);
        fragmentList.add(highwayDiseaseFragment);
        fragmentList.add(diseaseHandlerFragment);
        fragmentList.add(diseaseRecordTrend);
//        fragmentList.add(fragment2);
//        fragmentList.add(fragment3);
//        fragmentList.add(fragment4);

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
                if (object instanceof HeadFragment) {
                    ((HeadFragment) object).getData(filter);
                }
                else if (object instanceof DiseaseRecordQueryFragment) {
                    ((DiseaseRecordQueryFragment) object).getData(filter);
                }
                else if (object instanceof DiseaseTypeFragment) {
                    ((DiseaseTypeFragment) object).getData(filter);
                }
                else if (object instanceof HighwayDiseaseFragment) {
                    ((HighwayDiseaseFragment) object).getData(filter);
                }
                else if (object instanceof DiseaseHandlerFragment) {
                    ((DiseaseHandlerFragment) object).getData(filter);
                }
                else if (object instanceof DiseaseRecordTrend) {
                    ((DiseaseRecordTrend) object).getData(filter);
                }
                else if (object instanceof DiseaseRecordChartFragment) {
                    ((DiseaseRecordChartFragment) object).getData(filter);
                }
                return super.getItemPosition(object);
            }
        };
        viewPager.setAdapter(fragmentPagerAdapter);
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
                if (deptG != null) {
                    DiseaseRecordDialogFragment dialogFragment = new DiseaseRecordDialogFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("Dept", deptG);
                    dialogFragment.setArguments(bundle);
                    dialogFragment.show(getFragmentManager(), "DiseaseRecordDialogFragment");
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public static void startAction(Context context) {
        Intent intent = new Intent(context, DiseaseRecordActivity.class);
        intent.putExtra("title", "病害分析查询");
        context.startActivity(intent);
    }

    @Override
    public void onExchangData(Object object) {
        String filter = (String) object;
        this.filter = filter;
        fragmentPagerAdapter.notifyDataSetChanged();
    }
}
