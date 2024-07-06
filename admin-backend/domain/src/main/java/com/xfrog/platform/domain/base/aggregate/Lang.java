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
}
