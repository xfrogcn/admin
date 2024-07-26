package com.xfrog.platform.infrastructure.permission.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xfrog.platform.application.permission.dto.PermissionItemDTO;
import com.xfrog.platform.application.permission.dto.RoleDTO;
import com.xfrog.platform.application.permission.repository.RoleRepository;
import com.xfrog.platform.infrastructure.permission.common.PermissionCacheNames;
import com.xfrog.platform.infrastructure.permission.converter.RolePOConverter;
import com.xfrog.platform.infrastructure.permission.dataobject.RolePO;
import com.xfrog.platform.infrastructure.permission.mapper.RoleMapper;
import com.xfrog.platform.infrastructure.persistent.repository.BaseCacheableApplicationRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RoleRepositoryImpl extends BaseCacheableApplicationRepository<RoleDTO, RolePO, RoleMapper>
        implements RoleRepository {

    public RoleRepositoryImpl(RoleMapper roleMapper) {
        super(roleMapper, RolePOConverter.INSTANCE);
    }
    @Override
    public List<RoleDTO> queryAll() {
        LambdaQueryWrapper<RolePO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper = queryWrapper.eq(RolePO::getDeleted, false);
        List<RolePO> rolePOS = mapper.selectList(queryWrapper);
        return RolePOConverter.INSTANCE.toDTOList(rolePOS);
    }


    @Override
    public List<PermissionItemDTO> queryRolePermissions(Long roleId) {
        return mapper.queryRolePermissions(roleId);
    }

    @Override
    public String getCacheName() {
        return PermissionCacheNames.ROLE_DETAIL;
    }
}
