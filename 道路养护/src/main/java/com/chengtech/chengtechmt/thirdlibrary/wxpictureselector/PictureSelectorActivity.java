package com.chengtech.chengtechmt.thirdlibrary.wxpictureselector;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.chengtech.chengtechmt.R;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class PictureSelectorActivity extends Activity {
    public static final int SELECTED_RESULT = 0x01;
    private GridView gridView;
    private List<String> picturePaths;
    private MyPictureAdapter adapter;

    private TextView dir_tv,count_tv;
    private RelativeLayout bottom_layout;
    private File mCurrentDir;
    private int mMaxCount;
    private List<FloderBean> floders = new ArrayList<>();
    private ProgressDialog progressDialog;
    private ListDirPopupWindow popupWindow;
    public  Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
//            progressDialog.dismiss();
            data2view();

            initPopupWindow();
        }
    };

    private void initPopupWindow() {
        popupWindow = new ListDirPopupWindow(this,floders);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {

                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1.0f;
                getWindow().setAttributes(lp);
            }
        });

        popupWindow.setOnSelectDirListener(new ListDirPopupWindow.OnSelectDirListener() {
            @Override
            public void onSelected(FloderBean floderBean) {

                mCurrentDir = new File(floderBean.getDirPath());
                picturePaths = Arrays.asList(mCurrentDir.list(new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String filename) {
                        if (filename.contains(".jpeg") || filename.contains(".jpg") || filename.contains(".png"))
                            return true;
                        return false;
                    }
                }));
                adapter = new MyPictureAdapter(PictureSelectorActivity.this, picturePaths, mCurrentDir.getAbsolutePath());

                gridView.setAdapter(adapter);
                dir_tv.setText(floderBean.getName());
                count_tv.setText(floderBean.getCount() + "");
                popupWindow.dismiss();
            }
        });


    }

    private void data2view() {
        if (mCurrentDir==null) {
            Toast.makeText(PictureSelectorActivity.this, "未扫描到任何图片", Toast.LENGTH_SHORT).show();
            return;
        }

        picturePaths= Arrays.asList(mCurrentDir.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                if (filename.contains(".jpeg") ||filename.contains(".jpg")||filename.contains(".png"))
                    return true;
                return false;
            }
        }));

        adapter = new MyPictureAdapter(this,picturePaths,mCurrentDir.getAbsolutePath());

        gridView.setAdapter(adapter);

        count_tv.setText(mMaxCount + "");
        dir_tv.setText(mCurrentDir.getName());

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_selector);

        initView();
        initDatas();
        initEvent();
    }

    private void initEvent() {
        bottom_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 0.3f;
                getWindow().setAttributes(lp);
                popupWindow.showAsDropDown(bottom_layout, 0, 0);
            }
        });
    }

    private void initDatas() {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Snackbar.make(null,"没有可用的存储卡", Snackbar.LENGTH_SHORT).show();
            return;
        }
//        progressDialog = ProgressDialog.show(this,null,"正在加载....");
        new Thread(new Runnable() {
            @Override
            public void run() {
                Uri imgUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver cr = PictureSelectorActivity.this.getContentResolver();
                Cursor cursor = cr.query(imgUri, null, MediaStore.Images.Media.MIME_TYPE + " = ? or " + MediaStore.Images.Media.MIME_TYPE
                        + " = ? or "+MediaStore.Images.Media.MIME_TYPE+" = ?", new String[]{"image/jpg","image/jpeg", "image/png"}, MediaStore.Images.Media.DATE_MODIFIED);

                Set<String> dirpaths = new HashSet<String>();
                while (cursor.moveToNext()) {
                    String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    File parentFile = new File(path).getParentFile();
                    if (parentFile==null) {
                        continue;
                    }

                    String dirPath = parentFile.getAbsolutePath();
                    if (dirpaths.contains(dirPath)) {
                        continue;
                    }else {
                        dirpaths.add(dirPath);
                        FloderBean floderBean = new FloderBean();
                        floderBean.setDirPath(dirPath);
                        floderBean.setFirstImgPath(path);

                        int count = parentFile.list(new FilenameFilter() {
                            @Override
                            public boolean accept(File dir, String filename) {
                                if (filename.contains(".jpg") || filename.contains(".jpeg") || filename.contains(".png"))
                                    return true;
                                return false;
                            }
                        }).length;

                        floderBean.setCount(count);

                        floders.add(floderBean);
                        if (count>mMaxCount) {
                            mMaxCount = count;
                            mCurrentDir = parentFile;
                        }


                    }


                }

                cursor.close();

                handler.sendEmptyMessage(0x110);


            }
        }).start();


    }

    private void initView() {
        gridView = (GridView) findViewById(R.id.id_gridView);
        dir_tv = (TextView) findViewById(R.id.id_dir_name);
        count_tv = (TextView) findViewById(R.id.id_picture_count);
        bottom_layout = (RelativeLayout) findViewById(R.id.id_bottom_relative);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        ArrayList<String> picPath = new ArrayList<>();
        Iterator<String> iterator = MyPictureAdapter.checkedState.iterator();
        while (iterator.hasNext()) {
            picPath.add(iterator.next());
        }
        intent.putStringArrayListExtra("PicturePath", picPath);
        setResult(SELECTED_RESULT,intent);
        super.onBackPressed();
    }
}
