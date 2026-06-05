package com.hotelandinoplaza.sistemagestion;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Canal de entrada: Mensajes enviados desde el cliente React hacia el backend
        config.setApplicationDestinationPrefixes("/app");

        // Canal de salida: Eventos que emite el backend y a los que React se suscribe
        config.enableSimpleBroker("/topic");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Punto de conexión de red inicial para establecer el saludo (handshake)
        registry.addEndpoint("/ws-hotel")
                .setAllowedOriginPatterns("*") // 🔥 Reemplazo seguro para evitar bloqueos CORS con SockJS
                .withSockJS();
    }
}