package com.xfrog.platform.infrastructure.permission.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xfrog.platform.application.permission.dto.PermissionItemDTO;
import com.xfrog.platform.application.permission.repository.PermissionItemRepository;
import com.xfrog.platform.infrastructure.permission.converter.PermissionItemPOConverter;
import com.xfrog.platform.infrastructure.permission.dataobject.PermissionItemPO;
import com.xfrog.platform.infrastructure.permission.mapper.PermissionItemMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PermissionItemRepositoryImpl implements PermissionItemRepository {
    private final PermissionItemMapper permissionItemMapper;
    @Override
    public List<PermissionItemDTO> queryAll(Boolean includePlatform) {
        LambdaQueryWrapper<PermissionItemPO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PermissionItemPO::getDeleted, false);
        if (Boolean.FALSE.equals(includePlatform)) {
            queryWrapper.eq(PermissionItemPO::getPlatform, false);
        }
        List<PermissionItemPO> permissionItemPOS = permissionItemMapper.selectList(queryWrapper);
        return PermissionItemPOConverter.INSTANCE.toDTOList(permissionItemPOS);
    }
}
