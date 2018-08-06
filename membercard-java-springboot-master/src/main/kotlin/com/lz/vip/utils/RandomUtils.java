/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2016 All Rights Reserved.
 */
package com.lz.vip.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 */
public class RandomUtils {

    /**
     * 时间拼接随机数
     * @return
     */
    public static String getRandomRequestId() {
        String dateStr = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String randomStr = String.valueOf((long) (Math.random() * 1000000L));
        return dateStr + "00" + randomStr;
    }

    /**
     * 返回指定位数的随机数字
     * @param length
     * @return
     */
    public static String getRandomIdStr(int length) {
        String m = "1";
        for (int i = 0; i < length; i++) {
            m += "0";
        }

        String randomStr = String.valueOf((long) (Math.random() * 1L * Integer.valueOf(m)));
        return randomStr;
    }
}