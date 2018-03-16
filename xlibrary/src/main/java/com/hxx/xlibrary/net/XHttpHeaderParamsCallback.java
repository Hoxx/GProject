package com.hxx.xlibrary.net;

import android.util.SparseArray;

import java.util.Map;

/**
 * Created by Android on 2017/12/21.
 */

public interface XHttpHeaderParamsCallback extends XHttpCallback {

    SparseArray<Map<String,String>> getParams();
}
