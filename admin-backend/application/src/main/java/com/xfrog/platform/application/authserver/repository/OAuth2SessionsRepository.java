package com.xfrog.platform.application.authserver.repository;

import com.xfrog.platform.application.authserver.dto.SessionInformationDTO;
import com.xfrog.platform.application.authserver.dto.UserDetailsDTO;
import org.springframework.security.core.session.SessionInformation;

import java.util.Date;
import java.util.List;

public interface OAuth2SessionsRepository {
    Long savePrincipal(UserDetailsDTO principal);

    Long saveSession(Long principalId, Long userId, String sessionIdHash, SessionInformation sessionInformation);

    void updateSessionLastRequest(String sessionId, Date lastRequest);

    void expiredSession(String sessionId);

    SessionInformationDTO getSession(String sessionId);

    SessionInformationDTO getSessionBySessionIdHash(String sessionIdHash);

    Boolean hasSession(Long userId);

    void removeSession(String sessionId);

     void removePrincipal(Long userId);

    List<UserDetailsDTO> getAllPrincipals();

    List<SessionInformation> getAllSessions(Long userId, boolean includeExpiredSessions);

    void relatedAuthorization(String sessionIdHash, String authorizationId);
}
