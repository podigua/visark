package com.podigua.visark.core.expression.function;

import com.podigua.visark.core.expression.ExpressionFunction;
import com.podigua.visark.core.utils.UUIDUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.UUID;

/**
 * 字符串
 *
 * @author: podigua
 * @create: 2021-06-10 19:40
 **/
public class StringExpressionFunction implements ExpressionFunction {
    /**
     * 格式化时间
     *
     * @param date
     * @param pattern
     * @return
     */
    public String format(Date date, String pattern) {
        return DateFormatUtils.format(date, pattern);
    }

    /**
     * 生成UUID
     *
     * @return
     */
    public String uuid() {
        return UUIDUtils.uuid();
    }


    /**
     * md5
     *
     * @return
     */
    public String md5(String content) {
        return DigestUtils.md5DigestAsHex(content.getBytes());
    }

    @Override
    public String name() {
        return "string";
    }
}
