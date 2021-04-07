package com.erongdu.gateway.enhance.runner;

import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import com.erongdu.gateway.enhance.service.BlackListService;
import com.erongdu.gateway.enhance.service.RateLimitRuleService;
import com.erongdu.gateway.enhance.service.RouteEnhanceCacheService;

/**
 * @author erongdu.com
 */
@RequiredArgsConstructor
public class RouteEnhanceRunner implements ApplicationRunner {
	private static final Logger LOG = LoggerFactory.getLogger(RouteEnhanceRunner.class);
    private final RouteEnhanceCacheService cacheService;
    private final BlackListService blackListService;
    private final RateLimitRuleService rateLimitRuleService;

    @Override
    public void run(ApplicationArguments args) {
        LOG.info("已开启网关增强功能：请求日志、黑名单&限流。");
        cacheService.saveAllBlackList(blackListService.findAll());
        cacheService.saveAllRateLimitRules(rateLimitRuleService.findAll());
    }
}
