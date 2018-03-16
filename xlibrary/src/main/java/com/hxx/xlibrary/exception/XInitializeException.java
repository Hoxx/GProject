package com.hxx.xlibrary.exception;

/**
 * Created by Android on 2017/12/19 .
 */

public class XInitializeException extends XBaseException {

    public XInitializeException() {
        super(XExceptionConstant.NOT_INITIALIZE_EXCEPTION);
    }

    public XInitializeException(String message) {
        super(XExceptionConstant.NOT_INITIALIZE_EXCEPTION, message);
    }
}
