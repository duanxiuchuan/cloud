package com.erongdu.server.test.feign.fallback;

import com.erongdu.common.core.annotation.Fallback;
import com.erongdu.server.test.feign.IRemoteUserService;

import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;

/**
 * @author erongdu.com
 */
@Slf4j
@Fallback
public class RemoteUserServiceFallback implements FallbackFactory<IRemoteUserService> {

    @Override
    public IRemoteUserService create(Throwable throwable) {
        return (queryRequest, user) -> {
            log.error("获取用户信息失败", throwable);
            return null;
        };
    }
}