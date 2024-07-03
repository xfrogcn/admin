package com.xfrog.platform.application.authserver.service.impl;

import com.xfrog.platform.application.authserver.converter.RegisteredClientDTOConverter;
import com.xfrog.platform.application.authserver.dto.RegisteredClientDTO;
import com.xfrog.platform.application.authserver.repository.ClientsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RegisteredClientServiceImpl implements RegisteredClientRepository {

    private final ClientsRepository clientsRepository;

    @Override
    public void save(RegisteredClient registeredClient) {
        RegisteredClientDTO registeredClientDTO = RegisteredClientDTOConverter.INSTANCE.toDTO(registeredClient);
        clientsRepository.save(registeredClientDTO);
    }

    @Override
    public RegisteredClient findById(String id) {
        RegisteredClientDTO registeredClientDTO = clientsRepository.findById(Long.parseLong(id));
        return RegisteredClientDTOConverter.INSTANCE.toEntity(registeredClientDTO);
    }

    @Override
    public RegisteredClient findByClientId(String clientId) {
        RegisteredClientDTO registeredClientDTO = clientsRepository.findByClientId(clientId);
        return RegisteredClientDTOConverter.INSTANCE.toEntity(registeredClientDTO);
    }
}
