package com.chengtech.chengtechmt.activity.dbm;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.chengtech.chengtechmt.BaseActivity;
import com.chengtech.chengtechmt.R;
import com.chengtech.chengtechmt.activity.IView;
import com.chengtech.chengtechmt.activity.MapQueryActivity;
import com.chengtech.chengtechmt.adapter.DetailAdapter2;
import com.chengtech.chengtechmt.divider.RecycleViewDivider;
import com.chengtech.chengtechmt.entity.Attachment;
import com.chengtech.chengtechmt.entity.MapEntity;
import com.chengtech.chengtechmt.entity.ResultBean;
import com.chengtech.chengtechmt.entity.attachment.AttachmentInfo;
import com.chengtech.chengtechmt.presenter.DetailPresenter;
import com.chengtech.chengtechmt.util.CommonUtils;
import com.chengtech.chengtechmt.util.HttpclientUtil;
import com.chengtech.chengtechmt.util.MyConstants;
import com.chengtech.chengtechmt.util.RealmUtil;
import com.chengtech.chengtechmt.util.UserUtils;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.Realm;

public class DetailActivity extends BaseActivity implements IView<Object>, View.OnClickListener {

    public RecyclerView recyclerView;
    public List<String> originalData;
    public List<String> savedData;
    public DetailAdapter2 adapter;
    public ArrayList<String> subtitle;
    public ArrayList<String> subtitleProperty;
    public HashMap<Integer, String> editData;
    public DetailPresenter detailPresenter;
    public String className;
    public String id;
    public String url = "ms/common/updateClassValue.action";
    public String sessionId;
    public String[] photoUrl;
    public ViewPager viewPager;
    public LinearLayout indicator;
    public List<ImageView> imageViewList;
    public int lastPointPos;
    //    public FabOptions fabOptions;
    public FloatingActionButton fab_loaction, fab_gps, fab_attachment;
    public MapEntity mapEntity;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    showViewPager();
                    break;
                case 1:
                    AMapLocation aMapLocation = (AMapLocation) msg.obj;
                    if (subtitleProperty.contains("longitude") && subtitleProperty.contains("latitude")) {
                        int x = subtitleProperty.indexOf("longitude");
                        int y = subtitleProperty.indexOf("latitude");
                        originalData.remove(x);
                        originalData.add(x, aMapLocation.getLongitude()+"");
                        originalData.remove(y);
                        originalData.add(y, aMapLocation.getLatitude()+"");
                        //将修改过的数据的位置和内容保存在一个hashmap中，
                        editData.put(x, aMapLocation.getLongitude()+"");
                        editData.put(y, aMapLocation.getLatitude()+"");
                        adapter.notifyDataSetChanged();
                        updateObject();
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addContentView(R.layout.activity_detail);

        initView();


        setNavigationIcon(true);
        hidePagerNavigation(true);
        setAppBarLayoutScroll(false);
        initEvent();

        Intent intent = getIntent();
        originalData = (List<String>) intent.getSerializableExtra("content");
        subtitle = (ArrayList<String>) intent.getSerializableExtra("subtitle");
        className = intent.getStringExtra("className");
        id = intent.getStringExtra("id");
        mapEntity = (MapEntity) intent.getSerializableExtra("map");
        sessionId = intent.getStringExtra("sessionId");
        subtitleProperty = (ArrayList<String>) intent.getSerializableExtra("subtitleProperty");
        photoUrl = intent.getStringArrayExtra("photo");
        if (photoUrl != null && photoUrl.length > 0) {
            viewPager.setVisibility(View.VISIBLE);
            indicator.setVisibility(View.VISIBLE);
            String zoomImage = "";
            for (int i=0;i<photoUrl.length;i++) {
                if (i== photoUrl.length-1) {
                    zoomImage += photoUrl[i];
                }else {
                    zoomImage += photoUrl[i]+",,,";
                }
            }
            HttpclientUtil.getData(this,MyConstants.PRE_URL+"mt/common/zoomImage.action?imagePath="+zoomImage,handler,0);
        }
        adapter = new DetailAdapter2(this, subtitle, originalData);
//        CommonUtils.setListViewHeight(listView,adapter,subtitle.length);
//        adapter.setOnHeightChangeListener(new DetailAdapter.OnListViewHightChangeListener() {
//            @Override
//            public void onHeightChangeListener(int addHeight) {
////                ViewGroup.LayoutParams lp = listView.getLayoutParams();
////                lp.height = listView.getHeight()+addHeight;
////                listView.setLayoutParams(lp);
//                CommonUtils.setListViewHeight(listView,adapter,subtitle.length);
//            }
//        });
//        listView.setAdapter(adapter);
        adapter.setLongClickListener(new DetailAdapter2.OnItemLongClickListener() {
            @Override
            public void onLongClick(View view, int position) {
                String msg = ((TextView) view).getText().toString().toString();
                createDialog(msg, position);
            }
        });
        recyclerView.setAdapter(adapter);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
//        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
//            @Override
//            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
//                super.getItemOffsets(outRect, view, parent, state);
//                if (parent.getChildAdapterPosition(view) != 0)
//                    outRect.top = 10;
//            }
//        });
        recyclerView.addItemDecoration(new RecycleViewDivider(this,LinearLayout.VERTICAL));
        recyclerView.setLayoutManager(manager);
        recyclerView.smoothScrollBy(0, 0);
        initData();


    }

    private void initData() {

    }

    //如果有照片在详细列表，就要显示照片
    private void showViewPager() {
        imageViewList = new ArrayList<>();
        for (int i = 0; i < photoUrl.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            final String url = MyConstants.PRE_URL + photoUrl[i];
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(DetailActivity.this, OnePictureDisplayActivity.class);
                    intent.putExtra("url", url);
                    startActivity(intent);

                }
            });
            imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            CommonUtils.loadNetWorkPicture(this, url.replace("imageUpload","zoomImageUpload"), imageView);
            imageViewList.add(imageView);

            TextView tv = new TextView(this);
            tv.setTextSize(12);
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(50, 50);
            tv.setGravity(Gravity.CENTER);
            tv.setText(String.valueOf(i + 1));
            tv.setLayoutParams(layoutParams);
            tv.setBackground(getResources().getDrawable(R.drawable.selector_indicator_text));
            if (i == 0) {
                tv.setEnabled(true);
            } else {
                tv.setEnabled(false);
            }

            indicator.addView(tv);
        }

        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return photoUrl.length;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
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
                indicator.getChildAt(lastPointPos).setEnabled(false);
                indicator.getChildAt(position).setEnabled(true);
                lastPointPos = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int action = item.getItemId();
        if (action == R.id.action_update) {
            updateObject();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initEvent() {

//        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//
//                String msg = (String) parent.getItemAtPosition(position);
//                createDialog(msg, position);
//
//                return true;
//            }
//        });

//        fabOptions.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                switch (v.getId()) {
//                    case R.id.faboptions_attachment :
//                        showSheetDialog();
//                        break;
//                    case R.id.faboptions_location:
//
//                        break;
//                }
//            }
//        });

    }

    //创建编辑对话框
    private void createDialog(String msg, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(subtitle.get(position) + "(编辑)");
        final EditText et = new EditText(this);
        et.setText(msg);
        et.setSelection(msg.length());
        builder.setView(et).setPositiveButton("保存", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String editMsg = et.getText().toString();

                originalData.remove(position);
                originalData.add(position, editMsg);
                //将修改过的数据的位置和内容保存在一个hashmap中，
                editData.put(position, editMsg);
                adapter.notifyDataSetChanged();
            }
        })
                .setNegativeButton("取消", null)
                .show();

    }

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.detail_listview);
        editData = new HashMap<>();
        detailPresenter = new DetailPresenter(this);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        indicator = (LinearLayout) findViewById(R.id.id_pointGroup);
//        fabOptions = (FabOptions) findViewById(R.id.faboptions);
        fab_attachment = (FloatingActionButton) findViewById(R.id.fab_attachment);
        fab_loaction = (FloatingActionButton) findViewById(R.id.fab_location);
        fab_gps = (FloatingActionButton) findViewById(R.id.fab_gps);
        fab_attachment.setOnClickListener(this);
        fab_loaction.setOnClickListener(this);
        fab_gps.setOnClickListener(this);
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
    public void loadDataSuccess(Object resultBean) {
        ResultBean result = (ResultBean) resultBean;
        editData.clear();
        adapter.notifyDataSetChanged();
        Toast.makeText(this, result.msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void loadDataSuccess(Object resultBean, int type) {

    }

    @Override
    public void loadDataSuccess(Object resultBean, String className) {
        switch (className) {
            case "Attachment":
                final Realm realm = RealmUtil.getInstance(this);
                final List<Attachment> attachments = (List<Attachment>) resultBean;
                if (attachments.size() == 0) {
                    Toast.makeText(this, "无附件", Toast.LENGTH_SHORT).show();
                    break;
                }
                final BottomSheetDialog dialog = new BottomSheetDialog(this);
                ListView listView = new ListView(this);
                String[] fileNames = new String[attachments.size()];
                for (int i = 0; i < attachments.size(); i++) {
                    fileNames[i] = attachments.get(i).fileName;
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, fileNames);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String ids = attachments.get(position).id;
                        String fileName = attachments.get(position).fileName;
                        String filePath = MyConstants.PRE_URL + attachments.get(position).filePath;
                        AttachmentInfo attachmentInfo = realm.where(AttachmentInfo.class).equalTo("id", ids).findFirst();
                        if (fileName.contains(".jpg") || fileName.contains(".png") || fileName.contains(".jpeg")) {
                            Intent intent = new Intent(DetailActivity.this, OnePictureDisplayActivity.class);
                            intent.putExtra("url", filePath);
                            startActivity(intent);
                        } else if (attachmentInfo==null){
                            //使用自带的下载器下载文件
                            CommonUtils.downFile(DetailActivity.this, filePath, fileName,ids);
                            dialog.dismiss();
                        }else{
                            CommonUtils.openFile(DetailActivity.this, attachmentInfo.getFilePath());
                            dialog.dismiss();
                        }
                        realm.close();

                    }
                });
                dialog.setContentView(listView);
                dialog.show();
                break;
        }
    }

    @Override
    public void loadDataFailure() {
        Toast.makeText(this, "连接服务器出错", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void hasError() {
        UserUtils.reLogin(DetailActivity.this, loadDialog);
    }

    //编辑后保存
    public void updateObject() {
        //判断编辑后数据editData集合中是否有对象
        if (editData.size() > 0) {
            JSONObject jsonObject = new JSONObject();
            try {
                //先把classname和id放进去
                jsonObject.put("className", className);
                jsonObject.put("id", id);
                for (Map.Entry entry : editData.entrySet()) {

                    jsonObject.put(subtitleProperty.get(((int) entry.getKey())), entry.getValue());


                }
            } catch (Exception e) {

            }
            detailPresenter.getData(DetailActivity.this, MyConstants.PRE_URL + url, jsonObject.toString());
        } else {
            Toast.makeText(DetailActivity.this, "没有做任何修改", Toast.LENGTH_SHORT).show();
        }
    }

    //弹出底部框
    public void showSheetDialog() {
        if (!TextUtils.isEmpty(sessionId)) {
            detailPresenter.getData(this, sessionId);
        } else {
            Toast.makeText(this, "无附件", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_attachment:
                showSheetDialog();
                break;
            case R.id.fab_location:
                Intent intent = new Intent(this, MapQueryActivity.class);
                intent.putExtra("map", mapEntity);
                startActivity(intent);
                break;
            case R.id.fab_gps:
                //判断是否打开gps
                CommonUtils.getGpsLocation(this, handler);
                break;
        }
        FloatingActionsMenu parent = (FloatingActionsMenu) v.getParent();
        parent.toggle();
    }
}
