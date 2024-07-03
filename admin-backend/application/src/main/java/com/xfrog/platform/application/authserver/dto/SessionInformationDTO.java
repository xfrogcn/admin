package com.xfrog.platform.application.authserver.dto;

import com.xfrog.framework.common.EventPublisher;
import com.xfrog.platform.application.authserver.event.SessionExpiredEvent;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.session.SessionInformation;

import java.util.Date;


@Getter
@Setter
public class SessionInformationDTO extends SessionInformation {

    private Long userId;
    private Long principalId;
    private String authorizationId;

    public SessionInformationDTO(Object principal, String sessionId, Date lastRequest) {
        super(principal, sessionId, lastRequest);
    }

    public SessionInformationDTO(Object principal, String sessionId, Date lastRequest, Boolean expired) {
        super(principal, sessionId, lastRequest);
        if (Boolean.TRUE.equals(expired)) {
            super.expireNow();
        }
    }

    @Override
    public void expireNow() {
        super.expireNow();
        EventPublisher.publishEvent(new SessionExpiredEvent(this));
    }
}
