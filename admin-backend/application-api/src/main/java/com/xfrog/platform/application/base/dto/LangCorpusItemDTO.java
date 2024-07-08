package com.xfrog.platform.application.base.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "语料项信息")
public class LangCorpusItemDTO {
    @Schema(description = "语料编码")
    @NotEmpty
    @Size(min = 0, max = 128)
    private String corpusCode;
    @Schema(description = "语料说明")
    @Size(min = 0, max = 256)
    private String memo;
    @Schema(description = "是否启用")
    @NotNull
    private Boolean enabled;
    @Schema(description = "本地化语言配置")
    private Map<String, String> langLocales;
}
