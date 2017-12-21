package com.henallux.moveandseeandroid.Exception;

/**
 * Created by Alexandre on 01-12-17.
 */

public class HttpResultException extends Exception {
    private int resultCode;

    public HttpResultException(int resultCode) {
        this.resultCode = resultCode;
    }
    public int getResultCode() {
        return resultCode;
    }
}
