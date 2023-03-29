package com.rpc.springboot.provider.autoconfig;

import com.rpc.provider.spring.RpcSpringProvider;
import com.rpc.ratelimiter.common.contants.RateLimiterConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author xcx
 * @date
 */

@Configuration
@ConfigurationProperties("easyrpc.provider")
public class RpcSpringServerAutoConfig {
    /**
     * 本机地址
     */
    private String host = "127.0.0.1";
    /**
     * 本机端口
     */
    private String port = "1106";
    /**
     * 注册中心地址
     */
    private String regsitryAddress = "127.0.0.1:2181";
    /**
     * 注册中心类型
     */
    private String registryType = "zookeeper";
    /**
     * 是否限流
     */
    private boolean enableLimit = true;
    /**
     * 流量数量
     */
    private int permites = 1000;
    /**
     * 限流时间
     */
    private long limitTime = 2000;
    /**
     * 限流方式
     */
    private String rateLimiterType = RateLimiterConstants.RL_TOKEN;

    @Bean
    public RpcSpringProvider rpcSpringProviderServer() throws Exception {
        return
                new RpcSpringProvider(host, Integer.parseInt(port),
                        regsitryAddress, registryType, enableLimit, permites, limitTime, rateLimiterType);
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getRegsitryAddress() {
        return regsitryAddress;
    }

    public void setRegsitryAddress(String regsitryAddress) {
        this.regsitryAddress = regsitryAddress;
    }

    public String getRegistryType() {
        return registryType;
    }

    public void setRegistryType(String registryType) {
        this.registryType = registryType;
    }

    public boolean isEnableLimit() {
        return enableLimit;
    }

    public void setEnableLimit(boolean enableLimit) {
        this.enableLimit = enableLimit;
    }

    public int getPermites() {
        return permites;
    }

    public void setPermites(int permites) {
        this.permites = permites;
    }

    public long getLimitTime() {
        return limitTime;
    }

    public void setLimitTime(long limitTime) {
        this.limitTime = limitTime;
    }

    public String getRateLimiterType() {
        return rateLimiterType;
    }

    public void setRateLimiterType(String rateLimiterType) {
        this.rateLimiterType = rateLimiterType;
    }
}
