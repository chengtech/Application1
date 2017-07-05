package com.chengtech.etcdemo;

/**
 * 作者: LiuFuYingWang on 2017/4/13 14:15.
 * 配置文件
 */

public class ConfigBean {

    private String psamDirPurchase = "DF01";
    private int psamSlotPurchase = 1;

    public String getPsamDirPurchase() {
        return psamDirPurchase;
    }

    public void setPsamDirPurchase(String psamDirPurchase) {
        this.psamDirPurchase = psamDirPurchase;
    }

    public int getPsamSlotPurchase() {
        return psamSlotPurchase;
    }

    public void setPsamSlotPurchase(int psamSlotPurchase) {
        this.psamSlotPurchase = psamSlotPurchase;
    }
}
