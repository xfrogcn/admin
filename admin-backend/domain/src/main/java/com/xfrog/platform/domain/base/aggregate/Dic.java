package com.xfrog.platform.domain.base.aggregate;

import com.xfrog.framework.domain.AuditEntity;
import com.xfrog.platform.domain.base.command.CreateDicCommand;
import com.xfrog.platform.domain.base.command.UpdateDicCommand;
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
public class Dic extends AuditEntity {
    private String type;
    private String name;
    private String memo;
    private String labelLangCodeValue;
    private String labelLangCodeExtValue1;
    private String labelLangCodeExtValue2;

    public static Dic create(CreateDicCommand command) {
        return Dic.builder()
                .type(command.getType())
                .name(command.getName())
                .memo(command.getMemo())
                .labelLangCodeValue(command.getLabelLangCodeValue())
                .labelLangCodeExtValue1(command.getLabelLangCodeExtValue1())
                .labelLangCodeExtValue2(command.getLabelLangCodeExtValue2())
                .build();
    }

    public void update(UpdateDicCommand command) {
        this.type = command.getType();
        this.name = command.getName();
        this.memo = command.getMemo();
        this.labelLangCodeValue = command.getLabelLangCodeValue();
        this.labelLangCodeExtValue1 = command.getLabelLangCodeExtValue1();
        this.labelLangCodeExtValue2 = command.getLabelLangCodeExtValue2();
    }
}