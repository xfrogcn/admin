package com.xfrog.platform.application.permission.service.impl;

import com.xfrog.framework.exception.BusinessException;
import com.xfrog.framework.exception.business.FailedPreconditionException;
import com.xfrog.framework.exception.business.NotFoundException;
import com.xfrog.platform.application.permission.api.dto.CreateOrganizationRequestDTO;
import com.xfrog.platform.application.permission.api.dto.OrganizationDTO;
import com.xfrog.platform.application.permission.api.dto.QueryOrganizationRequestDTO;
import com.xfrog.platform.application.permission.api.dto.UpdateOrganizationRequestDTO;
import com.xfrog.platform.application.permission.converter.OrganizationDTOToCommandConverter;
import com.xfrog.platform.application.permission.repository.OrganizationRepository;
import com.xfrog.platform.application.permission.service.OrganizationService;
import com.xfrog.platform.domain.permission.aggregate.Organization;
import com.xfrog.platform.domain.permission.command.CreateOrganizationCommand;
import com.xfrog.platform.domain.permission.command.UpdateOrganizationCommand;
import com.xfrog.platform.domain.permission.repository.OrganizationDomainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrganizationServiceImpl implements OrganizationService {

    private final OrganizationDomainRepository organizationDomainRepository;
    private final OrganizationRepository organizationRepository;

    @Override
    @Transactional
    public Long createOrganization(CreateOrganizationRequestDTO organizationRequestDTO) {
        Organization parent = null;
        if (organizationRequestDTO.getParentId() != null) {
            parent = organizationDomainRepository.findById(organizationRequestDTO.getParentId());
        } else {
            parent = Organization.ROOT_ORGANIZATION;
        }
        if (parent == null) {
            throw new NotFoundException("parent organization not found");
        }

        if (organizationDomainRepository.existsByName(organizationRequestDTO.getName(), parent.getId(), null)) {
            throw new BusinessException("organization name exists");
        }

        int seq = getMaxSeq(parent) + 1;

        CreateOrganizationCommand command = OrganizationDTOToCommandConverter.INSTANCE.toCreateCommand(organizationRequestDTO);
        command.setCode(generateCode(parent, seq));
        command.setLevel(parent.getLevel() + 1);
        command.setParentIds(parent.getAllLevelIds());
        command.setSeq(seq);
        Organization organization = Organization.create(command);

        organization = organizationDomainRepository.save(organization);

        return organization.getId();
    }

    protected int getMaxSeq(Organization parent) {
        return organizationDomainRepository.getMaxSeq(parent.getId());
    }

    protected String generateCode(Organization parent, int seq) {
        return String.format("%s%04d", parent.getCode(), seq);
    }

    @Override
    @Transactional
    public void updateOrganization(Long organizationId, UpdateOrganizationRequestDTO updateOrganizationRequestDTO) {
        Organization organization = organizationDomainRepository.findById(organizationId);
        if (organization == null) {
            throw new NotFoundException("Organization Not Found");
        }
        if (organizationDomainRepository.existsByName(updateOrganizationRequestDTO.getName(),
                organization.getParentId(),
                List.of(organizationId))) {
            throw new BusinessException("organization name exists");
        }

        UpdateOrganizationCommand command = OrganizationDTOToCommandConverter.INSTANCE.toUpdateCommand(updateOrganizationRequestDTO);
        organization.update(command);

        organizationDomainRepository.save(organization);
    }

    @Override
    @Transactional
    public void deleteOrganization(Long organizationId) {
        if (organizationId == 1) {
            throw new FailedPreconditionException("can not delete root organization");
        }
        Organization organization = organizationDomainRepository.findById(organizationId);
        if (organization == null) {
            throw new NotFoundException("Organization Not Found");
        }
        if (organization.getParentId() == null) {
            throw new FailedPreconditionException("can not delete root organization");
        }
        if (organizationDomainRepository.existsChildren(organizationId)) {
            throw new BusinessException("organization has children");
        }
        organizationDomainRepository.logicDelete(organizationId);
    }

    @Override
    public List<OrganizationDTO> listOrganizations(QueryOrganizationRequestDTO queryDTO) {
        return organizationRepository.queryAll(queryDTO);
    }

    @Override
    public OrganizationDTO getOrganization(Long organizationId) {
        Organization organization = organizationDomainRepository.findById(organizationId);
        if (organization != null) {
            OrganizationDTO organizationDTO = OrganizationDTOToCommandConverter.INSTANCE.toDTO(organization);
            if (!CollectionUtils.isEmpty(organization.getParentIds())) {
                organizationDTO.setParentNames(organizationDomainRepository.findByIds(organization.getParentIds()).stream()
                                .sorted((o1, o2) -> o1.getLevel() - o2.getLevel())
                        .map(Organization::getName).toList());
            }
            return organizationDTO;
        }
        return null;
    }
}
