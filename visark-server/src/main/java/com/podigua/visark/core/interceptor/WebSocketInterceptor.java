package com.podigua.visark.core.interceptor;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.lang.NonNull;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;
import java.util.UUID;

/**
 * @author: podigua
 * @create: 2021-09-12 23:12
 **/
public class WebSocketInterceptor implements HandshakeInterceptor {
    @Override
    public boolean beforeHandshake(@NonNull ServerHttpRequest serverHttpRequest,
                                   @NonNull ServerHttpResponse serverHttpResponse,
                                   @NonNull WebSocketHandler webSocketHandler,
                                   @NonNull Map<String, Object> map) {
        if (serverHttpRequest instanceof ServletServerHttpRequest) {
            String uuid = UUID.randomUUID().toString().replace("-", "");
            map.put("uuid", uuid);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void afterHandshake(@NonNull ServerHttpRequest serverHttpRequest, @NonNull ServerHttpResponse serverHttpResponse, @NonNull WebSocketHandler webSocketHandler, Exception e) {

    }
}
