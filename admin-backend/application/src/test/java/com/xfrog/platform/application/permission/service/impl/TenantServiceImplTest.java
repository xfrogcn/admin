package com.xfrog.platform.application.permission.service.impl;

import com.xfrog.framework.exception.business.AlreadyExistsException;
import com.xfrog.framework.exception.business.FailedPreconditionException;
import com.xfrog.framework.exception.business.NotFoundException;
import com.xfrog.platform.application.permission.api.dto.CreateOrganizationRequestDTO;
import com.xfrog.platform.application.permission.api.dto.CreateRoleRequestDTO;
import com.xfrog.platform.application.permission.api.dto.CreateTenantRequestDTO;
import com.xfrog.platform.application.permission.api.dto.CreateUserRequestDTO;
import com.xfrog.platform.application.permission.api.dto.PermissionItemDTO;
import com.xfrog.platform.application.permission.api.dto.QueryTenantRequestDTO;
import com.xfrog.platform.application.permission.api.dto.UpdateTenantRequestDTO;
import com.xfrog.platform.application.permission.dto.PermissionDTOFixtures;
import com.xfrog.platform.application.permission.repository.TenantRepository;
import com.xfrog.platform.application.permission.service.DataScopeService;
import com.xfrog.platform.application.permission.service.OrganizationService;
import com.xfrog.platform.application.permission.service.PermissionItemService;
import com.xfrog.platform.application.permission.service.RoleService;
import com.xfrog.platform.application.permission.service.UserService;
import com.xfrog.platform.domain.permission.aggregate.PermissionFixtures;
import com.xfrog.platform.domain.permission.aggregate.Tenant;
import com.xfrog.platform.domain.permission.repository.TenantDomainRepository;
import com.xfrog.platform.domain.permission.repository.UserDomainRepository;
import com.xfrog.platform.domain.share.permission.DataScopeTargetType;
import com.xfrog.platform.domain.share.permission.DataScopeType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TenantServiceImplTest {
    @Mock
    private TenantDomainRepository tenantDomainRepository;
    @Mock
    private UserDomainRepository userDomainRepository;
    @Mock
    private TenantRepository tenantRepository;

    @Mock
    private OrganizationService organizationService;
    @Mock
    private UserService userService;
    @Mock
    private PermissionItemService permissionItemService;
    @Mock
    private RoleService roleService;
    @Mock
    private DataScopeService dataScopeService;

    @InjectMocks
    private TenantServiceImpl tenantService;

    @Test
    void createTenant_ShouldThrowAlreadyExistsException_WhenTenantAlreadyExists() {
        CreateTenantRequestDTO requestDTO = PermissionDTOFixtures.defaultCreateTenantRequestDTO().build();
        Tenant tenant = PermissionFixtures.createDefaultTenant().build();


        when(tenantDomainRepository.findByCode(requestDTO.getCode())).thenReturn(tenant);
        assertThrows(AlreadyExistsException.class, () -> tenantService.createTenant(requestDTO));
    }

    @Test
    void createTenant_ShouldThrowFailedPreconditionException_WhenAdminUserNameAlreadyExists() {
        CreateTenantRequestDTO requestDTO = PermissionDTOFixtures.defaultCreateTenantRequestDTO().build();

        when(tenantDomainRepository.findByCode(requestDTO.getCode())).thenReturn(null);
        when(userDomainRepository.findByUserName(requestDTO.getAdminUserName())).thenReturn(PermissionFixtures.createDefaultUser().build());
        assertThrows(FailedPreconditionException.class, () -> tenantService.createTenant(requestDTO));
    }

    @Test
    void createTenant_ShouldSuccess() {
        CreateTenantRequestDTO requestDTO = PermissionDTOFixtures.defaultCreateTenantRequestDTO().build();

        when(tenantDomainRepository.findByCode(requestDTO.getCode())).thenReturn(null);
        when(userDomainRepository.findByUserName(requestDTO.getAdminUserName())).thenReturn(null);

        // Simulate successful creation and return a mock Tenant ID
        when(tenantDomainRepository.save(any(Tenant.class))).thenReturn(PermissionFixtures.createDefaultTenant().build());
        when(organizationService.createOrganization(any(CreateOrganizationRequestDTO.class)))
                .thenReturn(1L);
        when(userService.createUser(any(CreateUserRequestDTO.class)))
                .thenReturn(1L);
        when(roleService.createRole(any(CreateRoleRequestDTO.class)))
                .thenReturn(2L);
        when(permissionItemService.listPermissionItems(false))
                .thenReturn(List.of(
                        PermissionItemDTO.builder()
                                .id(3L)
                                .code("test")
                                .platform(false)
                                .build()
                ));


        Long tenantId = tenantService.createTenant(requestDTO);


        // Verify interactions with the repository
        verify(organizationService, times(1))
                .createOrganization(argThat(request -> request.getName().equals(requestDTO.getName())
                        && request.getParentId() == null
                ));
        verify(userService, times(1))
                .createUser(argThat(request -> request.getUserName().equals(requestDTO.getAdminUserName())
                        && request.getOrganizationId() == 1L
                ));
        verify(roleService, times(1)).createRole(any(CreateRoleRequestDTO.class));
        verify(permissionItemService, times(1)).listPermissionItems(false);
        verify(roleService, times(1)).grantPermissionItems(2L, List.of(3L));
        verify(dataScopeService, times(1))
                .grantDataScope(argThat(request -> request.getScopeItems().get(0).getScopeType() == DataScopeType.USER_ORGANIZATION
                        && request.getScopeItems().get(0).getScopeId() == 0L
                        && request.getTargetType() == DataScopeTargetType.ROLE
                        && request.getTargetId() == 2L
                ));
        verify(userService, times(1))
                .grantRoles(1L, List.of(2L));

        verify(tenantDomainRepository, times(1))
                .save(argThat(domain -> domain.getName().equals(requestDTO.getName())
                        && domain.getCode().equals(requestDTO.getCode())
                        && domain.getEnabled().equals(requestDTO.getEnabled())
                        && domain.getMemo().equals(requestDTO.getMemo())
                        && domain.getAdminUserName().equals(requestDTO.getAdminUserName())
                        && domain.getAdminUserId() == 1L
                        && domain.getOrganizationId() == 1L
                ));
    }

    @Test
    void listTenants_ShouldSuccessfully() {
        QueryTenantRequestDTO requestDTO = QueryTenantRequestDTO.builder().build();
        tenantService.listTenants(requestDTO);
        verify(tenantRepository, times(1)).queryBy(requestDTO);
    }

    @Test
    void updateTenant_SuccessfullyUpdates() {
        // Arrange
        UpdateTenantRequestDTO updateTenantRequestDTO = PermissionDTOFixtures.defaultUpdateTenantRequestDTO()
                .name("updated_name")
                .memo("updated_memo")
                .build();

        Tenant tenant = PermissionFixtures.createDefaultTenant().build();

        when(tenantDomainRepository.findById(tenant.getId())).thenReturn(tenant);

        // Act
        tenantService.updateTenant(tenant.getId(), updateTenantRequestDTO);

        // Assert
        verify(tenantDomainRepository, times(1))
                .save(argThat(domain -> domain.getName().equals(updateTenantRequestDTO.getName())
                        && domain.getMemo().equals(updateTenantRequestDTO.getMemo())
                ));
    }

    @Test
    void updateTenant_ThrowsNotFoundExceptionWhenTenantNotFound() {
        // Arrange
        UpdateTenantRequestDTO updateTenantRequestDTO = PermissionDTOFixtures.defaultUpdateTenantRequestDTO()
                .build();

        when(tenantDomainRepository.findById(1L)).thenReturn(null);

        // Act & Assert
        assertThrows(NotFoundException.class, () -> tenantService.updateTenant(1L, updateTenantRequestDTO));
    }

    @Test
    void enableTenant_ShouldThrowNotFoundExceptionWhenTenantNotFound() {
        // Given
        when(tenantDomainRepository.findById(1L)).thenReturn(null);

        // When & Then
        assertThrows(NotFoundException.class, () -> tenantService.enableTenant(1L, true));
    }

    @Test
    void enableTenantShouldUpdateTenantStatusWhenFound() {
        // Given
        Tenant tenant = PermissionFixtures.createDefaultTenant().enabled(false).build();

        when(tenantDomainRepository.findById(tenant.getId())).thenReturn(tenant);

        // When
        tenantService.enableTenant(tenant.getId(), true);

        // Then
        verify(tenantDomainRepository, times(1))
                .save(argThat(Tenant::getEnabled));
    }
}