package com.hxx.xlibrary.manager;


import com.hxx.xlibrary.cache.XCacheClient;
import com.hxx.xlibrary.cache.XCacheResultCallback;
import com.hxx.xlibrary.net.XHttpConstant;
import com.hxx.xlibrary.net.XOnRequestByTagCallback;
import com.hxx.xlibrary.net.XOnRequestCallback;
import com.hxx.xlibrary.net.XRequestError;

/**
 * Created by HXX on 2018/1/1.
 */

abstract class XRequestCacheManager extends XBaseRequestManager {


    //获取缓存
    <T, V> void getCache(String key, T tag, Class<V> clz, XOnRequestByTagCallback<T, V> xOnRequestByTagCallback) {
        if (XCacheClient.getInstance().isEnable()) {
            XCacheClient.getInstance().get(key, clz, new CacheCallBack<>(tag, xOnRequestByTagCallback));
        } else {
            if (xOnRequestByTagCallback != null)
                xOnRequestByTagCallback.onFailed(tag, new XRequestError(XHttpConstant.REQUEST_NO_CACHE));
        }
    }

    //获取缓存
    <V> void getCache(String key, Class<V> clz, XOnRequestCallback<V> xOnRequestCallback) {
        if (XCacheClient.getInstance().isEnable()) {
            XCacheClient.getInstance().get(key, clz, new CacheCallBack<>(xOnRequestCallback));
        } else {
            if (xOnRequestCallback != null)
                xOnRequestCallback.onFailed(new XRequestError(XHttpConstant.REQUEST_NO_CACHE));
        }
    }

    //缓存结果回调处理
    class CacheCallBack<T, V> implements XCacheResultCallback<V> {

        private XOnRequestCallback<V> xOnRequestCallback;
        private XOnRequestByTagCallback<T, V> xOnRequestByTagCallback;
        private T tag;

        CacheCallBack(XOnRequestCallback<V> xOnRequestCallback) {
            this.xOnRequestCallback = xOnRequestCallback;
        }

        CacheCallBack(T tag, XOnRequestByTagCallback<T, V> xOnRequestByTagCallback) {
            this.xOnRequestByTagCallback = xOnRequestByTagCallback;
            this.tag = tag;
        }

        @Override
        public void call(V value) {
            if (xOnRequestCallback != null) {
                if (value != null) {
                    xOnRequestCallback.onSuccess(value);
                } else {
                    xOnRequestCallback.onFailed(new XRequestError(XHttpConstant.REQUEST_NO_CACHE));
                }
            } else if (xOnRequestByTagCallback != null) {
                if (value != null) {
                    xOnRequestByTagCallback.onSuccess(tag, value);
                } else {
                    xOnRequestByTagCallback.onFailed(tag, new XRequestError(XHttpConstant.REQUEST_NO_CACHE));
                }
            }
        }
    }


    //保存缓存
    <V> void saveCache(String key, V value, long validTime) {
        if (XCacheClient.getInstance().isEnable()) {
            XCacheClient.getInstance().save(key, value, validTime);
        }
    }

}
