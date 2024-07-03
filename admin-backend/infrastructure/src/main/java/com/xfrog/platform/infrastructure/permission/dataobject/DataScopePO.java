package com.xfrog.platform.infrastructure.permission.dataobject;

import com.baomidou.mybatisplus.annotation.TableName;
import com.xfrog.framework.po.AuditPO;
import com.xfrog.platform.domain.share.permission.DataScopeTargetType;
import com.xfrog.platform.domain.share.permission.DataScopeType;
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
@TableName("data_scopes")
public class DataScopePO extends AuditPO {
    private Long targetId;
    private DataScopeTargetType targetType;
    private DataScopeType scopeType;
    private Long scopeId;
}
