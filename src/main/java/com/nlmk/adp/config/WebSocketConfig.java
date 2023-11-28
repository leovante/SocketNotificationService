package com.nlmk.adp.config;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nlmk.adp.services.interceptor_websocket.HttpHandshakeInterceptor;
import com.nlmk.adp.services.interceptor_websocket.UserInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.messaging.converter.DefaultContentTypeResolver;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.session.Session;
import org.springframework.session.web.socket.config.annotation.AbstractSessionWebSocketMessageBrokerConfigurer;
import org.springframework.session.web.socket.server.SessionRepositoryMessageInterceptor;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import com.nlmk.adp.services.interceptor.websocket.HttpHandshakeInterceptor;
import com.nlmk.adp.services.interceptor.websocket.UserInterceptor;
import java.security.Principal;
import java.util.List;
import java.util.Map;


/**
 * WebSocketConfig.
 */
@Configuration
@EnableWebSocketMessageBroker
@Order(Ordered.HIGHEST_PRECEDENCE + 50)
public class WebSocketConfig extends AbstractSessionWebSocketMessageBrokerConfigurer<Session> {

    @Autowired
    private ObjectMapper objectMapper;

    //    @Autowired
    //    private SessionRepositoryMessageInterceptor sessionRepositoryMessageInterceptor;

    @Autowired
    private HttpHandshakeInterceptor handshakeInterceptor;

    @Autowired
    private UserInterceptor userInterceptor;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
    }

    @Override
    protected void configureStompEndpoints(StompEndpointRegistry stompEndpointRegistry) {
        stompEndpointRegistry
                .addEndpoint("/ws/websocket")
//                .addInterceptors(handshakeInterceptor)
//                .addInterceptors(sessionRepositoryMessageInterceptor)
                /*.setHandshakeHandler(new DefaultHandshakeHandler(){//попытка создать юзера для метода simpUserRegistry
                    @Override
                    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
                        if (request instanceof ServletServerHttpRequest r) {
                            var session = r.getServletRequest().getSession(false);
                            return session == null ? null : null *//*new StompPrincipal(session.getId())*//*;
                        }
                        return super.determineUser(request, wsHandler, attributes);
                    }
                })*/
                .setAllowedOriginPatterns("*")
//                .withSockJS()
//                .setWebSocketEnabled(false)
//                .setSessionCookieNeeded(false)
        ;
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
        registration.interceptors(userInterceptor);
    }

}
