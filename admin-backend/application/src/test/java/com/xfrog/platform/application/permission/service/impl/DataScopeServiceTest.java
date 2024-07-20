package com.xfrog.platform.application.permission.service.impl;

import com.xfrog.platform.application.permission.api.dto.DataScopeDTO;
import com.xfrog.platform.application.permission.api.dto.GrantDataScopeRequestDTO;
import com.xfrog.platform.application.permission.api.dto.OrganizationDTO;
import com.xfrog.platform.application.permission.dto.PermissionDTOFixtures;
import com.xfrog.platform.application.permission.repository.DataScopeRepository;
import com.xfrog.platform.application.permission.repository.OrganizationRepository;
import com.xfrog.platform.application.permission.repository.UserRepository;
import com.xfrog.platform.domain.permission.aggregate.PermissionFixtures;
import com.xfrog.platform.domain.permission.aggregate.UserRole;
import com.xfrog.platform.domain.permission.repository.DataScopeDomainRepository;
import com.xfrog.platform.domain.share.permission.DataScopeTargetType;
import com.xfrog.platform.domain.share.permission.DataScopeType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class )
class DataScopeServiceTest {
    @Mock
    private DataScopeDomainRepository dataScopeDomainRepository;
    @Mock
    private DataScopeRepository dataScopeRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private OrganizationRepository organizationRepository;
    @InjectMocks
    private DataScopeServiceImpl dataScopeService;

    @Test
    void grantDataScope_ShouldAddNewDataScopes_WhenNoneExist() {
        // Setup
        GrantDataScopeRequestDTO requestDTO = GrantDataScopeRequestDTO.builder()
                .targetType(DataScopeTargetType.ROLE)
                .targetId(1L)
                .scopeItems(List.of(
                        GrantDataScopeRequestDTO.DataScopeItem.builder()
                                .scopeType(DataScopeType.USER_ORGANIZATION).scopeId(0L).build(),
                        GrantDataScopeRequestDTO.DataScopeItem.builder()
                                .scopeType(DataScopeType.ORGANIZATION).scopeId(1L).build()
                ))
                .build();

        when(dataScopeDomainRepository.findByTargetTypeAndTargetId(requestDTO.getTargetType(), List.of(requestDTO.getTargetId())))
                .thenReturn(Collections.emptyList());

        // Execute
        dataScopeService.grantDataScope(requestDTO);

        // Verify
        verify(dataScopeDomainRepository, times(1))
                .saveAll(argThat(domain -> domain.size() == 2));
        verify(dataScopeDomainRepository, times(1))
                .logicDeleteAll(argThat(List::isEmpty));
    }

    @Test
    void grantDataScope_ShouldDeleteRemovedDataScopes_WhenNotInRequest() {
        // Setup
        GrantDataScopeRequestDTO requestDTO = GrantDataScopeRequestDTO.builder()
                .targetType(DataScopeTargetType.ROLE)
                .targetId(1L)
                .scopeItems(List.of(
                        GrantDataScopeRequestDTO.DataScopeItem.builder()
                                .scopeType(DataScopeType.USER_ORGANIZATION).scopeId(0L).build()
                ))
                .build();

        when(dataScopeDomainRepository.findByTargetTypeAndTargetId(requestDTO.getTargetType(), List.of(requestDTO.getTargetId())))
                .thenReturn(List.of(
                        PermissionFixtures.createDefaultDataScope()
                                .scopeType(DataScopeType.USER_ORGANIZATION)
                                .scopeId(0L)
                                .build(),
                        PermissionFixtures.createDefaultDataScope()
                                .scopeType(DataScopeType.ORGANIZATION)
                                .scopeId(1L)
                                .build()
                ));

        // Execute
        dataScopeService.grantDataScope(requestDTO);

        // Verify
        verify(dataScopeDomainRepository, times(1))
                .saveAll(argThat(List::isEmpty));
        verify(dataScopeDomainRepository, times(1))
                .logicDeleteAll(argThat(domain -> domain.size() == 1 && domain.get(0).getScopeType() == DataScopeType.ORGANIZATION));
    }

    @Test
    void getDataScopes_ShouldReturnDataScopes() {
        DataScopeDTO dataScope = PermissionDTOFixtures.defaultDataScopeDTO()
                .targetType(DataScopeTargetType.ROLE)
                .targetId(1L)
                .scopeType(DataScopeType.ORGANIZATION)
                .scopeId(1L)
                .build();
        OrganizationDTO organization = PermissionDTOFixtures.defaultOrganizationDTO(1L)
                .parentId(null)
                .parentIds(null)
                .build();

        when(dataScopeRepository.findByTargetTypeAndTargetId(DataScopeTargetType.ROLE, List.of(1L)))
                .thenReturn(List.of(dataScope));
        when(organizationRepository.queryByIds(List.of(1L))).thenReturn(List.of(organization));
        when(organizationRepository.queryByIds(List.of())).thenReturn(List.of());

        List<DataScopeDTO> result = dataScopeService.getDataScopes(DataScopeTargetType.ROLE, 1L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getScopeInfo()).isNotNull();
        assertThat(result.get(0).getScopeInfo().get("name")).isEqualTo(organization.getName());
        assertThat(result.get(0).getScopeInfo().get("id")).isEqualTo(organization.getId());
        assertThat(result.get(0).getScopeInfo().get("parentId")).isEqualTo(organization.getParentId());
        assertThat(result.get(0).getScopeInfo().get("parentIds")).isEqualTo(organization.getParentIds());

    }

    @Test
    void getUserDataScopes_ShouldReturnDataScopes() {
        UserRole userRole = PermissionFixtures.createDefaultUserRole(1L, 1L).build();
        DataScopeDTO dataScope1 = PermissionDTOFixtures.defaultDataScopeDTO()
                .targetType(DataScopeTargetType.ROLE)
                .targetId(1L)
                .scopeType(DataScopeType.USER_ORGANIZATION)
                .scopeId(0L)
                .build();
        DataScopeDTO dataScope2 = PermissionDTOFixtures.defaultDataScopeDTO()
                .targetType(DataScopeTargetType.USER)
                .targetId(1L)
                .scopeType(DataScopeType.ORGANIZATION)
                .scopeId(2L)
                .build();

        when(userRepository.queryUserRoleIds(1L)).thenReturn(List.of(userRole.getRoleId()));
        when(dataScopeRepository.findByTargetTypeAndTargetId(DataScopeTargetType.ROLE, List.of(1L)))
                .thenReturn(List.of(dataScope1));
        when(dataScopeRepository.findByTargetTypeAndTargetId(DataScopeTargetType.USER, List.of(1L)))
                .thenReturn(List.of(dataScope2));

        List<DataScopeDTO> dataScopes =  dataScopeService.getUserDataScopes(1L);
        assertThat(dataScopes).hasSize(2);
    }

    @Test
    void getUserDataScopes_ShouldReturnEmptyList() {
        List<DataScopeDTO> dataScopes =  dataScopeService.getUserDataScopes(null);
        assertThat(dataScopes).isEmpty();
    }

    @Test
    void fillOrganizationScopeInfo_WithNonEmptyDataScopesAndOrganizations_ShouldFillScopeInfo() {
        // Arrange
        DataScopeDTO dataScope = PermissionDTOFixtures.defaultDataScopeDTO()
                .targetType(DataScopeTargetType.ROLE)
                .targetId(1L)
                .scopeType(DataScopeType.ORGANIZATION)
                .scopeId(2L)
                .build();
        OrganizationDTO organization = PermissionDTOFixtures.defaultOrganizationDTO(2l)
                .parentId(1L)
                .parentIds(List.of(1L))
                .build();
        OrganizationDTO parent =  PermissionDTOFixtures.defaultOrganizationDTO(1l)
                .parentId(null)
                .parentIds(null)
                .name("parent")
                .build();

        when(organizationRepository.queryByIds(List.of(2L))).thenReturn(List.of(organization));
        when(organizationRepository.queryByIds(List.of(1L))).thenReturn(List.of(parent));

        List<DataScopeDTO> dataScopes = Collections.singletonList(dataScope);


        // Act
        dataScopeService.fillOrganizationScopeInfo(dataScopes);

        // Assert
        Map<String, Object> scopeInfo = dataScope.getScopeInfo();
        assertThat(scopeInfo).isNotNull();
        assertThat(scopeInfo.get("name")).isEqualTo(organization.getName());
        assertThat(scopeInfo.get("id")).isEqualTo(organization.getId());
        assertThat(scopeInfo.get("parentId")).isEqualTo(organization.getParentId());
        assertThat(scopeInfo.get("parentIds")).isEqualTo(organization.getParentIds());
        assertThat((List<String>)scopeInfo.get("parentNames")).containsExactly(parent.getName());
    }

    @Test
    void fillOrganizationScopeInfo_WithEmptyDataScopes_ShouldNotFillScopeInfo() {
        // Arrange
        List<DataScopeDTO> dataScopes = Collections.emptyList();

        // Act
        dataScopeService.fillOrganizationScopeInfo(dataScopes);

        // Assert
        verify(organizationRepository, never()).queryByIds(anyList());
    }

    @Test
    void fillOrganizationScopeInfo_WithEmptyOrganizationIds_ShouldNotFillScopeInfo() {
        // Arrange
        DataScopeDTO dataScope1 = PermissionDTOFixtures.defaultDataScopeDTO()
                .targetType(DataScopeTargetType.ROLE)
                .targetId(1L)
                .scopeType(DataScopeType.USER_ORGANIZATION)
                .scopeId(0L)
                .build();
        List<DataScopeDTO> dataScopes = List.of(dataScope1);

        // Act
        dataScopeService.fillOrganizationScopeInfo(dataScopes);

        // Assert
        verify(organizationRepository, never()).queryByIds(anyList());
    }
}
