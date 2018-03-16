package com.hxx.xlibrary.net;

import android.util.SparseArray;


import com.hxx.xlibrary.net.converter.XResponseBodyConverterCallBack;

import java.util.Map;

/**
 * Created by Android on 2017/12/21.
 */

public interface XHttpAllCallback extends XResponseBodyConverterCallBack {

    SparseArray<Map<String,String>> getParams();
}
