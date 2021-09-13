package com.podigua.visark.server.data.service;

import com.podigua.visark.core.utils.ExecutorUtils;
import com.podigua.visark.server.admin.service.AdminService;
import com.podigua.visark.server.cluster.entity.Cluster;
import com.podigua.visark.server.data.entity.Message;
import com.podigua.visark.server.option.entity.Option;
import com.podigua.visark.server.topic.entity.TopicOption;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.utils.Bytes;

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
public class KafkaReceive<K, V> {
    private final AtomicBoolean running = new AtomicBoolean(false);

    private final Cluster cluster;
    private final TopicOption topicOption;
    private final String groupId;
    private final KafkaConsumer<K, V> consumer;
    private final ExecutorService executorService = ExecutorUtils.single();
    private final AdminService adminService;
    public KafkaReceive(Option option, Cluster cluster, TopicOption topicOption, AdminService adminService) {
        this.cluster = cluster;
        this.topicOption = topicOption;
        this.adminService = adminService;
        this.groupId="visark-"+UUID.randomUUID().toString().replace("-","");
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, cluster.getBootstrapServers());
        properties.put(ConsumerConfig.REQUEST_TIMEOUT_MS_CONFIG, option.getTimeout());
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        properties.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        properties.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "500");
        properties.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, "3000000");
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, topicOption.getKeyDeserializer());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, topicOption.getValueDeserializer());
        consumer = new KafkaConsumer<K, V>(properties);
        List<String> topics = new ArrayList<>();
        topics.add(topicOption.getTopic());
        consumer.subscribe(topics);
    }

    /**
     * 启动
     *
     * @param messageConsumer
     */
    public void start(Consumer<Message> messageConsumer) {
        log.info("启动任务:{},{}",cluster.getName(),topicOption.getTopic());
        running.set(true);
        executorService.execute(() -> {
            try {
                while (running.get()) {
                    ConsumerRecords<K, V> records = consumer.poll(Duration.ofMillis(500L));
                    records.forEach((record) -> {
                        V value = record.value();
                        log.info("接收到消息:{}",value);
                        String message = "";
                        if (value instanceof Bytes) {
                            byte[] bytes = ((Bytes) value).get();
                            StringBuilder sb=new StringBuilder();
                            for (byte b : bytes) {
                                sb.append(b);
                            }
                            message = sb.toString();
                        } else if (value instanceof String) {
                            message = (String) value;
                        }
                        long offset = record.offset();
                        int partition = record.partition();
                        long timestamp = record.timestamp();
                        Headers headers = record.headers();
                        messageConsumer.accept(Message.create(partition, offset, message, timestamp, headers));
                    });
                }
            } catch (WakeupException e) {
                if (running.get()) {
                    throw e;
                }
            } finally {
                consumer.close();
                adminService.deleteConsumer(cluster.getId(),groupId);
                executorService.shutdown();
            }
        });
    }

    /**
     * 停止
     */
    public void stop() {
        if(running.get()){
            log.info("停止任务:{},{}",cluster.getName(),topicOption.getTopic());
            running.set(Boolean.FALSE);
            consumer.wakeup();
        }
    }
}
