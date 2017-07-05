package com.chengtech.chengtechmt.thirdlibrary.wxpictureselector;

/**
 * Created by Administrator on 2016/3/15.
 */
public class FloderBean {

    private String name;
    private int count;
    private String dirPath;
    private String firstImgPath;

    public String getName() {
        return name;
    }


    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getDirPath() {
        return dirPath;
    }

    public void setDirPath(String dirPath) {
        this.dirPath = dirPath;
        int lastIndexOf = this.dirPath.lastIndexOf("/");
        this.name = dirPath.substring(lastIndexOf+1);
    }

    public String getFirstImgPath() {
        return firstImgPath;
    }

    public void setFirstImgPath(String firstImgPath) {
        this.firstImgPath = firstImgPath;
    }
}
