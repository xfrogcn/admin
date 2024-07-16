package com.xfrog.platform.infrastructure.base.dataobject;

import com.baomidou.mybatisplus.annotation.TableName;
import com.xfrog.framework.po.AuditPO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@TableName("user_parameters")
public class UserParameterPO extends AuditPO {
    // 用户ID
    private Long userId;
    // 所属应用
    private String application;
    // 参数名称
    private String parameterName;
    // 参数值
    private String parameterValue;
}
