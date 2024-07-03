package com.xfrog.platform.application.authserver.service.impl;

import com.xfrog.framework.common.DateTimeUtils;
import com.xfrog.platform.application.authserver.dto.SessionInformationDTO;
import com.xfrog.platform.application.authserver.dto.UserDetailsDTO;
import com.xfrog.platform.application.authserver.event.SessionAuthorizationEvent;
import com.xfrog.platform.application.authserver.event.SessionExpiredEvent;
import com.xfrog.platform.application.authserver.repository.OAuth2SessionsRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.core.log.LogMessage;
import org.springframework.security.core.session.AbstractSessionEvent;
import org.springframework.security.core.session.SessionDestroyedEvent;
import org.springframework.security.core.session.SessionIdChangedEvent;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.session.SessionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JDBCSessionRegistry implements SessionRegistry, ApplicationListener<AbstractSessionEvent> {
    protected final Log logger = LogFactory.getLog(JDBCSessionRegistry.class);
    private final OAuth2SessionsRepository sessionsRepository;
    private final OAuth2AuthorizationService authorizationService;
    private final SessionRepository requestSessionRepository;
    private volatile MessageDigest md = null;

    @Override
    public List<Object> getAllPrincipals() {
        return sessionsRepository.getAllPrincipals().stream().map(it -> (Object) it).toList();
    }

    @Override
    public List<SessionInformation> getAllSessions(Object principal, boolean includeExpiredSessions) {
        // 其他认证方式下，不记录Session
        if (!(principal instanceof UserDetailsDTO)) {
            return new LinkedList<>();
        }
        UserDetailsDTO userDetailsDTO = (UserDetailsDTO) principal;
        return sessionsRepository.getAllSessions(userDetailsDTO.getUserId(), includeExpiredSessions);
    }

    @Override
    public SessionInformationDTO getSessionInformation(String sessionId) {
        return sessionsRepository.getSession(sessionId);
    }

    public SessionInformationDTO getSessionInformationBySessionIdHash(String sessionIdHash) throws NoSuchAlgorithmException {
        return sessionsRepository.getSessionBySessionIdHash(sessionIdHash);
    }

    @Override
    public void onApplicationEvent(AbstractSessionEvent event) {
        if (event instanceof SessionDestroyedEvent sessionDestroyedEvent) {
            String sessionId = sessionDestroyedEvent.getId();
            removeSessionInformation(sessionId);
        }
        else if (event instanceof SessionIdChangedEvent sessionIdChangedEvent) {
            String oldSessionId = sessionIdChangedEvent.getOldSessionId();
            SessionInformationDTO sessionInformationDTO = getSessionInformation(oldSessionId);
            if (sessionInformationDTO != null) {
                removeSessionInformation(oldSessionId);
                registerNewSession(sessionIdChangedEvent.getNewSessionId(), sessionInformationDTO.getPrincipal());
            }
        }
        else if (event instanceof SessionExpiredEvent sessionExpiredEvent) {
            SessionInformationDTO sessionInformation = sessionExpiredEvent.getSessionInformation();
            if (StringUtils.hasText(sessionInformation.getAuthorizationId())) {
                OAuth2Authorization authorization = authorizationService.findById(sessionInformation.getAuthorizationId());
                if (authorization != null) {
                    authorizationService.remove(authorization);
                }
            }
            requestSessionRepository.deleteById(sessionInformation.getSessionId());
            sessionsRepository.expiredSession(sessionExpiredEvent.getSessionInformation().getSessionId());
        } else if (event instanceof SessionAuthorizationEvent authenticatedEvent) {
            OAuth2Authorization.Token<OidcIdToken> idToken = authenticatedEvent.getAuthorization().getToken(OidcIdToken.class);
            if (idToken == null) {
                return;
            }
            if (idToken.getToken() != null && idToken.getToken().hasClaim("sid")) {
                String sessionIdHash = idToken.getToken().getClaimAsString("sid");
                if (StringUtils.hasText(sessionIdHash)) {
                    sessionsRepository.relatedAuthorization(sessionIdHash, authenticatedEvent.getAuthorization().getId());
                }
            }
        }
    }

    @Override
    public void refreshLastRequest(String sessionId) {
        Assert.hasText(sessionId, "SessionId required as per interface contract");
        sessionsRepository.updateSessionLastRequest(sessionId, DateTimeUtils.utcNowDate());
    }

    @SneakyThrows
    @Override
    @Transactional
    public void registerNewSession(String sessionId, Object principal) {
        Assert.hasText(sessionId, "SessionId required as per interface contract");
        Assert.notNull(principal, "Principal required as per interface contract");
        if (!(principal instanceof UserDetailsDTO)) {
            return;
        }

        if (this.logger.isDebugEnabled()) {
            this.logger.debug(LogMessage.format("Registering session %s, for principal %s", sessionId, principal));
        }
        UserDetailsDTO userDetailsDTO = (UserDetailsDTO) principal;
        Long principalId = sessionsRepository.savePrincipal(userDetailsDTO);

        sessionsRepository.saveSession(principalId, userDetailsDTO.getUserId(), createHash(sessionId), new SessionInformation(principal, sessionId, DateTimeUtils.utcNowDate()));
    }

    @Override
    public void removeSessionInformation(String sessionId) {
        SessionInformationDTO sessionInformation = getSessionInformation(sessionId);
        sessionsRepository.removeSession(sessionId);

        if (Boolean.FALSE.equals(sessionsRepository.hasSession(sessionInformation.getUserId()))) {
            sessionsRepository.removePrincipal(sessionInformation.getUserId());
        }
    }

    private String createHash(String value) throws NoSuchAlgorithmException {
        if (md == null) {
            synchronized (this) {
                if (md == null) {
                    md = MessageDigest.getInstance("SHA-256");
                }
            }
        }
        byte[] digest = md.digest(value.getBytes(StandardCharsets.US_ASCII));
        return Base64.getUrlEncoder().withoutPadding().encodeToString(digest);
    }
}
