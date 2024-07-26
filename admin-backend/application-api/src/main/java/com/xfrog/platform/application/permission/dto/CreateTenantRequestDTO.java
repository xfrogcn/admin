package com.xfrog.platform.application.permission.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "创建租户请求")
public class CreateTenantRequestDTO {
    @Schema(description = "租户编码")
    private String code;
    @Schema(description = "租户名称")
    @Size(min = 0, max = 128)
    private String name;
    @Schema(description = "是否启用")
    @NotNull
    private Boolean enabled;
    @Schema(description = "租户说明")
    @Size(min = 0, max = 128)
    private String memo;
    @Schema(description = "租户管理员登录账号")
    @Size(min = 0, max = 64)
    @NotEmpty
    private String adminUserName;
}
