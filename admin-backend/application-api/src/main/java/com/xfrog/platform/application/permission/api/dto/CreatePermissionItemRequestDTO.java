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
@Schema(description = "创建权限项请求")
public class CreatePermissionItemRequestDTO {
    @Schema(description = "权限项编码")
    @NotEmpty
    @Size(min = 0, max = 128)
    private String code;
    @Schema(description = "权限项名称")
    @NotEmpty
    @Size(min = 0, max = 64)
    private String name;
    @Schema(description = "权限项类型")
    @NotEmpty
    private String type;
    @Schema(description = "父权限项ID")
    private Long parentId;
    @Schema(description = "是否为平台权限项")
    @NotNull
    private Boolean platform;
}
