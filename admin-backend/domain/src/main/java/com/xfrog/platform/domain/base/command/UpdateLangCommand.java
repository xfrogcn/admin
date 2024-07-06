package com.xfrog.platform.domain.base.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateLangCommand {
    // 语言名称
    private String name;
    // 本地语言的名称
    private String localName;
    // 是否启用
    private Boolean enabled;
}
