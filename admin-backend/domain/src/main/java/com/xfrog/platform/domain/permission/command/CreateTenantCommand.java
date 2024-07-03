package com.xfrog.platform.domain.permission.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateTenantCommand {
    private String code;
    private String name;
    private Long organizationId;
    private Boolean enabled;
    private String memo;
    private String adminUserName;
    private Long adminUserId;
}
