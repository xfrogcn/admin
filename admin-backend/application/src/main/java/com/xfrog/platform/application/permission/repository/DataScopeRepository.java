package com.xfrog.platform.application.permission.repository;

import com.xfrog.platform.application.permission.api.dto.DataScopeDTO;
import com.xfrog.platform.domain.share.permission.DataScopeTargetType;

import java.util.List;

public interface DataScopeRepository {
    List<DataScopeDTO> findByTargetTypeAndTargetId(DataScopeTargetType targetType, List<Long> targetIds);

    void removeCacheByTargetTypeAndTargetId(DataScopeTargetType targetType, Long targetId);
}
