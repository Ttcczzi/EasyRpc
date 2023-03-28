package com.rpc.ratelimiter.strategies.impls;

import com.rpc.ratelimiter.common.base.AbstractRateLimiter;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author xcx
 * @date
 */
public class BucketRateLimiter extends AbstractRateLimiter {

    private AtomicInteger tokens = new AtomicInteger(permites);

    private long outTime;

    private ScheduledThreadPoolExecutor executor;

    {
        //默认60秒
        outTime = 60 * 1000;
        executor = new ScheduledThreadPoolExecutor(1);
        executor.scheduleAtFixedRate(new TokenIncreaseTask(), 0, limitTime, TimeUnit.MILLISECONDS);
    }


    @Override
    public boolean tryAcquire() {
        int leftTokens;

        long comeTime = System.currentTimeMillis();
        while ((leftTokens = tokens.get()) > 0){
            if(tokens.compareAndSet(leftTokens, leftTokens - 1)){
                return true;
            }
            if(System.currentTimeMillis() - comeTime >= outTime){
                return false;
            }
        }
        return false;
    }

    @Override
    public void release() {
        executor.shutdown();
    }

    class TokenIncreaseTask implements Runnable{
        @Override
        public void run() {
            if(tokens.get() < permites){
                tokens.incrementAndGet();
            }
        }
    }
}
