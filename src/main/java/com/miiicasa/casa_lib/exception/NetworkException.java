package com.miiicasa.casa_lib.exception;

/**
 * Created by showsky on 15/3/2.
 */
public class NetworkException extends Exception {

    private final static String TAG = NetworkException.class.getSimpleName();
    public enum TYPE {
        NETWORK_ERROR,
        SERVER_ERROR,
    };
    private static String[] message = {
        "Network error",
        "Server error",
    };
    private TYPE type = null;

    public NetworkException(TYPE type) {
        super(message[type.ordinal()]);
        this.type = type;
    }

    public NetworkException(TYPE type, String message) {
        super(message);
        this.type = type;
    }

    public TYPE getType() {
        return type;
    }
}
