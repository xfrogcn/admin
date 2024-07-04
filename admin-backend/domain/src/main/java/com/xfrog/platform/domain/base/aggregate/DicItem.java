package com.xfrog.platform.domain.base.aggregate;

import com.xfrog.framework.domain.AuditEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter(AccessLevel.PROTECTED)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class DicItem extends AuditEntity {
    private Long dicId;
    private Boolean enabled;
    private String displayText;
    private String langCode;
    private Integer displayOrder;
    private String value;
    private String extValue1;
    private String extValue2;
    private String memo;
}