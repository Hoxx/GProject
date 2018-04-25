package com.hxx.xlibrary.net;

import com.google.gson.Gson;
import com.hxx.xlibrary.exception.XResponseException;

/**
 * Created by Android on 2017/12/21.
 */

public interface XHttpCallback {

    String getHttpBaseUrl();

    void onConverter(Gson gson, String response) throws XResponseException;
}
