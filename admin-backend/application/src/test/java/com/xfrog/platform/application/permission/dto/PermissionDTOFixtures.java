package com.xfrog.platform.application.permission.dto;

import com.xfrog.platform.application.permission.api.dto.CreateOrganizationRequestDTO;
import com.xfrog.platform.application.permission.api.dto.CreatePermissionItemRequestDTO;
import com.xfrog.platform.application.permission.api.dto.CreateRoleRequestDTO;
import com.xfrog.platform.application.permission.api.dto.CreateUserRequestDTO;
import com.xfrog.platform.application.permission.api.dto.DataScopeDTO;
import com.xfrog.platform.application.permission.api.dto.OrganizationDTO;
import com.xfrog.platform.application.permission.api.dto.RoleDTO;
import com.xfrog.platform.application.permission.api.dto.UpdateOrganizationRequestDTO;
import com.xfrog.platform.application.permission.api.dto.UpdatePermissionItemRequestDTO;
import com.xfrog.platform.application.permission.api.dto.UpdateRoleRequestDTO;
import com.xfrog.platform.application.permission.api.dto.UpdateUserRequestDTO;
import com.xfrog.platform.application.permission.api.dto.UserDTO;
import com.xfrog.platform.domain.share.permission.DataScopeTargetType;
import com.xfrog.platform.domain.share.permission.DataScopeType;
import com.xfrog.platform.domain.share.permission.OrganizationStatus;

public class PermissionDTOFixtures {
    public static CreatePermissionItemRequestDTO.CreatePermissionItemRequestDTOBuilder defaultCreatePermissionItemRequestDTO() {
        return CreatePermissionItemRequestDTO.builder()
                .code("code")
                .name("name")
                .parentId(1L)
                .platform(true)
                .type("type");
    }

    public static UpdatePermissionItemRequestDTO.UpdatePermissionItemRequestDTOBuilder defaultUpdatePermissionItemRequestDTO() {
        return UpdatePermissionItemRequestDTO.builder()
                .name("name")
                .type("type");
    }

    public static CreateOrganizationRequestDTO.CreateOrganizationRequestDTOBuilder defaultCreateOrganizationRequestDTO() {
        return CreateOrganizationRequestDTO.builder()
                .name("name")
                .parentId(1L)
                .principal("principal")
                .status(OrganizationStatus.NORMAL)
                .displayOrder(100)
                .telephone("telephone");
    }

    public static UpdateOrganizationRequestDTO.UpdateOrganizationRequestDTOBuilder defaultUpdateOrganizationRequestDTO() {
        return UpdateOrganizationRequestDTO.builder()
                .name("name")
                .principal("principal")
                .status(OrganizationStatus.NORMAL)
                .displayOrder(100)
                .telephone("telephone");
    }

    public static OrganizationDTO.OrganizationDTOBuilder defaultOrganizationDTO() {
        return OrganizationDTO.builder()
                .code("code")
                .createdTime(null)
                .displayOrder(100)
                .id(1L)
                .level(1)
                .name("name")
                .parentId(null)
                .parentIds(null)
                .parentNames(null)
                .principal("principal")
                .status(OrganizationStatus.NORMAL)
                .telephone("telephone");
    }

    public static CreateRoleRequestDTO.CreateRoleRequestDTOBuilder defaultCreateRoleRequestDTO() {
        return CreateRoleRequestDTO.builder()
                .name("name")
                .enabled(true)
                .memo("memo");
    }

    public static UpdateRoleRequestDTO.UpdateRoleRequestDTOBuilder defaultUpdateRoleRequestDTO() {
        return UpdateRoleRequestDTO.builder()
                .name("name")
                .memo("memo");
    }

    public static CreateUserRequestDTO.CreateUserRequestDTOBuilder defaultCreateUserRequestDTO(){
        return CreateUserRequestDTO.builder()
                .email("email")
                .enabled(true)
                .mobilePhone("mobilePhone")
                .name("name")
                .organizationId(1L)
                .password("password")
                .sex("F")
                .userName("userName");
    }

    public static UpdateUserRequestDTO.UpdateUserRequestDTOBuilder defaultUpdateUserRequestDTO(){
        return UpdateUserRequestDTO.builder()
                .email("email")
                .mobilePhone("mobilePhone")
                .name("name")
                .organizationId(1L)
                .sex("F");
    }

    public static UserDTO.UserDTOBuilder defaultUserDTO(){
        return UserDTO.builder()
                .email("email")
                .enabled(true)
                .id(1L)
                .organizationName("GLOBAL")
                .mobilePhone("mobilePhone")
                .name("name")
                .organizationId(1L)
                .sex("F")
                .userName("userName");
    }

    public static RoleDTO.RoleDTOBuilder defaultRoleDTO(){
        return RoleDTO.builder()
                .createdTime(null)
                .enabled(true)
                .id(1L)
                .memo("memo")
                .name("name");
    }

    public static DataScopeDTO.DataScopeDTOBuilder defaultDataScopeDTO(){
        return DataScopeDTO.builder()
                .targetId(1L)
                .targetType(DataScopeTargetType.ROLE)
                .scopeId(1L)
                .scopeType(DataScopeType.ORGANIZATION);
    }
}
