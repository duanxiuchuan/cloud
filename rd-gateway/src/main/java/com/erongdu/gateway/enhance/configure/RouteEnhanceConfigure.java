package com.erongdu.gateway.enhance.configure;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.erongdu.common.core.entity.constant.CommonConstant;
import com.erongdu.gateway.enhance.runner.RouteEnhanceRunner;
import com.erongdu.gateway.enhance.service.BlackListService;
import com.erongdu.gateway.enhance.service.RateLimitRuleService;
import com.erongdu.gateway.enhance.service.RouteEnhanceCacheService;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author erongdu.com
 */
@EnableAsync
@Configuration
@EnableReactiveMongoRepositories(basePackages = "com.erongdu.gateway.enhance.mapper")
@ConditionalOnProperty(name = "rd.gateway.enhance", havingValue = "true")
public class RouteEnhanceConfigure {

    @Bean(CommonConstant.ASYNC_POOL)
    public ThreadPoolTaskExecutor asyncThreadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(20);
        executor.setQueueCapacity(100);
        executor.setKeepAliveSeconds(30);
        executor.setThreadNamePrefix("Febs-Gateway-Async-Thread");
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(60);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }

    @Bean
    public ApplicationRunner febsRouteEnhanceRunner(RouteEnhanceCacheService cacheService,
                                                    BlackListService blackListService,
                                                    RateLimitRuleService rateLimitRuleService) {
        return new RouteEnhanceRunner(cacheService, blackListService, rateLimitRuleService);
    }
}
