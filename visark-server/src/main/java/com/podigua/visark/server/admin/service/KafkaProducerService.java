package com.podigua.visark.server.admin.service;

import com.podigua.visark.server.admin.entity.KafkaMessage;

/**
 * @author: podigua
 * @create: 2021-09-24 16:59
 **/
public interface KafkaProducerService {
    /**
     * 发送消息
     *
     * @param id      集群ID
     * @param topic   主题
     * @param message 消息
     */
    void send(String id, String topic, KafkaMessage message);
}
