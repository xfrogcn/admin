package com.xfrog.platform.application.authserver.repository;

import com.xfrog.platform.application.authserver.dto.RegisteredClientDTO;

public interface ClientsRepository {
    void save(RegisteredClientDTO registeredClient);

    RegisteredClientDTO findById(Long id);

    RegisteredClientDTO findByClientId(String clientId);
}
