package com.xfrog.platform.application.authserver.event;

import org.springframework.security.core.session.AbstractSessionEvent;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;

public class SessionAuthorizationEvent extends AbstractSessionEvent {
    public SessionAuthorizationEvent(OAuth2Authorization source) {
        super(source);
    }

    public OAuth2Authorization getAuthorization() {
        return (OAuth2Authorization) getSource();
    }
}
