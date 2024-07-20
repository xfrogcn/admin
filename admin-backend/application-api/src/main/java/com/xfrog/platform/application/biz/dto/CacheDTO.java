package com.xfrog.platform.application.biz.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "缓存列表项")
public class CacheDTO {
    @Schema(description = "缓存名称")
    private String cacheName;
    @Schema(description = "缓存显示名称")
    private String displayText;
    @Schema(description = "缓存显示名称对应语料编码")
    private String displayCorpusCode;
    @Schema(description = "缓存说明")
    private String description;
    @Schema(description = "缓存说明对应语料编码")
    private String descriptionCorpusCode;
}
