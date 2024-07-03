package com.xfrog.platform.infrastructure.permission.dataobject;

import com.baomidou.mybatisplus.annotation.TableName;
import com.xfrog.framework.po.AuditPO;
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
@TableName("role_permission_items")
public class RolePermissionItemPO extends AuditPO {
    private Long roleId;
    private Long permissionItemId;
}
