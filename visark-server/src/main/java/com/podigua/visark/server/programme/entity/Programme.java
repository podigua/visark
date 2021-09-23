package com.podigua.visark.server.programme.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.podigua.visark.server.programme.handler.ExpressionFastjsonTypeHandler;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author: podigua
 * @create: 2021-09-17 15:37
 **/
@Data
@TableName(value = "b_programme", autoResultMap = true)
public class Programme {
    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;
    /**
     * 方案名称
     */
    private String name;
    /**
     * topic
     */
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String topic;
    /**
     * 集群ID
     */
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String clusterId;
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT, insertStrategy = FieldStrategy.NEVER)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    /**
     * 最后更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
    /**
     * key
     */
    private String contentKey;
    /**
     * value
     */
    private String contentValue;
    /**
     * 表达式
     */
    @TableField(typeHandler = ExpressionFastjsonTypeHandler.class)
    private List<Expression> expressions;

    @Data
    public static class Expression {
        private String code;
        private String expression;
    }
}
