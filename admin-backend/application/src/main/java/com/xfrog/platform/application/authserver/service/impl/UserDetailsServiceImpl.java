package com.xfrog.platform.application.authserver.service.impl;

import com.xfrog.platform.application.authserver.dto.UserDetailsDTO;
import com.xfrog.platform.application.common.RequestThreadMarkContext;
import com.xfrog.platform.domain.permission.aggregate.Tenant;
import com.xfrog.platform.domain.permission.aggregate.User;
import com.xfrog.platform.domain.permission.repository.TenantDomainRepository;
import com.xfrog.platform.domain.permission.repository.UserDomainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserDomainRepository userDomainRepository;
    private final TenantDomainRepository tenantDomainRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        RequestThreadMarkContext.threadMark().setIgnoreTenant(true);
        User user = userDomainRepository.findByUserName(username);

        if (user == null) {
            return null;
        }

        Tenant tenant = tenantDomainRepository.findByCode(user.getTenantId());
        return UserDetailsDTO.builder()
                .userId(user.getId())
                .organizationId(user.getOrganizationId())
                .username(user.getUserName())
                .enabled(tenant != null && Boolean.TRUE.equals(tenant.getEnabled()) && user.isEnabled())
                .accountNonExpired(user.isAccountNonExpired())
                .credentialsNonExpired(user.isCredentialsNonExpired())
                .accountNonLocked(user.isAccountNonLocked())
                .password(user.getPassword())
                .tenantId(user.getTenantId())
                .build();
    }
}
