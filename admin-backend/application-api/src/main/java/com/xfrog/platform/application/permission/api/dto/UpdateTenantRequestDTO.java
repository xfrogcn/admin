package com.xfrog.platform.application.permission.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
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
@Schema(description = "修改租户请求")
public class UpdateTenantRequestDTO {
    @Schema(description = "租户名称")
    @NotEmpty
    @Size(min = 0, max = 128)
    private String name;
    @Schema(description = "租户说明")
    @Size(min = 0, max = 128)
    private String memo;
}
