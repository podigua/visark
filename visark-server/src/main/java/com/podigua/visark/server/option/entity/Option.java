package com.podigua.visark.server.option.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Date;

/**
 * @author: podigua
 * @create: 2021-09-02 20:36
 **/
@Data
@TableName("b_option")
public class Option {
    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT, insertStrategy = FieldStrategy.NEVER)
    private Date createTime;
    /**
     * 最后更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
    /**
     * 超时时间
     */
    private Integer timeout = 10000;
    /**
     * 读超时时间
     */
    private Integer readTimeout = 10000;
    /**
     * Key 反序列化
     */
    private String keyDeserializer = StringDeserializer.class.getName();
    /**
     * value 反序列化
     */
    private String valueDeserializer = StringDeserializer.class.getName();
}
