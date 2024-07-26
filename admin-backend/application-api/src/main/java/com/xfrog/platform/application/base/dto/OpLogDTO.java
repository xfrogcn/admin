package com.xfrog.platform.application.base.dto;

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
@Schema(description = "操作日志")
public class OpLogDTO extends IdDTO {
    @Schema(description = "业务ID")
    private String bizId;
    @Schema(description = "业务编码")
    private String bizCode;
    @Schema(description = "请求ID")
    private String requestId;
    @Schema(description = "操作类型")
    private String tag;
    @Schema(description = "业务类型")
    private String bizType;
    @Schema(description = "操作动作")
    private String bizAction;
    @Schema(description = "扩展信息")
    private String extra;
    @Schema(description = "操作信息")
    private String message;
    @Schema(description = "操作人ID")
    private Long operatorId;
    @Schema(description = "操作人名称")
    private String operatorName;
    @Schema(description = "操作结果")
    private Boolean success;
    @Schema(description = "操作耗时")
    private Long executeTime;
    @Schema(description = "创建时间")
    private LocalDateTime createdTime;
}
