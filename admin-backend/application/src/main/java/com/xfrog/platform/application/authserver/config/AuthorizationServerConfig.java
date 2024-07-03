package com.xfrog.platform.application.authserver.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.xfrog.platform.application.authserver.granttype.OAuth2FakeAuthorizationCodeAuthenticationConverter;
import com.xfrog.platform.application.authserver.granttype.OAuth2PasswordTokenEndpointConfigurer;
import com.xfrog.platform.application.authserver.granttype.SPAPublicClientAuthenticationConverter;
import com.xfrog.platform.application.authserver.granttype.SPAPublicClientAuthenticationProvider;
import com.xfrog.platform.application.authserver.service.impl.UserLastLoginTimeUpdater;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;
import org.springframework.util.StringUtils;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.UUID;

@Configuration
@EnableJdbcHttpSession(tableName = "oauth2_request_sessions")
public class AuthorizationServerConfig {

    // 是否开启SPA应用验证支持
    @Value("${admin.auth-server.enableSpaOAuth: true}")
    boolean enableSpaOAuth;

    // 是否开启密码认证流程
    @Value("${admin.auth-server.enablePasswordGrant: false}")
    boolean enablePasswordGrant;

    // 用户最大可登录的会话数量
    @Value("${admin.auth-server.concurrentSessionCount: -1}")
    Integer concurrentSessionCount;

    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Bean
    @Order(1)
    public SecurityFilterChain loginSecurityFilterChain(HttpSecurity http) throws Exception {
        // 需要csrf
        http.securityMatcher("/login");
        // 跳转到 /login
        http.formLogin(httpSecurityFormLoginConfigurer -> {
            httpSecurityFormLoginConfigurer.loginPage("/login")
                    .loginProcessingUrl("/login");
        });
        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain staticSecurityFilterChain() {
        RequestMatcher requestMatcher = new OrRequestMatcher(
                AntPathRequestMatcher.antMatcher("/login/**"),
                AntPathRequestMatcher.antMatcher("/error"),
                AntPathRequestMatcher.antMatcher("/favicon.ico"));
        return new DefaultSecurityFilterChain(requestMatcher);
    }

    @Bean
    @Order(10)
    public SecurityFilterChain authorizationServerSecurityFilterChain(
            HttpSecurity http,
            UserDetailsService userDetailsService,
            UserLastLoginTimeUpdater userLastLoginTimeUpdater,
            RegisteredClientRepository registeredClientRepository,
            OAuth2AuthorizationService authorizationService) throws Exception {

        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
        http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
                .tokenEndpoint(oAuth2TokenEndpointConfigurer -> {
                    oAuth2TokenEndpointConfigurer.accessTokenRequestConverter(new OAuth2FakeAuthorizationCodeAuthenticationConverter());
                })
                .oidc(oAuth2OidcConfigurer -> {
                    // 登出处理，忽略invalid_token异常，总是跳转到指定的url
                    oAuth2OidcConfigurer.logoutEndpoint(oidcLogoutEndpointConfigurer -> {
                       oidcLogoutEndpointConfigurer.errorResponseHandler(this::alwaysRedirect);
                    });
                })
                .clientAuthentication(oAuth2ClientAuthenticationConfigurer -> {
                    if (enableSpaOAuth) {
                        oAuth2ClientAuthenticationConfigurer.authenticationConverter(new SPAPublicClientAuthenticationConverter());
                        oAuth2ClientAuthenticationConfigurer.authenticationProvider(
                                new SPAPublicClientAuthenticationProvider(registeredClientRepository, authorizationService));
                    }
                });

        http.sessionManagement(httpSecuritySessionManagementConfigurer -> {
            httpSecuritySessionManagementConfigurer.maximumSessions(concurrentSessionCount);
        });

        if (enablePasswordGrant) {
            http.with(new OAuth2PasswordTokenEndpointConfigurer(userLastLoginTimeUpdater), Customizer.withDefaults())
                    .oauth2ResourceServer(oAuth2ResourceServerConfigurer -> oAuth2ResourceServerConfigurer.jwt(Customizer.withDefaults()));
        }
        http.cors(httpSecurityCorsConfigurer -> {
            CorsConfiguration corsConfiguration = new CorsConfiguration();
            corsConfiguration.setAllowCredentials(false);
            corsConfiguration.addAllowedOrigin("*");
            corsConfiguration.addAllowedHeader("*");
            corsConfiguration.addAllowedMethod("*");

            UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
            urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);

            httpSecurityCorsConfigurer.configurationSource(urlBasedCorsConfigurationSource);
        })
        // 跳转到 /login
        .formLogin(Customizer.withDefaults());

        return http.build();
    }

    private void alwaysRedirect(HttpServletRequest request, HttpServletResponse response,
                                   AuthenticationException exception) throws IOException {
        // 如果是invalid_token
        OAuth2Error error = ((OAuth2AuthenticationException) exception).getError();
        if ("invalid_token".equals(error.getErrorCode())) {
            String uri = request.getParameter("post_logout_redirect_uri");
            String state = request.getParameter("state");
            UriComponentsBuilder uriBuilder = UriComponentsBuilder
                    .fromUriString(uri);
            String redirectUri;
            if (StringUtils.hasText(state)) {
                uriBuilder.queryParam(
                        OAuth2ParameterNames.STATE,
                        UriUtils.encode(state, StandardCharsets.UTF_8));
            }
            redirectUri = uriBuilder.build(true).toUriString();        // build(true) -> Components are explicitly encoded
            redirectStrategy.sendRedirect(request, response, redirectUri);
            return;
        }

        response.sendError(HttpStatus.BAD_REQUEST.value(), error.toString());
    }


    @Bean
    public JwtEncoder jwtEncoder() throws CertificateException, IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        return new NimbusJwtEncoder(jwkSource());
    }

    @Bean
    public OAuth2AuthorizationService oAuth2AuthorizationService(
            JdbcOperations jdbcOperations,
            RegisteredClientRepository registeredClientRepository) {
        return new JdbcOAuth2AuthorizationService(jdbcOperations, registeredClientRepository);
    }

    private JWKSource<SecurityContext> jwkSource() throws CertificateException, IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        RSAKey rsaKey = getRsaKey();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return new ImmutableJWKSet<>(jwkSet);
    }

    private RSAKey getRsaKey() throws CertificateException, IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        KeyPair keyPair = readRsaKey();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        return new RSAKey.Builder(publicKey).privateKey(privateKey)
                .keyID(UUID.randomUUID().toString())
                .build();
    }

    private KeyPair readRsaKey() throws CertificateException, IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        ClassPathResource publicKeyResource = new ClassPathResource("/auth/public.cert");
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        InputStream publicKeyInputStream = publicKeyResource.getInputStream();
        X509Certificate certificate = (X509Certificate) certificateFactory.generateCertificate(publicKeyInputStream);
        PublicKey publicKey = certificate.getPublicKey();

        // Load private key from PEM file
        ClassPathResource privateKeyResource = new ClassPathResource("/auth/private.key");
        String privateKeyPEM = new String(privateKeyResource.getInputStream().readAllBytes())
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s+", "");
        byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyPEM);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

        return new KeyPair(publicKey, privateKey);
    }
}
