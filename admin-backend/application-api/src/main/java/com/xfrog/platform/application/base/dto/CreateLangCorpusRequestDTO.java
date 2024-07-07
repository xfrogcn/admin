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

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "创建语料库信息请求")
public class CreateLangCorpusRequestDTO {
    @Schema(description = "所属应用")
    @NotEmpty
    @Size(min = 0, max = 32)
    private String application;
    @Schema(description = "语料类别")
    @NotEmpty
    @Size(min = 0, max = 64)
    private String corpusType;
    @Schema(description = "语料分组")
    @NotEmpty
    @Size(min = 0, max = 64)
    private String corpusGroup;
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
}