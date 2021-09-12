package com.podigua.visark.server.cluster.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 集群 插件
 * @author: podigua
 * @create: 2021-09-01 17:36
 **/
@Data
@TableName("b_cluster")
public class Cluster {
    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;
    /**
     * 名称
     */
    private String name;
    /**
     * 地址
     */
    private String bootstrapServers;
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT, insertStrategy = FieldStrategy.NEVER)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;
    /**
     * 最后更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;
    /**
     * 描述
     */
    private String description;
}
