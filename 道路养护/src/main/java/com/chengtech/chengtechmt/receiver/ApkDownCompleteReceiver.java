package com.chengtech.chengtechmt.receiver;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.widget.Toast;

import com.chengtech.chengtechmt.entity.attachment.AttachmentInfo;
import com.chengtech.chengtechmt.util.CommonUtils;
import com.chengtech.chengtechmt.util.DateUtils;
import com.chengtech.chengtechmt.util.RealmUtil;

import java.net.URLDecoder;
import java.util.Date;

import io.realm.Realm;

/**
 * 作者: LiuFuYingWang on 2016/10/27 10:16.
 * apk文件下载完成后的提示接收者
 */

public class ApkDownCompleteReceiver extends BroadcastReceiver {

    public ApkDownCompleteReceiver(){
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        if (intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(id);
            Cursor cursor = manager.query(query);
            while (cursor.moveToNext()) {
                try {
                    int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
                    int status = cursor.getInt(columnIndex);
                    if (status==DownloadManager.STATUS_FAILED) {
                        Toast.makeText(context, "文件下载失败。", Toast.LENGTH_SHORT).show();
                        context.unregisterReceiver(this);
                        return;
                    }
                    //自动安装apk文件
                    String address = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                    address = URLDecoder.decode(address,"utf-8");
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.setDataAndType(Uri.parse("file://" + address),
                            "application/vnd.android.package-archive");
                    context.startActivity(i);
//                    android.os.Process.killProcess(android.os.Process.myPid());
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            cursor.close();
        } else if (intent.getAction().equals(DownloadManager.ACTION_NOTIFICATION_CLICKED)) {
            long id = intent.getLongExtra(DownloadManager.EXTRA_NOTIFICATION_CLICK_DOWNLOAD_IDS, -1);
//            manager.remove(id);
//            Toast.makeText(context, "文件取消下载", Toast.LENGTH_SHORT).show();
        }
        context.unregisterReceiver(this);
    }
}
