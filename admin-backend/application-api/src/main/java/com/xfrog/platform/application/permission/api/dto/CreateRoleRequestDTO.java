package com.xfrog.platform.application.permission.api.dto;

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
@Schema(description = "创建角色请求")
public class CreateRoleRequestDTO {
    @Schema(description = "角色名称")
    @NotEmpty
    @Size(min = 0, max = 64)
    private String name;
    @Schema(description = "角色说明")
    @Size(min = 0, max = 128)
    private String memo;
    @Schema(description = "是否启用")
    @NotNull
    private Boolean enabled;
}
