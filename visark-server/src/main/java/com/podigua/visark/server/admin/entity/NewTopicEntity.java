package com.podigua.visark.server.admin.entity;

import lombok.Data;

import java.util.Map;

/**
 * @author: podigua
 * @create: 2021-09-03 14:59
 **/
@Data
public class NewTopicEntity {
    private String name;
    private Integer partitions;
    private Short replication;
    private Map<String, String> configs = null;
}
