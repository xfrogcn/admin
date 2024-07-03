package com.xfrog.platform.domain.permission.repository;

import com.xfrog.platform.domain.permission.aggregate.UserRole;
import com.xfrog.platform.domain.repository.DomainRepository;

import java.util.List;

public interface UserRoleDomainRepository extends DomainRepository<UserRole> {
    boolean existsByRoleId(Long roleId);

    List<UserRole> getByUserId(Long userId);

    List<UserRole> getByUserIds(List<Long> userIds);
}