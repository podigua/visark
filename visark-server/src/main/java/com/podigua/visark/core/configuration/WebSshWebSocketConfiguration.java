package com.podigua.visark.core.configuration;

import com.podigua.visark.core.interceptor.WebSocketInterceptor;
import com.podigua.visark.server.data.handler.ReceiveWebSocketHandler;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * @author: podigua
 * @create: 2021-09-12 23:12
 **/
@Configuration
@EnableWebSocket
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class WebSshWebSocketConfiguration implements WebSocketConfigurer {

    private final ReceiveWebSocketHandler receiveWebSocketHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
        webSocketHandlerRegistry.addHandler(receiveWebSocketHandler, "/receive")
                .addInterceptors(new WebSocketInterceptor())
                .setAllowedOrigins("*");
    }
}
