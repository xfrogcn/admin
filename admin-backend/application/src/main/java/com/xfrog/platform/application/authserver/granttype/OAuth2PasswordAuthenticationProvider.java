package com.xfrog.platform.application.authserver.granttype;

import com.xfrog.platform.application.authserver.dto.UserDetailsDTO;
import com.xfrog.platform.application.authserver.service.impl.UserLastLoginTimeUpdater;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.keygen.Base64StringKeyGenerator;
import org.springframework.security.crypto.keygen.StringKeyGenerator;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationCode;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeRequestAuthenticationException;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeRequestAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.util.StringUtils;

import java.security.Principal;
import java.time.Instant;
import java.util.Base64;


public class OAuth2PasswordAuthenticationProvider extends DaoAuthenticationProvider {

    private final StringKeyGenerator authorizationCodeGenerator =
            new Base64StringKeyGenerator(Base64.getUrlEncoder().withoutPadding(), 96);
    private final OAuth2AuthorizationService authorizationService;
    private final UserLastLoginTimeUpdater userLastLoginTimeUpdater;

    public OAuth2PasswordAuthenticationProvider(
            OAuth2AuthorizationService authorizationService,
            UserLastLoginTimeUpdater userLastLoginTimeUpdater) {
        super();
        this.authorizationService = authorizationService;
        this.userLastLoginTimeUpdater = userLastLoginTimeUpdater;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (!(authentication instanceof OAuth2UsernamePasswordAuthenticationToken)) {
            throwError(OAuth2ErrorCodes.INVALID_GRANT, OAuth2ParameterNames.PASSWORD);
        }
        OAuth2UsernamePasswordAuthenticationToken passwordAuthenticationToken = (OAuth2UsernamePasswordAuthenticationToken) authentication;
        OAuth2ClientAuthenticationToken clientAuthenticationToken = (OAuth2ClientAuthenticationToken) passwordAuthenticationToken.getClientPrincipal();
        if (clientAuthenticationToken == null || clientAuthenticationToken.getRegisteredClient() == null || !clientAuthenticationToken.isAuthenticated()) {
            throwError(OAuth2ErrorCodes.INVALID_CLIENT, OAuth2ParameterNames.CLIENT_SECRET);
        }

        UsernamePasswordAuthenticationToken principal = (UsernamePasswordAuthenticationToken) super.authenticate(authentication);
        if (!principal.isAuthenticated()) {
            return principal;
        }

        // 模拟code验证流程
        RegisteredClient registeredClient = clientAuthenticationToken.getRegisteredClient();

        OAuth2AuthorizationRequest authorizationRequest = OAuth2AuthorizationRequest.authorizationCode()
                .clientId(registeredClient.getClientId())
                .authorizationRequestUri(passwordAuthenticationToken.getUri())
                .authorizationUri(passwordAuthenticationToken.getUri())
                //   .redirectUri(authorizationCodeRequestAuthentication.getRedirectUri())
                .scopes(passwordAuthenticationToken.getScopes())
                // .state(authorizationCodeRequestAuthentication.getState())
                .additionalParameters(passwordAuthenticationToken.getAdditionalParameters())
                .build();


        OAuth2AuthorizationCode authorizationCode = generateAuthorizationCode(registeredClient);
        if (authorizationCode == null) {
            OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR,
                    "The token generator failed to generate the authorization code.", null);
            throw new OAuth2AuthorizationCodeRequestAuthenticationException(error, null);
        }

        OAuth2Authorization authorization = authorizationBuilder(registeredClient, principal, authorizationRequest)
                .authorizedScopes(authorizationRequest.getScopes())
                .token(authorizationCode)
                .build();
        this.authorizationService.save(authorization);

        if (this.logger.isTraceEnabled()) {
            this.logger.trace("Saved authorization");
        }

        String redirectUri = authorizationRequest.getRedirectUri();
        if (!StringUtils.hasText(redirectUri)) {
            redirectUri = registeredClient.getRedirectUris().iterator().next();
        }

        // 更新用户最后登录时间
        UserDetailsDTO userDetailsDTO = (UserDetailsDTO) principal.getPrincipal();
        if (userDetailsDTO != null && userLastLoginTimeUpdater != null) {
            userLastLoginTimeUpdater.updateLastLoginTime(userDetailsDTO.getUserId());
        }

        if (this.logger.isTraceEnabled()) {
            this.logger.trace("Authenticated authorization code request");
        }

        return new OAuth2AuthorizationCodeRequestAuthenticationToken(authorizationRequest.getAuthorizationUri(),
                registeredClient.getClientId(), clientAuthenticationToken, authorizationCode, redirectUri,
                authorizationRequest.getState(), authorizationRequest.getScopes());
    }

    private static OAuth2Authorization.Builder authorizationBuilder(RegisteredClient registeredClient, Authentication principal,
                                                                    OAuth2AuthorizationRequest authorizationRequest) {
        return OAuth2Authorization.withRegisteredClient(registeredClient)
                .principalName(principal.getName())
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .attribute(Principal.class.getName(), principal)
                .attribute(OAuth2AuthorizationRequest.class.getName(), authorizationRequest);
    }

    public OAuth2AuthorizationCode generateAuthorizationCode(RegisteredClient client) {
        Instant issuedAt = Instant.now();
        Instant expiresAt = issuedAt.plus(client.getTokenSettings().getAuthorizationCodeTimeToLive());
        return new OAuth2AuthorizationCode(this.authorizationCodeGenerator.generateKey(), issuedAt, expiresAt);
    }

    private static void throwError(String errorCode, String parameterName) {
        OAuth2Error error = new OAuth2Error(errorCode, "OAuth 2.0 Parameter: " + parameterName, null);
        throw new OAuth2AuthenticationException(error);
    }
}

