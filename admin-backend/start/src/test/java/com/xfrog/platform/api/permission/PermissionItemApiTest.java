package com.xfrog.platform.api.permission;

import com.xfrog.platform.application.permission.dto.CreatePermissionItemRequestDTO;
import com.xfrog.platform.application.permission.dto.UpdatePermissionItemRequestDTO;
import com.xfrog.platform.application.permission.dto.PermissionDTOFixtures;
import com.xfrog.platform.domain.permission.aggregate.PermissionItem;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PermissionItemApiTest extends BasePermissionApiTest {

    @SneakyThrows
    @Test
    void createPermissionItem_ShouldSuccessfully() {
        PermissionItem parent = permissionApiFixtures.createAndSavePermissionItem("parent_code", null);

        CreatePermissionItemRequestDTO requestDTO = PermissionDTOFixtures.defaultCreatePermissionItemRequestDTO()
                .parentId(parent.getId())
                .code("test_create_code")
                .build();

        request(post("/api/permission-items", requestDTO))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    void updatePermissionItem_ShouldSuccessfully() {
        PermissionItem parent = permissionApiFixtures.createAndSavePermissionItem("parent_code", null);
        PermissionItem permissionItem = permissionApiFixtures.createAndSavePermissionItem("code", parent.getId());

        UpdatePermissionItemRequestDTO requestDTO = PermissionDTOFixtures.defaultUpdatePermissionItemRequestDTO()
                .name("updated_name")
                .type("updated_type")
                .build();

        request(post(url("/api/permission-items/{permissionItemId}", permissionItem.getId()), requestDTO))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    void listPermissionItems_ShouldSuccessfully() {
        request(get("/api/permission-items/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].code").exists());
    }

    @SneakyThrows
    @Test
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
    void deletePermissionItem_ShouldSuccessfully() {
        PermissionItem permissionItem = permissionApiFixtures.createAndSavePermissionItem("code1", null);
        permissionApiFixtures.createAndSaveRolePermissionItem(1L, permissionItem.getId());

        request(delete(url("/api/permission-items/{permissionItemId}", permissionItem.getId()), null))
                .andExpect(status().isOk());
    }
}
