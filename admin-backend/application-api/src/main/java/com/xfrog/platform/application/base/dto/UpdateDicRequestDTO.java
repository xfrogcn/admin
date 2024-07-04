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
@Schema(description = "更新字典请求")
public class UpdateDicRequestDTO {
    @Schema(description = "字典类型")
    @NotEmpty
    @Size(min = 1, max = 64)
    private String type;
    @Schema(description = "字典名称")
    @NotEmpty
    @Size(min = 1, max = 128)
    private String name;
    @Schema(description = "字典说明")
    @Size(max = 255)
    private String memo;
}
