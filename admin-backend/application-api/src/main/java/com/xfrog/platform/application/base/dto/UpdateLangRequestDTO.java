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
@Schema(description = "更新语种信息请求")
public class UpdateLangRequestDTO {
    @Schema(description = "语言名称")
    @NotEmpty
    @Size(min = 0, max = 128)
    private String name;
    @Schema(description = "本地语言的名称")
    @NotEmpty
    @Size(min = 0, max = 128)
    private String localName;
}
