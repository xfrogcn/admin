package com.xfrog.platform.application.authserver.dto;

import com.xfrog.framework.dto.AuditDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class RegisteredClientDTO extends AuditDTO {
    private String clientId;
    private LocalDateTime clientIdIssuedAt;
    private String clientSecret;
    private LocalDateTime clientSecretExpiresAt;
    private String clientName;
    private String clientAuthenticationMethods;
    private String authorizationGrantTypes;
    private String redirectUris;
    private String postLogoutRedirectUris;
    private String scopes;
    private String clientSettings;
    private String tokenSettings;
}
