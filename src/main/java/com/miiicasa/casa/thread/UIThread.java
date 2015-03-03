package com.miiicasa.casa.thread;

import android.os.Handler;
import android.os.Looper;

/**
 * Created by showsky on 15/3/3.
 */
public class UIThread {

    private final static String TAG = UIThread.class.getSimpleName();
    private Handler handler;

    private static class LazyHolder {

        private static UIThread INSTANCE = new UIThread();
    }

    private UIThread() {
        handler = new Handler(Looper.getMainLooper());
    }

    public static UIThread getInstance() {
        return LazyHolder.INSTANCE;
    }

    public void post(Runnable runnable) {
        handler.post(runnable);
    }

    public void delayPost(Runnable runnable, long time) {
        handler.postDelayed(runnable, time);
    }
}
