package com.xfrog.platform.domain.base.aggregate;

import com.xfrog.framework.domain.AuditEntity;
import com.xfrog.platform.domain.base.command.CreateUserParameterCommand;
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
public class UserParameter extends AuditEntity {
    // 用户ID
    private Long userId;
    // 所属应用
    private String application;
    // 参数名称
    private String parameterName;
    // 参数值
    private String parameterValue;

    public static UserParameter create(CreateUserParameterCommand command) {
        return UserParameter.builder()
                .userId(command.getUserId())
                .application(command.getApplication())
                .parameterName(command.getParameterName())
                .parameterValue(command.getParameterValue())
                .build();
    }

    public void update(String parameterValue) {
        this.parameterValue = parameterValue;
    }
}
