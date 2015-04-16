package com.miiicasa.casa.exception;

/**
 * Created by showsky on 15/3/2.
 */
public class ApiException extends Exception {

    private final static String TAG = ApiException.class.getSimpleName();
    public enum TYPE {
        API_ERROR,
        JSON_FORMAT_ERROR,
    };
    private static String[] message = {
        "API error",
        "JSON format error",
    };
    private TYPE apiType = null;
    private int errNo = 0;
    private String errMessage;

    public ApiException(int errNo) {
        super("Api fail errno: " + errNo);
        this.errNo = errNo;
        this.errMessage = "Api fail errno: " + errNo;
        this.apiType = TYPE.API_ERROR;
    }

    public ApiException(int errNo, String errMessage) {
        super("Api fail errno: " + errNo + " errMessage: " + errMessage);
        this.errNo = errNo;
        this.errMessage = errMessage;
        this.apiType = TYPE.API_ERROR;
    }

    public ApiException(TYPE apiType) {
        super(message[apiType.ordinal()]);
        this.errMessage = message[apiType.ordinal()];
        this.apiType = apiType;
    }

    public int getErrNo() {
        return errNo;
    }

    public String getErrMessage() {
        return errMessage;
    }

    public TYPE getApiType() {
        return apiType;
    }
}
