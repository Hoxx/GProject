package com.hxx.xlibrary.exception;

/**
 * Created by Android on 2017/9/5 .
 */

public class XResponseNotFountException extends XResponseException {

    public XResponseNotFountException() {
        super(XExceptionConstant.NET_RESPONSE_NOT_FOUNT_EXCEPTION);
    }
}
