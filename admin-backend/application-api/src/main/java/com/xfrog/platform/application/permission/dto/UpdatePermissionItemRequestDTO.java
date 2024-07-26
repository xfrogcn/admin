package com.xfrog.platform.application.permission.dto;

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
@Schema(description = "修改权限项请求")
public class UpdatePermissionItemRequestDTO {
    @Schema(description = "权限项名称")
    @Size(min = 0, max = 64)
    private String name;
    @Schema(description = "权限项类型")
    private String type;
}
