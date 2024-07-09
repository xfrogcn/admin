package com.xfrog.platform.application.base.dto;

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
@Schema(description = "语料库查询请求")
public class QueryLangCorpusRequestDTO extends PageQueryDTO {
    @Schema(description = "所属应用")
    private String application;
    @Schema(description = "关键字")
    private String keyword;
    @Schema(description = "语料类型")
    private String corpusType;
    @Schema(description = "语料分组")
    private String corpusGroup;
    @Schema(description = "本地化是否已全部配置")
    private Boolean configured;
}
