package com.xfrog.platform.domain.base.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserParameterCommand {
    // 用户ID
    private Long userId;
    // 所属应用
    private String application;
    // 参数名称
    private String parameterName;
    // 参数值
    private String parameterValue;
}
