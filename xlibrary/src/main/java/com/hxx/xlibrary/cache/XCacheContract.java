package com.hxx.xlibrary.cache;

import android.content.Context;

/**
 * Created by HXX on 2017/12/24.
 */

public interface XCacheContract {

    void init(Context context);

    <V> void save(String key, V value);

    <V> void save(String key, V value, long validTime);

    void saveValue(String key, String value);

    void saveValue(String key, String value, long validTime);

    <V> void get(String key, Class<V> clz, XCacheResultCallback<V> callback);

    void getValue(String key, XCacheResultCallback<String> callback);

    void clearCache(String key);

    void clearAllCache();
}
