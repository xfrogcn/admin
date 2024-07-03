package com.xfrog.platform.domain.permission.command;

import com.xfrog.platform.domain.share.permission.OrganizationStatus;
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
public class UpdateOrganizationCommand {
    private String name;
    private OrganizationStatus status;
    private Integer displayOrder;
    private String telephone;
    private String principal;
}
