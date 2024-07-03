package com.xfrog.platform.application.authserver.granttype;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeRequestAuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationConverter;

// 将之前password认证通过后的token转为 OAuth2AuthorizationCodeAuthenticationToken 以便继续走code flow
public class OAuth2FakeAuthorizationCodeAuthenticationConverter implements AuthenticationConverter {
    @Override
    public Authentication convert(HttpServletRequest request) {
        String grantType = request.getParameter(OAuth2ParameterNames.GRANT_TYPE);
        if (!AuthorizationGrantType.PASSWORD.getValue().equals(grantType)) {
            return null;
        }

        // 当前的认证信息应为 OAuth2AuthorizationCodeRequestAuthenticationToken
        // 由 OAuth2PasswordAuthenticationProvider 提供
        OAuth2AuthorizationCodeRequestAuthenticationToken codeRequestAuthenticationToken = (OAuth2AuthorizationCodeRequestAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        return new OAuth2AuthorizationCodeAuthenticationToken(
                codeRequestAuthenticationToken.getAuthorizationCode().getTokenValue(),
                (Authentication) codeRequestAuthenticationToken.getPrincipal(),
                codeRequestAuthenticationToken.getRedirectUri(),
                codeRequestAuthenticationToken.getAdditionalParameters());
    }
}
