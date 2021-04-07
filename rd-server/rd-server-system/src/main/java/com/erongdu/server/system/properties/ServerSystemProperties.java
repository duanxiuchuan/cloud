package com.erongdu.server.system.properties;

import lombok.Data;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

/**
 * @author erongdu.com
 */
@Data
@SpringBootConfiguration
@PropertySource(value = {"classpath:server-system.properties"})
@ConfigurationProperties(prefix = "rd.server.system")
public class ServerSystemProperties {
    /**
     * 批量插入当批次可插入的最大值
     */
    private Integer batchInsertMaxNum = 1000;
}
