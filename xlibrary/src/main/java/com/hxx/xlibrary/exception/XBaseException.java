package com.hxx.xlibrary.exception;

/**
 * Created by Android on 2017/12/19 .
 */

public abstract class XBaseException extends RuntimeException {

    private int code;

    private XBaseException() {
    }

    public int getCode() {
        return code;
    }

    public XBaseException(int code) {
        this.code = code;
    }

    public XBaseException(int code, String message) {
        super(message);
        this.code = code;
    }

    public XBaseException(int code, Throwable cause) {
        super(cause);
        this.code = code;
    }

    public XBaseException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }


}
