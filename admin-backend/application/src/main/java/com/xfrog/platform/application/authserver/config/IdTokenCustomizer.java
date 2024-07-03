package com.xfrog.platform.application.authserver.config;

import com.xfrog.platform.application.authserver.dto.UserDetailsDTO;
import lombok.SneakyThrows;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.time.Instant;

// 自定义token claims
@Component
public class IdTokenCustomizer implements OAuth2TokenCustomizer<JwtEncodingContext> {

    private volatile MessageDigest md = null;

    @SneakyThrows
    @Override
    public void customize(JwtEncodingContext context) {
        if (context.getTokenType() == null || !context.getTokenType().getValue().equals("id_token")) {
            return;
        }
        // 过期时间与access token一致
        Instant issueAt = context.getClaims().build().getIssuedAt();
        context.getClaims().expiresAt(issueAt.plus(context.getRegisteredClient().getTokenSettings().getAccessTokenTimeToLive()));


        Authentication principal = context.getPrincipal();
        if (principal instanceof UsernamePasswordAuthenticationToken token) {
            Object userDetails = token.getPrincipal();
            if (userDetails instanceof UserDetailsDTO userDetailsDTO) {
                if (userDetailsDTO.getUserId() != null) {
                    context.getClaims().claim("uid", userDetailsDTO.getUserId().toString());
                }
                if (userDetailsDTO.getOrganizationId() != null) {
                    context.getClaims().claim("org", userDetailsDTO.getOrganizationId().toString());
                }
                if (userDetailsDTO.getTenantId() != null) {
                    context.getClaims().claim("tid", userDetailsDTO.getTenantId());
                }
            }
        }
    }
}
