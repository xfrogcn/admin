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
@TableName("tenants")
public class TenantPO extends AuditPO {
    private String code;
    private String name;
    private Long organizationId;
    private Boolean enabled;
    private String adminUserName;
    private Long adminUserId;
    private String memo;
}
