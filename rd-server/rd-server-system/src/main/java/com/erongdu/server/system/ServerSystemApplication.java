package com.erongdu.server.system;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.codingapi.txlcn.tc.config.EnableDistributedTransaction;
import com.erongdu.common.security.starter.annotation.EnableCloudResourceServer;

/**
 * @author erongdu.com
 */
@EnableAsync
@SpringBootApplication
@EnableCloudResourceServer
@EnableTransactionManagement
//@EnableDistributedTransaction
@MapperScan("com.erongdu.server.system.mapper")
public class ServerSystemApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(ServerSystemApplication.class)
                .web(WebApplicationType.SERVLET)
                .run(args);
    }
}
