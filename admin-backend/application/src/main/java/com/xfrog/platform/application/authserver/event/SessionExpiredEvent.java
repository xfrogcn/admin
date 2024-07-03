package com.xfrog.platform.application.authserver.event;

import com.xfrog.platform.application.authserver.dto.SessionInformationDTO;
import org.springframework.security.core.session.AbstractSessionEvent;

public class SessionExpiredEvent extends AbstractSessionEvent {

    public SessionExpiredEvent(SessionInformationDTO source) {
        super(source);
    }

    public SessionInformationDTO getSessionInformation() {
        return (SessionInformationDTO) getSource();
    }
}