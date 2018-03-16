package com.hxx.xlibrary.manager;

import android.text.TextUtils;


import com.hxx.xlibrary.cache.XCacheConstant;
import com.hxx.xlibrary.exception.XExceptionConstant;
import com.hxx.xlibrary.exception.XResponseException;
import com.hxx.xlibrary.net.XHttpConstant;
import com.hxx.xlibrary.net.XOnRequestByTagCallback;
import com.hxx.xlibrary.net.XOnRequestCallback;
import com.hxx.xlibrary.net.XRequestError;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by HXX on 12/21/2017.
 */

public class XRequestManager extends XRequestCacheManager {

    private static XRequestManager instance = new XRequestManager();


    private XRequestManager() {
    }

    public static XRequestManager getInstance() {
        if (instance == null)
            instance = new XRequestManager();
        return instance;
    }

    //无Tag，无缓存的请求
    public <V> void request(Call<V> call, XOnRequestCallback<V> xOnRequestCallback) {
        addCall("", call);
        call.enqueue(new RequestCallBack<>(xOnRequestCallback));
    }

    //无Tag，有缓存的请求
    public <V> void requestAndCache(Call<V> call, Class<V> clz, long validTime, XOnRequestCallback<V> xOnRequestCallback) {
        addCall("", call);
        String key = call.request().url().toString();
        getCache(key, clz, new RequestCacheCallback<>(call, key, validTime, xOnRequestCallback));
    }

    //请求检查缓存回调
    class RequestCacheCallback<V> implements XOnRequestCallback<V> {

        private Call<V> call;
        private String key;
        private long validTime;
        private XOnRequestCallback<V> xOnRequestCallback;

        RequestCacheCallback(Call<V> call, String key, long validTime, XOnRequestCallback<V> xOnRequestCallback) {
            this.call = call;
            this.key = key;
            this.validTime = validTime;
            this.xOnRequestCallback = xOnRequestCallback;
        }

        @Override
        public void onSuccess(V value) {
            if (xOnRequestCallback != null)
                xOnRequestCallback.onSuccess(value);
        }

        @Override
        public void onFailed(XRequestError error) {
            if (error.getCode() == XHttpConstant.REQUEST_NO_CACHE) {//没有缓存
                call.enqueue(new RequestCallBack<>(key, validTime, xOnRequestCallback));
            }
        }
    }

    //有Tag，无缓存的请求
    public <T, V> void request(Call<V> call, T tag, XOnRequestByTagCallback<T, V> xOnRequestByTagCallback) {
        addCall(tag, call);
        call.enqueue(new RequestCallBack<>(tag, xOnRequestByTagCallback));
    }

    //有Tag，有缓存的请求
    public <T, V> void requestAndCache(Call<V> call, T tag, Class<V> clz, long validTime, XOnRequestByTagCallback<T, V> xOnRequestByTagCallback) {
        String key = call.request().url().toString();
        getCache(key, tag, clz, new RequestCacheByTagCallback<>(call, key, validTime, xOnRequestByTagCallback));
    }

    //请求检查缓存回调
    class RequestCacheByTagCallback<T, V> implements XOnRequestByTagCallback<T, V> {
        private Call<V> call;
        private String key;
        private long validTime;
        private XOnRequestByTagCallback<T, V> xOnRequestByTagCallback;

        RequestCacheByTagCallback(Call<V> call, String key, long validTime, XOnRequestByTagCallback<T, V> xOnRequestByTagCallback) {
            this.call = call;
            this.key = key;
            this.validTime = validTime;
            this.xOnRequestByTagCallback = xOnRequestByTagCallback;
        }

        @Override
        public void onSuccess(T tag, V value) {
            if (xOnRequestByTagCallback != null)
                xOnRequestByTagCallback.onSuccess(tag, value);
        }

        @Override
        public void onFailed(T tag, XRequestError error) {
            if (error.getCode() == XHttpConstant.REQUEST_NO_CACHE) {//没有缓存
                call.enqueue(new RequestCallBack<>(key, validTime, tag, xOnRequestByTagCallback));
            }
        }
    }

    //请求结果回调
    class RequestCallBack<T, V> implements Callback<V> {

        private T tag;
        private XOnRequestByTagCallback<T, V> xOnRequestByTagCallback;
        private XOnRequestCallback<V> xOnRequestCallback;
        //保存缓存
        private String key;
        private long validTime;


        RequestCallBack(T tag, XOnRequestByTagCallback<T, V> xOnRequestByTagCallback) {
            this.tag = tag;
            this.xOnRequestByTagCallback = xOnRequestByTagCallback;
        }

        RequestCallBack(String key, long validTime, T tag, XOnRequestByTagCallback<T, V> xOnRequestByTagCallback) {
            this.key = key;
            this.validTime = validTime;
            this.tag = tag;
            this.xOnRequestByTagCallback = xOnRequestByTagCallback;
        }

        RequestCallBack(XOnRequestCallback<V> xOnRequestCallback) {
            this.xOnRequestCallback = xOnRequestCallback;
        }

        RequestCallBack(String key, long validTime, XOnRequestCallback<V> xOnRequestCallback) {
            this.key = key;
            this.validTime = validTime;
            this.xOnRequestCallback = xOnRequestCallback;
        }

        @Override
        public void onResponse(Call<V> call, Response<V> response) {
            if (call.isCanceled())//判断并关闭请求
                call.cancel();
            V value = response.body();
            if (!TextUtils.isEmpty(key) && (validTime == XCacheConstant.NO_VALID_TIME || validTime > 0))
                saveCache(key, value, validTime);
            if (xOnRequestByTagCallback != null)//回调成功结果
                xOnRequestByTagCallback.onSuccess(tag, value);
            if (xOnRequestCallback != null)//回调成功结果
                xOnRequestCallback.onSuccess(value);
        }

        @Override
        public void onFailure(Call<V> call, Throwable t) {
            if (call.isCanceled())//判断并关闭请求
                call.cancel();
            int failedCode = XExceptionConstant.NET_OTHER_EXCEPTION;
            if (t instanceof XResponseException) {
                failedCode = ((XResponseException) t).getCode();
            } else if (t instanceof SocketTimeoutException) {
                failedCode = XExceptionConstant.NET_TIME_OUT_EXCEPTION;
            } else if (t instanceof UnknownHostException) {
                failedCode = XExceptionConstant.NET_UNKNOWN_HOST_EXCEPTION;
            }
            if (xOnRequestByTagCallback != null)
                xOnRequestByTagCallback.onFailed(tag, new XRequestError(t.getMessage(), failedCode));
            if (xOnRequestCallback != null) {//回调错误
                xOnRequestCallback.onFailed(new XRequestError(t.getMessage(), failedCode));
            }
        }
    }
}
