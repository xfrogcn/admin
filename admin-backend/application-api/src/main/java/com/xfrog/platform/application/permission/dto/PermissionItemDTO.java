package com.xfrog.platform.application.permission.dto;

import com.xfrog.framework.dto.IdDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "权限项")
public class PermissionItemDTO extends IdDTO {
    @Schema(description = "权限项编码")
    private String code;
    @Schema(description = "权限项名称")
    private String name;
    @Schema(description = "权限项类型")
    private String type;
    @Schema(description = "父权限项ID")
    private Long parentId;
    @Schema(description = "是否为平台权限项")
    private Boolean platform;
}
