package com.xfrog.platform.application.permission.api.dto;

import com.xfrog.framework.common.DateTimeRange;
import com.xfrog.framework.dto.PageQueryDTO;
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
@Schema(description = "用户列表查询请求")
public class QueryUserRequestDTO extends PageQueryDTO {
    @Schema(description = "关键字")
    private String keyword;
    @Schema(description = "组织ID")
    private Long organizationId;
    @Schema(description = "是否启用")
    private Boolean enabled;
    @Schema(description = "创建时间区间")
    private DateTimeRange createdTime;
    @Schema(description = "最后登录时间区间")
    private DateTimeRange lastLoginTime;
}
