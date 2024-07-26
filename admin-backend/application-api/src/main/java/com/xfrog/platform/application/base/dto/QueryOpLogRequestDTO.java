package com.xfrog.platform.application.base.dto;

import com.xfrog.framework.common.DateTimeRange;
import com.xfrog.framework.dto.PageQueryDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "查询操作日志请求")
public class QueryOpLogRequestDTO extends PageQueryDTO {
    @Schema(description = "日志类型")
    private List<String> tags;
    @Schema(description = "业务类型")
    private List<String> bizTypes;
    @Schema(description = "业务动作")
    private List<String> bizActions;
    @Schema(description = "关键字")
    private String keyword;
    @Schema(description = "操作时间范围")
    private DateTimeRange timeRange;
}
