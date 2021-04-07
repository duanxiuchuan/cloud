package com.erongdu.gateway.common.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * @author erongdu.com
 */
@RestController
public class IndexController {

    @RequestMapping("/")
    public Mono<String> index() {
        return Mono.just("febs cloud gateway");
    }
}
