package com.xfrog.platform.application.permission.service.impl;

import com.xfrog.framework.dto.PageDTO;
import com.xfrog.framework.exception.business.AlreadyExistsException;
import com.xfrog.framework.exception.business.NotFoundException;
import com.xfrog.framework.exception.business.PermissionDeniedException;
import com.xfrog.framework.principal.CurrentPrincipalContext;
import com.xfrog.framework.principal.PrincipalInfo;
import com.xfrog.platform.application.permission.api.dto.CreateUserRequestDTO;
import com.xfrog.platform.application.permission.api.dto.CurrentUserInfoDTO;
import com.xfrog.platform.application.permission.api.dto.QueryUserRequestDTO;
import com.xfrog.platform.application.permission.api.dto.RoleDTO;
import com.xfrog.platform.application.permission.api.dto.UpdateUserRequestDTO;
import com.xfrog.platform.application.permission.api.dto.UserDTO;
import com.xfrog.platform.application.permission.dto.PermissionDTOFixtures;
import com.xfrog.platform.application.permission.repository.RoleRepository;
import com.xfrog.platform.application.permission.repository.UserRepository;
import com.xfrog.platform.domain.permission.aggregate.PermissionFixtures;
import com.xfrog.platform.domain.permission.aggregate.Role;
import com.xfrog.platform.domain.permission.aggregate.Tenant;
import com.xfrog.platform.domain.permission.aggregate.User;
import com.xfrog.platform.domain.permission.aggregate.UserRole;
import com.xfrog.platform.domain.permission.repository.TenantDomainRepository;
import com.xfrog.platform.domain.permission.repository.UserDomainRepository;
import com.xfrog.platform.domain.permission.repository.UserRoleDomainRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class )
class UserServiceImplTest {
    @Mock
    private UserDomainRepository userDomainRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserRoleDomainRepository userRoleDomainRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private TenantDomainRepository tenantDomainRepository;
    @InjectMocks
    private UserServiceImpl userService;
    @Test
    void listUsers_ShouldReturnEmptyData_WhenNoUsersFound() {
        QueryUserRequestDTO requestDTO = QueryUserRequestDTO.builder()
                .pageNum(1)
                .pageSize(10)
                .build();

        when(userRepository.queryAllBy(requestDTO)).thenReturn(new PageDTO<>(1L, 10L, 0L, 0L, Collections.emptyList()));

        PageDTO<UserDTO> result = userService.listUsers(requestDTO);

        assertTrue(result.getData().isEmpty());
    }

    @Test
    void listUsers_ShouldPopulateUserRoles() {
        QueryUserRequestDTO requestDTO = QueryUserRequestDTO.builder()
                .pageNum(1)
                .pageSize(10)
                .build();
        UserDTO user = PermissionDTOFixtures.defaultUserDTO().id(1L).build();
        RoleDTO role = PermissionDTOFixtures.defaultRoleDTO().id(1L).build();
        UserRole userRole = PermissionFixtures.createDefaultUserRole(user.getId(), role.getId()).build();

        when(userRepository.queryAllBy(requestDTO)).thenReturn(new PageDTO<>(1L, 10L, 1L, 1L, List.of(user)));
        when(userRoleDomainRepository.getByUserIds(Collections.singletonList(user.getId()))).thenReturn(List.of(userRole));
        when(roleRepository.queryByIds(Collections.singletonList(role.getId()))).thenReturn(List.of(role));

        PageDTO<UserDTO> result = userService.listUsers(requestDTO);

        assertFalse(result.getData().isEmpty());
        assertFalse(result.getData().get(0).getRoles().isEmpty());
        assertEquals(role.getName(), result.getData().get(0).getRoles().get(0).getName());
    }

    @Test
    void createUser_ThrowsException_WhenUsernameAlreadyExists() {
        CreateUserRequestDTO requestDTO = PermissionDTOFixtures.defaultCreateUserRequestDTO().build();
        User user = PermissionFixtures.createDefaultUser()
                .userName(requestDTO.getUserName()).build();

        // Arrange
        when(userDomainRepository.findByUserName(requestDTO.getUserName())).thenReturn(user);

        // Act & Assert
        assertThrows(AlreadyExistsException.class, () -> userService.createUser(requestDTO));
    }

    @Test
    void createUser_ReturnsUserId_WhenUsernameDoesNotExist() {
        CreateUserRequestDTO requestDTO = PermissionDTOFixtures.defaultCreateUserRequestDTO().build();

        // Arrange
        when(userDomainRepository.findByUserName(requestDTO.getUserName())).thenReturn(null);
        when(userDomainRepository.save(any(User.class))).thenReturn(PermissionFixtures.createDefaultUser().build());

        // Act
        userService.createUser(requestDTO);

        // Assert
        verify(userDomainRepository, times(1))
                .save(argThat(domain -> domain.getUserName().equals(requestDTO.getUserName())
                        && domain.getPassword().startsWith("{bcrypt}")
                        && domain.getEmail().equals(requestDTO.getEmail())
                        && domain.getMobilePhone().equals(requestDTO.getMobilePhone())
                        && domain.getSex().equals(requestDTO.getSex())
                        && domain.getOrganizationId().equals(requestDTO.getOrganizationId())
                        && domain.getName().equals(requestDTO.getName())
                        && domain.getLastLoginTime() == null
                        && domain.isAccountNonExpired()
                        && domain.isAccountNonLocked()
                        && domain.isCredentialsNonExpired()
                        && domain.isEnabled()
                ));
    }

    @Test
    void updateUser_WhenUserNotFound_ThrowsNotFoundException() {
        // Arrange
        when(userDomainRepository.findById(1L)).thenReturn(null);

        // Act & Assert
        assertThrows(NotFoundException.class, () -> userService.updateUser(1L, new UpdateUserRequestDTO()));
    }

    @Test
    void updateUser_WhenUserFound_UpdatesSuccessfully() {
        // Arrange
        User user = PermissionFixtures.createDefaultUser().build();
        UpdateUserRequestDTO requestDTO = PermissionDTOFixtures.defaultUpdateUserRequestDTO()
                .name("updated_name")
                .mobilePhone("updated_mobilePhone")
                .organizationId(2L)
                .sex("M")
                .email("updated_email")
                .build();

        when(userDomainRepository.findById(user.getId())).thenReturn(user);

        // Act
        userService.updateUser(user.getId(), requestDTO);

        // Assert
        verify(userDomainRepository, times(1))
                .save(argThat(domain -> domain.getEmail().equals(requestDTO.getEmail())
                        && domain.getMobilePhone().equals(requestDTO.getMobilePhone())
                        && domain.getSex().equals(requestDTO.getSex())
                        && domain.getOrganizationId().equals(requestDTO.getOrganizationId())
                        && domain.getName().equals(requestDTO.getName())
                ));
    }

    @Test
    void getUserDetail_ShouldSuccessfully() {
        userService.getUserDetail(1L);
        verify(userRepository,times(1)).queryById(1L);
    }

    @Test
    void resetPassword_UserNotFound_ThrowsNotFoundException() {
        // Setup
        when(userDomainRepository.findById(1L)).thenReturn(null);

        // Execution & Verification
        assertThrows(NotFoundException.class, () -> userService.resetPassword(1L));
    }

    @Test
    void resetPassword_UserFound_ChangesPassword() {
        // Setup
        User user = PermissionFixtures.createDefaultUser().build();
        when(userDomainRepository.findById(user.getId())).thenReturn(user);

        // Execution
        userService.resetPassword(user.getId());

        // Verification
        verify(userDomainRepository, times(1))
                .save(argThat(domain -> domain.getPassword().startsWith("{bcrypt}")));
    }

    @Test
    void disableUser_WhenUserNotFound_ThrowsNotFoundException() {
        // Arrange
        when(userDomainRepository.findById(1L)).thenReturn(null);

        // Act & Assert
        assertThrows(NotFoundException.class, () -> userService.disableUser(1L));
    }

    @Test
    void disableUser_WhenUserFound_DisablesTheUser() {
        // Arrange
        User user = PermissionFixtures.createDefaultUser().build();
        when(userDomainRepository.findById(user.getId())).thenReturn(user);

        // Act
        userService.disableUser(user.getId());

        // Assert
        verify(userDomainRepository, times(1))
                .save(argThat(domain -> !domain.isEnabled()));
    }

    @Test
    void enableUser_WithValidId_ShouldEnableUser() {
        // Arrange
        User user = PermissionFixtures.createDefaultUser().enabled(false).build();
        when(userDomainRepository.findById(user.getId())).thenReturn(user);

        // Act
        userService.enableUser(user.getId());

        // Assert
        verify(userDomainRepository, times(1))
                .save(argThat(User::isEnabled));
    }

    @Test
    void enableUser_WithInvalidId_ShouldThrowNotFoundException() {
        // Arrange
        when(userDomainRepository.findById(1L)).thenReturn(null);

        // Act & Assert
        assertThrows(NotFoundException.class, () -> userService.enableUser(1L));
    }

    @Test
    void changeCurrentUserPassword_ShouldNotChangePasswordForSystemUser() {
        PrincipalInfo systemPrincipal = PrincipalInfo.system();
        CurrentPrincipalContext.setCurrentPrincipal(systemPrincipal);
        userService.changeCurrentUserPassword("newPassword");
        verify(userDomainRepository, never()).save(any(User.class));
    }

    @Test
    void changeCurrentUserPassword_ShouldChangePasswordForRegularUser() {
        User user = PermissionFixtures.createDefaultUser().build();
        PrincipalInfo userPrincipal = PrincipalInfo.create(user.getId(), "testUser", 1L, "testClient", "testTenant");
        CurrentPrincipalContext.setCurrentPrincipal(userPrincipal);

        when(userDomainRepository.findById(userPrincipal.getUserId())).thenReturn(user);

        assertDoesNotThrow(() -> userService.changeCurrentUserPassword("newPassword"));
        verify(userDomainRepository, times(1)).save(any(User.class));
    }

    @Test
    void changeCurrentUserPassword_ShouldThrowPermissionDeniedExceptionWhenPrincipalIsNull() {
        CurrentPrincipalContext.clearCurrentPrincipal();
        userService.changeCurrentUserPassword("newPassword");
        verify(userDomainRepository, never()).save(any(User.class));
    }

    @Test
    void changeCurrentUserPassword_ShouldThrowPermissionDeniedExceptionWhenUserIdIsNull() {
        PrincipalInfo principalWithNullId = PrincipalInfo.create(null, "userWithoutId", 1L, "client", "tenant");
        CurrentPrincipalContext.setCurrentPrincipal(principalWithNullId);
        userService.changeCurrentUserPassword("newPassword");
        verify(userDomainRepository, never()).save(any(User.class));
    }

    @Test
    void changeCurrentUserPassword_ShouldNotChangePasswordWhenUserNotFound() {
        PrincipalInfo userPrincipal = PrincipalInfo.create(1L, "testUser", 1L, "testClient", "testTenant");
        CurrentPrincipalContext.setCurrentPrincipal(userPrincipal);
        when(userDomainRepository.findById(userPrincipal.getUserId())).thenReturn(null);

        assertThrows(NotFoundException.class, () -> userService.changeCurrentUserPassword("newPassword"));
    }

    @Test
    void grantRoles_ShouldAddNewRoles() {
        User user = PermissionFixtures.createDefaultUser().build();
        Role role = PermissionFixtures.createDefaultRole().build();
        Role role2 = PermissionFixtures.createDefaultRole().build();
        UserRole existingRole = PermissionFixtures.createDefaultUserRole(user.getId(), role.getId()).build();
        // Given
        when(userRoleDomainRepository.getByUserId(user.getId())).thenReturn(List.of(existingRole));

        // When
        userService.grantRoles(user.getId(), List.of(existingRole.getRoleId(), role2.getId()));

        // Then
        verify(userRoleDomainRepository, times(1))
                .saveAll(argThat(list-> list.size()==1 && Objects.equals(list.get(0).getRoleId(), role2.getId())));
        verify(userRoleDomainRepository, times(1))
                .logicDeleteAll(argThat(List::isEmpty));
    }

    @Test
    void grantRoles_ShouldDeleteRemovedRoles() {
        User user = PermissionFixtures.createDefaultUser().build();
        Role role = PermissionFixtures.createDefaultRole().build();
        UserRole existingRole = PermissionFixtures.createDefaultUserRole(user.getId(), role.getId()).build();
        // Given
        when(userRoleDomainRepository.getByUserId(user.getId())).thenReturn(List.of(existingRole));

        // When
        userService.grantRoles(user.getId(), List.of());

        // Then
        verify(userRoleDomainRepository, times(1))
                .saveAll(argThat(List::isEmpty));
        verify(userRoleDomainRepository, times(1))
                .logicDeleteAll(argThat(list -> list.size() == 1 && list.get(0).getRoleId().equals(role.getId())));
    }


    @Test
    void getCurrentUserDetail_Success() {

        User user = PermissionFixtures.createDefaultUser().tenantId("tid").build();
        Tenant tenant = PermissionFixtures.createDefaultTenant().code("tid").organizationId(1L).build();
        PrincipalInfo userPrincipal = PrincipalInfo.create(user.getId(), "testUser", 1L, "testClient", "testTenant");
        CurrentPrincipalContext.setCurrentPrincipal(userPrincipal);

        when(userDomainRepository.findById(user.getId())).thenReturn(user);
        when(tenantDomainRepository.findByCode(user.getTenantId())).thenReturn(tenant);

        CurrentUserInfoDTO result = userService.getCurrentUserDetail();

        assertThat(result).isNotNull();
        assertThat(result.getRootOrganizationId()).isEqualTo(1L);
        assertThat(result.getTenantId()).isEqualTo(tenant.getCode());
    }

    @Test
    void getCurrentUserDetail_TenantDisabled_ThrowsPermissionDeniedException() {
        User user = PermissionFixtures.createDefaultUser().tenantId("tid").build();
        Tenant tenant = PermissionFixtures.createDefaultTenant().code("tid").enabled(false).organizationId(1L).build();
        PrincipalInfo userPrincipal = PrincipalInfo.create(user.getId(), "testUser", 1L, "testClient", "testTenant");
        CurrentPrincipalContext.setCurrentPrincipal(userPrincipal);

        when(userDomainRepository.findById(user.getId())).thenReturn(user);
        when(tenantDomainRepository.findByCode(user.getTenantId())).thenReturn(tenant);

        assertThrows(PermissionDeniedException.class, () -> userService.getCurrentUserDetail());
    }

    @Test
    void getCurrentUserDetail_UserNotFound_ReturnsNull() {
        User user = PermissionFixtures.createDefaultUser().tenantId("tid").build();
        PrincipalInfo userPrincipal = PrincipalInfo.create(user.getId(), "testUser", 1L, "testClient", "testTenant");
        CurrentPrincipalContext.setCurrentPrincipal(userPrincipal);

        when(userDomainRepository.findById(user.getId())).thenReturn(null);

        assertNull(userService.getCurrentUserDetail(), "Expected null when user is not found");
    }

    @Test
    void getCurrentUserDetail_TenantNotFound_ThrowsPermissionDeniedException() {
        User user = PermissionFixtures.createDefaultUser().tenantId("tid").build();
        PrincipalInfo userPrincipal = PrincipalInfo.create(user.getId(), "testUser", 1L, "testClient", "testTenant");
        CurrentPrincipalContext.setCurrentPrincipal(userPrincipal);

        when(userDomainRepository.findById(user.getId())).thenReturn(user);
        when(tenantDomainRepository.findByCode(user.getTenantId())).thenReturn(null);

        assertThrows(PermissionDeniedException.class, () -> userService.getCurrentUserDetail(), "Expected PermissionDeniedException when tenant is not found");
    }

    @Test
    void getCurrentUserPermissionCodes() {
        User user = PermissionFixtures.createDefaultUser().tenantId("tid").build();
        PrincipalInfo userPrincipal = PrincipalInfo.create(user.getId(), "testUser", 1L, "testClient", "testTenant");
        CurrentPrincipalContext.setCurrentPrincipal(userPrincipal);

        userService.getCurrentUserPermissionCodes();

        verify(userRepository, times(1)).queryUserPermissionCodes(user.getId());
    }

    @Test
    void getUserPermissionCodes_Success() {
        userService.getUserPermissionCodes(1L);
        verify(userRepository, times(1)).queryUserPermissionCodes(1L);
    }
}