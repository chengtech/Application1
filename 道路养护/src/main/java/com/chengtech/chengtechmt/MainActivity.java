package com.chengtech.chengtechmt;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chengtech.chengtechmt.activity.AboutMeActivity;
import com.chengtech.chengtechmt.activity.ChartWebViewActivity;
import com.chengtech.chengtechmt.activity.MapQueryActivity;
import com.chengtech.chengtechmt.activity.MultiChartWebViewActivity;
import com.chengtech.chengtechmt.activity.WebViewActivity;
import com.chengtech.chengtechmt.activity.business.BusinessActivity;
import com.chengtech.chengtechmt.activity.business.DiseaseRegistrationListActivity;
import com.chengtech.chengtechmt.activity.dbm.DbmActivity;
import com.chengtech.chengtechmt.activity.expertdecision.ExpertDecisionActivity;
import com.chengtech.chengtechmt.activity.integratequery.IntegrateQueryActivity;
import com.chengtech.chengtechmt.activity.LoginActivity;
import com.chengtech.chengtechmt.activity.NoticeActivity;
import com.chengtech.chengtechmt.activity.monitoremergency.MonitorEmergencyActivity;
import com.chengtech.chengtechmt.activity.standard.StandardActivity;
import com.chengtech.chengtechmt.adapter.AttachementInfoAdapter;
import com.chengtech.chengtechmt.adapter.RecycleViewAdapter;
import com.chengtech.chengtechmt.divider.GridViewDivider;
import com.chengtech.chengtechmt.entity.UserRight;
import com.chengtech.chengtechmt.entity.attachment.AttachmentInfo;
import com.chengtech.chengtechmt.util.AppAccount;
import com.chengtech.chengtechmt.util.CommonUtils;
import com.chengtech.chengtechmt.util.HttpclientUtil;
import com.chengtech.chengtechmt.util.MyConstants;
import com.chengtech.chengtechmt.util.RealmUtil;
import com.chengtech.chengtechmt.view.MyDialog;
import com.chengtech.chengtechmt.view.SlidingMenu;
import com.pgyersdk.crash.PgyCrashManager;
import com.pgyersdk.update.PgyUpdateManager;

import org.apache.http.client.protocol.ClientContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.client.CookieStore;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class MainActivity extends AppCompatActivity implements OnClickListener {
    private ViewPager viewPager;
    private LinearLayout pointGroup;
    private int lastPointPos;
    private int lastX;
    private int lastY;
    private List<ImageView> imageViewList;
    private int[] bannerIds = {R.mipmap.bar_1, R.mipmap.bar_2, R.mipmap.banner1};
    private TextView username_tv;
    private ImageView schedule_iv, notice_iv;
    private List<Map<String, Object>> data;
    private List<Integer> order = new ArrayList<Integer>(); // 用于判断权限位置的顺序
    private RelativeLayout logout_tv, aboutMe_tv, attachment_rl;
    private Map<String, Class> activityName = new HashMap<String, Class>();
    private SlidingMenu leftmenu;
    private long lastTime;

    private Toolbar toolbar;
    private LinearLayout map, chart_layout, diseaseReg_layout;

    public RelativeLayout bottom1, bottom2, bottom3;

    private RecycleViewAdapter recycleViewAdapter;
    private RecyclerView recyclerView;
    public Realm realm;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            viewPager.setCurrentItem((viewPager.getCurrentItem() + 1) % bannerIds.length, true);
            handler.sendEmptyMessageDelayed(0, 5000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        initData();
        initEvent();
        handler.sendEmptyMessageDelayed(0, 5000);

        HttpclientUtil.setCookie(this);

        //生成待办事项中的权限字典
//        new Thread(new Runnable() {
//
//            @Override
//            public void run() {
//                MyConstants.flowUserHandlerDict.put("subitemproject", "分项工程开工申请表");
//                MyConstants.flowUserHandlerDict.put("projectstartapplayb", "工程开工申请表监表1-B");
//                MyConstants.flowUserHandlerDict.put("projectstartapplaya", "工程开工申请表监表1-A");
//                MyConstants.flowUserHandlerDict.put("acceptccie", "交工验收证书");
//                MyConstants.flowUserHandlerDict.put("changeproject", "变更立项");
//                MyConstants.flowUserHandlerDict.put("equipmentrepair", "维修申请");
//                MyConstants.flowUserHandlerDict.put("test", "测试");
//                MyConstants.flowUserHandlerDict.put("acceptance", "专项验收");
//                MyConstants.flowUserHandlerDict.put("suggestapproval", "建议审批");
//                MyConstants.flowUserHandlerDict.put("workrecordtable", "作业记录表");
//                MyConstants.flowUserHandlerDict.put("workplantable", "作业计划任务表");
//                MyConstants.flowUserHandlerDict.put("patrolrecordsheet", "巡查记录表");
//                MyConstants.flowUserHandlerDict.put("applypay", "合同支付");
//                MyConstants.flowUserHandlerDict.put("declarereplyc", "申报批复表监表3-02");
//                MyConstants.flowUserHandlerDict.put("declarereplyb", "申报批复表监表3-01");
//                MyConstants.flowUserHandlerDict.put("declarereplya", "申报批复表监表3");
//                MyConstants.flowUserHandlerDict.put("startworkream", "开工令");
//                MyConstants.flowUserHandlerDict.put("safetyreform", "安全检查记录表");
//                MyConstants.flowUserHandlerDict.put("safetyscience", "安全技术交底");
//                MyConstants.flowUserHandlerDict.put("safetyculture", "安全文明作业报备表");
//                MyConstants.flowUserHandlerDict.put("slopewalloftencheck", "边坡、挡墙检查");
//            }
//        }).start();


        // 生成相应的权限位置
        Intent intent = getIntent();
        List<UserRight> userRights = (List<UserRight>) intent.getSerializableExtra("data");

        for (UserRight right : userRights) {
//            if (right.itemName.equals("监测及应急保障")) {
//                MyConstants.userRights.put(right.itemName, false);
//            } else {
            MyConstants.userRights.put(right.itemName, right.hasRight);
//            }
        }

        recycleViewAdapter = new RecycleViewAdapter(this, MyConstants.MAIN_CONTENT, R.layout.item_recycle11);
        GridLayoutManager manager = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.addItemDecoration(new GridViewDivider(this));
        recyclerView.setAdapter(recycleViewAdapter);

        recycleViewAdapter.setOnItemClickListener(new RecycleViewAdapter.OnItemClickListener() {
            @Override
            public void setOnItemClickListener(View view, int position) {
                String itemName = MyConstants.MAIN_CONTENT[position];
                Class clazz = activityName.get(itemName);
                if (MyConstants.userRights.get(itemName) != null && MyConstants.userRights.get(itemName)) {
                    Intent intent = new Intent(MainActivity.this, clazz);
                    Bundle bundle = new Bundle();
                    bundle.putString("title", itemName);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "当前模块未开放", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void setOnItemLongClickListener(View view, int position) {

            }
        });


    }

    private void initData() {
        imageViewList = new ArrayList<>();
        for (int i = 0; i < bannerIds.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(bannerIds[i]);
            imageViewList.add(imageView);

            ImageView point = new ImageView(this);
            point.setBackgroundResource(R.drawable.banner_point);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.rightMargin = 10;
            point.setLayoutParams(lp);

            if (i == 0) {
                point.setEnabled(true);
            } else {
                point.setEnabled(false);
            }
            pointGroup.addView(point);
        }
//        RealmConfiguration config = new RealmConfiguration.Builder(this).name("chengtechmt").deleteRealmIfMigrationNeeded().build();
//        realm = Realm.getInstance(config);
        realm = RealmUtil.getInstance(this);
    }

    private void initEvent() {

        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return bannerIds.length;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                //因为viewpager的特殊性在与会创建三个view，所以如果只有三张图进行轮播的话，就会出现冲突
                //eg:加入目前显示在2位置，当往3位置移动时，就会创建4位置并且把1位置的view添加在4位置上，可是由于1位置的view还没有remove就去添加，这样就会导致报错。
//                if (position>bannerIds.length) {
//                    container.removeView(imageViewList.get((position % bannerIds.length) -1));
//                }
//                container.addView(imageViewList.get(position % bannerIds.length));
//                return imageViewList.get(position % bannerIds.length);
                container.addView(imageViewList.get(position));
                return imageViewList.get(position);
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
                object = null;
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
//                pointGroup.getChildAt(lastPointPos % bannerIds.length).setEnabled(false);
//                pointGroup.getChildAt(position % bannerIds.length).setEnabled(true);
                pointGroup.getChildAt(lastPointPos).setEnabled(false);
                pointGroup.getChildAt(position).setEnabled(true);
                lastPointPos = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {


                int currentItem = viewPager.getCurrentItem() % bannerIds.length;
//
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        lastX = (int) event.getX();
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int offset = (int) event.getX() - lastX;
                        if (leftmenu.isOpen) {
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                        } else if (offset > 0 && currentItem == 0) {
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                        } else {
                            v.getParent().requestDisallowInterceptTouchEvent(true);
                        }
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }
                return false;
            }
        });

        map.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2016/11/28
                Intent intent = new Intent(MainActivity.this, MapQueryActivity.class);
                intent.putExtra("isFirstLoad", true);
                startActivity(intent);
            }
        });
        chart_layout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MultiChartWebViewActivity.class);
                startActivity(intent);
//                String url = "ms/sys/homechart/maintenanceBusinessEchart.action";
//                ChartWebViewActivity.startAction(MainActivity.this, MyConstants.PRE_URL + url, "设施量统计");
            }
        });
        diseaseReg_layout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DiseaseRegistrationListActivity.class);
                startActivity(intent);
            }
        });

    }

    private void initView() {
//        listView = (ListView) findViewById(R.id.main_listview);
//        gridView = (GridView) findViewById(R.id.main_gridview);
        recyclerView = (RecyclerView) findViewById(R.id.recycleView);
        viewPager = (ViewPager) findViewById(R.id.id_viewPager);
        pointGroup = (LinearLayout) findViewById(R.id.id_pointGroup);
        map = (LinearLayout) findViewById(R.id.map);
        chart_layout = (LinearLayout) findViewById(R.id.chart);
        diseaseReg_layout = (LinearLayout) findViewById(R.id.diseaseReg);

//        schedule_iv = (ImageView) findViewById(R.id.main_schedule);
//        schedule_iv.setOnClickListener(this);

//        notice_iv = (ImageView) findViewById(R.id.main_notice);
//        notice_iv.setOnClickListener(this);

        data = new ArrayList<Map<String, Object>>();

        //注销
        logout_tv = (RelativeLayout) findViewById(R.id.logout_bt);
        logout_tv.setOnClickListener(this);


        //个人信息
        aboutMe_tv = (RelativeLayout) findViewById(R.id.leftmenu_aboutme);
        aboutMe_tv.setOnClickListener(this);
        //附件信息
        attachment_rl = (RelativeLayout) findViewById(R.id.attachment_bt);
        attachment_rl.setOnClickListener(this);

        //讯飞语音听写
//        findViewById(R.id.speech).setOnClickListener(this);

        username_tv = (TextView) findViewById(R.id.main_username);
        username_tv.setText(AppAccount.name);

        leftmenu = (SlidingMenu) findViewById(R.id.id_menu);

        activityName.put("基础数据管理", DbmActivity.class);
        activityName.put("规范标准管理", StandardActivity.class);
        activityName.put("养护业务管理", BusinessActivity.class);
        activityName.put("监测及应急保障", MonitorEmergencyActivity.class);
        activityName.put("综合查询", IntegrateQueryActivity.class);
        activityName.put("科学决策", ExpertDecisionActivity.class);

        //底部三个布局
        bottom1 = (RelativeLayout) findViewById(R.id.bottom1);
        bottom2 = (RelativeLayout) findViewById(R.id.bottom2);
        bottom3 = (RelativeLayout) findViewById(R.id.bottom3);
        bottom1.setOnClickListener(this);
        bottom2.setOnClickListener(this);
        bottom3.setOnClickListener(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bottom3:
                Intent intent = new Intent(this, NoticeActivity.class);
                startActivity(intent);
                break;

            case R.id.bottom1:
//                Intent intent2 = new Intent(this, ScheduleActivity.class);
//                startActivity(intent2);
                Toast.makeText(MainActivity.this, "该模块暂时未开放", Toast.LENGTH_SHORT).show();
                break;

            // 注销
            case R.id.logout_bt:
                showLogoutDialog();
                break;

            // 个人信息
            case R.id.leftmenu_aboutme:
                Intent intent3 = new Intent(this, AboutMeActivity.class);
                startActivity(intent3);
                break;
            //附件信息
            case R.id.attachment_bt:
                showAttachmentsDialog();
                break;

            //讯飞语音听写
//            case R.id.speech:
//                Intent intent4  = new Intent(this,SpeechActivity.class);
//                startActivity(intent4);
//                break;
            case R.id.bottom2:
                leftmenu.toggle();
                break;

        }
    }

    //弹出底部附件框
    private void showAttachmentsDialog() {
        //// TODO: 2016/11/1
        //记录！！！云笔记
        BottomSheetDialog sheetdialog = new BottomSheetDialog(this);

        //查询数据库
        realm = RealmUtil.getInstance(this);
        RealmResults<AttachmentInfo> downLoads = realm.where(AttachmentInfo.class).equalTo("type", AttachmentInfo.ATTACHMENT_TYPE_DOWNLOADED)
                .findAllSorted("time", Sort.DESCENDING);

        RealmResults<AttachmentInfo> upLoads = realm.where(AttachmentInfo.class).equalTo("type", AttachmentInfo.ATTACHMENT_TYPE_UPLOADED)
                .findAll();

        final ExpandableListView expandableListView = new ExpandableListView(this);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        expandableListView.setLayoutParams(lp);
        expandableListView.setGroupIndicator(null);
//        expandableListView.setDivider(getResources().getDrawable(R.drawable.line));
//        expandableListView.setChildDivider(getResources().getDrawable(R.drawable.line));
//        expandableListView.setDividerHeight(3);
        final String[] groupName = new String[]{"已下载", "已上传"};
        final Map<String, RealmResults<AttachmentInfo>> data = new HashMap<>();
        data.put(groupName[0], downLoads);
        data.put(groupName[1], upLoads);
        AttachementInfoAdapter adapter = new AttachementInfoAdapter(this, groupName, data);
        expandableListView.setAdapter(adapter);
        expandableListView.expandGroup(0);
        expandableListView.expandGroup(1);
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                AttachmentInfo info = data.get(groupName[groupPosition]).get(childPosition);
                CommonUtils.openFile(MainActivity.this, info.getFilePath());
                return true;
            }
        });
        sheetdialog.setContentView(expandableListView);
        leftmenu.closeMenu();
        expandableListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        lastY = (int) event.getY();
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int offset = (int) event.getY() - lastY;
                        if (offset > 0 && expandableListView.getChildAt(0).getTop() < 0) {
                            //下拉并且还没有到达顶部的时候，就让它不拦截
                            v.getParent().requestDisallowInterceptTouchEvent(true);
                        } else {
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                        }
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }
                return false;
            }
        });
        sheetdialog.show();
    }


    /**
     * 显示注销对话框
     *
     * @author liufuyingwang 2015-9-9 下午4:12:23
     */
    private void showLogoutDialog() {
        // 使用自定义对话框
        final MyDialog dialog = new MyDialog(this, R.style.myDialog);
        dialog.setOnPositiveClickListener("确定", new OnClickListener() {

            @Override
            public void onClick(View v) {
                new AsyncTask<Void, Void, Void>() {

                    @Override
                    protected Void doInBackground(Void... params) {
                        // 清除掉本地已经记录好的账户信息
                        SharedPreferences sp = getSharedPreferences("login",
                                MODE_PRIVATE);
                        if (sp != null) {
                            Editor edit = sp.edit();
                            edit.putString("username", null);
                            edit.putString("password", null);
                            edit.putString("name", null);
                            edit.putString("id", null);
                            edit.commit();
                        }
                        AppAccount.name = null;
                        AppAccount.userId = null;
                        // 清除cookie信息
                        CookieStore attribute = (CookieStore) HttpclientUtil
                                .getInstance(MainActivity.this)
                                .getHttpContext()
                                .getAttribute(ClientContext.COOKIE_STORE);
                        attribute.clear();
                        // 清除掉上个用户的所有fragment
                        // MyFragmentManager.clear();
                        return null;
                    }

                }.execute();

                // 跳转到登陆页面
                dialog.dismiss();
                Intent intent = new Intent(MainActivity.this,
                        LoginActivity.class);
                startActivity(intent);
                MainActivity.this.finish();

            }
        });
        dialog.setOnNagetiveClickListener("取消", new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
        dialog.show();
    }

    /**
     * 连续按两次退出
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            //先判断右侧菜单是否展开,若展开先合上
            if (leftmenu.isOpen) {
                leftmenu.toggle();
                return true;
            }
            if ((System.currentTimeMillis() - lastTime) > 2000) {
                Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
                lastTime = System.currentTimeMillis();
            } else {
                finish();
                CookieStore attribute = (CookieStore) HttpclientUtil
                        .getInstance(this).getHttpContext()
                        .getAttribute(ClientContext.COOKIE_STORE);
                attribute.clear();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PgyCrashManager.unregister();
        PgyUpdateManager.unregister();
    }
}
