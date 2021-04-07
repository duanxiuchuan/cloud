package com.erongdu.gateway.enhance.mapper;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.erongdu.gateway.enhance.entity.RouteLog;

import reactor.core.publisher.Flux;

import java.util.Collection;

/**
 * @author erongdu.com
 */
@Repository
public interface RouteLogMapper extends ReactiveMongoRepository<RouteLog, String> {

    /**
     * 删除路由日志
     *
     * @param ids 路由日志id
     * @return 被删除的路由日志
     */
    Flux<RouteLog> deleteByIdIn(Collection<String> ids);
}
