package com.podigua.visark.core.configuration;

import com.podigua.visark.core.properties.VisarkProperties;
import com.podigua.visark.core.utils.ExecutorUtils;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;

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
    @Bean
    public ExecutorService executorService(){
        return ExecutorUtils.executorService(10);
    }

}
