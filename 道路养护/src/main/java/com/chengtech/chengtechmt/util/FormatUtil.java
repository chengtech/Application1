package com.chengtech.chengtechmt.util;

import java.text.DecimalFormat;

/**
 * 作者: LiuFuYingWang on 2016/12/19 9:30.
 * 格式化工具类
 */

public class FormatUtil {

    /**
     * 取消小数某位为0，例如12的数值会转转换成12.0，解决该问题。
     * @param num
     * @return
     */
    public static String double2String(Double num){
        DecimalFormat decimalFormat = new DecimalFormat("###################.###########");
        return decimalFormat.format(num);
    }

}
