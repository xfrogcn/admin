package com.xfrog.platform.infrastructure.permission.repository;

import com.xfrog.platform.application.permission.api.dto.QueryTenantRequestDTO;
import com.xfrog.platform.application.permission.api.dto.TenantDTO;
import com.xfrog.platform.application.permission.repository.TenantRepository;
import com.xfrog.platform.infrastructure.permission.converter.TenantPOConverter;
import com.xfrog.platform.infrastructure.permission.dataobject.TenantPO;
import com.xfrog.platform.infrastructure.permission.mapper.TenantMapper;
import com.xfrog.platform.infrastructure.persistent.repository.BasePageableApplicationRepository;
import org.apache.commons.collections4.map.CaseInsensitiveMap;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class TenantRepositoryImpl extends BasePageableApplicationRepository<TenantDTO, TenantPO, TenantMapper, QueryTenantRequestDTO>
        implements TenantRepository {

    public TenantRepositoryImpl(TenantMapper tenantMapper) {
        super(tenantMapper, TenantPOConverter.INSTANCE);
    }

    private static final CaseInsensitiveMap<String, String> ORDER_FIELD_MAP =
            new CaseInsensitiveMap<>(Map.of(
                    "createdTime", "t.created_time",
                    "name", "t.name"));

    @Override
    protected Map<String, String> orderedFieldMap() {
        return ORDER_FIELD_MAP;
    }

}
