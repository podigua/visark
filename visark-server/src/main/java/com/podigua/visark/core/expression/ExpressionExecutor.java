package com.podigua.visark.core.expression;

import java.util.List;
import java.util.Map;

/**
 * @author: podigua
 * @create: 2021-06-23 17:20
 **/
public interface ExpressionExecutor {
    /**
     * 执行表达式
     *
     * @param expression
     * @param root
     * @param variables
     * @param <T>
     * @return
     */
    <T> T getValue(String expression, Object root, Map<String, Object>... variables);

    /**
     * 执行模板表达式
     *
     * @param expression
     * @param root
     * @param variables
     * @param <T>
     * @return
     */
    <T> T getValueByTemplate(String expression, Object root, Map<String, Object>... variables);

    /**
     * 获取所有函数
     *
     * @return
     */
    List<Function> getFunctions();
}
