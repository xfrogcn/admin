package com.xfrog.platform.application.authserver.event;

import com.xfrog.framework.common.EventPublisher;
import com.xfrog.framework.oplog.OpLogger;
import com.xfrog.framework.oplog.OperationActionConstants;
import com.xfrog.platform.application.authserver.constant.AuthServerOperationLogConstants;
import com.xfrog.platform.application.authserver.dto.UserDetailsDTO;
import com.xfrog.platform.application.authserver.service.impl.UserLastLoginTimeUpdater;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AbstractAuthenticationEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.oidc.authentication.OidcLogoutAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class AuthenticationEventListener implements ApplicationListener<AbstractAuthenticationEvent> {

    private final SessionRegistry sessionRegistry;

    private final OAuth2AuthorizationService authorizationService;

    private final UserLastLoginTimeUpdater userLastLoginTimeUpdater;

    private final ObjectProvider<OpLogger> opLoggerProvider;

    @Override
    public void onApplicationEvent(AbstractAuthenticationEvent event) {
        if (event instanceof AuthenticationSuccessEvent authenticationSuccessEvent) {
            if (authenticationSuccessEvent.getAuthentication() instanceof OidcLogoutAuthenticationToken authenticationToken) {
                String sessionId = authenticationToken.getSessionId();
                if (StringUtils.hasText(sessionId)) {
                    SessionInformation sessionInformation = sessionRegistry.getSessionInformation(sessionId);
                    if (sessionInformation != null) {
                        sessionInformation.expireNow();
                    }
                }

                OpLogger opLogger = opLoggerProvider.getIfAvailable();
                if (opLogger != null) {
                    UsernamePasswordAuthenticationToken userLogin = (UsernamePasswordAuthenticationToken) authenticationToken.getPrincipal();
                    if (userLogin != null) {
                        UserDetailsDTO userDetailsDTO = (UserDetailsDTO) userLogin.getPrincipal();
                        if (userDetailsDTO != null) {
                            opLogger.success(
                                    userDetailsDTO.getUserId(),
                                    AuthServerOperationLogConstants.OP_TYPE_AUTH,
                                    AuthServerOperationLogConstants.BIZ_TYPE_AUTH,
                                    OperationActionConstants.LOGOUT,
                                    String.valueOf(userDetailsDTO.getUserId()),
                                    userDetailsDTO.getUsername());
                        }
                    }
                }

            } else if (authenticationSuccessEvent.getAuthentication() instanceof OAuth2AccessTokenAuthenticationToken accessTokenAuthenticationToken) {
                // 关联Session与Authorization
                OAuth2Authorization authorization = authorizationService.findByToken(accessTokenAuthenticationToken.getAccessToken().getTokenValue(), OAuth2TokenType.ACCESS_TOKEN);
                if (authorization == null) {
                    return;
                }
                EventPublisher.publishEvent(new SessionAuthorizationEvent(authorization));
            } else if (authenticationSuccessEvent.getAuthentication() instanceof UsernamePasswordAuthenticationToken token) {
                UserDetailsDTO userDetailsDTO = (UserDetailsDTO) token.getPrincipal();
                if (userDetailsDTO != null) {
                    userLastLoginTimeUpdater.updateLastLoginTime(userDetailsDTO.getUserId());
                }
            }
        }
    }
}
