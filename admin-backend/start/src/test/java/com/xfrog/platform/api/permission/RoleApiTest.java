package com.xfrog.platform.api.permission;

import com.xfrog.platform.api.permission.fixtures.PermissionApiFixtures;
import com.xfrog.platform.application.permission.api.dto.CreateRoleRequestDTO;
import com.xfrog.platform.application.permission.api.dto.UpdateRoleRequestDTO;
import com.xfrog.platform.application.permission.dto.PermissionDTOFixtures;
import com.xfrog.platform.domain.permission.aggregate.PermissionFixtures;
import com.xfrog.platform.domain.permission.aggregate.Role;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RoleApiTest extends BasePermissionApiTest {
    @SneakyThrows
    @Test
    @Sql(statements = {PermissionApiFixtures.SQL_TRUNCATE_ROLES})
    void createRole_ShouldSuccessfully() {

        CreateRoleRequestDTO requestDTO = PermissionDTOFixtures.defaultCreateRoleRequestDTO()
                .build();

        request(post("/api/roles", requestDTO))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    @Sql(statements = {PermissionApiFixtures.SQL_TRUNCATE_ROLES})
    void listRoles_ShouldSuccessfully() {
        request(get("/api/roles/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").exists());
    }

    @SneakyThrows
    @Test
    @Sql(statements = {PermissionApiFixtures.SQL_TRUNCATE_ROLES, PermissionApiFixtures.SQL_TRUNCATE_ROLE_PERMISSION_ITEM})
    void getRolePermissionItems_ShouldSuccessfully() {
        request(get(url("/api/roles/{id}/permission-items", 1l)))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    @Sql(statements = {PermissionApiFixtures.SQL_TRUNCATE_ROLES})
    void updateRole_ShouldSuccessfully() {
        Role role = permissionApiFixtures.saveRole(PermissionFixtures.createDefaultRole().build());

        UpdateRoleRequestDTO requestDTO = PermissionDTOFixtures.defaultUpdateRoleRequestDTO()
                .build();

        request(post(url("/api/roles/{id}", role.getId()), requestDTO))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    @Sql(statements = {PermissionApiFixtures.SQL_TRUNCATE_ROLES})
    void deleteRole_ShouldSuccessfully() {
        request(delete(url("/api/roles/{id}", 2L), null))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    @Sql(statements = {PermissionApiFixtures.SQL_TRUNCATE_ROLES})
    void enableRole_ShouldSuccessfully() {

        Role role = permissionApiFixtures.saveRole(PermissionFixtures.createDefaultRole().build());

        request(post(url("/api/roles/{id}/{enable}", role.getId(), false), null))
                .andExpect(status().isOk());

        request(post(url("/api/roles/{id}/{enable}", role.getId(), true), null))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    @Sql(statements = {PermissionApiFixtures.SQL_TRUNCATE_ROLES})
    void grantPermissionItems_ShouldSuccessfully() {
        Role role = permissionApiFixtures.saveRole(PermissionFixtures.createDefaultRole().build());

        request(put(url("/api/roles/grant-permissions/{id}", role.getId()), List.of(1, 2, 3)))
                .andExpect(status().isOk());
    }
}
