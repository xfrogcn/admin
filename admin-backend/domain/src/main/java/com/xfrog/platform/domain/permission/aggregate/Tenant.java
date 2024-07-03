package com.xfrog.platform.domain.permission.aggregate;

import com.xfrog.framework.domain.AuditEntity;
import com.xfrog.platform.domain.permission.command.CreateTenantCommand;
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
public class Tenant extends AuditEntity {
    private String code;
    private String name;
    private Long organizationId;
    private Boolean enabled;
    private String memo;
    private String adminUserName;
    private Long adminUserId;
    public static Tenant create(CreateTenantCommand command) {
        return Tenant.builder()
                .code(command.getCode())
                .name(command.getName())
                .organizationId(command.getOrganizationId())
                .enabled(command.getEnabled())
                .memo(command.getMemo())
                .adminUserId(command.getAdminUserId())
                .adminUserName(command.getAdminUserName())
                .build();
    }


    public void update(String name, String memo) {
        this.name = name;
        this.memo = memo;
    }

    public void updateEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}
