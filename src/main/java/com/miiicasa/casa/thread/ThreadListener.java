package com.miiicasa.casa.thread;

import java.util.concurrent.Callable;

/**
 * Created by showsky on 15/3/3.
 */
public abstract class ThreadListener<T> implements Callable<T> {

    public abstract void onSuccess(T result) throws Exception;
    public abstract void onFail(Exception exception);
}
