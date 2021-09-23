package com.podigua.visark.server.data.service.impl;

import com.podigua.visark.core.utils.ExecutorUtils;
import com.podigua.visark.core.utils.UUIDUtils;
import com.podigua.visark.server.admin.service.AdminService;
import com.podigua.visark.server.cluster.entity.Cluster;
import com.podigua.visark.server.data.entity.Message;
import com.podigua.visark.server.option.entity.Option;
import com.podigua.visark.server.topic.entity.TopicOption;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

/**
 * @author: podigua
 * @create: 2021-09-12 23:12
 **/
@Slf4j
public class KafkaTimelyReceive<K, V> extends AbstractKafkaReceive {
    private final AtomicBoolean running = new AtomicBoolean(false);

    private final Cluster cluster;
    private final TopicOption topicOption;
    private final String groupId;
    private final KafkaConsumer<K, V> consumer;
    private final ExecutorService executorService = ExecutorUtils.single();
    private final AdminService adminService;

    public KafkaTimelyReceive(Option option, Cluster cluster, TopicOption topicOption, AdminService adminService, String offset) {
        this.cluster = cluster;
        this.topicOption = topicOption;
        this.adminService = adminService;
        this.groupId = "visark-" + UUIDUtils.uuid();
        consumer = createConsumer(cluster.getBootstrapServers(), option.getTimeout(), groupId, offset, 500L, topicOption.getKeyDeserializer(), topicOption.getValueDeserializer());
        List<String> topics = new ArrayList<>();
        topics.add(topicOption.getTopic());
        consumer.subscribe(Arrays.asList(topicOption.getTopic()));
    }

    /**
     * 启动
     *
     * @param messageConsumer
     */
    public void start(Consumer<Message> messageConsumer) {
        log.info("启动任务:{},{}", cluster.getName(), topicOption.getTopic());
        running.set(true);
        executorService.execute(() -> {
            try {
                while (running.get()) {
                    ConsumerRecords<K, V> records = consumer.poll(Duration.ofMillis(500L));
                    records.forEach((record) -> {
                        send(record, messageConsumer);
                    });
                }
            } catch (WakeupException e) {
                if (running.get()) {
                    throw e;
                }
            } finally {
                consumer.close();
                adminService.deleteConsumer(cluster.getId(), groupId);
                executorService.shutdown();
            }
        });
    }

    /**
     * 停止
     */
    public void stop() {
        if (running.get()) {
            log.info("停止任务:{},{}", cluster.getName(), topicOption.getTopic());
            running.set(Boolean.FALSE);
            consumer.wakeup();
        }
    }
}
