package com.xfrog.platform.application.base.dto;

import com.xfrog.framework.dto.PageQueryDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "查询字典请求")
public class QueryDicRequestDTO extends PageQueryDTO {
    @Schema(description = "关键字")
    private String keyword;
}
