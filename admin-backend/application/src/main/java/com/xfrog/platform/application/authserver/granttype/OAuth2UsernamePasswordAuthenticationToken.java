package com.xfrog.platform.application.authserver.granttype;

import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

@Getter
public class OAuth2UsernamePasswordAuthenticationToken extends UsernamePasswordAuthenticationToken {

    private Authentication clientPrincipal;
    private Set<String> scopes;
    private String uri;
    private Map<String, Object> additionalParameters;

    public OAuth2UsernamePasswordAuthenticationToken(Object principal, Object credentials,
                                                     Authentication clientPrincipal,
                                                     Set<String> scopes,
                                                     String uri,
                                                     Map<String, Object> additionalParameters) {
        super(principal, credentials);
        this.clientPrincipal = clientPrincipal;
        this.scopes =scopes;
        this.uri = uri;
        this.additionalParameters = additionalParameters;
    }

    public OAuth2UsernamePasswordAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }

}