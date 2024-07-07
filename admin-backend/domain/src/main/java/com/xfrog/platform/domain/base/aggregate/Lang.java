package com.xfrog.platform.domain.base.aggregate;

import com.xfrog.framework.domain.AuditEntity;
import com.xfrog.platform.domain.base.command.CreateLangCommand;
import com.xfrog.platform.domain.base.command.UpdateLangCommand;
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
public class Lang extends AuditEntity {
    // 所属应用
    private String application;
    // 语言代码
    private String code;
    // 语言名称
    private String name;
    // 本地语言的名称
    private String localName;
    // 是否启用
    private Boolean enabled;

    public static Lang create(CreateLangCommand command) {
        return Lang.builder()
                .application(command.getApplication())
                .code(command.getCode())
                .name(command.getName())
                .localName(command.getLocalName())
                .enabled(command.getEnabled())
                .build();
    }

    public void update(UpdateLangCommand command) {
        this.name = command.getName();
        this.localName = command.getLocalName();
    }

    public void enable() {
        this.enabled = true;
    }

    public void disable() {
        this.enabled = false;
    }
}
