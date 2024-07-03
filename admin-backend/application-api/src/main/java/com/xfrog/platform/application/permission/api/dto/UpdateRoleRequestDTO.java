package com.xfrog.platform.application.permission.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "更新角色请求")
public class UpdateRoleRequestDTO {
    @Schema(description = "角色名称")
    @Size(min = 0, max = 64)
    private String name;
    @Schema(description = "角色说明")
    @Size(min = 0, max = 128)
    private String memo;
}
