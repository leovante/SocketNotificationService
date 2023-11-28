package com.nlmk.adp.services.interceptor.websocket;

import java.util.Map;
import java.util.Optional;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.session.MapSession;
import org.springframework.session.SessionRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

/**
 * HttpHandshakeInterceptor.
 */
@Slf4j
@Component
public class HttpHandshakeInterceptor implements HandshakeInterceptor {

    private static final String SPRING_SESSION_ID_ATTR_NAME = "SPRING.SESSION.ID";

    private static final String CURRENT_SESSION = "org.springframework.session.SessionRepository.CURRENT_SESSION";

    @Autowired
    private SessionRepository<MapSession> repository;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
                                   Map attributes) {
        if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;

            var context = SecurityContextHolder.getContext();

            //      var ctx2 = (KeycloakSecurityContext) servletRequest.getServletRequest()
            //                       .getAttribute(KeycloakSecurityContext.class.getName());
            // var token = ctx2.getToken();

            HttpSession session = servletRequest.getServletRequest().getSession();

            Optional.ofNullable(attributes.get(SPRING_SESSION_ID_ATTR_NAME))
                    .orElseGet(() -> attributes.put(SPRING_SESSION_ID_ATTR_NAME, session.getId()));
            Optional.ofNullable(attributes.get(CURRENT_SESSION))
                    .orElseGet(() -> attributes.put(CURRENT_SESSION, session.getId()));

            repository.save(new MapSession(session.getId()));
            log.debug("websocket session established");

        }
        return true;
    }

    /**
     * afterHandshake.
     *
     * @param request
     *         the current request
     * @param response
     *         the current response
     * @param wsHandler
     *         the target WebSocket handler
     * @param ex
     *         an exception raised during the handshake, or {@code null} if none
     */
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
                               Exception ex) {
    }

}
