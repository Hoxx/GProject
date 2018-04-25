package com.hxx.xlibrary.exception;

/**
 * Created by Android on 2017/12/19 .
 */

public class XNetInitializeException extends XBaseException {

    public XNetInitializeException() {
        super(XExceptionConstant.NET_NOT_INITIALIZE_EXCEPTION);
    }

    public XNetInitializeException(String message) {
        super(XExceptionConstant.NET_NOT_INITIALIZE_EXCEPTION, message);
    }
}
