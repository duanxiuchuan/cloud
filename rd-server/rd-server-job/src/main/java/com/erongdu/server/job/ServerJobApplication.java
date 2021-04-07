package com.erongdu.server.job;

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
@MapperScan("com.erongdu.server.job.mapper")
public class ServerJobApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(ServerJobApplication.class)
                .web(WebApplicationType.SERVLET)
                .run(args);
    }

}
