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
public class Role extends AuditEntity {
    private String name;
    private String memo;
    private Boolean enabled;

    public void update(String name, String memo)
    {
        this.name = name;
        this.memo = memo;
    }

    public void updateEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}