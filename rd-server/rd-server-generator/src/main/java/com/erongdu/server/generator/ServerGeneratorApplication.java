package com.erongdu.server.generator;

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
@MapperScan("com.erongdu.server.generator.mapper")
public class ServerGeneratorApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(ServerGeneratorApplication.class)
                .web(WebApplicationType.SERVLET)
                .run(args);
    }

}
