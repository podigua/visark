package com.podigua.visark.server.programme.handler;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import com.podigua.visark.server.programme.entity.Programme;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;

/**
 * @program: monitor-alarm-config-parent
 * @description:
 * @author: podigua
 * @create: 2021-05-07 15:26
 **/
@Slf4j
@MappedTypes({Object.class})
@MappedJdbcTypes(JdbcType.VARCHAR)
public class ExpressionFastjsonTypeHandler extends FastjsonTypeHandler {

    public ExpressionFastjsonTypeHandler(Class<?> type) {
        super(type);
    }

    @Override
    protected Object parse(String json) {
        if (StringUtils.isEmpty(json)) {
            return Collections.emptyList();
        }
        try {
            List<Programme.Expression> array = JSON.parseArray(json, Programme.Expression.class);
            return array;
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
}
