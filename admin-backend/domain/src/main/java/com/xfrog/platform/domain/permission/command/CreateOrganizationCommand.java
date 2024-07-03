package com.xfrog.platform.domain.permission.command;

import com.xfrog.platform.domain.share.permission.OrganizationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrganizationCommand {
    private String name;
    private Long parentId;
    private List<Long> parentIds;
    private OrganizationStatus status;
    private Integer displayOrder;
    private String telephone;
    private String principal;
    private Integer level;
    private String code;
    private Integer seq;
}
