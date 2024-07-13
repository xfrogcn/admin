package com.xfrog.platform.api.permission;

import com.xfrog.platform.api.permission.fixtures.PermissionApiFixtures;
import com.xfrog.platform.application.permission.api.dto.CreateTenantRequestDTO;
import com.xfrog.platform.application.permission.api.dto.QueryTenantRequestDTO;
import com.xfrog.platform.application.permission.api.dto.UpdateTenantRequestDTO;
import com.xfrog.platform.application.permission.dto.PermissionDTOFixtures;
import com.xfrog.platform.domain.permission.aggregate.PermissionFixtures;
import com.xfrog.platform.domain.permission.aggregate.Tenant;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TenantApiTest  extends BasePermissionApiTest {
    @SneakyThrows
    @Test
    @Sql(statements = {
            PermissionApiFixtures.SQL_TRUNCATE_TENANTS,
            PermissionApiFixtures.SQL_TRUNCATE_USERS,
            PermissionApiFixtures.SQL_TRUNCATE_ORGANIZATION,
            PermissionApiFixtures.SQL_TRUNCATE_DATA_SCOPES,
            PermissionApiFixtures.SQL_TRUNCATE_ROLE_PERMISSION_ITEM,
            PermissionApiFixtures.SQL_TRUNCATE_ROLES})
    void createTenant_ShouldSuccessfully() {
        CreateTenantRequestDTO requestDTO = PermissionDTOFixtures.defaultCreateTenantRequestDTO()
                .build();

        request(post("/api/tenants", requestDTO))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    @Sql(statements = {
            PermissionApiFixtures.SQL_TRUNCATE_TENANTS})
    void listTenants_ShouldSuccessfully() {
        QueryTenantRequestDTO requestDTO = QueryTenantRequestDTO.builder()
                .pageNum(1)
                .pageSize(10)
                .build();

        request(post("/api/tenants/list", requestDTO))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    @Sql(statements = {
            PermissionApiFixtures.SQL_TRUNCATE_TENANTS})
    void updateTenant_ShouldSuccessfully() {
        UpdateTenantRequestDTO requestDTO = PermissionDTOFixtures.defaultUpdateTenantRequestDTO()
                .build();
        Tenant tenant = permissionApiFixtures.saveTenant(PermissionFixtures.createDefaultTenant().build());

        request(put(url("/api/tenants/{tenantId}", tenant.getId()), requestDTO))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    @Sql(statements = {
            PermissionApiFixtures.SQL_TRUNCATE_TENANTS})
    void enableTenant_ShouldSuccessfully() {
        Tenant tenant = permissionApiFixtures.saveTenant(PermissionFixtures.createDefaultTenant().enabled(false).build());

        request(post(url("/api/tenants/{tenantId}/{enabled}", tenant.getId(), true), null))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    @Sql(statements = {
            PermissionApiFixtures.SQL_TRUNCATE_TENANTS})
    void disableTenant_ShouldSuccessfully() {
        Tenant tenant = permissionApiFixtures.saveTenant(PermissionFixtures.createDefaultTenant().enabled(true).build());

        request(post(url("/api/tenants/{tenantId}/{enabled}", tenant.getId(), false), null))
                .andExpect(status().isOk());
    }
}
