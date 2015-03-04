package com.miiicasa.casa.exception;

/**
 * Created by showsky on 15/3/2.
 */
public class ApiException extends Exception {

    private final static String TAG = ApiException.class.getSimpleName();
    private int errno = 0;

    public ApiException(int errno) {
        super("Api fail errno: " + errno);
        this.errno = errno;
    }

    public int getErrNo() {
        return errno;
    }
}
