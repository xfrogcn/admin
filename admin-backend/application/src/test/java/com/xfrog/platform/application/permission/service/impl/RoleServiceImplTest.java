package com.xfrog.platform.application.permission.service.impl;

import com.xfrog.framework.exception.business.AlreadyExistsException;
import com.xfrog.framework.exception.business.FailedPreconditionException;
import com.xfrog.framework.exception.business.NotFoundException;
import com.xfrog.platform.application.permission.api.dto.CreateRoleRequestDTO;
import com.xfrog.platform.application.permission.api.dto.UpdateRoleRequestDTO;
import com.xfrog.platform.application.permission.dto.PermissionDTOFixtures;
import com.xfrog.platform.application.permission.repository.RoleRepository;
import com.xfrog.platform.application.permission.repository.UserRepository;
import com.xfrog.platform.domain.permission.aggregate.PermissionFixtures;
import com.xfrog.platform.domain.permission.aggregate.Role;
import com.xfrog.platform.domain.permission.aggregate.RolePermissionItem;
import com.xfrog.platform.domain.permission.repository.RoleDomainRepository;
import com.xfrog.platform.domain.permission.repository.RolePermissionItemDomainRepository;
import com.xfrog.platform.domain.permission.repository.UserRoleDomainRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoleServiceImplTest {
    @Mock
    private RoleDomainRepository roleDomainRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private UserRoleDomainRepository userRoleDomainRepository;
    @Mock
    private RolePermissionItemDomainRepository rolePermissionItemDomainRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private RoleServiceImpl roleService;

    @Test
    void createRole_ShouldThrowAlreadyExistsExceptionWhenRoleNameExists() {
        CreateRoleRequestDTO request = PermissionDTOFixtures.defaultCreateRoleRequestDTO().build();

        when(roleDomainRepository.existsByName(eq(request.getName()), isNull())).thenReturn(true);
        assertThrows(AlreadyExistsException.class, () -> roleService.createRole(request));
        verify(roleDomainRepository, never()).save(any(Role.class));
    }

    @Test
    void createRole_ShouldCreateRoleSuccessfullyWhenNameDoesNotExist() {
        CreateRoleRequestDTO request = PermissionDTOFixtures.defaultCreateRoleRequestDTO().build();

        when(roleDomainRepository.existsByName(eq(request.getName()), isNull())).thenReturn(false);
        when(roleDomainRepository.save(any(Role.class))).thenAnswer(invocation -> PermissionFixtures.createDefaultRole().build());

        Long createdRoleId = roleService.createRole(request);

        assertNotNull(createdRoleId);
        verify(roleDomainRepository, times(1))
                .save(argThat(domain -> domain.getName().equals(request.getName())
                        && domain.getMemo().equals(request.getMemo())
                        && domain.getEnabled().equals(request.getEnabled())
                ));
    }


    @Test
    void listRoles_ShouldSuccessfully() {
        roleService.listRoles();
        verify(roleRepository, times(1)).queryAll();
    }

    @Test
    void getRolePermissionItems_ShouldSuccessfully() {
        roleService.getRolePermissionItems(1L);
        verify(roleRepository, times(1)).queryRolePermissions(1L);
    }

    @Test
    void updateRole_ShouldThrowNotFoundExceptionWhenRoleNotFound() {
        when(roleDomainRepository.findById(1L)).thenReturn(null);

        assertThrows(NotFoundException.class, () -> roleService.updateRole(1L, new UpdateRoleRequestDTO()));
    }

    @Test
    void updateRole_ShouldThrowAlreadyExistsExceptionWhenRoleNameExists() {
        UpdateRoleRequestDTO requestDTO = PermissionDTOFixtures.defaultUpdateRoleRequestDTO().build();
        Role role = PermissionFixtures.createDefaultRole().build();

        when(roleDomainRepository.findById(role.getId())).thenReturn(role);
        when(roleDomainRepository.existsByName(requestDTO.getName(), List.of(role.getId()))).thenReturn(true);

        assertThrows(AlreadyExistsException.class, () -> roleService.updateRole(role.getId(), requestDTO));
    }

    @Test
    void updateRole_ShouldSuccess() {
        UpdateRoleRequestDTO requestDTO = PermissionDTOFixtures.defaultUpdateRoleRequestDTO()
                .name("updated_name")
                .memo("updated_memo")
                .build();
        Role role = PermissionFixtures.createDefaultRole().build();

        when(roleDomainRepository.findById(role.getId())).thenReturn(role);
        when(roleDomainRepository.existsByName(requestDTO.getName(), List.of(role.getId()))).thenReturn(false);

        roleService.updateRole(role.getId(), requestDTO);

        verify(roleDomainRepository, times(1))
                .save(argThat(domain -> domain.getName().equals(requestDTO.getName())
                        && domain.getMemo().equals(requestDTO.getMemo())
                ));
        verify(roleRepository, times(1))
                .removeCache(role.getId());
    }
    @Test
    void deleteRole_ShouldThrowNotFoundExceptionWhenRoleNotFound() {
        when(roleDomainRepository.findById(1L)).thenReturn(null);

        assertThrows(NotFoundException.class, () -> roleService.deleteRole(1L));
    }


    @Test
    void deleteRole_failedPrecondition() {
        // Given
        Role role = PermissionFixtures.createDefaultRole().build();

        when(roleDomainRepository.findById(role.getId())).thenReturn(role);
        when(userRoleDomainRepository.existsByRoleId(role.getId())).thenReturn(true);

        // Then
        assertThrows(FailedPreconditionException.class, () -> roleService.deleteRole(role.getId()));

        // Verify
        verify(roleDomainRepository, never()).logicDelete(role.getId());
    }

    @Test
    void deleteRole_success() {
        // Given
        Role role = PermissionFixtures.createDefaultRole().build();

        when(roleDomainRepository.findById(role.getId())).thenReturn(role);
        when(userRoleDomainRepository.existsByRoleId(role.getId())).thenReturn(false);

        // When
        roleService.deleteRole(role.getId());

        // Then
        verify(roleDomainRepository).logicDelete(role.getId());
        verify(roleRepository, times(1))
                .removeCache(role.getId());
    }

    @Test
    void enableRole_WhenRoleExists_ShouldDisableRole() {
        Role role = PermissionFixtures.createDefaultRole().enabled(true).build();
        // Arrange
        when(roleDomainRepository.findById(role.getId())).thenReturn(role);

        // Act
        roleService.enableRole(role.getId(), false);

        // Assert
        verify(roleDomainRepository, times(1))
                .save(argThat(domain -> domain.getEnabled().equals(false)));
    }

    @Test
    void enableRole_WhenRoleExists_ShouldEnableRole() {
        Role role = PermissionFixtures.createDefaultRole().enabled(false).build();
        // Arrange
        when(roleDomainRepository.findById(role.getId())).thenReturn(role);

        // Act
        roleService.enableRole(role.getId(), true);

        // Assert
        verify(roleDomainRepository, times(1))
                .save(argThat(domain -> domain.getEnabled().equals(true)));
        verify(roleRepository, times(1))
                .removeCache(role.getId());
    }

    @Test
    void enableRole_WhenRoleDoesNotExist_ShouldThrowNotFoundException() {
        // Arrange
        when(roleDomainRepository.findById(1L)).thenReturn(null);

        // Act & Assert
        assertThrows(NotFoundException.class, () -> roleService.enableRole(1L, false));
    }



    @Test
    void grantPermissionItems_ShouldAddNewItems() {
        Role role = PermissionFixtures.createDefaultRole().build();
        RolePermissionItem rolePermissionItem = PermissionFixtures.createDefaultRolePermissionItem()
                .roleId(role.getId())
                .permissionItemId(1L)
                .build();
        // Setup
        when(rolePermissionItemDomainRepository.getByRoleId(role.getId())).thenReturn(List.of(rolePermissionItem));
        when(userRoleDomainRepository.getByRoleId(role.getId()))
                .thenReturn(List.of(PermissionFixtures.createDefaultUserRole(1L, role.getId()).build()));

        // Exercise
        roleService.grantPermissionItems(role.getId(), List.of(1L, 2L));

        // Verify
        verify(rolePermissionItemDomainRepository, times(1))
                .saveAll(argThat(domain -> domain.size() == 1 && domain.get(0).getPermissionItemId() == 2L));

        verify(rolePermissionItemDomainRepository, times(1))
                .logicDeleteAll(argThat(List::isEmpty));

        verify(userRepository, times(1))
                .removeUserPermissionCodesCache(1L);
    }

    @Test
    void grantPermissionItems_ShouldDeleteRemovedItems() {
        Role role = PermissionFixtures.createDefaultRole().build();
        RolePermissionItem rolePermissionItem = PermissionFixtures.createDefaultRolePermissionItem()
                .roleId(role.getId())
                .permissionItemId(1L)
                .build();
        // Setup
        when(rolePermissionItemDomainRepository.getByRoleId(role.getId())).thenReturn(List.of(rolePermissionItem));

        // Exercise
        roleService.grantPermissionItems(role.getId(), List.of());

        // Verify
        verify(rolePermissionItemDomainRepository, times(1))
                .saveAll(argThat(List::isEmpty));

        verify(rolePermissionItemDomainRepository, times(1))
                .logicDeleteAll(argThat(domain -> domain.size()==1 && domain.get(0).getPermissionItemId() == 1L));
    }
}