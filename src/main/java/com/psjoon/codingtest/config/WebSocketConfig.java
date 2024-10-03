package com.psjoon.codingtest.config;

import com.psjoon.codingtest.handler.CodeWebSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new CodeWebSocketHandler(), "/ws/code")
                .setAllowedOrigins("*");  // 필요한 경우 출처 설정
    }
}
