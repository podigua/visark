package com.podigua.visark.server.admin.entity;

import lombok.Data;
import org.apache.kafka.clients.admin.TopicDescription;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: podigua
 * @create: 2021-09-12 01:41
 **/
@Data
public class TopicInfo {
    private String name;
    private boolean internal;
    private List<TopicPartition> partitions;

    /**
     * 创建
     *
     * @param description
     * @return
     */
    public static TopicInfo of(TopicDescription description) {
        TopicInfo result = new TopicInfo();
        if(description!=null){
            result.setName(description.name());
            result.setInternal(description.isInternal());
            if(!CollectionUtils.isEmpty(description.partitions())){
                result.setPartitions(description.partitions().stream().map(TopicPartition::of).collect(Collectors.toList()));
            }
        }
        return result;
    }
}
