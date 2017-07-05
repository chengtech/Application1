package com.chengtech.etc;

import android.widget.TabHost;

/**
 * 作者: LiuFuYingWang on 2017/4/25 15:43.
 * 该实体类是交易过程中所产生的交易信息结果
 */

public class CardConsumeResult {

    private String psamTerminateID; //psam卡号

    private String iccPaySerial; //卡片交易序号

    private String psamPaySerial; //psam卡交易序号

    private String tac; //tac码

    public String getPsamTerminateID() {
        return psamTerminateID;
    }

    public void setPsamTerminateID(String psamTerminateID) {
        this.psamTerminateID = psamTerminateID;
    }

    public String getIccPaySerial() {
        return iccPaySerial;
    }

    public void setIccPaySerial(String iccPaySerial) {
        this.iccPaySerial = iccPaySerial;
    }

    public String getPsamPaySerial() {
        return psamPaySerial;
    }

    public void setPsamPaySerial(String psamPaySerial) {
        this.psamPaySerial = psamPaySerial;
    }

    public String getTac() {
        return tac;
    }

    public void setTac(String tac) {
        this.tac = tac;
    }
}
