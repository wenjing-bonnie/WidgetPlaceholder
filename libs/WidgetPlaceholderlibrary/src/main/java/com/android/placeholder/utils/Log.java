package com.android.placeholder.utils;

/**
 * Created by wenjing.liu on 2020/10/10 .
 *
 * @author wenjing.liu
 */
public class Log {

    private static final boolean DEBUG = true;


    public static void v(String content) {
        if (!DEBUG) {
            return;
        }
        android.util.Log.v(generateTag(), content);
    }

    public static void d(String content) {
        if (!DEBUG) {
            return;
        }
        android.util.Log.d(generateTag(), content);
    }

    public static void w(String content) {
        if (!DEBUG) {
            return;
        }
        android.util.Log.w(generateTag(), content);
    }


    public static void e(String content) {
        if (!DEBUG) {
            return;
        }
        android.util.Log.e(generateTag(), content);
    }


    private static String generateTag() {
        StackTraceElement caller = Thread.currentThread().getStackTrace()[4];
        String tag = "%s.%s(L:%d)";
        String callerClazzName = caller.getClassName();
        callerClazzName = callerClazzName.substring(callerClazzName.lastIndexOf(".") + 1);
        tag = String.format(tag, new Object[]{callerClazzName, caller.getMethodName(), Integer.valueOf(caller.getLineNumber())});
        return tag;
    }

    private static StackTraceElement getCallerStackTraceElement() {
        return Thread.currentThread().getStackTrace()[4];
    }
}
