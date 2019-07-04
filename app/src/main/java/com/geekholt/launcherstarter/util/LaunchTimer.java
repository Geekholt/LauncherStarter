package com.geekholt.launcherstarter.util;


/**
 * @Author：wuhaoteng
 * @Date:2019/7/4
 * @Desc：
 */
public class LaunchTimer {

    private static long sTime;

    public static void startRecord() {
        sTime = System.currentTimeMillis();
    }

    public static void endRecord() {
        endRecord("");
    }

    public static void endRecord(String msg) {
        long cost = System.currentTimeMillis() - sTime;
        LogUtils.i(msg + " cost " + cost + "ms");
    }

}
