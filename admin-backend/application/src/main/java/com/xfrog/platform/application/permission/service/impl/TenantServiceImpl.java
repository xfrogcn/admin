package com.xfrog.platform.application.permission.service.impl;

import com.xfrog.framework.dto.PageDTO;
import com.xfrog.framework.exception.business.AlreadyExistsException;
import com.xfrog.framework.exception.business.FailedPreconditionException;
import com.xfrog.framework.exception.business.NotFoundException;
import com.xfrog.framework.oplog.OpLogMDC;
import com.xfrog.platform.application.common.RequestThreadMarkContext;
import com.xfrog.platform.application.permission.dto.CreateOrganizationRequestDTO;
import com.xfrog.platform.application.permission.dto.CreateRoleRequestDTO;
import com.xfrog.platform.application.permission.dto.CreateTenantRequestDTO;
import com.xfrog.platform.application.permission.dto.CreateUserRequestDTO;
import com.xfrog.platform.application.permission.dto.GrantDataScopeRequestDTO;
import com.xfrog.platform.application.permission.dto.PermissionItemDTO;
import com.xfrog.platform.application.permission.dto.QueryTenantRequestDTO;
import com.xfrog.platform.application.permission.dto.TenantDTO;
import com.xfrog.platform.application.permission.dto.UpdateTenantRequestDTO;
import com.xfrog.platform.application.permission.converter.TenantDTOConverter;
import com.xfrog.platform.application.permission.repository.TenantRepository;
import com.xfrog.platform.application.permission.service.DataScopeService;
import com.xfrog.platform.application.permission.service.OrganizationService;
import com.xfrog.platform.application.permission.service.PermissionItemService;
import com.xfrog.platform.application.permission.service.RoleService;
import com.xfrog.platform.application.permission.service.TenantService;
import com.xfrog.platform.application.permission.service.UserService;
import com.xfrog.platform.domain.permission.aggregate.Tenant;
import com.xfrog.platform.domain.permission.aggregate.User;
import com.xfrog.platform.domain.permission.command.CreateTenantCommand;
import com.xfrog.platform.domain.permission.repository.TenantDomainRepository;
import com.xfrog.platform.domain.permission.repository.UserDomainRepository;
import com.xfrog.platform.domain.share.permission.DataScopeTargetType;
import com.xfrog.platform.domain.share.permission.DataScopeType;
import com.xfrog.platform.domain.share.permission.OrganizationStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class TenantServiceImpl implements TenantService {

    private final TenantDomainRepository tenantDomainRepository;
    private final UserDomainRepository userDomainRepository;
    private final TenantRepository tenantRepository;

    private final OrganizationService organizationService;
    private final UserService userService;
    private final PermissionItemService permissionItemService;
    private final RoleService roleService;
    private final DataScopeService dataScopeService;

    @Override
    @Transactional
    public Long createTenant(CreateTenantRequestDTO tenantDTO) {

        Tenant tenant = tenantDomainRepository.findByCode(tenantDTO.getCode());
        if (tenant != null) {
            throw new AlreadyExistsException("tenant already exists");
        }

        RequestThreadMarkContext.threadMark().setMockTenantId(tenantDTO.getCode());
        User user = userDomainRepository.findByUserName(tenantDTO.getAdminUserName());
        if (user != null) {
            throw new FailedPreconditionException("admin user name already exists");
        }

        CreateTenantCommand createTenantCommand = TenantDTOConverter.INSTANCE.toCreateCommand(tenantDTO);
        // 创建组织
        CreateOrganizationRequestDTO createOrganizationRequestDTO = CreateOrganizationRequestDTO.builder()
                .displayOrder(1)
                .name(tenantDTO.getName())
                .parentId(null)
                .status(OrganizationStatus.NORMAL)
                .build();
        createTenantCommand.setOrganizationId(organizationService.createOrganization(createOrganizationRequestDTO));

        // 创建用户
        CreateUserRequestDTO createUserRequestDTO = CreateUserRequestDTO.builder()
                .userName(tenantDTO.getAdminUserName())
                .name("管理员")
                .organizationId(createTenantCommand.getOrganizationId())
                .password("123456")
                .enabled(true)
                .build();
        createTenantCommand.setAdminUserId(userService.createUser(createUserRequestDTO));

        // 创建管理员角色
        CreateRoleRequestDTO createRoleRequestDTO = CreateRoleRequestDTO.builder()
                .enabled(true)
                .name("管理员")
                .build();
        Long roleId = roleService.createRole(createRoleRequestDTO);

        // 角色赋权
        List<PermissionItemDTO> permissionItems = permissionItemService.listPermissionItems(false);
        roleService.grantPermissionItems(roleId, permissionItems.stream().map(PermissionItemDTO::getId).toList());

        // 角色数据权限
        GrantDataScopeRequestDTO grantDataScopeRequestDTO = GrantDataScopeRequestDTO.builder()
                .targetId(roleId)
                .targetType(DataScopeTargetType.ROLE)
                .scopeItems(List.of(GrantDataScopeRequestDTO.DataScopeItem.builder()
                                .scopeId(0L)
                                .scopeType(DataScopeType.USER_ORGANIZATION)
                        .build()))
                .build();
        dataScopeService.grantDataScope(grantDataScopeRequestDTO);

        // 用户赋权
        userService.grantRoles(createTenantCommand.getAdminUserId(), List.of(roleId));

        // 创建租户
        tenant = Tenant.create(createTenantCommand);
        tenant = tenantDomainRepository.save(tenant);

        return tenant.getId();
    }

    @Override
    public PageDTO<TenantDTO> listTenants(QueryTenantRequestDTO requestDTO) {
        return tenantRepository.queryBy(requestDTO);
    }

    @Override
    public void updateTenant(Long tenantId, UpdateTenantRequestDTO tenantDTO) {
        Tenant tenant = tenantDomainRepository.findById(tenantId);
        if (tenant == null) {
            throw new NotFoundException("tenant not found");
        }
        OpLogMDC.putBizCode(tenant.getCode());

        tenant.update(tenantDTO.getName(), tenantDTO.getMemo());

        tenantDomainRepository.save(tenant);
        tenantRepository.removeCache(tenantId);
        tenantRepository.removeCacheByCode(tenant.getCode());
    }

    @Override
    public void enableTenant(Long tenantId, Boolean enabled) {
        Tenant tenant = tenantDomainRepository.findById(tenantId);
        if (tenant == null) {
            throw new NotFoundException("tenant not found");
        }
        OpLogMDC.putBizCode(tenant.getCode());

        tenant.updateEnabled(enabled);

        tenantDomainRepository.save(tenant);
        tenantRepository.removeCache(tenantId);
        tenantRepository.removeCacheByCode(tenant.getCode());
    }
}
