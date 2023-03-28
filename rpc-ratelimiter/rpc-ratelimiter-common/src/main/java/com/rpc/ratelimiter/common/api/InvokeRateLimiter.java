package com.rpc.ratelimiter.common.api;

/**
 * @author xcx
 * @date
 */
public interface InvokeRateLimiter {
    boolean tryAcquire();

    void release();
}
