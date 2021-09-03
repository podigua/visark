package com.podigua.visark.core.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * mybatis-plus 自动填充字段
 *
 * @author podigua
 */
@Component
public class MybatisPlusMetaObjectHandler implements MetaObjectHandler {

    /**
     * 创建时间
     */
    private static final String CREATE_TIME = "createTime";
    /**
     * 更新时间
     */
    private static final String UPDATE_TIME = "updateTime";
    /**
     * 删除标识
     */
    private static final String DELETED_FLAG = "isDeleted";

    /**
     * 设置字段
     *
     * @param metaObject
     * @param field
     * @param value
     */
    private void setValue(MetaObject metaObject, String field, Object value) {
        boolean hadField = metaObject.hasSetter(field);
        if (hadField) {
            this.fillStrategy(metaObject, field, value);
        }
    }


    @Override
    public void insertFill(MetaObject metaObject) {
        setValue(metaObject, DELETED_FLAG, Boolean.FALSE);
        setValue(metaObject, CREATE_TIME, new Date());
        setValue(metaObject, UPDATE_TIME, new Date());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        boolean hadField = metaObject.hasSetter(UPDATE_TIME);
        if (hadField) {
            metaObject.setValue(UPDATE_TIME, new Date());
        }
    }


}
