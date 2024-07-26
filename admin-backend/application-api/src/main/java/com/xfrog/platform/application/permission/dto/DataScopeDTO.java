package com.xfrog.platform.application.permission.dto;

import com.xfrog.platform.domain.share.permission.DataScopeTargetType;
import com.xfrog.platform.domain.share.permission.DataScopeType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "组织信息")
public class DataScopeDTO {
    @Schema(description = "目标ID")
    private Long targetId;
    @Schema(description = "目标类型")
    private DataScopeTargetType targetType;
    @Schema(description = "数据范围类型")
    private DataScopeType scopeType;
    @Schema(description = "数据范围ID")
    private Long scopeId;
    @Schema(description = "数据范围信息")
    private Map<String, Object> scopeInfo;
}
