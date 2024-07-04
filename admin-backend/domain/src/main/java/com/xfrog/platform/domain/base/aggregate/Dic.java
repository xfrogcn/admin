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

    public static Dic create(CreateDicCommand command) {
        return Dic.builder()
                .type(command.getType())
                .name(command.getName())
                .memo(command.getMemo())
                .build();
    }

    public void update(UpdateDicCommand command) {
        this.type = command.getType();
        this.name = command.getName();
        this.memo = command.getMemo();
    }
}