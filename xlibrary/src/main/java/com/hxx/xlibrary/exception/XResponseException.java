package com.hxx.xlibrary.exception;

/**
 * Created by Android on 2017/9/5 .
 */

public class XResponseException extends XBaseException {

    public XResponseException(int exceptionCode) {
        super(exceptionCode);
    }

    public XResponseException(int exceptionCode, String message) {
        super(exceptionCode, message);
    }

    public XResponseException(int exceptionCode, Throwable cause) {
        super(exceptionCode, cause);
    }

    public XResponseException(int exceptionCode, String message, Throwable cause) {
        super(exceptionCode, message, cause);
    }
}
