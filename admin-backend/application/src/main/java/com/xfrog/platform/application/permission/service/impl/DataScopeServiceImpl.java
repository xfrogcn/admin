package com.xfrog.platform.application.permission.service.impl;

import com.xfrog.framework.common.ListComparator;
import com.xfrog.platform.application.permission.api.dto.DataScopeDTO;
import com.xfrog.platform.application.permission.api.dto.GrantDataScopeRequestDTO;
import com.xfrog.platform.application.permission.api.dto.OrganizationDTO;
import com.xfrog.platform.application.permission.repository.DataScopeRepository;
import com.xfrog.platform.application.permission.repository.OrganizationRepository;
import com.xfrog.platform.application.permission.repository.UserRepository;
import com.xfrog.platform.application.permission.service.DataScopeService;
import com.xfrog.platform.domain.permission.aggregate.DataScope;
import com.xfrog.platform.domain.permission.repository.DataScopeDomainRepository;
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
    private final DataScopeRepository dataScopeRepository;
    private final UserRepository userRepository;
    private final OrganizationRepository organizationRepository;

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
        dataScopeRepository.removeCacheByTargetTypeAndTargetId(requestDTO.getTargetType(), requestDTO.getTargetId());
    }

    @Override
    public List<DataScopeDTO> getDataScopes(DataScopeTargetType targetType, Long targetId) {
        List<DataScopeDTO> result = dataScopeRepository.findByTargetTypeAndTargetId(targetType, List.of(targetId));
        fillOrganizationScopeInfo(result);
        return result;
    }

    @Override
    public List<DataScopeDTO> getUserDataScopes(Long userId) {
        if (userId == null) {
            return new LinkedList<>();
        }
        List<Long> userRoleIds = userRepository.queryUserRoleIds(userId);
        List<DataScopeDTO> result = new LinkedList<>();
        if (!CollectionUtils.isEmpty(userRoleIds)) {
            List<DataScopeDTO> roleDataScopes = dataScopeRepository.findByTargetTypeAndTargetId(DataScopeTargetType.ROLE,
                    userRoleIds);
            result.addAll(roleDataScopes);
        }

        result.addAll(dataScopeRepository.findByTargetTypeAndTargetId(DataScopeTargetType.USER, List.of(userId)));

        fillOrganizationScopeInfo(result);

        return result;
    }

    protected void fillOrganizationScopeInfo(List<DataScopeDTO> dataScopes) {
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
        List<OrganizationDTO> organizations = organizationRepository.queryByIds(organizationIds);
        List<Long> parentOrganizationIds = organizations.stream().filter(it -> it.getParentIds() != null).flatMap(it -> it.getParentIds().stream())
                .distinct()
                .filter(it -> !organizationIds.contains(it))
                .toList();
        List<OrganizationDTO> parentOrganizations = organizationRepository.queryByIds(parentOrganizationIds);

        Map<Long, OrganizationDTO> organizationMap = Stream.concat(organizations.stream(), parentOrganizations.stream())
                .collect(Collectors.toMap(OrganizationDTO::getId, Function.identity()));

        dataScopes.stream()
            .filter(it -> it.getScopeType() == DataScopeType.ORGANIZATION)
            .forEach(item -> {
                OrganizationDTO organization = organizationMap.get(item.getScopeId());
                Map<String, Object> scopeInfo = new HashMap<>();
                if (organization != null) {
                    scopeInfo.put("id", organization.getId());
                    scopeInfo.put("name", organization.getName());
                    scopeInfo.put("parentId", organization.getParentId());
                    scopeInfo.put("parentIds", organization.getParentIds());
                    if (!CollectionUtils.isEmpty(organization.getParentIds())) {
                        scopeInfo.put("parentNames", organization.getParentIds().stream()
                                .map(organizationMap::get)
                                .filter(Objects::nonNull)
                                .map(OrganizationDTO::getName)
                                .toList());
                    }
                    item.setScopeInfo(scopeInfo);
                }
            });

    }
}
