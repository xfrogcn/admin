package com.xfrog.platform.application.authserver.service.impl;

import com.xfrog.framework.common.DateTimeUtils;
import com.xfrog.platform.domain.permission.repository.UserDomainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserLastLoginTimeUpdater {
    private final UserDomainRepository userRepository;
    public void updateLastLoginTime(Long userId) {
        userRepository.updateLastLoginTime(userId, DateTimeUtils.utcNow());
    }
}
