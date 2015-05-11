package com.miiicasa.casa.network;

import java.io.Serializable;

/**
 * Created by showsky on 5/11/15.
 */
public class CookieValues implements Serializable {

    private static final String TAG = CookieValues.class.getSimpleName();

    public String name;
    public String value;
    public String comment;
    public String commentUrl;
    public String domain;
    public long maxAge;
    public String path;
    public String protlist;
    public int version;
    public boolean secure;
    public boolean discard;
}
