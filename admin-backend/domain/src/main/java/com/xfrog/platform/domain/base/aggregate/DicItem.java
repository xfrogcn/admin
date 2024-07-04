package com.xfrog.platform.domain.base.aggregate;

import com.xfrog.framework.domain.AuditEntity;
import com.xfrog.platform.domain.base.command.CreateDicItemCommand;
import com.xfrog.platform.domain.base.command.UpdateDicItemCommand;
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

    public static DicItem create(CreateDicItemCommand command) {
        return DicItem.builder()
                .dicId(command.getDicId())
                .enabled(command.getEnabled())
                .displayText(command.getDisplayText())
                .langCode(command.getLangCode())
                .displayOrder(command.getDisplayOrder())
                .value(command.getValue())
                .extValue1(command.getExtValue1())
                .extValue2(command.getExtValue2())
                .memo(command.getMemo())
                .build();
    }

    public void update(UpdateDicItemCommand command) {
        this.enabled = command.getEnabled();
        this.displayText = command.getDisplayText();
        this.langCode = command.getLangCode();
        this.displayOrder = command.getDisplayOrder();
        this.value = command.getValue();
        this.extValue1 = command.getExtValue1();
        this.extValue2 = command.getExtValue2();
        this.memo = command.getMemo();
    }
}