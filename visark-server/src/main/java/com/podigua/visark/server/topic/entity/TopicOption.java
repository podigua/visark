package com.podigua.visark.server.topic.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Date;

/**
 * @author: podigua
 * @create: 2021-09-09 11:17
 **/
@Data
public class TopicOption {
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
     * Key 反序列化
     */
    private String keyDeserializer = StringDeserializer.class.getName();
    /**
     * value 反序列化
     */
    private String valueDeserializer = StringDeserializer.class.getName();
    /**
     * 集群ID
     */
    private String clusterId;
    /**
     * topic
     */
    private String topic;
}
