package com.xfrog.framework.principal;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
public class DataPermissionItem {
    private String scopeType;
    private Long scopeId;

    public static DataPermissionItem of(String scopeType, Long scopeId) {
        return new DataPermissionItem(scopeType, scopeId);
    }
}
