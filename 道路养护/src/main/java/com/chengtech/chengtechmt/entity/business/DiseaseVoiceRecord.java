package com.chengtech.chengtechmt.entity.business;

import java.io.Serializable;

/**
 * 作者: LiuFuYingWang on 2017/9/13 16:31.
 * 录音实体类
 */

public class DiseaseVoiceRecord implements Serializable{
    public String id;
    public String recordPath; //语音保存路径
    public String recordLength; //语音长度
    public String recordContent = "";  //语音翻译的文字
}
