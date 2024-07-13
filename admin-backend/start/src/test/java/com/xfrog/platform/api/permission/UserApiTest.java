package com.xfrog.platform.api.permission;

import com.xfrog.platform.api.permission.fixtures.PermissionApiFixtures;
import com.xfrog.platform.application.permission.api.dto.CreateUserRequestDTO;
import com.xfrog.platform.application.permission.api.dto.QueryUserRequestDTO;
import com.xfrog.platform.application.permission.api.dto.UpdateUserRequestDTO;
import com.xfrog.platform.application.permission.dto.PermissionDTOFixtures;
import com.xfrog.platform.domain.permission.aggregate.PermissionFixtures;
import com.xfrog.platform.domain.permission.aggregate.User;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserApiTest extends BasePermissionApiTest {
    @SneakyThrows
    @Test
    @Sql(statements = {PermissionApiFixtures.SQL_TRUNCATE_USERS})
    void listUsers_ShouldSuccessfully() {
        QueryUserRequestDTO requestDTO = QueryUserRequestDTO.builder()
                .pageNum(1).pageSize(10)
                .organizationId(1L).build();

        request(post("/api/users/list", requestDTO))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(1L))
                .andExpect(jsonPath("$.data[0].id").value(1L))
                .andExpect(jsonPath("$.data[0].roles[0]").exists());
    }

    @SneakyThrows
    @Test
    @Sql(statements = {PermissionApiFixtures.SQL_TRUNCATE_USERS})
    void createUser_ShouldSuccessfully() {
        CreateUserRequestDTO requestDTO = PermissionDTOFixtures.defaultCreateUserRequestDTO()
                .userName("test_user")
                .organizationId(1L)
                .build();

        request(post("/api/users", requestDTO))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    @Sql(statements = {PermissionApiFixtures.SQL_TRUNCATE_USERS})
    void updateUser_ShouldSuccessfully() {
        UpdateUserRequestDTO requestDTO = PermissionDTOFixtures.defaultUpdateUserRequestDTO()
                .name("test")
                .organizationId(1L)
                .build();
        User user = permissionApiFixtures.saveUser(PermissionFixtures.createDefaultUser()
                .name("test_user")
                .organizationId(1L)
                .build());

        request(put(url("/api/users/{userId}", user.getId()), requestDTO))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    @Sql(statements = {PermissionApiFixtures.SQL_TRUNCATE_USERS})
    void getUserDetail_ShouldSuccessfully() {
        User user = permissionApiFixtures.saveUser(PermissionFixtures.createDefaultUser()
                .name("test_user")
                .organizationId(1L)
                .build());

        request(get(url("/api/users/{userId}", user.getId())))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    @Sql(statements = {PermissionApiFixtures.SQL_TRUNCATE_USERS})
    void resetPassword_ShouldSuccessfully() {
        User user = permissionApiFixtures.saveUser(PermissionFixtures.createDefaultUser()
                .name("test_user")
                .organizationId(1L)
                .build());

        request(put(url("/api/users/reset-password/{userId}", user.getId()), null))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    @Sql(statements = {PermissionApiFixtures.SQL_TRUNCATE_USERS})
    void disableUser_ShouldSuccessfully() {
        User user = permissionApiFixtures.saveUser(PermissionFixtures.createDefaultUser()
                .name("test_user")
                .enabled(true)
                .organizationId(1L)
                .build());

        request(put(url("/api/users/disable/{userId}", user.getId()), null))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    @Sql(statements = {PermissionApiFixtures.SQL_TRUNCATE_USERS})
    void enableUser_ShouldSuccessfully() {
        User user = permissionApiFixtures.saveUser(PermissionFixtures.createDefaultUser()
                .name("test_user")
                .enabled(false)
                .organizationId(1L)
                .build());

        request(put(url("/api/users/enable/{userId}", user.getId()), null))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    @Sql(statements = {PermissionApiFixtures.SQL_TRUNCATE_USERS})
    void changeCurrentUserPassword_ShouldSuccessfully() {
        request(put("/api/users/change-password", null)
                .param("newPassword", "123456"))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    @Sql(statements = {PermissionApiFixtures.SQL_TRUNCATE_USERS})
    void grantRoles_ShouldSuccessfully() {
        User user = permissionApiFixtures.saveUser(PermissionFixtures.createDefaultUser()
                .name("test_user")
                .enabled(false)
                .organizationId(1L)
                .build());

        request(put(url("/api/users/grant-roles/{userId}", user.getId()), List.of(1, 2)))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    @Sql(statements = {PermissionApiFixtures.SQL_TRUNCATE_USERS})
    void getCurrentUserDetail_ShouldSuccessfully() {
        request(get("/api/users//current"))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    @Sql(statements = {PermissionApiFixtures.SQL_TRUNCATE_USERS})
    void getCurrentUserPermissionCodes_ShouldSuccessfully() {
        request(get("/api/users/current/permission-codes"))
                .andExpect(status().isOk());
    }
}
