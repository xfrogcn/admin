package com.xfrog.platform.application.permission.api.dto;

import com.xfrog.platform.domain.share.permission.DataScopeTargetType;
import com.xfrog.platform.domain.share.permission.DataScopeType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "数据权限赋权请求")
public class GrantDataScopeRequestDTO {
    @Schema(description = "目标ID")
    @NotNull
    private Long targetId;
    @Schema(description = "目标类型")
    @NotNull
    private DataScopeTargetType targetType;
    @Schema(description = "数据权限项")
    @Valid
    private List<DataScopeItem> scopeItems;


    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "数据权限项")
    public static class DataScopeItem {
        @Schema(description = "数据范围类型")
        @NotNull
        private DataScopeType scopeType;
        @Schema(description = "数据范围ID")
        @NotNull
        private Long scopeId;
    }
}
