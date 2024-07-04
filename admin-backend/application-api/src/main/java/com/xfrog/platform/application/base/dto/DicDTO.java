package com.xfrog.platform.application.base.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "字典信息")
public class DicDTO {
    @Schema(description = "字典ID")
    private Long id;
    @Schema(description = "字典类型")
    private String type;
    @Schema(description = "字典名称")
    private String name;
    @Schema(description = "字典说明")
    private String memo;
    @Schema(description = "创建时间")
    private LocalDateTime createdTime;
}
