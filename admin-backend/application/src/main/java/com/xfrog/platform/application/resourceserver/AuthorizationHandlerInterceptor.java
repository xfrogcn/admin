package com.xfrog.platform.application.resourceserver;


import com.xfrog.framework.exception.business.PermissionDeniedException;
import com.xfrog.framework.exception.business.UnauthenticatedException;
import com.xfrog.platform.application.permission.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AuthorizationHandlerInterceptor implements HandlerInterceptor {
    private final ConcurrentHashMap<Method, HashSet<OrStringList>> methodAuthorizationMap = new ConcurrentHashMap<>();
    private final UserService userService;

     static final class OrStringList {
        private final Set<String> orStringList;

        private OrStringList(String str) {
            orStringList = Arrays.stream(str.split("\\|")).map(String::trim).collect(Collectors.toSet());
        }

        private boolean isMatch(String permissionCode) {
            return orStringList.contains(permissionCode);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            OrStringList that = (OrStringList) o;
            return Objects.equals(orStringList, that.orStringList);
        }

        @Override
        public int hashCode() {
            return Objects.hash(orStringList);
        }
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;

        Set<OrStringList> permissionCodes = methodAuthorizationMap.computeIfAbsent(handlerMethod.getMethod(), (key) -> {
            Authorization authorization = handlerMethod.getMethod().getAnnotation(Authorization.class);
            if (authorization == null) {
                return new HashSet<>();
            }
            HashSet<OrStringList> set = new HashSet<>();
            if (StringUtils.hasText(authorization.value())) {
                set.add(new OrStringList(authorization.value()));
            }
            if (authorization.permissionCodes() != null) {
                set.addAll(Arrays.stream(authorization.permissionCodes()).filter(StringUtils::hasText).map(OrStringList::new).toList());
            }
            return set;
        });

        if (permissionCodes.isEmpty()) {
            return true;
        }

        Long userId = getCurrentUserId();
        if (userId == null) {
            throw new UnauthenticatedException("Unauthenticated");
        }

        List<String> hasCodes = userService.getUserPermissionCodes(userId);
       if (!permissionCodes.stream().allMatch(codes -> hasCodes.stream().anyMatch(codes::isMatch))) {
           throw new PermissionDeniedException("Permission Denied");
       }

       return true;
    }

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken jwtAuthenticationToken) {
            Jwt jwt = (Jwt) jwtAuthenticationToken.getPrincipal();
            if (jwt != null) {
                Long userId = null;
                if (jwt.getClaim("uid") != null) {
                    userId = Long.valueOf(jwt.getClaim("uid"));
                }
                return userId;
            }
        }
        return  null;
    }
}

