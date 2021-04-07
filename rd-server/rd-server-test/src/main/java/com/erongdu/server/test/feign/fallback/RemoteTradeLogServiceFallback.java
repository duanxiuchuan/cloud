package com.erongdu.server.test.feign.fallback;

import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import com.erongdu.server.test.feign.IRemoteTradeLogService;

/**
 * @author erongdu.com
 */
@Slf4j
@Component
public class RemoteTradeLogServiceFallback implements FallbackFactory<IRemoteTradeLogService> {
    @Override
    public IRemoteTradeLogService create(Throwable throwable) {
        return tradeLog -> log.error("调用失败", throwable);
    }
}
