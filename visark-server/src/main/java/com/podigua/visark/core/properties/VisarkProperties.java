package com.podigua.visark.core.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.SocketUtils;

/**
 * @author: podigua
 * @create: 2021-09-01 10:01
 **/
@Configuration
@Data
@ConfigurationProperties("podigua.visark")
@EnableConfigurationProperties(value = VisarkProperties.class)
public class VisarkProperties {

}
