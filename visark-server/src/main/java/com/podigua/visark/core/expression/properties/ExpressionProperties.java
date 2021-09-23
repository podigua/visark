package com.podigua.visark.core.expression.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author: podigua
 * @create: 2020-09-03 13:53
 **/
@Data
@ConfigurationProperties(prefix = "square.itsm.expression")
public class ExpressionProperties {
    /**
     * 前缀
     */
    private String prefix = "${";
    /**
     * 后缀
     */
    private String suffix = "}";
}
