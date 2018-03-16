package com.hxx.xlibrary.net;

/**
 * Created by HXX on 12/21/2017.
 */

public interface XOnRequestByTagCallback<T, V> {
    void onSuccess(T tag, V value);

    void onFailed(T tag, XRequestError error);
}
