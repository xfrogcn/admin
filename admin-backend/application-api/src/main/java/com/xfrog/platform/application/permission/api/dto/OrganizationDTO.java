package com.xfrog.platform.application.permission.api.dto;

import com.xfrog.framework.dto.IdDTO;
import com.xfrog.platform.domain.share.permission.OrganizationStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "组织信息")
public class OrganizationDTO extends IdDTO {
    @Schema(description = "组织名称")
    private String name;
    @Schema(description = "父组织ID")
    private Long parentId;
    @Schema(description = "父组织ID列表")
    private List<Long> parentIds;
    @Schema(description = "父组织名称列表")
    private List<String> parentNames;
    @Schema(description = "组织编码")
    private String code;
    @Schema(description = "组织层级")
    private int level;
    @Schema(description = "组织状态")
    private OrganizationStatus status;
    @Schema(description = "显示顺序")
    private Integer displayOrder;
    @Schema(description = "电话")
    private String telephone;
    @Schema(description = "负责人")
    private String principal;
    @Schema(description = "创建时间")
    private LocalDateTime createdTime;
}
