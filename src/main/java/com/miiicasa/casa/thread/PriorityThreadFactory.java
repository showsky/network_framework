package com.miiicasa.casa.thread;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by showsky on 3/25/15.
 */
public class PriorityThreadFactory implements ThreadFactory {

    private final static String TAG = PriorityThreadFactory.class.getSimpleName();
    private AtomicInteger mThreadNumber = new AtomicInteger(1);
    private int mPriority = Thread.NORM_PRIORITY;

    public PriorityThreadFactory setPriority(int priority) {
        mPriority = priority;
        return this;
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(r, "Thread #id " + mThreadNumber.getAndIncrement());
        thread.setPriority(mPriority);
        thread.setDaemon(false);
        return thread;
    }
}
