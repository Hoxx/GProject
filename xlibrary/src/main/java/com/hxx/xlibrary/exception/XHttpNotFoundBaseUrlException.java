package com.hxx.xlibrary.exception;

/**
 * Created by Android on 2017/12/21.
 */

public class XHttpNotFoundBaseUrlException extends XBaseException {

    public XHttpNotFoundBaseUrlException() {
        super(XExceptionConstant.NET_BASE_URL_NOT_FOUNT_EXCEPTION);
    }
}
