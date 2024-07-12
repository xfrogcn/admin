package com.xfrog.platform.application.permission.service.impl;

import com.xfrog.framework.exception.business.AlreadyExistsException;
import com.xfrog.framework.exception.business.FailedPreconditionException;
import com.xfrog.framework.exception.business.NotFoundException;
import com.xfrog.platform.application.permission.api.dto.CreateOrganizationRequestDTO;
import com.xfrog.platform.application.permission.api.dto.OrganizationDTO;
import com.xfrog.platform.application.permission.api.dto.QueryOrganizationRequestDTO;
import com.xfrog.platform.application.permission.api.dto.UpdateOrganizationRequestDTO;
import com.xfrog.platform.application.permission.dto.PermissionDTOFixtures;
import com.xfrog.platform.application.permission.repository.OrganizationRepository;
import com.xfrog.platform.domain.permission.aggregate.Organization;
import com.xfrog.platform.domain.permission.aggregate.PermissionFixtures;
import com.xfrog.platform.domain.permission.repository.OrganizationDomainRepository;
import com.xfrog.platform.domain.share.permission.OrganizationStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrganizationServiceImplTest {

    @Mock
    private OrganizationDomainRepository organizationDomainRepository;
    @Mock
    private OrganizationRepository organizationRepository;
    @InjectMocks
    private OrganizationServiceImpl organizationService;

    @Test
    void createOrganization_WhenParentNotFound_ShouldThrowNotFoundException() {

        CreateOrganizationRequestDTO requestDTO = PermissionDTOFixtures.defaultCreateOrganizationRequestDTO()
                        .parentId(1L).build();

        when(organizationDomainRepository.findById(1L)).thenReturn(null);

        assertThrows(NotFoundException.class, () -> organizationService.createOrganization(requestDTO));
    }

    @Test
    void createOrganization_WhenOrganizationNameExists_ShouldThrowBusinessException() {
        Organization parent = PermissionFixtures.createDefaultOrganization().parentId(null).name("parent").build();
        CreateOrganizationRequestDTO requestDTO = PermissionDTOFixtures.defaultCreateOrganizationRequestDTO()
                .parentId(parent.getId()).build();

        when(organizationDomainRepository.findById(parent.getId())).thenReturn(parent);
        when(organizationDomainRepository.existsByName(requestDTO.getName(), parent.getId(), null)).thenReturn(true);

        assertThrows(AlreadyExistsException.class, () -> organizationService.createOrganization(requestDTO));
    }

    @Test
    void createOrganization_ShouldCreateOrganizationAndReturnId() {
        Organization parent = PermissionFixtures.createDefaultOrganization().parentId(null)
                .code("0001").level(1).name("parent").build();
        CreateOrganizationRequestDTO requestDTO = PermissionDTOFixtures.defaultCreateOrganizationRequestDTO()
                .parentId(parent.getId()).build();

        when(organizationDomainRepository.findById(parent.getId())).thenReturn(parent);
        when(organizationDomainRepository.existsByName(requestDTO.getName(), parent.getId(), null)).thenReturn(false);
        when(organizationDomainRepository.getMaxSeq(parent.getId())).thenReturn(0);
        when(organizationDomainRepository.save(any(Organization.class))).thenReturn(PermissionFixtures.createDefaultOrganization().build());

        Long resultId = organizationService.createOrganization(requestDTO);

        assertNotNull(resultId);
        verify(organizationDomainRepository, times(1))
                .save(argThat(domain -> domain.getLevel() == 2
                        && domain.getName().equals(requestDTO.getName())
                        && domain.getParentId().equals(parent.getId())
                        && domain.getCode().equals("00010001")
                        && domain.getSeq() == 1
                        && domain.getStatus() == OrganizationStatus.NORMAL
                        && Objects.equals(domain.getDisplayOrder(), requestDTO.getDisplayOrder())
                        && domain.getParentIds().equals(List.of(parent.getId()))
                        && domain.getPrincipal().equals(requestDTO.getPrincipal())
                        && domain.getTelephone().equals(requestDTO.getTelephone())
                ));
    }


    @Test
    void updateOrganization_ShouldThrowNotFoundExceptionWhenOrganizationNotFound() {
        when(organizationDomainRepository.findById(1L)).thenReturn(null);
        assertThrows(NotFoundException.class, () -> organizationService.updateOrganization(1L, new UpdateOrganizationRequestDTO()));
    }

    @Test
    void updateOrganization_ShouldThrowBusinessExceptionWhenNameExists() {
        Organization organization = PermissionFixtures.createDefaultOrganization().build();
        UpdateOrganizationRequestDTO updateOrganizationRequestDTO = PermissionDTOFixtures.defaultUpdateOrganizationRequestDTO()
                .name("updated_name")
                .build();
        when(organizationDomainRepository.findById(organization.getId())).thenReturn(organization);
        when(organizationDomainRepository.existsByName(updateOrganizationRequestDTO.getName(), organization.getParentId(), List.of(organization.getId()))).thenReturn(true);
        assertThrows(AlreadyExistsException.class, () -> organizationService.updateOrganization(organization.getId(), updateOrganizationRequestDTO));
    }

    @Test
    void updateOrganization_ShouldSuccess() {
        Organization organization = PermissionFixtures.createDefaultOrganization().build();
        UpdateOrganizationRequestDTO updateOrganizationRequestDTO = PermissionDTOFixtures.defaultUpdateOrganizationRequestDTO()
                .name("updated_name")
                .displayOrder(1)
                .principal("updated_principal")
                .status(OrganizationStatus.DISABLED)
                .telephone("updated_telephone")
                .build();

        when(organizationDomainRepository.findById(organization.getId())).thenReturn(organization);
        when(organizationDomainRepository.existsByName(updateOrganizationRequestDTO.getName(), organization.getParentId(), List.of(organization.getId()))).thenReturn(false);


        organizationService.updateOrganization(organization.getId(), updateOrganizationRequestDTO);

        verify(organizationDomainRepository, times(1))
                .save(argThat(domain -> domain.getName().equals(updateOrganizationRequestDTO.getName())
                        && domain.getStatus() == OrganizationStatus.DISABLED
                        && Objects.equals(domain.getDisplayOrder(), updateOrganizationRequestDTO.getDisplayOrder())
                        && domain.getPrincipal().equals(updateOrganizationRequestDTO.getPrincipal())
                        && domain.getTelephone().equals(updateOrganizationRequestDTO.getTelephone())
                ));
    }


    @Test
    void deleteOrganization_ShouldThrowFailedPreconditionExceptionWhenDeletingRootOrganization() {
        assertThrows(FailedPreconditionException.class, () -> organizationService.deleteOrganization(1L));
    }

    @Test
    void deleteOrganization_ShouldThrowNotFoundExceptionWhenOrganizationDoesNotExist() {
        when(organizationDomainRepository.findById(2L)).thenReturn(null);
        assertThrows(NotFoundException.class, () -> organizationService.deleteOrganization(2L));
    }

    @Test
    void deleteOrganization_ShouldThrowFailedPreconditionExceptionWhenDeletingOrganizationWithoutParent() {
        Organization organization = PermissionFixtures.createDefaultOrganization().parentId(null).build();

        when(organizationDomainRepository.findById(organization.getId())).thenReturn(organization);
        assertThrows(FailedPreconditionException.class, () -> organizationService.deleteOrganization(organization.getId()));
    }

    @Test
    void deleteOrganization_ShouldThrowBusinessExceptionWhenOrganizationHasChildren() {
        Organization organization = PermissionFixtures.createDefaultOrganization().parentId(1L).build();

        when(organizationDomainRepository.findById(organization.getId())).thenReturn(organization);
        when(organizationDomainRepository.existsChildren(organization.getId())).thenReturn(true);
        assertThrows(FailedPreconditionException.class, () -> organizationService.deleteOrganization(organization.getId()));
    }

    @Test
    void deleteOrganization_ShouldSucceedWhenConditionsMet() {
        Organization organization = PermissionFixtures.createDefaultOrganization().parentId(1L).build();

        when(organizationDomainRepository.findById(organization.getId())).thenReturn(organization);
        when(organizationDomainRepository.existsChildren(organization.getId())).thenReturn(false);

        organizationService.deleteOrganization(organization.getId());
        verify(organizationDomainRepository, times(1)).logicDelete(organization.getId());
    }

    @Test
    void listOrganizations_ShouldSucceed() {
        QueryOrganizationRequestDTO queryDTO = QueryOrganizationRequestDTO.builder()
                .status(OrganizationStatus.NORMAL)
                .build();

        organizationService.listOrganizations(queryDTO);
        verify(organizationRepository, times(1)).queryAll(queryDTO);
    }

    @Test
    void getOrganization_ShouldSucceed() {
        OrganizationDTO parent = PermissionDTOFixtures.defaultOrganizationDTO()
                .id(1L)
                .parentIds(null)
                .parentId(null)
                .name("parent")
                        .build();
        OrganizationDTO organization = PermissionDTOFixtures.defaultOrganizationDTO()
                .id(2L)
                .parentIds(List.of(1L))
                .parentId(1L)
                .name("organization")
                .build();

        when(organizationRepository.queryById(organization.getId())).thenReturn(organization);
        when(organizationRepository.queryByIds(organization.getParentIds())).thenReturn(List.of(parent));

        OrganizationDTO result = organizationService.getOrganization(organization.getId());
        assertThat(result.getParentNames()).containsExactly(parent.getName());
    }
}