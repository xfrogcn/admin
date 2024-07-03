package com.xfrog.platform.domain.permission.aggregate;

import com.xfrog.framework.domain.AuditEntity;
import com.xfrog.platform.domain.share.permission.DataScopeTargetType;
import com.xfrog.platform.domain.share.permission.DataScopeType;
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
public class DataScope extends AuditEntity {
    private Long targetId;
    private DataScopeTargetType targetType;
    private DataScopeType scopeType;
    private Long scopeId;
}
