package com.xfrog.platform.application.base.dto;

import com.xfrog.framework.dto.IdDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "字典项信息")
public class DicItemDTO extends IdDTO {
    @Schema(description = "关联的字典 ID")
    private Long dicId;
    @Schema(description = "是否启用")
    private Boolean enabled;
    @Schema(description = "显示名称")
    private String displayText;
    @Schema(description = "多语言语料编码")
    private String langCode;
    @Schema(description = "显示顺序")
    private Integer displayOrder;
    @Schema(description = "对应值")
    private String value;
    @Schema(description = "扩展值 1")
    private String extValue1;
    @Schema(description = "扩展值 2")
    private String extValue2;
    @Schema(description = "说明")
    private String memo;
    @Schema(description = "创建时间")
    private LocalDateTime createdTime;
}
