package com.xfrog.platform.api.permission;

import com.xfrog.platform.api.permission.fixtures.PermissionApiFixtures;
import com.xfrog.platform.application.permission.api.dto.CreateOrganizationRequestDTO;
import com.xfrog.platform.application.permission.api.dto.UpdateOrganizationRequestDTO;
import com.xfrog.platform.application.permission.dto.PermissionDTOFixtures;
import com.xfrog.platform.domain.permission.aggregate.Organization;
import com.xfrog.platform.domain.permission.aggregate.PermissionFixtures;
import com.xfrog.platform.domain.share.permission.OrganizationStatus;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class OrganizationApiTest  extends BasePermissionApiTest {

    @SneakyThrows
    @Test
    @Sql(statements = {PermissionApiFixtures.SQL_TRUNCATE_ORGANIZATION})
    void createOrganization_ShouldSuccessfully() {
        Organization parent = permissionApiFixtures.saveOrganization(PermissionFixtures.createDefaultOrganization()
                .parentId(1L)
                .code("00010001").level(2).name("parent").build());
        CreateOrganizationRequestDTO requestDTO = PermissionDTOFixtures.defaultCreateOrganizationRequestDTO()
                .parentId(parent.getId()).build();

        request(post("/api/organizations", requestDTO))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    @Sql(statements = {PermissionApiFixtures.SQL_TRUNCATE_ORGANIZATION})
    void updateOrganization_ShouldSuccessfully() {
        Organization organization = permissionApiFixtures.saveOrganization(PermissionFixtures.createDefaultOrganization().parentId(1L).build());
        UpdateOrganizationRequestDTO requestDTO = PermissionDTOFixtures.defaultUpdateOrganizationRequestDTO()
                .name("updated_name")
                .displayOrder(1)
                .principal("updated_principal")
                .status(OrganizationStatus.DISABLED)
                .telephone("updated_telephone")
                .build();

        request(post(url("/api/organizations/{id}", organization.getId()), requestDTO))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    @Sql(statements = {PermissionApiFixtures.SQL_TRUNCATE_ORGANIZATION})
    void listOrganizations_ShouldSuccessfully() {
        request(get("/api/organizations/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @SneakyThrows
    @Test
    @Sql(statements = {PermissionApiFixtures.SQL_TRUNCATE_ORGANIZATION})
    void getOrganization_ShouldSuccessfully() {
        request(get(url("/api/organizations/{id}", 1)))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    @Sql(statements = {PermissionApiFixtures.SQL_TRUNCATE_ORGANIZATION})
    void deleteOrganization_ShouldSuccessfully() {
        Organization organization = permissionApiFixtures.saveOrganization(PermissionFixtures.createDefaultOrganization().parentId(1L).build());

        request(delete(url("/api/organizations/{id}", organization.getId()), null))
                .andExpect(status().isOk());
    }

}
