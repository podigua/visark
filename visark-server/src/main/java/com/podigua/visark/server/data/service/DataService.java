package com.podigua.visark.server.data.service;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * @author: podigua
 * @create: 2021-09-12 23:08
 **/
public interface DataService {
    /**
     * 接收数据
     * @param cluster
     * @param topic
     * @param emitter
     */
    void receive(String cluster, String topic, SseEmitter emitter);
}
