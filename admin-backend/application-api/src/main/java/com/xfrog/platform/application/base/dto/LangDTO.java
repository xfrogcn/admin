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
@Schema(description = "语种信息")
public class LangDTO {
    @Schema(description = "语言 ID")
    private Long id;
    @Schema(description = "所属应用")
    private String application;
    @Schema(description = "语言代码")
    private String code;
    @Schema(description = "语言名称")
    private String name;
    @Schema(description = "本地语言的名称")
    private String localName;
    @Schema(description = "是否启用")
    private Boolean enabled;
    @Schema(description = "创建时间")
    private LocalDateTime createdTime;
}
