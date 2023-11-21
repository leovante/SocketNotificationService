package com.nlmk.adp.services.handler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ParticipantRepository.
 */
public class ParticipantRepository {

    private Map<String, String> activeSessions = new ConcurrentHashMap<>();

    /**
     * add.
     *
     * @param sessionId sessionId
     * @param event event
     */
    public void add(String sessionId, String event) {
        activeSessions.put(sessionId, event);
    }

    /**
     * getParticipant.
     *
     * @param sessionId sessionId
     * @return String
     */
    public String getParticipant(String sessionId) {
        return activeSessions.get(sessionId);
    }

    /**
     * removeParticipant.
     *
     * @param sessionId sessionId
     */
    public void removeParticipant(String sessionId) {
        activeSessions.remove(sessionId);
    }

    /**
     * getActiveSessions.
     *
     * @return Map
     */
    public Map<String, String> getActiveSessions() {
        return activeSessions;
    }

    /**
     * setActiveSessions.
     *
     * @param activeSessions activeSessions
     */
    public void setActiveSessions(Map<String, String> activeSessions) {
        this.activeSessions = activeSessions;
    }

}
