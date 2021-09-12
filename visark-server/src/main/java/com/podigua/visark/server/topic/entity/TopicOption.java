package com.podigua.visark.server.topic.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.podigua.visark.server.topic.service.TopicOptionService;
import lombok.Data;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Date;

/**
 * @author: podigua
 * @create: 2021-09-12 21:02
 **/
@Data
@TableName("b_topic_option")
public class TopicOption {
    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;
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
     * Key 反序列化
     */
    private String keyDeserializer = StringDeserializer.class.getName();
    /**
     * value 反序列化
     */
    private String valueDeserializer = StringDeserializer.class.getName();

    /**
     * 创建 默认 配置
     * @param cluster 集群
     * @param topic topic
     * @param keyDeserializer key 反序列化
     * @param valueDeserializer value 反序列化
     * @return {@link TopicOption}
     */
    public static TopicOption create(String cluster, String topic, String keyDeserializer, String valueDeserializer) {
        TopicOption result = new TopicOption();
        result.setClusterId(cluster);
        result.setTopic(topic);
        result.setKeyDeserializer(keyDeserializer);
        result.setValueDeserializer(valueDeserializer);
        return result;
    }

}
