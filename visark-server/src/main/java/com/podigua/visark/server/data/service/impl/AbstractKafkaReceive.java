package com.podigua.visark.server.data.service.impl;

import com.podigua.visark.server.data.entity.Message;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.utils.Bytes;

import java.security.MessageDigest;
import java.util.Properties;
import java.util.function.Consumer;

/**
 * @author: podigua
 * @create: 2021-09-15 18:32
 **/
public class AbstractKafkaReceive {

    protected KafkaConsumer createConsumer(String bootStrapServers, Integer timeout, String groupId, String offset, Long size, String keyDeserializer, String valueDeserializer) {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootStrapServers);
        properties.put(ConsumerConfig.REQUEST_TIMEOUT_MS_CONFIG, timeout + "");
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        properties.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        properties.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, size + "");
        properties.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, "3000000");
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, offset);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, keyDeserializer);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, valueDeserializer);
        return new KafkaConsumer(properties);
    }

    protected Message buildMessage(ConsumerRecord record) {
        Object value = record.value();
        String message = "";
        if (value instanceof Bytes) {
            byte[] bytes = ((Bytes) value).get();
            StringBuilder sb = new StringBuilder();
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
        return Message.create(partition, offset, message, timestamp, headers);
    }

    protected void send(ConsumerRecord record, Consumer<Message> consumer) {
        consumer.accept(buildMessage(record));
    }
}
