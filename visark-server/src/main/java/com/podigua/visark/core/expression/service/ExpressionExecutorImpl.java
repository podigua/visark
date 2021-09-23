package com.podigua.visark.core.expression.service;


import com.podigua.visark.core.expression.ExpressionExecutor;
import com.podigua.visark.core.expression.ExpressionFunction;
import com.podigua.visark.core.expression.Function;
import com.podigua.visark.core.expression.properties.ExpressionProperties;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.expression.MapAccessor;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author: podigua
 * @create: 2021-06-23 17:26
 **/
public class ExpressionExecutorImpl implements ExpressionExecutor {
    private final static String DEFAULT_METHOD_NAME = "name";

    private final List<ExpressionFunction> expressionFunctions;
    private final ApplicationContext applicationContext;
    private final ExpressionProperties expressionProperties;

    public ExpressionExecutorImpl(List<ExpressionFunction> expressionFunctions, ApplicationContext applicationContext, ExpressionProperties expressionProperties) {
        this.expressionFunctions = expressionFunctions;
        this.applicationContext = applicationContext;
        this.expressionProperties = expressionProperties;
    }

    @Override
    public <T> T getValue(String expression, Object root, Map<String, Object>... variables) {
        return getValue(expression, root, Boolean.FALSE, variables);
    }

    @Override
    public <T> T getValueByTemplate(String expression, Object root, Map<String, Object>... variables) {
        return getValue(expression, root, Boolean.TRUE, variables);
    }

    @Override
    public List<Function> getFunctions() {
        List<Function> result = new ArrayList<>();
        Map<String, ExpressionFunction> beans = applicationContext.getBeansOfType(ExpressionFunction.class);
        beans.forEach((k, obj) -> {
            Class<? extends ExpressionFunction> clazz = obj.getClass();
            String prefix = getPrefix(obj, clazz);
            Method[] methods = clazz.getDeclaredMethods();
            Arrays.stream(methods).filter(method -> !StringUtils.equals(DEFAULT_METHOD_NAME, method.getName())).forEach(method -> {
                result.add(Function.of(buildExp(prefix, method, Boolean.FALSE), buildExp(prefix, method, Boolean.TRUE)));
            });
        });
        return result;
    }

    /**
     * 构建表达式
     *
     * @param prefix
     * @param method
     * @param hasType
     * @return
     */
    private String buildExp(String prefix, Method method, boolean hasType) {
        StringBuilder value = new StringBuilder("#");
        value.append(prefix).append(".");
        String methodName = method.getName();
        value.append(methodName).append("(");
        Parameter[] parameters = method.getParameters();
        AtomicInteger index = new AtomicInteger();
        Arrays.stream(parameters).forEach(parameter -> {
            if (index.getAndIncrement() != 0) {
                value.append(",");
            }
            if (hasType) {
                value.append(parameter.getType().getSimpleName()).append(" ").append(parameter.getName());
            } else {
                value.append(parameter.getName());
            }
        });
        value.append(")");
        return value.toString();
    }

    /**
     * 获取方法的name
     *
     * @param obj
     * @param clazz
     * @return
     */
    @SneakyThrows
    private String getPrefix(ExpressionFunction obj, Class<? extends ExpressionFunction> clazz) {
        Method method = clazz.getMethod(DEFAULT_METHOD_NAME);
        return (String) method.invoke(obj);
    }

    /**
     * 获取值
     *
     * @param expression
     * @param root
     * @param iseTemplate
     * @param variables
     * @param <T>
     * @return
     */
    @SafeVarargs
    private final <T> T getValue(String expression, Object root, boolean iseTemplate, Map<String, Object>... variables) {
        if (StringUtils.isEmpty(expression)) {
            return null;
        }
        ExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.addPropertyAccessor(new MapAccessor());
        Optional.ofNullable(root).ifPresent(rootObject -> {
            context.setRootObject(root);
        });
        if (variables != null && variables.length > 0) {
            Arrays.stream(variables).forEach(param -> {
                param.forEach(context::setVariable);
            });
        }
        expressionFunctions.forEach(function -> {
            context.setVariable(function.name(), function);
        });
        Expression exp = null;
        if (iseTemplate) {
            exp = parser.parseExpression(expression, new TemplateParserContext(expressionProperties.getPrefix(), expressionProperties.getSuffix()));
        } else {
            exp = parser.parseExpression(expression);
        }
        return (T) exp.getValue(context);
    }
}
