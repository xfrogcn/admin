package com.xfrog.platform.application.resourceserver;

import com.xfrog.platform.application.authserver.service.impl.JDBCSessionRegistry;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

// 自定义的令牌校验器，令牌强制失效等功能可在此处实现
@Component
@RequiredArgsConstructor
@Slf4j
public class CustomerOAuth2TokenValidator implements OAuth2TokenValidator<Jwt> {

    private final JDBCSessionRegistry sessionRegistry;

    @SneakyThrows
    @Override
    public OAuth2TokenValidatorResult validate(Jwt token) {
        String sessionIdHash = token.getClaim("sid");
        if (StringUtils.hasText(sessionIdHash)) {
            SessionInformation sessionInformation = sessionRegistry.getSessionInformationBySessionIdHash(sessionIdHash);
            if (sessionInformation == null || sessionInformation.isExpired()) {
                log.info("token is invalid, sessionId: {}, token: {}", sessionIdHash, token.getTokenValue());
                return OAuth2TokenValidatorResult.failure(new OAuth2Error("invalid_token", "The token is invalid", null));
            }
        }
        return OAuth2TokenValidatorResult.success();
    }
}