package com.podigua.visark.core.expression;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author: podigua
 * @create: 2021-06-23 17:41
 **/
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Function {
    /**
     * 表达式
     */
    private String value;
    /**
     * 说明
     */
    private String label;

    /**
     * 创建
     *
     * @param value
     * @param label
     * @return
     */
    public static Function of(String value, String label) {
        return new Function(value, label);
    }
}
