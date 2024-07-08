package com.xfrog.platform.application.config;

import com.xfrog.platform.application.resourceserver.CurrentPrincipalInfoFilter;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class AuthorizationClientConfig {

    @Bean
    @Order(100)
    public SecurityFilterChain authorizationClientSecurityFilterChain(HttpSecurity http,
                                                                      CurrentPrincipalInfoFilter principalInfoFilter) throws Exception {
        http.oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
                .cors(httpSecurityCorsConfigurer -> {
                    CorsConfiguration corsConfiguration = new CorsConfiguration();
                    corsConfiguration.setAllowCredentials(false);
                    corsConfiguration.addAllowedOrigin("*");
                    corsConfiguration.addAllowedHeader("*");
                    corsConfiguration.addAllowedMethod("*");

                    UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
                    urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);

                    httpSecurityCorsConfigurer.configurationSource(urlBasedCorsConfigurationSource);
                })
                .authorizeHttpRequests(httpSecurityExpressionInterceptUrlRegistry -> {
                    httpSecurityExpressionInterceptUrlRegistry
                            .requestMatchers(
                                    "/error",
                                    "/swagger-ui.html", "/swagger-ui/**", "v3/api-docs/**")
                            .permitAll()
                            .anyRequest().authenticated();
                });

        http.csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.ignoringRequestMatchers("/api/**"));

        http.addFilterAfter(principalInfoFilter, BearerTokenAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder(ObjectProvider<OAuth2TokenValidator<Jwt>> additionalValidators)
            throws CertificateException, IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withPublicKey(readRsaPublicKey())
                .build();

        List<OAuth2TokenValidator<Jwt>> validators = new ArrayList<>();
        validators.add(JwtValidators.createDefault());
        validators.addAll(additionalValidators.orderedStream().toList());
        jwtDecoder.setJwtValidator(new DelegatingOAuth2TokenValidator<>(validators));
        return jwtDecoder;
    }

    private RSAPublicKey readRsaPublicKey() throws CertificateException, IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        ClassPathResource publicKeyResource = new ClassPathResource("/auth/public.cert");
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        InputStream publicKeyInputStream = publicKeyResource.getInputStream();
        X509Certificate certificate = (X509Certificate) certificateFactory.generateCertificate(publicKeyInputStream);

        return (RSAPublicKey) certificate.getPublicKey();
    }
}
