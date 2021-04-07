package com.erongdu.server.activiti;

import org.activiti.spring.boot.SecurityAutoConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.codingapi.txlcn.tc.config.EnableDistributedTransaction;
import com.erongdu.common.security.starter.annotation.EnableCloudResourceServer;

/**
 * @author lh@erongdu.com
 */
@EnableAsync
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@EnableCloudResourceServer
@EnableTransactionManagement
@EnableDistributedTransaction
@MapperScan("com.erongdu.server.activiti.dao")
public class ServerActivitiApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(ServerActivitiApplication.class)
                .web(WebApplicationType.SERVLET)
                .run(args);
    }
}
