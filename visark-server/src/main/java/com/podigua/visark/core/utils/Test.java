package com.podigua.visark.core.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.*;

/**
 * @author: podigua
 * @create: 2021-09-15 19:24
 **/
@Slf4j
public class Test {
    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "10.1.9.120:9092");
        properties.put(ConsumerConfig.REQUEST_TIMEOUT_MS_CONFIG, "10000");
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "test22222");
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        properties.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        properties.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "500");
        properties.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, "3000000");

        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        Long size = 100L;
        KafkaConsumer consumer = new KafkaConsumer(properties);
        List<PartitionInfo> partitionInfos = consumer.partitionsFor("dataSource001");
        List<TopicPartition> partitions = new ArrayList<>();
        for (PartitionInfo info : partitionInfos) {
            partitions.add(new TopicPartition(info.topic(), info.partition()));
        }
        Map<TopicPartition, Long> endOffsets = consumer.endOffsets(partitions);
        Map<TopicPartition, Long> startOffsets = consumer.beginningOffsets(partitions);

        consumer.subscribe(Arrays.asList("dataSource001"));
        for (TopicPartition partition : partitions) {
            Long startOffset = getOffset(partition.partition(), startOffsets);
            Long endOffset = getOffset(partition.partition(), endOffsets);
            if (endOffset - startOffset == 0) {
                continue;
            }
            Long diff = endOffset - startOffset;
            Long start = startOffset;
            if (diff > size) {
                start = endOffset - size;
            }
            consumer.poll(Duration.ofSeconds(100));
            consumer.seek(partition, start);
            ConsumerRecords records = consumer.poll(Duration.ofSeconds(100));
            for (Object record : records) {
                System.out.println(record);
            }
        }
    }

    private static Long getOffset(int partition, Map<TopicPartition, Long> offsets) {
        for (Map.Entry<TopicPartition, Long> entry : offsets.entrySet()) {
            if (partition == entry.getKey().partition()) {
                return entry.getValue();
            }
        }
        return null;
    }
}
