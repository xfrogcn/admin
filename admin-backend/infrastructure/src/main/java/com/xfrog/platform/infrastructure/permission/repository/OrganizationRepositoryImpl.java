package com.xfrog.platform.infrastructure.permission.repository;

import com.xfrog.platform.application.permission.api.dto.OrganizationDTO;
import com.xfrog.platform.application.permission.api.dto.QueryOrganizationRequestDTO;
import com.xfrog.platform.application.permission.repository.OrganizationRepository;
import com.xfrog.platform.infrastructure.permission.mapper.OrganizationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrganizationRepositoryImpl implements OrganizationRepository {
    private final OrganizationMapper organizationMapper;
    @Override
    public List<OrganizationDTO> queryAll(QueryOrganizationRequestDTO queryDTO) {
        return organizationMapper.queryAll(queryDTO);
    }
}
