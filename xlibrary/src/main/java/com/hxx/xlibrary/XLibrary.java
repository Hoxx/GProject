package com.hxx.xlibrary;

import android.content.Context;

import com.hxx.xlibrary.cache.XCacheClient;
import com.hxx.xlibrary.db.XDBClient;
import com.hxx.xlibrary.db.XDBCreateCallback;
import com.hxx.xlibrary.image.XImageClient;
import com.hxx.xlibrary.memory.XMemoryData;
import com.hxx.xlibrary.net.XHttpCallback;
import com.hxx.xlibrary.net.XHttpClient;
import com.hxx.xlibrary.util.T;
import com.hxx.xlibrary.util.XConfigData;


/**
 * Created by Android on 2017/12/19 .
 */

public class XLibrary {

    public static Context context;

    private XLibrary() {

    }

    private XLibrary(Builder builder) {
        XConstant.DEBUG = builder.debug;
        context = builder.context;
        if (builder.openImageClient)
            XImageClient.getInstance().init(context);
        if (builder.xHttpCallback != null)
            XHttpClient.getInstance().init(builder.xHttpCallback);
        if (builder.xdbCreateCallback != null)
            XDBClient.getInstance().init(context, builder.xdbCreateCallback);
        if (builder.openCache)
            XCacheClient.getInstance().init(context);
        if (builder.closeToast)
            T.closeShow();
        if (builder.useConfigData)
            XConfigData.getInstance().init(context);
        if (builder.openMemoryData)
            XMemoryData.getInstance();
        XDataClient.getInstance();
    }

    public static final class Builder {

        Context context;
        boolean debug;
        //Image
        boolean openImageClient;
        //Http
        XHttpCallback xHttpCallback;
        //DB
        XDBCreateCallback xdbCreateCallback;
        //Cache
        boolean openCache;
        //Util
        boolean closeToast;
        boolean useConfigData;
        //内存数据
        boolean openMemoryData;

        public Builder(Context context) {
            this.context = context.getApplicationContext();
        }

        public Builder openImageClient() {
            openImageClient = true;
            return this;
        }

        public Builder openHttpClient(XHttpCallback xHttpCallback) {
            this.xHttpCallback = xHttpCallback;
            return this;
        }

        public Builder userDB(XDBCreateCallback callback) {
            this.xdbCreateCallback = callback;
            return this;
        }

        public Builder setDebug(boolean flag) {
            this.debug = flag;
            return this;
        }

        public Builder openCache() {
            this.openCache = true;
            return this;
        }

        public Builder closeToast() {
            this.closeToast = true;
            return this;
        }

        public Builder useConfigData() {
            this.useConfigData = true;
            return this;
        }

        public Builder openMemoryData() {
            this.openMemoryData = true;
            return this;
        }

        public XLibrary build() {
            return new XLibrary(this);
        }

    }
}
