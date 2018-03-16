package com.hxx.xlibrary.net;

/**
 * Created by HXX on 12/21/2017.
 */

public class XRequestError extends Error {

    private int code;

    public XRequestError(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public XRequestError(String message, int code) {
        super(message);
        this.code = code;
    }

    public XRequestError(String message, Throwable cause, int code) {
        super(message, cause);
        this.code = code;
    }

    public XRequestError(Throwable cause, int code) {
        super(cause);
        this.code = code;
    }
}
