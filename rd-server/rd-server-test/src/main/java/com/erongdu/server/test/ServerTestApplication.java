package com.erongdu.server.test;

import com.codingapi.txlcn.tc.config.EnableDistributedTransaction;
import com.erongdu.common.security.starter.annotation.EnableCloudResourceServer;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author erongdu.com
 */
@EnableFeignClients
@SpringBootApplication
@EnableCloudResourceServer
@EnableTransactionManagement
@EnableDistributedTransaction
@MapperScan("com.erongdu.server.test.mapper")
public class ServerTestApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(ServerTestApplication.class)
                .web(WebApplicationType.SERVLET)
                .run(args);
    }

}
