package com.xfrog.platform.application.base.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "语料库信息")
public class LangCorpusDTO {
    @Schema(description = "语料库 ID")
    private Long id;
    @Schema(description = "所属应用")
    private String application;
    @Schema(description = "语料类别")
    private String corpusType;
    @Schema(description = "语料分组")
    private String corpusGroup;
    @Schema(description = "语料编码")
    private String corpusCode;
    @Schema(description = "语料说明")
    private String memo;
    @Schema(description = "是否启用")
    private Boolean enabled;
    @Schema(description = "是否配置本地化")
    private Boolean configured;
    @Schema(description = "创建时间")
    private LocalDateTime createdTime;
    @Schema(description = "本地化语言配置")
    private Map<String, String> langLocales;
}