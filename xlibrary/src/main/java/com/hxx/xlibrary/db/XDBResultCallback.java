package com.hxx.xlibrary.db;

/**
 * Created by HXX on 2017/12/24.
 */

public interface XDBResultCallback<T> {
    void call(int token, T t);
}
