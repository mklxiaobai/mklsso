package com.client1.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author 马锴梁
 * @version 1.0
 * @date 2019/12/13 19:40
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "sso.server")
public class SsoConfig {
    private String url;
    private String loginPath;
}
