package com.android.widgetplaceholder.utils;

/**
 * Created by wenjing.liu on 2020/10/10 .
 *
 * @author wenjing.liu
 */
public class Log {

    private static final boolean DEBUG = true;

    public static void logV(String tag, String content) {
        if (!DEBUG) {
            return;
        }
        android.util.Log.v(tag, content);
    }

    public static void logD(String tag, String content) {
        if (!DEBUG) {
            return;
        }
        android.util.Log.d(tag, content);
    }
}
