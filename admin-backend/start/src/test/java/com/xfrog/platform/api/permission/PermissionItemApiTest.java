package com.xfrog.platform.api.permission;

import com.xfrog.platform.api.BaseApiTest;
import com.xfrog.platform.api.permission.fixtures.PermissionApiFixtures;
import com.xfrog.platform.application.permission.api.dto.CreatePermissionItemRequestDTO;
import com.xfrog.platform.application.permission.api.dto.UpdatePermissionItemRequestDTO;
import com.xfrog.platform.application.permission.dto.PermissionItemDTOFixtures;
import com.xfrog.platform.domain.permission.aggregate.PermissionItem;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import({ PermissionApiFixtures.class })
public class PermissionItemApiTest extends BaseApiTest {
    @Autowired
    private PermissionApiFixtures permissionApiFixtures;

    @SneakyThrows
    @Test
    @Sql(statements = {PermissionApiFixtures.SQL_TRUNCATE_PERMISSION_ITEM})
    void createPermissionItem_ShouldSuccessfully() {
        PermissionItem parent = permissionApiFixtures.createAndSavePermissionItem("parent_code", null);

        CreatePermissionItemRequestDTO requestDTO = PermissionItemDTOFixtures.defaultCreatePermissionItemRequestDTO()
                .parentId(parent.getId())
                .build();

        request(post("/api/permission-items", requestDTO))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    @Sql(statements = {PermissionApiFixtures.SQL_TRUNCATE_PERMISSION_ITEM})
    void updatePermissionItem_ShouldSuccessfully() {
        PermissionItem parent = permissionApiFixtures.createAndSavePermissionItem("parent_code", null);
        PermissionItem permissionItem = permissionApiFixtures.createAndSavePermissionItem("code", parent.getId());

        UpdatePermissionItemRequestDTO requestDTO = PermissionItemDTOFixtures.defaultUpdatePermissionItemRequestDTO()
                .name("updated_name")
                .type("updated_type")
                .build();

        request(post(url("/api/permission-items/{permissionItemId}", permissionItem.getId()), requestDTO))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    @Sql(statements = {PermissionApiFixtures.SQL_TRUNCATE_PERMISSION_ITEM})
    void listPermissionItems_ShouldSuccessfully() {
        permissionApiFixtures.createAndSavePermissionItem("code1", null);
        permissionApiFixtures.createAndSavePlatformPermissionItem("code2", null);

        request(get("/api/permission-items/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].code").exists())
                .andExpect(jsonPath("$[1]").doesNotExist());
    }

    @SneakyThrows
    @Test
    @Sql(statements = {PermissionApiFixtures.SQL_TRUNCATE_PERMISSION_ITEM})
    void listPermissionItemsFormPlatform_ShouldSuccessfully() {
        permissionApiFixtures.createAndSavePermissionItem("code1", null);
        permissionApiFixtures.createAndSavePlatformPermissionItem("code2", null);

        request(get("/api/permission-items/list/platform"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].code").exists())
                .andExpect(jsonPath("$[1].code").exists());
    }

    @SneakyThrows
    @Test
    @Sql(statements = {PermissionApiFixtures.SQL_TRUNCATE_PERMISSION_ITEM, PermissionApiFixtures.SQL_TRUNCATE_ROLE_PERMISSION_ITEM})
    void deletePermissionItem_ShouldSuccessfully() {
        PermissionItem permissionItem = permissionApiFixtures.createAndSavePermissionItem("code1", null);
        permissionApiFixtures.createAndSaveRolePermissionItem(1L, permissionItem.getId());

        request(delete(url("/api/permission-items/{permissionItemId}", permissionItem.getId()), null))
                .andExpect(status().isOk());
    }
}
