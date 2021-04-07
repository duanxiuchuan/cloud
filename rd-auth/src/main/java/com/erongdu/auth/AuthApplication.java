package com.erongdu.auth;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import com.erongdu.common.security.starter.annotation.EnableCloudResourceServer;

/**
 * @author erongdu.com
 */
@SpringBootApplication
@EnableCloudResourceServer
@MapperScan("com.erongdu.auth.mapper")
public class AuthApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(AuthApplication.class)
                .web(WebApplicationType.SERVLET)
                .run(args);
    }
}
