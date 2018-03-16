package com.hxx.xlibrary.net;

/**
 * Created by HXX on 12/21/2017.
 */

public interface XOnRequestCallback<V> {
    void onSuccess(V value);

    void onFailed(XRequestError error);
}
