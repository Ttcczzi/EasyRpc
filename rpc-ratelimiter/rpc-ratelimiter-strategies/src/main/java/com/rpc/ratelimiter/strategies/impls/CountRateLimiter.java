package com.rpc.ratelimiter.strategies.impls;

import com.rpc.ratelimiter.common.base.AbstractRateLimiter;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 固定窗口
 * @author xcx
 * @date
 */
public class CountRateLimiter extends AbstractRateLimiter {

    private AtomicInteger countPermits = new AtomicInteger();

    private long lastTimeStamp = System.currentTimeMillis();
    @Override
    public boolean tryAcquire() {
        long currentTimeStamp = System.currentTimeMillis();
        if(currentTimeStamp - lastTimeStamp > limitTime){
            countPermits.set(0);
            lastTimeStamp = currentTimeStamp;

            return true;
        }

        if(countPermits.incrementAndGet() <= permites){
            return true;
        }
        return false;
    }

    @Override
    public void release() {

    }
}
