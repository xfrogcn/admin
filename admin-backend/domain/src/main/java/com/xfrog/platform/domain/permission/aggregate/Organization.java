package com.xfrog.platform.domain.permission.aggregate;

import com.xfrog.framework.domain.AuditEntity;
import com.xfrog.platform.domain.permission.command.CreateOrganizationCommand;
import com.xfrog.platform.domain.permission.command.UpdateOrganizationCommand;
import com.xfrog.platform.domain.share.permission.OrganizationStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter(AccessLevel.PROTECTED)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Organization extends AuditEntity {
    public static final int LEVEL_ROOT = 0;
    public static final Organization ROOT_ORGANIZATION = Organization.builder()
            .level(LEVEL_ROOT)
            .code("")
            .build();
    private String name;
    private Long parentId;
    private List<Long> parentIds;
    private String code;
    private int level;
    private int seq;
    private OrganizationStatus status;
    private Integer displayOrder;
    private String telephone;
    private String principal;

    public static Organization create(CreateOrganizationCommand command) {
        return Organization.builder()
                .level(command.getLevel())
                .code(command.getCode())
                .name(command.getName())
                .parentId(command.getParentId())
                .parentIds(command.getParentIds())
                .seq(command.getSeq())
                .status(command.getStatus())
                .displayOrder(command.getDisplayOrder())
                .telephone(command.getTelephone())
                .principal(command.getPrincipal())
                .build();
    }

    public List<Long> getAllLevelIds() {
        List<Long> parentIds = this.getParentIds();
        if (parentIds == null) {
            parentIds = new ArrayList<>();
        }
        if (this.getId() != null) {
            parentIds.add(this.getId());
        }
        return parentIds;
    }

    public void update(UpdateOrganizationCommand command) {
        this.name = command.getName();
        this.status = command.getStatus();
        this.displayOrder = command.getDisplayOrder();
        this.telephone = command.getTelephone();
        this.principal = command.getPrincipal();
    }
}
