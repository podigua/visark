package com.podigua.visark.server.data.service;

import org.springframework.web.socket.WebSocketSession;

/**
 * @author: podigua
 * @create: 2021-09-13 10:51
 **/
public interface KafkaReceiveService {
    /**
     * init
     * @param session
     */
    void init(WebSocketSession session);

    /**
     * 关闭
     * @param  session
     */
    void close(WebSocketSession session);
}
