package com.podigua.visark.core.configuration;

import com.podigua.visark.core.properties.VisarkProperties;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: podigua
 * @create: 2021-09-01 10:00
 **/
@Configuration
public class VisarkConfiguration {
    @Bean
    public TomcatServletWebServerFactory servletWebServerFactory(VisarkProperties visarkProperties) {
        return new TomcatServletWebServerFactory(visarkProperties.getPort());
    }
}
