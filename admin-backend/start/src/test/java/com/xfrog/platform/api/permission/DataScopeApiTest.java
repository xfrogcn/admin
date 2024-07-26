package com.xfrog.platform.api.permission;

import com.xfrog.platform.api.permission.fixtures.PermissionApiFixtures;
import com.xfrog.platform.application.permission.dto.GrantDataScopeRequestDTO;
import com.xfrog.platform.domain.permission.aggregate.Organization;
import com.xfrog.platform.domain.permission.aggregate.PermissionFixtures;
import com.xfrog.platform.domain.share.permission.DataScopeTargetType;
import com.xfrog.platform.domain.share.permission.DataScopeType;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DataScopeApiTest extends BasePermissionApiTest {
    @SneakyThrows
    @Test
    @Sql(statements = {
            PermissionApiFixtures.SQL_TRUNCATE_ORGANIZATION,
            PermissionApiFixtures.SQL_TRUNCATE_ROLES,
            PermissionApiFixtures.SQL_TRUNCATE_DATA_SCOPES})
    void grantDataScope_ShouldSuccessfully() {
        Organization organization = permissionApiFixtures.saveOrganization(PermissionFixtures.createDefaultOrganization()
                        .parentId(1L)
                .build());
        Long roleId = permissionApiFixtures.saveRole(PermissionFixtures.createDefaultRole().build()).getId();
        GrantDataScopeRequestDTO request = GrantDataScopeRequestDTO.builder()
                .targetId(roleId)
                .targetType(DataScopeTargetType.ROLE)
                .scopeItems(List.of(
                        GrantDataScopeRequestDTO.DataScopeItem.builder()
                                .scopeType(DataScopeType.ORGANIZATION)
                                .scopeId(organization.getId())
                                .build()
                ))
                        .build();

        request(put("/api/data-scopes/grant", request))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    @Sql(statements = {
            PermissionApiFixtures.SQL_TRUNCATE_ORGANIZATION,
            PermissionApiFixtures.SQL_TRUNCATE_ROLES,
            PermissionApiFixtures.SQL_TRUNCATE_DATA_SCOPES})
    void getDataScopes_ShouldSuccessfully() {
        request(get(url("/api/data-scopes/{targetType}/{targetId}", DataScopeTargetType.ROLE, 1L)))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    @Sql(statements = {
            PermissionApiFixtures.SQL_TRUNCATE_ORGANIZATION,
            PermissionApiFixtures.SQL_TRUNCATE_ROLES,
            PermissionApiFixtures.SQL_TRUNCATE_DATA_SCOPES})
    void getUserDataScopes_ShouldSuccessfully() {
        request(get(url("/api/data-scopes/users/{userId}", 1L)))
                .andExpect(status().isOk());
    }
}
