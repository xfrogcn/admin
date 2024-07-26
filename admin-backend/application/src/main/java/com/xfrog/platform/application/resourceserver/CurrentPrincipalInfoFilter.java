package com.xfrog.platform.application.resourceserver;


import com.xfrog.framework.principal.CurrentPrincipalContext;
import com.xfrog.framework.principal.DataPermissionItem;
import com.xfrog.framework.principal.PrincipalInfo;
import com.xfrog.platform.application.authserver.dto.UserDetailsDTO;
import com.xfrog.platform.application.permission.dto.DataScopeDTO;
import com.xfrog.platform.application.permission.service.DataScopeService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CurrentPrincipalInfoFilter extends OncePerRequestFilter {

    private final DataScopeService dataScopeService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken jwtAuthenticationToken) {
            setCurrentPrincipalInfo(jwtAuthenticationToken);
        } else if (authentication instanceof UsernamePasswordAuthenticationToken usernameToken) {
            UserDetailsDTO principal = (UserDetailsDTO) usernameToken.getPrincipal();
            if (principal != null) {
                PrincipalInfo principalInfo = PrincipalInfo.create(principal.getUserId(), principal.getUsername(), principal.getOrganizationId(), null, principal.getTenantId());
                List<DataScopeDTO> dataScopes = dataScopeService.getUserDataScopes(principalInfo.getUserId());
                principalInfo.setDataPermission(dataScopes.stream().map(it -> DataPermissionItem.of(it.getScopeType().name(), it.getScopeId())).toList());
                CurrentPrincipalContext.setCurrentPrincipal(principalInfo);
            }
        }

        filterChain.doFilter(request, response);

        CurrentPrincipalContext.clearCurrentPrincipal();
    }

    private void setCurrentPrincipalInfo(JwtAuthenticationToken jwtAuthenticationToken) {
        Jwt jwt = (Jwt) jwtAuthenticationToken.getPrincipal();
        if (jwt != null) {
            Long userId = null;
            Long orgId =  null;
            String userName = null;
            String clientId=  null;
            String tenantId = null;
            if (jwt.getClaim("uid") != null) {
                userId = Long.valueOf(jwt.getClaim("uid"));
            }
            if (jwt.getClaim("org") != null) {
                orgId = Long.valueOf(jwt.getClaim("org"));
            }
            if (jwt.getClaim("sub") != null) {
                userName = jwt.getClaim("sub");
            }
            if (jwt.getClaim("azp") != null) {
                clientId = jwt.getClaim("azp");
            }
            if (jwt.getClaim("tid") != null) {
                tenantId = jwt.getClaim("tid");
            }

            PrincipalInfo principalInfo = PrincipalInfo.create(userId, userName, orgId, clientId, tenantId);
            List<DataScopeDTO> dataScopes = dataScopeService.getUserDataScopes(principalInfo.getUserId());
            principalInfo.setDataPermission(dataScopes.stream().map(it -> DataPermissionItem.of(it.getScopeType().name(), it.getScopeId())).toList());

            CurrentPrincipalContext.setCurrentPrincipal(principalInfo);
        }
    }
}
