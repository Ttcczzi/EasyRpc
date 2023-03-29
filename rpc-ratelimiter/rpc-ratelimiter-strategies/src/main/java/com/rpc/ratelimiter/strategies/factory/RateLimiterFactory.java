package com.rpc.ratelimiter.strategies.factory;

import com.rpc.ratelimiter.common.api.InvokeRateLimiter;
import com.rpc.ratelimiter.common.contants.RateLimiterConstants;
import com.rpc.ratelimiter.strategies.impls.BucketRateLimiter;
import com.rpc.ratelimiter.strategies.impls.CountRateLimiter;

/**
 * @author xcx
 * @date
 */
public class RateLimiterFactory {

    public static InvokeRateLimiter getRateLimiter(boolean enableLimit, int permites, long limitTime, String type) {
        if (!enableLimit) {
            return null;
        }
        switch (type) {
            case RateLimiterConstants.RL_TOKEN:
                return new BucketRateLimiter(permites, limitTime);
            case RateLimiterConstants.RL_COUNT:
                return new CountRateLimiter(permites, limitTime);
            default:
                return null;
        }
    }
}
