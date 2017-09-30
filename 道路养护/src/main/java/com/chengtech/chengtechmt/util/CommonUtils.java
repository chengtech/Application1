package com.chengtech.chengtechmt.util;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.chengtech.chengtechmt.R;
import com.chengtech.chengtechmt.entity.Attachment;
import com.chengtech.chengtechmt.receiver.ApkDownCompleteReceiver;
import com.chengtech.chengtechmt.receiver.DownCompleteReceiver;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.apache.commons.codec.Encoder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.net.ssl.HandshakeCompletedListener;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * 作者: LiuFuYingWang on 2016/10/27 9:49.
 * 常用工具类
 */

public class CommonUtils {

    public static final int GB = 1024 * 1024 * 1024;
    public static final int MB = 1024 * 1024;
    public static final int KB = 1024;
    public static final int TAKE_PHOTO = 0x111;
    public static final String CAMERA_DEFAULT_NAME = "output_image.jpg";

    //使用系统downloadmanager服务下载文件
    public static void downFile(Context context, String downPath, String fileName, String id) {
        //注册接收者
        DownCompleteReceiver receiver = new DownCompleteReceiver(id);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        intentFilter.addAction(DownloadManager.ACTION_NOTIFICATION_CLICKED);
        context.registerReceiver(receiver, intentFilter);
        //获得manager

        DownloadManager manager = (DownloadManager) context.getSystemService(context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(downPath));
//        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE); //设置下载的网络，默认任何网络都可以
//        request.setAllowedOverRoaming(false); //是否允许漫游状态下载
        request.setVisibleInDownloadsUi(true); //显示通知栏
        request.setShowRunningNotification(true);
//        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);//显示notification信息
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);//显示notification信息
        request.setTitle(fileName);  //设置Notification的title信息
        request.setDescription(fileName + "正在下载"); //Notification的内容描述
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
        manager.enqueue(request);
    }

    //使用系统downloadmanager服务下载文件
    public static void downApk(Context context, String downPath, String fileName) {
        //注册接收者
        ApkDownCompleteReceiver receiver = new ApkDownCompleteReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        intentFilter.addAction(DownloadManager.ACTION_NOTIFICATION_CLICKED);
        context.registerReceiver(receiver, intentFilter);
        //获得manager
        DownloadManager manager = (DownloadManager) context.getSystemService(context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(downPath));
        request.setVisibleInDownloadsUi(true); //显示通知栏
        request.setShowRunningNotification(true);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);//显示notification信息
//        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);//显示notification信息
        request.setTitle(fileName);  //设置Notification的title信息
        request.setDescription(fileName + "正在下载"); //Notification的内容描述
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
        manager.enqueue(request);
    }

    /**
     * 将list集合转换成一个字符串二维数组
     *
     * @param data  集合类
     * @param title 顶部的标题栏
     */
    public static String[][] translateToMartrix(List<String> data, List<String> title) {
        String[][] data2;
        List<String> content = new ArrayList<>();
        if (title != null && title.size() > 0) {
            content.addAll(title);
        }
        content.addAll(data);
        int column = title.size();
        int row = content.size() / column;
        data2 = new String[row][column];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                data2[i][j] = content.get(i * column + j);
            }
        }

        return data2;
    }

    /**
     * 下载网络图片，使用picasso
     */
    public static void loadNetWorkPicture(Context context, String url, ImageView img, int... targetWidth) {
        Transformation transformation = null;
        if (targetWidth != null && targetWidth.length > 0) {
            final int targetWidth1 = targetWidth[0];
            transformation = new Transformation() {

                @Override
                public Bitmap transform(Bitmap source) {
                    if (source.getWidth() == 0) {
                        return source;
                    }

                    //如果图片小于设置的宽度，则返回原图
                    if (source.getWidth() < targetWidth1) {
                        return source;
                    } else {
                        //如果图片大小大于等于设置的宽度，则按照设置的宽度比例来缩放
                        double aspectRatio = (double) source.getHeight() / (double) source.getWidth();
                        int targetHeight = (int) (targetWidth1 * aspectRatio);
                        if (targetHeight != 0 && targetWidth1 != 0) {
                            Bitmap result = Bitmap.createScaledBitmap(source, targetWidth1, targetHeight, false);
                            if (result != source) {
                                // Same bitmap is returned if sizes are the same
                                source.recycle();
                            }
                            return result;
                        } else {
                            return source;
                        }
                    }

                }

                @Override
                public String key() {
                    return "transformation" + " desiredWidth";
                }
            };
        }
        Picasso.with(context).load(url).placeholder(R.mipmap.placeholder)
                .error(R.mipmap.placeholder2)
//                .transform(new RoundeCornerTransformation())
//                .resize(img.getWidth(),img.getHeight())
//                .transform(targetWidth.length>0?transformation:null)
                .into(img);

    }

    /**
     * 根据文件路径，来打开文件
     */
    public static void openFile(Context context, String filePath) {
        try {
            //判断文件是否存在,将中文的文件名进行编码，否侧会因为特殊字符而报错
            String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
            fileName = URLEncoder.encode(fileName);
            filePath = filePath.substring(0, filePath.lastIndexOf("/") + 1) + fileName;
            File file = new File(URI.create(filePath));
            if (file.exists()) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                //获取文件file的MIME类型
                String type = getMIMEType(file.getName());
                Uri uri = Uri.parse(filePath);
                intent.setDataAndType(uri, type);
                context.startActivity(intent);
            } else {
                Toast.makeText(context, "文件不存在", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(context, "文件打开错误", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 根据文件名的后缀名获得mimetype
     *
     * @param fileName “123.txt”
     */
    public static String getMIMEType(String fileName) {
        String type = "*/*";
        //获取文件的点的位置
        int indexOfDot = fileName.lastIndexOf(".");
        if (indexOfDot < 0)
            return type;

        //获取文件的后缀名
        String end = fileName.substring(indexOfDot + 1, fileName.length()).toLowerCase();
        if (end == "")
            return type;

        //在MIME和文件类型的匹配表中找到对应的MIME类型。
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        if (mimeTypeMap.hasExtension(end)) {
            type = mimeTypeMap.getMimeTypeFromExtension(end);
        }
        return type;
    }

    /**
     * 将字节数转换成kb，mb，gb
     */
    public static String ByteConversionGBMBKB(int size) {
        DecimalFormat format = new DecimalFormat("0.0");
        float result;
        if (size / GB >= 1) {
            result = size / (float) GB;
            return format.format(result) + " GB";
        } else if (size / MB >= 1) {
            result = size / (float) MB;
            return format.format(result) + " MB";
        } else if (size / KB >= 1) {
            result = size / (float) KB;
            return format.format(result) + " KB";
        }
        return format.format(size) + " Byte";
    }


    /**
     * 动态设置listView的高度，该方法要用在setadapter之前
     * count 总条目
     */
    public static void setListViewHeight(ListView listView, BaseAdapter adapter,
                                         int count) {
        int totalHeight = 0;
        for (int i = 0; i < count; i++) {
            View listItem = adapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * count) + 50;
        listView.setLayoutParams(params);
    }

    /**
     * 动态设置gridView的高度,该方法要用在setadapter之前
     * count 总条目
     */
    public static void setGridViewHeight(GridView gridview, BaseAdapter adapter, int count) {
        int totalHeight = 0;
        for (int i = 0; i < count; i++) {
            if (i % 3 == 0) {
                View listItem = adapter.getView(i, null, gridview);
                listItem.measure(0, 0);
                totalHeight += listItem.getMeasuredHeight();
            }
        }
        ViewGroup.LayoutParams params = gridview.getLayoutParams();
        params.height = totalHeight + (gridview.getHorizontalSpacing() * (int) Math.ceil(count / 3.0)) + 50;
        gridview.setLayoutParams(params);
    }


    /**
     * 将dp值转化成px
     */
    public static int dp2px(Context context, int dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5f);
    }

    /**
     * 将px转换成dp
     */
    public static int px2dp(Context context, int px) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (px / density + 0.5f);
    }

    /**
     * 判断gps定位服务是否打开,如果不打开则调往设置页面，如果打开了则获取经纬度
     */
    public static void getGpsLocation(final Context context, final Handler handler) {
        LocationManager manger = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean providerEnabled = manger.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (providerEnabled) {
            final SweetAlertDialog dialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
            dialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            dialog.getProgressHelper().resetCount();
            dialog.setTitleText("正在获取经纬度....");
            dialog.setCancelable(true);
            dialog.show();
            //使用高德定位
            AMapLocationClientOption option = new AMapLocationClientOption();
            option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            option.setOnceLocation(true);
            option.setOnceLocationLatest(true);
            option.setHttpTimeOut(15000);

            final AMapLocationClient client = new AMapLocationClient(context);
            client.setLocationOption(option);
            client.setLocationListener(new AMapLocationListener() {
                @Override
                public void onLocationChanged(final AMapLocation aMapLocation) {
                    if (aMapLocation.getErrorCode() == 0) {
                        dialog.setTitleText("获取成功");
                        dialog.setContentText("经度：" + aMapLocation.getLongitude()
                                + "\n纬度：" + aMapLocation.getLatitude()
                                + "\n城市信息：" + aMapLocation.getCity()
                                + "\n城区信息：" + aMapLocation.getDistrict()
                                + "\n街道信息：" + aMapLocation.getStreet()
                                + "\nAOI信息：" + aMapLocation.getAoiName()
                                + "\n定位时间：" + DateUtils.convertDate3(new Date(aMapLocation.getTime()))
                        )
                                .setCancelText("取消")
                                .setCancelClickListener(null)
                                .setConfirmText("更新并保存。")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        Message msg = handler.obtainMessage();
                                        msg.obj = aMapLocation;
                                        msg.what = 1;
                                        handler.sendMessage(msg);
                                        sweetAlertDialog.dismiss();

                                    }
                                })
                                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                        ;

                    } else {
                        dialog.setTitleText("获取失败")
                                .setConfirmText("OK")
                                .setConfirmClickListener(null)
                                .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                    }
                    client.onDestroy();
                }
            });
            client.startLocation();


        } else {
            SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE);
            sweetAlertDialog.setTitleText("GPS定位服务未打开")
                    .setContentText("是否前往设置中打开GPS？")
                    .setConfirmText("确定,前往。")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            Intent intent2 = new Intent(
                                    Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            ((Activity) context).startActivityForResult(intent2, 0);
                            sweetAlertDialog.dismiss();
                        }
                    })
                    .setCanceledOnTouchOutside(true);
            sweetAlertDialog.show();
        }
    }

    public static double convertStake(String stake) {
        String result = null;
        try {
            result = stake.toUpperCase().replace("+", ".");
            result = result.replace("K", "");
            if (result.indexOf(".") == 1) {
                return Double.parseDouble(result);
            }
            while (result.charAt(0) == '0') {
                result = result.substring(1, result.length());
                if (result.indexOf(".") == 1) {
                    break;
                }
            }
            return Double.parseDouble(result);
        } catch (Exception e) {
            return 0.0;
        }
    }

    public static boolean isNetFileAvailable(String path) {
        try {
            URL url = new URL(path);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            if (urlConnection.getResponseCode() == 200) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取屏幕的宽度
     *
     * @param context
     * @return
     */
    public static int getWindowsWidth(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        return width;
    }

    public static String camera(Context context) {
        Uri imageUri = null;
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File mfile = new File(context.getExternalCacheDir(), CAMERA_DEFAULT_NAME);
        try {
            if (mfile.exists()) {
                mfile.delete();
            }
            mfile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (Build.VERSION.SDK_INT >= 24) {
            imageUri = FileProvider.getUriForFile(context, "com.chengtechmt.fileprovider", mfile);
        } else {
            imageUri = Uri.fromFile(mfile);
        }
        Activity activity = (Activity) context;
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        activity.startActivityForResult(intent, TAKE_PHOTO);
        return context.getExternalCacheDir() + "/" + CommonUtils.CAMERA_DEFAULT_NAME;
    }

}
