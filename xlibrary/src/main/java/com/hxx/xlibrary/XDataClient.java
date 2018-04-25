package com.hxx.xlibrary;

import com.hxx.xlibrary.db.XDBClient;
import com.hxx.xlibrary.exception.XDBInitializeException;
import com.hxx.xlibrary.manager.XRequestManager;
import com.hxx.xlibrary.net.XHttpClient;
import com.hxx.xlibrary.util.L;

/**
 * Created by HXX on 2017/12/30.
 */

public class XDataClient {

    private static XDataClient instance = new XDataClient();

    private XRequestManager xRequestManager;
    private XDBClient xdbClient;
    private XHttpClient xHttpClient;

    private Object API;

    public static XDataClient getInstance() {
        if (instance == null) {
            instance = new XDataClient();
        }
        return instance;
    }

    private XDataClient() {
        init();
    }

    private void init() {
        xRequestManager = XRequestManager.getInstance();
        xdbClient = XDBClient.getInstance();
        xHttpClient = XHttpClient.getInstance();
    }

    public XDBClient XDB() {
        if (!xdbClient.isEnable())
            throw new XDBInitializeException("XDB Not init");
        return xdbClient;
    }

    public XHttpClient XHttp() {
        return xHttpClient;
    }

    //调用请求
    public XRequestManager XRequest() {
        return xRequestManager;
    }

    //取消请求
    public void cancelRequest() {
        XRequest().cancelAllRequest();
    }

    //取消请求
    public void cancelRequest(String... tags) {
        XRequest().cancelRequest(tags);
    }

    public <T> T getAPI(final Class<T> service) {
        if (API == null)
            API = XHttp().createService(service);
        return (T) API;
    }

}
