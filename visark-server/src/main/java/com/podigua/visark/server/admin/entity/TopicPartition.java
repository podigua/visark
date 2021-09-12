package com.podigua.visark.server.admin.entity;

import lombok.Data;
import org.apache.kafka.common.TopicPartitionInfo;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: podigua
 * @create: 2021-09-12 01:42
 **/
@Data
public class TopicPartition {
    private int partition;
    private NodeInfo leader;
    private List<NodeInfo> replicas;
    private List<NodeInfo> isr;

    public static TopicPartition of(TopicPartitionInfo info) {
        TopicPartition result = new TopicPartition();
        if(info!=null){
            result.setPartition(info.partition());
            if(info.leader()!=null){
                result.setLeader(NodeInfo.of(info.leader()));
            }
            if(!CollectionUtils.isEmpty(info.replicas())){
                result.setReplicas(info.replicas().stream().map(NodeInfo::of).collect(Collectors.toList()));
            }
            if(!CollectionUtils.isEmpty(info.isr())){
                result.setIsr(info.isr().stream().map(NodeInfo::of).collect(Collectors.toList()));
            }
        }
        return result;
    }
}
