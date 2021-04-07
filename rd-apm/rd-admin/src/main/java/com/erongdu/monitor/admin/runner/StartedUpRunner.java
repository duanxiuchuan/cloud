package com.erongdu.monitor.admin.runner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author erongdu.com
 */
@Component
public class StartedUpRunner implements ApplicationRunner {
	private static final Logger LOG = LoggerFactory.getLogger(StartedUpRunner.class);
    private final ConfigurableApplicationContext context;
    private final Environment environment;

    @Autowired
    public StartedUpRunner(ConfigurableApplicationContext context, Environment environment) {
        this.context = context;
        this.environment = environment;
    }

    private static void printSystemUpBanner(Environment environment) {
        LOG.info("-----------------------------------------");
        LOG.info("服务启动成功，时间：{}", LocalDateTime.now());
        LOG.info("服务名称：{}", environment.getProperty("spring.application.name") );
        LOG.info("端口号：{}", environment.getProperty("server.port"));
        LOG.info("-----------------------------------------");
    }

    @Override
    public void run(ApplicationArguments args) {
        if (context.isActive()) {
            printSystemUpBanner(environment);
        }
    }
}
