package com.miiicasa.casa.thread;

import com.miiicasa.Config;
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
public class Run {

    private final static String TAG = Run.class.getSimpleName();
    private final static TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;
    private BlockingQueue<Runnable> workQueue;
    private ThreadPoolExecutor threadPoolExecutor;

    private static class LazyHolder {

        private static Run INSTANCE = new Run();
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

    private Run() {
        workQueue = new LinkedBlockingQueue<>();
        threadPoolExecutor = new ThreadPoolExecutor(
            Config.THREAD_POOL_SIZE,
            Config.THREAD_POOL_MAX_SZIE,
            Config.THREAD_KEEP_ALIVE_TIME,
            KEEP_ALIVE_TIME_UNIT,
            workQueue,
            new ThreadFactory() {
                @Override
                public Thread newThread(Runnable r) {
                    Thread thread = new Thread(r);
                    thread.setDaemon(false);
                    return thread;
                }
            }
        );
    }

    public static Run getInstance() {
        return LazyHolder.INSTANCE;
    }

    public ThreadPoolExecutor getThreadPoolExecutor() {
        return threadPoolExecutor;
    }

    public CasaTask submit(ThreadListener listener) {
        CasaTask task = new CasaTask(listener);
        threadPoolExecutor.execute(task);
        return task;
    }
}
