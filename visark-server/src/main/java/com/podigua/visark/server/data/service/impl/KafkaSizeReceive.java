package com.podigua.visark.server.data.service.impl;

import com.podigua.visark.server.admin.service.AdminService;
import com.podigua.visark.server.cluster.entity.Cluster;
import com.podigua.visark.server.data.entity.Message;
import com.podigua.visark.server.option.entity.Option;
import com.podigua.visark.server.topic.entity.TopicOption;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author: podigua
 * @create: 2021-09-12 23:12
 **/
@Slf4j
public class KafkaSizeReceive<K, V> extends AbstractKafkaReceive {

    private final Cluster cluster;
    private final TopicOption topicOption;
    private final String groupId;
    private final KafkaConsumer<K, V> consumer;
    private final Long size;

    public KafkaSizeReceive(Option option, Cluster cluster, TopicOption topicOption, AdminService adminService, String offset, Long size) {
        this.cluster = cluster;
        this.topicOption = topicOption;
        this.size = size;
        this.groupId = "visark";
        consumer = createConsumer(cluster.getBootstrapServers(), option.getTimeout(), groupId, offset, size, topicOption.getKeyDeserializer(), topicOption.getValueDeserializer());
    }

    /**
     * 启动
     */
    public List<Message> start() {
        List<Message> list = new ArrayList<>();
        try {
            consumer.subscribe(Arrays.asList(topicOption.getTopic()));
            Set<TopicPartition> partitions = new HashSet<>();
            while (partitions.size() == 0) {
                consumer.poll(Duration.ofMillis(100L));
                partitions = consumer.assignment();
            }
            Map<TopicPartition, Long> endOffsets = consumer.endOffsets(partitions);
            Map<TopicPartition, Long> startOffsets = consumer.beginningOffsets(partitions);
            for (TopicPartition partition : partitions) {
                Long startOffset = getOffset(partition.partition(), startOffsets);
                Long endOffset = getOffset(partition.partition(), endOffsets);
                if (endOffset - startOffset == 0) {
                    continue;
                }
                Long start = startOffset;
                Long diff = endOffset - startOffset;
                Long realSize = size;
                if (diff > size) {
                    start = endOffset - size;
                } else {
                    realSize = diff;
                }
                consumer.seek(partition, start);
                AtomicInteger count = new AtomicInteger();
               wait: while (true) {
                    ConsumerRecords<K, V> records = consumer.poll(Duration.ofMillis(100));
                    for (ConsumerRecord record : records) {
                        list.add(buildMessage(record));
                        if (count.incrementAndGet() == realSize.intValue()) {
                            break wait;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            consumer.close();
        }
        return list;
    }

    private static Long getOffset(int partition, Map<TopicPartition, Long> offsets) {
        for (Map.Entry<TopicPartition, Long> entry : offsets.entrySet()) {
            if (partition == entry.getKey().partition()) {
                return entry.getValue();
            }
        }
        return 0L;
    }

    public static interface Callback {
        /**
         * 完成
         */
        void complete();
    }
}
