package com.xfrog.platform.application.base.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
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
@Schema(description = "创建字典请求")
public class CreateDicRequestDTO {
    @Schema(description = "字典类型")
    @NotEmpty
    @Size(min = 1, max = 64)
    private String type;
    @Schema(description = "字典名称")
    @NotEmpty
    @Size(min = 1, max = 128)
    private String name;
    @Schema(description = "字典值标签语料编码")
    @Size(max = 128)
    private String labelLangCodeValue;
    @Schema(description = "字典扩展值1标签语料编码")
    @Size(max = 128)
    private String labelLangCodeExtValue1;
    @Schema(description = "字典扩展值2标签语料编码")
    @Size(max = 128)
    private String labelLangCodeExtValue2;
    @Schema(description = "字典说明")
    @Size(max = 255)
    private String memo;
}
