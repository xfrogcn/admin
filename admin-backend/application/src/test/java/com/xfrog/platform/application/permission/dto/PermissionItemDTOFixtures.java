package com.xfrog.platform.application.permission.dto;

import com.xfrog.platform.application.permission.api.dto.CreatePermissionItemRequestDTO;
import com.xfrog.platform.application.permission.api.dto.UpdatePermissionItemRequestDTO;

public class PermissionItemDTOFixtures {
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
}
