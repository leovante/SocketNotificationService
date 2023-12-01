package com.nlmk.adp.config;

import org.springframework.lang.Nullable;
import org.springframework.messaging.simp.user.DefaultUserDestinationResolver;
import org.springframework.messaging.simp.user.SimpUserRegistry;

/**
 * UserDestinationResolverImpl.
 */
public class UserDestinationResolverImpl extends DefaultUserDestinationResolver {

    /**
     * UserDestinationResolverImpl.
     *
     * @param userRegistry userRegistry
     */
    public UserDestinationResolverImpl(SimpUserRegistry userRegistry) {
        super(userRegistry);
    }

    /**
     * getTargetDestination.
     *
     * @param sourceDestination sourceDestination
     * @param actualDestination actualDestination
     * @param sessionId sessionId
     * @param user user
     * @return String
     */
    @Nullable
    protected String getTargetDestination(String sourceDestination,
                                          String actualDestination,
                                          String sessionId,
                                          @Nullable String user) {
        return actualDestination;
    }

}