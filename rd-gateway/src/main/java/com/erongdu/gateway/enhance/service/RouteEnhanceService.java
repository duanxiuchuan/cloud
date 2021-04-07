package com.erongdu.gateway.enhance.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.web.server.ServerWebExchange;

import com.erongdu.common.core.entity.constant.CommonConstant;

import reactor.core.publisher.Mono;

/**
 * @author erongdu.com
 */
public interface RouteEnhanceService {

    /**
     * 根据黑名单规则进行过滤
     *
     * @param exchange ServerWebExchange
     * @return Mono<Void>
     */
    Mono<Void> filterBlackList(ServerWebExchange exchange);

    /**
     * 根据限流规则进行过滤
     *
     * @param exchange ServerWebExchange
     * @return Mono<Void>
     */
    Mono<Void> filterRateLimit(ServerWebExchange exchange);

    /**
     * 异步存储请求日志
     *
     * @param exchange ServerWebExchange
     */
    @Async(CommonConstant.ASYNC_POOL)
    void saveRequestLogs(ServerWebExchange exchange);

    /**
     * 异步存储拦截日志
     *
     * @param exchange ServerWebExchange
     */
    @Async(CommonConstant.ASYNC_POOL)
    void saveBlockLogs(ServerWebExchange exchange);

    /**
     * 异步存储限流日志
     *
     * @param exchange ServerWebExchange
     */
    @Async(CommonConstant.ASYNC_POOL)
    void saveRateLimitLogs(ServerWebExchange exchange);
}
