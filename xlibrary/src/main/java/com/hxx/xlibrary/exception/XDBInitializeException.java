package com.hxx.xlibrary.exception;

/**
 * Created by Android on 2017/12/19 .
 */

public class XDBInitializeException extends XBaseException {

    public XDBInitializeException() {
        super(XExceptionConstant.DB_NOT_INITIALIZE_EXCEPTION);
    }

    public XDBInitializeException(String message) {
        super(XExceptionConstant.DB_NOT_INITIALIZE_EXCEPTION, message);
    }
}
