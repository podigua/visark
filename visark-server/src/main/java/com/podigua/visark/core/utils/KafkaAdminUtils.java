package com.podigua.visark.core.utils;

import com.podigua.visark.server.option.entity.Option;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.ConsumerGroupListing;
import org.apache.kafka.clients.admin.KafkaAdminClient;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.Node;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: podigua
 * @create: 2021-09-02 13:09
 **/
public class KafkaAdminUtils {
    /**
     * 创建KafkaAdmin连接
     *
     * @param bootstrapServers 地址
     * @return {@link KafkaAdminClient}
     */
    public static KafkaAdminClient create(String bootstrapServers, Option option) {
        Properties properties = new Properties();
        properties.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.put(CommonClientConfigs.REQUEST_TIMEOUT_MS_CONFIG, option.getTimeout());
        return (KafkaAdminClient) AdminClient.create(properties);
    }

    /**
     * 关闭
     *
     * @param client
     */
    public static void close(KafkaAdminClient client) {
        if (client != null) {
            client.close(Duration.ofSeconds(10L));
        }
    }

    /**
     * 获取所有节点
     *
     * @param client
     * @return
     */
    public static Collection<Node> nodes(KafkaAdminClient client) {
        try {
            return client.describeCluster().nodes().get();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    /**
     * 获取所有的topic
     *
     * @param client
     * @return
     */
    public static Set<String> topics(KafkaAdminClient client) {
        try {
            return client.listTopics().names().get();
        } catch (Exception e) {
            return Collections.emptySet();
        }
    }

    /**
     * 消费者
     *
     * @param client
     * @return
     */
    public static List<String> consumers(KafkaAdminClient client) {
        try {
            return client.listConsumerGroups().all().get().stream().map(ConsumerGroupListing::groupId).collect(Collectors.toList());
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

}
