package com.xfrog.platform.application.permission.service.impl;

import com.xfrog.framework.exception.business.AlreadyExistsException;
import com.xfrog.framework.exception.business.FailedPreconditionException;
import com.xfrog.framework.exception.business.NotFoundException;
import com.xfrog.platform.application.permission.api.dto.CreatePermissionItemRequestDTO;
import com.xfrog.platform.application.permission.api.dto.PermissionItemDTO;
import com.xfrog.platform.application.permission.api.dto.UpdatePermissionItemRequestDTO;
import com.xfrog.platform.application.permission.repository.PermissionItemRepository;
import com.xfrog.platform.application.permission.service.PermissionItemService;
import com.xfrog.platform.domain.permission.aggregate.PermissionItem;
import com.xfrog.platform.domain.permission.aggregate.RolePermissionItem;
import com.xfrog.platform.domain.permission.repository.PermissionItemDomainRepository;
import com.xfrog.platform.domain.permission.repository.RolePermissionItemDomainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PermissionItemServiceImpl implements PermissionItemService {

    private final PermissionItemDomainRepository permissionItemDomainRepository;

    private final PermissionItemRepository permissionItemRepository;

    private final RolePermissionItemDomainRepository rolePermissionItemDomainRepository;

    @Override
    @Transactional
    public Long createPermissionItem(CreatePermissionItemRequestDTO permissionItemRequestDTO) {

        PermissionItem parentItem = null;
        if (permissionItemRequestDTO.getParentId() != null) {
            parentItem = permissionItemDomainRepository.findById(permissionItemRequestDTO.getParentId());
            if (parentItem == null) {
                throw new NotFoundException("parent permission item not found");
            }
        }

        if (permissionItemDomainRepository.existsByCode(permissionItemRequestDTO.getCode(), null)) {
            throw new AlreadyExistsException("permission item code already exists");
        }

        PermissionItem permissionItem = PermissionItem.builder()
                .name(permissionItemRequestDTO.getName())
                .parentId(permissionItemRequestDTO.getParentId())
                .type(permissionItemRequestDTO.getType())
                .code(permissionItemRequestDTO.getCode())
                .platform(permissionItemRequestDTO.getPlatform())
                .build();

        return permissionItemDomainRepository.save(permissionItem).getId();
    }

    @Override
    @Transactional
    public void updatePermissionItem(Long permissionItemId, UpdatePermissionItemRequestDTO updatePermissionItemRequestDTO) {
        PermissionItem permissionItem = permissionItemDomainRepository.findById(permissionItemId);
        if (permissionItem == null) {
            throw new NotFoundException("permission item not found");
        }

        permissionItem.update(updatePermissionItemRequestDTO.getName(), updatePermissionItemRequestDTO.getType());

        permissionItemDomainRepository.save(permissionItem);
    }

    @Override
    public List<PermissionItemDTO> listPermissionItems(boolean includePlatform) {
        return permissionItemRepository.queryAll(includePlatform);
    }

    @Override
    @Transactional
    public void deletePermissionItem(Long permissionItemId) {

        if (permissionItemDomainRepository.existsChildren(permissionItemId)) {
            throw new FailedPreconditionException("permission item has children, can not delete");
        }

        // 删除所有关联的角色权限
        List<RolePermissionItem> rolePermissionItems = rolePermissionItemDomainRepository.getByPermissionItemId(permissionItemId);
        rolePermissionItemDomainRepository.logicDeleteAll(rolePermissionItems);

        permissionItemDomainRepository.logicDelete(permissionItemId);
    }


}
