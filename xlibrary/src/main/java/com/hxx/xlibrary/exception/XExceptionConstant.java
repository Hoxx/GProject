package com.hxx.xlibrary.exception;

/**
 * Created by Android on 2017/12/19 .
 */

public class XExceptionConstant {
    //初始化错误
    static int NOT_INITIALIZE_EXCEPTION = 0x0001;//未初始化错误

    //网络错误
    static int NET_NOT_INITIALIZE_EXCEPTION = 0x1001;//网络库未初始化错误
    static int NET_RESPONSE_NOT_FOUNT_EXCEPTION = 0x1002;//未找到请求结果
    static int NET_BASE_URL_NOT_FOUNT_EXCEPTION = 0x1003;//未找到baseUrl

    public static int NET_TIME_OUT_EXCEPTION = 0x1003;//网络请求超时错误
    public static int NET_UNKNOWN_HOST_EXCEPTION = 0x1005;//未知主机错误
    public static int NET_OTHER_EXCEPTION = 0x1010;//其他错误
    //数据库错误
    static int DB_NOT_INITIALIZE_EXCEPTION = 0x2001;//数据库未初始化错误

    //内存数据错误
    static int MEMORY_DATA_NULL_EXCEPTION = 0x3001;//内存数据容器未初始化
}
