package com.cm.strawberry.util;

/**
 * Created by zhouwei on 17-8-5.
 */

public class ListenerUtil {

    private static final long MIN_INTERNAL = 500;
    private static final long MIN_INTERNAL_Lock = 600;
    private static long lastCalledMills = -1L;

    public static boolean isMultipleClick() {
        long internal = System.currentTimeMillis() - lastCalledMills;
        lastCalledMills = System.currentTimeMillis();
        return Math.abs(internal) < MIN_INTERNAL;
    }

    public static boolean isMultipleLockClick() {
        long internal = System.currentTimeMillis() - lastCalledMills;
        lastCalledMills = System.currentTimeMillis();
        return Math.abs(internal) < MIN_INTERNAL_Lock;
    }
}
