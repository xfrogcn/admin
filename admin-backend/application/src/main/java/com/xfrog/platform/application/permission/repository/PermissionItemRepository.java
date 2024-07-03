package com.xfrog.platform.application.permission.repository;


import com.xfrog.platform.application.permission.api.dto.PermissionItemDTO;

import java.util.List;

public interface PermissionItemRepository {
    List<PermissionItemDTO> queryAll(Boolean includePlatform);
}
