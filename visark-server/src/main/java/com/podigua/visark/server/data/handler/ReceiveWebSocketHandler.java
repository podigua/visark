package com.podigua.visark.server.data.handler;

import com.podigua.visark.server.data.service.KafkaReceiveService;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

/**
 * @author: podigua
 * @create: 2021-09-12 23:12
 **/
@Service
@Slf4j
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ReceiveWebSocketHandler implements WebSocketHandler {
    private final KafkaReceiveService kafkaReceiveService;

    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession webSocketSession) throws Exception {
        kafkaReceiveService.init(webSocketSession);
    }

    @Override
    public void handleMessage(@NonNull WebSocketSession webSocketSession, WebSocketMessage<?> webSocketMessage) throws Exception {

    }

    @Override
    public void handleTransportError(@NonNull WebSocketSession webSocketSession, @NonNull Throwable throwable) throws Exception {
        kafkaReceiveService.close(webSocketSession);
    }

    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession webSocketSession, @NonNull CloseStatus closeStatus) throws Exception {
        kafkaReceiveService.close(webSocketSession);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
