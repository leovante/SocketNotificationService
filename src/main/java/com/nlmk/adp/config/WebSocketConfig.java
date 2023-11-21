package com.nlmk.adp.config;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.DefaultContentTypeResolver;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.session.Session;
import org.springframework.session.web.socket.config.annotation.AbstractSessionWebSocketMessageBrokerConfigurer;
import org.springframework.session.web.socket.server.SessionRepositoryMessageInterceptor;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

import com.nlmk.adp.services.interceptor.websocket.HttpHandshakeInterceptor;
import com.nlmk.adp.services.interceptor.websocket.UserInterceptor;

/**
 * WebSocketConfig.
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractSessionWebSocketMessageBrokerConfigurer<Session> {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private SessionRepositoryMessageInterceptor sessionRepositoryMessageInterceptor;
    @Autowired
    private HttpHandshakeInterceptor handshakeInterceptor;
    @Autowired
    private UserInterceptor userInterceptor;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
        config.setUserDestinationPrefix("/user");
    }

    @Override
    protected void configureStompEndpoints(StompEndpointRegistry stompEndpointRegistry) {
        stompEndpointRegistry
                .addEndpoint("/ws/websocket")
                .addInterceptors(handshakeInterceptor)
                .addInterceptors(sessionRepositoryMessageInterceptor)
                // попытка создать юзера для метода simpUserRegistry
                /* .setHandshakeHandler(new DefaultHandshakeHandler(){
                    @Override
                    protected Principal determineUser(ServerHttpRequest request,
                                                      WebSocketHandler wsHandler,
                                                      Map<String, Object> attributes) {
                        if (request instanceof ServletServerHttpRequest r) {
                            var session = r.getServletRequest().getSession(false);
                            return session == null ? null : new StompPrincipal(session.getId());
                        }
                        return super.determineUser(request, wsHandler, attributes);
                    }
                })*/
                .setAllowedOriginPatterns("*")
                .withSockJS()
        ;
        stompEndpointRegistry.addEndpoint("/hello");
    }

    @Override
    public boolean configureMessageConverters(List<MessageConverter> messageConverters) {
        DefaultContentTypeResolver resolver = new DefaultContentTypeResolver();
        resolver.setDefaultMimeType(MimeTypeUtils.APPLICATION_JSON);
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setObjectMapper(objectMapper);
        converter.setContentTypeResolver(resolver);
        messageConverters.add(converter);
        return false;
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.setInterceptors(userInterceptor);
    }

}
