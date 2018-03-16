package com.hxx.xlibrary.util;

import com.google.gson.Gson;

/**
 * Created by HXX on 2017/12/24.
 */

public class XGson {

    private static XGson instance = new XGson();

    private Gson gson;

    private XGson() {
        gson = new Gson();
    }

    public static XGson getInstance() {
        if (instance == null)
            instance = new XGson();
        return instance;
    }

    public synchronized Gson getGson() {
        synchronized (XGson.class) {
            if (gson == null)
                gson = new Gson();
            return gson;
        }
    }
}
