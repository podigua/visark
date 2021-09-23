package com.podigua.visark.core.expression;

import lombok.Data;

import java.util.Map;

/**
 * @author: podigua
 * @create: 2021-06-24 09:46
 **/
@Data
public class ExpressionRequest {
    /**
     * 表达式
     */
    private String expression;
    /**
     * root
     */
    private Map<String, Object> root;
    /**
     * 参数
     */
    private Map<String, Object>[] variables;
}
