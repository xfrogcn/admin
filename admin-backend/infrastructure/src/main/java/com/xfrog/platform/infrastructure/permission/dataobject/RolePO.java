package com.xfrog.platform.infrastructure.permission.dataobject;

import com.baomidou.mybatisplus.annotation.TableName;
import com.xfrog.framework.po.TenantAuditPO;
import com.xfrog.platform.infrastructure.persistent.config.TenantTable;
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
@TableName("roles")
@TenantTable
public class RolePO extends TenantAuditPO {
    private String name;
    private String memo;
    private Boolean enabled;
}
