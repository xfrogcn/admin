package com.xfrog.platform.domain.base.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateDicItemCommand {
    private Boolean enabled;
    private String displayText;
    private String langCode;
    private Integer displayOrder;
    private String value;
    private String extValue1;
    private String extValue2;
    private String memo;
}
