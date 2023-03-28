package com.rpc.ratelimiter.common.base;

import com.rpc.ratelimiter.common.api.InvokeRateLimiter;

/**
 * @author xcx
 * @date
 */
public abstract class AbstractRateLimiter implements InvokeRateLimiter {

    protected int permites;

    protected long limitTime;

    public void init(int permites, int limitTime){
        this.permites = permites;
        this.limitTime = limitTime;
    }
}
