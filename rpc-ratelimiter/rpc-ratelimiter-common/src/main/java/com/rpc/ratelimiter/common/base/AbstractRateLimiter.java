package com.rpc.ratelimiter.common.base;

import com.rpc.ratelimiter.common.api.InvokeRateLimiter;

/**
 * @author xcx
 * @date
 */
public abstract class AbstractRateLimiter implements InvokeRateLimiter {

    protected int permites;

    protected long limitTime;

    public AbstractRateLimiter(int permites, long limitTime){
        this.permites = permites;
        this.limitTime = limitTime;
    }

    @Override
    public String toString() {
        return "AbstractRateLimiter{" +
                "permites=" + permites +
                ", limitTime=" + limitTime +
                ", type=" + this.getClass() +
                '}';
    }
}
