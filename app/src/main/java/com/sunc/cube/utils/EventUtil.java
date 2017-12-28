package com.sunc.cube.utils;

/**
 * Created by Administrator on 2017/9/25.
 */

public class EventUtil {
    protected static long lastClickTime;

    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if(0 < timeD && timeD < 800) {
            return true;
        }
        lastClickTime = time;
        return false;
    }
}
