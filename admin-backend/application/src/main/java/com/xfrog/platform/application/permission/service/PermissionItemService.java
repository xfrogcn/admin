package com.xfrog.platform.application.permission.service;


import com.xfrog.platform.application.permission.dto.CreatePermissionItemRequestDTO;
import com.xfrog.platform.application.permission.dto.PermissionItemDTO;
import com.xfrog.platform.application.permission.dto.UpdatePermissionItemRequestDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PermissionItemService {

    /**
     * 创建权限项
     * @param permissionItemRequestDTO 权限项创建请求数据传输对象
     * @return 创建后的权限项ID
     */
    Long createPermissionItem(CreatePermissionItemRequestDTO permissionItemRequestDTO);

    /**
     * 更新权限项
     * @param permissionItemId 权限项ID
     * @param permissionItem 更新的权限项数据传输对象
     */
    void updatePermissionItem(Long permissionItemId, UpdatePermissionItemRequestDTO permissionItem);

    /**
     * 列出所有权限项
     * @return 权限项列表
     */
    List<PermissionItemDTO> listPermissionItems(boolean includePlatform);

    /**
     * 删除权限项
     * @param permissionItemId 权限项ID
     */
    void deletePermissionItem(Long permissionItemId);
}
