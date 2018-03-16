package com.hxx.xlibrary.manager;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by HXX on 2018/1/1.
 */

abstract class XBaseRequestManager {

    private Map<Object, Call> requestMap = new HashMap<>();


    //添加当前请求到请求列表
    <T> void addCall(T tag, Call call) {
        requestMap.put(tag, call);
    }

    //关闭全部请求
    public void cancelAllRequest() {
        for (Map.Entry<Object, Call> entry : requestMap.entrySet()) {
            Call call = entry.getValue();
            if (call.isCanceled())
                call.cancel();
        }
        requestMap.clear();
    }

    //关闭全部请求
    public <T> void cancelRequest(T... tags) {
        for (T tag : tags) {
            if (requestMap.containsKey(tag)) {
                Call call = requestMap.get(tag);
                if (!call.isCanceled())
                    call.cancel();
            }
            requestMap.remove(tag);
        }
    }

}
