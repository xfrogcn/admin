package com.xfrog.platform.application.permission.service.impl;

import com.xfrog.framework.common.ListComparator;
import com.xfrog.framework.exception.business.AlreadyExistsException;
import com.xfrog.framework.exception.business.FailedPreconditionException;
import com.xfrog.framework.exception.business.NotFoundException;
import com.xfrog.platform.application.permission.api.dto.CreateRoleRequestDTO;
import com.xfrog.platform.application.permission.api.dto.PermissionItemDTO;
import com.xfrog.platform.application.permission.api.dto.RoleDTO;
import com.xfrog.platform.application.permission.api.dto.UpdateRoleRequestDTO;
import com.xfrog.platform.application.permission.repository.RoleRepository;
import com.xfrog.platform.application.permission.repository.UserRepository;
import com.xfrog.platform.application.permission.service.RoleService;
import com.xfrog.platform.domain.permission.aggregate.Role;
import com.xfrog.platform.domain.permission.aggregate.RolePermissionItem;
import com.xfrog.platform.domain.permission.aggregate.UserRole;
import com.xfrog.platform.domain.permission.repository.RoleDomainRepository;
import com.xfrog.platform.domain.permission.repository.RolePermissionItemDomainRepository;
import com.xfrog.platform.domain.permission.repository.UserRoleDomainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleDomainRepository roleDomainRepository;
    private final RoleRepository roleRepository;
    private final UserRoleDomainRepository userRoleDomainRepository;
    private final RolePermissionItemDomainRepository rolePermissionItemDomainRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public Long createRole(CreateRoleRequestDTO createRoleRequestDTO) {
        if (roleDomainRepository.existsByName(createRoleRequestDTO.getName(), null)) {
            throw new AlreadyExistsException("role name already exists");
        }

        Role role = Role.builder()
                .name(createRoleRequestDTO.getName())
                .memo(createRoleRequestDTO.getMemo())
                .enabled(createRoleRequestDTO.getEnabled())
                .build();

        return roleDomainRepository.save(role).getId();
    }

    @Override
    public List<RoleDTO> listRoles() {
        return roleRepository.queryAll();
    }

    @Override
    public List<PermissionItemDTO> getRolePermissionItems(Long roleId) {
        return roleRepository.queryRolePermissions(roleId);
    }

    @Override
    @Transactional
    public void updateRole(Long roleId, UpdateRoleRequestDTO updateRoleRequestDTO) {
        Role oldRole = roleDomainRepository.findById(roleId);
        if (oldRole == null) {
            throw new NotFoundException("role not found");
        }

        if (roleDomainRepository.existsByName(updateRoleRequestDTO.getName(), List.of(roleId))) {
            throw new AlreadyExistsException("role name already exists");
        }

        oldRole.update(updateRoleRequestDTO.getName(), updateRoleRequestDTO.getMemo());

        roleDomainRepository.save(oldRole);
    }

    @Override
    @Transactional
    public void deleteRole(Long roleId) {
        if (userRoleDomainRepository.existsByRoleId(roleId)) {
            throw new FailedPreconditionException("This role has been used");
        }
        roleDomainRepository.logicDelete(roleId);
    }

    @Override
    @Transactional
    public void enableRole(Long roleId, Boolean enabled) {
        Role oldRole = roleDomainRepository.findById(roleId);
        if (oldRole == null) {
            throw new NotFoundException("role not found");
        }
        oldRole.updateEnabled(enabled);

        roleDomainRepository.save(oldRole);
    }

    @Override
    @Transactional
    public void grantPermissionItems(Long roleId, List<Long> permissionItemIds) {
        List<RolePermissionItem> rolePermissionItems = rolePermissionItemDomainRepository.getByRoleId(roleId);
        ListComparator.CompareResult<RolePermissionItem, Long> compareResult = ListComparator.compare(
                rolePermissionItems,
                permissionItemIds,
                (rolePermissionItem, permissionItemId) -> Objects.equals(rolePermissionItem.getPermissionItemId(), permissionItemId));
        // 此处不用管更新
        List<RolePermissionItem> added = compareResult.getAdded().stream()
                .map(permissionItemId -> RolePermissionItem.builder()
                        .roleId(roleId)
                        .permissionItemId(permissionItemId).build())
                .collect(Collectors.toList());
        rolePermissionItemDomainRepository.saveAll(added);
        rolePermissionItemDomainRepository.logicDeleteAll(compareResult.getRemoved());

        if (!added.isEmpty() || !compareResult.getRemoved().isEmpty()) {
            // 清除对应用户权限缓存
            List<UserRole> userRoles = userRoleDomainRepository.getByRoleId(roleId);
            if (!CollectionUtils.isEmpty(userRoles)) {
                userRoles.forEach(userRole -> {
                    userRepository.removeUserPermissionCodesCache(userRole.getUserId());
                });
            }
        }
    }
}
