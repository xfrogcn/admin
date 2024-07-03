package com.xfrog.platform.infrastructure.permission.dataobject;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xfrog.framework.po.TenantAuditPO;
import com.xfrog.platform.domain.share.permission.OrganizationStatus;
import com.xfrog.platform.infrastructure.persistent.config.DataScopeColumn;
import com.xfrog.platform.infrastructure.persistent.config.DataScopeTable;
import com.xfrog.platform.infrastructure.persistent.config.TenantTable;
import com.xfrog.platform.infrastructure.persistent.handler.datascope.OrganizationDataScopeHandler;
import com.xfrog.platform.infrastructure.persistent.typehandler.LongListTypeHandler;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@TableName("organizations")
@TenantTable
@DataScopeTable(
        value = OrganizationDataScopeHandler.class,
        columns = {@DataScopeColumn(handler = OrganizationDataScopeHandler.ORGANIZATION_DATA_SCOPE_HANDLER_NAME,
                column = "id")})
public class OrganizationPO extends TenantAuditPO {
    private String name;
    private Long parentId;
    @TableField(typeHandler = LongListTypeHandler.class)
    private List<Long> parentIds;
    private String code;
    private Integer level;
    private Integer seq;
    private OrganizationStatus status;
    private Integer displayOrder;
    private String telephone;
    private String principal;
}
