package com.xfrog.platform.application.base.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "用户基础设置")
public class UserSettingsDTO {
    @Schema(description = "用户ID")
    private Long userId;
    @Schema(description = "语种列表")
    private List<LangDTO> langs;
    @Schema(description = "用户参数")
    private Map<String, String> parameters;
}
