package com.hxx.xlibrary.util;

import android.util.Log;

import com.hxx.xlibrary.XConstant;


/**
 * Created by Android on 2017/7/10 .
 */

public class L {
    /**
     * 是否打开Log日志输出
     */
    private static boolean DEBUG = XConstant.DEBUG;
    /**
     * 默认日志Tag
     */
    private static String TAG = L.class.getSimpleName();
    /*
    * 默认输出DEBUG级别
    * */
    private final static int defaultPriority = Log.DEBUG;

    /**
     * 输出V级别以上日志
     *
     * @param msg 日志信息
     */
    public static void v(String msg) {
        OUT(Log.VERBOSE, TAG, msg);
    }

    /**
     * 输出V级别以上日志
     *
     * @param tag 日志Tag
     * @param msg 日志信息
     */
    public static void v(String tag, String msg) {
        OUT(Log.VERBOSE, TAG, msg);
    }

    /**
     * 输出d级别以上日志
     *
     * @param msg 日志信息
     */
    public static void d(String msg) {
        OUT(Log.DEBUG, TAG, msg);
    }

    /**
     * 输出d级别以上日志
     *
     * @param tag 日志Tag
     * @param msg 日志信息
     */
    public static void d(String tag, String msg) {
        OUT(Log.DEBUG, TAG, msg);
    }

    /**
     * 输出i级别以上日志
     *
     * @param msg 日志信息
     */
    public static void i(String msg) {
        OUT(Log.INFO, TAG, msg);
    }

    /**
     * 输出w级别以上日志
     *
     * @param tag 日志Tag
     * @param msg 日志信息
     */
    public static void w(String tag, String msg) {
        OUT(Log.INFO, TAG, msg);
    }

    /**
     * 输出w级别以上日志
     *
     * @param msg 日志信息
     */
    public static void w(String msg) {
        OUT(Log.WARN, TAG, msg);
    }

    /**
     * 输出i级别以上日志
     *
     * @param tag 日志Tag
     * @param msg 日志信息
     */
    public static void i(String tag, String msg) {
        OUT(Log.WARN, TAG, msg);
    }


    /**
     * 输出e级别以上日志
     *
     * @param msg 日志信息
     */
    public static void e(String msg) {
        OUT(Log.ERROR, TAG, msg);
    }

    /**
     * 输出e级别以上日志
     *
     * @param tag 日志Tag
     * @param msg 日志信息
     */
    public static void e(String tag, String msg) {
        OUT(Log.ERROR, tag, msg);
    }

    /**
     * 输出a级别以上日志
     *
     * @param msg 日志信息
     */
    public static void a(String msg) {
        OUT(Log.ASSERT, TAG, msg);
    }

    /**
     * 输出a级别以上日志
     *
     * @param tag 日志Tag
     * @param msg 日志信息
     */
    public static void a(String tag, String msg) {
        OUT(Log.ASSERT, tag, msg);
    }

    /**
     * 输出自定义级别以上日志
     *
     * @param priority 日志级别
     * @param tag      日志Tag
     * @param msg      日志信息
     */
    private static void OUT(int priority, String tag, String msg) {
        if (DEBUG) {
            if (priority >= Log.VERBOSE && priority <= Log.ASSERT) {
                Log.println(priority, tag, msg);
            } else {
                Log.println(defaultPriority, tag, msg);
            }
        }
    }

}
