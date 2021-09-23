package com.podigua.visark.core.expression;

import java.io.Serializable;

/**
 * @author: podigua
 * @create: 2021-06-23 17:21
 **/
public interface ExpressionFunction extends Serializable {
    /**
     * 获取key
     *
     * @return
     */
    String name();
}
