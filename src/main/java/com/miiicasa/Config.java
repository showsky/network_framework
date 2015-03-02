package com.miiicasa;

/**
 * Created by showsky on 15/3/2.
 */
public class Config {

    public final static int STORAGE_CACHE_SIZE = 1024 * 1024 * 30;
    public final static int MEMORY_CACHE_SIZE = 30;

    public final static String NETWORK_DEFAULT_USER_AGENT = "miiicasa";
    public final static int NETWORK_CONNECT_TIMEOUT_SECOND = 20;

    public static final boolean PROXY = false;
    public static final String PROXY_IP = "192.168.0.100";
    public static final int PROXY_PORT = 8888;

    public static final boolean RETRY_NETWORK = true;
    public static final int RETRY_MAX = 2;
}
