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
@Schema(description = "创建语种请求")
public class CreateLangRequestDTO {
    @Schema(description = "所属应用")
    @NotEmpty
    @Size(min = 0, max = 32)
    private String application;
    @Schema(description = "语言代码")
    @NotEmpty
    @Size(min = 0, max = 16)
    private String code;
    @Schema(description = "语言名称")
    @NotEmpty
    @Size(min = 0, max = 128)
    private String name;
    @Schema(description = "本地语言的名称")
    @NotEmpty
    @Size(min = 0, max = 128)
    private String localName;
    @Schema(description = "是否启用")
    @NotNull
    private Boolean enabled;
    @Schema(description = "参考语种")
    private Long referenceLangId;
}
