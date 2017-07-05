package com.chengtech.chengtechmt.entity.attachment;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * 作者: LiuFuYingWang on 2016/11/1 8:57.
 */

public class AttachmentInfo extends RealmObject {


    @PrimaryKey
    private String id;
    public static final int ATTACHMENT_TYPE_DOWNLOADED = 0;  //已下载的文件，标识类型
    public static final int ATTACHMENT_TYPE_UPLOADED = 1;  //已上传的文件，标识类型
    private String fileName;
    private String filePath;
    private String time;
    private int type;
    private String size;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
