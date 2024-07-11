package com.xfrog.platform.application.base.dto;

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
@Schema(description = "语料本地化项")
public class LangLocalDTO {
    @Schema(description = "语种编码")
    private String langCode;
    @Schema(description = "本地化语言")
    private String langValue;
}
