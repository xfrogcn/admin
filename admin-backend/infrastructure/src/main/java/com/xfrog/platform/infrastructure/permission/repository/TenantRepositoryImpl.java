package com.xfrog.platform.infrastructure.permission.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xfrog.framework.dto.PageDTO;
import com.xfrog.platform.application.permission.api.dto.QueryTenantRequestDTO;
import com.xfrog.platform.application.permission.api.dto.TenantDTO;
import com.xfrog.platform.application.permission.repository.TenantRepository;
import com.xfrog.platform.infrastructure.permission.mapper.TenantMapper;
import com.xfrog.platform.infrastructure.util.PageUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.map.CaseInsensitiveMap;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class TenantRepositoryImpl implements TenantRepository {

    private final TenantMapper tenantMapper;

    private static final CaseInsensitiveMap<String, String> ORDER_FIELD_MAP =
            new CaseInsensitiveMap<>(Map.of(
                    "createdTime", "t.created_time",
                    "name", "t.name"));

    @Override
    public PageDTO<TenantDTO> queryAllBy(QueryTenantRequestDTO queryDTO) {
        Page<TenantDTO> page = PageUtils.page(queryDTO, ORDER_FIELD_MAP);
        List<TenantDTO> tenantDTOS = tenantMapper.queryAllBy(queryDTO, page);
        return PageUtils.result(page, tenantDTOS);
    }
}
