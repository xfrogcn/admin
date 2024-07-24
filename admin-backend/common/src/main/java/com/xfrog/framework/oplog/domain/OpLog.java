package com.xfrog.framework.oplog.domain;

import com.xfrog.framework.domain.AuditEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter(AccessLevel.PROTECTED)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class OpLog extends AuditEntity {
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
    private Long tenantId;
}
