package com.xfrog.platform.application.authserver.granttype;

import com.xfrog.platform.application.authserver.service.impl.UserLastLoginTimeUpdater;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.web.OAuth2TokenEndpointFilter;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.util.matcher.RequestMatcher;


public class OAuth2PasswordTokenEndpointConfigurer
        extends AbstractHttpConfigurer<OAuth2PasswordTokenEndpointConfigurer, HttpSecurity> {
    private AuthenticationManager authenticationManager;
    private RequestMatcher requestMatcher;
    private AuthenticationConverter authenticationConverter;
    private final UserLastLoginTimeUpdater userLastLoginTimeUpdater;

    public OAuth2PasswordTokenEndpointConfigurer(UserLastLoginTimeUpdater userLastLoginTimeUpdater) {
        this.userLastLoginTimeUpdater = userLastLoginTimeUpdater;
    }


    @Override
    public void init(HttpSecurity httpSecurity) throws Exception {
        UserDetailsService userDetailsService = getUserDetailsService(httpSecurity);
        OAuth2AuthorizationService authorizationService = getAuthorizationService(httpSecurity);

        OAuth2PasswordAuthenticationProvider passwordAuthenticationProvider = new OAuth2PasswordAuthenticationProvider(authorizationService, userLastLoginTimeUpdater);
        passwordAuthenticationProvider.setUserDetailsService(userDetailsService);

        httpSecurity.authenticationProvider(passwordAuthenticationProvider);
    }

    @Override
    public void configure(HttpSecurity httpSecurity) throws Exception {
        AuthenticationManager authenticationManager = httpSecurity.getSharedObject(AuthenticationManager.class);
        AuthorizationServerSettings authorizationServerSettings = getAuthorizationServerSettings(httpSecurity);

        OAuth2PasswordTokenEndpointFilter tokenEndpointFilter =
                new OAuth2PasswordTokenEndpointFilter(
                        authenticationManager,
                        authorizationServerSettings.getTokenEndpoint());

        httpSecurity.addFilterBefore(postProcess(tokenEndpointFilter), OAuth2TokenEndpointFilter.class);
    }

    static OAuth2AuthorizationService  getAuthorizationService(HttpSecurity httpSecurity) {
        OAuth2AuthorizationService auth2AuthorizationService = httpSecurity.getSharedObject(OAuth2AuthorizationService.class);
        if (auth2AuthorizationService == null) {
            auth2AuthorizationService = getBean(httpSecurity, OAuth2AuthorizationService.class);
            httpSecurity.setSharedObject(OAuth2AuthorizationService.class, auth2AuthorizationService);
        }
        return auth2AuthorizationService;
    }

    static UserDetailsService getUserDetailsService(HttpSecurity httpSecurity) {
        UserDetailsService userDetailsService = httpSecurity.getSharedObject(UserDetailsService.class);
        if (userDetailsService == null) {
            userDetailsService = getBean(httpSecurity, UserDetailsService.class);
            httpSecurity.setSharedObject(UserDetailsService.class, userDetailsService);
        }
        return userDetailsService;
    }

    static AuthorizationServerSettings getAuthorizationServerSettings(HttpSecurity httpSecurity) {
        AuthorizationServerSettings authorizationServerSettings = httpSecurity.getSharedObject(AuthorizationServerSettings.class);
        if (authorizationServerSettings == null) {
            authorizationServerSettings = getBean(httpSecurity, AuthorizationServerSettings.class);
            httpSecurity.setSharedObject(AuthorizationServerSettings.class, authorizationServerSettings);
        }
        return authorizationServerSettings;
    }

    static <T> T getBean(HttpSecurity httpSecurity, Class<T> type) {
        return httpSecurity.getSharedObject(ApplicationContext.class).getBean(type);
    }
}
