package com.xfrog.platform.application.authserver.granttype;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.log.LogMessage;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2ErrorAuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class OAuth2PasswordTokenEndpointFilter extends OncePerRequestFilter {
    private final AuthenticationManager authenticationManager;
    private final RequestMatcher tokenEndpointMatcher;
    private final AuthenticationConverter authenticationConverter;
    private AuthenticationSuccessHandler authenticationSuccessHandler = this::onAuthenticationSuccess;
    private AuthenticationFailureHandler authenticationFailureHandler = new OAuth2ErrorAuthenticationFailureHandler();

    private static final String DEFAULT_ERROR_URI = "https://datatracker.ietf.org/doc/html/rfc6749#section-5.2";
    public OAuth2PasswordTokenEndpointFilter(AuthenticationManager authenticationManager, String tokenEndpointUri) {
        this.authenticationManager = authenticationManager;
        this.tokenEndpointMatcher = new AntPathRequestMatcher(tokenEndpointUri, HttpMethod.POST.name());
        this.authenticationConverter = new OAuth2PasswordAuthenticationConverter();
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (!this.tokenEndpointMatcher.matches(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String[] grantTypes = request.getParameterValues(OAuth2ParameterNames.GRANT_TYPE);
            if (grantTypes == null || grantTypes.length != 1) {
                throwError(OAuth2ErrorCodes.INVALID_REQUEST, OAuth2ParameterNames.GRANT_TYPE);
            }

            Authentication authorizationGrantAuthentication = this.authenticationConverter.convert(request);
            if (authorizationGrantAuthentication == null) {
                // 不需要此步骤处理，继续下一个过滤器
                filterChain.doFilter(request, response);
                return;
            }

            Authentication authenticationResult = this.authenticationManager.authenticate(authorizationGrantAuthentication);
            this.authenticationSuccessHandler.onAuthenticationSuccess(request, response, authenticationResult);

            filterChain.doFilter(request, response);

        } catch (OAuth2AuthenticationException ex) {
            SecurityContextHolder.clearContext();
            if (this.logger.isTraceEnabled()) {
                this.logger.trace(LogMessage.format("Token request failed: %s", ex.getError()), ex);
            }
            this.authenticationFailureHandler.onAuthenticationFailure(request, response, ex);
        }
    }

    private void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                         Authentication authentication) {

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);
        if (this.logger.isDebugEnabled()) {
            this.logger.debug(LogMessage.format("Set SecurityContextHolder authentication to %s",
                    authentication.getClass().getSimpleName()));
        }
    }

    private static void throwError(String errorCode, String parameterName) {
        OAuth2Error error = new OAuth2Error(errorCode, "OAuth 2.0 Parameter: " + parameterName, DEFAULT_ERROR_URI);
        throw new OAuth2AuthenticationException(error);
    }
}
