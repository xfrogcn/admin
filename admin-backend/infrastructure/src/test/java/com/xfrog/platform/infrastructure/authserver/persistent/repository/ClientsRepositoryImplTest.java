package com.xfrog.platform.infrastructure.authserver.persistent.repository;

import com.xfrog.platform.application.authserver.dto.RegisteredClientDTO;
import com.xfrog.platform.infrastructure.authserver.mapper.ClientsMapper;
import com.xfrog.platform.infrastructure.authserver.repository.ClientsRepositoryImpl;
import com.xfrog.platform.infrastructure.persistent.BaseRepositoryTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;


@Import(ClientsRepositoryImpl.class)
class ClientsRepositoryImplTest extends BaseRepositoryTest {

    @Autowired
    private ClientsRepositoryImpl clientsRepository;

    @Autowired
    private ClientsMapper clientsMapper;

    @Test
    void clients_repository_impl_should_work_correctly() {
        RegisteredClientDTO registeredClientDTO = RegisteredClientDTO.builder()
                .clientId("1")
                .clientName("test")
                .scopes("id_token")
                .clientSecret("123")
                .authorizationGrantTypes("password")
                .redirectUris("http://localhost:8080")
                .postLogoutRedirectUris("http://localhost:8080/logout")
                .build();
        clientsRepository.save(registeredClientDTO);

        registeredClientDTO = clientsRepository.findByClientId("1");
        assertThat(registeredClientDTO).isNotNull();
        assertThat(registeredClientDTO.getCreatedBy()).isEqualTo(1L);
        assertThat(registeredClientDTO.getUpdatedBy()).isEqualTo(1L);
        assertThat(registeredClientDTO.getCreatedTime()).isNotNull();
        assertThat(registeredClientDTO.getUpdatedTime()).isNotNull();
        assertThat(registeredClientDTO.getDeleted()).isFalse();
        assertThat(registeredClientDTO.getDeletedBy()).isNull();
        assertThat(registeredClientDTO.getDeletedTime()).isNull();
        assertThat(registeredClientDTO.getClientId()).isEqualTo("1");
        assertThat(registeredClientDTO.getClientName()).isEqualTo("test");
        assertThat(registeredClientDTO.getScopes()).isEqualTo("id_token");
        assertThat(registeredClientDTO.getClientSecret()).isEqualTo("123");
        assertThat(registeredClientDTO.getAuthorizationGrantTypes()).isEqualTo("password");
        assertThat(registeredClientDTO.getRedirectUris()).isEqualTo("http://localhost:8080");
        assertThat(registeredClientDTO.getPostLogoutRedirectUris()).isEqualTo("http://localhost:8080/logout");
        assertThat(registeredClientDTO.getId()).isNotNull();


        registeredClientDTO = clientsRepository.findById(registeredClientDTO.getId());
        assertThat(registeredClientDTO).isNotNull();

    }
}