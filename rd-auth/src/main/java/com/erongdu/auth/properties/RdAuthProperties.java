package com.erongdu.auth.properties;

import lombok.Data;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

/**
 * @author erongdu.com
 */
@Data
@SpringBootConfiguration
@PropertySource(value = {"classpath:rd-auth.properties"})
@ConfigurationProperties(prefix = "rd.auth")
public class RdAuthProperties {
    /**
     * 验证码配置
     */
    private RdValidateCodeProperties code = new RdValidateCodeProperties();
    /**
     * JWT加签密钥
     */
    private String jwtAccessKey;
    /**
     * 是否使用 JWT令牌
     */
    private Boolean enableJwt;

    /**
     * 社交登录所使用的 Client
     */
    private String socialLoginClientId;
}
