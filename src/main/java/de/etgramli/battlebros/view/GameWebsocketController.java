package de.etgramli.battlebros.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.lang.NonNull;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;
import java.util.UUID;


/**
 * WebSocket controller to provide bidirectional communication to the web client.
 */
@Configuration
@EnableWebSocketMessageBroker
public class GameWebsocketController implements WebSocketMessageBrokerConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(GameWebsocketController.class);
    public static final String APPLICATION_PREFIX = "/app";
    public static final String USER_PREFIX = "/user";
    public static final String BROKER_PREFIX = "/topic";

    @Override
    public void configureMessageBroker(@NonNull MessageBrokerRegistry registry) {
        registry.enableSimpleBroker(BROKER_PREFIX);
        registry.setApplicationDestinationPrefixes(APPLICATION_PREFIX);
        registry.setUserDestinationPrefix(USER_PREFIX);
        logger.info(String.format(
                "Websocket controller configured to use broker url \"%s\" and application prefix \"%s\" and user destination prefix: \"%s\"",
                BROKER_PREFIX, APPLICATION_PREFIX, USER_PREFIX));
    }

    @Override
    public void registerStompEndpoints(@NonNull StompEndpointRegistry registry) {
        registry.addEndpoint("/stomp")
                .setAllowedOrigins("http://localhost:8080", "http://localhost:8089")
                .setHandshakeHandler(new CustomHandshakeHandler())
                .withSockJS();
        logger.info("Set up STOMP endpoint.");
    }

    private static class CustomHandshakeHandler extends DefaultHandshakeHandler {
        @NonNull
        @Override
        protected Principal determineUser(@NonNull final ServerHttpRequest request,
                                          @NonNull final WebSocketHandler webSocketHandler,
                                          @NonNull final Map<String, Object> attributes) {
            return new StompPrincipal(UUID.randomUUID().toString());
        }
    }
}
