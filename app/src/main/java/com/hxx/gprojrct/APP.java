package com.hxx.gprojrct;

import android.app.Application;

import com.google.gson.Gson;
import com.hxx.xlibrary.XLibrary;
import com.hxx.xlibrary.exception.XResponseException;
import com.hxx.xlibrary.net.XHttpCallback;
import com.hxx.xlibrary.util.L;

/**
 * Created by Android on 2018/3/19.
 */

public class APP extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        new XLibrary.Builder(getApplicationContext())
                .setDebug(true)
                .openHttpClient(getXHttpCallback1())
                .build();
    }

    private XHttpCallback getXHttpCallback1() {
        return new XHttpCallback() {
            @Override
            public String getHttpBaseUrl() {
                return "https://kyfw.12306.cn/";
            }

            @Override
            public void onConverter(Gson gson, String response) throws XResponseException {
                L.e("onConverter结果：" + response);
            }
        };
    }
}
