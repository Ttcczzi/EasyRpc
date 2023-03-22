package com.rpc.proxy.threadpool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 执行回调方法的线程池
 * @author xcx
 * @date
 */
public class CallBackThreadPool {
    public static ThreadPoolExecutor executor;

    static {
        executor =
                new ThreadPoolExecutor(12,
                        16,
                        600L,
                        TimeUnit.SECONDS,
                        new ArrayBlockingQueue<Runnable>(65536));
    }

    public static void submit(Runnable task){
        executor.submit(task);
    }

    public static void shutdown(){
        if (!executor.isShutdown()){
            executor.shutdown();
        }
    }
}
