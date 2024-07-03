package com.xfrog.platform.infrastructure.permission.dataobject;

import com.baomidou.mybatisplus.annotation.TableName;
import com.xfrog.framework.po.TenantAuditPO;
import com.xfrog.platform.infrastructure.persistent.config.DataScopeColumn;
import com.xfrog.platform.infrastructure.persistent.config.DataScopeTable;
import com.xfrog.platform.infrastructure.persistent.config.TenantTable;
import com.xfrog.platform.infrastructure.persistent.handler.datascope.OrganizationDataScopeHandler;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@TableName(value = "users")
@TenantTable
@DataScopeTable(
        value = OrganizationDataScopeHandler.class,
        columns = {@DataScopeColumn(handler = OrganizationDataScopeHandler.ORGANIZATION_DATA_SCOPE_HANDLER_NAME,
                column = "organization_id")})
public class UserPO extends TenantAuditPO {
    private String userName;
    private String password;
    private Long organizationId;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;
    private String name;
    private String sex;
    private String mobilePhone;
    private String email;
    private Timestamp lastLoginTime;
}