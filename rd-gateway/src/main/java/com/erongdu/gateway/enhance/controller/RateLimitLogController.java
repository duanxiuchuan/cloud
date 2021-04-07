package com.erongdu.gateway.enhance.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.erongdu.common.core.entity.QueryRequest;
import com.erongdu.gateway.enhance.entity.RateLimitLog;
import com.erongdu.gateway.enhance.service.RateLimitLogService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author erongdu.com
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("route/auth/rateLimitLog")
public class RateLimitLogController {

    private final RateLimitLogService rateLimitLogService;

    @GetMapping("data")
    public Flux<RateLimitLog> findUserPages(QueryRequest request, RateLimitLog rateLimitLog) {
        return rateLimitLogService.findPages(request, rateLimitLog);
    }

    @GetMapping("count")
    public Mono<Long> findUserCount(RateLimitLog rateLimitLog) {
        return rateLimitLogService.findCount(rateLimitLog);
    }

    @DeleteMapping
    @PreAuthorize("hasAuthority('admin')")
    public Flux<RateLimitLog> deleteRateLimitLog(String ids) {
        return rateLimitLogService.delete(ids);
    }
}
