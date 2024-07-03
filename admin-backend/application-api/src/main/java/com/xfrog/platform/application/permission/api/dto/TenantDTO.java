package com.xfrog.platform.application.permission.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "租户信息")
public class TenantDTO {
    @Schema(description = "租户ID")
    private Long id;
    @Schema(description = "租户编码")
    private String code;
    @Schema(description = "租户名称")
    private String name;
    @Schema(description = "是否启用")
    private Boolean enabled;
    @Schema(description = "租户说明")
    private String memo;
    @Schema(description = "创建时间")
    private LocalDateTime createdTime;
    @Schema(description = "租户管理员用户ID")
    private Long adminUserId;
    @Schema(description = "租户管理员登录账号")
    private String adminUserName;
}
