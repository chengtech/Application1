package com.chengtech.chengtechmt.entity.business;

import java.io.Serializable;

/**
 * 作者: LiuFuYingWang on 2017/11/29 15:45.
 * 病害登记中的陪同人员
 */

public class FellowMen implements Serializable{
    public String name;
    public boolean isChecked = false;

    public FellowMen(String name, boolean isChecked) {
        this.name = name;
        this.isChecked = isChecked;
    }

}
