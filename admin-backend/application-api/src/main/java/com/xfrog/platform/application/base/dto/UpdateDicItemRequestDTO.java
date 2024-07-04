package com.xfrog.platform.application.base.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "更新字典项信息请求")
public class UpdateDicItemRequestDTO {
    @Schema(description = "是否启用")
    @NotNull
    private Boolean enabled;
    @Schema(description = "显示名称")
    @Size(min = 0, max = 128)
    private String displayText;
    @Schema(description = "多语言语料编码")
    @Size(max = 128)
    private String langCode;
    @Schema(description = "显示顺序")
    @NotNull
    private Integer displayOrder;
    @Schema(description = "对应值")
    @Size(max = 64)
    private String value;
    @Schema(description = "扩展值 1")
    @Size(max = 255)
    private String extValue1;
    @Schema(description = "扩展值 2")
    @Size(max = 255)
    private String extValue2;
    @Schema(description = "说明")
    @Size(max = 255)
    private String memo;
}