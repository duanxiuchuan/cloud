package com.erongdu.gateway.enhance.mapper;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.erongdu.gateway.enhance.entity.BlockLog;

import reactor.core.publisher.Flux;

import java.util.Collection;

/**
 * @author erongdu.com
 */
public interface BlockLogMapper extends ReactiveMongoRepository<BlockLog, String> {

    /**
     * 删除拦截日志
     *
     * @param ids 日志id
     * @return 被删除的拦截日志
     */
    Flux<BlockLog> deleteByIdIn(Collection<String> ids);
}
