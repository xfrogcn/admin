package com.xfrog.platform.application.permission.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "用户信息")
public class UserDTO {
    @Schema(description = "用户ID")
    private Long id;
    @Schema(description = "用户账户名")
    private String userName;
    @Schema(description = "所属组织机构ID")
    private Long organizationId;
    @Schema(description = "组织名称")
    private String organizationName;
    @Schema(description = "是否过期")
    private boolean accountNonExpired;
    @Schema(description = "是否锁定")
    private boolean accountNonLocked;
    @Schema(description = "是否过期")
    private boolean credentialsNonExpired;
    @Schema(description = "是否启用")
    private boolean enabled;
    @Schema(description = "用户姓名")
    private String name;
    @Schema(description = "性别")
    private String sex;
    @Schema(description = "电话")
    private String mobilePhone;
    @Schema(description = "邮件地址")
    private String email;
    @Schema(description = "最近登录时间")
    private LocalDateTime lastLoginTime;
    @Schema(description = "创建时间")
    private LocalDateTime createdTime;
    @Schema(description = "角色列表")
    private List<RoleDTO> roles;
}
