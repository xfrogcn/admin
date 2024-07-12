package com.xfrog.platform.infrastructure.permission.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xfrog.platform.application.permission.api.dto.OrganizationDTO;
import com.xfrog.platform.application.permission.api.dto.QueryOrganizationRequestDTO;
import com.xfrog.platform.application.permission.repository.OrganizationRepository;
import com.xfrog.platform.infrastructure.permission.converter.OrganizationPOConverter;
import com.xfrog.platform.infrastructure.permission.dataobject.OrganizationPO;
import com.xfrog.platform.infrastructure.permission.mapper.OrganizationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrganizationRepositoryImpl implements OrganizationRepository {
    private final OrganizationMapper organizationMapper;
    @Override
    public List<OrganizationDTO> queryAll(QueryOrganizationRequestDTO queryDTO) {
        return organizationMapper.queryAll(queryDTO);
    }

    @Override
    public OrganizationDTO queryById(Long organizationId) {
        LambdaQueryWrapper<OrganizationPO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrganizationPO::getDeleted, false);
        queryWrapper.eq(OrganizationPO::getId, organizationId);

        return OrganizationPOConverter.INSTANCE.toDTO(organizationMapper.selectOne(queryWrapper));
    }

    @Override
    public List<OrganizationDTO> queryByIds(List<Long> organizationIds) {
        if (CollectionUtils.isEmpty(organizationIds)) {
            return new ArrayList<>();
        }
        LambdaQueryWrapper<OrganizationPO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrganizationPO::getDeleted, false);
        queryWrapper.in(OrganizationPO::getId, organizationIds);
        List<OrganizationPO> organizations =  organizationMapper.selectList(queryWrapper);
        return OrganizationPOConverter.INSTANCE.toDTOList(organizations);
    }
}
