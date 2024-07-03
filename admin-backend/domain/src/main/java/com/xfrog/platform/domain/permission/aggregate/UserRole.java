package com.xfrog.platform.domain.permission.aggregate;

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
public class UserRole extends AuditEntity {
    private Long userId;
    private Long roleId;
}
