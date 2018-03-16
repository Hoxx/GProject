package com.hxx.xlibrary.net;


import com.hxx.xlibrary.XConstant;

/**
 * Created by Android on 2017/12/21.
 */

public class XHttpConstant {

    public static int REQUEST_NO_CACHE = 0xA001;

    public static boolean DEBUG = XConstant.DEBUG;
    public static String HTTP_LOG_TAG = "XHTTP-LOG";

    //外部参数类型
    public static final int PARAMS_TYPE_HEAD = 1;
    public static final int PARAMS_TYPE_QUERY = 2;

    public static long HTTP_TIME_OUT_MILLISECOND = 10_000;//http超时时间
}
