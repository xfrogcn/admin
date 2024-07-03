package com.xfrog.platform.infrastructure.persistent.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.xfrog.framework.common.DateTimeUtils;
import com.xfrog.framework.common.UidGenerator;
import com.xfrog.framework.po.AuditPO;
import com.xfrog.framework.po.IdPO;
import com.xfrog.framework.po.TenantPO;
import com.xfrog.framework.principal.CurrentPrincipalContext;
import com.xfrog.framework.principal.PrincipalInfo;
import com.xfrog.platform.application.common.RequestThreadMarkContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class AutoFillMetaObjectHandler implements MetaObjectHandler {

    private final UidGenerator uidGenerator;
    @Override
    public void insertFill(MetaObject metaObject) {
        Object originalObject = metaObject.getOriginalObject();
        if (originalObject instanceof IdPO idPO) {
            if (idPO.getId() == null) {
                idPO.setId(uidGenerator.nextId());
            }
        }

        if (originalObject instanceof TenantPO tenantPO) {
            if (!StringUtils.hasText(tenantPO.getTenantId())) {
                tenantPO.setTenantId(RequestThreadMarkContext.currentTenantId());
            }
        }

        if (originalObject instanceof AuditPO auditPO) {
            PrincipalInfo principalInfo = getCurrentPrincipal();
            if (Objects.isNull(auditPO.getCreatedBy())) {
                auditPO.setCreatedBy(principalInfo.getUserId());
            }
            if (Objects.isNull(auditPO.getUpdatedBy())) {
                auditPO.setUpdatedBy(principalInfo.getUserId());
            }
            if (Objects.isNull(auditPO.getCreatedTime())) {
                auditPO.setCreatedTime(DateTimeUtils.utcNow());
            }
            if (Objects.isNull(auditPO.getUpdatedTime())) {
                auditPO.setUpdatedTime(DateTimeUtils.utcNow());
            }
            if (Objects.isNull(auditPO.getDeleted())) {
                auditPO.setDeleted(false);
            }
            if (Boolean.TRUE.equals(auditPO.getDeleted())) {
                auditPO.setDeletedTime(DateTimeUtils.utcNow());
                auditPO.setDeletedBy(principalInfo.getUserId());
            }
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        Object originalObject = metaObject.getOriginalObject();

        if (originalObject instanceof AuditPO auditPO) {
            PrincipalInfo principalInfo = getCurrentPrincipal();

            if (Boolean.TRUE.equals(auditPO.getDeleted())) {
                auditPO.setDeletedTime(DateTimeUtils.utcNow());
                auditPO.setDeletedBy(principalInfo.getUserId());
            } else {
                auditPO.setUpdatedBy(principalInfo.getUserId());
                auditPO.setUpdatedTime(DateTimeUtils.utcNow());
                auditPO.setDeletedTime(null);
                auditPO.setDeletedBy(null);
            }
        }
    }

    private PrincipalInfo getCurrentPrincipal() {
        return CurrentPrincipalContext.currentPrincipalOrSystem();
    }
}
