package com.podigua.visark.server.cluster.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 集群 插件
 * @author: podigua
 * @create: 2021-09-01 17:36
 **/
@Getter
@Setter
@TableName("b_cluster")
public class Cluster {
    public Cluster(){

    }
    public Cluster(String id,String name){
        this.id=id;
        this.name=name;

    }
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
    private Date createTime;
    /**
     * 最后更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
    /**
     * 描述
     */
    private String description;

    @Override
    public String toString() {
        return this.getName();
    }
}
