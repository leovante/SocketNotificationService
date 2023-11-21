package com.nlmk.adp.services.handler;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.SubProtocolCapable;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * SessionHandler.
 */
public class SessionHandler extends TextWebSocketHandler implements SubProtocolCapable {

    private final Map<String, WebSocketSession> idToActiveSession = new HashMap<>();

    /**
     * afterConnectionEstablished.
     *
     * @param session session
     * @throws Exception Exception
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        idToActiveSession.put(session.getId(), session);
        super.afterConnectionEstablished(session);
    }

    /**
     * handleTextMessage.
     *
     * @param session session
     * @param message message
     * @throws Exception Exception
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        for (Map.Entry<String, WebSocketSession> otherSession : idToActiveSession.entrySet()) {
            if (otherSession.getKey().equals(session.getId())) {
                continue;
            }
            otherSession.getValue().sendMessage(new TextMessage(payload));
        }
    }

    /**
     * afterConnectionClosed.
     *
     * @param session session
     * @param status status
     * @throws Exception Exception
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        idToActiveSession.remove(session.getId());
        super.afterConnectionClosed(session, status);
    }

    /**
     * getSubProtocols.
     *
     * @return List
     */
    @Override
    public List<String> getSubProtocols() {
        return Collections.singletonList("subprotocol.demo.websocket");
    }

}
