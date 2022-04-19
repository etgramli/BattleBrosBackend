package de.etgramli.battlebros.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;


/**
 * WebSocket controller to provide bidirectional communication to the web client.
 */
@Configuration
@EnableWebSocketMessageBroker
public class GameWebsocketController implements WebSocketMessageBrokerConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(GameWebsocketController.class);

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        final String brokerUrl = "/topic";
        registry.enableSimpleBroker(brokerUrl);  // Url to subscribe to (+ sub-urls)
        final String applicationPrefix = "/app";
        registry.setApplicationDestinationPrefixes(applicationPrefix);
        logger.info("Websocket controller configured to use broker url \"%s\" and application prefix \"%s\""
                .formatted(brokerUrl, applicationPrefix));
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/stomp")
                .setAllowedOrigins("http://localhost:8080", "http://localhost:8089")
                .withSockJS();
        logger.info("Set up STOMP endpoint.");
    }
}
