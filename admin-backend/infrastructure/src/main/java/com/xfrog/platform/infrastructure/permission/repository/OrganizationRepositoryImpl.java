package com.xfrog.platform.infrastructure.permission.repository;

import com.xfrog.platform.application.permission.api.dto.OrganizationDTO;
import com.xfrog.platform.application.permission.api.dto.QueryOrganizationRequestDTO;
import com.xfrog.platform.application.permission.repository.OrganizationRepository;
import com.xfrog.platform.infrastructure.permission.converter.OrganizationPOConverter;
import com.xfrog.platform.infrastructure.permission.dataobject.OrganizationPO;
import com.xfrog.platform.infrastructure.permission.mapper.OrganizationMapper;
import com.xfrog.platform.infrastructure.persistent.repository.BaseApplicationRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrganizationRepositoryImpl extends BaseApplicationRepository<OrganizationDTO, OrganizationPO, OrganizationMapper>
        implements OrganizationRepository {

    public OrganizationRepositoryImpl(OrganizationMapper mapper) {
        super(mapper, OrganizationPOConverter.INSTANCE);
    }


    @Override
    public List<OrganizationDTO> queryBy(QueryOrganizationRequestDTO queryDTO) {
        return mapper.queryBy(queryDTO);
    }
}
