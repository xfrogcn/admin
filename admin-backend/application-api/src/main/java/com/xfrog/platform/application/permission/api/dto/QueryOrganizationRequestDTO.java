package com.xfrog.platform.application.permission.api.dto;

import com.xfrog.platform.domain.share.permission.OrganizationStatus;
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
@Schema(description = "组织查询请求")
public class QueryOrganizationRequestDTO {
    @Schema(description = "关键字")
    private String keyword;
    @Schema(description = "上级组织ID")
    private Long parentId;
    @Schema(description = "组织状态")
    private OrganizationStatus status;
}
