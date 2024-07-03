package com.xfrog.platform.application.authserver.granttype;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.authentication.PublicClientAuthenticationProvider;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;

// 支持向SPA前端应用返回refresh_token。
public class SPAPublicClientAuthenticationProvider implements AuthenticationProvider {
    private final RegisteredClientRepository registeredClientRepository;
    private final OAuth2AuthorizationService authorizationService;
    private static final String ERROR_URI = "https://datatracker.ietf.org/doc/html/rfc6749#section-3.2.1";
    private final PublicClientAuthenticationProvider publicClientAuthenticationProvider;

    public SPAPublicClientAuthenticationProvider(RegisteredClientRepository registeredClientRepository,
                                              OAuth2AuthorizationService authorizationService) {
        this.registeredClientRepository =registeredClientRepository;
        this.authorizationService = authorizationService;
        this.publicClientAuthenticationProvider = new PublicClientAuthenticationProvider(registeredClientRepository, authorizationService);
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        OAuth2ClientAuthenticationToken clientAuthentication =
                (OAuth2ClientAuthenticationToken) authentication;

        if (ClientAuthenticationMethod.NONE.equals(clientAuthentication.getClientAuthenticationMethod())) {
            // spa前端应用通过code认证时，需要改变默认为none的认证方式，否则不会生成refresh token
            OAuth2ClientAuthenticationToken pub = (OAuth2ClientAuthenticationToken) publicClientAuthenticationProvider.authenticate(authentication);
            if (pub == null) {
                return pub;
            }
            return new OAuth2ClientAuthenticationToken(pub.getRegisteredClient(),
                    new ClientAuthenticationMethod(SPAPublicClientAuthenticationConverter.SPA_PUBLIC_CLIENT_METHOD), null);
        }

        if (!SPAPublicClientAuthenticationConverter.SPA_PUBLIC_CLIENT_METHOD.equals(clientAuthentication.getClientAuthenticationMethod().getValue())) {
            return null;
        }

        String clientId = clientAuthentication.getPrincipal().toString();
        RegisteredClient registeredClient = this.registeredClientRepository.findByClientId(clientId);
        if (registeredClient == null) {
            throwInvalidClient(OAuth2ParameterNames.CLIENT_ID);
        }

        if (!registeredClient.getClientAuthenticationMethods().contains(
                clientAuthentication.getClientAuthenticationMethod())) {
            throwInvalidClient("authentication_method");
        }

        return new OAuth2ClientAuthenticationToken(registeredClient,
                clientAuthentication.getClientAuthenticationMethod(), null);
    }

    private static void throwInvalidClient(String parameterName) {
        OAuth2Error error = new OAuth2Error(
                OAuth2ErrorCodes.INVALID_CLIENT,
                "Client authentication failed: " + parameterName,
                ERROR_URI
        );
        throw new OAuth2AuthenticationException(error);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return OAuth2ClientAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
