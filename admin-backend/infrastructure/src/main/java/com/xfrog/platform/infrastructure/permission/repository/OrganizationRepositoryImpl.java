package com.xfrog.platform.infrastructure.permission.repository;

import com.xfrog.platform.application.permission.dto.OrganizationDTO;
import com.xfrog.platform.application.permission.dto.QueryOrganizationRequestDTO;
import com.xfrog.platform.application.permission.repository.OrganizationRepository;
import com.xfrog.platform.infrastructure.permission.common.PermissionCacheNames;
import com.xfrog.platform.infrastructure.permission.converter.OrganizationPOConverter;
import com.xfrog.platform.infrastructure.permission.dataobject.OrganizationPO;
import com.xfrog.platform.infrastructure.permission.mapper.OrganizationMapper;
import com.xfrog.platform.infrastructure.persistent.repository.BaseCacheableApplicationRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrganizationRepositoryImpl extends BaseCacheableApplicationRepository<OrganizationDTO, OrganizationPO, OrganizationMapper>
        implements OrganizationRepository {

    public OrganizationRepositoryImpl(OrganizationMapper mapper) {
        super(mapper, OrganizationPOConverter.INSTANCE);
    }


    @Override
    public List<OrganizationDTO> queryBy(QueryOrganizationRequestDTO queryDTO) {
        return mapper.queryBy(queryDTO);
    }

    @Override
    public String getCacheName() {
        return PermissionCacheNames.ORGANIZATION_DETAIL;
    }
}
