package com.xfrog.platform.application.common;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import org.springframework.boot.web.servlet.filter.OrderedFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@Component
public class RequestThreadMarkFilter extends GenericFilterBean implements OrderedFilter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            RequestThreadMarkContext.clearThreadMark();
        }
    }

    @Override
    public int getOrder() {
        return Integer.MIN_VALUE;
    }
}
