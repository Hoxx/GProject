package com.hxx.xlibrary.net.converter;

import com.google.gson.Gson;
import com.hxx.xlibrary.exception.XResponseException;
import com.hxx.xlibrary.net.XHttpCallback;


/**
 * Created by Android on 2017/12/20.
 */

public interface XResponseBodyConverterCallBack extends XHttpCallback {

    void onConverter(Gson gson, String response) throws XResponseException;

}
