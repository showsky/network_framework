package com.miiicasa.casa.thread;

import com.miiicasa.casa.utils.Logger;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by showsky on 15/3/3.
 */
public class Request {

    private final static String TAG = Request.class.getSimpleName();
    private final static int INITIAL_POOL_SIZE = 3;
    private final static int MAX_POOL_SIZE = 5;

    // Sets the amount of time an idle thread waits before terminating
    private final static int KEEP_ALIVE_TIME = 10;

    // Sets the Time Unit to seconds
    private final static TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;

    private BlockingQueue<Runnable> workQueue;
    private ThreadPoolExecutor threadPoolExecutor;

    private static class LazyHolder {

        private static Request INSTANCE = new Request();
    }

    public static Request getInstance() {
        return LazyHolder.INSTANCE;
    }

    public static class CasaTask extends FutureTask {

        private ThreadListener listener;

        public CasaTask(ThreadListener listener) {
            super(listener);
            this.listener = listener;
        }

        @Override
        protected void done() {
            super.done();
            UIThread.getInstance().post(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (listener != null) {
                            listener.onSuccess(get());
                        }
                    } catch (ExecutionException e) {
                        if (Logger.isDebug()) {
                            e.printStackTrace();
                        }
                        if (listener != null) {
                            listener.onFail(e.getCause());
                        }
                    } catch (InterruptedException e) {
                        if (Logger.isDebug()) {
                            e.printStackTrace();
                        }
                        if (listener != null) {
                            listener.onFail(e.getCause());
                        }
                    }
                }
            });
        }
    }

    private Request() {
        workQueue = new LinkedBlockingQueue<>();
        threadPoolExecutor = new ThreadPoolExecutor(
            INITIAL_POOL_SIZE,
            MAX_POOL_SIZE,
            KEEP_ALIVE_TIME,
            KEEP_ALIVE_TIME_UNIT,
            workQueue,
            new ThreadFactory() {
                @Override
                public Thread newThread(Runnable r) {
                    return new Thread(r);
                }
            }
        );
    }

    public CasaTask submit(String TAG, ThreadListener listener) {
        CasaTask task = new CasaTask(listener);
        threadPoolExecutor.execute(task);
        return task;
    }
}
