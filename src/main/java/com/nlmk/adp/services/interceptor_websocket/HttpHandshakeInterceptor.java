package com.nlmk.adp.services.interceptor_websocket;

import lombok.extern.slf4j.Slf4j;
import org.keycloak.KeycloakSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.session.MapSession;
import org.springframework.session.Session;
import org.springframework.session.SessionRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.servlet.http.HttpSession;
import java.util.Map;

import static java.util.Optional.ofNullable;

@Slf4j
@Component
public class HttpHandshakeInterceptor implements HandshakeInterceptor {

    private static final String SPRING_SESSION_ID_ATTR_NAME = "SPRING.SESSION.ID";
    private static final String CURRENT_SESSION = "org.springframework.session.SessionRepository.CURRENT_SESSION";


    @Override
    public boolean beforeHandshake(ServerHttpRequest request,
                                   ServerHttpResponse response,
                                   WebSocketHandler wsHandler,
                                   Map attributes) {
        if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;

//            var ctx2 = (KeycloakSecurityContext) servletRequest.getServletRequest().getAttribute(KeycloakSecurityContext.class.getName());
//            var token = ctx2.getToken();

/*            HttpSession session = servletRequest.getServletRequest().getSession();

            ofNullable(attributes.get(SPRING_SESSION_ID_ATTR_NAME))
                    .orElseGet(() -> attributes.put(SPRING_SESSION_ID_ATTR_NAME, session.getId()));
            ofNullable(attributes.get(CURRENT_SESSION))
                    .orElseGet(() -> attributes.put(CURRENT_SESSION, session.getId()));*/

            log.debug("beforeHandshake session established");
        }
        return true;
    }

    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
                               Exception ex) {
    }

}
