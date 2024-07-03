package com.xfrog.platform.application.authserver.converter;

import com.xfrog.framework.common.DateTimeUtils;
import com.xfrog.framework.common.JsonHelper;
import com.xfrog.platform.application.authserver.dto.RegisteredClientDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.ConfigurationSettingNames;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.util.CollectionUtils;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper
public abstract class RegisteredClientDTOConverter {
    public static RegisteredClientDTOConverter INSTANCE = Mappers.getMapper(RegisteredClientDTOConverter.class);
    private static final String DEFAULT_DELIMITER = ",";
    private static final List<String> TTL_SETTING_NAMES = List.of(
            ConfigurationSettingNames.Token.ACCESS_TOKEN_TIME_TO_LIVE,
            ConfigurationSettingNames.Token.REFRESH_TOKEN_TIME_TO_LIVE,
            ConfigurationSettingNames.Token.DEVICE_CODE_TIME_TO_LIVE,
            ConfigurationSettingNames.Token.AUTHORIZATION_CODE_TIME_TO_LIVE
    );

    public RegisteredClientDTO toDTO(RegisteredClient registeredClient) {
        if (registeredClient == null) {
            return null;
        }
        RegisteredClientDTO.RegisteredClientDTOBuilder<?, ?> builder = RegisteredClientDTO.builder()
                .clientId(registeredClient.getClientId())
                .clientSecret(registeredClient.getClientSecret())
                .clientIdIssuedAt(DateTimeUtils.toLocalDateTime(registeredClient.getClientIdIssuedAt()))
                .clientSecretExpiresAt(DateTimeUtils.toLocalDateTime(registeredClient.getClientSecretExpiresAt()))
                .clientName(registeredClient.getClientName())
                .id(Long.parseLong(registeredClient.getId()))
                .clientSettings(JsonHelper.serialize(registeredClient.getClientSettings() == null ? null : registeredClient.getClientSettings().getSettings()))
                .tokenSettings(JsonHelper.serialize(registeredClient.getTokenSettings() == null ? null : registeredClient.getTokenSettings().getSettings()));

        if (!CollectionUtils.isEmpty(registeredClient.getRedirectUris())) {
            builder.redirectUris(String.join(DEFAULT_DELIMITER, registeredClient.getRedirectUris()));
        }
        if (!CollectionUtils.isEmpty(registeredClient.getPostLogoutRedirectUris())) {
            builder.postLogoutRedirectUris(String.join(DEFAULT_DELIMITER, registeredClient.getPostLogoutRedirectUris()));
        }
        if (!CollectionUtils.isEmpty(registeredClient.getScopes())) {
            builder.scopes(String.join(DEFAULT_DELIMITER, registeredClient.getScopes()));
        }
        if (!CollectionUtils.isEmpty(registeredClient.getAuthorizationGrantTypes())) {
            builder.authorizationGrantTypes(registeredClient.getAuthorizationGrantTypes().stream()
                    .map(AuthorizationGrantType::getValue).collect(Collectors.joining(DEFAULT_DELIMITER)));
        }
        if (!CollectionUtils.isEmpty(registeredClient.getClientAuthenticationMethods())) {
            builder.clientAuthenticationMethods(registeredClient.getClientAuthenticationMethods().stream()
                    .map(ClientAuthenticationMethod::getValue).collect(Collectors.joining(DEFAULT_DELIMITER)));
        }

        return builder.build();
    }

    public RegisteredClient toEntity(RegisteredClientDTO registeredClientDTO) {
        if (registeredClientDTO == null) {
            return null;
        }
        RegisteredClient.Builder builder = RegisteredClient.withId(registeredClientDTO.getId().toString())
                .clientId(registeredClientDTO.getClientId())
                .clientSecret(registeredClientDTO.getClientSecret())
                .clientIdIssuedAt(DateTimeUtils.toInstant(registeredClientDTO.getClientIdIssuedAt()))
                .clientSecretExpiresAt(DateTimeUtils.toInstant(registeredClientDTO.getClientSecretExpiresAt()))
                .clientName(registeredClientDTO.getClientName());

        if (registeredClientDTO.getClientSettings() != null) {
            builder.clientSettings(
                    ClientSettings.builder()
                            .settings(map -> map.putAll(JsonHelper.deserializeToMap(registeredClientDTO.getClientSettings())))
                    .build());
        }
        if (registeredClientDTO.getTokenSettings() != null) {
            Map<String, Object> tokenSettings = JsonHelper.deserializeToMap(registeredClientDTO.getTokenSettings());
            TTL_SETTING_NAMES.forEach(name -> {
                Object value = tokenSettings.get(name);
                if (value != null) {
                    tokenSettings.put(name, Duration.ofSeconds(((Double) value).longValue()));
                }
            });
            Object tokenFormat = tokenSettings.get(ConfigurationSettingNames.Token.ACCESS_TOKEN_FORMAT);
            if (tokenFormat instanceof Map<?, ?> formatMap) {
                tokenSettings.put(ConfigurationSettingNames.Token.ACCESS_TOKEN_FORMAT, new OAuth2TokenFormat(String.valueOf(formatMap.get("value"))));
            }
            Object signatureAlgorithm = tokenSettings.get(ConfigurationSettingNames.Token.ID_TOKEN_SIGNATURE_ALGORITHM);
            if (signatureAlgorithm != null) {
                tokenSettings.put(ConfigurationSettingNames.Token.ID_TOKEN_SIGNATURE_ALGORITHM, SignatureAlgorithm.from(String.valueOf(signatureAlgorithm)));
            }
            builder.tokenSettings(
                    TokenSettings.builder()
                            .settings(map -> map.putAll(tokenSettings))
                    .build());
        }

        if (registeredClientDTO.getRedirectUris() != null) {
            builder.redirectUris(uris -> uris.addAll(Arrays.asList(registeredClientDTO.getRedirectUris().split(DEFAULT_DELIMITER))));
        }
        if (registeredClientDTO.getPostLogoutRedirectUris() != null) {
            builder.postLogoutRedirectUris(uris -> uris.addAll(Arrays.asList(registeredClientDTO.getPostLogoutRedirectUris().split(DEFAULT_DELIMITER))));
        }
        if (registeredClientDTO.getScopes() != null) {
            builder.scopes(scopes -> scopes.addAll(Arrays.asList(registeredClientDTO.getScopes().split(DEFAULT_DELIMITER))));
        }
        if (registeredClientDTO.getAuthorizationGrantTypes() != null) {
            builder.authorizationGrantTypes(grantTypes -> grantTypes.addAll(Arrays.stream(registeredClientDTO.getAuthorizationGrantTypes().split(DEFAULT_DELIMITER))
                    .map(AuthorizationGrantType::new).toList()));
        }
        if (registeredClientDTO.getClientAuthenticationMethods() != null) {
            builder.clientAuthenticationMethods(methods -> methods.addAll(Arrays.stream(registeredClientDTO.getClientAuthenticationMethods().split(DEFAULT_DELIMITER))
                    .map(ClientAuthenticationMethod::new).toList()));
        }

        return builder.build();
    }
}
