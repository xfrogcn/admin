package com.xfrog.platform.infrastructure.permission.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xfrog.platform.application.permission.api.dto.PermissionItemDTO;
import com.xfrog.platform.application.permission.api.dto.RoleDTO;
import com.xfrog.platform.application.permission.repository.RoleRepository;
import com.xfrog.platform.infrastructure.permission.converter.RolePOConverter;
import com.xfrog.platform.infrastructure.permission.dataobject.RolePO;
import com.xfrog.platform.infrastructure.permission.mapper.RoleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class RoleRepositoryImpl implements RoleRepository {
    private final RoleMapper roleMapper;
    @Override
    public List<RoleDTO> queryAll() {
        LambdaQueryWrapper<RolePO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper = queryWrapper.eq(RolePO::getDeleted, false);
        List<RolePO> rolePOS = roleMapper.selectList(queryWrapper);
        return RolePOConverter.INSTANCE.toDTOList(rolePOS);
    }

    @Override
    public List<RoleDTO> queryByIds(List<Long> roleIds) {
        if (CollectionUtils.isEmpty(roleIds)) {
            return new ArrayList<>();
        }
        LambdaQueryWrapper<RolePO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RolePO::getDeleted, false);
        queryWrapper.in(RolePO::getId, roleIds);
        List<RolePO> rolePOS = roleMapper.selectList(queryWrapper);
        return RolePOConverter.INSTANCE.toDTOList(rolePOS);
    }

    @Override
    public List<PermissionItemDTO> queryRolePermissions(Long roleId) {
        return roleMapper.queryRolePermissions(roleId);
    }
}
