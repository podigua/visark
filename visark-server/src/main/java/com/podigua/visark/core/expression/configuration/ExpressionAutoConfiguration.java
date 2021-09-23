package com.podigua.visark.core.expression.configuration;

import com.podigua.visark.core.expression.ExpressionExecutor;
import com.podigua.visark.core.expression.ExpressionFunction;
import com.podigua.visark.core.expression.controller.ExpressionRestController;
import com.podigua.visark.core.expression.function.DateExpressionFunction;
import com.podigua.visark.core.expression.function.StringExpressionFunction;
import com.podigua.visark.core.expression.properties.ExpressionProperties;
import com.podigua.visark.core.expression.service.ExpressionExecutorImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author: podigua
 * @create: 2021-06-23 19:07
 **/
@Configuration
@ConditionalOnProperty(name = "square.itsm.expression.enabled", matchIfMissing = true)
@EnableConfigurationProperties(ExpressionProperties.class)
public class ExpressionAutoConfiguration {
    @Bean
    public ExpressionFunction dateExpressionFunction() {
        return new DateExpressionFunction();
    }

    @Bean
    public ExpressionFunction stringExpressionFunction() {
        return new StringExpressionFunction();
    }

    @Bean
    public ExpressionExecutor expressionExecutor(List<ExpressionFunction> expressionFunctions, ApplicationContext applicationContext, ExpressionProperties expressionProperties) {
        return new ExpressionExecutorImpl(expressionFunctions, applicationContext, expressionProperties);
    }

    @Bean
    @ConditionalOnMissingBean
    public ExpressionRestController expressionRestController(ExpressionExecutor expressionExecutor) {
        return new ExpressionRestController(expressionExecutor);
    }
}
