package com.xfrog.platform.application.permission.service.impl;

import com.xfrog.framework.common.ListComparator;
import com.xfrog.framework.domain.IdEntity;
import com.xfrog.platform.application.permission.api.dto.DataScopeDTO;
import com.xfrog.platform.application.permission.api.dto.GrantDataScopeRequestDTO;
import com.xfrog.platform.application.permission.converter.DataScopeDTOConverter;
import com.xfrog.platform.application.permission.service.DataScopeService;
import com.xfrog.platform.domain.permission.aggregate.DataScope;
import com.xfrog.platform.domain.permission.aggregate.Organization;
import com.xfrog.platform.domain.permission.aggregate.UserRole;
import com.xfrog.platform.domain.permission.repository.DataScopeDomainRepository;
import com.xfrog.platform.domain.permission.repository.OrganizationDomainRepository;
import com.xfrog.platform.domain.permission.repository.UserRoleDomainRepository;
import com.xfrog.platform.domain.share.permission.DataScopeTargetType;
import com.xfrog.platform.domain.share.permission.DataScopeType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class DataScopeServiceImpl implements DataScopeService {

    private final DataScopeDomainRepository dataScopeDomainRepository;
    private final UserRoleDomainRepository userRoleDomainRepository;
    private final OrganizationDomainRepository organizationDomainRepository;

    @Override
    public void grantDataScope(GrantDataScopeRequestDTO requestDTO) {
        List<DataScope> originalDataScopes = dataScopeDomainRepository.findByTargetTypeAndTargetId(requestDTO.getTargetType(), List.of(requestDTO.getTargetId()));

        ListComparator.CompareResult<DataScope, GrantDataScopeRequestDTO.DataScopeItem> compareResult =
                ListComparator.compare(
                        originalDataScopes,
                        requestDTO.getScopeItems(),
                        (originalDataScope, scopeItem) -> Objects.equals(originalDataScope.getScopeId(), scopeItem.getScopeId()) && originalDataScope.getScopeType() == scopeItem.getScopeType());

        List<DataScope> added = compareResult.getAdded().stream()
                .map(dataScopeItem -> DataScope.builder()
                        .targetId(requestDTO.getTargetId())
                        .targetType(requestDTO.getTargetType())
                        .scopeId(dataScopeItem.getScopeId())
                        .scopeType(dataScopeItem.getScopeType())
                        .build()).collect(Collectors.toList());

        dataScopeDomainRepository.saveAll(added);
        dataScopeDomainRepository.logicDeleteAll(compareResult.getRemoved());
    }

    @Override
    public List<DataScopeDTO> getDataScopes(DataScopeTargetType targetType, Long targetId) {
        List<DataScope> dataScopes = dataScopeDomainRepository.findByTargetTypeAndTargetId(targetType, List.of(targetId));

        List<DataScopeDTO> result = DataScopeDTOConverter.INSTANCE.toDTOList(dataScopes);
        fillOrganizationScopeInfo(result);
        return result;
    }

    @Override
    public List<DataScopeDTO> getUserDataScopes(Long userId) {
        if (userId == null) {
            return new LinkedList<>();
        }
        List<UserRole> userRoles = userRoleDomainRepository.getByUserId(userId);
        List<DataScopeDTO> result = new LinkedList<>();
        if (!CollectionUtils.isEmpty(userRoles)) {
            List<DataScope> roleDataScopes = dataScopeDomainRepository.findByTargetTypeAndTargetId(DataScopeTargetType.ROLE,
                    userRoles.stream().map(UserRole::getRoleId).toList());
            result.addAll(DataScopeDTOConverter.INSTANCE.toDTOList(roleDataScopes));
        }

        result.addAll(DataScopeDTOConverter.INSTANCE.toDTOList(dataScopeDomainRepository.findByTargetTypeAndTargetId(DataScopeTargetType.USER, List.of(userId))));

        fillOrganizationScopeInfo(result);

        return result;
    }

    private void fillOrganizationScopeInfo(List<DataScopeDTO> dataScopes) {
        if (CollectionUtils.isEmpty(dataScopes)) {
            return;
        }

        List<Long> organizationIds = dataScopes.stream()
                .filter(it -> it.getScopeType() == DataScopeType.ORGANIZATION)
                .map(DataScopeDTO::getScopeId)
                .distinct()
                .toList();
        if (CollectionUtils.isEmpty(organizationIds)) {
            return;
        }
        List<Organization> organizations = organizationDomainRepository.findByIds(organizationIds);
        List<Long> parentOrganizationIds = organizations.stream().filter(it -> it.getParentIds() != null).flatMap(it -> it.getParentIds().stream())
                .distinct()
                .filter(it -> !organizationIds.contains(it))
                .toList();
        List<Organization> parentOrganizations = organizationDomainRepository.findByIds(parentOrganizationIds);

        Map<Long, Organization> organizationMap = Stream.concat(organizations.stream(), parentOrganizations.stream())
                .collect(Collectors.toMap(IdEntity::getId, Function.identity()));

        dataScopes.stream()
            .filter(it -> it.getScopeType() == DataScopeType.ORGANIZATION)
            .forEach(item -> {
                Organization organization = organizationMap.get(item.getScopeId());
                Map<String, Object> scopeInfo = new HashMap<>();
                if (organization != null) {
                    scopeInfo.put("id", organization.getId());
                    scopeInfo.put("name", organization.getName());
                    scopeInfo.put("parentIds", organization.getParentIds());
                    if (!CollectionUtils.isEmpty(organization.getParentIds())) {
                        scopeInfo.put("parentNames", organization.getParentIds().stream()
                                .map(organizationMap::get)
                                .filter(Objects::nonNull)
                                .map(Organization::getName)
                                .toList());
                    }
                    item.setScopeInfo(scopeInfo);
                }
            });

    }
}
