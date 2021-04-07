package com.erongdu.gateway.enhance.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.erongdu.common.core.entity.CommonResponse;
import com.erongdu.gateway.enhance.auth.JwtTokenHelper;
import com.erongdu.gateway.enhance.service.RouteUserService;

import reactor.core.publisher.Mono;

/**
 * @author erongdu.com
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("route")
public class RouteLoginController {

    private final JwtTokenHelper tokenHelper;
    private final PasswordEncoder passwordEncoder;
    private final RouteUserService routeUserService;

    @GetMapping("login")
    public Mono<ResponseEntity<CommonResponse>> login(String username, String password) {
        String error = "认证失败，用户名或密码错误";
        return routeUserService.findByUsername(username)
                .map(u -> passwordEncoder.matches(password, u.getPassword()) ?
                        ResponseEntity.ok(new CommonResponse().data(tokenHelper.generateToken(u))) :
                        new ResponseEntity<>(new CommonResponse().message(error), HttpStatus.INTERNAL_SERVER_ERROR))
                .defaultIfEmpty(new ResponseEntity<>(new CommonResponse().message(error), HttpStatus.INTERNAL_SERVER_ERROR));
    }
}
