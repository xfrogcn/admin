package com.xfrog.platform.application.permission.dto;

import com.xfrog.framework.dto.IdDTO;
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
@Schema(description = "角色信息")
public class RoleDTO extends IdDTO {
    @Schema(description = "角色名称")
    private String name;
    @Schema(description = "角色说明")
    private String memo;
    @Schema(description = "是否启用")
    private Boolean enabled;
    @Schema(description = "创建时间")
    private LocalDateTime createdTime;
}
