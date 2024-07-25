package com.xfrog.platform.infrastructure.base.dataobject;

import com.baomidou.mybatisplus.annotation.TableName;
import com.xfrog.framework.po.TenantAuditPO;
import com.xfrog.platform.infrastructure.persistent.config.TenantTable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * 操作日志
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@TableName("op_logs")
@TenantTable
public class OpLogPO extends TenantAuditPO {
    private String bizId;
    private String bizCode;
    private String requestId;
    private String tag;
    private String bizType;
    private String bizAction;
    private String extra;
    private String message;
    private Long operatorId;
    private Boolean success;
    private Long executeTime;
}
