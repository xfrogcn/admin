package com.xfrog.platform.application.permission.dto;

import com.xfrog.platform.domain.share.permission.OrganizationStatus;
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
@Schema(description = "更新组织信息请求")
public class UpdateOrganizationRequestDTO {
    @Schema(description = "组织名称")
    @NotEmpty
    @Size(min = 0, max = 64)
    private String name;
    @Schema(description = "组织状态")
    @NotNull
    private OrganizationStatus status;
    @Schema(description = "显示顺序")
    @NotNull
    private Integer displayOrder;
    @Schema(description = "电话")
    @Size(min = 0, max = 32)
    private String telephone;
    @Schema(description = "负责人")
    @Size(min = 0, max = 32)
    private String principal;
}
