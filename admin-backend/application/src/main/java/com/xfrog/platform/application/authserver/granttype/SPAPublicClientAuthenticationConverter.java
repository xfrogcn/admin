package com.xfrog.platform.application.authserver.granttype;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class SPAPublicClientAuthenticationConverter implements AuthenticationConverter {

    public static final String SPA_PUBLIC_CLIENT_METHOD= "spa_token";

    @Override
    public Authentication convert(HttpServletRequest request) {

        MultiValueMap<String, String> parameters = OAuth2PasswordAuthenticationConverter.getParameters(request);
        String grantType = parameters.getFirst(OAuth2ParameterNames.GRANT_TYPE);
        if (!OAuth2ParameterNames.REFRESH_TOKEN.equals(grantType)) {
            return null;
        }

        String clientId = parameters.getFirst(OAuth2ParameterNames.CLIENT_ID);
        if (!StringUtils.hasText(clientId)
                || parameters.get(OAuth2ParameterNames.CLIENT_ID).size() != 1) {
            throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_REQUEST);
        }

        parameters.remove(OAuth2ParameterNames.CLIENT_ID);

        Map<String, Object> additionalParameters = new HashMap<>();
        parameters.forEach((key, value) ->
                additionalParameters.put(key, (value.size() == 1) ? value.get(0) : value.toArray(new String[0])));

        return new OAuth2ClientAuthenticationToken(clientId, new ClientAuthenticationMethod(SPA_PUBLIC_CLIENT_METHOD), null,
                additionalParameters);
    }
}
