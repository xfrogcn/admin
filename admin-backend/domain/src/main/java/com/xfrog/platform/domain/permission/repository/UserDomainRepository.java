package com.xfrog.platform.domain.permission.repository;


import com.xfrog.platform.domain.permission.aggregate.User;
import com.xfrog.platform.domain.repository.DomainRepository;

import java.time.LocalDateTime;

public interface UserDomainRepository extends DomainRepository<User> {
    User findByUserName(String userName);

    void updateLastLoginTime(Long userId, LocalDateTime lastLoginTime);
}
