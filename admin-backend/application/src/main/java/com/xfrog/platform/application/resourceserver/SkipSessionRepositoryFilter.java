package com.xfrog.platform.application.resourceserver;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.core.Ordered;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.session.web.http.SessionRepositoryFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class SkipSessionRepositoryFilter extends OncePerRequestFilter implements Ordered, InitializingBean {

    private final String sessionRepositoryFilter = SessionRepositoryFilter.class.getName() + ".FILTERED";

    private final RequestMatcher apiRequestMatcher = new AntPathRequestMatcher("/api/**");

    private final ObjectProvider<SessionRepositoryFilter<?>> sessionRepositoryFilterObjectProvider;

    private boolean hasSessionRepositoryFilter = false;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (!hasSessionRepositoryFilter) {
            return;
        }
        if (apiRequestMatcher.matches(request)) {
            request.setAttribute(sessionRepositoryFilter, Boolean.TRUE);
        }
        filterChain.doFilter(request, response);
    }

    @Override
    public void afterPropertiesSet() throws ServletException {
        super.afterPropertiesSet();
        hasSessionRepositoryFilter = sessionRepositoryFilterObjectProvider.getIfAvailable() != null;
    }

    @Override
    public int getOrder() {
        return Integer.MIN_VALUE + 1;
    }
}
